package org.example.databasework.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.example.databasework.model.Bed;

@Mapper
public interface BedMapper {
    @Select("SELECT * FROM Bed WHERE bedID = #{bedId}")
    Bed findById(Integer bedId);

    @Select("UPDATE Bed SET status = #{occupied} WHERE bedID = #{bedId}")
    void updateStatus(Integer bedId, String occupied);
}
