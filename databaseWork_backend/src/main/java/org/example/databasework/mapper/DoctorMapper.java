package org.example.databasework.mapper;

import org.apache.ibatis.annotations.*;
import org.example.databasework.model.Doctor;
import org.example.databasework.model.Department;

import java.util.List;

@Mapper
public interface DoctorMapper {
    
    @Insert("INSERT INTO Doctor (name, gender, title, phone, password, departmentID) VALUES (#{name}, #{gender}, #{title}, #{phone}, #{password}, #{department.departmentID})")
    @Options(useGeneratedKeys = true, keyProperty = "doctorID", keyColumn = "doctorID")
    int createDoctor(Doctor doctor);
    
    @Select("SELECT d.*, dept.name as departmentName FROM Doctor d LEFT JOIN Department dept ON d.departmentID = dept.departmentID")
    @Results({
        @Result(property = "doctorID", column = "doctorID"),
        @Result(property = "name", column = "name"),
        @Result(property = "gender", column = "gender"),
        @Result(property = "title", column = "title"),
        @Result(property = "phone", column = "phone"),
        @Result(property = "department", column = "departmentID", javaType = Department.class,
            one = @One(select = "org.example.databasework.mapper.DepartmentMapper.findById"))
    })
    List<Doctor> findAllDoctors();
    
    @Select("SELECT d.*, dept.name as departmentName FROM Doctor d LEFT JOIN Department dept ON d.departmentID = dept.departmentID WHERE d.doctorID = #{doctorId}")
    @Results({
        @Result(property = "doctorID", column = "doctorID"),
        @Result(property = "name", column = "name"),
        @Result(property = "gender", column = "gender"),
        @Result(property = "title", column = "title"),
        @Result(property = "phone", column = "phone"),
        @Result(property = "department", column = "departmentID", javaType = Department.class,
            one = @One(select = "org.example.databasework.mapper.DepartmentMapper.findById"))
    })
    Doctor findDoctorById(Integer doctorId);
    
    @Update("UPDATE Doctor SET name = #{name}, gender = #{gender}, title = #{title}, phone = #{phone}, departmentID = #{department.departmentID} WHERE doctorID = #{doctorID}")
    int updateDoctor(Doctor doctor);
    
    @Update("UPDATE Doctor SET password = #{password} WHERE doctorID = #{doctorID}")
    int updateDoctorPassword(Doctor doctor);
    
    @Delete("DELETE FROM Doctor WHERE doctorID = #{doctorId}")
    int deleteDoctor(Integer doctorId);
    
    @Select("SELECT COUNT(*) FROM Doctor")
    int countDoctors();
    
    @Select("SELECT d.*, dept.name as departmentName FROM Doctor d LEFT JOIN Department dept ON d.departmentID = dept.departmentID ORDER BY d.doctorID OFFSET #{offset} ROWS FETCH NEXT #{limit} ROWS ONLY")
    @Results({
        @Result(property = "doctorID", column = "doctorID"),
        @Result(property = "name", column = "name"),
        @Result(property = "gender", column = "gender"),
        @Result(property = "title", column = "title"),
        @Result(property = "phone", column = "phone"),
        @Result(property = "department", column = "departmentID", javaType = Department.class,
            one = @One(select = "org.example.databasework.mapper.DepartmentMapper.findById"))
    })
    List<Doctor> findDoctorsByPage(int offset, int limit);

    @Select("SELECT d.DoctorID,d.Name,Gender,Title,Phone,d.DepartmentID, dept.name as departmentName FROM Doctor d LEFT JOIN Department dept ON d.departmentID = dept.departmentID WHERE d.departmentID = #{departmentId}")
    @Results({
        @Result(property = "doctorID", column = "doctorID"),
        @Result(property = "name", column = "name"),
        @Result(property = "gender", column = "gender"),
        @Result(property = "title", column = "title"),
        @Result(property = "phone", column = "phone"),
        @Result(property = "department", column = "departmentID", javaType = Department.class,
            one = @One(select = "org.example.databasework.mapper.DepartmentMapper.findById"))
    })
    List<Doctor> findByDepartmentId(Integer departmentId);
}