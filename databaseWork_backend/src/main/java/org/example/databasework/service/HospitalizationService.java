package org.example.databasework.service;

import org.example.databasework.model.HospitalizationDailyRecord;
import org.example.databasework.model.HospitalizationRecord;

import java.time.LocalDate;
import java.util.List;

/**
 * 住院服务接口
 */
public interface HospitalizationService {
    
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