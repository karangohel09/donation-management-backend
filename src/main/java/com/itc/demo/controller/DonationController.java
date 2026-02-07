package com.itc.demo.controller;

import com.itc.demo.dto.request.CreateDonationRequest;
import com.itc.demo.dto.response.DonationResponse;
import com.itc.demo.dto.response.DonationReceiptResponse;
import com.itc.demo.service.DonationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/donations")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DonationController {

    @Autowired
    private DonationService donationService;

    /**
     * POST /api/donations
     * Create a new donation
     */
    @PostMapping
    public ResponseEntity<?> createDonation(
            @Valid @RequestBody CreateDonationRequest request,
            Authentication authentication) {
        try {
            // Get user ID from authentication
            String userId = authentication.getName();
            Long userIdLong = Long.parseLong(userId);

            DonationResponse response = donationService.createDonation(request, userIdLong);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Donation created successfully");
            result.put("data", response);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * GET /api/donations/{id}
     * Get donation by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDonationById(@PathVariable Long id) {
        try {
            DonationResponse response = donationService.getDonationById(id);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", response);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * GET /api/donations/donor/{donorId}
     * Get all donations for a donor
     */
    @GetMapping("/donor/{donorId}")
    public ResponseEntity<?> getDonationsByDonor(@PathVariable Long donorId) {
        try {
            List<DonationResponse> responses = donationService.getDonationsByDonor(donorId);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("count", responses.size());
            result.put("data", responses);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * GET /api/donations/appeal/{appealId}
     * Get all donations for an appeal
     */
    @GetMapping("/appeal/{appealId}")
    public ResponseEntity<?> getDonationsByAppeal(@PathVariable Long appealId) {
        try {
            List<DonationResponse> responses = donationService.getDonationsByAppeal(appealId);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("count", responses.size());
            result.put("data", responses);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * GET /api/donations
     * Get all donations
     */
    @GetMapping
    public ResponseEntity<?> getAllDonations() {
        try {
            List<DonationResponse> responses = donationService.getAllDonations();

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("count", responses.size());
            result.put("data", responses);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * GET /api/donations/status/{status}
     * Get donations by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getDonationsByStatus(@PathVariable String status) {
        try {
            List<DonationResponse> responses = donationService.getDonationsByStatus(status);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("count", responses.size());
            result.put("data", responses);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * POST /api/donations/{id}/confirm
     * Confirm a donation
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<?> confirmDonation(@PathVariable Long id) {
        try {
            DonationResponse response = donationService.confirmDonation(id);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Donation confirmed successfully");
            result.put("data", response);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * POST /api/donations/{id}/fail
     * Mark donation as failed
     */
    @PostMapping("/{id}/fail")
    public ResponseEntity<?> failDonation(@PathVariable Long id) {
        try {
            DonationResponse response = donationService.failDonation(id);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Donation marked as failed");
            result.put("data", response);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * POST /api/donations/{id}/generate-receipt
     * Generate receipt for donation
     */
    @PostMapping("/{id}/generate-receipt")
    public ResponseEntity<?> generateReceipt(@PathVariable Long id) {
        try {
            DonationReceiptResponse response = donationService.generateReceipt(id);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Receipt generated successfully");
            result.put("data", response);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * POST /api/donations/receipts/{receiptId}/send-email
     * Send receipt via email
     */
    @PostMapping("/receipts/{receiptId}/send-email")
    public ResponseEntity<?> sendReceiptEmail(@PathVariable Long receiptId) {
        try {
            donationService.sendReceiptEmail(receiptId);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Receipt email sent successfully");

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * GET /api/donations/pending-receipt
     * Get donations pending receipt
     */
    @GetMapping("/pending-receipt")
    public ResponseEntity<?> getDonationsPendingReceipt() {
        try {
            List<DonationResponse> responses = donationService.getDonationsPendingReceipt();

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("count", responses.size());
            result.put("data", responses);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * DELETE /api/donations/{id}
     * Delete a donation (only if PENDING)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDonation(@PathVariable Long id) {
        try {
            donationService.deleteDonation(id);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Donation deleted successfully");

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}