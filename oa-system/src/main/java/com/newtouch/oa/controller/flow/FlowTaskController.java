package com.newtouch.oa.controller.flow;

import com.newtouch.common.core.result.Result;
import com.newtouch.oa.entity.flow.FlowTask;
import com.newtouch.oa.service.flow.IFlowTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程任务 Controller
 */
@Slf4j
@RestController
@RequestMapping("/flow/task")
@Tag(name = "流程任务")
public class FlowTaskController {

    @Autowired
    private IFlowTaskService flowTaskService;

    @GetMapping("/todo")
    @Operation(summary = "获取我的待办")
    public Result<List<FlowTask>> getTodoTasks(@RequestParam String userId) {
        List<FlowTask> tasks = flowTaskService.getTodoTasks(userId);
        return Result.success(tasks);
    }

    @GetMapping("/done")
    @Operation(summary = "获取我的已办")
    public Result<List<FlowTask>> getDoneTasks(@RequestParam String userId) {
        List<FlowTask> tasks = flowTaskService.getDoneTasks(userId);
        return Result.success(tasks);
    }

    @PostMapping("/{taskId}/approve")
    @Operation(summary = "审批任务")
    public Result<Boolean> approve(
            @PathVariable String taskId,
            @RequestParam Integer action,
            @RequestParam(required = false) String opinion,
            @RequestBody(required = false) Map<String, Object> variables) {

        flowTaskService.approve(taskId, action, opinion, variables);
        return Result.success(true);
    }

    @PostMapping("/{taskId}/reject")
    @Operation(summary = "驳回任务")
    public Result<Boolean> reject(
            @PathVariable String taskId,
            @RequestParam String opinion) {

        flowTaskService.reject(taskId, opinion);
        return Result.success(true);
    }

    @PostMapping("/{taskId}/transfer")
    @Operation(summary = "转办任务")
    public Result<Boolean> transfer(
            @PathVariable String taskId,
            @RequestParam String targetUserId,
            @RequestParam(required = false) String opinion) {

        flowTaskService.transfer(taskId, targetUserId, opinion);
        return Result.success(true);
    }
}
