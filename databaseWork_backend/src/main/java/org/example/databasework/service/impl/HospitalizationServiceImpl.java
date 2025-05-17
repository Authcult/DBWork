package org.example.databasework.service.impl;

import org.example.databasework.mapper.DoctorMapper;
import org.example.databasework.mapper.HospitalizationDailyRecordMapper;
import org.example.databasework.mapper.HospitalizationRecordMapper;
import org.example.databasework.mapper.PatientMapper;
import org.example.databasework.model.Bed;
import org.example.databasework.model.Doctor;
import org.example.databasework.model.HospitalizationDailyRecord;
import org.example.databasework.model.HospitalizationRecord;
import org.example.databasework.model.Patient;
import org.example.databasework.model.Ward;
import org.example.databasework.service.HospitalizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 住院服务实现类
 */
@Service
public class HospitalizationServiceImpl implements HospitalizationService {
    
    private final HospitalizationRecordMapper hospitalizationRecordMapper;
    private final HospitalizationDailyRecordMapper dailyRecordMapper;
    private final PatientMapper patientMapper;
    private final DoctorMapper doctorMapper;
    
    @Autowired
    public HospitalizationServiceImpl(HospitalizationRecordMapper hospitalizationRecordMapper,
                                     HospitalizationDailyRecordMapper dailyRecordMapper,
                                     PatientMapper patientMapper,
                                     DoctorMapper doctorMapper) {
        this.hospitalizationRecordMapper = hospitalizationRecordMapper;
        this.dailyRecordMapper = dailyRecordMapper;
        this.patientMapper = patientMapper;
        this.doctorMapper = doctorMapper;
    }
    
    @Override
    public List<HospitalizationRecord> getHospitalizationRecordsByDoctor(Integer doctorId) {
        return hospitalizationRecordMapper.findByDoctor(doctorId);
    }
    
    @Override
    public HospitalizationRecord getHospitalizationRecordById(Integer recordId, Integer doctorId) {
        HospitalizationRecord record = hospitalizationRecordMapper.findById(recordId);
        
        // 验证医生权限
        if (!record.getDoctor().getDoctorID().equals(doctorId)) {
            throw new RuntimeException("无权查看此住院记录");
        }
        
        return record;
    }
    
    @Override
    @Transactional
    public HospitalizationRecord createHospitalizationRecord(Integer patientId, Integer attendingDoctorId, Integer wardId, Integer bedId, LocalDate admissionDate) {
        // 获取患者、医生信息
        Patient patient = patientMapper.findById(patientId);
        Doctor doctor = doctorMapper.findDoctorById(attendingDoctorId);
        
        // 创建病房和床位对象
        Ward ward = new Ward();
        ward.setWardID(wardId);
        
        Bed bed = new Bed();
        bed.setBedID(bedId);
        bed.setStatus("occupied"); // 直接设置为已占用
        
        // 创建住院记录
        HospitalizationRecord record = new HospitalizationRecord();
        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setWard(ward);
        record.setBed(bed);
        record.setAdmissionDate(admissionDate);
        
        // 保存住院记录
        hospitalizationRecordMapper.create(record);
        
        // 注意：由于没有BedMapper，床位状态更新需要在数据库层面处理
        // 或者通过其他方式实现，这里省略该步骤
        
        return record;
    }
    
    @Override
    public List<HospitalizationDailyRecord> getDailyRecordsByHospitalizationRecord(Integer recordId) {
        return dailyRecordMapper.findByRecordId(recordId);
    }
    
    @Override
    @Transactional
    public HospitalizationDailyRecord createDailyRecord(Integer recordId, String treatmentPlan, Integer doctorId) {
        // 获取住院记录
        HospitalizationRecord record = hospitalizationRecordMapper.findById(recordId);
        
        // 验证医生权限
        if (!record.getDoctor().getDoctorID().equals(doctorId)) {
            throw new RuntimeException("无权为此患者添加诊疗记录");
        }
        
        // 创建每日诊疗记录
        HospitalizationDailyRecord dailyRecord = new HospitalizationDailyRecord();
        dailyRecord.setRecord(record);
        dailyRecord.setDate(LocalDate.now());
        dailyRecord.setTreatmentPlan(treatmentPlan);
        
        // 保存每日诊疗记录
        dailyRecordMapper.create(dailyRecord);
        
        return dailyRecord;
    }
    
    @Override
    @Transactional
    public HospitalizationRecord dischargePatient(Integer recordId, LocalDate dischargeDate, Integer doctorId) {
        // 获取住院记录
        HospitalizationRecord record = hospitalizationRecordMapper.findById(recordId);
        
        // 验证医生权限
        if (!record.getDoctor().getDoctorID().equals(doctorId)) {
            throw new RuntimeException("无权为此患者办理出院");
        }
        
        // 更新出院日期
        hospitalizationRecordMapper.updateDischargeDate(recordId, dischargeDate);
        
        // 注意：由于没有BedMapper，床位状态更新需要在数据库层面处理
        // 或者通过其他方式实现，这里省略该步骤
        
        // 重新获取更新后的住院记录
        return hospitalizationRecordMapper.findById(recordId);
    }
}