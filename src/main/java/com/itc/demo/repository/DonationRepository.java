package com.itc.demo.repository;

import com.itc.demo.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    List<Donation> findByDonorId(Long donorId);

    List<Donation> findByAppealId(Long appealId);

    List<Donation> findByStatus(String status);

    @Query("SELECT COUNT(d) as count, SUM(d.amount) as total FROM Donation d WHERE d.appeal.id = ?1 AND d.status = 'CONFIRMED'")
    Object getDonationSummaryByAppeal(Long appealId);

    List<Donation> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT d FROM Donation d WHERE d.receiptSent = false AND d.status = 'CONFIRMED'")
    List<Donation> findPendingForReceiptSend();
}