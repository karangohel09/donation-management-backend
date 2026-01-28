package com.itc.demo.service;

import com.itc.demo.entity.Appeal;
import com.itc.demo.dto.AutoTriggeredCommunicationDTO;
import java.util.List;

public interface CommunicationService {

    void notifyDonorsOnApproval(Appeal appeal, Long approverUserId);

    void notifyDonorsOnRejection(Appeal appeal, String rejectionReason);

    List<AutoTriggeredCommunicationDTO> getAutoTriggeredCommunications();

    List<AutoTriggeredCommunicationDTO> getAutoTriggeredCommunicationsByAppeal(Long appealId);
}