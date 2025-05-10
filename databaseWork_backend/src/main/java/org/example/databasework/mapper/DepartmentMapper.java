package org.example.databasework.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.databasework.model.Department;

@Mapper
public interface DepartmentMapper {
    
    @Select("SELECT * FROM Department WHERE departmentID = #{departmentId}")
    Department findById(Integer departmentId);
}