package com.itc.demo.service.impl;

import com.itc.demo.entity.*;
import com.itc.demo.enums.CommunicationStatus;
import com.itc.demo.enums.CommunicationTrigger;
import com.itc.demo.dto.AutoTriggeredCommunicationDTO;
import com.itc.demo.repository.*;
import com.itc.demo.service.CommunicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class CommunicationServiceImpl implements CommunicationService {

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private CommunicationHistoryRepository communicationHistoryRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    /**
     * Notify all donors when an appeal is approved
     */
    @Override
    public void notifyDonorsOnApproval(Appeal appeal, Long approverUserId) {
        try {
            // Get all donors associated with this appeal
            List<Donor> relevantDonors = donorRepository.findByAppealId(appeal.getId());

            if (relevantDonors.isEmpty()) {
                log.warn("No donors found for appeal: " + appeal.getId());
                return;
            }

            log.info("Notifying {} donors about approval of appeal: {}", relevantDonors.size(), appeal.getId());

            // Build approval message
            String emailSubject = "Great News! Your Appeal \"" + appeal.getTitle() + "\" is Approved";
            String emailContent = buildApprovalEmailContent(appeal);
            String whatsappMessage = buildApprovalWhatsAppMessage(appeal);

            // Send email notifications
            sendEmailNotifications(appeal, relevantDonors, emailSubject, emailContent,
                    CommunicationTrigger.APPROVAL, approverUserId);

            // Send WhatsApp notifications (if service is available)
            sendWhatsAppNotifications(appeal, relevantDonors, whatsappMessage,
                    CommunicationTrigger.APPROVAL, approverUserId);

            log.info("Successfully notified donors for appeal approval: {}", appeal.getId());

        } catch (Exception e) {
            log.error("Error notifying donors on approval: " + e.getMessage(), e);
            // Don't throw exception - approval should not fail if communication fails
        }
    }

    /**
     * Notify all donors when an appeal is rejected
     */
    @Override
    public void notifyDonorsOnRejection(Appeal appeal, String rejectionReason) {
        try {
            List<Donor> relevantDonors = donorRepository.findByAppealId(appeal.getId());

            if (relevantDonors.isEmpty()) {
                log.warn("No donors found for appeal: " + appeal.getId());
                return;
            }

            log.info("Notifying {} donors about rejection of appeal: {}", relevantDonors.size(), appeal.getId());

            String emailSubject = "Update on Your Appeal: \"" + appeal.getTitle() + "\"";
            String emailContent = buildRejectionEmailContent(appeal, rejectionReason);

            sendEmailNotifications(appeal, relevantDonors, emailSubject, emailContent,
                    CommunicationTrigger.REJECTION, null);

            log.info("Successfully notified donors for appeal rejection: {}", appeal.getId());

        } catch (Exception e) {
            log.error("Error notifying donors on rejection: " + e.getMessage(), e);
        }
    }

    /**
     * Get all auto-triggered communications
     */
    @Override
    public List<AutoTriggeredCommunicationDTO> getAutoTriggeredCommunications() {
        return communicationHistoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get auto-triggered communications for specific appeal
     */
    @Override
    public List<AutoTriggeredCommunicationDTO> getAutoTriggeredCommunicationsByAppeal(Long appealId) {
        return communicationHistoryRepository.findByAppealId(appealId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ===== Private Helper Methods =====

    private String buildApprovalEmailContent(Appeal appeal) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "  <style>\n" +
                "    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }\n" +
                "    .container { max-width: 600px; margin: 0 auto; padding: 20px; }\n" +
                "    .header { background-color: #28a745; color: white; padding: 20px; border-radius: 5px 5px 0 0; }\n" +
                "    .content { border: 1px solid #ddd; padding: 20px; }\n" +
                "    .footer { background-color: #f8f9fa; padding: 10px; font-size: 12px; color: #666; }\n" +
                "    .amount { color: #28a745; font-weight: bold; font-size: 18px; }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"container\">\n" +
                "    <div class=\"header\">\n" +
                "      <h2>✓ Approval Confirmed</h2>\n" +
                "    </div>\n" +
                "    <div class=\"content\">\n" +
                "      <p>Dear Donor,</p>\n" +
                "      <p>We are delighted to inform you that your appeal <strong>\"" + appeal.getTitle() + "\"</strong> has been officially <strong>APPROVED</strong>.</p>\n" +
                "      <div style=\"background-color: #e8f5e9; padding: 15px; border-radius: 5px; margin: 20px 0;\">\n" +
                "        <p><strong>Approval Details:</strong></p>\n" +
                "        <p>Appeal ID: " + appeal.getId() + "</p>\n" +
                "        <p>Approved Amount: <span class=\"amount\">₹" + appeal.getApprovedAmount().toPlainString() + "</span></p>\n" +
                "        <p>Approval Date: " + LocalDateTime.now().toString() + "</p>\n" +
                "      </div>\n" +
                "      <p><strong>What's Next?</strong></p>\n" +
                "      <ul>\n" +
                "        <li>Implementation will commence shortly</li>\n" +
                "        <li>We will keep you updated on progress with regular impact reports</li>\n" +
                "        <li>Your contribution will create meaningful change</li>\n" +
                "      </ul>\n" +
                "      <p>Thank you for your generous support and trust in our mission!</p>\n" +
                "      <p>Best regards,<br/>ITC × Anoopam Mission Team</p>\n" +
                "    </div>\n" +
                "    <div class=\"footer\">\n" +
                "      <p>This is an automated notification. Please do not reply to this email.</p>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>";
    }

    private String buildApprovalWhatsAppMessage(Appeal appeal) {
        return "🎉 *Great News!* 🎉\n\n" +
                "Your appeal *\"" + appeal.getTitle() + "\"* has been *APPROVED*!\n\n" +
                "✓ Approved Amount: ₹" + appeal.getApprovedAmount().toPlainString() + "\n" +
                "✓ Appeal ID: " + appeal.getId() + "\n\n" +
                "We are excited to begin implementation and will keep you updated with regular impact reports.\n\n" +
                "Thank you for your generous support!\n\n" +
                "ITC × Anoopam Mission Team";
    }

    private String buildRejectionEmailContent(Appeal appeal, String rejectionReason) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "  <style>\n" +
                "    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }\n" +
                "    .container { max-width: 600px; margin: 0 auto; padding: 20px; }\n" +
                "    .header { background-color: #dc3545; color: white; padding: 20px; border-radius: 5px 5px 0 0; }\n" +
                "    .content { border: 1px solid #ddd; padding: 20px; }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"container\">\n" +
                "    <div class=\"header\">\n" +
                "      <h2>Appeal Status Update</h2>\n" +
                "    </div>\n" +
                "    <div class=\"content\">\n" +
                "      <p>Dear Donor,</p>\n" +
                "      <p>Thank you for submitting the appeal <strong>\"" + appeal.getTitle() + "\"</strong>.</p>\n" +
                "      <p>After careful review, we regret to inform you that your appeal has been <strong>rejected</strong>.</p>\n" +
                "      <p><strong>Reason for Rejection:</strong></p>\n" +
                "      <p style=\"background-color: #f8d7da; padding: 15px; border-radius: 5px;\">" + rejectionReason + "</p>\n" +
                "      <p>We appreciate your understanding. If you have any questions or wish to discuss further, please feel free to reach out to us.</p>\n" +
                "      <p>Best regards,<br/>ITC × Anoopam Mission Team</p>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>";
    }

    private void sendEmailNotifications(Appeal appeal, List<Donor> donors, String subject,
                                        String content, CommunicationTrigger trigger, Long approverUserId) {
        try {
            int sentCount = 0;

            for (Donor donor : donors) {
                try {
                    if (donor.getEmail() != null && !donor.getEmail().isEmpty()) {
                        SimpleMailMessage message = new SimpleMailMessage();
                        message.setTo(donor.getEmail());
                        message.setSubject(subject);
                        message.setText(content);
                        // message.setFrom(fromEmail); // Configure in application.properties

                        javaMailSender.send(message);
                        sentCount++;

                        log.info("Email sent to: {}", donor.getEmail());
                    }
                } catch (Exception e) {
                    log.error("Failed to send email to {}: {}", donor.getEmail(), e.getMessage());
                }
            }

            // Log communication history
            logCommunication(appeal.getId(), trigger, "EMAIL", sentCount, content,
                    CommunicationStatus.SENT, approverUserId);

        } catch (Exception e) {
            log.error("Error sending email notifications: " + e.getMessage(), e);
            logCommunication(appeal.getId(), trigger, "EMAIL", 0, content,
                    CommunicationStatus.FAILED, approverUserId);
        }
    }

    private void sendWhatsAppNotifications(Appeal appeal, List<Donor> donors, String message,
                                           CommunicationTrigger trigger, Long approverUserId) {
        // TODO: Implement WhatsApp integration using Twilio, WhatsApp Business API, etc.
        // For now, just log that it's pending
        int phoneCount = (int) donors.stream()
                .filter(d -> d.getPhoneNumber() != null && !d.getPhoneNumber().isEmpty())
                .count();

        if (phoneCount > 0) {
            log.info("WhatsApp notification queued for {} recipients", phoneCount);
            logCommunication(appeal.getId(), trigger, "WHATSAPP", phoneCount, message,
                    CommunicationStatus.PENDING, approverUserId);
        }
    }

    private void logCommunication(Long appealId, CommunicationTrigger trigger, String channel,
                                  int recipientCount, String content, CommunicationStatus status,
                                  Long approverUserId) {
        try {
            CommunicationHistory history = CommunicationHistory.builder()
                    .appealId(appealId)
                    .triggerType(trigger)
                    .channel(channel)
                    .recipientCount(recipientCount)
                    .content(content)
                    .status(status)
                    .sentByUserId(approverUserId)
                    .sentDate(LocalDateTime.now())
                    .build();

            communicationHistoryRepository.save(history);
            log.info("Communication logged: Appeal {} via {}", appealId, channel);

        } catch (Exception e) {
            log.error("Failed to log communication: " + e.getMessage(), e);
        }
    }

    private AutoTriggeredCommunicationDTO convertToDTO(CommunicationHistory history) {
        return AutoTriggeredCommunicationDTO.builder()
                .id(history.getId())
                .appealId(history.getAppealId())
                .triggerType(history.getTriggerType().toString().toLowerCase())
                .channels(history.getChannel())
                .recipientCount(history.getRecipientCount())
                .status(history.getStatus().toString().toLowerCase())
                .sentDate(history.getSentDate())
                .build();
    }
}