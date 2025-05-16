package org.example.databasework.service;

import org.example.databasework.model.Bed;
import org.example.databasework.model.Ward;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WardService {
    /**
     * 添加新病房
     * @param ward 病房信息
     * @return 添加后的病房信息
     */
    Ward addWard(Ward ward);
    
    /**
     * 获取所有病房列表
     * @return 病房列表
     */
    List<Ward> getAllWards();
    
    /**
     * 分页获取病房列表
     * @param page 页码（从0开始）
     * @param pageSize 每页记录数
     * @return 分页病房列表
     */
    Page<Ward> getWardsByPage(int page, int pageSize);
    
    /**
     * 根据ID获取病房详情
     * @param wardId 病房ID
     * @return 病房信息
     */
    Ward getWardById(Integer wardId);
    
    /**
     * 更新病房信息
     * @param wardId 病房ID
     * @param ward 更新的病房信息
     * @return 更新后的病房信息
     */
    Ward updateWard(Integer wardId, Ward ward);
    
    /**
     * 删除病房
     * @param wardId 病房ID
     */
    void deleteWard(Integer wardId);
    
    /**
     * 根据病房ID获取所有床铺信息
     * @param wardId 病房ID
     * @return 床铺列表
     */
    List<Bed> getBedsByWard(Integer wardId);
}