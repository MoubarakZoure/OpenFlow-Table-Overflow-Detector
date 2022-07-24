/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.portdesc;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class SwitchPortsDesc {

    private PortDesc[] portDesc;
    private String version;

    public PortDesc[] getPortDesc() {
        return portDesc;
    }

    public void setPortDesc(PortDesc[] portDesc) {
        this.portDesc = portDesc;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Arrays.deepHashCode(this.portDesc);
        hash = 73 * hash + Objects.hashCode(this.version);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SwitchPortsDesc other = (SwitchPortsDesc) obj;
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Arrays.deepEquals(this.portDesc, other.portDesc)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SwitchPortsDesc{" + "portDesc=" + portDesc + ", version=" + version + '}';
    }

    public SwitchPortsDesc(PortDesc[] portDesc, String version) {
        this.portDesc = portDesc;
        this.version = version;
    }

}
