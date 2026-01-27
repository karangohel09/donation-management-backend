package com.itc.demo.service.impl;

import com.itc.demo.dto.request.ApproveAppealRequest;
import com.itc.demo.dto.request.CreateAppealRequest;
import com.itc.demo.dto.request.UpdateAppealRequest;
import com.itc.demo.dto.response.AppealResponseDTO;
import com.itc.demo.entity.Appeal;
import com.itc.demo.entity.Document;
import com.itc.demo.entity.User;
import com.itc.demo.enums.AppealStatus;
import com.itc.demo.mapper.AppealMapper;
import com.itc.demo.repository.AppealRepository;
import com.itc.demo.repository.DocumentRepository;
import com.itc.demo.repository.UserRepository;
import com.itc.demo.service.AppealService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppealServiceImpl implements AppealService {

    private final AppealRepository appealRepository;
    private final UserRepository userRepository;
    private final AppealMapper appealMapper;
    private final DocumentRepository documentRepository;
    public AppealServiceImpl(AppealRepository appealRepository,
                             UserRepository userRepository,
                             AppealMapper appealMapper, DocumentRepository documentRepository) {
        this.appealRepository = appealRepository;
        this.userRepository = userRepository;
        this.appealMapper = appealMapper;
        this.documentRepository = documentRepository;
    }

    @Override
    public AppealResponseDTO createAppeal(CreateAppealRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Appeal appeal = appealMapper.toEntity(request);
        appeal.setCreatedBy(user);
        appeal.setStatus(AppealStatus.PENDING);

        Appeal savedAppeal = appealRepository.save(appeal);
        return appealMapper.toDTO(savedAppeal);
    }

    @Override
    public List<AppealResponseDTO> getAllAppeals() {
        return appealRepository.findAll()
                .stream()
                .map(appealMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AppealResponseDTO getAppealById(Long id) {
        Appeal appeal = appealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appeal not found"));
        return appealMapper.toDTO(appeal);
    }

    @Override
    public AppealResponseDTO updateAppeal(Long id, UpdateAppealRequest request) {
        Appeal appeal = appealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appeal not found"));

        if (!appeal.getStatus().equals(AppealStatus.PENDING) &&
                !appeal.getStatus().equals(AppealStatus.SUBMITTED)) {
            throw new RuntimeException("Cannot update appeal with status: " + appeal.getStatus());
        }

        Appeal updatedAppeal = appealMapper.toEntity(request, appeal);
        return appealMapper.toDTO(appealRepository.save(updatedAppeal));
    }

    @Override
    public void deleteAppeal(Long id) {
        Appeal appeal = appealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appeal not found"));

        if (!appeal.getStatus().equals(AppealStatus.PENDING)) {
            throw new RuntimeException("Can only delete draft appeals");
        }

        appealRepository.deleteById(id);
    }

    @Override
    public AppealResponseDTO approveAppeal(Long id, ApproveAppealRequest request) {
        Appeal appeal = appealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appeal not found"));

        appeal.setStatus(AppealStatus.APPROVED);
        appeal.setApprovedAmount(request.getApprovedAmount());

        return appealMapper.toDTO(appealRepository.save(appeal));
    }

    @Override
    public AppealResponseDTO rejectAppeal(Long id, String reason) {
        Appeal appeal = appealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appeal not found"));

        appeal.setStatus(AppealStatus.REJECTED);
        return appealMapper.toDTO(appealRepository.save(appeal));
    }

    @Override
    public List<AppealResponseDTO> getAppealsByStatus(AppealStatus status) {
        return appealRepository.findByStatus(status)
                .stream()
                .map(appealMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppealResponseDTO> getUserAppeals(Long userId) {
        return appealRepository.findByCreatedBy_Id(userId)
                .stream()
                .map(appealMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public void uploadDocument(Long appealId, MultipartFile file, Long userId) throws IOException {
        Appeal appeal = appealRepository.findById(appealId)
                .orElseThrow(() -> new RuntimeException("Appeal not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create upload directory if it doesn't exist
        String uploadDir = "uploads/documents/";
        Files.createDirectories(Paths.get(uploadDir));

        // Generate unique filename
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);

        // Save file to disk
        Files.write(filePath, file.getBytes());

        // Create document record in database
        Document document = new Document();
        document.setFileName(file.getOriginalFilename());
        document.setFilePath(filePath.toString());
        document.setFileSize(file.getSize());
        document.setFileType(file.getContentType());
        document.setAppeal(appeal);
        document.setUploadedBy(user);

        documentRepository.save(document);
    }

    @Override
    public List<Document> getAppealDocuments(Long appealId) {
        return documentRepository.findByAppealId(appealId);
    }

    @Override
    public void deleteDocument(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        try {
            Files.deleteIfExists(Paths.get(document.getFilePath()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage());
        }

        documentRepository.delete(document);
    }
}