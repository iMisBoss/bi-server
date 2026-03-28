package com.newtouch.oa.entity.flow;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程定义实体类
 */
@Data
@Schema(description = "流程定义")
@TableName("flow_process_def")
public class ProcessDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @Schema(description = "流程名称")
    private String name;

    @Schema(description = "流程编码")
    private String code;

    @Schema(description = "流程分类")
    private String category;

    @Schema(description = "流程版本")
    private Integer version;

    @Schema(description = "流程定义状态 1-草稿 2-已发布 3-已停用")
    private Integer status;

    @Schema(description = "表单类型 1-自定义表单 2-Flowable 表单")
    private Integer formType;

    @Schema(description = "表单 ID")
    private String formId;

    @Schema(description = "Flowable 流程定义 ID")
    private String flowableDefId;

    @Schema(description = "流程模型 JSON")
    private String processModel;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "排序号")
    private Integer sort;

    @Schema(description = "创建人 ID")
    private String createBy;

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
