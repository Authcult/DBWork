package org.example.databasework.mapper;

import org.apache.ibatis.annotations.*;
import org.example.databasework.model.Prescription;
import org.example.databasework.model.PrescriptionItem;

import java.util.List;

@Mapper
public interface PrescriptionMapper {
    
    /**
     * 创建处方
     */
    @Insert("INSERT INTO Prescription (registrationID, symptomDescription, diagnosisFee) " +
           "VALUES (#{registration.registrationID}, #{symptomDescription}, #{diagnosisFee})")
    @Options(useGeneratedKeys = true, keyProperty = "prescriptionID", keyColumn = "prescriptionID")
    int create(Prescription prescription);
    
    /**
     * 根据ID查询处方
     */
    @Select("SELECT * FROM Prescription WHERE prescriptionID = #{prescriptionId}")
    @Results({
        @Result(property = "prescriptionID", column = "prescriptionID"),
        @Result(property = "registration", column = "registrationID", javaType = org.example.databasework.model.OutpatientRegistration.class,
            one = @One(select = "org.example.databasework.mapper.OutpatientRegistrationMapper.findById"))
    })
    Prescription findById(Integer prescriptionId);
    
    /**
     * 根据挂号ID查询处方
     */
    @Select("SELECT * FROM Prescription WHERE registrationID = #{registrationId}")
    @Results({
        @Result(property = "prescriptionID", column = "prescriptionID"),
        @Result(property = "registration", column = "registrationID", javaType = org.example.databasework.model.OutpatientRegistration.class,
            one = @One(select = "org.example.databasework.mapper.OutpatientRegistrationMapper.findById")),
        @Result(property = "items", column = "prescriptionID", javaType = List.class,
            many = @Many(select = "org.example.databasework.mapper.PrescriptionMapper.findItemsByPrescriptionId"))
    })
    Prescription findByRegistrationId(Integer registrationId);
    
    /**
     * 创建处方项
     * return 创建的处方项的ID
     */
    @Insert("INSERT INTO PrescriptionItem (prescriptionID, drugID, quantity, usageInstruction) " +
           "VALUES (#{prescription.prescriptionID}, #{drug.drugID}, #{quantity}, #{usageInstruction})")
    @Options(useGeneratedKeys = true, keyProperty = "itemID", keyColumn = "itemID")
    int createItem(PrescriptionItem prescriptionItem);
    
    /**
     * 根据处方ID查询处方项列表
     */
    @Select("SELECT * FROM PrescriptionItem WHERE prescriptionID = #{prescriptionId}")
    @Results({
        @Result(property = "itemID", column = "itemID"),
        @Result(property = "prescription", column = "prescriptionID", javaType = org.example.databasework.model.Prescription.class,
            one = @One(select = "org.example.databasework.mapper.PrescriptionMapper.findById")),
        @Result(property = "drug", column = "drugID", javaType = org.example.databasework.model.Drug.class,
            one = @One(select = "org.example.databasework.mapper.DrugMapper.findById"))
    })
    List<PrescriptionItem> findItemsByPrescriptionId(Integer prescriptionId);
}