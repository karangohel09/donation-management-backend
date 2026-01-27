package com.itc.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateAppealRequest {
    private String title;
    private String description;
    private BigDecimal estimatedAmount;
    private String beneficiaryCategory;
    private String duration;
}