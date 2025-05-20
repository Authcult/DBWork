package org.example.databasework.service.impl;

import org.example.databasework.mapper.*;
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
import java.util.Map;

/**
 * 住院服务实现类
 */
@Service
public class HospitalizationServiceImpl implements HospitalizationService {
    
    private final HospitalizationRecordMapper hospitalizationRecordMapper;
    private final HospitalizationDailyRecordMapper dailyRecordMapper;
    private final PatientMapper patientMapper;
    private final DoctorMapper doctorMapper;
    private final BedMapper bedMapper;
    private final WardMapper wardMapper;
    
    @Autowired
    public HospitalizationServiceImpl(HospitalizationRecordMapper hospitalizationRecordMapper,
                                     HospitalizationDailyRecordMapper dailyRecordMapper,
                                     PatientMapper patientMapper,
                                     DoctorMapper doctorMapper
                                    , BedMapper bedMapper,  WardMapper wardMapper) {
        this.hospitalizationRecordMapper = hospitalizationRecordMapper;
        this.dailyRecordMapper = dailyRecordMapper;
        this.patientMapper = patientMapper;
        this.doctorMapper = doctorMapper;
        this.bedMapper = bedMapper;
        this.wardMapper = wardMapper;
    }

    @Override
    public List<Map<String, Object>> getPatientHospitalizationRecords(Integer patientId) {
        return hospitalizationRecordMapper.findByPatient(patientId);
    }

    @Override
    public Map<String, Object> getHospitalizationRecordDetail(Integer recordId, Integer patientId) {
        HospitalizationRecord record = hospitalizationRecordMapper.findById(recordId);
        if (record == null || !record.getPatient().getPatientID().equals(patientId)) {
            return null;
        }
        List<HospitalizationDailyRecord> dailyRecords = dailyRecordMapper.findByRecordId(recordId);

        return Map.ofEntries(
                Map.entry("recordId", record.getRecordID()),
                Map.entry("patientId", record.getPatient().getPatientID()),
                Map.entry("patientName", record.getPatient().getName()),
                Map.entry("attendingDoctorID", record.getDoctor().getDoctorID()),
                Map.entry("attendingDoctorName", record.getDoctor().getName()),
                Map.entry("wardId", record.getWard().getWardID()),
                Map.entry("wardLocation", record.getWard().getLocation()),
                Map.entry("bedId", record.getBed().getBedID()),
                Map.entry("bedNumber", record.getBed().getBedNumber()),
                Map.entry("admissionDate", record.getAdmissionDate().toString()),
                Map.entry("dischargeDate", record.getDischargeDate() == null ? "null" : record.getDischargeDate().toString()),
                Map.entry("dailyRecords", dailyRecords)
        );
    }

    @Override
    public Map<String, Object> createPayment(Integer patientId, Double amount, String paymentType, String referenceId, String paymentMethod) {
        return Map.of();
    }

    @Override
    public List<Map<String, Object>> getPatientPayments(Integer patientId) {
        return List.of();
    }

    @Override
    public String getPaymentStatus(Integer paymentId, Integer patientId) {
        return "";
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
        Ward ward = wardMapper.findById(wardId);

        
        Bed bed = new Bed();
        bed.setBedID(bedId);
        bed.setWard(ward);
        bed.setBedNumber(bedMapper.findById(bedId).getBedNumber());
        
        // 创建住院记录
        HospitalizationRecord record = new HospitalizationRecord();
        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setWard(ward);
        record.setBed(bed);
        record.setAdmissionDate(admissionDate);
        
        // 保存住院记录
        hospitalizationRecordMapper.create(record);
        
        if (bedMapper.findById(bedId).getStatus()  == "unoccupied"||  bedMapper.findById(bedId).getStatus() == null) {
            bedMapper.updateStatus(bedId, "occupied");
        }else{
            throw new RuntimeException("此病床已占用");
        }


        
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
        
        if (record.getBed().getStatus() == "occupied") {
            bedMapper.updateStatus(record.getBed().getBedID(), "unoccupied");
        }

        // 重新获取更新后的住院记录
        return hospitalizationRecordMapper.findById(recordId);
    }
}