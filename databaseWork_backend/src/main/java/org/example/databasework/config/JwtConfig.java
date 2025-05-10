package org.example.databasework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT配置类
 * 用于配置JWT相关参数
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * JWT密钥
     * 默认为空，实际应用中应在application.properties中配置
     */
    private String secret = "defaultSecretKeyNeedsToBeAtLeast32BytesLong12345678901234";

    /**
     * JWT令牌有效期（毫秒）
     * 默认为1小时
     */
    private long expiration = 3600000;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
}