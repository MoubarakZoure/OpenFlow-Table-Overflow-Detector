/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.statcollector;

import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class PortBandwidth {

    private long rx;
    private long tx;
    private String dpid;
    private String port;
    private String updatedTime;

    public PortBandwidth(long rx, long tx, String dpid, String port, String updatedTime) {
        this.rx = rx;
        this.tx = tx;
        this.dpid = dpid;
        this.port = port;
        this.updatedTime = updatedTime;
       
       
    }

    public long getRx() {
        return rx;
    }

    public void setRx(long rx) {
        this.rx = rx;
    }

    public long getTx() {
        return tx;
    }

    public void setTx(long tx) {
        this.tx = tx;
    }

    public String getDpid() {
        return dpid;
    }

    public void setDpid(String dpid) {
        this.dpid = dpid;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (int) (this.rx ^ (this.rx >>> 32));
        hash = 23 * hash + (int) (this.tx ^ (this.tx >>> 32));
        hash = 23 * hash + Objects.hashCode(this.dpid);
        hash = 23 * hash + Objects.hashCode(this.port);
        hash = 23 * hash + Objects.hashCode(this.updatedTime);
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
        final PortBandwidth other = (PortBandwidth) obj;
        if (this.rx != other.rx) {
            return false;
        }
        if (this.tx != other.tx) {
            return false;
        }
        if (this.port != other.port) {
            return false;
        }
        if (!Objects.equals(this.dpid, other.dpid)) {
            return false;
        }
        if (!Objects.equals(this.updatedTime, other.updatedTime)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PortBandwidth{" + "rx=" + rx + ", tx=" + tx + ", dpid=" + dpid + ", port=" + port + ", updatedTime=" + updatedTime + '}';
    }

}
