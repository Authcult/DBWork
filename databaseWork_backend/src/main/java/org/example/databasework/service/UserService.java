package org.example.databasework.service;

import org.example.databasework.model.Admin;
import org.example.databasework.model.Doctor;
import org.example.databasework.model.Patient;


public interface UserService {
    Patient findByUsername(String username);
    
    Doctor findDoctorById(String doctorId);
    
    Admin findAdminByUsername(String username);
    
    Object findUserByCredentials(String identifier, String password, String role);

    Patient createUser(String name, String gender, String address, String phone, String username, String password);
}
