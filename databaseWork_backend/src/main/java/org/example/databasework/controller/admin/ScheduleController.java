package org.example.databasework.controller.admin;

import org.example.databasework.model.ApiResponse;
import org.example.databasework.model.Schedule;
import org.example.databasework.service.DoctorService;
import org.example.databasework.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/schedules")
public class ScheduleController {
    private final DoctorService doctorService;
    private final JwtUtil jwtUtil;

    @Autowired
    public ScheduleController(DoctorService doctorService, JwtUtil jwtUtil) {
        this.doctorService = doctorService;
        this.jwtUtil = jwtUtil;
    }
    
    /**
     * 验证是否为管理员角色
     */
    private void validateAdminRole(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String role = jwtUtil.getClaimFromToken(token, claims -> claims.get("role", String.class));
            if (!"admin".equals(role)) {
                throw new RuntimeException("权限不足，需要管理员权限");
            }
        } else {
            throw new RuntimeException("未提供有效的认证信息");
        }
    }

    /**
     * 修改排班信息
     * PUT /admin/schedules/{scheduleId}
     */
    @PutMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateSchedule(
            @PathVariable Integer scheduleId,
            @RequestBody Map<String, Object> scheduleRequest,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        // 获取当前排班信息
        Schedule schedule = doctorService.getScheduleById(scheduleId);
        if (schedule == null) {
            return ResponseEntity.status(404).body(ApiResponse.error("SCHEDULE_NOT_FOUND", "排班信息不存在"));
        }
        
        // 从请求体中获取排班信息
        if (scheduleRequest.containsKey("workType")) {
            schedule.setWorkType((String) scheduleRequest.get("workType"));
        }
        
        if (scheduleRequest.containsKey("startTime")) {
            String startTimeStr = (String) scheduleRequest.get("startTime");
            LocalDateTime startTime = LocalDateTime.parse(startTimeStr.replace("Z", ""));
            schedule.setStartTime(startTime);
        }
        
        if (scheduleRequest.containsKey("endTime")) {
            String endTimeStr = (String) scheduleRequest.get("endTime");
            LocalDateTime endTime = LocalDateTime.parse(endTimeStr.replace("Z", ""));
            schedule.setEndTime(endTime);
        }
        
        // 更新排班信息
        Schedule updatedSchedule = doctorService.updateSchedule(scheduleId, schedule);
        
        // 构建响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("scheduleId", updatedSchedule.getScheduleID());
        data.put("doctorId", updatedSchedule.getDoctor().getDoctorID());
        data.put("workType", updatedSchedule.getWorkType());
        data.put("startTime", updatedSchedule.getStartTime().toString());
        data.put("endTime", updatedSchedule.getEndTime().toString());
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "排班信息更新成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 删除排班
     * DELETE /admin/schedules/{scheduleId}
     */
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<Void>> deleteSchedule(
            @PathVariable Integer scheduleId,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        // 检查排班是否存在
        Schedule schedule = doctorService.getScheduleById(scheduleId);
        if (schedule == null) {
            return ResponseEntity.status(404).body(ApiResponse.error("SCHEDULE_NOT_FOUND", "排班信息不存在"));
        }
        
        // 删除排班
        doctorService.deleteSchedule(scheduleId);
        
        ApiResponse<Void> response = ApiResponse.success(null, "排班删除成功");
        return ResponseEntity.ok(response);
    }
}