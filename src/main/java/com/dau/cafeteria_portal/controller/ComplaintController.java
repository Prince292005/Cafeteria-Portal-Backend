package com.dau.cafeteria_portal.controller;

import com.dau.cafeteria_portal.dto.ComplaintDTO;
import com.dau.cafeteria_portal.enums.ComplaintStatus;
import com.dau.cafeteria_portal.service.ComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user/complaints")
@Tag(name = "Complaints", description = "APIs for complaint management")
public class ComplaintController {

    private final ComplaintService complaintService;
    public ComplaintController(ComplaintService complaintService)
    {
        this.complaintService=complaintService;
    }

    @PostMapping
    @Operation(
            summary = "Create Complaint",
            description = "Creates a new complaint for the logged-in user",
            requestBody = @RequestBody(
                    description = "Complaint details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ComplaintDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Complaint created successfully",
                            content = @Content(schema = @Schema(implementation = ComplaintDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
            }
    )
    public ResponseEntity<ComplaintDTO> createComplaint(
            @org.springframework.web.bind.annotation.RequestBody ComplaintDTO complaintDTO,
            Principal principal) {

        String emailId = principal.getName(); // from JWT or session
        Long canteenId=complaintDTO.getCanteenId();
        ComplaintDTO created = complaintService.createComplaint(complaintDTO, emailId,canteenId);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/mycomplaints")
    @Operation(
            summary = "Get My Complaints",
            description = "Fetch all complaints created by the logged-in user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of complaints",
                            content = @Content(schema = @Schema(implementation = ComplaintDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
            }
    )
    public ResponseEntity<List<ComplaintDTO>> getMyComplaints( Principal principal) {
        String emailId = principal.getName();
        List<ComplaintDTO> complaints = complaintService.getMyComplaints(emailId);
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get Complaint Details",
            description = "Fetch complaint details by complaint ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Complaint details",
                            content = @Content(schema = @Schema(implementation = ComplaintDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Complaint not found", content = @Content)
            }
    )
    public ResponseEntity<ComplaintDTO> getComplaintDetails(@PathVariable Long id) {
        return complaintService.getComplaintDetails(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/{id}/attach")
    public ResponseEntity<String> attachFileToComplaint(
            @PathVariable Long id,
            @RequestParam String fileKey,
            Principal principal) {

        complaintService.attachFile(id, fileKey, principal.getName());
        return ResponseEntity.ok("File attached successfully");
    }

}
