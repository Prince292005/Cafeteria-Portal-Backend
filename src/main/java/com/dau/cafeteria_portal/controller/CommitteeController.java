package com.dau.cafeteria_portal.controller;
import com.dau.cafeteria_portal.dto.CommitteeResponseDTO;
import com.dau.cafeteria_portal.dto.MemberDTO;
import com.dau.cafeteria_portal.service.CommitteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/committee")
public class CommitteeController {

    @Autowired
    private CommitteeService service;

    @GetMapping
    public CommitteeResponseDTO getCommittee() {
        return service.getCommittee();
    }


}
