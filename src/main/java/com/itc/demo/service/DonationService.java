package com.itc.demo.service;

import com.itc.demo.dto.request.CreateDonationRequest;
import com.itc.demo.dto.response.DonationReceiptResponse;
import com.itc.demo.dto.response.DonationResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface DonationService {

    /**
     * Record a new donation
     */
    DonationResponse createDonation(CreateDonationRequest request, Long userId);

    /**
     * Get donation by ID
     */
    DonationResponse getDonationById(Long id);

    /**
     * Get all donations for a donor
     */
    List<DonationResponse> getDonationsByDonor(Long donorId);

    /**
     * Get all donations for an appeal
     */
    List<DonationResponse> getDonationsByAppeal(Long appealId);

    /**
     * Get all donations
     */
    List<DonationResponse> getAllDonations();

    /**
     * Get donations by status
     */
    List<DonationResponse> getDonationsByStatus(String status);

    /**
     * Get donations between dates
     */
    List<DonationResponse> getDonationsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Confirm donation (change status from PENDING to CONFIRMED)
     */
    DonationResponse confirmDonation(Long donationId);

    /**
     * Mark donation as failed
     */
    DonationResponse failDonation(Long donationId);

    /**
     * Generate receipt for donation (PDF)
     */
    DonationReceiptResponse generateReceipt(Long donationId);

    /**
     * Send receipt to donor via email
     */
    void sendReceiptEmail(Long receiptId);

    /**
     * Get all donations pending receipt send
     */
    List<DonationResponse> getDonationsPendingReceipt();

    /**
     * Get total donations for an appeal
     */
    Double getTotalDonationByAppeal(Long appealId);

    /**
     * Delete donation (only if PENDING)
     */
    void deleteDonation(Long donationId);
}