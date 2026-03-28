package com.newtouch.oa.entity.org;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 部门实体类
 */
@Data
@Schema(description = "部门信息")
@TableName("sys_unit")
public class Unit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "部门编码")
    private String code;

    @Schema(description = "上级部门 ID")
    private String parentId;

    @Schema(description = "层级")
    private Integer level;

    @Schema(description = "类型 company/dept/group")
    private String type;

    @Schema(description = "排序号")
    private Integer sort;

    @Schema(description = "负责人 ID")
    private String leaderId;

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
