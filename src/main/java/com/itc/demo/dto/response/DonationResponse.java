package com.itc.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DonationResponse {

    private Long id;
    private Long donorId;
    private String donorName;
    private Long appealId;
    private String appealTitle;
    private BigDecimal amount;
    private String paymentMode;
    private String transactionId;
    private LocalDateTime paymentDate;
    private String status;
    private Boolean receiptSent;
    private LocalDateTime receiptSentDate;
    private String notes;
    private String recordedByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}