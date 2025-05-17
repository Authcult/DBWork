package org.example.databasework.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.databasework.model.Ward;

@Mapper
public interface WardMapper {

    @Select("SELECT * FROM Ward WHERE wardID = #{wardId}")
    Ward findById(Integer wardId);
}
