-- 建信消金 OA 系统 - TiDB 数据库表结构
-- 开发公司：新致软件

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 人员表
DROP TABLE IF EXISTS `sys_person`;
CREATE TABLE `sys_person` (
                              `id` varchar(64) NOT NULL COMMENT '主键',
                              `name` varchar(50) NOT NULL COMMENT '姓名',
                              `unique_id` varchar(64) NOT NULL COMMENT '唯一标识',
                              `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
                              `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
                              `avatar` varchar(500) DEFAULT NULL COMMENT '头像 URL',
                              `gender` tinyint DEFAULT 1 COMMENT '性别 1-男 2-女',
                              `birthday` date DEFAULT NULL COMMENT '生日',
                              `age` int DEFAULT NULL COMMENT '年龄',
                              `entry_date` date DEFAULT NULL COMMENT '入职日期',
                              `position_level` varchar(50) DEFAULT NULL COMMENT '职级',
                              `status` tinyint DEFAULT 1 COMMENT '状态 1-正常 2-试用 3-离职',
                              `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除',
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `uk_unique_id` (`unique_id`),
                              KEY `idx_mobile` (`mobile`),
                              KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='人员表';

-- 部门表
DROP TABLE IF EXISTS `sys_unit`;
CREATE TABLE `sys_unit` (
                            `id` varchar(64) NOT NULL COMMENT '主键',
                            `name` varchar(100) NOT NULL COMMENT '部门名称',
                            `code` varchar(50) DEFAULT NULL COMMENT '部门编码',
                            `parent_id` varchar(64) DEFAULT NULL COMMENT '上级部门 ID',
                            `level` int DEFAULT 1 COMMENT '层级',
                            `type` varchar(20) DEFAULT NULL COMMENT '类型 company/dept/group',
                            `sort` int DEFAULT 0 COMMENT '排序号',
                            `leader_id` varchar(64) DEFAULT NULL COMMENT '负责人 ID',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `deleted` tinyint(1) DEFAULT 0,
                            PRIMARY KEY (`id`),
                            KEY `idx_parent_id` (`parent_id`),
                            KEY `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

SET FOREIGN_KEY_CHECKS = 1;
