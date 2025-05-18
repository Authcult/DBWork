package org.example.databasework.mapper;

import org.apache.ibatis.annotations.*;
import org.example.databasework.model.OutpatientRegistration;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface OutpatientRegistrationMapper {
    
    /**
     * 根据医生ID和日期查询挂号列表
     */
    @Select("SELECT r.* FROM OutpatientRegistration r " +
           "JOIN Doctor s ON r.doctorID = s.doctorID " +
           "WHERE s.doctorID = #{doctorId} AND CONVERT(DATE, r.registrationTime) = #{date}")
    @Results({
        @Result(property = "registrationID", column = "registrationID"),
        @Result(property = "patient", column = "patientID", javaType = org.example.databasework.model.Patient.class,
            one = @One(select = "org.example.databasework.mapper.PatientMapper.findById")),
        @Result(property = "schedule", column = "scheduleID", javaType = org.example.databasework.model.Schedule.class,
            one = @One(select = "org.example.databasework.mapper.ScheduleMapper.findById"))
    })
    List<OutpatientRegistration> findByDoctorAndDate(Integer doctorId, LocalDate date);
    
    /**
     * 根据ID查询挂号信息
     */
    @Select("SELECT * FROM OutpatientRegistration WHERE registrationID = #{registrationId}")
    @Results({
        @Result(property = "registrationID", column = "registrationID"),
        @Result(property = "patient", column = "patientID", javaType = org.example.databasework.model.Patient.class,
            one = @One(select = "org.example.databasework.mapper.PatientMapper.findById")),
        @Result(property = "doctor", column = "doctorID", javaType = org.example.databasework.model.Doctor.class,
            one = @One(select = "org.example.databasework.mapper.DoctorMapper.findDoctorById")),
        @Result(property ="registrationTime",  column = "registrationTime",  javaType = java.time.LocalDateTime.class),
        @Result(property ="status", column = "status")
    })
    OutpatientRegistration findById(Integer registrationId);
    
    /**
     * 更新挂号状态
     */
    @Update("UPDATE OutpatientRegistration SET status = #{status} WHERE registrationID = #{registrationId}")
    int updateStatus(Integer registrationId, String status);

    /**
     *根据患者ID查询挂号记录
     */
    @Select("SELECT registrationID,patientID,doctorID ,status " +
            "FROM OutpatientRegistration WHERE OutpatientRegistration.patientID = #{patientId} ")
    @Results({
        @Result(property = "registrationID", column = "registrationID"),
        @Result(property = "patient", column = "patientID", javaType = org.example.databasework.model.Patient.class,
            one = @One(select = "org.example.databasework.mapper.PatientMapper.findById")),
        @Result(property = "doctor", column = "doctorID", javaType = org.example.databasework.model.Doctor.class,
            one = @One(select = "org.example.databasework.mapper.DoctorMapper.findDoctorById")),
        @Result(property = "status", column = "status"
        )
    })
    List<OutpatientRegistration> findByPatientId(Integer patientId);

    /**
     * 创建挂号记录
     */
    @Insert("INSERT INTO OutpatientRegistration (patientID, doctorID, registrationTime, status) " +
            "VALUES (#{patient.patientID}, #{doctor.doctorID}, #{registrationTime}, 'wait')")
    @Options(useGeneratedKeys = true, keyProperty = "registrationID")
    int createRegistration(OutpatientRegistration registration);

    /**
     * 取消挂号
     */
    @Delete("DELETE FROM OutpatientRegistration WHERE registrationID = #{registrationId}")
    int cancelRegistration(Integer registrationId);
}