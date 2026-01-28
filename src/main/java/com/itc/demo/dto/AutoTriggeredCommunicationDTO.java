package com.itc.demo.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoTriggeredCommunicationDTO {

    private Long id;
    private Long appealId;
    private String appealTitle;
    private String triggerType; // 'approval', 'rejection', 'status_update'
    private String channels;
    private Integer recipientCount;
    private String status; // 'sent', 'pending', 'failed'
    private LocalDateTime sentDate;
    private String approverName;
    private Double approvedAmount;
}