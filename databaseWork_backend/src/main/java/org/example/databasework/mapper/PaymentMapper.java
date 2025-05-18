package org.example.databasework.mapper;

import org.apache.ibatis.annotations.*;
import org.example.databasework.model.Payment;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PaymentMapper {

    @Select("SELECT * FROM Payment WHERE patientID = #{patientId} AND paidDate IS NULL")
    @Results({
            @Result(property = "paymentID", column = "paymentID"),
            @Result(property = "patient", column = "patientID", javaType = org.example.databasework.model.Patient.class,
                    one = @One(select = "org.example.databasework.mapper.PatientMapper.findById")),
    })
    List<Payment> findUnmappedPaymentsByPatientId(Integer patientId);

    @Select("SELECT * FROM Payment WHERE patientID = #{patientId}")
    List<Payment> findByPatientId(Integer patientId);

    /**
     * 创建一个支付记录
     */
    @Select("INSERT INTO Payment (patientID, amount, paymentType,createdDate) " +
            "VALUES (#{patient.patientID}, #{amount}, #{paymentType}, #{createdDate})")
    @Options(useGeneratedKeys = true, keyProperty = "paymentID", keyColumn = "paymentID")
    Integer create(Payment payment);
    /*
     ** 完成支付
     */
    @Select("UPDATE Payment SET paidDate = #{paidDate} WHERE paymentID = #{paymentId}")
    Integer completePayment(Integer paymentId, LocalDateTime paidDate);

    @Select("SELECT * FROM Payment WHERE paymentID = #{paymentId}")
    @Results({
            @Result(property = "paymentID", column = "paymentID"),
            @Result(property = "patient", column = "patientID", javaType = org.example.databasework.model.Patient.class,
                    one = @One(select = "org.example.databasework.mapper.PatientMapper.findById")),
    })
    Payment findById(Integer paymentId);
}
