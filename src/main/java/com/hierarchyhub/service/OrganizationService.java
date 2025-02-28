package com.hierarchyhub.service;

import com.hierarchyhub.config.ApplicationConfiguration;
import com.hierarchyhub.dto.OrganizationRequest;
import com.hierarchyhub.exception.InternalServerException;
import com.hierarchyhub.exception.ResourceNotFoundException;
import com.hierarchyhub.model.Organization;
import com.hierarchyhub.model.OrganizationDTO;
import com.hierarchyhub.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository repository;

    @Autowired
    ApplicationConfiguration configuration;

    public Organization addOrganization(OrganizationRequest request) {

        Organization org = new Organization();
        org.setId(generateMapid());
        org.setOrgname(request.getOrgName());
        org.setChannel(request.getChannel());
        org.setTenant(request.isTenant());
        org.setOrgType(request.getOrganisationType());
        org.setOrgSubType(request.getOrganisationSubType());
        org.setRequestedBy(request.getRequestedBy());
        org.setExternalSourceId(request.getExternalSourceId() != null ? request.getExternalSourceId() : null);

        if (request.getAdditionalProperties() != null) {
            org.setAdditionalProperties(request.getAdditionalProperties());
        }

        if (request.getParentId() != null) {
            org.setParentOrgId(request.getParentId());
        }


        Organization savedOrg = repository.save(org);

        if (!request.isParent() && request.getParentId() != null) {
            Organization parent = repository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent Organization not found"));

            if (parent.getChildren() == null) {
                parent.setChildren(new ArrayList<>());
            }

            parent.getChildren().add(savedOrg);

            repository.save(parent);
        }

        return savedOrg;
    }

    public Map<String,Object> fetchHierarchy(String id) {
        return repository.findHierarchyByOrgId(id);
    }

    public static String generateMapid() {
        long timestamp = System.currentTimeMillis();

        Random rand = new Random();
        long randomPart = rand.nextInt(1000000);

        long mapidLong = timestamp * 1000000 + randomPart;

        DecimalFormat df = new DecimalFormat("0000000000000000000");
        return df.format(mapidLong);
    }


    public Object renameKeysRecursively(Object input) {
        if (input instanceof Map) {
            Map<String, Object> map = new HashMap<>();
            Map<?, ?> originalMap = (Map<?, ?>) input;
            for (Map.Entry<?, ?> entry : originalMap.entrySet()) {
                String key = entry.getKey().toString();
                Object value = entry.getValue();
                if (key.equalsIgnoreCase("parent_of")) {
                    key = "parent";
                }
                map.put(key, renameKeysRecursively(value));
            }
            return map;
        } else if (input instanceof List) {
            List<Object> list = new ArrayList<>();
            for (Object item : (List<?>) input) {
                list.add(renameKeysRecursively(item));
            }
            return list;
        } else {
            return input;
        }
    }

    public Object nestAdditionalPropertiesRecursive(Object input) {
        if (input instanceof Map) {
            Map<String, Object> result = new HashMap<>();
            Map<String, Object> additionalNested = new HashMap<>();
            Map<?, ?> originalMap = (Map<?, ?>) input;

            for (Map.Entry<?, ?> entry : originalMap.entrySet()) {
                String key = entry.getKey().toString();
                Object value = nestAdditionalPropertiesRecursive(entry.getValue());

                if (key.startsWith("additionalProperties.")) {
                    String nestedKey = key.substring("additionalProperties.".length());
                    additionalNested.put(nestedKey, value);
                } else {
                    result.put(key, value);
                }
            }
            if (!additionalNested.isEmpty()) {
                result.put("additionalProperties", additionalNested);
            }
            return result;
        } else if (input instanceof List) {
            List<Object> list = new ArrayList<>();
            for (Object item : (List<?>) input) {
                list.add(nestAdditionalPropertiesRecursive(item));
            }
            return list;
        } else {
            return input;
        }
    }

    public Map<String, Object> getSearchOrganizationHierarchy(String searchTerm) {
         return repository.findOrganizationHierarchy(searchTerm, configuration.getMaxLevel());
    }

    public List<Map<String, Object>> getLevel1Organizations() {
        try {
            List<Map<String, Object>> organizations = repository.getLevel1OrganizationsWithChildrenCount();

            if (organizations == null || organizations.isEmpty()) {
                throw new ResourceNotFoundException("No Level 1 organizations found");
            }

            return organizations;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerException("An unexpected error occurred while fetching Level 1 organizations");
        }
    }
}
