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

    @Query("MATCH (matchedOrg:Organization {id: $orgId}) " +
            "OPTIONAL MATCH pathToRoot = (parentNode)-[:PARENT_OF*]->(matchedOrg) " +
            "WITH matchedOrg, COLLECT(DISTINCT parentNode) AS parentNodes " +

            "CALL apoc.path.subgraphNodes(matchedOrg, { " +
            "   relationshipFilter: 'PARENT_OF', " +
            "   maxLevel: 10, " +
            "   bfs: true " +
            "}) YIELD node " +

            "WITH matchedOrg, parentNodes, COLLECT(DISTINCT node) AS childNodes " +

            "RETURN { " +
            "   parent: matchedOrg {.*, " +
            "       children: [c IN childNodes WHERE (matchedOrg)-[:PARENT_OF]->(c) | c {.*, " +
            "           children: [c2 IN childNodes WHERE (c)-[:PARENT_OF]->(c2) | c2 {.*, " +
            "               children: [c3 IN childNodes WHERE (c2)-[:PARENT_OF]->(c3) | c3 {.*, " +
            "                   children: [c4 IN childNodes WHERE (c3)-[:PARENT_OF]->(c4) | c4 {.*, " +
            "                       children: [c5 IN childNodes WHERE (c4)-[:PARENT_OF]->(c5) | c5 {.*, " +
            "                           children: [c6 IN childNodes WHERE (c5)-[:PARENT_OF]->(c6) | c6 {.*, " +
            "                               children: [c7 IN childNodes WHERE (c6)-[:PARENT_OF]->(c7) | c7 {.*, " +
            "                                   children: [c8 IN childNodes WHERE (c7)-[:PARENT_OF]->(c8) | c8 {.*, " +
            "                                       children: [c9 IN childNodes WHERE (c8)-[:PARENT_OF]->(c9) | c9 {.*, " +
            "                                           children: [c10 IN childNodes WHERE (c9)-[:PARENT_OF]->(c10) | c10 {.*}] " +
            "                                       }] " +
            "                                   }] " +
            "                               }] " +
            "                           }] " +
            "                       }] " +
            "                   }] " +
            "               }] " +
            "           }] " +
            "       }] " +
            "   } " +
            "} AS hierarchy")
    Map<String, Object> findHierarchyByOrgId(@Param("orgId") String orgId);

    @Query("MATCH (matchedOrg:Organization) " +
            "WHERE toLower(matchedOrg.orgname) CONTAINS toLower($searchTerm) " +
            "OPTIONAL MATCH pathToRoot = (parentNode)-[:PARENT_OF*]->(matchedOrg) " +
            "CALL apoc.path.subgraphNodes(matchedOrg, { " +
            "    relationshipFilter: 'PARENT_OF', " +
            "    maxLevel: $maxLevel, " +
            "    bfs: true " +
            "}) YIELD node " +
            "WITH matchedOrg, COLLECT(DISTINCT parentNode) AS parentNodes, " +
            "     COLLECT(DISTINCT node) AS childNodes " +
            "RETURN { " +
            "  org: matchedOrg {.*, " +
            "    parents: [p IN parentNodes | p {.*}], " +
            "    children: [c1 IN childNodes WHERE (matchedOrg)-[:PARENT_OF]->(c1) | c1 {.*, " +
            "      children: [c2 IN childNodes WHERE (c1)-[:PARENT_OF]->(c2) | c2 {.*, " +
            "        children: [c3 IN childNodes WHERE (c2)-[:PARENT_OF]->(c3) | c3 {.*, " +
            "          children: [c4 IN childNodes WHERE (c3)-[:PARENT_OF]->(c4) | c4 {.*, " +
            "            children: [c5 IN childNodes WHERE (c4)-[:PARENT_OF]->(c5) | c5 {.*, " +
            "              children: [c6 IN childNodes WHERE (c5)-[:PARENT_OF]->(c6) | c6 {.*, " +
            "                children: [c7 IN childNodes WHERE (c6)-[:PARENT_OF]->(c7) | c7 {.*, " +
            "                  children: [c8 IN childNodes WHERE (c7)-[:PARENT_OF]->(c8) | c8 {.*, " +
            "                    children: [c9 IN childNodes WHERE (c8)-[:PARENT_OF]->(c9) | c9 {.*, " +
            "                      children: [c10 IN childNodes WHERE (c9)-[:PARENT_OF]->(c10) | c10 {.*}] " +
            "                    }] " +
            "                  }] " +
            "                }] " +
            "              }] " +
            "            }] " +
            "          }] " +
            "        }] " +
            "      }] " +
            "    }] " +
            "  } " +
            "} AS hierarchy")
    Map<String, Object> findOrganizationHierarchy(@Param("searchTerm") String searchTerm, @Param("maxLevel") int maxLevel);

    @Query("MATCH (l1:Organization) " +
            "WHERE NOT (:Organization)-[:PARENT_OF]->(l1) " +
            "OPTIONAL MATCH (l1)-[:PARENT_OF]->(child:Organization) " +
            "WITH l1, COUNT(child) AS childCount " +
            "RETURN COLLECT({mapid: l1.id, orgname: l1.orgname, channel: l1.channel, externalSourceId: l1.externalSourceId, sborgType: l1.orgType, sborgSubType: l1.orgSubType, isTenant: l1.isTenant, childCount: childCount}) AS organizations")
    List<Map<String, Object>> getLevel1OrganizationsWithChildrenCount();

}
