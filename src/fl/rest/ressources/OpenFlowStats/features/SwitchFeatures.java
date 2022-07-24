/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.features;

import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class SwitchFeatures {

    private int buffers;
    private String version;
    private int tables;
    private String dpid;
    private String capabilities;
    
    
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + this.buffers;
        hash = 11 * hash + Objects.hashCode(this.version);
        hash = 11 * hash + this.tables;
        hash = 11 * hash + Objects.hashCode(this.dpid);
        hash = 11 * hash + Objects.hashCode(this.capabilities);
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
        final SwitchFeatures other = (SwitchFeatures) obj;
        if (this.buffers != other.buffers) {
            return false;
        }
        if (this.tables != other.tables) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.dpid, other.dpid)) {
            return false;
        }
        if (!Objects.equals(this.capabilities, other.capabilities)) {
            return false;
        }
        return true;
    }
    

    public SwitchFeatures(int buffers, String version, int tables, String dpid, String capabilities) {
        this.buffers = buffers;
        this.version = version;
        this.tables = tables;
        this.dpid = dpid;
        this.capabilities = capabilities;
    }

    public int getBuffers() {
        return buffers;
    }

    public void setBuffers(int buffers) {
        this.buffers = buffers;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getTables() {
        return tables;
    }

    public void setTables(int tables) {
        this.tables = tables;
    }

    public String getDpid() {
        return dpid;
    }

    public void setDpid(String dpid) {
        this.dpid = dpid;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    @Override
    public String toString() {
        return "SwitchFeatures{" + "buffers=" + buffers + ", version=" + version + ", tables=" + tables + ", dpid=" + dpid + ", capabilities=" + capabilities + '}';
    }


   
    
    
    

}
