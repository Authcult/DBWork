package org.example.databasework.service;

import org.example.databasework.model.Bed;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BedService {
    /**
     * 添加新病床
     * @param bed 病床信息
     * @return 添加后的病床信息
     */
    Bed addBed(Bed bed);
    
    /**
     * 获取所有病床列表
     * @return 病床列表
     */
    List<Bed> getAllBeds();
    
    /**
     * 分页获取病床列表
     * @param page 页码（从0开始）
     * @param pageSize 每页记录数
     * @return 分页病床列表
     */
    Page<Bed> getBedsByPage(int page, int pageSize);
    
    /**
     * 根据ID获取病床详情
     * @param bedId 病床ID
     * @return 病床信息
     */
    Bed getBedById(Integer bedId);
    
    /**
     * 更新病床信息
     * @param bedId 病床ID
     * @param bed 更新的病床信息
     * @return 更新后的病床信息
     */
    Bed updateBed(Integer bedId, Bed bed);
    
    /**
     * 删除病床
     * @param bedId 病床ID
     */
    void deleteBed(Integer bedId);
    
    /**
     * 根据病房ID获取病床列表
     * @param wardId 病房ID
     * @return 病床列表
     */
    List<Bed> getBedsByWardId(Integer wardId);
    
    /**
     * 根据病房ID分页获取病床列表
     * @param wardId 病房ID
     * @param page 页码（从0开始）
     * @param pageSize 每页记录数
     * @return 分页病床列表
     */
    Page<Bed> getBedsByWardIdAndPage(Integer wardId, int page, int pageSize);
}