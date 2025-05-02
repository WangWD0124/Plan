package com.plan.work.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Copyright: Copyright (c) 2025 Asiainfo
 *
 * @ClassName: com.plan.work.config.WebConfig
 * @Description:
 * @version: v1.0.0
 * @author: wangwd7
 * @date: 2025-04-30
 * <p>
 * Modification History:
 * Date         Author          Version            Description
 * ---------------------------------------------------------*
 * 2025-04-30     wangwd7          v1.0.0               创建
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/item-records/**")
                .allowedOrigins("http://localhost") // 根据前端实际地址修改
                .allowedMethods("GET", "POST");
    }
}
