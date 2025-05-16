package org.example.databasework.controller.admin;

import org.example.databasework.model.ApiResponse;
import org.example.databasework.model.Drug;
import org.example.databasework.service.DrugService;
import org.example.databasework.util.JwtUtil;
import org.example.databasework.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/drugs")
public class DrugController {

    private final DrugService drugService;
    private final JwtUtil jwtUtil;

    @Autowired
    public DrugController(DrugService drugService, JwtUtil jwtUtil) {
        this.drugService = drugService;
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
     * 添加新药品
     * POST /admin/drugs
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> addDrug(
            @RequestBody Map<String, Object> drugRequest,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        Drug drug = new Drug();
        drug.setName((String) drugRequest.get("name"));
        drug.setPrice(new BigDecimal(drugRequest.get("price").toString()));
        drug.setStock(Integer.parseInt(drugRequest.get("stock").toString()));
        
        Drug savedDrug = drugService.addDrug(drug);
        
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("drugId", savedDrug.getDrugID());
        responseData.put("name", savedDrug.getName());
        responseData.put("price", savedDrug.getPrice());
        responseData.put("stock", savedDrug.getStock());
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(responseData, "药品添加成功");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 获取药品列表
     * GET /admin/drugs
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllDrugs(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        Page<Drug> drugPage = drugService.getDrugsByPage(page - 1, pageSize);
        
        Map<String, Object> data = PageUtils.getPageData(drugPage, page, pageSize, drug -> {
            Map<String, Object> item = new HashMap<>();
            item.put("drugId", drug.getDrugID());
            item.put("name", drug.getName());
            item.put("price", drug.getPrice());
            item.put("stock", drug.getStock());
            return item;
        });
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "获取药品列表成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 获取单个药品详情
     * GET /admin/drugs/{drugId}
     */
    @GetMapping("/{drugId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDrugById(
            @PathVariable Integer drugId,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        Drug drug = drugService.getDrugById(drugId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("drugId", drug.getDrugID());
        data.put("name", drug.getName());
        data.put("price", drug.getPrice());
        data.put("stock", drug.getStock());
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(data, "获取药品详情成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 修改药品信息
     * PUT /admin/drugs/{drugId}
     */
    @PutMapping("/{drugId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateDrug(
            @PathVariable Integer drugId,
            @RequestBody Map<String, Object> drugRequest,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        Drug drug = new Drug();
        drug.setName((String) drugRequest.get("name"));
        drug.setPrice(new BigDecimal(drugRequest.get("price").toString()));
        drug.setStock(Integer.parseInt(drugRequest.get("stock").toString()));
        
        Drug updatedDrug = drugService.updateDrug(drugId, drug);
        
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("drugId", updatedDrug.getDrugID());
        responseData.put("name", updatedDrug.getName());
        responseData.put("price", updatedDrug.getPrice());
        responseData.put("stock", updatedDrug.getStock());
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(responseData, "药品更新成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 删除药品
     * DELETE /admin/drugs/{drugId}
     */
    @DeleteMapping("/{drugId}")
    public ResponseEntity<ApiResponse<Void>> deleteDrug(
            @PathVariable Integer drugId,
            HttpServletRequest request) {
        validateAdminRole(request);
        
        drugService.deleteDrug(drugId);
        
        ApiResponse<Void> response = ApiResponse.success(null, "药品删除成功");
        return ResponseEntity.ok(response);
    }
}