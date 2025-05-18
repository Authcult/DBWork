package org.example.databasework.service.impl;

import org.example.databasework.mapper.*;
import org.example.databasework.model.*;
import org.example.databasework.service.OutpatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * 门诊服务实现类
 */
@Service
public class OutpatientServiceImpl implements OutpatientService {
    
    private final OutpatientRegistrationMapper registrationMapper;
    private final PrescriptionMapper prescriptionMapper;
    private final DrugMapper drugMapper;
    private final ScheduleMapper scheduleMapper;
    private final OutpatientRegistrationMapper outpatientRegistrationMapper;
    private final PatientMapper patientMapper;
    private final DoctorMapper doctorMapper;

    @Autowired
    public OutpatientServiceImpl(OutpatientRegistrationMapper registrationMapper,
                                 PrescriptionMapper prescriptionMapper,
                                 DrugMapper drugMapper, ScheduleMapper scheduleMapper,
                                 OutpatientRegistrationMapper outpatientRegistrationMapper,
                                 PatientMapper patientMapper,
                                 DoctorMapper doctorMapper) {
        this.registrationMapper = registrationMapper;
        this.prescriptionMapper = prescriptionMapper;
        this.drugMapper = drugMapper;
        this.scheduleMapper = scheduleMapper;
        this.outpatientRegistrationMapper = outpatientRegistrationMapper;
        this.patientMapper = patientMapper;
        this.doctorMapper = doctorMapper;
    }

    @Override
    public List<Map<String, Object>> getAvailableSlots() {
        return scheduleMapper.findAvailableSchedules();
    }

    @Override
    public OutpatientRegistration createRegistration(Integer patientId, Integer doctorId, String registrationTime) {
        //查询改医生是否在该registrationTime有schedule
        if (scheduleMapper.findSchedulesByDoctorId(doctorId) == null) {
            throw new RuntimeException("该医生没有该时间段的排班");
        }else{
            List<Schedule> schedules = scheduleMapper.findSchedulesByDoctorId(doctorId);
            for (Schedule schedule : schedules) {
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(registrationTime);
                LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
                if (schedule.getStartTime().isBefore(localDateTime) && schedule.getEndTime().isAfter(localDateTime)&&schedule.getWorkType().equals("门诊")) {
                    // 创建挂号记录
                    OutpatientRegistration registration = new OutpatientRegistration();
                    registration.setPatient(patientMapper.findById(patientId));
                    registration.setDoctor(doctorMapper.findDoctorById(doctorId));
                    registration.setRegistrationTime(localDateTime);
                    registration.setStatus("wait");
                    outpatientRegistrationMapper.createRegistration(registration);
                    return registration;
                }
            }
            throw new RuntimeException("该医生没有该时间段的排班");
        }
    }

    @Override
    public List<OutpatientRegistration> getPatientRegistrations(Integer patientId) {
        return outpatientRegistrationMapper.findByPatientId(patientId);
    }

    @Override
    public boolean cancelRegistration(Integer registrationId, Integer patientId) {
        if (registrationMapper.findById(registrationId) == null) {
            return false;
        }
        if (!registrationMapper.findById(registrationId).getPatient().getPatientID().equals(patientId)) {
            return false;
        }
        return registrationMapper.cancelRegistration(registrationId) > 0;
    }

    @Override
    public List<OutpatientRegistration> getRegistrationsByPatientId(Integer patientId) {
        return registrationMapper.findByPatientId(patientId);
    }

    @Override
    public List<OutpatientRegistration> getRegistrationsByDoctorAndDate(Integer doctorId, LocalDate date) {
        return registrationMapper.findByDoctorAndDate(doctorId, date);
    }
    
    @Override
    public OutpatientRegistration startConsultation(Integer registrationId, Integer doctorId) {
        // 获取挂号信息
        OutpatientRegistration registration = registrationMapper.findById(registrationId);
        
        // 验证医生权限
        if (!registration.getDoctor().getDoctorID().equals(doctorId)) {
            throw new RuntimeException("无权操作此挂号记录");
        }
        
        // 更新状态为正在接诊
        registrationMapper.updateStatus(registrationId, "consulting");
        
        // 重新获取更新后的挂号信息
        return registrationMapper.findById(registrationId);
    }
    
    @Override
    @Transactional
    public OutpatientRegistration completeConsultation(Integer registrationId, String status, Integer doctorId) {
        // 获取挂号信息
        OutpatientRegistration registration = registrationMapper.findById(registrationId);
        
        // 验证医生权限
        if (!registration.getDoctor().getDoctorID().equals(doctorId)) {
            throw new RuntimeException("无权操作此挂号记录");
        }

        // 更新挂号状态为已完成
        registrationMapper.updateStatus(registrationId, status);
        
        // 返回创建的处方（包含处方项）
        return registrationMapper.findById(registrationId);
    }


}