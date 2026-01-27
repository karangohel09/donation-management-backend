package com.itc.demo.dto.response;

import com.itc.demo.enums.AppealStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AppealResponseDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal estimatedAmount;
    private BigDecimal approvedAmount;
    private String beneficiaryCategory;
    private String duration;
    private AppealStatus status;
    private UserResponseDTO createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}