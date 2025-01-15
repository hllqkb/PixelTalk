package com.pixeltalk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data

@ConfigurationProperties(prefix = "sky")
public class SkyProperties {

    private Datasource datasource;
    private Redis redis;
    @Data
    public static class Datasource {
        private String driverClassName;
        private String host;
        private int port;
        private String database;
        private String username;
        private String password;
    }
    @Data
    public static class Redis {
        private String host;
        private int port;
        private String password;
        private int database;
    }
}
