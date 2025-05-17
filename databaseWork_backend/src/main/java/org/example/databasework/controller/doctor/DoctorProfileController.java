package org.example.databasework.controller.doctor;

import jakarta.servlet.http.HttpServletRequest;
import org.example.databasework.model.ApiResponse;
import org.example.databasework.model.Doctor;
import org.example.databasework.model.Schedule;
import org.example.databasework.service.DoctorService;
import org.example.databasework.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 医生个人信息与排班控制器
 */
@RestController
@RequestMapping("/doctor")
public class DoctorProfileController {
    
    private final DoctorService doctorService;
    private final JwtUtil jwtUtil;
    
    @Autowired
    public DoctorProfileController(DoctorService doctorService, JwtUtil jwtUtil) {
        this.doctorService = doctorService;
        this.jwtUtil = jwtUtil;
    }
    
    /**
     * 验证是否为医生角色
     */
    private Integer validateDoctorRole(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String role = jwtUtil.getClaimFromToken(token, claims -> claims.get("role", String.class));
            if (!"doctor".equals(role)) {
                throw new RuntimeException("权限不足，需要医生权限");
            }
            return Integer.valueOf(jwtUtil.getClaimFromToken(token, claims -> claims.get("userId", String.class)));
        } else {
            throw new RuntimeException("未提供有效的认证信息");
        }
    }
    
    /**
     * 获取当前登录医生的个人信息
     * GET /doctor/profile
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDoctorProfile(HttpServletRequest request) {
        Integer doctorId = validateDoctorRole(request);
        Doctor doctor = doctorService.getDoctorById(doctorId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("doctorId", doctor.getDoctorID());
        data.put("name", doctor.getName());
        data.put("gender", doctor.getGender());
        data.put("title", doctor.getTitle());
        data.put("phone", doctor.getPhone());
        data.put("departmentId", doctor.getDepartment().getDepartmentID());
        data.put("departmentName", doctor.getDepartment().getName());
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "获取医生个人信息成功");
        return ResponseEntity.ok(response);
    }
    
    /**
     * 修改当前登录医生的个人信息
     * PUT /doctor/profile
     */
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateDoctorProfile(
            @RequestBody Map<String, Object> profileRequest,
            HttpServletRequest request) {
        Integer doctorId = validateDoctorRole(request);
        
        // 获取当前医生信息
        Doctor currentDoctor = doctorService.getDoctorById(doctorId);
        
        // 更新医生信息
        if (profileRequest.containsKey("phone")) {
            currentDoctor.setPhone((String) profileRequest.get("phone"));
        }
        
        // 保存更新后的医生信息
        Doctor updatedDoctor = doctorService.updateDoctor(doctorId, currentDoctor);
        
        // 构建响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("doctorId", updatedDoctor.getDoctorID());
        data.put("name", updatedDoctor.getName());
        data.put("gender", updatedDoctor.getGender());
        data.put("title", updatedDoctor.getTitle());
        data.put("phone", updatedDoctor.getPhone());
        data.put("departmentId", updatedDoctor.getDepartment().getDepartmentID());
        data.put("departmentName", updatedDoctor.getDepartment().getName());
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "医生个人信息更新成功");
        return ResponseEntity.ok(response);
    }
    
    /**
     * 医生修改自己的登录密码
     * POST /doctor/profile/change-password
     */
    @PostMapping("/profile/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestBody Map<String, String> passwordRequest,
            HttpServletRequest request) {
        Integer doctorId = validateDoctorRole(request);
        
        String oldPassword = passwordRequest.get("oldPassword");
        String newPassword = passwordRequest.get("newPassword");
        
        // 验证旧密码是否正确
        Doctor doctor = doctorService.getDoctorById(doctorId);
        if (!doctor.getPassword().equals(oldPassword)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400","旧密码不正确"));
        }
        
        // 更新密码
        doctorService.updateDoctorPassword(doctorId, newPassword);
        
        ApiResponse<Void> response = ApiResponse.success(null, "密码修改成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 获取当前登录医生的排班信息
     * GET /doctor/schedule
     */
    @GetMapping("/schedule")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDoctorSchedules(
            HttpServletRequest request) {
        Integer doctorId=validateDoctorRole(request);

        // 获取医生排班列表
        List<Schedule> schedules = doctorService.getDoctorSchedules(doctorId);

        // 转换为前端需要的格式
        List<Map<String, Object>> scheduleList = schedules.stream().map(schedule -> {
            Map<String, Object> scheduleMap = new HashMap<>();
            scheduleMap.put("scheduleId", schedule.getScheduleID());
            scheduleMap.put("doctorId", schedule.getDoctor().getDoctorID());
            scheduleMap.put("workType", schedule.getWorkType());
            scheduleMap.put("startTime", schedule.getStartTime().toString());
            scheduleMap.put("endTime", schedule.getEndTime().toString());
            return scheduleMap;
        }).collect(Collectors.toList());

        // 构建响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("total", schedules.size());
        data.put("list", scheduleList);

        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "获取医生排班列表成功");
        return ResponseEntity.ok(response);
    }
}