package com.newtouch.oa.controller.flow;

import com.newtouch.common.core.result.Result;
import com.newtouch.oa.entity.flow.ProcessInstance;
import com.newtouch.oa.service.flow.IProcessInstanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 流程实例 Controller
 */
@Slf4j
@RestController
@RequestMapping("/flow/instance")
@Tag(name = "流程实例")
public class ProcessInstanceController {

    @Autowired
    private IProcessInstanceService processInstanceService;

    @PostMapping("/start")
    @Operation(summary = "发起流程")
    public Result<ProcessInstance> startProcess(
            @RequestParam String processDefId,
            @RequestParam String businessKey,
            @RequestBody(required = false) Map<String, Object> variables,
            @RequestParam(required = false) String formData,
            @RequestParam String userId,
            @RequestParam String userName) {

        ProcessInstance processInstance = processInstanceService.startProcess(
                processDefId, businessKey, variables, formData, userId, userName);

        return Result.success(processInstance);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据 ID 查询流程实例")
    public Result<ProcessInstance> getById(@PathVariable String id) {
        ProcessInstance processInstance = processInstanceService.getById(id);
        return Result.success(processInstance);
    }

    @GetMapping("/flowable/{flowableInstanceId}")
    @Operation(summary = "根据 Flowable 实例 ID 查询")
    public Result<ProcessInstance> getByFlowableInstanceId(@PathVariable String flowableInstanceId) {
        ProcessInstance processInstance = processInstanceService.getByFlowableInstanceId(flowableInstanceId);
        return Result.success(processInstance);
    }

    @PutMapping("/{id}/terminate")
    @Operation(summary = "终止流程")
    public Result<Boolean> terminate(
            @PathVariable String id,
            @RequestParam String reason) {

        processInstanceService.terminateProcess(id, reason);
        return Result.success(true);
    }

    @PutMapping("/{id}/suspend")
    @Operation(summary = "挂起流程")
    public Result<Boolean> suspend(@PathVariable String id) {
        processInstanceService.suspendProcess(id);
        return Result.success(true);
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "激活流程")
    public Result<Boolean> activate(@PathVariable String id) {
        processInstanceService.activateProcess(id);
        return Result.success(true);
    }
}
