package com.itc.demo.repository;

import com.itc.demo.entity.DonorAppeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonorAppealRepository extends JpaRepository<DonorAppeal, Long> {
    List<DonorAppeal> findByAppealId(Long appealId);
    List<DonorAppeal> findByDonorId(Long donorId);
}