package com.newtouch.oa.service.flow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newtouch.oa.entity.flow.FormDefinition;

/**
 * 表单定义 Service 接口
 */
public interface IFormDefinitionService extends IService<FormDefinition> {

    /**
     * 根据编码查询表单定义
     */
    FormDefinition getByCode(String code);

    /**
     * 保存表单（包含字段）
     */
    void saveForm(FormDefinition formDef, String fieldConfig);
}
