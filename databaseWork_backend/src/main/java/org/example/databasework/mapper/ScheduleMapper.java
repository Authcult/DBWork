package org.example.databasework.mapper;

import org.apache.ibatis.annotations.*;
import org.example.databasework.model.Department;
import org.example.databasework.model.Schedule;

import java.util.List;

@Mapper
public interface ScheduleMapper {
    
    @Insert("INSERT INTO Schedule (doctorID, workType, startTime, endTime) VALUES (#{doctor.doctorID}, #{workType}, #{startTime}, #{endTime})")
    @Options(useGeneratedKeys = true, keyProperty = "scheduleID", keyColumn = "scheduleID")
    int createSchedule(Schedule schedule);
    
    @Select("SELECT * FROM Schedule WHERE doctorID = #{doctorId}")
    @Results({
        @Result(property = "scheduleID", column = "scheduleID"),
        @Result(property = "doctor", column = "doctorID", javaType = org.example.databasework.model.Doctor.class,
            one = @One(select = "org.example.databasework.mapper.DoctorMapper.findDoctorById")),
        @Result(property = "workType", column = "workType"),
        @Result(property = "startTime", column = "startTime"),
        @Result(property = "endTime", column = "endTime")
    })
    List<Schedule> findSchedulesByDoctorId(Integer doctorId);
    
    @Select("SELECT * FROM Schedule WHERE scheduleID = #{scheduleId}")
    @Results({
        @Result(property = "scheduleID", column = "scheduleID"),
        @Result(property = "doctor", column = "doctorID", javaType = org.example.databasework.model.Doctor.class,
            one = @One(select = "org.example.databasework.mapper.DoctorMapper.findDoctorById")),
        @Result(property = "workType", column = "workType"),
        @Result(property = "startTime", column = "startTime"),
        @Result(property = "endTime", column = "endTime")
    })
    Schedule findScheduleById(Integer scheduleId);
    
    @Update("UPDATE Schedule SET workType = #{workType}, startTime = #{startTime}, endTime = #{endTime} WHERE scheduleID = #{scheduleID}")
    int updateSchedule(Schedule schedule);
    
    @Delete("DELETE FROM Schedule WHERE scheduleID = #{scheduleId}")
    int deleteSchedule(Integer scheduleId);
    
    @Delete("DELETE FROM Schedule WHERE doctorID = #{doctorId}")
    int deleteSchedulesByDoctorId(Integer doctorId);

    @Select("SELECT * FROM Schedule")
    @Results({
            @Result(property = "doctor", column = "doctorID", javaType = org.example.databasework.model.Doctor.class,
                    one = @One(select = "org.example.databasework.mapper.DoctorMapper.findDoctorById")),
            @Result(property = "workType", column = "workType"),
            @Result(property = "startTime", column = "startTime"),
            @Result(property = "endTime", column = "endTime")
    })
    List<Schedule> findAllSchedules();
}