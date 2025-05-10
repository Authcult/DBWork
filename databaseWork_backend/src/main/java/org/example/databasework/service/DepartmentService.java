package org.example.databasework.service;

import org.example.databasework.model.Department;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DepartmentService {
    /**
     * 添加新科室
     * @param department 科室信息
     * @return 添加后的科室信息
     */
    Department addDepartment(Department department);
    
    /**
     * 获取所有科室列表
     * @return 科室列表
     */
    List<Department> getAllDepartments();
    
    /**
     * 分页获取科室列表
     * @param page 页码（从0开始）
     * @param pageSize 每页记录数
     * @return 分页科室列表
     */
    Page<Department> getDepartmentsByPage(int page, int pageSize);
    
    /**
     * 根据ID获取科室详情
     * @param departmentId 科室ID
     * @return 科室信息
     */
    Department getDepartmentById(Integer departmentId);
    
    /**
     * 更新科室信息
     * @param departmentId 科室ID
     * @param department 更新的科室信息
     * @return 更新后的科室信息
     */
    Department updateDepartment(Integer departmentId, Department department);
    
    /**
     * 删除科室
     * @param departmentId 科室ID
     */
    void deleteDepartment(Integer departmentId);
}