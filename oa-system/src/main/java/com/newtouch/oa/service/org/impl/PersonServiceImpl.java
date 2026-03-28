package com.newtouch.oa.service.org.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newtouch.oa.entity.org.Person;
import com.newtouch.oa.mapper.org.PersonMapper;
import com.newtouch.oa.service.org.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 人员 Service 实现类
 */
@Slf4j
@Service
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements IPersonService {

    @Override
    public Person getByMobile(String mobile) {
        return baseMapper.selectByMobile(mobile);
    }

    @Override
    public Person getByUniqueId(String uniqueId) {
        return baseMapper.selectByUniqueId(uniqueId);
    }

    @Override
    public Page<Person> pageQuery(int pageNum, int pageSize, String name) {
        Page<Person> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Person> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Person::getDeleted, 0);

        if (StringUtils.isNotBlank(name)) {
            wrapper.like(Person::getName, name);
        }

        return baseMapper.selectPage(page, wrapper);
    }
}
