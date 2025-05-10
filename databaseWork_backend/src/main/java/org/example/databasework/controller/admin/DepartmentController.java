package org.example.databasework.controller.admin;

import org.example.databasework.dto.DepartmentDTO;
import org.example.databasework.model.ApiResponse;
import org.example.databasework.model.Department;
import org.example.databasework.service.DepartmentService;
import org.example.databasework.util.JwtUtil;
import org.example.databasework.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;  
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final JwtUtil jwtUtil;

    @Autowired
    public DepartmentController(DepartmentService departmentService, JwtUtil jwtUtil) {
        this.departmentService = departmentService;
        this.jwtUtil = jwtUtil;
    }
    
    /**
     * 验证是否为管理员角色
     */
    private void validateAdminRole(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String role = jwtUtil.getClaimFromToken(token, claims -> claims.get("role", String.class));
            if (!"admin".equals(role)) {
                throw new RuntimeException("权限不足，需要管理员权限");
            }
        }
    }

    /**
     * 添加新科室
     * POST /admin/departments
     */
    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentDTO>> addDepartment(
            @RequestBody DepartmentDTO departmentDTO,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        Department department = new Department();
        department.setName(departmentDTO.getName());
        
        Department savedDepartment = departmentService.addDepartment(department);
        
        DepartmentDTO responseDTO = new DepartmentDTO();
        responseDTO.setDepartmentId(savedDepartment.getDepartmentID());
        responseDTO.setName(savedDepartment.getName());
        
        ApiResponse<DepartmentDTO> response = ApiResponse.success(responseDTO, "科室添加成功");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 获取科室列表
     * GET /admin/departments
     * @param page 页码（可选，默认为1）
     * @param pageSize 每页记录数（可选，默认为10）
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllDepartments(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        validateAdminRole(request);
        
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
     * 获取单个科室详情
     * GET /admin/departments/{departmentId}
     */
    @GetMapping("/{departmentId}")
    public ResponseEntity<ApiResponse<DepartmentDTO>> getDepartmentById(
            @PathVariable Integer departmentId,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        Department department = departmentService.getDepartmentById(departmentId);
        
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentId(department.getDepartmentID());
        departmentDTO.setName(department.getName());
        
        ApiResponse<DepartmentDTO> response = ApiResponse.success(departmentDTO, "获取科室详情成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 修改科室信息
     * PUT /admin/departments/{departmentId}
     */
    @PutMapping("/{departmentId}")
    public ResponseEntity<ApiResponse<DepartmentDTO>> updateDepartment(
            @PathVariable Integer departmentId,
            @RequestBody DepartmentDTO departmentDTO,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        Department department = new Department();
        department.setName(departmentDTO.getName());
        
        Department updatedDepartment = departmentService.updateDepartment(departmentId, department);
        
        DepartmentDTO responseDTO = new DepartmentDTO();
        responseDTO.setDepartmentId(updatedDepartment.getDepartmentID());
        responseDTO.setName(updatedDepartment.getName());
        
        ApiResponse<DepartmentDTO> response = ApiResponse.success(responseDTO, "科室更新成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 删除科室
     * DELETE /admin/departments/{departmentId}
     */
    @DeleteMapping("/{departmentId}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(
            @PathVariable Integer departmentId,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        departmentService.deleteDepartment(departmentId);
        
        ApiResponse<Void> response = ApiResponse.success(null, "科室删除成功");
        return ResponseEntity.ok(response);
    }
}