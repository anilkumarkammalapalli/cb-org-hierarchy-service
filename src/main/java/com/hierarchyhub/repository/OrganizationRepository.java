package com.hierarchyhub.repository;

import com.hierarchyhub.model.Organization;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends Neo4jRepository<Organization, String> {

    @Query("MATCH (root:Organization {mapid: $orgId}) " +
            "CALL apoc.path.subgraphNodes(root, { " +
            "   relationshipFilter: 'PARENT_OF', " +
            "   bfs: true, " +
            "   maxLevel: $maxLevel " +
            "}) YIELD node " +

            "OPTIONAL MATCH (node)-[:PARENT_OF]->(child) " +
            "WITH node, COLLECT(child.id) AS childIds " +

            "WITH COLLECT({ " +
            "    id: node.id, " +
            "    orgname: node.orgname, " +
            "    properties: apoc.map.clean(properties(node), ['labels'], []), " +
            "    parentOrgId: node.parentmapid, " +
            "    children: childIds " +
            "}) AS nodes " +

            "RETURN apoc.convert.toJson(nodes) AS hierarchy")
    String findHierarchyByOrgId(@Param("orgId") String orgId, @Param("maxLevel") int maxLevel);

    @Query("MATCH (matchedOrg:Organization) " +
            "WHERE toLower(matchedOrg.orgname) CONTAINS toLower($searchTerm) " +
            "CALL apoc.path.subgraphNodes(matchedOrg, { " +
            "    relationshipFilter: 'PARENT_OF', " +
            "    maxLevel: $maxLevel, " +
            "    bfs: true " +
            "}) YIELD node " +
            "OPTIONAL MATCH (node)-[:PARENT_OF]->(child) " +
            "WITH node, COLLECT(child.id) AS childIds " +
            "WITH COLLECT({ " +
            "    id: node.id, " +
            "    orgname: node.orgname, " +
            "    properties: apoc.map.clean(properties(node), ['labels'], []), " +
            "    parentOrgId: node.parentmapid, " +
            "    children: childIds " +
            "}) AS nodes " +
            "RETURN apoc.convert.toJson(nodes) AS hierarchy")
    String findOrganizationHierarchy(@Param("searchTerm") String searchTerm, @Param("maxLevel") int maxLevel);

    @Query("MATCH (l1:Organization) " +
            "WHERE NOT (:Organization)-[:PARENT_OF]->(l1) " +
            "OPTIONAL MATCH (l1)-[:PARENT_OF]->(child:Organization) " +
            "WITH l1, COUNT(child) AS childCount " +
            "RETURN properties(l1) AS organization, childCount")
    List<Map<String, Object>> getLevel1OrganizationsWithChildrenCount();

}
