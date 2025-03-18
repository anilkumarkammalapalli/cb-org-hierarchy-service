package com.hierarchyhub.model;

import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@NodeEntity
public class Organization {
        @Id
        private String id;
        private String orgname;
        private String channel;
        private String orgcode;
        private String parentOrgId;
        private String orgType;
        private String orgSubType;
        private Boolean isTenant;
    private String requestedBy;
    private String externalSourceId;

        @Relationship(type = "PARENT_OF", direction = Relationship.OUTGOING)
        private List<Organization> children;

    @Properties
    private Map<String, Object> additionalProperties = new HashMap<>();

    public Organization() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOrgcode() {
        return orgcode;
    }

    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode;
    }

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgSubType() {
        return orgSubType;
    }

    public void setOrgSubType(String orgSubType) {
        this.orgSubType = orgSubType;
    }

    public Boolean getTenant() {
        return isTenant;
    }

    public void setTenant(Boolean tenant) {
        isTenant = tenant;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getExternalSourceId() {
        return externalSourceId;
    }

    public void setExternalSourceId(String externalSourceId) {
        this.externalSourceId = externalSourceId;
    }

    public List<Organization> getChildren() {
        return children;
    }

    public void setChildren(List<Organization> children) {
        this.children = children;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
