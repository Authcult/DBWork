package org.example.databasework.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Date;

/**
 * Token黑名单服务
 * 用于管理已失效的JWT令牌
 */
@Service
public class TokenBlacklistService {
    
    // 使用ConcurrentHashMap存储已失效的令牌，键为令牌，值为过期时间
    private final Map<String, Date> blacklist = new ConcurrentHashMap<>();
    
    /**
     * 将令牌添加到黑名单
     * @param token 令牌
     * @param expiryDate 过期时间
     */
    public void addToBlacklist(String token, Date expiryDate) {
        blacklist.put(token, expiryDate);
    }
    
    /**
     * 检查令牌是否在黑名单中
     * @param token 令牌
     * @return 如果令牌在黑名单中，则返回true
     */
    public boolean isBlacklisted(String token) {
        return blacklist.containsKey(token);
    }
    
    /**
     * 清理过期的黑名单令牌
     * 此方法可以定期调用，以清理已过期的令牌
     */
    public void cleanupExpiredTokens() {
        Date now = new Date();
        blacklist.entrySet().removeIf(entry -> entry.getValue().before(now));
    }
}