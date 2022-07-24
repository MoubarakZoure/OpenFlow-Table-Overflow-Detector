/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.flows;

/**
 *
 * @author Moubarak
 */
public class SimpleFlowStats {

    private long byteCount;
    private long durationNSeconds;
    private long durationSeconds;
    private int flags;
    private long packetCount;

    public SimpleFlowStats(long byteCount, long durationNSeconds, long durationSeconds, int flags, long packetCount) {
        this.byteCount = byteCount;
        this.durationNSeconds = durationNSeconds;
        this.durationSeconds = durationSeconds;
        this.flags = flags;
        this.packetCount = packetCount;
    }

    public long getByteCount() {
        return byteCount;
    }

    public void setByteCount(long byteCount) {
        this.byteCount = byteCount;
    }

    public long getDurationNSeconds() {
        return durationNSeconds;
    }

    public void setDurationNSeconds(long durationNSeconds) {
        this.durationNSeconds = durationNSeconds;
    }

    public long getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public long getPacketCount() {
        return packetCount;
    }

    public void setPacketCount(long packetCount) {
        this.packetCount = packetCount;
    }

    @Override
    public String toString() {
        return "SimpleFlowStats{" + "byteCount=" + byteCount + ", durationNSeconds=" + durationNSeconds + ", durationSeconds=" + durationSeconds + ", flags=" + flags + ", packetCount=" + packetCount + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (int) (this.byteCount ^ (this.byteCount >>> 32));
        hash = 41 * hash + (int) (this.durationNSeconds ^ (this.durationNSeconds >>> 32));
        hash = 41 * hash + (int) (this.durationSeconds ^ (this.durationSeconds >>> 32));
        hash = 41 * hash + this.flags;
        hash = 41 * hash + (int) (this.packetCount ^ (this.packetCount >>> 32));
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
        final SimpleFlowStats other = (SimpleFlowStats) obj;
        if (this.byteCount != other.byteCount) {
            return false;
        }
        if (this.durationNSeconds != other.durationNSeconds) {
            return false;
        }
        if (this.durationSeconds != other.durationSeconds) {
            return false;
        }
        if (this.flags != other.flags) {
            return false;
        }
        if (this.packetCount != other.packetCount) {
            return false;
        }
        return true;
    }
    
    

}
