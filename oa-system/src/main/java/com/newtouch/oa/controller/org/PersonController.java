package com.newtouch.oa.controller.org;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtouch.common.core.result.Result;
import com.newtouch.oa.entity.org.Person;
import com.newtouch.oa.service.org.IPersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 人员管理 Controller
 */
@Slf4j
@RestController
@RequestMapping("/org/person")
@Tag(name = "人员管理")
public class PersonController {

    @Autowired
    private IPersonService personService;

    @GetMapping("/{id}")
    @Operation(summary = "根据 ID 查询人员")
    public Result<Person> getById(@PathVariable String id) {
        Person person = personService.getById(id);
        return Result.success(person);
    }

    @GetMapping("/mobile/{mobile}")
    @Operation(summary = "根据手机号查询人员")
    public Result<Person> getByMobile(@PathVariable String mobile) {
        Person person = personService.getByMobile(mobile);
        return Result.success(person);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询人员")
    public Result<Page<Person>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String name) {

        Page<Person> page = personService.pageQuery(pageNum, pageSize, name);
        return Result.success(page);
    }

    @PostMapping
    @Operation(summary = "创建人员")
    public Result<Boolean> create(@RequestBody Person person) {
        boolean saved = personService.save(person);
        return Result.success(saved);
    }

    @PutMapping
    @Operation(summary = "更新人员")
    public Result<Boolean> update(@RequestBody Person person) {
        boolean updated = personService.updateById(person);
        return Result.success(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除人员")
    public Result<Boolean> delete(@PathVariable String id) {
        boolean removed = personService.removeById(id);
        return Result.success(removed);
    }
}
