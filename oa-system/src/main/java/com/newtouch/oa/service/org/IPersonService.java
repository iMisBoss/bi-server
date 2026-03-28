package com.newtouch.oa.service.org;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.newtouch.oa.entity.org.Person;

/**
 * 人员 Service 接口
 */
public interface IPersonService extends IService<Person> {

    /**
     * 根据手机号查询人员
     */
    Person getByMobile(String mobile);

    /**
     * 根据唯一标识查询人员
     */
    Person getByUniqueId(String uniqueId);

    /**
     * 分页查询人员
     */
    Page<Person> pageQuery(int pageNum, int pageSize, String name);
}
