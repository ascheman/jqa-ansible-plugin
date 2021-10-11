package net.aschemann.jqassistant.plugin.ansible.impl.scanner;

import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractDirectoryScannerPlugin;
import net.aschemann.ansible.inventory.type.AnsibleInventory;
import net.aschemann.ansible.inventory.util.AnsibleInventoryReader;
import net.aschemann.jqassistant.plugin.ansible.api.model.AnsibleDirectoryDescriptor;

import java.io.File;
import java.io.IOException;

import static net.aschemann.jqassistant.plugin.ansible.api.AnsibleScope.INVENTORY;

public class AnsibleInventoryDirectoryScannerPlugin extends AbstractDirectoryScannerPlugin<AnsibleDirectoryDescriptor> {

    @Override
    protected Scope getRequiredScope() {
        return INVENTORY;
    }

    @Override
    protected AnsibleDirectoryDescriptor getContainerDescriptor(File container, ScannerContext scannerContext) {
        return scannerContext.getStore().create(AnsibleDirectoryDescriptor.class);
    }

    @Override
    protected void enterContainer(File container, AnsibleDirectoryDescriptor ansibleInventoryDescriptor, ScannerContext scannerContext) throws IOException {
        Store store = scannerContext.getStore();
        AnsibleInventory ansibleInventory = AnsibleInventoryReader.read(container.toPath());
        ansibleInventory.getHosts().forEach(ansibleHost -> AbstractAnsibleScannerPlugin.add(ansibleInventoryDescriptor, ansibleHost, store));
        ansibleInventory.getGroups().forEach(ansibleGroup -> AbstractAnsibleScannerPlugin.add(ansibleInventoryDescriptor, ansibleGroup, store));
    }

    @Override
    protected void leaveContainer(File container, AnsibleDirectoryDescriptor containerDescriptor, ScannerContext scannerContext) {
    }

}
