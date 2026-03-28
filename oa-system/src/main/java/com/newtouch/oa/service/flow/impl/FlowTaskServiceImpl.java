package com.newtouch.oa.service.flow.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newtouch.common.core.exception.BusinessException;
import com.newtouch.oa.entity.flow.FlowOpinion;
import com.newtouch.oa.entity.flow.FlowTask;
import com.newtouch.oa.entity.flow.ProcessInstance;
import com.newtouch.oa.mapper.flow.FlowTaskMapper;
import com.newtouch.oa.service.flow.IFlowOpinionService;
import com.newtouch.oa.service.flow.IFlowTaskService;
import com.newtouch.oa.service.flow.IProcessInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 流程任务 Service 实现类（完整审批逻辑）
 */
@Slf4j
@Service
public class FlowTaskServiceImpl extends ServiceImpl<FlowTaskMapper, FlowTask> implements IFlowTaskService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IProcessInstanceService processInstanceService;

    @Autowired
    private IFlowOpinionService flowOpinionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<FlowTask> getTodoTasks(String userId) {
        return baseMapper.selectTodoTasks(userId);
    }

    @Override
    public List<FlowTask> getDoneTasks(String userId) {
        return baseMapper.selectDoneTasks(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(String taskId, Integer action, String opinion, Map<String, Object> variables) {
        // 1. 查询任务
        FlowTask flowTask = getById(taskId);
        if (flowTask == null) {
            throw new BusinessException("任务不存在");
        }

        if (flowTask.getStatus() != 1) {
            throw new BusinessException("任务已办理，不能重复审批");
        }

        // 2. 查询 Flowable 任务
        Task task = taskService.createTaskQuery()
                .taskId(flowTask.getFlowableTaskId())
                .singleResult();

        if (task == null) {
            throw new BusinessException("Flowable 任务不存在");
        }

        // 3. 构建流程变量
        if (variables == null) {
            variables = new HashMap<>();
        }
        variables.put("approveUserId", flowTask.getAssignee());
        variables.put("approveUserName", flowTask.getAssigneeName());
        variables.put("approveTime", new Date());
        variables.put("approveAction", action);
        variables.put("approveOpinion", opinion);

        // 4. 完成任务
        taskService.complete(task.getId(), variables);

        // 5. 更新任务状态
        flowTask.setStatus(2); // 已办理
        flowTask.setFinishTime(new Date());
        flowTask.setDuration(System.currentTimeMillis() - flowTask.getCreateTime().getTime());
        updateById(flowTask);

        // 6. 保存审批意见
        saveFlowOpinion(flowTask, action, opinion);

        // 7. 更新流程实例状态
        updateProcessInstance(task.getProcessInstanceId());

        log.info("任务审批完成：taskId={}, action={}, opinion={}", taskId, action, opinion);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(String taskId, String opinion) {
        // 1. 查询任务
        FlowTask flowTask = getById(taskId);
        if (flowTask == null) {
            throw new BusinessException("任务不存在");
        }

        // 2. 查询 Flowable 任务
        Task task = taskService.createTaskQuery()
                .taskId(flowTask.getFlowableTaskId())
                .singleResult();

        if (task == null) {
            throw new BusinessException("Flowable 任务不存在");
        }

        // 3. 驳回任务（回退到发起人）
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(task.getProcessInstanceId())
                .moveActivityIdTo(task.getTaskDefinitionKey(), getStartActivityId(task.getProcessInstanceId()))
                .changeState();

        // 4. 更新任务状态
        flowTask.setStatus(3); // 已驳回
        flowTask.setFinishTime(new Date());
        updateById(flowTask);

        // 5. 保存审批意见
        saveFlowOpinion(flowTask, 2, opinion);

        // 6. 更新流程实例状态
        updateProcessInstance(task.getProcessInstanceId());

        log.info("任务驳回完成：taskId={}, opinion={}", taskId, opinion);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(String taskId, String targetUserId, String opinion) {
        // 1. 查询任务
        FlowTask flowTask = getById(taskId);
        if (flowTask == null) {
            throw new BusinessException("任务不存在");
        }

        // 2. 查询 Flowable 任务
        Task task = taskService.createTaskQuery()
                .taskId(flowTask.getFlowableTaskId())
                .singleResult();

        if (task == null) {
            throw new BusinessException("Flowable 任务不存在");
        }

        // 3. 转办任务
        taskService.setAssignee(task.getId(), targetUserId);

        // 4. 更新任务状态
        flowTask.setStatus(4); // 已转办
        updateById(flowTask);

        // 5. 创建新任务记录
        FlowTask newTask = new FlowTask();
        newTask.setId(UUID.randomUUID().toString().replace("-", ""));
        newTask.setTaskName(task.getName());
        newTask.setFlowableTaskId(task.getId());
        newTask.setProcessInstanceId(task.getProcessInstanceId());
        newTask.setProcessDefId(task.getProcessDefinitionId());
        newTask.setActivityId(task.getTaskDefinitionKey());
        newTask.setActivityType("userTask");
        newTask.setAssignee(targetUserId);
        newTask.setAssigneeName("待获取"); // TODO: 从用户服务获取
        newTask.setStatus(1); // 待办理
        newTask.setIsRead(0);
        newTask.setPriority(task.getPriority());
        newTask.setCreateTime(new Date());
        save(newTask);

        log.info("任务转办完成：taskId={}, targetUserId={}", taskId, targetUserId);
    }

    /**
     * 保存审批意见
     */
    private void saveFlowOpinion(FlowTask flowTask, Integer action, String opinion) {
        FlowOpinion flowOpinion = new FlowOpinion();
        flowOpinion.setId(UUID.randomUUID().toString().replace("-", ""));
        flowOpinion.setProcessInstanceId(flowTask.getProcessInstanceId());
        flowOpinion.setTaskId(flowTask.getId());
        flowOpinion.setUserId(flowTask.getAssignee());
        flowOpinion.setUserName(flowTask.getAssigneeName());
        flowOpinion.setAction(action);
        flowOpinion.setOpinion(opinion);
        flowOpinion.setDuration(flowTask.getDuration());
        flowOpinion.setApproveTime(new Date());
        flowOpinion.setCreateTime(new Date());
        flowOpinionService.save(flowOpinion);
    }

    /**
     * 更新流程实例状态
     */
    private void updateProcessInstance(String flowableInstanceId) {
        // 查询流程实例是否还有未完成的任务
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(flowableInstanceId)
                .list();

        ProcessInstance processInstance = processInstanceService.getByFlowableInstanceId(flowableInstanceId);
        if (processInstance == null) {
            return;
        }

        if (tasks.isEmpty()) {
            // 没有任务了，流程已结束
            HistoricProcessInstance historicInstance = historyService
                    .createHistoricProcessInstanceQuery()
                    .processInstanceId(flowableInstanceId)
                    .singleResult();

            if (historicInstance != null && historicInstance.getEndTime() != null) {
                processInstance.setStatus(2); // 已完成
                processInstance.setEndTime(historicInstance.getEndTime());
                processInstance.setDuration(historicInstance.getDurationInMillis());
            }
        } else {
            // 更新当前节点信息
            Task currentTask = tasks.get(0);
            processInstance.setCurrentActivityId(currentTask.getTaskDefinitionKey());
            processInstance.setCurrentActivityName(currentTask.getName());
        }

        processInstance.setUpdateTime(new Date());
        processInstanceService.updateById(processInstance);
    }

    /**
     * 获取流程的开始节点 ID
     */
    private String getStartActivityId(String processInstanceId) {
        HistoricProcessInstance historicInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (historicInstance != null) {
            return historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .activityType("startEvent")
                    .orderByHistoricActivityInstanceStartTime()
                    .asc()
                    .list()
                    .get(0)
                    .getActivityId();
        }

        return null;
    }
}
