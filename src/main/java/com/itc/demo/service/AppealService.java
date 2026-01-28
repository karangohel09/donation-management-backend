package com.itc.demo.service;

import com.itc.demo.dto.request.ApproveAppealRequest;
import com.itc.demo.dto.request.CreateAppealRequest;
import com.itc.demo.dto.request.UpdateAppealRequest;
import com.itc.demo.dto.response.AppealResponseDTO;
import com.itc.demo.dto.response.ApprovalResponseDTO;
import com.itc.demo.entity.Document;
import com.itc.demo.enums.AppealStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AppealService {
    AppealResponseDTO createAppeal(CreateAppealRequest request, Long userId);
    List<AppealResponseDTO> getAllAppeals();
    AppealResponseDTO getAppealById(Long id);
    AppealResponseDTO updateAppeal(Long id, UpdateAppealRequest request);
    void deleteAppeal(Long id);
    AppealResponseDTO approveAppeal(Long id, ApproveAppealRequest request, Long approverUserId);
    AppealResponseDTO rejectAppeal(Long id, String reason, Long approverUserId);
    List<AppealResponseDTO> getAppealsByStatus(AppealStatus status);
    List<AppealResponseDTO> getUserAppeals(Long userId);
    void uploadDocument(Long appealId, MultipartFile file, Long userId) throws IOException;
    List<Document> getAppealDocuments(Long appealId);
    void deleteDocument(Long documentId);
    List<ApprovalResponseDTO> getPendingApprovals();
}