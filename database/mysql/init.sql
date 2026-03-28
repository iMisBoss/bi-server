-- =====================================================
-- OA 系统数据库初始化脚本
-- 包含：业务表 + Flowable 引擎表
-- =====================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS oa_jianxin
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_unicode_ci;

USE oa_jianxin;

-- =====================================================
-- 第一部分：自定义业务表
-- =====================================================

-- 1. 流程定义表
DROP TABLE IF EXISTS flow_process_def;
CREATE TABLE flow_process_def (
                                  id VARCHAR(64) NOT NULL COMMENT '主键 ID',
                                  code VARCHAR(100) NOT NULL COMMENT '流程编码',
                                  name VARCHAR(200) NOT NULL COMMENT '流程名称',
                                  `key` VARCHAR(100) DEFAULT NULL COMMENT 'Flowable 流程定义 Key',
                                  version INT DEFAULT 1 COMMENT '版本号',
                                  category VARCHAR(50) DEFAULT NULL COMMENT '分类',
                                  status INT DEFAULT 1 COMMENT '状态 1-草稿 2-已发布 3-已停用',
                                  form_code VARCHAR(100) DEFAULT NULL COMMENT '关联表单编码',
                                  description VARCHAR(500) DEFAULT NULL COMMENT '描述',
                                  sort INT DEFAULT 0 COMMENT '排序',
                                  create_by VARCHAR(64) DEFAULT NULL COMMENT '创建人',
                                  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
                                  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  deleted INT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
                                  PRIMARY KEY (id),
                                  UNIQUE KEY uk_code (code),
                                  KEY idx_status (status),
                                  KEY idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='流程定义表';

-- 2. 流程实例表
DROP TABLE IF EXISTS flow_process_instance;
CREATE TABLE flow_process_instance (
                                       id VARCHAR(64) NOT NULL COMMENT '主键 ID',
                                       flowable_instance_id VARCHAR(64) NOT NULL COMMENT 'Flowable 流程实例 ID',
                                       process_def_id VARCHAR(64) NOT NULL COMMENT '流程定义 ID',
                                       business_key VARCHAR(100) DEFAULT NULL COMMENT '业务主键',
                                       instance_name VARCHAR(200) DEFAULT NULL COMMENT '实例名称',
                                       title VARCHAR(200) DEFAULT NULL COMMENT '标题',
                                       applicant_id VARCHAR(64) DEFAULT NULL COMMENT '申请人 ID',
                                       applicant_name VARCHAR(100) DEFAULT NULL COMMENT '申请人姓名',
                                       department VARCHAR(100) DEFAULT NULL COMMENT '部门',
                                       status INT DEFAULT 1 COMMENT '状态 1-进行中 2-已完成 3-已终止 4-已挂起',
                                       current_activity_id VARCHAR(64) DEFAULT NULL COMMENT '当前活动节点 ID',
                                       current_activity_name VARCHAR(200) DEFAULT NULL COMMENT '当前活动节点名称',
                                       start_user_id VARCHAR(64) DEFAULT NULL COMMENT '发起人 ID',
                                       start_user_name VARCHAR(100) DEFAULT NULL COMMENT '发起人姓名',
                                       start_time DATETIME DEFAULT NULL COMMENT '开始时间',
                                       end_time DATETIME DEFAULT NULL COMMENT '结束时间',
                                       duration BIGINT DEFAULT 0 COMMENT '耗时 (毫秒)',
                                       variables TEXT COMMENT '流程变量 JSON',
                                       form_data TEXT COMMENT '表单数据 JSON',
                                       create_by VARCHAR(64) DEFAULT NULL COMMENT '创建人',
                                       create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
                                       update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       deleted INT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
                                       PRIMARY KEY (id),
                                       KEY idx_flowable_instance_id (flowable_instance_id),
                                       KEY idx_process_def_id (process_def_id),
                                       KEY idx_business_key (business_key),
                                       KEY idx_start_user_id (start_user_id),
                                       KEY idx_status (status),
                                       KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='流程实例表';

-- 3. 流程任务表
DROP TABLE IF EXISTS flow_task;
CREATE TABLE flow_task (
                           id VARCHAR(64) NOT NULL COMMENT '主键 ID',
                           task_name VARCHAR(200) DEFAULT NULL COMMENT '任务名称',
                           flowable_task_id VARCHAR(64) NOT NULL COMMENT 'Flowable 任务 ID',
                           process_instance_id VARCHAR(64) DEFAULT NULL COMMENT '流程实例 ID',
                           process_def_id VARCHAR(64) DEFAULT NULL COMMENT '流程定义 ID',
                           activity_id VARCHAR(64) DEFAULT NULL COMMENT '活动节点 ID',
                           activity_type VARCHAR(50) DEFAULT NULL COMMENT '活动节点类型',
                           assignee VARCHAR(64) DEFAULT NULL COMMENT '审批人 ID',
                           assignee_name VARCHAR(100) DEFAULT NULL COMMENT '审批人姓名',
                           candidates TEXT COMMENT '候选人 JSON',
                           status INT DEFAULT 1 COMMENT '状态 1-待办理 2-已办理 3-已驳回 4-已转办',
                           is_read INT DEFAULT 0 COMMENT '是否已读 0-否 1-是',
                           priority INT DEFAULT 0 COMMENT '优先级',
                           task_variables TEXT COMMENT '任务变量 JSON',
                           create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           finish_time DATETIME DEFAULT NULL COMMENT '办理时间',
                           duration BIGINT DEFAULT 0 COMMENT '耗时 (毫秒)',
                           create_by VARCHAR(64) DEFAULT NULL COMMENT '创建人',
                           update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
                           deleted INT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
                           PRIMARY KEY (id),
                           KEY idx_flowable_task_id (flowable_task_id),
                           KEY idx_process_instance_id (process_instance_id),
                           KEY idx_assignee (assignee),
                           KEY idx_status (status),
                           KEY idx_create_time (create_time),
                           KEY idx_finish_time (finish_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='流程任务表';

-- 4. 表单定义表
DROP TABLE IF EXISTS flow_form_def;
CREATE TABLE flow_form_def (
                               id VARCHAR(64) NOT NULL COMMENT '主键 ID',
                               code VARCHAR(100) NOT NULL COMMENT '表单编码',
                               name VARCHAR(200) NOT NULL COMMENT '表单名称',
                               type VARCHAR(50) DEFAULT NULL COMMENT '表单类型',
                               version INT DEFAULT 1 COMMENT '版本号',
                               content TEXT COMMENT '表单内容 HTML/JSON',
                               field_config TEXT COMMENT '字段配置 JSON',
                               status INT DEFAULT 1 COMMENT '状态 1-草稿 2-已发布 3-已停用',
                               create_by VARCHAR(64) DEFAULT NULL COMMENT '创建人',
                               create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
                               update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               deleted INT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
                               PRIMARY KEY (id),
                               UNIQUE KEY uk_code (code),
                               KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='表单定义表';

-- 5. 审批意见表
DROP TABLE IF EXISTS flow_opinion;
CREATE TABLE flow_opinion (
                              id VARCHAR(64) NOT NULL COMMENT '主键 ID',
                              process_instance_id VARCHAR(64) DEFAULT NULL COMMENT '流程实例 ID',
                              task_id VARCHAR(64) DEFAULT NULL COMMENT '任务 ID',
                              user_id VARCHAR(64) DEFAULT NULL COMMENT '审批人 ID',
                              user_name VARCHAR(100) DEFAULT NULL COMMENT '审批人姓名',
                              action INT DEFAULT NULL COMMENT '审批动作 1-同意 2-拒绝 3-驳回 4-转办 5-加签',
                              opinion TEXT COMMENT '审批意见',
                              duration BIGINT DEFAULT 0 COMMENT '审批时长 (毫秒)',
                              approve_time DATETIME DEFAULT NULL COMMENT '审批时间',
                              create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              deleted INT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
                              PRIMARY KEY (id),
                              KEY idx_process_instance_id (process_instance_id),
                              KEY idx_task_id (task_id),
                              KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审批意见表';

-- =====================================================
-- 第二部分：Flowable 引擎表
-- 说明：Flowable 启动时会自动创建以下表，无需手动执行
-- =====================================================

-- ACT_GE_* (2 张表)
-- ACT_GE_BYTEARRAY - 二进制数据表
-- ACT_GE_PROPERTY - 属性表

-- ACT_RE_* (3 张表)
-- ACT_RE_DEPLOYMENT - 部署单元表
-- ACT_RE_PROCDEF - 流程定义表
-- ACT_RE_MODEL - 模型表

-- ACT_RU_* (10 张表)
-- ACT_RU_EXECUTION - 流程执行实例表
-- ACT_RU_TASK - 运行时任务表
-- ACT_RU_VARIABLE - 运行时变量表
-- ACT_RU_IDENTITYLINK - 运行时参与者表
-- ACT_RU_JOB - 作业表
-- ACT_RU_SUSPENDED_JOB - 暂停作业表
-- ACT_RU_TIMER_JOB - 定时器作业表
-- ACT_RU_HISTORY_JOB - 历史作业表
-- ACT_RU_DEADLETTER_JOB - 死信作业表
-- ACT_RU_EXTERNAL_JOB - 外部作业表

-- ACT_HI_* (11 张表)
-- ACT_HI_ACTINST - 历史活动实例表
-- ACT_HI_ATTACHMENT - 历史附件表
-- ACT_HI_COMMENT - 历史评论表
-- ACT_HI_DETAIL - 历史详情表
-- ACT_HI_ENTITY_LINK - 历史实体链接表
-- ACT_HI_IDENTITYLINK - 历史参与者表
-- ACT_HI_PROCINST - 历史流程实例表
-- ACT_HI_TASKINST - 历史任务实例表
-- ACT_HI_TSK_LOG - 历史任务日志表
-- ACT_HI_VARINST - 历史变量表
-- ACT_HI_PROCDEF - 历史流程定义表

-- ACT_ID_* (7 张表)
-- ACT_ID_USER - 用户表
-- ACT_ID_GROUP - 用户组表
-- ACT_ID_MEMBERSHIP - 用户组关系表
-- ACT_ID_INFO - 用户信息表
-- ACT_ID_TOKEN - 令牌表
-- ACT_ID_PRIV - 权限表
-- ACT_ID_PRIV_MAPPING - 权限映射表

-- ACT_EVT_* (1 张表)
-- ACT_EVT_LOG - 事件日志表

-- ACT_DMN_* (DMN 决策表，可选)
-- ACT_DMN_DECISION_TABLE - 决策表
-- ACT_DMN_INPUT - 输入表
-- ACT_DMN_OUTPUT - 输出表
-- 等等...

-- =====================================================
-- 第三部分：初始化数据
-- =====================================================

-- 插入测试数据（可选）
INSERT INTO flow_process_def (id, code, name, `key`, version, category, status, description) VALUES
                                                                                                 ('1', 'LEAVE_PROCESS', '请假流程', 'leaveProcess', 1, 'OA', 2, '员工请假审批流程'),
                                                                                                 ('2', 'EXPENSE_PROCESS', '报销流程', 'expenseProcess', 1, '财务', 2, '费用报销审批流程'),
                                                                                                 ('3', 'PURCHASE_PROCESS', '采购流程', 'purchaseProcess', 1, '采购', 2, '采购申请审批流程');

INSERT INTO flow_form_def (id, code, name, type, version, status) VALUES
                                                                      ('1', 'LEAVE_FORM', '请假申请表', 'form', 1, 2),
                                                                      ('2', 'EXPENSE_FORM', '报销申请表', 'form', 1, 2),
                                                                      ('3', 'PURCHASE_FORM', '采购申请表', 'form', 1, 2);
