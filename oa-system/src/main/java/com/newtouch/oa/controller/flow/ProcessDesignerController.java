package com.newtouch.oa.controller.flow;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.common.core.result.Result;
import com.newtouch.oa.entity.flow.ProcessDefinition;
import com.newtouch.oa.service.flow.IProcessDefinitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 流程设计器 Controller
 */
@Slf4j
@RestController
@RequestMapping("/flow/designer")
@Tag(name = "流程设计器")
public class ProcessDesignerController {

    @Autowired
    private IProcessDefinitionService processDefinitionService;

    @GetMapping("/page")
    @Operation(summary = "分页查询流程定义")
    public Result<Page<ProcessDefinition>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status) {

        Page<ProcessDefinition> page = processDefinitionService.pageQuery(pageNum, pageSize, name, status);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据 ID 查询流程定义")
    public Result<ProcessDefinition> getById(@PathVariable String id) {
        ProcessDefinition processDef = processDefinitionService.getById(id);
        return Result.success(processDef);
    }

    @PostMapping
    @Operation(summary = "创建流程定义")
    public Result<Boolean> create(@RequestBody ProcessDefinition processDef) {
        processDef.setStatus(1); // 草稿
        boolean saved = processDefinitionService.save(processDef);
        return Result.success(saved);
    }

    @PutMapping
    @Operation(summary = "更新流程定义")
    public Result<Boolean> update(@RequestBody ProcessDefinition processDef) {
        boolean updated = processDefinitionService.updateById(processDef);
        return Result.success(updated);
    }

    @PutMapping("/{id}/publish")
    @Operation(summary = "发布流程定义")
    public Result<Boolean> publish(@PathVariable String id) {
        processDefinitionService.publish(id);
        return Result.success(true);
    }

    @PutMapping("/{id}/stop")
    @Operation(summary = "停用流程定义")
    public Result<Boolean> stop(@PathVariable String id) {
        processDefinitionService.stop(id);
        return Result.success(true);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除流程定义")
    public Result<Boolean> delete(@PathVariable String id) {
        boolean removed = processDefinitionService.removeById(id);
        return Result.success(removed);
    }
}
