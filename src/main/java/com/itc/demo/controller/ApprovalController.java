package com.itc.demo.controller;

import com.itc.demo.dto.request.ApproveAppealRequest;
import com.itc.demo.dto.request.RejectAppealRequest;
import com.itc.demo.dto.response.AppealResponseDTO;
import com.itc.demo.dto.response.ApprovalResponseDTO;
import com.itc.demo.service.AppealService;
import com.itc.demo.service.CommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
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
            @RequestBody ApproveAppealRequest request,
            Authentication authentication) {

        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(appealService.approveAppeal(id, request, userId));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('MISSION_ADMIN') or hasRole('FINANCE_ADMIN')")
    public ResponseEntity<AppealResponseDTO> rejectAppeal(
            @PathVariable Long id,
            @RequestBody RejectAppealRequest request,
            Authentication authentication) {

        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(appealService.rejectAppeal(id, request.getReason(), userId));
    }

    @Autowired
    private CommunicationService communicationService;

    @GetMapping("/communications/auto-triggered")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('MISSION_ADMIN')")
    public ResponseEntity<?> getAutoTriggeredCommunications() {
        return ResponseEntity.ok(communicationService.getAutoTriggeredCommunications());
    }

                    @GetMapping("/communications/auto-triggered/appeal/{appealId}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('MISSION_ADMIN')")
    public ResponseEntity<?> getAutoTriggeredByAppeal(@PathVariable Long appealId) {
        return ResponseEntity.ok(communicationService.getAutoTriggeredCommunicationsByAppeal(appealId));
    }


    private Long getUserIdFromAuth(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {

            return 1L;
        }
        return 1L;
    }

}