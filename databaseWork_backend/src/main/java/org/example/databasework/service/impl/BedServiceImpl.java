package org.example.databasework.service.impl;

import org.example.databasework.model.Bed;
import org.example.databasework.model.Ward;
import org.example.databasework.repository.BedRepository;
import org.example.databasework.repository.WardRepository;
import org.example.databasework.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BedServiceImpl implements BedService {

    private final BedRepository bedRepository;
    private final WardRepository wardRepository;

    @Autowired
    public BedServiceImpl(BedRepository bedRepository, WardRepository wardRepository) {
        this.bedRepository = bedRepository;
        this.wardRepository = wardRepository;
    }

    @Override
    @Transactional
    public Bed addBed(Bed bed) {
        // 确保病房存在
        if (bed.getWard() != null && bed.getWard().getWardID() != null) {
            Ward ward = wardRepository.findById(bed.getWard().getWardID())
                    .orElseThrow(() -> new RuntimeException("病房不存在，ID: " + bed.getWard().getWardID()));
            bed.setWard(ward);
        }
        return bedRepository.save(bed);
    }

    @Override
    public List<Bed> getAllBeds() {
        return bedRepository.findAll();
    }
    
    @Override
    public Page<Bed> getBedsByPage(int page, int pageSize) {
        return bedRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Bed getBedById(Integer bedId) {
        return bedRepository.findById(bedId)
                .orElseThrow(() -> new RuntimeException("病床不存在，ID: " + bedId));
    }

    @Override
    @Transactional
    public Bed updateBed(Integer bedId, Bed bedDetails) {
        Bed bed = getBedById(bedId);
        bed.setBedNumber(bedDetails.getBedNumber());
        
        // 更新病房关联
        if (bedDetails.getWard() != null && bedDetails.getWard().getWardID() != null) {
            Ward ward = wardRepository.findById(bedDetails.getWard().getWardID())
                    .orElseThrow(() -> new RuntimeException("病房不存在，ID: " + bedDetails.getWard().getWardID()));
            bed.setWard(ward);
        }
        
        return bedRepository.save(bed);
    }

    @Override
    @Transactional
    public void deleteBed(Integer bedId) {
        Bed bed = getBedById(bedId);
        bedRepository.delete(bed);
    }
    
    @Override
    public List<Bed> getBedsByWardId(Integer wardId) {
        // 确保病房存在
        wardRepository.findById(wardId)
                .orElseThrow(() -> new RuntimeException("病房不存在，ID: " + wardId));
        
        return bedRepository.findAllByWardId(wardId);
    }
    
    @Override
    public Page<Bed> getBedsByWardIdAndPage(Integer wardId, int page, int pageSize) {
        // 由于BedRepository没有直接提供分页查询特定病房的方法，这里先获取所有符合条件的记录
        // 在实际项目中，应该在BedRepository中添加相应的方法以提高性能
        List<Bed> beds = getBedsByWardId(wardId);
        
        // 手动分页处理（简化实现，实际项目中应该优化）
        int start = page * pageSize;
        int end = Math.min(start + pageSize, beds.size());
        
        if (start >= beds.size()) {
            // 返回空页
            return Page.empty(PageRequest.of(page, pageSize));
        }
        
        List<Bed> pageContent = beds.subList(start, end);
        
        // 创建Page对象（简化实现，实际项目中应该使用PageImpl或其他实现）
        return new org.springframework.data.domain.PageImpl<>(pageContent, PageRequest.of(page, pageSize), beds.size());
    }
}