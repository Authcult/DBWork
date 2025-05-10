package org.example.databasework.service;

import org.example.databasework.model.Doctor;
import org.example.databasework.model.Schedule;
import org.springframework.data.domain.Page;

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
}