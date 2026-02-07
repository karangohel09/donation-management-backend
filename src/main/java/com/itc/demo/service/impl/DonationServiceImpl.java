package com.itc.demo.service.impl;

import com.itc.demo.dto.request.CreateDonationRequest;
import com.itc.demo.dto.response.DonationResponse;
import com.itc.demo.dto.response.DonationReceiptResponse;
import com.itc.demo.entity.Donation;
import com.itc.demo.entity.DonationReceipt;
import com.itc.demo.entity.Donor;
import com.itc.demo.entity.Appeal;
import com.itc.demo.entity.User;
import com.itc.demo.repository.DonationRepository;
import com.itc.demo.repository.DonationReceiptRepository;
import com.itc.demo.repository.DonorRepository;
import com.itc.demo.repository.AppealRepository;
import com.itc.demo.repository.UserRepository;
import com.itc.demo.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DonationServiceImpl implements DonationService {

    private static final Logger logger = LoggerFactory.getLogger(DonationServiceImpl.class);

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private DonationReceiptRepository donationReceiptRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private AppealRepository appealRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public DonationResponse createDonation(CreateDonationRequest request, Long userId) {
        logger.info("Creating donation for donor: {} appeal: {}", request.getDonorId(), request.getAppealId());

        // Fetch donor and appeal
        Donor donor = donorRepository.findById(request.getDonorId())
                .orElseThrow(() -> new RuntimeException("Donor not found with ID: " + request.getDonorId()));

        Appeal appeal = appealRepository.findById(request.getAppealId())
                .orElseThrow(() -> new RuntimeException("Appeal not found with ID: " + request.getAppealId()));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Create donation
        Donation donation = new Donation();
        donation.setDonor(donor);
        donation.setAppeal(appeal);
        donation.setAmount(request.getAmount());
        donation.setPaymentMode(request.getPaymentMode());
        donation.setTransactionId(request.getTransactionId());
        donation.setPaymentDate(LocalDateTime.now());
        donation.setStatus("PENDING");
        donation.setReceiptSent(false);
        donation.setNotes(request.getNotes());
        donation.setRecordedBy(user);

        Donation savedDonation = donationRepository.save(donation);
        logger.info("Donation created with ID: {}", savedDonation.getId());

        return convertToResponse(savedDonation);
    }

    @Override
    public DonationResponse getDonationById(Long id) {
        logger.info("Fetching donation by ID: {}", id);
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found with ID: " + id));
        return convertToResponse(donation);
    }

    @Override
    public List<DonationResponse> getDonationsByDonor(Long donorId) {
        logger.info("Fetching donations for donor: {}", donorId);
        List<Donation> donations = donationRepository.findByDonorId(donorId);
        return donations.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public List<DonationResponse> getDonationsByAppeal(Long appealId) {
        logger.info("Fetching donations for appeal: {}", appealId);
        List<Donation> donations = donationRepository.findByAppealId(appealId);
        return donations.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public List<DonationResponse> getAllDonations() {
        logger.info("Fetching all donations");
        List<Donation> donations = donationRepository.findAll();
        return donations.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public List<DonationResponse> getDonationsByStatus(String status) {
        logger.info("Fetching donations with status: {}", status);
        List<Donation> donations = donationRepository.findByStatus(status);
        return donations.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public List<DonationResponse> getDonationsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Fetching donations between {} and {}", startDate, endDate);
        List<Donation> donations = donationRepository.findByCreatedAtBetween(startDate, endDate);
        return donations.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public DonationResponse confirmDonation(Long donationId) {
        logger.info("Confirming donation: {}", donationId);
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found with ID: " + donationId));

        donation.setStatus("CONFIRMED");
        Donation updatedDonation = donationRepository.save(donation);

        logger.info("Donation confirmed: {}", donationId);
        return convertToResponse(updatedDonation);
    }

    @Override
    public DonationResponse failDonation(Long donationId) {
        logger.info("Marking donation as failed: {}", donationId);
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found with ID: " + donationId));

        donation.setStatus("FAILED");
        Donation updatedDonation = donationRepository.save(donation);

        logger.info("Donation marked as failed: {}", donationId);
        return convertToResponse(updatedDonation);
    }

    @Override
    public DonationReceiptResponse generateReceipt(Long donationId) {
        logger.info("Generating receipt for donation: {}", donationId);

        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found with ID: " + donationId));

        // Generate unique receipt number
        String receiptNumber = "RCPT-" + System.currentTimeMillis();

        // Create receipt
        DonationReceipt receipt = new DonationReceipt();
        receipt.setDonation(donation);
        receipt.setReceiptNumber(receiptNumber);
        receipt.setFilePath("/receipts/" + receiptNumber + ".pdf");
        receipt.setEmailStatus("PENDING");
        receipt.setEmailRetryCount(0);

        DonationReceipt savedReceipt = donationReceiptRepository.save(receipt);

        // Mark donation as receipt pending
        donation.setReceiptSent(false);
        donationRepository.save(donation);

        logger.info("Receipt generated with number: {}", receiptNumber);
        return convertReceiptToResponse(savedReceipt);
    }

    @Override
    public void sendReceiptEmail(Long receiptId) {
        logger.info("Sending receipt email for receipt ID: {}", receiptId);

        DonationReceipt receipt = donationReceiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Receipt not found with ID: " + receiptId));

        try {
            Donation donation = receipt.getDonation();
            String donorEmail = donation.getDonor().getEmail();
            String donorName = donation.getDonor().getName();

            // Create email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(donorEmail);
            message.setSubject("Donation Receipt - " + receipt.getReceiptNumber());
            message.setText(buildReceiptEmailBody(donation, receipt, donorName));
            message.setFrom("noreply@donationmanagement.com");

            // Send email
            mailSender.send(message);

            // Update receipt status
            receipt.setEmailStatus("SENT");
            receipt.setEmailSentDate(LocalDateTime.now());
            donationReceiptRepository.save(receipt);

            // Mark donation receipt as sent
            donation.setReceiptSent(true);
            donation.setReceiptSentDate(LocalDateTime.now());
            donationRepository.save(donation);

            logger.info("Receipt email sent successfully to: {}", donorEmail);
        } catch (Exception e) {
            logger.error("Failed to send receipt email for receipt ID: {}", receiptId, e);

            // Mark as failed and increment retry count
            receipt.setEmailStatus("FAILED");
            receipt.setEmailRetryCount(receipt.getEmailRetryCount() + 1);
            donationReceiptRepository.save(receipt);

            throw new RuntimeException("Failed to send receipt email: " + e.getMessage());
        }
    }

    @Override
    public List<DonationResponse> getDonationsPendingReceipt() {
        logger.info("Fetching donations pending receipt");
        List<Donation> donations = donationRepository.findPendingForReceiptSend();
        return donations.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public Double getTotalDonationByAppeal(Long appealId) {
        logger.info("Calculating total donation for appeal: {}", appealId);
        Object result = donationRepository.getDonationSummaryByAppeal(appealId);
        // Handle the result appropriately based on your database
        return 0.0; // Placeholder - adjust based on actual query result
    }

    @Override
    public void deleteDonation(Long donationId) {
        logger.info("Deleting donation: {}", donationId);

        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found with ID: " + donationId));

        // Only allow deletion of PENDING donations
        if (!"PENDING".equals(donation.getStatus())) {
            throw new RuntimeException("Can only delete donations with PENDING status");
        }

        donationRepository.deleteById(donationId);
        logger.info("Donation deleted: {}", donationId);
    }

    // Helper methods
    private DonationResponse convertToResponse(Donation donation) {
        return new DonationResponse(
                donation.getId(),
                donation.getDonor().getId(),
                donation.getDonor().getName(),
                donation.getAppeal().getId(),
                donation.getAppeal().getTitle(),
                donation.getAmount(),
                donation.getPaymentMode(),
                donation.getTransactionId(),
                donation.getPaymentDate(),
                donation.getStatus(),
                donation.getReceiptSent(),
                donation.getReceiptSentDate(),
                donation.getNotes(),
                donation.getRecordedBy() != null ? donation.getRecordedBy().getName() : null,
                donation.getCreatedAt(),
                donation.getUpdatedAt()
        );
    }

    private DonationReceiptResponse convertReceiptToResponse(DonationReceipt receipt) {
        DonationReceiptResponse response = new DonationReceiptResponse(
                receipt.getId(),
                receipt.getDonation().getId(),
                receipt.getReceiptNumber(),
                receipt.getFilePath(),
                receipt.getEmailStatus(),
                receipt.getEmailSentDate(),
                receipt.getEmailRetryCount(),
                receipt.getCreatedAt(),
                receipt.getUpdatedAt()
        );
        response.setDonation(convertToResponse(receipt.getDonation()));
        return response;
    }

    private String buildReceiptEmailBody(Donation donation, DonationReceipt receipt, String donorName) {
        return "Dear " + donorName + ",\n\n" +
                "Thank you for your generous donation!\n\n" +
                "Receipt Details:\n" +
                "Receipt Number: " + receipt.getReceiptNumber() + "\n" +
                "Amount: ₹" + donation.getAmount() + "\n" +
                "Payment Mode: " + donation.getPaymentMode() + "\n" +
                "Appeal: " + donation.getAppeal().getTitle() + "\n" +
                "Date: " + donation.getPaymentDate() + "\n\n" +
                "Your donation will make a real difference.\n\n" +
                "Best regards,\n" +
                "Donation Management Team";
    }
}