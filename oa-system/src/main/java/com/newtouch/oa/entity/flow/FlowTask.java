package com.newtouch.oa.entity.flow;

import org.apache.ibatis.type.JdbcType;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程任务实体类
 */
@Data
@Schema(description = "流程任务")
@TableName("flow_task")
public class FlowTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @Schema(description = "任务名称")
    private String taskName;

    @Schema(description = "Flowable 任务 ID")
    private String flowableTaskId;

    @Schema(description = "流程实例 ID")
    private String processInstanceId;

    @Schema(description = "流程定义 ID")
    private String processDefId;

    @Schema(description = "节点 ID")
    private String activityId;

    @Schema(description = "节点类型")
    private String activityType;

    @Schema(description = "审批人 ID")
    private String assignee;

    @Schema(description = "审批人姓名")
    private String assigneeName;

    @Schema(description = "候选人 JSON")
    private String candidates;

    @Schema(description = "任务状态 1-待办理 2-已办理 3-已驳回 4-已转办")
    private Integer status;

    @Schema(description = "是否已读 1-是 0-否")
    private Integer isRead;

    @Schema(description = "任务优先级")
    private Integer priority;

    @Schema(description = "任务变量 JSON")
    private String taskVariables;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "办理时间")
    private Date finishTime;

    @Schema(description = "耗时 (毫秒)")
    private Long duration;

    @Schema(description = "逻辑删除")
    @TableLogic
    private Integer deleted;
}
