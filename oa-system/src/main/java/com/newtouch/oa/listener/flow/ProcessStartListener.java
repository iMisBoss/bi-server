package com.newtouch.oa.listener.flow;

import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.delegate.event.FlowableProcessStartedEvent;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 流程启动监听器
 */
@Slf4j
@Component
public class ProcessStartListener extends AbstractFlowableEngineEventListener {

    public ProcessStartListener() {
        super(Collections.singleton(FlowableEngineEventType.PROCESS_STARTED));
    }

    @Override
    protected void processStarted(FlowableProcessStartedEvent event) {
        String processInstanceId = event.getNestedProcessInstanceId();
        String processDefinitionId = event.getNestedProcessDefinitionId();

        log.info("流程启动：processInstanceId={}, processDefinitionId={}",
                processInstanceId, processDefinitionId);
    }
}
