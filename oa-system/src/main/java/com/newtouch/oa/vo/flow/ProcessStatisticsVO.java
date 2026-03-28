package com.newtouch.oa.vo.flow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 流程统计 VO
 */
@Data
@Schema(description = "流程统计信息")
public class ProcessStatisticsVO {

    @Schema(description = "运行中数量")
    private Long runningCount;

    @Schema(description = "已完成数量")
    private Long completedCount;

    @Schema(description = "已终止数量")
    private Long terminatedCount;

    @Schema(description = "总数量")
    private Long totalCount;

    @Schema(description = "近 7 天发起数量")
    private Long last7DaysCount;
}
