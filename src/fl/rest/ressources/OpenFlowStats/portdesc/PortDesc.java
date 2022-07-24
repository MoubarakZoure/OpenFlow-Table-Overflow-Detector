/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.portdesc;

import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class PortDesc {

    private int advertisedFeatures;
    private int config;
    private int currSpeed;
    private int currentFeatures;
    private String hardwareAddress;
    private int maxSpeed;
    private String name;
    private int peerFeatures;
    private String portNumber;
    private int state;
    private int supportedFeatures;

    public PortDesc(int advertisedFeatures, int config, int currSpeed, int currentFeatures, String hardwareAddress, int maxSpeed, String name, int peerFeatures, String portNumber, int state, int supportedFeatures) {
        this.advertisedFeatures = advertisedFeatures;
        this.config = config;
        this.currSpeed = currSpeed;
        this.currentFeatures = currentFeatures;
        this.hardwareAddress = hardwareAddress;
        this.maxSpeed = maxSpeed;
        this.name = name;
        this.peerFeatures = peerFeatures;
        this.portNumber = portNumber;
        this.state = state;
        this.supportedFeatures = supportedFeatures;
    }

    public int getAdvertisedFeatures() {
        return advertisedFeatures;
    }

    public void setAdvertisedFeatures(int advertisedFeatures) {
        this.advertisedFeatures = advertisedFeatures;
    }

    public int getConfig() {
        return config;
    }

    public void setConfig(int config) {
        this.config = config;
    }

    public int getCurrSpeed() {
        return currSpeed;
    }

    public void setCurrSpeed(int currSpeed) {
        this.currSpeed = currSpeed;
    }

    public int getCurrentFeatures() {
        return currentFeatures;
    }

    public void setCurrentFeatures(int currentFeatures) {
        this.currentFeatures = currentFeatures;
    }

    public String getHardwareAddress() {
        return hardwareAddress;
    }

    public void setHardwareAddress(String hardwareAddress) {
        this.hardwareAddress = hardwareAddress;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPeerFeatures() {
        return peerFeatures;
    }

    public void setPeerFeatures(int peerFeatures) {
        this.peerFeatures = peerFeatures;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getSupportedFeatures() {
        return supportedFeatures;
    }

    public void setSupportedFeatures(int supportedFeatures) {
        this.supportedFeatures = supportedFeatures;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.advertisedFeatures;
        hash = 41 * hash + this.config;
        hash = 41 * hash + this.currSpeed;
        hash = 41 * hash + this.currentFeatures;
        hash = 41 * hash + Objects.hashCode(this.hardwareAddress);
        hash = 41 * hash + this.maxSpeed;
        hash = 41 * hash + Objects.hashCode(this.name);
        hash = 41 * hash + this.peerFeatures;
        hash = 41 * hash + Objects.hashCode(this.portNumber);
        hash = 41 * hash + this.state;
        hash = 41 * hash + this.supportedFeatures;
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
        final PortDesc other = (PortDesc) obj;
        if (this.advertisedFeatures != other.advertisedFeatures) {
            return false;
        }
        if (this.config != other.config) {
            return false;
        }
        if (this.currSpeed != other.currSpeed) {
            return false;
        }
        if (this.currentFeatures != other.currentFeatures) {
            return false;
        }
        if (this.maxSpeed != other.maxSpeed) {
            return false;
        }
        if (this.peerFeatures != other.peerFeatures) {
            return false;
        }
        if (!(null == this.portNumber ? other.portNumber != null : !this.portNumber.equals(other.portNumber))) {
        } else {
            return false;
        }
        if (this.state != other.state) {
            return false;
        }
        if (this.supportedFeatures != other.supportedFeatures) {
            return false;
        }
        if (!Objects.equals(this.hardwareAddress, other.hardwareAddress)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PortDesc{" + "advertisedFeatures=" + advertisedFeatures + ", config=" + config + ", currSpeed=" + currSpeed + ", currentFeatures=" + currentFeatures + ", hardwareAddress=" + hardwareAddress + ", maxSpeed=" + maxSpeed + ", name=" + name + ", peerFeatures=" + peerFeatures + ", portNumber=" + portNumber + ", state=" + state + ", supportedFeatures=" + supportedFeatures + '}';
    }
    
    

}
