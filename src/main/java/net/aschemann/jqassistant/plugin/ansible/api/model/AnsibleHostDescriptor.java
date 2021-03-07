package net.aschemann.jqassistant.plugin.ansible.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Host")
public interface AnsibleHostDescriptor extends AnsibleDescriptor {
    String getName();

    void setName(final String name);

    @Relation("HAS_VARIABLE")
    List<AnsibleVariableDescriptor> getVariables();
}
