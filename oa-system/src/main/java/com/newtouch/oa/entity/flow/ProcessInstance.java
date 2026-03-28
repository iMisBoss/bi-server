package com.newtouch.oa.entity.flow;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程实例实体类
 */
@Data
@Schema(description = "流程实例")
@TableName("flow_process_instance")
public class ProcessInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @Schema(description = "流程定义 ID")
    private String processDefId;

    @Schema(description = "流程实例名称")
    private String instanceName;

    @Schema(description = "Flowable 流程实例 ID")
    private String flowableInstanceId;

    @Schema(description = "业务主键")
    private String businessKey;

    @Schema(description = "发起人 ID")
    private String startUserId;

    @Schema(description = "发起人姓名")
    private String startUserName;

    @Schema(description = "发起部门 ID")
    private String startDeptId;

    @Schema(description = "当前节点 ID")
    private String currentActivityId;

    @Schema(description = "当前节点名称")
    private String currentActivityName;

    @Schema(description = "流程状态 1-进行中 2-已完成 3-已终止 4-已取消")
    private Integer status;

    @Schema(description = "流程变量 JSON")
    private String variables;

    @Schema(description = "表单数据 JSON")
    private String formData;

    @Schema(description = "开始时间")
    private Date startTime;

    @Schema(description = "结束时间")
    private Date endTime;

    @Schema(description = "耗时 (毫秒)")
    private Long duration;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Schema(description = "逻辑删除")
    @TableLogic
    private Integer deleted;
}
