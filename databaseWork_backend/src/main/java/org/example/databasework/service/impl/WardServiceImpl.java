package org.example.databasework.service.impl;

import org.example.databasework.model.Bed;
import org.example.databasework.model.Department;
import org.example.databasework.model.Ward;
import org.example.databasework.repository.DepartmentRepository;
import org.example.databasework.repository.WardRepository;
import org.example.databasework.repository.BedRepository;
import org.example.databasework.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WardServiceImpl implements WardService {

    private final WardRepository wardRepository;
    private final DepartmentRepository departmentRepository;
    private final BedRepository bedRepository;

    @Autowired
    public WardServiceImpl(WardRepository wardRepository, DepartmentRepository departmentRepository, 
                           BedRepository bedRepository) {
        this.wardRepository = wardRepository;
        this.departmentRepository = departmentRepository;
        this.bedRepository = bedRepository;
    }

    @Override
    @Transactional
    public Ward addWard(Ward ward) {
        // 确保科室存在
        if (ward.getDepartment() != null && ward.getDepartment().getDepartmentID() != null) {
            Department department = departmentRepository.findById(ward.getDepartment().getDepartmentID())
                    .orElseThrow(() -> new RuntimeException("科室不存在，ID: " + ward.getDepartment().getDepartmentID()));
            ward.setDepartment(department);
        }
        return wardRepository.save(ward);
    }

    @Override
    public List<Ward> getAllWards() {
        return wardRepository.findAll();
    }
    
    @Override
    public Page<Ward> getWardsByPage(int page, int pageSize) {
        return wardRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Ward getWardById(Integer wardId) {
        return wardRepository.findById(wardId)
                .orElseThrow(() -> new RuntimeException("病房不存在，ID: " + wardId));
    }

    @Override
    @Transactional
    public Ward updateWard(Integer wardId, Ward wardDetails) {
        Ward ward = getWardById(wardId);
        ward.setLocation(wardDetails.getLocation());
        ward.setChargeStandard(wardDetails.getChargeStandard());
        
        // 更新科室关联
        if (wardDetails.getDepartment() != null && wardDetails.getDepartment().getDepartmentID() != null) {
            Department department = departmentRepository.findById(wardDetails.getDepartment().getDepartmentID())
                    .orElseThrow(() -> new RuntimeException("科室不存在，ID: " + wardDetails.getDepartment().getDepartmentID()));
            ward.setDepartment(department);
        }
        
        return wardRepository.save(ward);
    }

    @Override
    @Transactional
    public void deleteWard(Integer wardId) {
        Ward ward = getWardById(wardId);
        wardRepository.delete(ward);
    }

    @Override
    @Transactional
    public List<Bed> getBedsByWard(Integer wardId) {
        Ward ward = wardRepository.findById(wardId)
                .orElseThrow(() -> new RuntimeException("病房不存在，ID: " + wardId));
        
        return bedRepository.findAllByWardId(wardId);
    }
}
