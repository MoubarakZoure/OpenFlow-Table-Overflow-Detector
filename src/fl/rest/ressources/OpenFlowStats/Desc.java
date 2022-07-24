/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats;

import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class Desc {

    private String datapathDescription;
    private String hardwareDescription;
    private String manufacturerDescription;
    private String serialNumber;
    private String softwareDescription;
    private String version;
    
    
    

    @Override
    public String toString() {
        return "desc{" + "datapathDescription=" + datapathDescription + ", hardwareDescription=" + hardwareDescription + ", manufacturerDescription=" + manufacturerDescription + ", serialNumber=" + serialNumber + ", softwareDescription=" + softwareDescription + ", version=" + version + '}';
    }
    
    
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.datapathDescription);
        hash = 59 * hash + Objects.hashCode(this.hardwareDescription);
        hash = 59 * hash + Objects.hashCode(this.manufacturerDescription);
        hash = 59 * hash + Objects.hashCode(this.serialNumber);
        hash = 59 * hash + Objects.hashCode(this.softwareDescription);
        hash = 59 * hash + Objects.hashCode(this.version);
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
        final Desc other = (Desc) obj;
        if (!Objects.equals(this.datapathDescription, other.datapathDescription)) {
            return false;
        }
        if (!Objects.equals(this.hardwareDescription, other.hardwareDescription)) {
            return false;
        }
        if (!Objects.equals(this.manufacturerDescription, other.manufacturerDescription)) {
            return false;
        }
        if (!Objects.equals(this.serialNumber, other.serialNumber)) {
            return false;
        }
        if (!Objects.equals(this.softwareDescription, other.softwareDescription)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        return true;
    }
    
    
    

    public Desc(String datapathDescription, String hardwareDescription, String manufacturerDescription, String serialNumber, String softwareDescription, String version) {
        this.datapathDescription = datapathDescription;
        this.hardwareDescription = hardwareDescription;
        this.manufacturerDescription = manufacturerDescription;
        this.serialNumber = serialNumber;
        this.softwareDescription = softwareDescription;
        this.version = version;
    }
    
    

    public String getDatapathDescription() {
        return datapathDescription;
    }

    public void setDatapathDescription(String datapathDescription) {
        this.datapathDescription = datapathDescription;
    }

    public String getHardwareDescription() {
        return hardwareDescription;
    }

    public void setHardwareDescription(String hardwareDescription) {
        this.hardwareDescription = hardwareDescription;
    }

    public String getManufacturerDescription() {
        return manufacturerDescription;
    }

    public void setManufacturerDescription(String manufacturerDescription) {
        this.manufacturerDescription = manufacturerDescription;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSoftwareDescription() {
        return softwareDescription;
    }

    public void setSoftwareDescription(String softwareDescription) {
        this.softwareDescription = softwareDescription;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    
    

}
