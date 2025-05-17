package org.example.databasework.controller.doctor;

import jakarta.servlet.http.HttpServletRequest;
import org.example.databasework.model.ApiResponse;
import org.example.databasework.model.Doctor;
import org.example.databasework.model.HospitalizationDailyRecord;
import org.example.databasework.model.HospitalizationRecord;
import org.example.databasework.model.Patient;
import org.example.databasework.model.Ward;
import org.example.databasework.model.Bed;
import org.example.databasework.service.DoctorService;
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
 * 医生住院病人管理控制器
 */
@RestController
@RequestMapping("/doctor")
public class HospitalizationController {
    
    private final DoctorService doctorService;
    private final JwtUtil jwtUtil;
    
    @Autowired
    public HospitalizationController(DoctorService doctorService, JwtUtil jwtUtil) {
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
     * 获取当前医生负责的主治住院病人列表
     * GET /doctor/hospitalization-records/my-patients
     */
    @GetMapping("/hospitalization-records/my-patients")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMyHospitalizedPatients(HttpServletRequest request) {
        Integer doctorId = validateDoctorRole(request);
        
        // 获取医生负责的住院病人列表
        // 注意：这里需要实现相应的服务方法
        List<HospitalizationRecord> records = findHospitalizationRecordsByDoctor(doctorId);
        
        // 转换为前端需要的格式
        List<Map<String, Object>> recordList = records.stream().map(record -> {
            Map<String, Object> recordMap = new HashMap<>();
            recordMap.put("recordId", record.getRecordID());
            recordMap.put("patientId", record.getPatient().getPatientID());
            recordMap.put("patientName", record.getPatient().getName());
            recordMap.put("wardId", record.getWard().getWardID());
            recordMap.put("wardLocation", record.getWard().getLocation());
            recordMap.put("bedId", record.getBed().getBedID());
            recordMap.put("bedNumber", record.getBed().getBedNumber());
            recordMap.put("admissionDate", record.getAdmissionDate().toString());
            recordMap.put("dischargeDate", record.getDischargeDate() != null ? record.getDischargeDate().toString() : null);
            return recordMap;
        }).collect(Collectors.toList());
        
        // 构建响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("total", records.size());
        data.put("list", recordList);
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "获取住院病人列表成功");
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取住院病人详情
     * GET /doctor/hospitalization-records/{recordId}
     */
    @GetMapping("/hospitalization-records/{recordId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getHospitalizationRecordDetail(
            @PathVariable Integer recordId,
            HttpServletRequest request) {
        Integer doctorId = validateDoctorRole(request);
        
        // 获取住院记录详情
        // 注意：这里需要实现相应的服务方法
        HospitalizationRecord record = getHospitalizationRecordById(recordId, doctorId);
        
        // 获取每日诊疗记录
        List<HospitalizationDailyRecord> dailyRecords = getDailyRecordsByHospitalizationRecord(recordId);
        
        // 转换每日诊疗记录为前端需要的格式
        List<Map<String, Object>> dailyRecordList = dailyRecords.stream().map(dailyRecord -> {
            Map<String, Object> dailyRecordMap = new HashMap<>();
            dailyRecordMap.put("dailyRecordId", dailyRecord.getDailyRecordID());
            dailyRecordMap.put("date", dailyRecord.getDate().toString());
            dailyRecordMap.put("treatmentPlan", dailyRecord.getTreatmentPlan());
            return dailyRecordMap;
        }).collect(Collectors.toList());
        
        // 构建响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("recordId", record.getRecordID());
        data.put("patientId", record.getPatient().getPatientID());
        data.put("patientName", record.getPatient().getName());
        data.put("attendingDoctorId", record.getDoctor().getDoctorID());
        data.put("attendingDoctorName", record.getDoctor().getName());
        data.put("wardId", record.getWard().getWardID());
        data.put("wardLocation", record.getWard().getLocation());
        data.put("bedId", record.getBed().getBedID());
        data.put("bedNumber", record.getBed().getBedNumber());
        data.put("admissionDate", record.getAdmissionDate().toString());
        data.put("dischargeDate", record.getDischargeDate() != null ? record.getDischargeDate().toString() : null);
        data.put("dailyRecords", dailyRecordList);
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "获取住院记录详情成功");
        return ResponseEntity.ok(response);
    }
    
