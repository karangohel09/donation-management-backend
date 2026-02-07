package com.itc.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DonationReceiptResponse {

    private Long id;
    private Long donationId;
    private String receiptNumber;
    private String filePath;
    private String emailStatus;
    private LocalDateTime emailSentDate;
    private Integer emailRetryCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DonationResponse donation;

    public DonationReceiptResponse(Long id, Long id1, String receiptNumber, String filePath, String emailStatus, LocalDateTime emailSentDate, Integer emailRetryCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
    }
}