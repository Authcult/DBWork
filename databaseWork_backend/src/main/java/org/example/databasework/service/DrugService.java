package org.example.databasework.service;

import org.example.databasework.model.Drug;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DrugService {
    /**
     * 添加新药品
     * @param drug 药品信息
     * @return 添加后的药品信息
     */
    Drug addDrug(Drug drug);
    
    /**
     * 获取所有药品列表
     * @return 药品列表
     */
    List<Drug> getAllDrugs();
    
    /**
     * 分页获取药品列表
     * @param page 页码（从0开始）
     * @param pageSize 每页记录数
     * @return 分页药品列表
     */
    Page<Drug> getDrugsByPage(int page, int pageSize);
    
    /**
     * 根据ID获取药品详情
     * @param drugId 药品ID
     * @return 药品信息
     */
    Drug getDrugById(Integer drugId);
    
    /**
     * 更新药品信息
     * @param drugId 药品ID
     * @param drug 更新的药品信息
     * @return 更新后的药品信息
     */
    Drug updateDrug(Integer drugId, Drug drug);
    
    /**
     * 删除药品
     * @param drugId 药品ID
     */
    void deleteDrug(Integer drugId);
}