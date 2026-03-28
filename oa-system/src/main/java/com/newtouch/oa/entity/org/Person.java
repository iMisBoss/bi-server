package com.newtouch.oa.entity.org;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 人员实体类
 */
@Data
@Schema(description = "人员信息")
@TableName("sys_person")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键 ID")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "唯一标识")
    private String uniqueId;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像 URL")
    private String avatar;

    @Schema(description = "性别 1-男 2-女")
    private Integer gender;

    @Schema(description = "生日")
    private Date birthday;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "入职日期")
    private Date entryDate;

    @Schema(description = "职级")
    private String positionLevel;

    @Schema(description = "状态 1-正常 2-试用 3-离职")
    private Integer status;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Schema(description = "逻辑删除 0-未删除 1-已删除")
    @TableLogic
    private Integer deleted;
}
