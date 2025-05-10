package org.example.databasework.repository;

import org.example.databasework.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    // 基础CRUD操作由JpaRepository提供
}