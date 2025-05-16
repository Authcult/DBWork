package org.example.databasework.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.databasework.model.Drug;

@Mapper
public interface DrugMapper {
    
    @Select("SELECT * FROM Drug WHERE drugID = #{drugId}")
    Drug findById(Integer drugId);
}