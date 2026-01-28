package com.itc.demo.entity;

import com.itc.demo.enums.CommunicationStatus;
import com.itc.demo.enums.CommunicationTrigger;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "communication_history", indexes = {
        @Index(name = "idx_appeal_trigger", columnList = "appeal_id, trigger_type"),
        @Index(name = "idx_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunicationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appeal_id", nullable = false)
    private Long appealId;

    @Enumerated(EnumType.STRING)
    @Column(name = "trigger_type")
    private CommunicationTrigger triggerType;

    @Column(nullable = false)
    private String channel;

    @Column(name = "recipient_count", nullable = false)
    private Integer recipientCount;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommunicationStatus status;

    @Column(name = "sent_by_user_id")
    private Long sentByUserId;

    @Column(name = "sent_date")
    private LocalDateTime sentDate;

    @Column(name = "error_message", columnDefinition = "LONGTEXT")
    private String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}