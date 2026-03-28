package com.newtouch.oa.mapper.flow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newtouch.oa.entity.flow.FlowTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 流程任务 Mapper 接口
 */
@Mapper
public interface FlowTaskMapper extends BaseMapper<FlowTask> {

    /**
     * 查询用户的待办任务
     */
    List<FlowTask> selectTodoTasks(String userId);

    /**
     * 查询用户的已办任务
     */
    List<FlowTask> selectDoneTasks(String userId);
}
