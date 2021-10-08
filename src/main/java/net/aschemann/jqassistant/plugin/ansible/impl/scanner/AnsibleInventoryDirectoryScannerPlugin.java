package net.aschemann.jqassistant.plugin.ansible.impl.scanner;

import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractDirectoryScannerPlugin;
import net.aschemann.jqassistant.plugin.ansible.api.model.AnsibleDirectoryDescriptor;

import java.io.File;

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
    protected void enterContainer(File container, AnsibleDirectoryDescriptor containerDescriptor, ScannerContext scannerContext) {
    }

    @Override
    protected void leaveContainer(File container, AnsibleDirectoryDescriptor containerDescriptor, ScannerContext scannerContext) {
    }

}
