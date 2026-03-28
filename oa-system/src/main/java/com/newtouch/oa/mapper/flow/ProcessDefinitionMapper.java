package com.newtouch.oa.mapper.flow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newtouch.oa.entity.flow.ProcessDefinition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 流程定义 Mapper 接口
 */
@Mapper
public interface ProcessDefinitionMapper extends BaseMapper<ProcessDefinition> {

    /**
     * 根据编码查询流程定义
     */
    ProcessDefinition selectByCode(String code);

    /**
     * 查询已发布的流程定义
     */
    List<ProcessDefinition> selectPublished();
}
