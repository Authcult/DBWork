package org.example.databasework.mapper;

import org.apache.ibatis.annotations.*;
import org.example.databasework.model.Admin;
import org.example.databasework.model.Doctor;
import org.example.databasework.model.Patient;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from Patient where username = #{username}")
    Patient findByUsername(String username);
    
    @Select("select * from Patient where phone = #{phone}")
    Patient findByPhone(String phone);
    
    @Select("select * from Doctor where doctorID = #{doctorId}")
    Doctor findDoctorById(String doctorId);
    
    @Select("select * from Patient where patientID = #{patientId}")
    Patient findPatientById(String patientId);
    
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
    
    @Update("update Patient set name = #{name}, gender = #{gender}, address = #{address}, phone = #{phone}, username = #{username}, password = #{password} where patientID = #{patientID}")
    int updatePatient(Patient patient);

    @Insert("INSERT INTO Admin (username, password, fullName, phone, email) VALUES (#{username}, #{password}, #{fullName}, #{phone}, #{email})")
    int createAdmin(Admin admin);

    @Select("SELECT * FROM Admin")
    List<Admin> getAllAdmins();

    @Select("SELECT * FROM Admin WHERE adminID = #{adminId}")
    Admin getAdminById(Integer adminId);

    @Update({
    "<script>",
    "UPDATE Admin SET",
    "fullName = #{fullName},",
    "phone = #{phone},",
    "email = #{email}",
    "<if test='password != null and password != \"\"'>, password = #{password}</if>",
    "WHERE adminID = #{adminID}",
    "</script>"
    })
    int updateAdmin(Admin admin);

    @Delete("DELETE FROM Admin WHERE adminID = #{adminId}")
    int deleteAdmin(Integer adminId);


}
