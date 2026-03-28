package com.newtouch.oa.service.flow.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newtouch.oa.entity.flow.FlowOpinion;
import com.newtouch.oa.mapper.flow.FlowOpinionMapper;
import com.newtouch.oa.service.flow.IFlowOpinionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 审批意见 Service 实现类
 */
@Slf4j
@Service
public class FlowOpinionServiceImpl extends ServiceImpl<FlowOpinionMapper, FlowOpinion> implements IFlowOpinionService {

}
