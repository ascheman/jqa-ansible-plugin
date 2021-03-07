package net.aschemann.jqasssistant.plugin.ansible.impl.scanner;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.plugin.common.test.AbstractPluginIT;
import net.aschemann.jqassistant.plugin.ansible.api.model.AnsibleInventoryDescriptor;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AnsibleScannerPluginTest extends AbstractPluginIT {

    public static final String VAGRANT_INVENTORY = "/inventories/vagrant-inventory";

    @Test
    public void scanInventoryFile() {
        File testFile = new File(getClassesDirectory(AnsibleScannerPluginTest.class), VAGRANT_INVENTORY);

        store.beginTransaction();
        // Scan the Ansible Inventory file and assert that the returned descriptor is a AnsibleDescriptor
        Descriptor descriptor = getScanner().scan(testFile, VAGRANT_INVENTORY, DefaultScope.NONE);
        assertThat(descriptor).isInstanceOf(AnsibleInventoryDescriptor.class);

        AnsibleInventoryDescriptor inventory = (AnsibleInventoryDescriptor) descriptor;
        assertThat(inventory.getHosts().size()).isEqualTo(0);
        assertThat(inventory.getGroups().stream().filter(
                ansibleGroupDescriptor -> ansibleGroupDescriptor.getName().equals("lamp_www")
        ).findFirst().get().getHosts().size()).isEqualTo(2);
        assertThat(inventory.getGroups().stream().filter(
                ansibleGroupDescriptor -> ansibleGroupDescriptor.getName().equals("lamp_memcached")
        ).findFirst().get().getHosts().size()).isEqualTo(1);
        assertThat(inventory.getGroups().stream().filter(
                ansibleGroupDescriptor -> ansibleGroupDescriptor.getName().equals("lamp_varnish"))
                .findFirst().get().getHosts().stream().filter(
                        ansibleHostDescriptor -> ansibleHostDescriptor.getName().equals("192.168.2.2")
                ).findFirst().get().getName()).isEqualTo("192.168.2.2");
        assertThat(inventory.getGroups().stream().filter(
                ansibleGroupDescriptor -> ansibleGroupDescriptor.getName().equals("lamp_db"))
                .findFirst().get().getHosts().stream().filter(
                        ansibleHostDescriptor -> ansibleHostDescriptor.getName().equals("192.168.2.6")
                ).findFirst().get().getVariables().stream().filter(
                        ansibleVariableDescriptor -> ansibleVariableDescriptor.getName().equals("mysql_replication_role")
                        ).findFirst().get().getValue()).isEqualTo("slave");

        store.commitTransaction();
    }
}