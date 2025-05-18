package org.example.databasework.service.impl;

import org.example.databasework.mapper.DepartmentMapper;
import org.example.databasework.mapper.DoctorMapper;
import org.example.databasework.model.Department;
import org.example.databasework.model.Doctor;
import org.example.databasework.repository.DepartmentRepository;
import org.example.databasework.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final DoctorMapper doctorMapper;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper, DoctorMapper doctorMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.doctorMapper = doctorMapper;
    }

    @Override
    @Transactional
    public Department addDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    
    @Override
    public Page<Department> getDepartmentsByPage(int page, int pageSize) {
        return departmentRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Department getDepartmentById(Integer departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("科室不存在，ID: " + departmentId));
    }

    @Override
    @Transactional
    public Department updateDepartment(Integer departmentId, Department departmentDetails) {
        Department department = getDepartmentById(departmentId);
        department.setName(departmentDetails.getName());
        return departmentRepository.save(department);
    }

    @Override
    @Transactional
    public void deleteDepartment(Integer departmentId) {
        Department department = getDepartmentById(departmentId);
        departmentRepository.delete(department);
    }

    @Override
    public List<Doctor> getDoctorsByDepartmentId(Integer departmentId) {
        return doctorMapper.findByDepartmentId(departmentId);
    }
}