package com.pixeltalk;

import cn.dev33.satoken.SaTokenManager;
import com.pixeltalk.websocket.NettyWebSocketStarter;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@Slf4j
@MapperScan("com.pixeltalk.mapper")//扫描mybatis-plus的mapper接口
public class PixelTalkApplication {

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(PixelTalkApplication.class, args);
        PixelTalkApplication application = context.getBean(PixelTalkApplication.class);
        if (application.checkDatabaseConnection()) {
            log.info("欢迎使用PixelTalk!");
            log.info("sa-token配置如下：" + SaTokenManager.getConfig());
            NettyWebSocketStarter nettyWebSocketStarter = context.getBean(NettyWebSocketStarter.class);
            nettyWebSocketStarter.startNetty();
        } else {
            log.error("数据库连接失败，PixelTalk无法启动!");
            System.exit(1);
        }
    }

    private boolean checkDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            log.info("数据库连接成功!");
            return true;
        } catch (SQLException e) {
            log.error("数据库连接失败: ", e);
            return false;
        }
    }
}
