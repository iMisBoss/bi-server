package com.newtouch.oa.controller.flow;

import com.newtouch.common.core.result.Result;
import com.newtouch.oa.entity.flow.FormDefinition;
import com.newtouch.oa.service.flow.IFormDefinitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 表单设计器 Controller
 */
@Slf4j
@RestController
@RequestMapping("/flow/form")
@Tag(name = "表单设计器")
public class FormDesignerController {

    @Autowired
    private IFormDefinitionService formDefinitionService;

    @GetMapping("/{id}")
    @Operation(summary = "根据 ID 查询表单定义")
    public Result<FormDefinition> getById(@PathVariable String id) {
        FormDefinition formDef = formDefinitionService.getById(id);
        return Result.success(formDef);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "根据编码查询表单定义")
    public Result<FormDefinition> getByCode(@PathVariable String code) {
        FormDefinition formDef = formDefinitionService.getByCode(code);
        return Result.success(formDef);
    }

    @PostMapping
    @Operation(summary = "创建表单定义")
    public Result<Boolean> create(@RequestBody FormDefinition formDef) {
        formDefinitionService.saveForm(formDef, formDef.getFieldConfig());
        return Result.success(true);
    }

    @PutMapping
    @Operation(summary = "更新表单定义")
    public Result<Boolean> update(@RequestBody FormDefinition formDef) {
        formDefinitionService.saveForm(formDef, formDef.getFieldConfig());
        return Result.success(true);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除表单定义")
    public Result<Boolean> delete(@PathVariable String id) {
        boolean removed = formDefinitionService.removeById(id);
        return Result.success(removed);
    }
}
