package com.example.demo1.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(DemoConfiguration.Security.class)
public class DemoConfiguration {

    @ConfigurationProperties(prefix = "demo1.security")
    public static class Security {
        private List<String> permitAllPath;

        public List<String> getPermitAllPath() {
            return permitAllPath;
        }

        public void setPermitAllPath(List<String> permitAllPath) {
            this.permitAllPath = permitAllPath;
        }
    }
}
