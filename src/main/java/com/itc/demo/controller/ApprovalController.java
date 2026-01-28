package com.itc.demo.controller;

import com.itc.demo.dto.request.ApproveAppealRequest;
import com.itc.demo.dto.request.RejectAppealRequest;
import com.itc.demo.dto.response.AppealResponseDTO;
import com.itc.demo.dto.response.ApprovalResponseDTO;
import com.itc.demo.service.AppealService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approvals")
@CrossOrigin
public class ApprovalController {

    private final AppealService appealService;

    public ApprovalController(AppealService appealService) {
        this.appealService = appealService;
    }

    @GetMapping("/pending")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ApprovalResponseDTO>> getPendingApprovals() {
        return ResponseEntity.ok(appealService.getPendingApprovals());
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('MISSION_ADMIN') or hasRole('FINANCE_ADMIN')")
    public ResponseEntity<AppealResponseDTO> approveAppeal(
            @PathVariable Long id,
            @RequestBody ApproveAppealRequest request) {

        return ResponseEntity.ok(appealService.approveAppeal(id, request));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('MISSION_ADMIN') or hasRole('FINANCE_ADMIN')")
    public ResponseEntity<AppealResponseDTO> rejectAppeal(
            @PathVariable Long id,
            @RequestBody RejectAppealRequest request) {

        if (request.getReason() == null || request.getReason().trim().isEmpty()) {
            throw new RuntimeException("Rejection reason is required");
        }

        return ResponseEntity.ok(appealService.rejectAppeal(id, request.getReason()));
    }

}