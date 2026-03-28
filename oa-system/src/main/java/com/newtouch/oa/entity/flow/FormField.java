package com.newtouch.oa.entity.flow;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 表单字段实体类
 */
@Data
@Schema(description = "表单字段")
@TableName("flow_form_field")
public class FormField implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @Schema(description = "表单 ID")
    private String formId;

    @Schema(description = "字段名称")
    private String fieldName;

    @Schema(description = "字段标签")
    private String fieldLabel;

    @Schema(description = "字段类型 text/number/date/select/radio/checkbox/textarea/file")
    private String fieldType;

    @Schema(description = "字段长度")
    private Integer fieldLength;

    @Schema(description = "是否必填 1-是 0-否")
    private Integer required;

    @Schema(description = "默认值")
    private String defaultValue;

    @Schema(description = "选项配置 JSON")
    private String options;

    @Schema(description = "校验规则")
    private String validation;

    @Schema(description = "排序号")
    private Integer sort;

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
