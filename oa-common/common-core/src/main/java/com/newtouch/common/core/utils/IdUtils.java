
package com.newtouch.common.core.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * ID 生成工具类
 */
public class IdUtils {

    private static final Snowflake snowflake = IdUtil.getSnowflake(1, 1);

    /**
     * 生成雪花算法 ID
     */
    public static String generate() {
        return snowflake.nextIdStr();
    }

    /**
     * 生成 UUID
     */
    public static String uuid() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}