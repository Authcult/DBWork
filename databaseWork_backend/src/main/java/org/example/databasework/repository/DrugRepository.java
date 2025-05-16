package org.example.databasework.repository;

import org.example.databasework.model.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Integer> {
    // 基础CRUD操作由JpaRepository提供
}