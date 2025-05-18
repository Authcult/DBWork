package org.example.databasework.service;

import org.example.databasework.model.Admin;
import org.example.databasework.model.Doctor;
import org.example.databasework.model.Patient;


public interface UserService {
    Patient findByUsername(String username);
    
    Patient findByPhone(String phone);
    
    Doctor findDoctorById(String doctorId);
    
    Patient findPatientById(String patientId);
    
    Admin findAdminByUsername(String username);
    
    Object findUserByCredentials(String identifier, String password, String role);

    Patient createUser(String name, String gender, String address, String phone, String username, String password);
    
    void updatePatient(Patient patient);
}
