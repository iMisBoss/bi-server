package com.newtouch.oa.mapper.flow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newtouch.oa.entity.flow.ProcessInstance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 流程实例 Mapper 接口
 */
@Mapper
public interface ProcessInstanceMapper extends BaseMapper<ProcessInstance> {

    /**
     * 根据 Flowable 流程实例 ID 查询
     */
    ProcessInstance selectByFlowableInstanceId(String flowableInstanceId);
}
