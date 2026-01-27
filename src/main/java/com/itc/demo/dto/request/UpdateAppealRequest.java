package com.itc.demo.dto.request;

import com.itc.demo.enums.AppealStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateAppealRequest {
    private String title;
    private String description;
    private BigDecimal estimatedAmount;
    private String beneficiaryCategory;
    private String duration;
    private AppealStatus status;
}