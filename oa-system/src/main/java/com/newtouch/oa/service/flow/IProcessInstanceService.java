package com.newtouch.oa.service.flow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newtouch.oa.entity.flow.ProcessInstance;

import java.util.List;
import java.util.Map;

/**
 * 流程实例 Service 接口
 */
public interface IProcessInstanceService extends IService<ProcessInstance> {

    /**
     * 启动流程
     */
    ProcessInstance startProcess(String processDefId, String businessKey,
                                 Map<String, Object> variables, String formData,
                                 String userId, String userName);

    /**
     * 根据 Flowable 流程实例 ID 查询
     */
    ProcessInstance getByFlowableInstanceId(String flowableInstanceId);

    /**
     * 终止流程
     */
    void terminateProcess(String processInstanceId, String reason);

    /**
     * 挂起流程
     */
    void suspendProcess(String processInstanceId);

    /**
     * 激活流程
     */
    void activateProcess(String processInstanceId);

    /**
     * 获取用户正在进行的流程
     */
    List<ProcessInstance> getRunningProcesses(String userId);

    /**
     * 获取用户已完成的流程
     */
    List<ProcessInstance> getCompletedProcesses(String userId);

    /**
     * 获取流程统计数据
     */
    Map<String, Object> getProcessStatistics(String userId);

    /**
     * 获取流程时长统计
     */
    Map<String, Object> getProcessDurationStatistics(String processInstanceId);

    /**
     * 获取节点活动统计
     */
    List<Map<String, Object>> getProcessActivityStatistics(String processInstanceId);

    /**
     * 获取整体统计信息
     */
    Map<String, Object> getOverallStatistics();

    /**
     * 流程催办
     */
    void urgeProcess(String processInstanceId, String urgeUserId, String urgeMessage);

    /**
     * 检查流程超时并发送提醒
     */
    void checkProcessTimeoutAndRemind();

    /**
     * 获取超时流程列表
     */
    List<Map<String, Object>> getTimeoutProcesses(int timeoutHours);
}
