package com.newtouch.oa.service.flow.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newtouch.common.core.exception.BusinessException;
import com.newtouch.oa.entity.flow.ProcessDefinition;
import com.newtouch.oa.entity.flow.ProcessInstance;
import com.newtouch.oa.mapper.flow.ProcessInstanceMapper;
import com.newtouch.oa.service.flow.IProcessDefinitionService;
import com.newtouch.oa.service.flow.IProcessInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 流程实例 Service 实现类
 */
@Slf4j
@Service
public class ProcessInstanceServiceImpl extends ServiceImpl<ProcessInstanceMapper, ProcessInstance> implements IProcessInstanceService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IProcessDefinitionService processDefinitionService;

    @Autowired
    private ObjectMapper objectMapper;

    // 默认超时时间（小时）
    private static final int DEFAULT_TIMEOUT_HOURS = 48;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProcessInstance startProcess(String processDefId, String businessKey,
                                        Map<String, Object> variables, String formData,
                                        String userId, String userName) {
        // 1. 查询流程定义
        ProcessDefinition processDef = processDefinitionService.getById(processDefId);
        if (processDef == null) {
            throw new BusinessException("流程定义不存在");
        }

        if (processDef.getStatus() != 2) {
            throw new BusinessException("流程定义未发布，不能发起");
        }

        if (variables == null) {
            variables = new HashMap<>();
        }
        variables.put("startUserId", userId);
        variables.put("startUserName", userName);
        variables.put("startTime", new Date());
        variables.put("formData", formData);

        String processInstanceName = processDef.getName() + "-" + businessKey;
        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder()
                .processDefinitionId(processDef.getFlowableDefId())
                .businessKey(businessKey)
                .name(processInstanceName)
                .variables(variables);

        org.flowable.engine.runtime.ProcessInstance flowableInstance = processInstanceBuilder.start();

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setId(UUID.randomUUID().toString().replace("-", ""));
        processInstance.setProcessDefId(processDefId);
        processInstance.setInstanceName(processInstanceName);
        processInstance.setFlowableInstanceId(flowableInstance.getId());
        processInstance.setBusinessKey(businessKey);
        processInstance.setStartUserId(userId);
        processInstance.setStartUserName(userName);
        processInstance.setStatus(1);
        processInstance.setVariables(objectMapper.valueToTree(variables).toString());
        processInstance.setFormData(formData);
        processInstance.setStartTime(new Date());
        processInstance.setCreateTime(new Date());
        processInstance.setUpdateTime(new Date());

        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(flowableInstance.getId())
                .list();

        if (!tasks.isEmpty()) {
            Task task = tasks.get(0);
            processInstance.setCurrentActivityId(task.getTaskDefinitionKey());
            processInstance.setCurrentActivityName(task.getName());
        }

        save(processInstance);

        log.info("流程发起成功：processInstanceId={}, flowableInstanceId={}, businessKey={}",
                processInstance.getId(), flowableInstance.getId(), businessKey);

        return processInstance;
    }

    @Override
    public ProcessInstance getByFlowableInstanceId(String flowableInstanceId) {
        return getBaseMapper().selectByFlowableInstanceId(flowableInstanceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void terminateProcess(String processInstanceId, String reason) {
        ProcessInstance processInstance = getById(processInstanceId);
        if (processInstance == null) {
            throw new BusinessException("流程实例不存在");
        }

        runtimeService.deleteProcessInstance(processInstance.getFlowableInstanceId(), reason);

        processInstance.setStatus(3);
        processInstance.setEndTime(new Date());
        updateById(processInstance);

        log.info("流程终止成功：processInstanceId={}, reason={}", processInstanceId, reason);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void suspendProcess(String processInstanceId) {
        ProcessInstance processInstance = getById(processInstanceId);
        if (processInstance == null) {
            throw new BusinessException("流程实例不存在");
        }

        runtimeService.suspendProcessInstanceById(processInstance.getFlowableInstanceId());

        log.info("流程挂起成功：processInstanceId={}", processInstanceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activateProcess(String processInstanceId) {
        ProcessInstance processInstance = getById(processInstanceId);
        if (processInstance == null) {
            throw new BusinessException("流程实例不存在");
        }

        runtimeService.activateProcessInstanceById(processInstance.getFlowableInstanceId());

        log.info("流程激活成功：processInstanceId={}", processInstanceId);
    }

    @Override
    public List<ProcessInstance> getRunningProcesses(String userId) {
        LambdaQueryWrapper<ProcessInstance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessInstance::getStatus, 1)
                .eq(ProcessInstance::getStartUserId, userId)
                .orderByDesc(ProcessInstance::getStartTime);
        return list(wrapper);
    }

    @Override
    public List<ProcessInstance> getCompletedProcesses(String userId) {
        LambdaQueryWrapper<ProcessInstance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessInstance::getStatus, 2)
                .eq(ProcessInstance::getStartUserId, userId)
                .orderByDesc(ProcessInstance::getEndTime);
        return list(wrapper);
    }

    @Override
    public Map<String, Object> getProcessStatistics(String userId) {
        Map<String, Object> statistics = new HashMap<>();

        LambdaQueryWrapper<ProcessInstance> runningWrapper = new LambdaQueryWrapper<>();
        runningWrapper.eq(ProcessInstance::getStatus, 1)
                .eq(ProcessInstance::getStartUserId, userId);
        long runningCount = count(runningWrapper);

        LambdaQueryWrapper<ProcessInstance> completedWrapper = new LambdaQueryWrapper<>();
        completedWrapper.eq(ProcessInstance::getStatus, 2)
                .eq(ProcessInstance::getStartUserId, userId);
        long completedCount = count(completedWrapper);

        LambdaQueryWrapper<ProcessInstance> terminatedWrapper = new LambdaQueryWrapper<>();
        terminatedWrapper.eq(ProcessInstance::getStatus, 3)
                .eq(ProcessInstance::getStartUserId, userId);
        long terminatedCount = count(terminatedWrapper);

        statistics.put("runningCount", runningCount);
        statistics.put("completedCount", completedCount);
        statistics.put("terminatedCount", terminatedCount);
        statistics.put("totalCount", runningCount + completedCount + terminatedCount);

        return statistics;
    }

    @Override
    public Map<String, Object> getProcessDurationStatistics(String processInstanceId) {
        Map<String, Object> durationStats = new HashMap<>();

        ProcessInstance processInstance = getById(processInstanceId);
        if (processInstance == null) {
            return durationStats;
        }

        HistoricProcessInstance historicInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstance.getFlowableInstanceId())
                .singleResult();

        if (historicInstance != null) {
            durationStats.put("durationMillis", historicInstance.getDurationInMillis());
            durationStats.put("durationSeconds", historicInstance.getDurationInMillis() / 1000);
            durationStats.put("durationMinutes", historicInstance.getDurationInMillis() / 60000);

            if (historicInstance.getStartTime() != null) {
                durationStats.put("startTime", historicInstance.getStartTime());
            }
            if (historicInstance.getEndTime() != null) {
                durationStats.put("endTime", historicInstance.getEndTime());
            }
        }

        return durationStats;
    }

    @Override
    public List<Map<String, Object>> getProcessActivityStatistics(String processInstanceId) {
        List<Map<String, Object>> activityStats = new ArrayList<>();

        var activityInstances = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .orderByHistoricActivityInstanceEndTime()
                .asc()
                .list();

        for (var activity : activityInstances) {
            Map<String, Object> stats = new HashMap<>();
            stats.put("activityId", activity.getActivityId());
            stats.put("activityName", activity.getActivityName());
            stats.put("activityType", activity.getActivityType());
            stats.put("assignee", activity.getAssignee());

            if (activity.getStartTime() != null) {
                stats.put("startTime", activity.getStartTime());
            }
            if (activity.getEndTime() != null) {
                stats.put("endTime", activity.getEndTime());
            }
            if (activity.getDurationInMillis() != null) {
                stats.put("durationMillis", activity.getDurationInMillis());
            }

            activityStats.add(stats);
        }

        return activityStats;
    }

    @Override
    public Map<String, Object> getOverallStatistics() {
        Map<String, Object> overallStats = new HashMap<>();

        long totalCount = count();

        LambdaQueryWrapper<ProcessInstance> runningWrapper = new LambdaQueryWrapper<>();
        runningWrapper.eq(ProcessInstance::getStatus, 1);
        long runningCount = count(runningWrapper);

        LambdaQueryWrapper<ProcessInstance> completedWrapper = new LambdaQueryWrapper<>();
        completedWrapper.eq(ProcessInstance::getStatus, 2);
        long completedCount = count(completedWrapper);

        LambdaQueryWrapper<ProcessInstance> terminatedWrapper = new LambdaQueryWrapper<>();
        terminatedWrapper.eq(ProcessInstance::getStatus, 3);
        long terminatedCount = count(terminatedWrapper);

        overallStats.put("totalCount", totalCount);
        overallStats.put("runningCount", runningCount);
        overallStats.put("completedCount", completedCount);
        overallStats.put("terminatedCount", terminatedCount);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        Date sevenDaysAgo = calendar.getTime();

        LambdaQueryWrapper<ProcessInstance> recentWrapper = new LambdaQueryWrapper<>();
        recentWrapper.ge(ProcessInstance::getStartTime, sevenDaysAgo);
        long recentCount = count(recentWrapper);
        overallStats.put("last7DaysCount", recentCount);

        return overallStats;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void urgeProcess(String processInstanceId, String urgeUserId, String urgeMessage) {
        ProcessInstance processInstance = getById(processInstanceId);
        if (processInstance == null) {
            throw new BusinessException("流程实例不存在");
        }

        if (processInstance.getStatus() != 1) {
            throw new BusinessException("流程已结束，无法催办");
        }

        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstance.getFlowableInstanceId())
                .list();

        if (tasks.isEmpty()) {
            throw new BusinessException("当前无待办任务，无法催办");
        }

        for (Task task : tasks) {
            String assignee = task.getAssignee();
            if (assignee != null && !assignee.equals(urgeUserId)) {
                log.info("发送催办通知：taskId={}, assignee={}, message={}",
                        task.getId(), assignee, urgeMessage);

                runtimeService.setVariable(processInstance.getFlowableInstanceId(),
                        "urgeMessage", urgeMessage);
                runtimeService.setVariable(processInstance.getFlowableInstanceId(),
                        "urgeTime", new Date());
                runtimeService.setVariable(processInstance.getFlowableInstanceId(),
                        "urgeUserId", urgeUserId);

                sendUrgentNotification(task, processInstance, urgeMessage);
            }
        }

        log.info("流程催办完成：processInstanceId={}, urgeUserId={}", processInstanceId, urgeUserId);
    }

    @Override
    @Scheduled(cron = "0 0/30 * * * ?")
    public void checkProcessTimeoutAndRemind() {
        log.info("开始执行流程超时检查任务");

        List<Map<String, Object>> timeoutProcesses = getTimeoutProcesses(DEFAULT_TIMEOUT_HOURS);

        for (Map<String, Object> timeoutProcess : timeoutProcesses) {
            String processInstanceId = (String) timeoutProcess.get("processInstanceId");
            String currentActivityName = (String) timeoutProcess.get("currentActivityName");
            String assignee = (String) timeoutProcess.get("assignee");
            Long timeoutHours = (Long) timeoutProcess.get("timeoutHours");

            log.warn("发现超时流程：processInstanceId={}, 当前节点={}, 处理人={}, 超时{}小时",
                    processInstanceId, currentActivityName, assignee, timeoutHours);

            sendTimeoutReminder(processInstanceId, currentActivityName, assignee, timeoutHours);
        }

        log.info("流程超时检查完成，共发现{}个超时流程", timeoutProcesses.size());
    }

    @Override
    public List<Map<String, Object>> getTimeoutProcesses(int timeoutHours) {
        List<Map<String, Object>> timeoutProcesses = new ArrayList<>();

        LambdaQueryWrapper<ProcessInstance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessInstance::getStatus, 1);

        List<ProcessInstance> runningProcesses = list(wrapper);
        LocalDateTime now = LocalDateTime.now();

        for (ProcessInstance processInstance : runningProcesses) {
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getFlowableInstanceId())
                    .list();

            for (Task task : tasks) {
                Date createTime = task.getCreateTime();
                if (createTime != null) {
                    LocalDateTime taskCreateTime = LocalDateTime.ofInstant(
                            createTime.toInstant(), ZoneId.systemDefault());

                    long hours = Duration.between(taskCreateTime, now).toHours();

                    if (hours >= timeoutHours) {
                        Map<String, Object> timeoutInfo = new HashMap<>();
                        timeoutInfo.put("processInstanceId", processInstance.getId());
                        timeoutInfo.put("instanceName", processInstance.getInstanceName());
                        timeoutInfo.put("businessKey", processInstance.getBusinessKey());
                        timeoutInfo.put("currentActivityId", task.getTaskDefinitionKey());
                        timeoutInfo.put("currentActivityName", task.getName());
                        timeoutInfo.put("assignee", task.getAssignee());
                        timeoutInfo.put("assigneeName", "");
                        timeoutInfo.put("createTime", createTime);
                        timeoutInfo.put("timeoutHours", hours);
                        timeoutInfo.put("startTime", processInstance.getStartTime());

                        timeoutProcesses.add(timeoutInfo);
                    }
                }
            }
        }

        return timeoutProcesses;
    }

    private void sendUrgentNotification(Task task, ProcessInstance processInstance, String message) {
        String assignee = task.getAssignee();
        if (assignee == null || assignee.isEmpty()) {
            return;
        }

        Map<String, Object> notification = new HashMap<>();
        notification.put("type", "URGENT");
        notification.put("taskId", task.getId());
        notification.put("taskName", task.getName());
        notification.put("processInstanceId", processInstance.getId());
        notification.put("instanceName", processInstance.getInstanceName());
        notification.put("assignee", assignee);
        notification.put("message", message);
        notification.put("time", new Date());

        log.info("催办通知：{}", objectMapper.valueToTree(notification).toString());
    }

    private void sendTimeoutReminder(String processInstanceId, String activityName,
                                     String assignee, Long timeoutHours) {
        if (assignee == null || assignee.isEmpty()) {
            return;
        }

        String reminderMessage = String.format(
                "【流程超时提醒】您有一个待办任务已超时%d小时，当前节点：%s，请及时处理！",
                timeoutHours, activityName);

        Map<String, Object> notification = new HashMap<>();
        notification.put("type", "TIMEOUT");
        notification.put("processInstanceId", processInstanceId);
        notification.put("activityName", activityName);
        notification.put("assignee", assignee);
        notification.put("message", reminderMessage);
        notification.put("timeoutHours", timeoutHours);
        notification.put("time", new Date());

        log.warn("超时提醒：{}", objectMapper.valueToTree(notification).toString());
    }
}
