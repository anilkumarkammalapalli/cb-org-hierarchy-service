package com.hierarchyhub.controller;

import com.hierarchyhub.dto.OrganizationRequest;
import com.hierarchyhub.exception.InternalServerException;
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
@RequestMapping("/org/hierarchy")
public class OrgController {


    @Autowired
    private OrganizationService service;

    @Autowired
    OrganizationRepository repository;

    @PostMapping
    public Organization addOrganization(@RequestBody OrganizationRequest request) {
        return service.addOrganization(request);
    }

    @GetMapping("/list/{id}")
    public Map<String, Object> fetchHierarchy(@PathVariable("id") String id) throws Exception {
        return service.fetchHierarchy(id);
    }

    @PostMapping("/search")
    public Map<String, Object> searchOrganization(@RequestParam String orgname) {
        return service.getSearchOrganizationHierarchy(orgname);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Map<String, Object>>> getLevel1Organizations() throws InternalServerException {
        return ResponseEntity.ok(service.getLevel1Organizations());
    }

}
