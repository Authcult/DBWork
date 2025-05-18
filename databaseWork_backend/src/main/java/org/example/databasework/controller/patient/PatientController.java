package org.example.databasework.controller.patient;

import org.example.databasework.model.*;
import org.example.databasework.service.*;
import org.example.databasework.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 病人接口控制器
 */
@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private UserService userService;

    @Autowired
    private OutpatientService outpatientService;

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private HospitalizationService hospitalizationService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PaymentService paymentService;


    /**
     * 验证病人身份
     */
    private Integer validatePatientRole(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String role = jwtUtil.getClaimFromToken(token, claims -> claims.get("role", String.class));
            if (!"patient".equals(role)) {
                throw new RuntimeException("权限不足，需要病人权限");
            }
            return Integer.valueOf(jwtUtil.getClaimFromToken(token, claims -> claims.get("userId", String.class)));
        } else {
            throw new RuntimeException("未提供有效的认证信息");
        }
    }

    /**
     * 获取当前登录病人的个人信息
     */
    @GetMapping("/profile")
    public ApiResponse getProfile(HttpServletRequest request) {
        Integer patientId = validatePatientRole(request);
        Patient patient = userService.findPatientById(patientId.toString());
        
        if (patient == null) {
            return ApiResponse.error("400","未找到病人信息");
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("patientId", patient.getPatientID());
        data.put("name", patient.getName());
        data.put("gender", patient.getGender());
        data.put("address", patient.getAddress());
        data.put("phone", patient.getPhone());
        data.put("username", patient.getUsername());
        
        return ApiResponse.success(data, "获取个人信息成功");
    }

    /**
     * 修改当前登录病人的个人信息
     */
    @PutMapping("/profile")
    public ApiResponse updateProfile(@RequestBody Map<String, String> params, HttpServletRequest request) {
        Integer patientId = validatePatientRole(request);
        Patient patient = userService.findPatientById(patientId.toString());
        
        if (patient == null) {
            return ApiResponse.error("400","未找到病人信息");
        }
        
        String name = params.get("name");
        String address = params.get("address");
        String phone = params.get("phone");
        
        if (name != null) {
            patient.setName(name);
        }
        if (address != null) {
            patient.setAddress(address);
        }
        if (phone != null) {
            patient.setPhone(phone);
        }
        
        userService.updatePatient(patient);
        
        return ApiResponse.success(null, "个人信息更新成功");
    }

    /**
     * 病人修改自己登录密码
     */
    @PostMapping("/profile/change-password")
    public ApiResponse changePassword(@RequestParam String currentPassword, 
                                     @RequestParam String newPassword,
                                     HttpServletRequest request) {
        Integer patientId = validatePatientRole(request);
        Patient patient = userService.findPatientById(patientId.toString());
        
        if (patient == null) {
            return ApiResponse.error("400","未找到病人信息");
        }
        
        if (!patient.getPassword().equals(currentPassword)) {
            return ApiResponse.error("400","当前密码不正确");
        }
        
        patient.setPassword(newPassword);
        userService.updatePatient(patient);
        
        return ApiResponse.success(null, "密码修改成功");
    }

    /**
     * 查询可挂号的时间段和医生
     */
    @GetMapping("/available-slots")
    public ApiResponse getAvailableSlots(HttpServletRequest request) {
        Integer patientId = validatePatientRole(request);
        List<Map<String, Object>> availableSlots = outpatientService.getAvailableSlots();
        
        Map<String, Object> data = new HashMap<>();
        data.put("total", availableSlots.size());
        data.put("list", availableSlots);
        
        return ApiResponse.success(data, "获取可挂号时间段成功");
    }

    /**
     * 病人进行门诊挂号
     */
    @PostMapping("/registrations")
    public ApiResponse registerOutpatient(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Integer patientId = validatePatientRole(request);
        Integer doctorId = (Integer) params.get("doctorId");
        String registrationTime = (String) params.get("registrationTime");
        
        if (doctorId == null || registrationTime == null) {
            return ApiResponse.error("400","医生ID和挂号时间不能为空");
        }
        
        OutpatientRegistration registrationResult = outpatientService.createRegistration(patientId, doctorId, registrationTime);
        
        if (registrationResult == null) {
            return ApiResponse.error("400","挂号失败，请检查医生ID和挂号时间是否有效");
        }
        
        return ApiResponse.success(registrationResult, "挂号成功，请及时支付");
    }

    /**
     * 查询当前病人的挂号记录
     */
    @GetMapping("/registrations")
    public ApiResponse getRegistrations(HttpServletRequest request) {
        Integer patientId = validatePatientRole(request);
        List<OutpatientRegistration> registrations = outpatientService.getPatientRegistrations(patientId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("total", registrations.size());
        data.put("list", registrations);
        
        return ApiResponse.success(data, "获取挂号记录成功");
    }

    /**
     * 病人取消已预约的挂号
     */
    @PostMapping("/registrations/{registrationId}/cancel")
    public ApiResponse cancelRegistration(@PathVariable Integer registrationId, HttpServletRequest request) {
        Integer patientId = validatePatientRole(request);
        boolean success = outpatientService.cancelRegistration(registrationId, patientId);
        
        if (!success) {
            return ApiResponse.error("400","取消挂号失败，请确认挂号ID是否正确或是否有权限取消");
        }
        
        return ApiResponse.success(null, "取消挂号成功");
    }

    /**
     * 查询病人自己的门诊处方记录
     */
    @GetMapping("/prescriptions")
    public ApiResponse getPrescriptions(HttpServletRequest request) {
        Integer patientId = validatePatientRole(request);
        List<Map<String, Object>> prescriptions = prescriptionService.getPatientPrescriptions(patientId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("total", prescriptions.size());
        data.put("list", prescriptions);
        
        return ApiResponse.success(data, "以上为所有记录");
    }

    /**
     * 获取单个门诊处方详情
     */
    @GetMapping("/prescriptions/{prescriptionId}")
    public ApiResponse getPrescriptionDetail(@PathVariable Integer prescriptionId, HttpServletRequest request) {
        Integer patientId = validatePatientRole(request);
        Map<String, Object> prescription = prescriptionService.getPrescriptionDetail(prescriptionId, patientId);
        
        if (prescription == null) {
            return ApiResponse.error("400","未找到处方记录或无权限查看");
        }
        
        return ApiResponse.success(prescription, "以上为处方详情");
    }

    /**
     * 查询病人自己的住院档案记录
     */
    @GetMapping("/hospitalization-records")
    public ApiResponse getHospitalizationRecords(HttpServletRequest request) {
        Integer patientId = validatePatientRole(request);
        List<Map<String, Object>> records = hospitalizationService.getPatientHospitalizationRecords(patientId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("total", records.size());
        data.put("list", records);
        
        return ApiResponse.success(data, "获取住院记录成功");
    }

    /**
     * 获取单个住院档案的详细信息
     */
    @GetMapping("/hospitalization-records/{recordId}")
    public ApiResponse getHospitalizationRecordDetail(@PathVariable Integer recordId, HttpServletRequest request) {
        Integer patientId = validatePatientRole(request);
        Map<String, Object> record = hospitalizationService.getHospitalizationRecordDetail(recordId, patientId);
        
        if (record == null) {
            return ApiResponse.error("400","未找到住院记录或无权限查看");
        }
        
        return ApiResponse.success(record, "获取住院记录详情成功");
    }
    /**
     * 获取未缴费的记录
     */
    @GetMapping("/unpay")
    public ApiResponse getUnpayRecords(HttpServletRequest request) {
        Integer patientId = validatePatientRole(request);
        List<Payment> unpayRecords = paymentService.getPatientUnpayRecords(patientId);

        Map<String, Object> data = new HashMap<>();
        data.put("total", unpayRecords.size());
        data.put("list", unpayRecords);

        return ApiResponse.success(data, "获取未缴费记录成功");
    }

    /**
     * 病人进行缴费
     */
    @GetMapping("/payments/{paymentId}")
    public ApiResponse completePayment(@PathVariable Integer paymentId, HttpServletRequest request) {
        Integer patientId = validatePatientRole(request);
        boolean success = paymentService.completePayment(paymentId, patientId);
        if (!success) {
            return ApiResponse.error("400","缴费失败，请确认缴费ID是否正确或是否有权限缴费");
        }else {
            return ApiResponse.success(null, "缴费成功");
        }
    }

    /**
     * 查询病人自己的缴费历史
     */
    @GetMapping("/payment-history")
    public ApiResponse getPaymentHistory(HttpServletRequest request) {
        Integer patientId = validatePatientRole(request);
        List<Payment> payments = paymentService.getPatientPaymentHistory(patientId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("total", payments.size());
        data.put("list", payments);
        
        return ApiResponse.success(data, "以上为所有订单");
    }

    /**
     * 查询支付状态
     */
    @GetMapping("/payments/{paymentId}/status")
    public ApiResponse getPaymentStatus(@PathVariable Integer paymentId, HttpServletRequest request) {
        Integer patientId = validatePatientRole(request);
        String status = paymentService.getPaymentStatus(paymentId, patientId);
        
        if (status == null) {
            return ApiResponse.error("400","未找到支付订单或无权限查看");
        }
        
        return ApiResponse.success(null, "支付状态：" + status);
    }
}