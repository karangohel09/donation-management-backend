package com.itc.demo.repository;

import com.itc.demo.entity.DonationReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonationReceiptRepository extends JpaRepository<DonationReceipt, Long> {

    Optional<DonationReceipt> findByDonationId(Long donationId);

    Optional<DonationReceipt> findByReceiptNumber(String receiptNumber);

    @Query("SELECT d FROM DonationReceipt d WHERE d.emailStatus = 'PENDING' ORDER BY d.createdAt ASC")
    List<DonationReceipt> findPendingEmailSends();

    @Query("SELECT d FROM DonationReceipt d WHERE d.emailStatus = 'FAILED' AND d.emailRetryCount < 3")
    List<DonationReceipt> findFailedEmailsForRetry();
}