package org.example.databasework.controller.doctor;

import jakarta.servlet.http.HttpServletRequest;
import org.example.databasework.model.ApiResponse;
import org.example.databasework.model.Doctor;
import org.example.databasework.model.OutpatientRegistration;
import org.example.databasework.model.Prescription;
import org.example.databasework.service.DoctorService;
import org.example.databasework.service.OutpatientService;
import org.example.databasework.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 医生门诊接诊控制器
 */
@RestController
@RequestMapping("/doctor")
public class OutpatientController {
    
    private final DoctorService doctorService;
    private final JwtUtil jwtUtil;
    private final OutpatientService outpatientService;
    
    @Autowired
    public OutpatientController(DoctorService doctorService, JwtUtil jwtUtil, OutpatientService outpatientService) {
        this.doctorService = doctorService;
        this.jwtUtil = jwtUtil;
        this.outpatientService = outpatientService;
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
     * 获取当前医生今日或指定日期的门诊挂号列表
     * GET /doctor/registrations
     */
    @GetMapping("/registrations")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDoctorRegistrations(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpServletRequest request) {

        Integer doctorId = validateDoctorRole(request);
        
        // 如果未指定日期，则使用当前日期
        LocalDate queryDate = date != null ? date : LocalDate.now();
        
        // 获取医生当日挂号列表
        // 注意：这里需要实现相应的服务方法
        List<OutpatientRegistration> registrations = findRegistrationsByDoctorAndDate(doctorId, queryDate);
        
        // 转换为前端需要的格式
        List<Map<String, Object>> registrationList = registrations.stream().map(reg -> {
            Map<String, Object> regMap = new HashMap<>();
            regMap.put("registrationId", reg.getRegistrationID());
            regMap.put("patientId", reg.getPatient().getPatientID());
            regMap.put("patientName", reg.getPatient().getName());
            regMap.put("registrationTime", reg.getRegistrationTime().toString());
            regMap.put("status", reg.getStatus());
            return regMap;
        }).collect(Collectors.toList());
        
        // 构建响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("date", queryDate.toString());
        data.put("total", registrations.size());
        data.put("list", registrationList);
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "获取挂号列表成功");
        return ResponseEntity.ok(response);
    }
    
    /**
     * 医生开始接诊病人
     * POST /doctor/registrations/{registrationId}/start-consultation
     */
    @PostMapping("/registrations/{registrationId}/start-consultation")
    public ResponseEntity<ApiResponse<Map<String, Object>>> startConsultation(
            @PathVariable Integer registrationId,
            HttpServletRequest request) {
        Integer doctorId = validateDoctorRole(request);
        
        // 更新挂号状态为正在接诊
        // 注意：这里需要实现相应的服务方法
        OutpatientRegistration registration = updateRegistrationStatus(registrationId, "consulting", doctorId);
        
        // 构建响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("registrationId", registration.getRegistrationID());
        data.put("patientId", registration.getPatient().getPatientID());
        data.put("patientName", registration.getPatient().getName());
        data.put("status", registration.getStatus());
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "开始接诊成功");
        return ResponseEntity.ok(response);
    }
    
    /**
     * 医生完成接诊
     * POST /doctor/registrations/{registrationId}/complete-consultation
     */
    @PostMapping("/registrations/{registrationId}/complete-consultation")
    public ResponseEntity<ApiResponse<Map<String, Object>>> completeConsultation(
            @PathVariable Integer registrationId,
            HttpServletRequest request) {
        Integer doctorId = validateDoctorRole(request);

        
        // 更新挂号状态为已完成
        OutpatientRegistration registration = updateRegistrationStatus(registrationId, "completed", doctorId);

        Map<String, Object> data = new HashMap<>();
        data.put("registrationId", registration.getRegistrationID());
        data.put("patientId", registration.getPatient().getPatientID());
        data.put("patientName", registration.getPatient().getName());
        data.put("status", registration.getStatus());
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data,"接诊完成，处方已创建");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 查看指定病人的过往就诊记录
     * GET /doctor/patients/{patientId}/history
     */
    @GetMapping("/patients/{patientId}/history")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPatientHistory(
            @PathVariable Integer patientId,
            HttpServletRequest request) {
        Integer doctorId = validateDoctorRole(request);

        // 获取指定病人过往就诊记录
        // 注意：这里需要实现相应服务方法
        List<OutpatientRegistration> registrations = findRegistrationsByPatient(patientId);

        // 构建响应数据
        List<Map<String, Object>> registrationList = registrations.stream().map(reg -> {
            Map<String, Object> regMap = new HashMap<>();
            regMap.put("registrationId", reg.getRegistrationID());
            regMap.put("patientId", reg.getPatient().getPatientID());
            regMap.put("patientName", reg.getPatient().getName());
            regMap.put("registrationTime", reg.getRegistrationTime().toString());
            regMap.put("status", reg.getStatus());
            return regMap;
        }).collect(Collectors.toList());
        Map<String, Object> data = new HashMap<>();
        data.put("total", registrations.size());
        data.put("list", registrationList);
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "获取病人过往就诊记录成功");
        return ResponseEntity.ok(response);
    }

    private List<OutpatientRegistration> findRegistrationsByPatient(Integer patientId) {
        return outpatientService.getRegistrationsByPatientId(patientId);
    }


    // 以下是需要实现的辅助方法
    
    /**
     * 根据医生ID和日期查询挂号列表
     */
    private List<OutpatientRegistration> findRegistrationsByDoctorAndDate(Integer doctorId, LocalDate date) {
        return doctorService.getRegistrationsByDoctorAndDate(doctorId, date);
    }
    
    /**
     * 更新挂号状态
     */
    private OutpatientRegistration updateRegistrationStatus(Integer registrationId, String status, Integer doctorId) {
        return doctorService.updateRegistrationStatus(registrationId, status, doctorId);
    }
    
    /**
     * 创建处方
     */
    private Prescription createPrescription(Integer registrationId, String symptomDescription, Double diagnosisFee, List<Map<String, Object>> items, Integer doctorId) {
        return doctorService.createPrescription(registrationId, symptomDescription, diagnosisFee, items, doctorId);
    }
}