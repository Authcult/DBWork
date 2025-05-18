package org.example.databasework.service;

import org.example.databasework.model.Doctor;
import org.example.databasework.model.HospitalizationDailyRecord;
import org.example.databasework.model.HospitalizationRecord;
import org.example.databasework.model.OutpatientRegistration;
import org.example.databasework.model.Schedule;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface DoctorService {
    /**
     * 添加新医生
     * @param doctor 医生信息
     * @return 添加后的医生信息
     */
    Doctor addDoctor(Doctor doctor);
    
    /**
     * 获取所有医生列表
     * @return 医生列表
     */
    List<Doctor> getAllDoctors();
    
    /**
     * 分页获取医生列表
     * @param page 页码（从0开始）
     * @param pageSize 每页记录数
     * @return 分页医生列表
     */
    Page<Doctor> getDoctorsByPage(int page, int pageSize);
    
    /**
     * 根据ID获取医生详情
     * @param doctorId 医生ID
     * @return 医生信息
     */
    Doctor getDoctorById(Integer doctorId);
    
    /**
     * 更新医生信息
     * @param doctorId 医生ID
     * @param doctor 更新的医生信息
     * @return 更新后的医生信息
     */
    Doctor updateDoctor(Integer doctorId, Doctor doctor);
    
    /**
     * 更新医生密码
     * @param doctorId 医生ID
     * @param password 新密码
     * @return 更新后的医生信息
     */
    Doctor updateDoctorPassword(Integer doctorId, String password);
    
    /**
     * 删除医生
     * @param doctorId 医生ID
     */
    void deleteDoctor(Integer doctorId);
    
    /**
     * 为医生添加排班
     * @param doctorId 医生ID
     * @param schedule 排班信息
     * @return 添加后的排班信息
     */
    Schedule addSchedule(Integer doctorId, Schedule schedule);
    
    /**
     * 获取医生的排班列表
     * @param doctorId 医生ID
     * @return 排班列表
     */
    List<Schedule> getDoctorSchedules(Integer doctorId);
    
    /**
     * 根据ID获取排班详情
     * @param scheduleId 排班ID
     * @return 排班信息
     */
    Schedule getScheduleById(Integer scheduleId);
    
    /**
     * 更新排班信息
     * @param scheduleId 排班ID
     * @param schedule 更新的排班信息
     * @return 更新后的排班信息
     */
    Schedule updateSchedule(Integer scheduleId, Schedule schedule);
    
    /**
     * 删除排班
     * @param scheduleId 排班ID
     */
    void deleteSchedule(Integer scheduleId);
    
    // 住院相关方法
    
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
     * @param doctorId 医生ID
     * @return 创建的每日诊疗记录
     */
    HospitalizationDailyRecord createDailyRecord(Integer recordId, String treatmentPlan, Integer doctorId);
    
    /**
     * 办理病人出院
     * @param recordId 住院记录ID
     * @param dischargeDate 出院日期
     * @param doctorId 医生ID
     * @return 更新后的住院记录
     */
    HospitalizationRecord dischargePatient(Integer recordId, LocalDate dischargeDate, Integer doctorId);
    
    // 门诊相关方法
    
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
     * @param status 状态
     * @param doctorId 医生ID
     * @return 更新后的挂号信息
     */
    OutpatientRegistration updateRegistrationStatus(Integer registrationId, String status, Integer doctorId);

    /**
     * 获取全体排班信息
     *
     * @return 排班信息
     */
    List<Schedule> getAllSchedules();
}