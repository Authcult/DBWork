package org.example.databasework.mapper;

import org.apache.ibatis.annotations.*;
import org.example.databasework.model.HospitalizationRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface HospitalizationRecordMapper {
    
    /**
     * 根据医生ID查询住院记录列表
     */
    @Select("SELECT * FROM HospitalizationRecord WHERE doctorID = #{doctorId}")
    @Results({
        @Result(property = "recordID", column = "recordID"),
        @Result(property = "patient", column = "patientID", javaType = org.example.databasework.model.Patient.class,
            one = @One(select = "org.example.databasework.mapper.PatientMapper.findById")),
        @Result(property = "doctor", column = "doctorID", javaType = org.example.databasework.model.Doctor.class,
            one = @One(select = "org.example.databasework.mapper.DoctorMapper.findDoctorById")),
        @Result(property = "ward", column = "wardID", javaType = org.example.databasework.model.Ward.class,
            one = @One(select = "org.example.databasework.mapper.WardMapper.findById")),
        @Result(property = "bed", column = "bedID", javaType = org.example.databasework.model.Bed.class,
            one = @One(select = "org.example.databasework.mapper.BedMapper.findById"))
    })
    List<HospitalizationRecord> findByDoctor(Integer doctorId);
    
    /**
     * 根据ID查询住院记录
     */
    @Select("SELECT * FROM HospitalizationRecord WHERE recordID = #{recordId}")
    @Results({
        @Result(property = "recordID", column = "recordID"),
        @Result(property = "patient", column = "patientID", javaType = org.example.databasework.model.Patient.class,
            one = @One(select = "org.example.databasework.mapper.PatientMapper.findById")),
        @Result(property = "doctor", column = "doctorID", javaType = org.example.databasework.model.Doctor.class,
            one = @One(select = "org.example.databasework.mapper.DoctorMapper.findDoctorById")),
        @Result(property = "ward", column = "wardID", javaType = org.example.databasework.model.Ward.class,
            one = @One(select = "org.example.databasework.mapper.WardMapper.findById")),
        @Result(property = "bed", column = "bedID", javaType = org.example.databasework.model.Bed.class,
            one = @One(select = "org.example.databasework.mapper.BedMapper.findById"))
    })
    HospitalizationRecord findById(Integer recordId);
    
    /**
     * 创建住院记录
     */
    @Insert("INSERT INTO HospitalizationRecord (patientID, doctorID, wardID, bedID, admissionDate) " +
           "VALUES (#{patient.patientID}, #{doctor.doctorID}, #{ward.wardID}, #{bed.bedID}, #{admissionDate})")
    @Options(useGeneratedKeys = true, keyProperty = "recordID", keyColumn = "recordID")
    int create(HospitalizationRecord record);
    
    /**
     * 更新出院日期
     */
    @Update("UPDATE HospitalizationRecord SET dischargeDate = #{dischargeDate} WHERE recordID = #{recordId}")
    int updateDischargeDate(Integer recordId, LocalDate dischargeDate);


    /**
     * 根据患者ID查询住院记录列表
     */
    @Select("SELECT * FROM HospitalizationRecord WHERE patientID = #{patientId}")
    @Results({
        @Result(property = "recordID", column = "recordID"),
        @Result(property = "patient", column = "patientID", javaType = org.example.databasework.model.Patient.class,
            one = @One(select = "org.example.databasework.mapper.PatientMapper.findById")),
        @Result(property = "doctor", column = "doctorID", javaType = org.example.databasework.model.Doctor.class,
            one = @One(select = "org.example.databasework.mapper.DoctorMapper.findDoctorById")
        ),
        @Result(property = "ward", column = "wardID", javaType = org.example.databasework.model.Ward.class,
            one = @One(select = "org.example.databasework.mapper.WardMapper.findById")),
        @Result(property = "bed", column = "bedID", javaType = org.example.databasework.model.Bed.class,
            one = @One(select = "org.example.databasework.mapper.BedMapper.findById"))
    })
    List<Map<String, Object>> findByPatient(Integer patientId);
}