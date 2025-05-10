package org.example.databasework.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.example.databasework.model.Admin;
import org.example.databasework.model.Doctor;
import org.example.databasework.model.Patient;

@Mapper
public interface UserMapper {

    @Select("select * from Patient where username = #{username}")
    Patient findByUsername(String username);
    
    @Select("select * from Doctor where doctorID = #{doctorId}")
    Doctor findDoctorById(String doctorId);
    
    @Select("select * from Admin where username = #{username}")
    Admin findAdminByUsername(String username);
    
    @Select("select * from Patient where username = #{identifier} and password = #{password}")
    Patient findPatientByCredentials(String identifier, String password);
    
    @Select("select * from Doctor where doctorID = #{identifier} and password = #{password}")
    Doctor findDoctorByCredentials(String identifier, String password);
    
    @Select("select * from Admin where username = #{identifier} and password = #{password}")
    Admin findAdminByCredentials(String identifier, String password);

    @Insert("insert into Patient (name, gender, address, phone, username, password) values (#{name}, #{gender}, #{address}, #{phone}, #{username}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "patientID", keyColumn = "patientID")
    int createUser(Patient patient);
}
