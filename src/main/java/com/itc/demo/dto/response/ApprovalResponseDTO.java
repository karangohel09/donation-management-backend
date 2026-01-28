// File: com/itc/demo/dto/response/ApprovalResponseDTO.java
package com.itc.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalResponseDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal estimatedAmount;
    private String beneficiaryCategory;
    private String duration;
    private String status;
    private LocalDateTime createdAt;
    private UserResponseDTO createdBy;
    private int documents;
}