-- 建信消金 OA 系统 - 流程模块数据库表结构
-- 开发公司：新致软件

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 流程定义表
DROP TABLE IF EXISTS `flow_process_def`;
CREATE TABLE `flow_process_def` (
                                    `id` varchar(64) NOT NULL COMMENT '主键',
                                    `name` varchar(200) NOT NULL COMMENT '流程名称',
                                    `code` varchar(100) NOT NULL COMMENT '流程编码',
                                    `category` varchar(50) DEFAULT NULL COMMENT '流程分类',
                                    `version` int DEFAULT 1 COMMENT '流程版本',
                                    `status` tinyint DEFAULT 1 COMMENT '状态 1-草稿 2-已发布 3-已停用',
                                    `form_type` tinyint DEFAULT 1 COMMENT '表单类型 1-自定义表单 2-Flowable 表单',
                                    `form_id` varchar(64) DEFAULT NULL COMMENT '表单 ID',
                                    `flowable_def_id` varchar(100) DEFAULT NULL COMMENT 'Flowable 流程定义 ID',
                                    `process_model` text COMMENT '流程模型 JSON',
                                    `icon` varchar(200) DEFAULT NULL COMMENT '图标',
                                    `description` varchar(500) DEFAULT NULL COMMENT '描述',
                                    `sort` int DEFAULT 0 COMMENT '排序号',
                                    `create_by` varchar(64) DEFAULT NULL COMMENT '创建人 ID',
                                    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `uk_code` (`code`),
                                    KEY `idx_category` (`category`),
                                    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='流程定义表';

-- 表单定义表
DROP TABLE IF EXISTS `flow_form_def`;
CREATE TABLE `flow_form_def` (
                                 `id` varchar(64) NOT NULL COMMENT '主键',
                                 `name` varchar(200) NOT NULL COMMENT '表单名称',
                                 `code` varchar(100) NOT NULL COMMENT '表单编码',
                                 `form_type` tinyint DEFAULT 1 COMMENT '表单类型 1-普通表单 2-主从表单',
                                 `category` varchar(50) DEFAULT NULL COMMENT '表单分类',
                                 `form_content` text COMMENT '表单内容 JSON',
                                 `form_config` text COMMENT '表单配置 JSON',
                                 `field_config` text COMMENT '字段配置 JSON',
                                 `status` tinyint DEFAULT 1 COMMENT '状态 1-启用 2-停用',
                                 `version` int DEFAULT 1 COMMENT '版本号',
                                 `create_by` varchar(64) DEFAULT NULL COMMENT '创建人 ID',
                                 `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='表单定义表';

-- 表单字段表
DROP TABLE IF EXISTS `flow_form_field`;
CREATE TABLE `flow_form_field` (
                                   `id` varchar(64) NOT NULL COMMENT '主键',
                                   `form_id` varchar(64) NOT NULL COMMENT '表单 ID',
                                   `field_name` varchar(100) NOT NULL COMMENT '字段名称',
                                   `field_label` varchar(200) NOT NULL COMMENT '字段标签',
                                   `field_type` varchar(50) DEFAULT NULL COMMENT '字段类型',
                                   `field_length` int DEFAULT NULL COMMENT '字段长度',
                                   `required` tinyint DEFAULT 0 COMMENT '是否必填',
                                   `default_value` varchar(500) DEFAULT NULL COMMENT '默认值',
                                   `options` text COMMENT '选项配置 JSON',
                                   `validation` varchar(500) DEFAULT NULL COMMENT '校验规则',
                                   `sort` int DEFAULT 0 COMMENT '排序号',
                                   `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除',
                                   PRIMARY KEY (`id`),
                                   KEY `idx_form_id` (`form_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='表单字段表';

-- 流程实例表
DROP TABLE IF EXISTS `flow_process_instance`;
CREATE TABLE `flow_process_instance` (
                                         `id` varchar(64) NOT NULL COMMENT '主键',
                                         `process_def_id` varchar(64) NOT NULL COMMENT '流程定义 ID',
                                         `instance_name` varchar(200) DEFAULT NULL COMMENT '流程实例名称',
                                         `flowable_instance_id` varchar(100) DEFAULT NULL COMMENT 'Flowable 流程实例 ID',
                                         `business_key` varchar(100) DEFAULT NULL COMMENT '业务主键',
                                         `start_user_id` varchar(64) DEFAULT NULL COMMENT '发起人 ID',
                                         `start_user_name` varchar(50) DEFAULT NULL COMMENT '发起人姓名',
                                         `start_dept_id` varchar(64) DEFAULT NULL COMMENT '发起部门 ID',
                                         `current_activity_id` varchar(100) DEFAULT NULL COMMENT '当前节点 ID',
                                         `current_activity_name` varchar(200) DEFAULT NULL COMMENT '当前节点名称',
                                         `status` tinyint DEFAULT 1 COMMENT '流程状态 1-进行中 2-已完成 3-已终止 4-已取消',
                                         `variables` text COMMENT '流程变量 JSON',
                                         `form_data` text COMMENT '表单数据 JSON',
                                         `start_time` datetime DEFAULT NULL COMMENT '开始时间',
                                         `end_time` datetime DEFAULT NULL COMMENT '结束时间',
                                         `duration` bigint DEFAULT NULL COMMENT '耗时 (毫秒)',
                                         `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                         `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除',
                                         PRIMARY KEY (`id`),
                                         KEY `idx_process_def_id` (`process_def_id`),
                                         KEY `idx_flowable_instance_id` (`flowable_instance_id`),
                                         KEY `idx_start_user_id` (`start_user_id`),
                                         KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='流程实例表';

-- 流程任务表
DROP TABLE IF EXISTS `flow_task`;
CREATE TABLE `flow_task` (
                             `id` varchar(64) NOT NULL COMMENT '主键',
                             `task_name` varchar(200) DEFAULT NULL COMMENT '任务名称',
                             `flowable_task_id` varchar(100) DEFAULT NULL COMMENT 'Flowable 任务 ID',
                             `process_instance_id` varchar(64) NOT NULL COMMENT '流程实例 ID',
                             `process_def_id` varchar(64) NOT NULL COMMENT '流程定义 ID',
                             `activity_id` varchar(100) DEFAULT NULL COMMENT '节点 ID',
                             `activity_type` varchar(50) DEFAULT NULL COMMENT '节点类型',
                             `assignee` varchar(64) DEFAULT NULL COMMENT '审批人 ID',
                             `assignee_name` varchar(50) DEFAULT NULL COMMENT '审批人姓名',
                             `candidates` text COMMENT '候选人 JSON',
                             `status` tinyint DEFAULT 1 COMMENT '任务状态 1-待办理 2-已办理 3-已驳回 4-已转办',
                             `is_read` tinyint DEFAULT 0 COMMENT '是否已读',
                             `priority` int DEFAULT 0 COMMENT '任务优先级',
                             `task_variables` text COMMENT '任务变量 JSON',
                             `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `finish_time` datetime DEFAULT NULL COMMENT '办理时间',
                             `duration` bigint DEFAULT NULL COMMENT '耗时 (毫秒)',
                             `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除',
                             PRIMARY KEY (`id`),
                             KEY `idx_process_instance_id` (`process_instance_id`),
                             KEY `idx_assignee` (`assignee`),
                             KEY `idx_flowable_task_id` (`flowable_task_id`),
                             KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='流程任务表';

-- 审批意见表
DROP TABLE IF EXISTS `flow_opinion`;
CREATE TABLE `flow_opinion` (
                                `id` varchar(64) NOT NULL COMMENT '主键',
                                `process_instance_id` varchar(64) NOT NULL COMMENT '流程实例 ID',
                                `task_id` varchar(64) DEFAULT NULL COMMENT '任务 ID',
                                `user_id` varchar(64) NOT NULL COMMENT '审批人 ID',
                                `user_name` varchar(50) NOT NULL COMMENT '审批人姓名',
                                `action` tinyint DEFAULT NULL COMMENT '审批动作 1-同意 2-拒绝 3-驳回 4-转办 5-加签',
                                `opinion` varchar(1000) DEFAULT NULL COMMENT '审批意见',
                                `duration` bigint DEFAULT NULL COMMENT '审批时长 (毫秒)',
                                `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
                                `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除',
                                PRIMARY KEY (`id`),
                                KEY `idx_process_instance_id` (`process_instance_id`),
                                KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审批意见表';

SET FOREIGN_KEY_CHECKS = 1;
