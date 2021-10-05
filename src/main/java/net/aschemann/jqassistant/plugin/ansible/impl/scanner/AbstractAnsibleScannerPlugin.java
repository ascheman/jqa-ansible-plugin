package net.aschemann.jqassistant.plugin.ansible.impl.scanner;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.model.DirectoryDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import net.aschemann.ansible.inventory.type.AnsibleGroup;
import net.aschemann.ansible.inventory.type.AnsibleHost;
import net.aschemann.ansible.inventory.type.AnsibleInventory;
import net.aschemann.ansible.inventory.type.AnsibleVariable;
import net.aschemann.ansible.inventory.util.AnsibleInventoryReader;
import net.aschemann.jqassistant.plugin.ansible.api.model.AnsibleDescriptor;
import net.aschemann.jqassistant.plugin.ansible.api.model.AnsibleGroupDescriptor;
import net.aschemann.jqassistant.plugin.ansible.api.model.AnsibleHostDescriptor;
import net.aschemann.jqassistant.plugin.ansible.api.model.AnsibleInventoryDescriptor;
import net.aschemann.jqassistant.plugin.ansible.api.model.AnsibleVariableDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * An Ansible scanner plugin.
 */
// This plugin takes the file descriptor created by the file scanner plugin as input.
public abstract class AbstractAnsibleScannerPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAnsibleScannerPlugin.class);

    protected static void add(final AnsibleInventoryDescriptor ansibleInventoryDescriptor,
                              final AnsibleHost ansibleHost,
                     final Store store) {
        LOGGER.debug("Ansible: Adding new host '{}'", ansibleHost.getName());
        AnsibleHostDescriptor ansibleHostDescriptor = store.create(AnsibleHostDescriptor.class);
        ansibleHostDescriptor.setName(ansibleHost.getName());
        ansibleInventoryDescriptor.getHosts().add(ansibleHostDescriptor);
        ansibleHost.getVariables().forEach(ansibleVariable -> add(ansibleHostDescriptor, ansibleVariable, store));
    }

    protected static void add(final AnsibleHostDescriptor ansibleHostDescriptor, final AnsibleVariable ansibleVariable,
                     final Store store) {
        LOGGER.debug("Ansible: Adding new variable '{}' with value '{}' to host '{}'",
                ansibleVariable.getName(), ansibleVariable.getValue(), ansibleVariable.getName());
        AnsibleVariableDescriptor ansibleVariableDescriptor = store.create(AnsibleVariableDescriptor.class);
        ansibleVariableDescriptor.setName(ansibleVariable.getName());
        if (!(ansibleVariable.getValue() instanceof String)) {
            LOGGER.warn("Variable '{}' of host '{}' is not a String object: '{}'", ansibleVariable.getName(),
                    ansibleHostDescriptor.getName(), ansibleVariable.getValue());
        }
        ansibleVariableDescriptor.setValue(ansibleVariable.getValue().toString());
        ansibleHostDescriptor.getVariables().add(ansibleVariableDescriptor);
    }

    protected static void add(final AnsibleInventoryDescriptor ansibleInventoryDescriptor, final AnsibleGroup ansibleGroup,
                     final Store store) {
        LOGGER.debug("Ansible: Adding new group '{}'", ansibleGroup.getName());
        AnsibleGroupDescriptor ansibleGroupDescriptor = store.create(AnsibleGroupDescriptor.class);
        ansibleGroupDescriptor.setName(ansibleGroup.getName());
        ansibleInventoryDescriptor.getGroups().add(ansibleGroupDescriptor);
        ansibleGroup.getHosts().forEach(ansibleHost -> add(ansibleGroupDescriptor, ansibleHost, store));
        ansibleGroup.getSubgroups().forEach(subGroup -> add(ansibleGroupDescriptor, subGroup, store));
    }

    protected static void add(final AnsibleGroupDescriptor ansibleGroupDescriptor, final AnsibleHost ansibleHost,
                     final Store store) {
        LOGGER.debug("Ansible: Adding new host '{}' to group '{}'", ansibleHost.getName(),
                ansibleGroupDescriptor.getName());
        AnsibleHostDescriptor ansibleHostDescriptor = store.create(AnsibleHostDescriptor.class);
        ansibleHostDescriptor.setName(ansibleHost.getName());
        ansibleGroupDescriptor.getHosts().add(ansibleHostDescriptor);
        ansibleHost.getVariables().forEach(ansibleVariable -> add(ansibleHostDescriptor, ansibleVariable, store));
    }

    protected static void add(final AnsibleGroupDescriptor ansibleGroupDescriptor, final AnsibleGroup subGroup,
                     final Store store) {
        LOGGER.debug("Ansible: Adding new (sub) group '{}' to group '{}'", subGroup.getName(),
                ansibleGroupDescriptor.getName());
        AnsibleGroupDescriptor subGroupDescriptor = store.create(AnsibleGroupDescriptor.class);
        subGroupDescriptor.setName(subGroup.getName());
        subGroupDescriptor.getGroups().add(subGroupDescriptor);
        subGroup.getSubgroups().forEach(ansibleGroup -> add(subGroupDescriptor, ansibleGroup, store));
    }

}