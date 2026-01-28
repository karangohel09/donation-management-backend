package com.itc.demo.repository;

import com.itc.demo.entity.CommunicationHistory;
import com.itc.demo.enums.CommunicationTrigger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunicationHistoryRepository extends JpaRepository<CommunicationHistory, Long> {

    List<CommunicationHistory> findByAppealId(Long appealId);

    List<CommunicationHistory> findByAppealIdAndTriggerType(Long appealId, CommunicationTrigger triggerType);
}