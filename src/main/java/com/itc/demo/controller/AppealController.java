package com.itc.demo.controller;

import com.itc.demo.dto.request.ApproveAppealRequest;
import com.itc.demo.dto.request.CreateAppealRequest;
import com.itc.demo.dto.request.UpdateAppealRequest;
import com.itc.demo.dto.response.AppealResponseDTO;
import com.itc.demo.entity.Document;
import com.itc.demo.service.AppealService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/appeals")
@CrossOrigin
public class AppealController {

    private final AppealService appealService;

    public AppealController(AppealService appealService) {
        this.appealService = appealService;
    }

    @GetMapping
    public ResponseEntity<List<AppealResponseDTO>> getAllAppeals() {
        return ResponseEntity.ok(appealService.getAllAppeals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppealResponseDTO> getAppealById(@PathVariable Long id) {
        return ResponseEntity.ok(appealService.getAppealById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ITC_ADMIN') or hasRole('MISSION_ADMIN')")
    public ResponseEntity<AppealResponseDTO> createAppeal(
            @RequestBody CreateAppealRequest request,
            Authentication authentication) {

        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appealService.createAppeal(request, userId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ITC_ADMIN')")
    public ResponseEntity<AppealResponseDTO> updateAppeal(
            @PathVariable Long id,
            @RequestBody UpdateAppealRequest request) {
        return ResponseEntity.ok(appealService.updateAppeal(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ITC_ADMIN')")
    public ResponseEntity<Void> deleteAppeal(@PathVariable Long id) {
        appealService.deleteAppeal(id);
        return ResponseEntity.noContent().build();
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
            @RequestParam String reason) {
        return ResponseEntity.ok(appealService.rejectAppeal(id, reason));
    }

    private Long getUserIdFromAuth(Authentication authentication) {
        // You'll need to extract the user ID from the JWT token or authentication object
        // For now, returning a placeholder - adjust based on your auth implementation
        return 1L;
    }
    @PostMapping("/{id}/documents")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ITC_ADMIN') or hasRole('MISSION_ADMIN')")
    public ResponseEntity<Void> uploadDocument(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws IOException {

        Long userId = getUserIdFromAuth(authentication);
        appealService.uploadDocument(id, file, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<List<Document>> getAppealDocuments(@PathVariable Long id) {
        return ResponseEntity.ok(appealService.getAppealDocuments(id));
    }

    @DeleteMapping("/documents/{documentId}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ITC_ADMIN')")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long documentId) {
        appealService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }
}