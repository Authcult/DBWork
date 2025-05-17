package org.example.databasework.service.impl;

import org.example.databasework.mapper.DrugMapper;
import org.example.databasework.mapper.OutpatientRegistrationMapper;
import org.example.databasework.mapper.PrescriptionMapper;
import org.example.databasework.model.Drug;
import org.example.databasework.model.OutpatientRegistration;
import org.example.databasework.model.Prescription;
import org.example.databasework.model.PrescriptionItem;
import org.example.databasework.service.OutpatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    
    @Autowired
    public OutpatientServiceImpl(OutpatientRegistrationMapper registrationMapper, 
                                PrescriptionMapper prescriptionMapper,
                                DrugMapper drugMapper) {
        this.registrationMapper = registrationMapper;
        this.prescriptionMapper = prescriptionMapper;
        this.drugMapper = drugMapper;
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