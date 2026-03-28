package com.newtouch.oa.controller.flow;

import com.newtouch.common.core.result.Result;
import com.newtouch.oa.service.flow.IProcessInstanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 流程催办 Controller
 */
@Slf4j
@RestController
@RequestMapping("/flow/urge")
@Tag(name = "流程催办")
public class ProcessUrgeController {

    @Autowired
    private IProcessInstanceService processInstanceService;

    @PostMapping("/{processInstanceId}")
    @Operation(summary = "流程催办")
    public Result<Void> urgeProcess(
            @PathVariable String processInstanceId,
            @RequestParam String urgeUserId,
            @RequestParam(required = false) String urgeMessage) {

        String defaultMessage = "您好，您有待办任务需要及时处理！";
        String message = urgeMessage != null ? urgeMessage : defaultMessage;

        processInstanceService.urgeProcess(processInstanceId, urgeUserId, message);
        return Result.success(null);
    }

    @GetMapping("/timeout/list")
    @Operation(summary = "获取超时流程列表")
    public Result<List<Map<String, Object>>> getTimeoutProcesses(
            @RequestParam(defaultValue = "48") int timeoutHours) {
        List<Map<String, Object>> timeoutProcesses =
                processInstanceService.getTimeoutProcesses(timeoutHours);
        return Result.success(timeoutProcesses);
    }

    @PostMapping("/timeout/check")
    @Operation(summary = "手动触发超时检查")
    public Result<Void> checkTimeout() {
        processInstanceService.checkProcessTimeoutAndRemind();
        return Result.success(null);
    }
}
