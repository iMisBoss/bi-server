package com.newtouch.oa.vo.flow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 节点活动统计 VO
 */
@Data
@Schema(description = "节点活动统计")
public class ActivityStatisticsVO {

    @Schema(description = "节点 ID")
    private String activityId;

    @Schema(description = "节点名称")
    private String activityName;

    @Schema(description = "节点类型")
    private String activityType;

    @Schema(description = "审批人")
    private String assignee;

    @Schema(description = "开始时间")
    private Date startTime;

    @Schema(description = "结束时间")
    private Date endTime;

    @Schema(description = "耗时 (毫秒)")
    private Long durationMillis;
}
