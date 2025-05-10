package org.example.databasework.controller;

import org.example.databasework.model.ApiResponse;
import org.example.databasework.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Token控制器
 * 用于处理与Token相关的请求
 */
@RestController
@RequestMapping("/api/token")
public class TokenController {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 验证Token有效性
     * @param authorization 请求头中的Authorization字段
     * @return 验证结果
     */
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Hashtable<String, Object>>> verifyToken(
            @RequestHeader("Authorization") String authorization) {
        
        // 检查Authorization头是否存在并且格式正确
        if (authorization != null && authorization.startsWith("Bearer ")) {
            // 提取token
            String token = authorization.substring(7);
            
            // 验证token
            if (jwtUtil.validateToken(token)) {
                // 获取token中的信息
                String username = jwtUtil.getUsernameFromToken(token);
                
                // 返回验证成功信息
                Hashtable<String, Object> data = new Hashtable<>();
                data.put("valid", true);
                data.put("username", username);
                return ResponseEntity.ok(ApiResponse.success(data, "Token有效"));
            }
        }
        
        // 返回验证失败信息
        Hashtable<String, Object> data = new Hashtable<>();
        data.put("valid", false);
        return ResponseEntity.status(401).body(ApiResponse.error("INVALID_TOKEN", "Token无效或已过期"));
    }
}