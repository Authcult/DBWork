package org.example.databasework.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.databasework.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT认证拦截器
 * 用于拦截请求并验证JWT令牌
 */
@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取Authorization头
        String authHeader = request.getHeader("Authorization");
        
        // 对于OPTIONS请求放行（预检请求）
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        
        // 检查是否存在Authorization头，并且是否以Bearer开头
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // 提取token
            String token = authHeader.substring(7);
            
            // 验证token
            if (jwtUtil.validateToken(token)) {
                // token有效，将用户信息存入请求属性中，以便后续使用
                String username = jwtUtil.getUsernameFromToken(token);
                request.setAttribute("username", username);
                return true;
            }
        }
        
        // 如果没有token或token无效，返回401未授权状态码
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":\"UNAUTHORIZED\",\"message\":\"未授权访问，请先登录\"}");
        return false;
    }
}