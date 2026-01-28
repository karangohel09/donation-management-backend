package com.itc.demo.repository;

import com.itc.demo.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {

    Optional<Donor> findByEmail(String email);

    @Query("SELECT DISTINCT d FROM Donor d " +
            "WHERE d.isActive = true AND d.id IN " +
            "(SELECT da.donor.id FROM DonorAppeal da WHERE da.appeal.id = :appealId)")
    List<Donor> findByAppealId(@Param("appealId") Long appealId);

    List<Donor> findByIsActiveTrue();
}