    /**
     * 办理住院手续（建立住院档案）
     * POST /doctor/hospitalization-records
     */
    @PostMapping("/hospitalization-records")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createHospitalizationRecord(
            @RequestBody Map<String, Object> recordRequest,
            HttpServletRequest request) {
        Integer doctorId = validateDoctorRole(request);
        
        // 从请求体中获取住院信息
        Integer patientId = (Integer) recordRequest.get("patientId");
        Integer attendingDoctorId = (Integer) recordRequest.get("attendingDoctorId");
        Integer wardId = (Integer) recordRequest.get("wardId");
        Integer bedId = (Integer) recordRequest.get("bedId");
        String admissionDateStr = (String) recordRequest.get("admissionDate");
        
        // 转换日期格式
        LocalDate admissionDate = LocalDate.parse(admissionDateStr);
        
        // 创建住院记录
        // 注意：这里需要实现相应的服务方法
        HospitalizationRecord record = createHospitalizationRecord(patientId, attendingDoctorId, wardId, bedId, admissionDate);
        
        // 构建响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("recordId", record.getRecordID());
        data.put("patientId", record.getPatient().getPatientID());
        data.put("patientName", record.getPatient().getName());
        data.put("attendingDoctorId", record.getDoctor().getDoctorID());
        data.put("attendingDoctorName", record.getDoctor().getName());
        data.put("wardId", record.getWard().getWardID());
        data.put("wardLocation", record.getWard().getLocation());
        data.put("bedId", record.getBed().getBedID());
        data.put("bedNumber", record.getBed().getBedNumber());
        data.put("admissionDate", record.getAdmissionDate().toString());
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "住院手续办理成功");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * 为住院病人添加每日诊疗记录
     * POST /doctor/hospitalization-records/{recordId}/daily-records
     */
    @PostMapping("/hospitalization-records/{recordId}/daily-records")
    public ResponseEntity<ApiResponse<Map<String, Object>>> addDailyRecord(
            @PathVariable Integer recordId,
            @RequestBody Map<String, String> dailyRecordRequest,
            HttpServletRequest request) {
        Integer doctorId = validateDoctorRole(request);
        
        // 从请求体中获取诊疗记录信息
        String treatmentPlan = dailyRecordRequest.get("treatmentPlan");
        
        // 创建每日诊疗记录
        // 注意：这里需要实现相应的服务方法
        HospitalizationDailyRecord dailyRecord = createDailyRecord(recordId, treatmentPlan, doctorId);
        
        // 构建响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("dailyRecordId", dailyRecord.getDailyRecordID());
        data.put("recordId", dailyRecord.getRecord().getRecordID());
        data.put("date", dailyRecord.getDate().toString());
        data.put("treatmentPlan", dailyRecord.getTreatmentPlan());
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "每日诊疗记录添加成功");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * 办理病人出院
     * PUT /doctor/hospitalization-records/{recordId}/discharge
     */
    @PutMapping("/hospitalization-records/{recordId}/discharge")
    public ResponseEntity<ApiResponse<Map<String, Object>>> dischargePatient(
            @PathVariable Integer recordId,
            @RequestBody Map<String, String> dischargeRequest,
            HttpServletRequest request) {
        Integer doctorId = validateDoctorRole(request);
        
        // 从请求体中获取出院日期
        String dischargeDateStr = dischargeRequest.get("dischargeDate");
        LocalDate dischargeDate = LocalDate.parse(dischargeDateStr);
        
        // 办理出院
        // 注意：这里需要实现相应的服务方法
        HospitalizationRecord record = dischargePatient(recordId, dischargeDate, doctorId);
        
        // 获取每日诊疗记录
        List<HospitalizationDailyRecord> dailyRecords = getDailyRecordsByHospitalizationRecord(recordId);
        
        // 转换每日诊疗记录为前端需要的格式
        List<Map<String, Object>> dailyRecordList = dailyRecords.stream().map(dailyRecord -> {
            Map<String, Object> dailyRecordMap = new HashMap<>();
            dailyRecordMap.put("dailyRecordId", dailyRecord.getDailyRecordID());
            dailyRecordMap.put("date", dailyRecord.getDate().toString());
            dailyRecordMap.put("treatmentPlan", dailyRecord.getTreatmentPlan());
            return dailyRecordMap;
        }).collect(Collectors.toList());
        
        // 构建响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("recordId", record.getRecordID());
        data.put("patientId", record.getPatient().getPatientID());
        data.put("patientName", record.getPatient().getName());
        data.put("attendingDoctorId", record.getDoctor().getDoctorID());
        data.put("attendingDoctorName", record.getDoctor().getName());
        data.put("wardId", record.getWard().getWardID());
        data.put("wardLocation", record.getWard().getLocation());
        data.put("bedId", record.getBed().getBedID());
        data.put("bedNumber", record.getBed().getBedNumber());
        data.put("admissionDate", record.getAdmissionDate().toString());
        data.put("dischargeDate", record.getDischargeDate().toString());
        data.put("dailyRecords", dailyRecordList);
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "病人出院手续办理成功");
        return ResponseEntity.ok(response);
    }
    
    // 以下是需要实现的辅助方法
    
    /**
     * 根据医生ID查询住院记录列表
     */
    private List<HospitalizationRecord> findHospitalizationRecordsByDoctor(Integer doctorId) {
        return doctorService.getHospitalizationRecordsByDoctor(doctorId);
    }
    
    /**
     * 根据ID获取住院记录
     */
    private HospitalizationRecord getHospitalizationRecordById(Integer recordId, Integer doctorId) {
        return doctorService.getHospitalizationRecordById(recordId, doctorId);
    }
    
    /**
     * 根据住院记录ID获取每日诊疗记录列表
     */
    private List<HospitalizationDailyRecord> getDailyRecordsByHospitalizationRecord(Integer recordId) {
        return doctorService.getDailyRecordsByHospitalizationRecord(recordId);
    }
    
    /**
     * 创建住院记录
     */
    private HospitalizationRecord createHospitalizationRecord(Integer patientId, Integer attendingDoctorId, Integer wardId, Integer bedId, LocalDate admissionDate) {
        return doctorService.createHospitalizationRecord(patientId, attendingDoctorId, wardId, bedId, admissionDate);
    }
    
    /**
     * 创建每日诊疗记录
     */
    private HospitalizationDailyRecord createDailyRecord(Integer recordId, String treatmentPlan, Integer doctorId) {
        return doctorService.createDailyRecord(recordId, treatmentPlan, doctorId);
    }
    
    /**
     * 办理病人出院
     */
    private HospitalizationRecord dischargePatient(Integer recordId, LocalDate dischargeDate, Integer doctorId) {
        return doctorService.dischargePatient(recordId, dischargeDate, doctorId);
    }
}