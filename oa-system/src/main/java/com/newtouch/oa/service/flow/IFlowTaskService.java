package com.newtouch.oa.service.flow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newtouch.oa.entity.flow.FlowTask;

import java.util.List;
import java.util.Map;

/**
 * 流程任务 Service 接口
 */
public interface IFlowTaskService extends IService<FlowTask> {

    /**
     * 获取用户的待办任务列表
     */
    List<FlowTask> getTodoTasks(String userId);

    /**
     * 获取用户的已办任务列表
     */
    List<FlowTask> getDoneTasks(String userId);

    /**
     * 审批任务
     */
    void approve(String taskId, Integer action, String opinion, Map<String, Object> variables);

    /**
     * 驳回任务
     */
    void reject(String taskId, String opinion);

    /**
     * 转办任务
     */
    void transfer(String taskId, String targetUserId, String opinion);
}
