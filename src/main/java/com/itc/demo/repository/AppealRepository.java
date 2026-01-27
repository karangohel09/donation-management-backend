package com.itc.demo.repository;

import com.itc.demo.entity.Appeal;
import com.itc.demo.enums.AppealStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppealRepository extends JpaRepository<Appeal, Long> {
    List<Appeal> findByStatus(AppealStatus status);
    List<Appeal> findByCreatedBy_Id(Long userId);
    List<Appeal> findByBeneficiaryCategory(String category);
}