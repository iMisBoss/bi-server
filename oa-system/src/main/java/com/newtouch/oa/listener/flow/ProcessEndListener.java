package com.newtouch.oa.listener.flow;

import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 流程结束监听器
 */
@Slf4j
@Component
public class ProcessEndListener extends AbstractFlowableEngineEventListener {

    public ProcessEndListener() {
        super(Collections.singleton(FlowableEngineEventType.PROCESS_COMPLETED));
    }

    @Override
    protected void processCompleted(FlowableEngineEntityEvent event) {
        log.info("流程完成 - 流程实例 ID: {}, 流程定义 ID: {}",
                event.getProcessInstanceId(), event.getProcessDefinitionId());
    }
}
