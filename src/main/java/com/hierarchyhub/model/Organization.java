package com.hierarchyhub.model;

import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@NodeEntity
public class Organization {

    @Id
    @Property("mapid")
    private String mapid;

    @Property("orgname")
    private String orgname;

    @Property("orgcode")
    private String orgcode;

    @Property("sborgtype")
    private String sborgtype;

    @Property("sborgsubtype")
    private String sborgsubtype;

    @Relationship(type = "PARENT_OF", direction = Relationship.OUTGOING)
        private List<Organization> children;

    @Properties
    private Map<String, Object> additionalProperties = new HashMap<>();

    public Organization() {
    }

    public String getMapid() {
        return mapid;
    }

    public void setMapid(String mapid) {
        this.mapid = mapid;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getOrgcode() {
        return orgcode;
    }

    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode;
    }

    public String getSborgtype() {
        return sborgtype;
    }

    public void setSborgtype(String sborgtype) {
        this.sborgtype = sborgtype;
    }

    public String getSborgsubtype() {
        return sborgsubtype;
    }

    public void setSborgsubtype(String sborgsubtype) {
        this.sborgsubtype = sborgsubtype;
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
