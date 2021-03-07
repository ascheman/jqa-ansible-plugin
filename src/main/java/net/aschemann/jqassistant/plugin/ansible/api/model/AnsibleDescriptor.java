package net.aschemann.jqassistant.plugin.ansible.api.model;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;

/**
 * Defines the label which is shared by all nodes representing Ansible structures.
 */
@Label("Ansible")
public interface AnsibleDescriptor extends Descriptor {
}