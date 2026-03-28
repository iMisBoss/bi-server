package com.newtouch.oa.entity.flow;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 审批意见实体类
 */
@Data
@Schema(description = "审批意见")
@TableName("flow_opinion")
public class FlowOpinion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @Schema(description = "流程实例 ID")
    private String processInstanceId;

    @Schema(description = "任务 ID")
    private String taskId;

    @Schema(description = "审批人 ID")
    private String userId;

    @Schema(description = "审批人姓名")
    private String userName;

    @Schema(description = "审批动作 1-同意 2-拒绝 3-驳回 4-转办 5-加签")
    private Integer action;

    @Schema(description = "审批意见")
    private String opinion;

    @Schema(description = "审批时长 (毫秒)")
    private Long duration;

    @Schema(description = "审批时间")
    private Date approveTime;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "逻辑删除")
    @TableLogic
    private Integer deleted;
}
