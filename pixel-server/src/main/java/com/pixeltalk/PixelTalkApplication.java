package com.pixeltalk;

import cn.dev33.satoken.SaTokenManager;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@Slf4j
@MapperScan("com.pixeltalk.mapper")//扫描mybatis-plus的mapper接口
public class PixelTalkApplication {
    public static void main(String[] args) {
        SpringApplication.run(PixelTalkApplication.class, args);
        log.info("欢迎使用PixelTalk!");
        log.info("sa-token配置如下：" + SaTokenManager.getConfig());
    }
}
