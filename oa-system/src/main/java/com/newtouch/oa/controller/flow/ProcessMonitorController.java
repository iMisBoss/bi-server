package com.newtouch.oa.controller.flow;

import com.newtouch.common.core.result.Result;
import com.newtouch.oa.entity.flow.ProcessInstance;
import com.newtouch.oa.service.flow.IProcessInstanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 流程监控 Controller
 */
@Slf4j
@RestController
@RequestMapping("/flow/monitor")
@Tag(name = "流程监控")
public class ProcessMonitorController {

    @Autowired
    private IProcessInstanceService processInstanceService;

    @GetMapping("/running/{userId}")
    @Operation(summary = "获取用户正在进行的流程")
    public Result<List<ProcessInstance>> getRunningProcesses(@PathVariable String userId) {
        List<ProcessInstance> processes = processInstanceService.getRunningProcesses(userId);
        return Result.success(processes);
    }

    @GetMapping("/completed/{userId}")
    @Operation(summary = "获取用户已完成的流程")
    public Result<List<ProcessInstance>> getCompletedProcesses(@PathVariable String userId) {
        List<ProcessInstance> processes = processInstanceService.getCompletedProcesses(userId);
        return Result.success(processes);
    }

    @GetMapping("/statistics/{userId}")
    @Operation(summary = "获取用户流程统计")
    public Result<Map<String, Object>> getProcessStatistics(@PathVariable String userId) {
        Map<String, Object> statistics = processInstanceService.getProcessStatistics(userId);
        return Result.success(statistics);
    }

    @GetMapping("/duration/{processInstanceId}")
    @Operation(summary = "获取流程时长统计")
    public Result<Map<String, Object>> getProcessDurationStatistics(@PathVariable String processInstanceId) {
        Map<String, Object> durationStats = processInstanceService.getProcessDurationStatistics(processInstanceId);
        return Result.success(durationStats);
    }

    @GetMapping("/activities/{processInstanceId}")
    @Operation(summary = "获取节点活动统计")
    public Result<List<Map<String, Object>>> getProcessActivityStatistics(@PathVariable String processInstanceId) {
        List<Map<String, Object>> activityStats = processInstanceService.getProcessActivityStatistics(processInstanceId);
        return Result.success(activityStats);
    }

    @GetMapping("/overall")
    @Operation(summary = "获取整体统计信息")
    public Result<Map<String, Object>> getOverallStatistics() {
        Map<String, Object> overallStats = processInstanceService.getOverallStatistics();
        return Result.success(overallStats);
    }
}
