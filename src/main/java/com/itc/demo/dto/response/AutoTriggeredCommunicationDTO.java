package com.itc.demo.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoTriggeredCommunicationDTO {
    private Long id;
    private Long appealId;
    private String triggerType;  // APPROVAL, REJECTION, STATUS_UPDATE
    private String channels;      // EMAIL, WHATSAPP, POSTAL_MAIL
    private Integer recipientCount;
    private String status;        // SENT, PENDING, FAILED
    private LocalDateTime sentDate;
}
