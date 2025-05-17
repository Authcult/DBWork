package org.example.databasework.mapper;

import org.apache.ibatis.annotations.*;
import org.example.databasework.model.Patient;

@Mapper
public interface PatientMapper {
    
    /**
     * 根据ID查询患者信息
     */
    @Select("SELECT * FROM Patient WHERE patientID = #{patientId}")
    Patient findById(Integer patientId);
    
    /**
     * 根据用户名查询患者信息
     */
    @Select("SELECT * FROM Patient WHERE username = #{username}")
    Patient findByUsername(String username);
    
    /**
     * 根据手机号查询患者信息
     */
    @Select("SELECT * FROM Patient WHERE phone = #{phone}")
    Patient findByPhone(String phone);
}