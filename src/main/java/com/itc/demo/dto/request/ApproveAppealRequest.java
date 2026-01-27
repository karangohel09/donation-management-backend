package com.itc.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ApproveAppealRequest {
    private BigDecimal approvedAmount;
    private String remarks;
}