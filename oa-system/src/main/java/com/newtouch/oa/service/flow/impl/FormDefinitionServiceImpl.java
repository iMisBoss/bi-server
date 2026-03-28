package com.newtouch.oa.service.flow.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newtouch.common.core.exception.BusinessException;
import com.newtouch.oa.entity.flow.FormDefinition;
import com.newtouch.oa.mapper.flow.FormDefinitionMapper;
import com.newtouch.oa.service.flow.IFormDefinitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 表单定义 Service 实现类
 */
@Slf4j
@Service
public class FormDefinitionServiceImpl extends ServiceImpl<FormDefinitionMapper, FormDefinition> implements IFormDefinitionService {

    @Override
    public FormDefinition getByCode(String code) {
        return baseMapper.selectByCode(code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveForm(FormDefinition formDef, String fieldConfig) {
        if (formDef == null) {
            throw new BusinessException("表单定义不能为空");
        }

        formDef.setFieldConfig(fieldConfig);

        if (formDef.getId() == null || formDef.getId().isEmpty()) {
            // 新增
            formDef.setStatus(1); // 草稿状态
            save(formDef);
            log.info("表单定义已创建：{} - {}", formDef.getCode(), formDef.getName());
        } else {
            // 更新
            updateById(formDef);
            log.info("表单定义已更新：{} - {}", formDef.getCode(), formDef.getName());
        }
    }
}
