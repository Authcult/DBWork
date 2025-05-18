package org.example.databasework.service;

import org.example.databasework.model.OutpatientRegistration;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 门诊服务接口
 */
public interface OutpatientService {
    /**
     * 获取可挂号的时间段和医生
     * @return 可挂号的时间段和医生列表
     */
    List<Map<String, Object>> getAvailableSlots();
    
    /**
     * 病人进行门诊挂号
     *
     * @param patientId        病人ID
     * @param doctorId         医生ID
     * @param registrationTime 挂号时间
     * @return 挂号结果
     */
    OutpatientRegistration createRegistration(Integer patientId, Integer doctorId, String registrationTime);
    
    /**
     * 查询当前病人的挂号记录
     *
     * @param patientId 病人ID
     * @return 挂号记录列表
     */
    List<OutpatientRegistration> getPatientRegistrations(Integer patientId);
    
    /**
     * 病人取消已预约的挂号
     * @param registrationId 挂号ID
     * @param patientId 病人ID
     * @return 是否取消成功
     */
    boolean cancelRegistration(Integer registrationId, Integer patientId);
    
    /**
     * 根据病人ID获取挂号列表
     * @param patientId 病人ID
     * @return 挂号列表
     */
    List<OutpatientRegistration> getRegistrationsByPatientId(Integer patientId);

    
    /**
     * 根据医生ID和日期获取挂号列表
     * @param doctorId 医生ID
     * @param date 日期
     * @return 挂号列表
     */
    List<OutpatientRegistration> getRegistrationsByDoctorAndDate(Integer doctorId, LocalDate date);
    
    /**
     * 开始接诊
     * @param registrationId 挂号ID
     * @param doctorId 医生ID
     * @return 更新后的挂号信息
     */
    OutpatientRegistration startConsultation(Integer registrationId, Integer doctorId);
    
    /**
     * 完成接诊
     * @param registrationId 挂号ID
     * @param doctorId 医生ID
     * @return 创建的处方
     */
    OutpatientRegistration completeConsultation(Integer registrationId, String status, Integer doctorId);
}