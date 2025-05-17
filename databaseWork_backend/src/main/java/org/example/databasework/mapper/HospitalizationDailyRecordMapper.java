package org.example.databasework.mapper;

import org.apache.ibatis.annotations.*;
import org.example.databasework.model.HospitalizationDailyRecord;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface HospitalizationDailyRecordMapper {
    
    /**
     * 根据住院记录ID查询每日诊疗记录列表
     */
    @Select("SELECT * FROM HospitalizationDailyRecord WHERE recordID = #{recordId} ORDER BY date DESC")
    @Results({
        @Result(property = "dailyRecordID", column = "dailyRecordID"),
        @Result(property = "record", column = "recordID", javaType = org.example.databasework.model.HospitalizationRecord.class,
            one = @One(select = "org.example.databasework.mapper.HospitalizationRecordMapper.findById"))
    })
    List<HospitalizationDailyRecord> findByRecordId(Integer recordId);
    
    /**
     * 根据ID查询每日诊疗记录
     */
    @Select("SELECT * FROM HospitalizationDailyRecord WHERE dailyRecordID = #{dailyRecordId}")
    @Results({
        @Result(property = "dailyRecordID", column = "dailyRecordID"),
        @Result(property = "record", column = "recordID", javaType = org.example.databasework.model.HospitalizationRecord.class,
            one = @One(select = "org.example.databasework.mapper.HospitalizationRecordMapper.findById"))
    })
    HospitalizationDailyRecord findById(Integer dailyRecordId);
    
    /**
     * 创建每日诊疗记录
     */
    @Insert("INSERT INTO HospitalizationDailyRecord (recordID, date, treatmentPlan) " +
           "VALUES (#{record.recordID}, #{date}, #{treatmentPlan})")
    @Options(useGeneratedKeys = true, keyProperty = "dailyRecordID", keyColumn = "dailyRecordID")
    int create(HospitalizationDailyRecord dailyRecord);
}