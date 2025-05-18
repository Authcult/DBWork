package org.example.databasework.service.impl;

import org.example.databasework.mapper.DrugMapper;
import org.example.databasework.mapper.OutpatientRegistrationMapper;
import org.example.databasework.mapper.PrescriptionMapper;
import org.example.databasework.model.OutpatientRegistration;
import org.example.databasework.model.Prescription;
import org.example.databasework.model.PrescriptionItem;
import org.example.databasework.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    @Autowired
    private DrugMapper drugMapper;

    @Autowired
    private PrescriptionMapper prescriptionMapper;

    @Autowired
    private OutpatientRegistrationMapper outpatientRegistrationMapper;
    @Override
    public Prescription createPrescription(Integer registrationId, String symptomDescription, Double diagnosisFee, List<Map<String, Object>> items, Integer doctorId) {
        Prescription prescription = new Prescription();
        prescription.setRegistration(outpatientRegistrationMapper.findById(registrationId));
        prescription.setSymptomDescription(symptomDescription);
        prescription.setDiagnosisFee(BigDecimal.valueOf(diagnosisFee));
        BigDecimal totalDrugFee = BigDecimal.ZERO;
        for (Map<String, Object> item : items) {
            Integer drugID = (Integer) item.get("drugId");
            Integer quantity = (Integer) item.get("quantity");
            Integer stock = drugMapper.getStock(drugID);
            if (stock < quantity) {
                throw new RuntimeException("库存不足");
            }else{Double price = drugMapper.getPrice(drugID);
                totalDrugFee = totalDrugFee.add(BigDecimal.valueOf(price * quantity));
                drugMapper.reduceStock(drugID, quantity);
            }
        }
        prescription.setTotalDrugFee(totalDrugFee);
        prescription.setTotalAmount(totalDrugFee.add(prescription.getDiagnosisFee()));
        prescriptionMapper.create(prescription);
        for (Map<String, Object> item : items) {
            Integer drugID = (Integer) item.get("drugId");
            Integer quantity = (Integer) item.get("quantity");
            String usageInstruction = (String) item.get("usageInstruction");
            PrescriptionItem prescriptionItem = new PrescriptionItem();
            prescriptionItem.setDrug(drugMapper.findById(drugID));
            prescriptionItem.setQuantity(quantity);
            prescriptionItem.setUsageInstruction(usageInstruction);
            prescriptionItem.setPrescription(prescription);
            prescriptionMapper.createItem(prescriptionItem);
        }
        return prescription;
    }
    
    @Override
    public List<Map<String, Object>> getPatientPrescriptions(Integer patientId) {
        // 查询该患者的所有挂号记录对应的处方
        List<Prescription> prescriptions = new ArrayList<>();
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 获取患者的所有挂号记录
        List<OutpatientRegistration> registrations = outpatientRegistrationMapper.findByPatientId(patientId);
        
        // 对每个挂号记录查询对应的处方
        for (OutpatientRegistration registration : registrations) {
            List<Prescription> prescription_ss = prescriptionMapper.findByRegistrationId(registration.getRegistrationID());
            for (Prescription prescription : prescription_ss) {
                prescriptions.add(prescription);
            }
        }
        
        // 转换为前端需要的格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Prescription prescription : prescriptions) {
            Map<String, Object> map = new HashMap<>();
            map.put("prescriptionId", prescription.getPrescriptionID());
            map.put("registrationId", prescription.getRegistration().getRegistrationID());
            map.put("symptomDescription", prescription.getSymptomDescription());
            map.put("doctorName", prescription.getRegistration().getDoctor().getName());
            map.put("departmentName", prescription.getRegistration().getDoctor().getDepartment().getName());
            map.put("registrationTime", prescription.getRegistration().getRegistrationTime().format(formatter));
            map.put("items", prescriptionMapper.findItemsByPrescriptionId(prescription.getPrescriptionID()));
            map.put("totalDrugFee", prescription.getTotalDrugFee());
            map.put("diagnosisFee", prescription.getDiagnosisFee());
            map.put("totalAmount", prescription.getTotalAmount());
            result.add(map);
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> getPrescriptionDetail(Integer prescriptionId, Integer patientId) {
        // 查询处方详情
        Prescription prescription = prescriptionMapper.findById(prescriptionId);
        
        // 验证该处方是否属于该患者
        if (prescription == null || !prescription.getRegistration().getPatient().getPatientID().equals(patientId)) {
            return null;
        }
        
        // 查询处方项
        List<PrescriptionItem> items = prescriptionMapper.findItemsByPrescriptionId(prescriptionId);
        
        // 转换为前端需要的格式
        Map<String, Object> result = new HashMap<>();
        result.put("prescriptionId", prescription.getPrescriptionID());
        result.put("registrationId", prescription.getRegistration().getRegistrationID());
        result.put("doctorName", prescription.getRegistration().getDoctor().getName());
        result.put("departmentName", prescription.getRegistration().getDoctor().getDepartment().getName());
        result.put("symptomDescription", prescription.getSymptomDescription());
        result.put("diagnosisFee", prescription.getDiagnosisFee());
        result.put("totalDrugFee", prescription.getTotalDrugFee());
        result.put("totalAmount", prescription.getTotalAmount());
        
        // 处理处方项
        List<Map<String, Object>> itemsList = new ArrayList<>();
        for (PrescriptionItem item : items) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("drugId", item.getDrug().getDrugID());
            itemMap.put("drugName", item.getDrug().getName());
            itemMap.put("price", item.getDrug().getPrice());
            itemMap.put("quantity", item.getQuantity());
            itemMap.put("usageInstruction", item.getUsageInstruction());
            itemsList.add(itemMap);
        }
        result.put("items", itemsList);
        
        return result;
    }
}
