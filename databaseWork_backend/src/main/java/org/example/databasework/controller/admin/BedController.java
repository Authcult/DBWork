package org.example.databasework.controller.admin;

import org.example.databasework.dto.BedDTO;
import org.example.databasework.model.ApiResponse;
import org.example.databasework.model.Bed;
import org.example.databasework.model.Ward;
import org.example.databasework.service.BedService;
import org.example.databasework.service.WardService;
import org.example.databasework.util.JwtUtil;
import org.example.databasework.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/admin/beds")
public class BedController {

    private final BedService bedService;
    private final WardService wardService;
    private final JwtUtil jwtUtil;

    @Autowired
    public BedController(BedService bedService, WardService wardService, JwtUtil jwtUtil) {
        this.bedService = bedService;
        this.wardService = wardService;
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
     * 添加新病床
     * POST /admin/beds
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BedDTO>> addBed(
            @RequestBody BedDTO bedDTO,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        Bed bed = new Bed();
        bed.setBedNumber(bedDTO.getBedNumber());
        
        // 设置病房关联
        if (bedDTO.getWardId() != null) {
            Ward ward = new Ward();
            ward.setWardID(bedDTO.getWardId());
            bed.setWard(ward);
        }
        
        Bed savedBed = bedService.addBed(bed);
        
        BedDTO responseDTO = convertToDTO(savedBed);
        
        ApiResponse<BedDTO> response = ApiResponse.success(responseDTO, "病床添加成功");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 获取病床列表
     * GET /admin/beds
     * @param page 页码（可选，默认为1）
     * @param pageSize 每页记录数（可选，默认为10）
     * @param wardId 病房ID（可选，按病房筛选）
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllBeds(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer wardId,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        Page<Bed> bedPage;
        if (wardId != null) {
            // 按病房ID筛选
            bedPage = bedService.getBedsByWardIdAndPage(wardId, page - 1, pageSize);
        } else {
            // 获取所有病床
            bedPage = bedService.getBedsByPage(page - 1, pageSize);
        }
        
        // 使用PageUtils处理分页结果并转换为DTO
        Map<String, Object> data = PageUtils.getPageData(bedPage, page, pageSize, this::convertToDTO);
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "获取病床列表成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 获取单个病床详情
     * GET /admin/beds/{bedId}
     */
    @GetMapping("/{bedId}")
    public ResponseEntity<ApiResponse<BedDTO>> getBedById(
            @PathVariable Integer bedId,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        Bed bed = bedService.getBedById(bedId);
        
        BedDTO bedDTO = convertToDTO(bed);
        
        ApiResponse<BedDTO> response = ApiResponse.success(bedDTO, "获取病床详情成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 修改病床信息
     * PUT /admin/beds/{bedId}
     */
    @PutMapping("/{bedId}")
    public ResponseEntity<ApiResponse<BedDTO>> updateBed(
            @PathVariable Integer bedId,
            @RequestBody BedDTO bedDTO,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        Bed bed = new Bed();
        bed.setBedNumber(bedDTO.getBedNumber());
        
        // 设置病房关联
        if (bedDTO.getWardId() != null) {
            Ward ward = new Ward();
            ward.setWardID(bedDTO.getWardId());
            bed.setWard(ward);
        }
        
        Bed updatedBed = bedService.updateBed(bedId, bed);
        
        BedDTO responseDTO = convertToDTO(updatedBed);
        
        ApiResponse<BedDTO> response = ApiResponse.success(responseDTO, "病床更新成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 删除病床
     * DELETE /admin/beds/{bedId}
     */
    @DeleteMapping("/{bedId}")
    public ResponseEntity<ApiResponse<Void>> deleteBed(
            @PathVariable Integer bedId,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        bedService.deleteBed(bedId);
        
        ApiResponse<Void> response = ApiResponse.success(null, "病床删除成功");
        return ResponseEntity.ok(response);
    }
    
    /**
     * 将Bed实体转换为BedDTO
     */
    private BedDTO convertToDTO(Bed bed) {
        BedDTO dto = new BedDTO();
        dto.setBedId(bed.getBedID());
        dto.setBedNumber(bed.getBedNumber());
        
        if (bed.getWard() != null) {
            dto.setWardId(bed.getWard().getWardID());
            dto.setWardLocation(bed.getWard().getLocation());
            
            if (bed.getWard().getDepartment() != null) {
                dto.setDepartmentId(bed.getWard().getDepartment().getDepartmentID());
                dto.setDepartmentName(bed.getWard().getDepartment().getName());
            }
        }
        
        return dto;
    }
}