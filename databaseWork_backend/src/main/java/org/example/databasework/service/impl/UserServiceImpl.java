package org.example.databasework.service.impl;

import org.example.databasework.mapper.UserMapper;
import org.example.databasework.model.Admin;
import org.example.databasework.model.Doctor;
import org.example.databasework.model.Patient;
import org.example.databasework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public Patient findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public Doctor findDoctorById(String doctorId) {
        return userMapper.findDoctorById(doctorId);
    }

    @Override
    public Admin findAdminByUsername(String username) {
        return userMapper.findAdminByUsername(username);
    }

    @Override
    public Object findUserByCredentials(String identifier, String password, String role) {
        if (role.equalsIgnoreCase("patient")) {
            return userMapper.findPatientByCredentials(identifier, password);
        } else if (role.equalsIgnoreCase("doctor")) {
            return userMapper.findDoctorByCredentials(identifier, password);
        } else if (role.equalsIgnoreCase("admin")) {
            return userMapper.findAdminByCredentials(identifier, password);
        }
        return null;
    }

    @Override
    public Patient createUser(String name, String gender, String address, String phone, String username, String password) {
       Patient patient = new Patient();
       patient.setName(name);
       patient.setGender(gender);
       patient.setAddress(address);
       patient.setPhone(phone);
       patient.setUsername(username);
       patient.setPassword(password);

       userMapper.createUser(patient); // 插入后主键会自动填充到 patient 对象中
       return patient;
    }


}
