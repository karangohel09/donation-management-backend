package com.itc.demo.mapper;

import com.itc.demo.dto.response.DocumentResponseDTO;
import com.itc.demo.entity.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {
    public DocumentResponseDTO toDTO(Document document) {
        DocumentResponseDTO dto = new DocumentResponseDTO();
        dto.setId(document.getId());
        dto.setFileName(document.getFileName());
        dto.setFilePath(document.getFilePath());
        dto.setFileSize(document.getFileSize());
        dto.setFileType(document.getFileType());
        dto.setUploadedAt(document.getUploadedAt());
        return dto;
    }
}