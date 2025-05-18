package org.example.databasework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 * 用于注册拦截器和配置拦截路径
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtAuthInterceptor jwtAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册JWT认证拦截器
        registry.addInterceptor(jwtAuthInterceptor)
                // 拦截所有请求
                .addPathPatterns("/**")
                // 排除不需要验证的路径
                .excludePathPatterns(
                        "/public/**",
                        "/auth/login",     // 登录接口
                        "/auth/register",  // 注册接口
                        "/error",          // 错误页面
                        "/swagger-ui/**",  // Swagger UI
                        "/v3/api-docs/**"  // OpenAPI文档
                );
    }
}