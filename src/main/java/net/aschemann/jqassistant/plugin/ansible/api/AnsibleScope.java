package net.aschemann.jqassistant.plugin.ansible.api;

import com.buschmais.jqassistant.core.scanner.api.Scope;

public enum AnsibleScope implements Scope {

    INVENTORY;

    @Override
    public String getPrefix() {
        return "ansible";
    }

    @Override
    public String getName() {
        return name();
    }
}
