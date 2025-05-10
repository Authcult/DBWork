package org.example.databasework.controller;

import org.example.databasework.model.Admin;
import org.example.databasework.model.ApiResponse;
import org.example.databasework.model.Doctor;
import org.example.databasework.model.Patient;
import org.example.databasework.service.TokenBlacklistService;
import org.example.databasework.service.UserService;
import org.example.databasework.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Hashtable<String, Object>>> register(@RequestBody Map<String, Object> params) {
        // 查询用户是否已存在
        Patient u = userService.findByUsername(params.get("username").toString());
        if (u != null) {
            return ResponseEntity.status(400).body(ApiResponse.error("USER_EXISTS", "用户已存在"));
        } else {
            // 创建新用户
            Patient n=userService.createUser(
                params.get("name").toString(),
                params.get("gender").toString(),
                params.get("address").toString(),
                params.get("phone").toString(),
                params.get("username").toString(),
                params.get("password").toString()
            );
            Hashtable<String,Object> data= new Hashtable<>();
            data.put("patientID",n.getPatientID());
            data.put("username",n.getUsername());
            return ResponseEntity.status(201).body(ApiResponse.success(data,"注册成功"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Hashtable<String, Object>>> login(@RequestBody Map<String, Object> params) {
        String username = params.get("username").toString();
        String password = params.get("password").toString();
        String role = params.get("role").toString();
        
        // 根据角色查找用户
        Object user = userService.findUserByCredentials(username, password, role);
        
        if (user == null) {
            return ResponseEntity.status(400).body(ApiResponse.error("USER_NOT_EXISTS", "用户不存在"));
        } else {
            Hashtable<String, Object> data = new Hashtable<>();
            Hashtable<String, Object> userData = new Hashtable<>();
            
            // 根据不同角色设置返回数据
            if ("patient".equals(role)) {
                Patient patient = (Patient) user;
                userData.put("id", patient.getPatientID());
                userData.put("username", patient.getUsername());
                userData.put("role", "patient");
                userData.put("name", patient.getName());
            } else if ("doctor".equals(role)) {
                Doctor doctor = (Doctor) user;
                userData.put("id", doctor.getDoctorID());
                userData.put("username", doctor.getDoctorID().toString());
                userData.put("role", "doctor");
                userData.put("name", doctor.getName());
            } else if ("admin".equals(role)) {
                Admin admin = (Admin) user;
                userData.put("id", admin.getAdminID());
                userData.put("username", admin.getUsername());
                userData.put("role", "admin");
                userData.put("name", admin.getFullName());
            }

            // 生成JWT token
            String userId = userData.get("id").toString();
            String userUsername = userData.get("username").toString();
            String userRole = userData.get("role").toString();
            String token = jwtUtil.generateToken(userUsername, userRole, userId);

            // 设置token信息
            data.put("token", token);
            data.put("expiresIn", jwtUtil.getTokenExpirationInMillis() / 1000); // 转换为秒
            data.put("user", userData);
            
            return ResponseEntity.status(200).body(ApiResponse.success(data, "登录成功"));
        }
    }
    
    /**
     * 用户登出
     * @param authorization 请求头中的Authorization字段
     * @return 登出结果
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(@RequestHeader("Authorization") String authorization) {
        // 检查Authorization头是否存在并且格式正确
        if (authorization != null && authorization.startsWith("Bearer ")) {
            // 提取token
            String token = authorization.substring(7);
            
            try {
                // 获取token的过期时间
                Date expiryDate = jwtUtil.getExpirationDateFromToken(token);
                
                // 将token添加到黑名单
                tokenBlacklistService.addToBlacklist(token, expiryDate);
                
                // 返回登出成功信息
                return ResponseEntity.ok(ApiResponse.success(null, "登出成功"));
            } catch (Exception e) {
                // 如果token无效，也视为登出成功
                return ResponseEntity.ok(ApiResponse.success(null, "登出成功"));
            }
        }
        
        // 如果没有提供有效的token，返回错误信息
        return ResponseEntity.status(401).body(ApiResponse.error("INVALID_TOKEN", "无效的认证信息"));
    }

}
