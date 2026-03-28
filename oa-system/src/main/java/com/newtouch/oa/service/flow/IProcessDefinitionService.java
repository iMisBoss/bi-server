package com.newtouch.oa.service.flow;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.newtouch.oa.entity.flow.ProcessDefinition;

/**
 * 流程定义 Service 接口
 */
public interface IProcessDefinitionService extends IService<ProcessDefinition> {

    /**
     * 根据编码查询流程定义
     */
    ProcessDefinition getByCode(String code);

    /**
     * 发布流程定义
     */
    void publish(String id);

    /**
     * 停用流程定义
     */
    void stop(String id);

    /**
     * 分页查询流程定义
     */
    Page<ProcessDefinition> pageQuery(int pageNum, int pageSize, String name, Integer status);
}
