package com.newtouch.oa;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 建信消金 OA 系统启动类
 *
 * @author 新致软件
 */
@Slf4j
@SpringBootApplication
@EnableScheduling
@MapperScan("com.newtouch.oa.mapper")
@EnableAsync
public class OaApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(OaApplication.class, args);

        String env = application.getEnvironment().getProperty("spring.profiles.active");
        String port = application.getEnvironment().getProperty("server.port");
        String contextPath = application.getEnvironment().getProperty("server.servlet.context-path", "");
        String localHostAddress = InetAddress.getLocalHost().getHostAddress();

        String localUrl = String.format("http://localhost:%s%s", port, contextPath);
        String networkUrl = String.format("http://%s:%s%s", localHostAddress, port, contextPath);

        log.info("\n" +
                        "----------------------------------------------------------\n" +
                        "\t建信消金 OA 系统启动成功！\n" +
                        "\t环境：{}\n" +
                        "\t地址：{}\n" +
                        "\t地址：{}\n" +
                        "\t文档：{}/doc.html\n" +
                        "----------------------------------------------------------",
                env, localUrl, networkUrl, contextPath);
    }
}
