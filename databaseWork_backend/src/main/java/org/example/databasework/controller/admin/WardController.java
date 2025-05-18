package org.example.databasework.controller.admin;

import org.example.databasework.dto.BedDTO;
import org.example.databasework.dto.WardDTO;
import org.example.databasework.model.ApiResponse;
import org.example.databasework.model.Bed;
import org.example.databasework.model.Department;
import org.example.databasework.model.Ward;
import org.example.databasework.service.DepartmentService;
import org.example.databasework.service.WardService;
import org.example.databasework.util.JwtUtil;
import org.example.databasework.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/wards")
public class WardController {

    private final WardService wardService;
    private final DepartmentService departmentService;
    private final JwtUtil jwtUtil;

    @Autowired
    public WardController(WardService wardService, DepartmentService departmentService, JwtUtil jwtUtil) {
        this.wardService = wardService;
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
     * 添加新病房
     * POST /admin/wards
     */
    @PostMapping
    public ResponseEntity<ApiResponse<WardDTO>> addWard(
            @RequestBody WardDTO wardDTO,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        Ward ward = new Ward();
        ward.setLocation(wardDTO.getLocation());
        ward.setChargeStandard(wardDTO.getChargeStandard());
        
        // 设置科室关联
        if (wardDTO.getDepartmentId() != null) {
            Department department = new Department();
            department.setDepartmentID(wardDTO.getDepartmentId());
            ward.setDepartment(department);
        }
        
        Ward savedWard = wardService.addWard(ward);
        
        WardDTO responseDTO = convertToDTO(savedWard);
        
        ApiResponse<WardDTO> response = ApiResponse.success(responseDTO, "病房添加成功");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 获取病房列表
     * GET /admin/wards
     * @param page 页码（可选，默认为1）
     * @param pageSize 每页记录数（可选，默认为10）
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllWards(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        // 使用PageUtils创建分页请求
        Page<Ward> wardPage = wardService.getWardsByPage(page - 1, pageSize);
        
        // 使用PageUtils处理分页结果并转换为DTO
        Map<String, Object> data = PageUtils.getPageData(wardPage, page, pageSize, this::convertToDTO);
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "获取病房列表成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 获取单个病房详情
     * GET /admin/wards/{wardId}
     */
    @GetMapping("/{wardId}")
    public ResponseEntity<ApiResponse<WardDTO>> getWardById(
            @PathVariable Integer wardId,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        Ward ward = wardService.getWardById(wardId);
        
        WardDTO wardDTO = convertToDTO(ward);
        
        ApiResponse<WardDTO> response = ApiResponse.success(wardDTO, "获取病房详情成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 修改病房信息
     * PUT /admin/wards/{wardId}
     */
    @PutMapping("/{wardId}")
    public ResponseEntity<ApiResponse<WardDTO>> updateWard(
            @PathVariable Integer wardId,
            @RequestBody WardDTO wardDTO,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        Ward ward = new Ward();
        ward.setLocation(wardDTO.getLocation());
        ward.setChargeStandard(wardDTO.getChargeStandard());
        
        // 设置科室关联
        if (wardDTO.getDepartmentId() != null) {
            Department department = new Department();
            department.setDepartmentID(wardDTO.getDepartmentId());
            ward.setDepartment(department);
        }
        
        Ward updatedWard = wardService.updateWard(wardId, ward);
        
        WardDTO responseDTO = convertToDTO(updatedWard);
        
        ApiResponse<WardDTO> response = ApiResponse.success(responseDTO, "病房更新成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 删除病房
     * DELETE /admin/wards/{wardId}
     */
    @DeleteMapping("/{wardId}")
    public ResponseEntity<ApiResponse<Void>> deleteWard(
            @PathVariable Integer wardId,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        wardService.deleteWard(wardId);
        
        ApiResponse<Void> response = ApiResponse.success(null, "病房删除成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 获取指定病房中的病床列表
     */
    @GetMapping("/{wardId}/beds")
    public ResponseEntity<ApiResponse<List<BedDTO>>> getBedsByWard(
            @PathVariable Integer wardId,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        List<Bed> beds = wardService.getBedsByWard(wardId);
        List<BedDTO> bedDTOs = beds.stream()
                .map(this::convertBedToDTO)
                .collect(Collectors.toList());
        
        ApiResponse<List<BedDTO>> response = ApiResponse.success(bedDTOs, "获取病房病床列表成功");
        return ResponseEntity.ok(response);
    }
    
    /**
     * 将Ward实体转换为WardDTO
     */
    private WardDTO convertToDTO(Ward ward) {
        WardDTO dto = new WardDTO();
        dto.setWardId(ward.getWardID());
        dto.setLocation(ward.getLocation());
        dto.setChargeStandard(ward.getChargeStandard());
        
        if (ward.getDepartment() != null) {
            dto.setDepartmentId(ward.getDepartment().getDepartmentID());
            dto.setDepartmentName(ward.getDepartment().getName());
        }
        
        return dto;
    }
    
    /**
     * 将Bed实体转换为BedDTO
     */
    private BedDTO convertBedToDTO(Bed bed) {
        BedDTO dto = new BedDTO();
        dto.setBedId(bed.getBedID());
        dto.setBedNumber(bed.getBedNumber());
        
        if (bed.getWard() != null) {
            Ward ward = bed.getWard();
            dto.setWardId(ward.getWardID());
            dto.setWardLocation(ward.getLocation());
            
            if (ward.getDepartment() != null) {
                dto.setDepartmentId(ward.getDepartment().getDepartmentID());
                dto.setDepartmentName(ward.getDepartment().getName());
            }
        }
        
        return dto;
    }
}
