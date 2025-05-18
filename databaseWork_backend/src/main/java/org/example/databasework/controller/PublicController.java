package org.example.databasework.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.databasework.dto.DepartmentDTO;
import org.example.databasework.model.ApiResponse;
import org.example.databasework.model.Department;
import org.example.databasework.model.Doctor;
import org.example.databasework.service.DepartmentService;
import org.example.databasework.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 获取科室列表
     * GET /admin/departments
     * @param page 页码（可选，默认为1）
     * @param pageSize 每页记录数（可选，默认为10）
     */
    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllDepartments(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {

        // 使用PageUtils创建分页请求
        Page<Department> departmentPage = departmentService.getDepartmentsByPage(page - 1, pageSize);

        // 使用PageUtils处理分页结果并转换为DTO
        Map<String, Object> data = PageUtils.getPageData(departmentPage, page, pageSize, dept -> {
            DepartmentDTO dto = new DepartmentDTO();
            dto.setDepartmentId(dept.getDepartmentID());
            dto.setName(dept.getName());
            return dto;
        });

        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "获取科室列表成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 查询指定科室下的医生列表
     * @param departmentId
     * @param request
     * @return
     */
    @GetMapping("/departments/{departmentId}/doctors")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDoctorsByDepartmentId(
            @PathVariable Integer departmentId,
            HttpServletRequest request) {
        List<Doctor> doctors = departmentService.getDoctorsByDepartmentId(departmentId);
        Map<String, Object> data = new HashMap<>();
        data.put("total",  doctors.size());
        data.put("doctors", doctors);
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "获取医生列表成功");
        return ResponseEntity.ok(response);
    }
}
