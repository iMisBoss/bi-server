package com.neusoft.common.xinchuang.detector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 信创环境检测器
 */
@Slf4j
@Component
public class XinchuangDetector {

    /**
     * 检测是否运行在信创环境
     */
    public boolean isXinchuangEnvironment() {
        String osName = System.getProperty("os.name").toLowerCase();
        String osArch = System.getProperty("os.arch").toLowerCase();
        String javaVendor = System.getProperty("java.vendor").toLowerCase();

        log.info("========== 信创环境检测 ==========");
        log.info("操作系统：{}", osName);
        log.info("CPU 架构：{}", osArch);
        log.info("JDK 厂商：{}", javaVendor);

        boolean isDomesticOS = osName.contains("kylin") ||
                               osName.contains("uos") ||
                               osName.contains("euleros");

        boolean isDomesticCPU = osArch.contains("aarch64") ||
                                osArch.contains("loongarch");

        boolean isDomesticJDK = javaVendor.contains("huawei") ||
                                javaVendor.contains("dragonwell");

        boolean isXinchuang = isDomesticOS && (isDomesticCPU || isDomesticJDK);

        log.info("信创环境检测结果：{}", isXinchuang ? "✅ 是" : "❌ 否");
        log.info("====================================");

        return isXinchuang;
    }

    /**
     * 获取信创环境信息
     */
    public Map<String, String> getXinchuangInfo() {
        Map<String, String> info = new LinkedHashMap<>();

        info.put("os.name", System.getProperty("os.name"));
        info.put("os.version", System.getProperty("os.version"));
        info.put("os.arch", System.getProperty("os.arch"));
        info.put("java.vendor", System.getProperty("java.vendor"));
        info.put("java.version", System.getProperty("java.version"));
        info.put("xinchuang.enabled", String.valueOf(isXinchuangEnvironment()));

        return info;
    }
}
