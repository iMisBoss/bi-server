package com.newtouch.oa.mapper.org;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newtouch.oa.entity.org.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 人员 Mapper 接口
 */
@Mapper
public interface PersonMapper extends BaseMapper<Person> {

    /**
     * 根据手机号查询人员
     */
    @Select("SELECT * FROM sys_person WHERE mobile = #{mobile} AND deleted = 0")
    Person selectByMobile(String mobile);

    /**
     * 根据唯一标识查询人员
     */
    @Select("SELECT * FROM sys_person WHERE unique_id = #{uniqueId} AND deleted = 0")
    Person selectByUniqueId(String uniqueId);
}
