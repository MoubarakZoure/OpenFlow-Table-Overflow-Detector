/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.aggregate;

import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class Aggregate {

    private long byteCount;
    private int flags;
    private int flowCount;
    private long packetCount;
    private long time_stamp;
    private String version;

    public Aggregate(int flowCount, long time_stamp) {
        this.flowCount = flowCount;
        this.time_stamp = time_stamp;
    }
    
    
    

    public long getByteCount() {
        return byteCount;
    }

    public void setByteCount(long byteCount) {
        this.byteCount = byteCount;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getFlowCount() {
        return flowCount;
    }

    public void setFlowCount(int flowCount) {
        this.flowCount = flowCount;
    }

    public long getPacketCount() {
        return packetCount;
    }

    public void setPacketCount(long packetCount) {
        this.packetCount = packetCount;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    
    
    
    @Override
    public String toString() {
        return "FlowsAggregate{" + "byteCount=" + byteCount + ", flags=" + flags + ", flowCount=" + flowCount + ", packetCount=" + packetCount + ", version=" + version + '}';
    }
    
    
    

    public Aggregate(long byteCount, int flags, int flowCount, long packetCount, String version) {
        this.byteCount = byteCount;
        this.flags = flags;
        this.flowCount = flowCount;
        this.packetCount = packetCount;
        this.version = version;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (int) (this.byteCount ^ (this.byteCount >>> 32));
        hash = 71 * hash + this.flags;
        hash = 71 * hash + this.flowCount;
        hash = 71 * hash + (int) (this.packetCount ^ (this.packetCount >>> 32));
        hash = 71 * hash + Objects.hashCode(this.version);
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
        final Aggregate other = (Aggregate) obj;
        if (this.byteCount != other.byteCount) {
            return false;
        }
        if (this.flags != other.flags) {
            return false;
        }
        if (this.flowCount != other.flowCount) {
            return false;
        }
        if (this.packetCount != other.packetCount) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        return true;
    }
    
    

}
