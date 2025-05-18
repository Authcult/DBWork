package org.example.databasework.service;

import org.example.databasework.model.HospitalizationDailyRecord;
import org.example.databasework.model.HospitalizationRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 住院服务接口
 */
public interface HospitalizationService {
    
    /**
     * 查询病人自己的住院档案记录
     * @param patientId 病人ID
     * @return 住院档案记录列表
     */
    List<Map<String, Object>> getPatientHospitalizationRecords(Integer patientId);
    
    /**
     * 获取单个住院档案的详细信息
     * @param recordId 住院档案ID
     * @param patientId 病人ID
     * @return 住院档案详情
     */
    Map<String, Object> getHospitalizationRecordDetail(Integer recordId, Integer patientId);
    
    /**
     * 病人进行缴费
     * @param patientId 病人ID
     * @param amount 缴费金额
     * @param paymentType 缴费类型
     * @param referenceId 引用ID
     * @param paymentMethod 支付方式
     * @return 支付结果
     */
    Map<String, Object> createPayment(Integer patientId, Double amount, String paymentType, String referenceId, String paymentMethod);
    
    /**
     * 查询病人自己的缴费历史
     * @param patientId 病人ID
     * @return 缴费历史列表
     */
    List<Map<String, Object>> getPatientPayments(Integer patientId);
    
    /**
     * 查询支付状态
     * @param paymentId 支付ID
     * @param patientId 病人ID
     * @return 支付状态
     */
    String getPaymentStatus(Integer paymentId, Integer patientId);
    
    /**
     * 根据医生ID获取住院病人列表
     * @param doctorId 医生ID
     * @return 住院记录列表
     */
    List<HospitalizationRecord> getHospitalizationRecordsByDoctor(Integer doctorId);
    
    /**
     * 根据ID获取住院记录详情
     * @param recordId 住院记录ID
     * @param doctorId 医生ID（用于权限验证）
     * @return 住院记录
     */
    HospitalizationRecord getHospitalizationRecordById(Integer recordId, Integer doctorId);
    
    /**
     * 创建住院记录
     * @param patientId 患者ID
     * @param attendingDoctorId 主治医生ID
     * @param wardId 病房ID
     * @param bedId 床位ID
     * @param admissionDate 入院日期
     * @return 创建的住院记录
     */
    HospitalizationRecord createHospitalizationRecord(Integer patientId, Integer attendingDoctorId, Integer wardId, Integer bedId, LocalDate admissionDate);
    
    /**
     * 获取住院记录的每日诊疗记录列表
     * @param recordId 住院记录ID
     * @return 每日诊疗记录列表
     */
    List<HospitalizationDailyRecord> getDailyRecordsByHospitalizationRecord(Integer recordId);
    
    /**
     * 添加每日诊疗记录
     * @param recordId 住院记录ID
     * @param treatmentPlan 治疗计划
     * @param doctorId 医生ID（用于权限验证）
     * @return 创建的每日诊疗记录
     */
    HospitalizationDailyRecord createDailyRecord(Integer recordId, String treatmentPlan, Integer doctorId);
    
    /**
     * 办理病人出院
     * @param recordId 住院记录ID
     * @param dischargeDate 出院日期
     * @param doctorId 医生ID（用于权限验证）
     * @return 更新后的住院记录
     */
    HospitalizationRecord dischargePatient(Integer recordId, LocalDate dischargeDate, Integer doctorId);
}