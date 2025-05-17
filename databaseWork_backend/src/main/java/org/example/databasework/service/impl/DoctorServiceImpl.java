package org.example.databasework.service.impl;

import org.example.databasework.mapper.DoctorMapper;
import org.example.databasework.mapper.ScheduleMapper;
import org.example.databasework.model.Doctor;
import org.example.databasework.model.HospitalizationDailyRecord;
import org.example.databasework.model.HospitalizationRecord;
import org.example.databasework.model.OutpatientRegistration;
import org.example.databasework.model.Prescription;
import org.example.databasework.model.Schedule;
import org.example.databasework.service.DoctorService;
import org.example.databasework.service.HospitalizationService;
import org.example.databasework.service.OutpatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorMapper doctorMapper;
    private final ScheduleMapper scheduleMapper;
    private final HospitalizationService hospitalizationService;
    private final OutpatientService outpatientService;

    @Autowired
    public DoctorServiceImpl(DoctorMapper doctorMapper, ScheduleMapper scheduleMapper,
                            HospitalizationService hospitalizationService,
                            OutpatientService outpatientService) {
        this.doctorMapper = doctorMapper;
        this.scheduleMapper = scheduleMapper;
        this.hospitalizationService = hospitalizationService;
        this.outpatientService = outpatientService;
    }

    @Override
    @Transactional
    public Doctor addDoctor(Doctor doctor) {
        doctorMapper.createDoctor(doctor);
        return doctor;
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorMapper.findAllDoctors();
    }

    @Override
    public Page<Doctor> getDoctorsByPage(int page, int pageSize) {
        int total = doctorMapper.countDoctors();
        List<Doctor> doctors = doctorMapper.findDoctorsByPage(page * pageSize, pageSize);
        return new PageImpl<>(doctors, PageRequest.of(page, pageSize), total);
    }

    @Override
    public Doctor getDoctorById(Integer doctorId) {
        Doctor doctor = doctorMapper.findDoctorById(doctorId);
        if (doctor == null) {
            throw new RuntimeException("医生不存在，ID: " + doctorId);
        }
        return doctor;
    }

    @Override
    @Transactional
    public Doctor updateDoctor(Integer doctorId, Doctor doctorDetails) {
        Doctor doctor = getDoctorById(doctorId);
        
        // 更新医生信息
        doctor.setName(doctorDetails.getName());
        doctor.setGender(doctorDetails.getGender());
        doctor.setTitle(doctorDetails.getTitle());
        doctor.setPhone(doctorDetails.getPhone());
        doctor.setDepartment(doctorDetails.getDepartment());
        
        doctorMapper.updateDoctor(doctor);
        return doctor;
    }

    @Override
    @Transactional
    public Doctor updateDoctorPassword(Integer doctorId, String password) {
        Doctor doctor = getDoctorById(doctorId);
        doctor.setPassword(password);
        doctorMapper.updateDoctorPassword(doctor);
        return doctor;
    }

    @Override
    @Transactional
    public void deleteDoctor(Integer doctorId) {
        // 先删除医生关联的排班记录
        scheduleMapper.deleteSchedulesByDoctorId(doctorId);
        // 再删除医生记录
        int result = doctorMapper.deleteDoctor(doctorId);
        if (result == 0) {
            throw new RuntimeException("医生不存在，ID: " + doctorId);
        }
    }

    @Override
    @Transactional
    public Schedule addSchedule(Integer doctorId, Schedule schedule) {
        Doctor doctor = getDoctorById(doctorId);
        schedule.setDoctor(doctor);
        scheduleMapper.createSchedule(schedule);
        return schedule;
    }

    @Override
    public List<Schedule> getDoctorSchedules(Integer doctorId) {
        // 验证医生是否存在
        getDoctorById(doctorId);
        return scheduleMapper.findSchedulesByDoctorId(doctorId);
    }
    
    @Override
    public Schedule getScheduleById(Integer scheduleId) {
        return scheduleMapper.findScheduleById(scheduleId);
    }
    
    @Override
    public Schedule updateSchedule(Integer scheduleId, Schedule schedule) {
        // 验证排班是否存在
        Schedule existingSchedule = getScheduleById(scheduleId);
        if (existingSchedule == null) {
            throw new RuntimeException("排班信息不存在");
        }
        
        // 保留医生信息
        schedule.setScheduleID(scheduleId);
        schedule.setDoctor(existingSchedule.getDoctor());
        
        // 更新排班信息
        scheduleMapper.updateSchedule(schedule);
        
        return getScheduleById(scheduleId);
    }
    
    @Override
    public void deleteSchedule(Integer scheduleId) {
        // 验证排班是否存在
        Schedule existingSchedule = getScheduleById(scheduleId);
        if (existingSchedule == null) {
            throw new RuntimeException("排班信息不存在");
        }
        
        scheduleMapper.deleteSchedule(scheduleId);
    }
    
    // 住院相关方法实现
    
    @Override
    public List<HospitalizationRecord> getHospitalizationRecordsByDoctor(Integer doctorId) {
        return hospitalizationService.getHospitalizationRecordsByDoctor(doctorId);
    }
    
    @Override
    public HospitalizationRecord getHospitalizationRecordById(Integer recordId, Integer doctorId) {
        return hospitalizationService.getHospitalizationRecordById(recordId, doctorId);
    }
    
    @Override
    public HospitalizationRecord createHospitalizationRecord(Integer patientId, Integer attendingDoctorId, Integer wardId, Integer bedId, LocalDate admissionDate) {
        return hospitalizationService.createHospitalizationRecord(patientId, attendingDoctorId, wardId, bedId, admissionDate);
    }
    
    @Override
    public List<HospitalizationDailyRecord> getDailyRecordsByHospitalizationRecord(Integer recordId) {
        return hospitalizationService.getDailyRecordsByHospitalizationRecord(recordId);
    }
    
    @Override
    public HospitalizationDailyRecord createDailyRecord(Integer recordId, String treatmentPlan, Integer doctorId) {
        return hospitalizationService.createDailyRecord(recordId, treatmentPlan, doctorId);
    }
    
    @Override
    public HospitalizationRecord dischargePatient(Integer recordId, LocalDate dischargeDate, Integer doctorId) {
        return hospitalizationService.dischargePatient(recordId, dischargeDate, doctorId);
    }
    
    // 门诊相关方法实现
    
    @Override
    public List<OutpatientRegistration> getRegistrationsByDoctorAndDate(Integer doctorId, LocalDate date) {
        return outpatientService.getRegistrationsByDoctorAndDate(doctorId, date);
    }
    
    @Override
    public OutpatientRegistration updateRegistrationStatus(Integer registrationId, String status, Integer doctorId) {
        if ("consulting".equals(status)) {
            return outpatientService.startConsultation(registrationId, doctorId);
        } else {

            return outpatientService.completeConsultation(registrationId, status, doctorId);
        }
    }
    
    @Override
    public Prescription createPrescription(Integer registrationId, String symptomDescription, Double diagnosisFee, List<Map<String, Object>> items, Integer doctorId) {
        return null;
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleMapper.findAllSchedules();
    }
}