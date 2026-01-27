package com.itc.demo.mapper;

import com.itc.demo.dto.request.CreateAppealRequest;
import com.itc.demo.dto.request.UpdateAppealRequest;
import com.itc.demo.dto.response.AppealResponseDTO;
import com.itc.demo.dto.response.UserResponseDTO;
import com.itc.demo.entity.Appeal;
import org.springframework.stereotype.Component;

@Component
public class AppealMapper {

    public Appeal toEntity(CreateAppealRequest request) {
        Appeal appeal = new Appeal();
        appeal.setTitle(request.getTitle());
        appeal.setDescription(request.getDescription());
        appeal.setEstimatedAmount(request.getEstimatedAmount());
        appeal.setBeneficiaryCategory(request.getBeneficiaryCategory());
        appeal.setDuration(request.getDuration());
        return appeal;
    }

    public Appeal toEntity(UpdateAppealRequest request, Appeal existingAppeal) {
        if (request.getTitle() != null) {
            existingAppeal.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            existingAppeal.setDescription(request.getDescription());
        }
        if (request.getEstimatedAmount() != null) {
            existingAppeal.setEstimatedAmount(request.getEstimatedAmount());
        }
        if (request.getBeneficiaryCategory() != null) {
            existingAppeal.setBeneficiaryCategory(request.getBeneficiaryCategory());
        }
        if (request.getDuration() != null) {
            existingAppeal.setDuration(request.getDuration());
        }
        if (request.getStatus() != null) {
            existingAppeal.setStatus(request.getStatus());
        }
        return existingAppeal;
    }

    public AppealResponseDTO toDTO(Appeal appeal) {
        AppealResponseDTO dto = new AppealResponseDTO();
        dto.setId(appeal.getId());
        dto.setTitle(appeal.getTitle());
        dto.setDescription(appeal.getDescription());
        dto.setEstimatedAmount(appeal.getEstimatedAmount());
        dto.setApprovedAmount(appeal.getApprovedAmount());
        dto.setBeneficiaryCategory(appeal.getBeneficiaryCategory());
        dto.setDuration(appeal.getDuration());
        dto.setStatus(appeal.getStatus());
        dto.setCreatedAt(appeal.getCreatedAt());
        dto.setUpdatedAt(appeal.getUpdatedAt());

        if (appeal.getCreatedBy() != null) {
            UserResponseDTO userDTO = new UserResponseDTO();
            userDTO.setId(appeal.getCreatedBy().getId());
            userDTO.setName(appeal.getCreatedBy().getName());
            userDTO.setEmail(appeal.getCreatedBy().getEmail());
            userDTO.setRole(appeal.getCreatedBy().getRole());
            dto.setCreatedBy(userDTO);
        }

        return dto;
    }
}