package com.newtouch.oa.config.flow;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.history.HistoryLevel;
import org.flowable.common.engine.impl.persistence.StrongUuidGenerator;
import org.flowable.engine.*;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Flowable 配置类
 * 适配 dynamic-datasource
 */
@Slf4j
@Configuration
public class FlowableConfig {

    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(
            DataSource dataSource,
            PlatformTransactionManager transactionManager) {

        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();

        configuration.setDataSource(dataSource);
        configuration.setTransactionManager(transactionManager);
        configuration.setDatabaseSchemaUpdate("true");
        configuration.setAsyncExecutorActivate(true);
        configuration.setDbHistoryUsed(true);
        configuration.setHistoryLevel(HistoryLevel.FULL);
        configuration.setIdGenerator(new StrongUuidGenerator());
        log.info("Flowable SpringProcessEngineConfiguration 配置完成");

        return configuration;
    }

    @Bean
    public ProcessEngine processEngine(SpringProcessEngineConfiguration configuration) {
        log.info("开始构建 ProcessEngine...");
        ProcessEngine engine = configuration.buildProcessEngine();
        log.info("ProcessEngine 构建成功: {}", engine.getName());
        return engine;
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    @PostConstruct
    public void init() {
        log.info("=========================================");
        log.info("Flowable 配置类初始化完成");
        log.info("=========================================");
    }

    public TaskService getTaskService() {
        return taskService(processEngine(null));
    }

    public RuntimeService getRuntimeService() {
        return runtimeService(processEngine(null));
    }

    public HistoryService getHistoryService() {
        return historyService(processEngine(null));
    }

    public RepositoryService getRepositoryService() {
        return repositoryService(processEngine(null));
    }

    public ProcessEngine getProcessEngine() {
        return processEngine(null);
    }
}
