package org.example.databasework.repository;

import org.example.databasework.model.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedRepository extends JpaRepository<Bed, Integer> {
    // 根据病房ID查询所有病床
    @Query("SELECT b FROM Bed b WHERE b.ward.wardID = :wardId")
    List<Bed> findAllByWardId(@Param("wardId") Integer wardId);
}