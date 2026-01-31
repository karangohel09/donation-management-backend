package com.itc.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendCommunicationRequest {
    private Long appealId;
    private String channel;  // EMAIL, WHATSAPP, POSTAL_MAIL
    private String subject;
    private String message;
    private String recipientType;  // DONORS, APPROVERS, ALL
}
