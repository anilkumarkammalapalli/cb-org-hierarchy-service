package com.hierarchyhub.controller;

import com.hierarchyhub.dto.OrganizationRequest;
import com.hierarchyhub.exception.InternalServerException;
import com.hierarchyhub.model.ApiResponse;
import com.hierarchyhub.model.Organization;
import com.hierarchyhub.model.OrganizationDTO;
import com.hierarchyhub.repository.OrganizationRepository;
import com.hierarchyhub.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/org/v1/hierarchy")
public class OrgController {


    @Autowired
    private OrganizationService service;

    @Autowired
    OrganizationRepository repository;

    @GetMapping("/list/{id}")
    public ResponseEntity<?> fetchHierarchy(@PathVariable("id") String id) throws Exception {
        ApiResponse response = service.fetchHierarchy(id);
        return new ResponseEntity<>(response, response.getResponseCode());
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchOrganization(@RequestBody Map<String, Object> searchCriteria) {
        ApiResponse response = service.getSearchOrganizationHierarchy(searchCriteria);
        return new ResponseEntity<>(response, response.getResponseCode());
    }

    @GetMapping("/list")
    public ResponseEntity<?> getLevel1Organizations() {
        ApiResponse response = service.getLevel1Organizations();
        return new ResponseEntity<>(response, response.getResponseCode());
    }

}
