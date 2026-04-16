# OA管理系统 - 后端框架（Flowable集成测试版）

## 1. 项目概述
本项目为OA管理系统后端基础框架，**仅完成技术架构搭建和Flowable工作流引擎集成测试**，未开发任何业务接口、未实现数据逻辑、未配置生产数据库。

## 2. 技术栈
- **核心框架**: Spring Boot 3.2.0 + JDK 17
- **持久层**: MyBatis-Plus 3.5.5
- **工作流引擎**: Flowable 6.8.0
- **数据库连接池**: Druid 1.2.23 / Dynamic DataSource 4.3.0
- **工具类库**: Hutool 5.8.16、FastJSON2 2.0.25、Guava 31.1
- **API文档**: Knife4j 4.1.0 (OpenAPI3)
- **安全认证**: JWT (jjwt 0.11.5)
- **构建工具**: Maven 3.6+

## 3. 环境与运行
### 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+ 或 TiDB（数据库脚本已提供但未强制配置）

### 启动方式
1. 启动主类：`com.newtouch.oa.OaApplication`
2. 默认端口：8080
3. 访问地址：
  - API文档：http://localhost:8080/doc.html
  - 健康检查：http://localhost:8080/actuator/health

### 服务状态
✅ 可正常启动空服务  
❌ **无任何可用业务接口**  
⚠️ Flowable工作流引擎已集成并自动建表，但未开发业务流程

## 4. 已完成内容

### 4.1 基础架构
- ✅ 多模块Maven项目结构（oa-common、oa-system）
- ✅ 统一返回结果封装（Result）
- ✅ 全局异常处理（BusinessException）
- ✅ 分层架构设计（controller/service/mapper/entity）
- ✅ MyBatis-Plus自动填充配置
- ✅ 日志配置与Swagger文档集成

### 4.2 Flowable工作流集成测试
- ✅ Flowable 6.8.0引擎集成
- ✅ 自动创建Flowable数据库表（28张核心表）
- ✅ 流程定义管理基础代码
  - ProcessDefinitionController（流程定义查询）
  - ProcessInstanceController（流程实例管理）
  - FlowTaskController（任务管理）
  - FormDesignerController（表单设计器）
  - ProcessMonitorController（流程监控）
  - ProcessUrgeController（流程催办）
- ✅ 流程实体类定义
  - ProcessDefinition、ProcessInstance、FlowTask
  - FormDefinition、FormField、FlowOpinion
- ✅ 流程监听器
  - ProcessStartListener（流程启动监听）
  - ProcessEndListener（流程结束监听）
- ✅ 示例流程定义（leave-process.bpmn20.xml）
- ✅ 流程统计VO（ActivityStatisticsVO、ProcessDurationVO等）

### 4.3 公共组件模块（oa-common）
- ✅ common-core：核心工具类、统一返回结果
- ✅ common-mybatis：MyBatis-Plus配置
- ✅ common-security：JWT Token提供者
- ✅ common-xinchuang：信创适配（国密SM4加密）
- ✅ common-ai：AI模块占位（未实现）

### 4.4 数据库脚本
- ✅ MySQL初始化脚本（database/mysql/init.sql）
- ✅ TiDB流程表结构（database/tidb/flow-schema.sql）
- ✅ TiDB业务表结构（database/tidb/schema.sql）

## 5. 未完成内容（核心业务）

### 5.1 业务功能缺失
- ❌ 所有OA业务接口（请假、报销、审批等）
- ❌ 用户登录认证与权限控制（仅有JWT工具类）
- ❌ 组织架构管理（Person/Unit实体存在但无完整CRUD）
- ❌ 表单设计与动态渲染
- ❌ 流程部署与实际业务绑定
- ❌ 消息通知与待办提醒

### 5.2 数据层缺失
- ❌ 数据库连接未配置（application-dev.yml需手动配置）
- ❌ 大部分Mapper XML文件为空或仅有基础结构
- ❌ Service层方法未实现具体业务逻辑
- ❌ 数据验证与事务控制

### 5.3 其他缺失
- ❌ 单元测试与集成测试
- ❌ 前后端联调（无可用接口）
- ❌ 生产环境部署配置
- ❌ 性能优化与安全加固
- ❌ 接口权限拦截与审计日志

## 6. 项目结构说明
bi-server/ ├── oa-common/ # 公共组件模块 
│ ├── common-core/ # 核心工具类 
│ ├── common-mybatis/ # MyBatis配置 
│ ├── common-security/ # 安全认证 
│ ├── common-xinchuang/ # 信创适配 
│ └── common-ai/ # AI模块（占位） 
├── oa-system/ # 系统主模块 
│ ├── controller/flow/ # Flowable相关控制器（6个） 
│ ├── entity/flow/ # 流程实体类（6个） 
│ ├── mapper/flow/ # 流程Mapper接口（5个） 
│ ├── service/flow/ # 流程Service（5个接口+5个实现） 
│ ├── listener/flow/ # 流程监听器（2个） 
│ ├── vo/flow/ # 流程视图对象（3个） 
│ └── config/flow/ # Flowable配置 
└── database/ # 数据库脚本 
├── mysql/init.sql # MySQL初始化 
└── tidb/ # TiDB脚本

## 7. 重要声明

### 当前状态
1. **本后端仅为技术验证框架**，已完成Spring Boot 3 + Flowable 6.8.0集成测试
2. **Flowable引擎可正常运行**，自动创建28张工作流表，支持流程定义部署与实例启动
3. **无实际业务功能**，所有Controller接口均未实现完整业务逻辑
4. **无法与前端联调**，缺少完整的RESTful API响应数据
5. **不具备生产交付能力**，仅作为技术架构参考与Flowable集成演示

### 适用场景
- ✅ Flowable工作流引擎技术选型验证
- ✅ Spring Boot 3.x项目架构参考
- ✅ 多模块Maven项目结构设计
- ✅ 信创环境适配方案研究

### 不适用场景
- ❌ 直接用于生产环境
- ❌ 前后端联调开发
- ❌ 实际OA业务流程处理
- ❌ 企业级应用部署

## 8. 后续开发建议

如需继续开发完整OA系统，建议按以下顺序推进：

1. **数据库配置**：完善application-dev.yml中的数据源配置
2. **用户认证**：实现登录接口、JWT Token生成与验证、权限拦截
3. **组织架构**：完成部门、人员管理的CRUD接口
4. **流程开发**：
  - 实现流程定义部署接口
  - 开发流程实例启动与查询
  - 完成任务审批与驳回
  - 实现表单动态渲染
5. **业务模块**：开发请假、报销等具体业务流程
6. **测试与优化**：补充单元测试、性能测试、安全加固
7. **部署配置**：编写Dockerfile、K8s配置文件

## 9. 联系方式
- 开发单位：新致软件
- 项目名称：建信消金OA系统
- 版本：1.0.0-SNAPSHOT
