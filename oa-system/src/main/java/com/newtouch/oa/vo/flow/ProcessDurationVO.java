package com.newtouch.oa.vo.flow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 流程时长统计 VO
 */
@Data
@Schema(description = "流程时长统计")
public class ProcessDurationVO {

    @Schema(description = "耗时 (毫秒)")
    private Long durationMillis;

    @Schema(description = "耗时 (秒)")
    private Long durationSeconds;

    @Schema(description = "耗时 (分钟)")
    private Long durationMinutes;

    @Schema(description = "开始时间")
    private Date startTime;

    @Schema(description = "结束时间")
    private Date endTime;
}
