package org.example.databasework.service;

import org.example.databasework.model.Prescription;

import java.util.List;
import java.util.Map;

public interface PrescriptionService {
    /**
     * 创建处方
     * @param registrationId 挂号ID
     * @param symptomDescription 症状描述
     * @param diagnosisFee 诊断费用
     * @param items 处方项目列表
     * @param doctorId 医生ID
     * @return 创建的处方
     */
    Prescription createPrescription(Integer registrationId, String symptomDescription, Double diagnosisFee, List<Map<String, Object>> items, Integer doctorId);
    
    /**
     * 查询病人自己的门诊处方记录
     * @param patientId 病人ID
     * @return 处方记录列表
     */
    List<Map<String, Object>> getPatientPrescriptions(Integer patientId);
    
    /**
     * 获取单个门诊处方详情
     * @param prescriptionId 处方ID
     * @param patientId 病人ID
     * @return 处方详情
     */
    Map<String, Object> getPrescriptionDetail(Integer prescriptionId, Integer patientId);
}
