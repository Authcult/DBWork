package org.example.databasework.controller.admin;

import org.example.databasework.dto.AdminDTO;
import org.example.databasework.mapper.UserMapper;
import org.example.databasework.model.Admin;
import org.example.databasework.model.ApiResponse;
import org.example.databasework.service.UserService;
import org.example.databasework.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/admins")
public class AdminController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Autowired
    public AdminController(UserService userService, UserMapper userMapper, JwtUtil jwtUtil) {
        this.userService = userService;
        this.userMapper = userMapper;
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
     * 添加新管理员账户
     * POST /admin/admins
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AdminDTO>> addAdmin(
            @RequestBody AdminDTO adminDTO,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        // 检查用户名是否已存在
        Admin existingAdmin = userService.findAdminByUsername(adminDTO.getUsername());
        if (existingAdmin != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("ADMIN_EXISTS", "管理员用户名已存在"));
        }
        
        // 创建新管理员账户
        Admin admin = new Admin();
        admin.setUsername(adminDTO.getUsername());
        admin.setPassword(adminDTO.getPassword()); // 实际应用中应该加密密码
        admin.setFullName(adminDTO.getFullName());
        admin.setPhone(adminDTO.getPhone());
        admin.setEmail(adminDTO.getEmail());
        
        // 保存管理员账户
        // 注意：这里假设UserMapper有createAdmin方法，如果没有需要添加
        userMapper.createAdmin(admin);
        
        AdminDTO responseDTO = convertToDTO(admin);
        
        ApiResponse<AdminDTO> response = ApiResponse.success(responseDTO, "管理员账户创建成功");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 获取管理员账户列表
     * GET /admin/admins
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllAdmins(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        // 获取管理员列表
        // 注意：这里假设UserMapper有getAdminsByPage方法，如果没有需要添加
        List<Admin> admins = userMapper.getAllAdmins();
        
        // 手动分页处理
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, admins.size());
        
        List<Admin> pageContent = admins.subList(start, end);
        
        // 转换为DTO
        List<AdminDTO> adminDTOs = pageContent.stream()
                .map(this::convertToDTO)
                .toList();
        
        // 构建分页数据
        Map<String, Object> data = new HashMap<>();
        data.put("content", adminDTOs);
        data.put("totalElements", admins.size());
        data.put("totalPages", (int) Math.ceil((double) admins.size() / pageSize));
        data.put("currentPage", page);
        data.put("pageSize", pageSize);
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "获取管理员列表成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 获取单个管理员账户详情
     * GET /admin/admins/{adminId}
     */
    @GetMapping("/{adminId}")
    public ResponseEntity<ApiResponse<AdminDTO>> getAdminById(
            @PathVariable Integer adminId,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        // 获取管理员详情
        // 注意：这里假设UserMapper有getAdminById方法，如果没有需要添加
        Admin admin = userMapper.getAdminById(adminId);
        
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("ADMIN_NOT_FOUND", "管理员账户不存在"));
        }
        
        AdminDTO adminDTO = convertToDTO(admin);
        
        ApiResponse<AdminDTO> response = ApiResponse.success(adminDTO, "获取管理员详情成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 修改管理员账户信息
     * PUT /admin/admins/{adminId}
     */
    @PutMapping("/{adminId}")
    public ResponseEntity<ApiResponse<AdminDTO>> updateAdmin(
            @PathVariable Integer adminId,
            @RequestBody AdminDTO adminDTO,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        // 获取管理员详情
        Admin admin = userMapper.getAdminById(adminId);
        
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("ADMIN_NOT_FOUND", "管理员账户不存在"));
        }
        
        // 更新管理员信息
        admin.setFullName(adminDTO.getFullName());
        admin.setPhone(adminDTO.getPhone());
        admin.setEmail(adminDTO.getEmail());
        
        // 如果提供了新密码，则更新密码
        if (adminDTO.getPassword() != null && !adminDTO.getPassword().isEmpty()) {
            admin.setPassword(adminDTO.getPassword()); // 实际应用中应该加密密码
        }
        
        // 保存更新后的管理员信息
        userMapper.updateAdmin(admin);
        
        AdminDTO responseDTO = convertToDTO(admin);
        
        ApiResponse<AdminDTO> response = ApiResponse.success(responseDTO, "管理员账户更新成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 删除管理员账户
     * DELETE /admin/admins/{adminId}
     */
    @DeleteMapping("/{adminId}")
    public ResponseEntity<ApiResponse<Void>> deleteAdmin(
            @PathVariable Integer adminId,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        // 获取管理员详情
        Admin admin = userMapper.getAdminById(adminId);
        
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("ADMIN_NOT_FOUND", "管理员账户不存在"));
        }
        
        // 删除管理员账户
        userMapper.deleteAdmin(adminId);
        
        ApiResponse<Void> response = ApiResponse.success(null, "管理员账户删除成功");
        return ResponseEntity.ok(response);
    }
    
    /**
     * 将Admin实体转换为AdminDTO
     */
    private AdminDTO convertToDTO(Admin admin) {
        AdminDTO dto = new AdminDTO();
        dto.setAdminId(admin.getAdminID());
        dto.setUsername(admin.getUsername());
        dto.setFullName(admin.getFullName());
        dto.setPhone(admin.getPhone());
        dto.setEmail(admin.getEmail());
        // 不返回密码
        return dto;
    }
}