package org.example.databasework.service.impl;

import org.example.databasework.mapper.DrugMapper;
import org.example.databasework.mapper.OutpatientRegistrationMapper;
import org.example.databasework.mapper.PrescriptionMapper;
import org.example.databasework.model.Prescription;
import org.example.databasework.model.PrescriptionItem;
import org.example.databasework.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
}
