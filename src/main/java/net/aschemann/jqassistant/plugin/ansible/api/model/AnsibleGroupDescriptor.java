package net.aschemann.jqassistant.plugin.ansible.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Group")
public interface AnsibleGroupDescriptor extends AnsibleDescriptor {
    String getName();
    void setName(final String name);

    @Relation("HAS_HOST")
    List<AnsibleHostDescriptor> getHosts();

    @Relation("HAS_GROUP")
    List<AnsibleGroupDescriptor> getGroups();
}
