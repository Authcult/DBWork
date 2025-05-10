package org.example.databasework.controller.admin;

import org.example.databasework.model.ApiResponse;
import org.example.databasework.model.Doctor;
import org.example.databasework.model.Department;
import org.example.databasework.model.Schedule;
import org.example.databasework.service.DoctorService;
import org.example.databasework.service.DepartmentService;
import org.example.databasework.util.JwtUtil;
import org.example.databasework.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/doctors")
public class DoctorController {
    private final DoctorService doctorService;
    private final DepartmentService departmentService;
    private final JwtUtil jwtUtil;

    @Autowired
    public DoctorController(DoctorService doctorService, DepartmentService departmentService, JwtUtil jwtUtil) {
        this.doctorService = doctorService;
        this.departmentService = departmentService;
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
     * 添加新医生
     * POST /admin/doctors
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> addDoctor(
            @RequestBody Map<String, Object> doctorRequest,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        // 从请求体中获取医生信息
        String name = (String) doctorRequest.get("name");
        String gender = (String) doctorRequest.get("gender");
        String title = (String) doctorRequest.get("title");
        String phone = (String) doctorRequest.get("phone");
        String password = (String) doctorRequest.get("password");
        Integer departmentId = (Integer) doctorRequest.get("departmentId");
        
        // 获取科室信息
        Department department = departmentService.getDepartmentById(departmentId);
        
        // 创建医生对象
        Doctor doctor = new Doctor();
        doctor.setName(name);
        doctor.setGender(gender);
        doctor.setTitle(title);
        doctor.setPhone(phone);
        doctor.setPassword(password);
        doctor.setDepartment(department);
        
        // 保存医生信息
        Doctor savedDoctor = doctorService.addDoctor(doctor);
        
        // 构建响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("doctorId", savedDoctor.getDoctorID());
        data.put("name", savedDoctor.getName());
        data.put("gender", savedDoctor.getGender());
        data.put("title", savedDoctor.getTitle());
        data.put("phone", savedDoctor.getPhone());
        data.put("departmentId", savedDoctor.getDepartment().getDepartmentID());
        data.put("departmentName", savedDoctor.getDepartment().getName());
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "医生信息添加成功");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 获取医生列表
     * GET /admin/doctors
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllDoctors(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        // 获取分页医生列表
        Page<Doctor> doctorPage = doctorService.getDoctorsByPage(page - 1, pageSize);
        
        // 使用PageUtils处理分页结果
        Map<String, Object> data = PageUtils.getPageData(doctorPage, page, pageSize, doctor -> {
            Map<String, Object> doctorMap = new HashMap<>();
            doctorMap.put("doctorId", doctor.getDoctorID());
            doctorMap.put("name", doctor.getName());
            doctorMap.put("gender", doctor.getGender());
            doctorMap.put("title", doctor.getTitle());
            doctorMap.put("phone", doctor.getPhone());
            doctorMap.put("departmentId", doctor.getDepartment().getDepartmentID());
            doctorMap.put("departmentName", doctor.getDepartment().getName());
            return doctorMap;
        });
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "获取医生列表成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 获取单个医生详情
     * GET /admin/doctors/{doctorId}
     */
    @GetMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDoctorById(
            @PathVariable Integer doctorId,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        Doctor doctor = doctorService.getDoctorById(doctorId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("doctorId", doctor.getDoctorID());
        data.put("name", doctor.getName());
        data.put("gender", doctor.getGender());
        data.put("title", doctor.getTitle());
        data.put("phone", doctor.getPhone());
        data.put("departmentId", doctor.getDepartment().getDepartmentID());
        data.put("departmentName", doctor.getDepartment().getName());
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "获取医生详情成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 修改医生信息
     * PUT /admin/doctors/{doctorId}
     */
    @PutMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateDoctor(
            @PathVariable Integer doctorId,
            @RequestBody Map<String, Object> doctorRequest,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        // 获取当前医生信息
        Doctor currentDoctor = doctorService.getDoctorById(doctorId);
        
        // 更新医生信息
        if (doctorRequest.containsKey("name")) {
            currentDoctor.setName((String) doctorRequest.get("name"));
        }
        if (doctorRequest.containsKey("gender")) {
            currentDoctor.setGender((String) doctorRequest.get("gender"));
        }
        if (doctorRequest.containsKey("title")) {
            currentDoctor.setTitle((String) doctorRequest.get("title"));
        }
        if (doctorRequest.containsKey("phone")) {
            currentDoctor.setPhone((String) doctorRequest.get("phone"));
        }
        if (doctorRequest.containsKey("departmentId")) {
            Integer departmentId = (Integer) doctorRequest.get("departmentId");
            Department department = departmentService.getDepartmentById(departmentId);
            currentDoctor.setDepartment(department);
        }
        
        // 如果请求中包含密码，则单独更新密码
        if (doctorRequest.containsKey("password")) {
            String password = (String) doctorRequest.get("password");
            doctorService.updateDoctorPassword(doctorId, password);
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
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "医生信息更新成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 删除医生信息
     * DELETE /admin/doctors/{doctorId}
     */
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(
            @PathVariable Integer doctorId,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        doctorService.deleteDoctor(doctorId);
        
        ApiResponse<Void> response = ApiResponse.success(null, "医生删除成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 为医生排班
     * POST /admin/doctors/{doctorId}/schedule
     */
    @PostMapping("/{doctorId}/schedule")
    public ResponseEntity<ApiResponse<Map<String, Object>>> addSchedule(
            @PathVariable Integer doctorId,
            @RequestBody Map<String, Object> scheduleRequest,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        // 从请求体中获取排班信息
        String workType = (String) scheduleRequest.get("workType");
        String startTimeStr = (String) scheduleRequest.get("startTime");
        String endTimeStr = (String) scheduleRequest.get("endTime");
        
        // 转换时间格式
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr.replace("Z", ""));
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr.replace("Z", ""));
        
        // 创建排班对象
        Schedule schedule = new Schedule();
        schedule.setWorkType(workType);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        
        // 保存排班信息
        Schedule savedSchedule = doctorService.addSchedule(doctorId, schedule);
        
        // 构建响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("scheduleId", savedSchedule.getScheduleID());
        data.put("doctorId", savedSchedule.getDoctor().getDoctorID());
        data.put("workType", savedSchedule.getWorkType());
        data.put("startTime", savedSchedule.getStartTime().toString());
        data.put("endTime", savedSchedule.getEndTime().toString());
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "排班成功");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * 获取医生排班列表
     * GET /admin/doctors/{doctorId}/schedule
     */
    @GetMapping("/{doctorId}/schedule")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDoctorSchedules(
            @PathVariable Integer doctorId,
            HttpServletRequest request) {
        validateAdminRole(request);
        
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
