/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.controller;

import java.util.Objects;

public class Switch {

    private long connectedSince;
    private String inetAddress;
    private String switchDPID;

    public Switch(long connectedSince, String inetAddress, String switchDPID) {
        this.connectedSince = connectedSince;
        this.inetAddress = inetAddress;
        this.switchDPID = switchDPID;
    }

    public long getConnectedSince() {
        return connectedSince;
    }

    public void setConnectedSince(long connectedSince) {
        this.connectedSince = connectedSince;
    }

    public String getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(String inetAddress) {
        this.inetAddress = inetAddress;
    }

    public String getSwitchDPID() {
        return switchDPID;
    }

    public void setSwitchDPID(String switchDPID) {
        this.switchDPID = switchDPID;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (int) (this.connectedSince ^ (this.connectedSince >>> 32));
        hash = 53 * hash + Objects.hashCode(this.inetAddress);
        hash = 53 * hash + Objects.hashCode(this.switchDPID);
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
        final Switch other = (Switch) obj;
        if (this.connectedSince != other.connectedSince) {
            return false;
        }
        if (!Objects.equals(this.inetAddress, other.inetAddress)) {
            return false;
        }
        return Objects.equals(this.switchDPID, other.switchDPID);
    }

    @Override
    public String toString() {
        return "Switch{" + "connectedSince=" + connectedSince + ", inetAddress=" + inetAddress + ", switchDPID=" + switchDPID + '}';
    }

   

}
