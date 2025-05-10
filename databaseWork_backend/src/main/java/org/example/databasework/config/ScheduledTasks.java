package org.example.databasework.config;

import org.example.databasework.service.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 定时任务配置
 * 用于定期执行系统维护任务
 */
@Configuration
@EnableScheduling
public class ScheduledTasks {

    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    
    /**
     * 定期清理过期的黑名单令牌
     * 每小时执行一次
     */
    @Scheduled(fixedRate = 3600000) // 每小时执行一次
    public void cleanupExpiredTokens() {
        tokenBlacklistService.cleanupExpiredTokens();
    }
}