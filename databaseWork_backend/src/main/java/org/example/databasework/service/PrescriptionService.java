package org.example.databasework.service;

import org.example.databasework.model.Prescription;

import java.util.List;
import java.util.Map;

public interface PrescriptionService {
    Prescription createPrescription(Integer registrationId, String symptomDescription, Double diagnosisFee, List<Map<String, Object>> items, Integer doctorId);

}
