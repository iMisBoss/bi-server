package com.newtouch.oa.mapper.flow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newtouch.oa.entity.flow.FormDefinition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 表单定义 Mapper 接口
 */
@Mapper
public interface FormDefinitionMapper extends BaseMapper<FormDefinition> {

    /**
     * 根据编码查询表单定义
     */
    FormDefinition selectByCode(String code);
}
