package org.example.databasework.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.databasework.model.Drug;

@Mapper
public interface DrugMapper {
    
    @Select("SELECT * FROM Drug WHERE drugID = #{drugId}")
    Drug findById(Integer drugId);

    /**
     * 获取药品的价格
     */
    @Select("SELECT price FROM Drug WHERE drugID = #{drugId}")
    Double getPrice(Integer drugId);

    /**
     * 获取药品的库存
     */
    @Select("SELECT stock FROM Drug WHERE drugID = #{drugId}")
    Integer getStock(Integer drugId);

    /**
     * 减少库存
     */
    @Select("UPDATE Drug SET stock = stock - #{quantity} WHERE drugID = #{drugId}")
    Integer reduceStock(Integer drugId, Integer quantity);

}