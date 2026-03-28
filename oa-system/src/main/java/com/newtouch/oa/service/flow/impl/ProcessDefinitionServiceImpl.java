package com.newtouch.oa.service.flow.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newtouch.common.core.exception.BusinessException;
import com.newtouch.oa.entity.flow.ProcessDefinition;
import com.newtouch.oa.mapper.flow.ProcessDefinitionMapper;
import com.newtouch.oa.service.flow.IProcessDefinitionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 流程定义 Service 实现类
 */
@Slf4j
@Service
public class ProcessDefinitionServiceImpl extends ServiceImpl<ProcessDefinitionMapper, ProcessDefinition> implements IProcessDefinitionService {

    @Override
    public ProcessDefinition getByCode(String code) {
        return baseMapper.selectByCode(code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publish(String id) {
        ProcessDefinition processDef = getById(id);
        if (processDef == null) {
            throw new BusinessException("流程定义不存在");
        }

        processDef.setStatus(2); // 已发布
        updateById(processDef);

        log.info("流程定义已发布：{} - {}", processDef.getCode(), processDef.getName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stop(String id) {
        ProcessDefinition processDef = getById(id);
        if (processDef == null) {
            throw new BusinessException("流程定义不存在");
        }

        processDef.setStatus(3); // 已停用
        updateById(processDef);

        log.info("流程定义已停用：{} - {}", processDef.getCode(), processDef.getName());
    }

    @Override
    public Page<ProcessDefinition> pageQuery(int pageNum, int pageSize, String name, Integer status) {
        Page<ProcessDefinition> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<ProcessDefinition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessDefinition::getDeleted, 0);

        if (StringUtils.isNotBlank(name)) {
            wrapper.like(ProcessDefinition::getName, name);
        }

        if (status != null) {
            wrapper.eq(ProcessDefinition::getStatus, status);
        }

        wrapper.orderByDesc(ProcessDefinition::getCreateTime);

        return baseMapper.selectPage(page, wrapper);
    }
}
