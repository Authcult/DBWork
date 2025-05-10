package org.example.databasework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置类
 * 用于解决前后端分离架构中的跨域问题
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许跨域的源
        config.addAllowedOriginPattern("*");
        // 允许跨域的请求头
        config.addAllowedHeader("*");
        // 允许跨域的请求方法
        config.addAllowedMethod("*");
        // 允许携带cookie
        config.setAllowCredentials(true);
        // 暴露响应头
        config.addExposedHeader("Authorization");
        // 预检请求的缓存时间
        config.setMaxAge(3600L);
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}