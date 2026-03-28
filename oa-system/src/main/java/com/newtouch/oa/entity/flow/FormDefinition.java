package com.newtouch.oa.entity.flow;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 表单定义实体类
 */
@Data
@Schema(description = "表单定义")
@TableName("flow_form_def")
public class FormDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @Schema(description = "表单名称")
    private String name;

    @Schema(description = "表单编码")
    private String code;

    @Schema(description = "表单类型 1-普通表单 2-主从表单")
    private Integer formType;

    @Schema(description = "表单分类")
    private String category;

    @Schema(description = "表单内容 JSON")
    private String formContent;

    @Schema(description = "表单配置 JSON")
    private String formConfig;

    @Schema(description = "字段配置 JSON")
    private String fieldConfig;

    @Schema(description = "状态 1-启用 2-停用")
    private Integer status;

    @Schema(description = "版本号")
    private Integer version;

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
