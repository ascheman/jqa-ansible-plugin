package net.aschemann.jqassistant.plugin.ansible.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Variable")
public interface AnsibleVariableDescriptor extends AnsibleDescriptor {
    String getName();
    void setName(final String name);
    String getValue();
    void setValue(final String value);
}
