package net.aschemann.jqassistant.plugin.ansible.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Inventory")
public interface AnsibleInventoryDescriptor extends AnsibleDescriptor {
    @Relation("HAS_HOST")
    List<AnsibleHostDescriptor> getHosts();

    @Relation("HAS_GROUP")
    List<AnsibleGroupDescriptor> getGroups();
}
