package org.example.databasework.repository;

import org.example.databasework.model.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WardRepository extends JpaRepository<Ward, Integer> {
    // 基础CRUD操作由JpaRepository提供
}