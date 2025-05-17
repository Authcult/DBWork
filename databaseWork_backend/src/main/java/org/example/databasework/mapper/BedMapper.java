package org.example.databasework.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.databasework.model.Bed;

@Mapper
public interface BedMapper {
    @Select("SELECT * FROM Bed WHERE bedID = #{bedId}")
    Bed findById(Integer bedId);
}
