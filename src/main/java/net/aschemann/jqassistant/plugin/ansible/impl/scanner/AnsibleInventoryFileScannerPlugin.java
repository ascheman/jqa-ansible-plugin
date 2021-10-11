package net.aschemann.jqassistant.plugin.ansible.impl.scanner;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import net.aschemann.ansible.inventory.type.AnsibleInventory;
import net.aschemann.ansible.inventory.util.AnsibleInventoryReader;
import net.aschemann.jqassistant.plugin.ansible.api.model.AnsibleDescriptor;
import net.aschemann.jqassistant.plugin.ansible.api.model.AnsibleInventoryDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static net.aschemann.jqassistant.plugin.ansible.api.AnsibleScope.INVENTORY;

@ScannerPlugin.Requires({FileDescriptor.class})
public class AnsibleInventoryFileScannerPlugin extends AbstractScannerPlugin<FileResource, AnsibleDescriptor> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnsibleInventoryFileScannerPlugin.class);

    @Override
    public boolean accepts(FileResource item, String path, Scope scope) {
        String lowercasePath = path.toLowerCase();
        boolean decision = lowercasePath.endsWith("inventory");
        LOGGER.debug("Ansible: Checking '{}' ('{}') for acceptance: {}", path, lowercasePath, decision);
        return decision;
    }

    @Override
    public AnsibleDescriptor scan(FileResource item, String path, Scope scope, Scanner scanner) throws IOException {
        ScannerContext context = scanner.getContext();
        LOGGER.debug("Ansible: Creating new inventory from '{}'", item.getFile().getPath());
        final Store store = context.getStore();
        final FileDescriptor fileDescriptor = context.getCurrentDescriptor();
        final AnsibleInventoryDescriptor ansibleInventoryDescriptor = store.addDescriptorType(fileDescriptor,
                AnsibleInventoryDescriptor.class);
        AnsibleInventory ansibleInventory =
                AnsibleInventoryReader.read(item.getFile().toPath());
        ansibleInventory.getHosts().forEach(ansibleHost -> AbstractAnsibleScannerPlugin.add(ansibleInventoryDescriptor, ansibleHost, store));
        ansibleInventory.getGroups().forEach(ansibleGroup -> AbstractAnsibleScannerPlugin.add(ansibleInventoryDescriptor, ansibleGroup, store));
        return ansibleInventoryDescriptor;
    }

}
