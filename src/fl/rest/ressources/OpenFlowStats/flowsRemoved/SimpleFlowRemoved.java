/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.flowsRemoved;

import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class SimpleFlowRemoved {

    private String matching;
    private int duration_sec;
    private long duration_nsec;
    private int byteCount;
    private int packetCount;
    private int total_count;

    public SimpleFlowRemoved(String matching, int duration_sec, long duration_nsec, int byteCount, int packetCount, int total_count) {
        this.matching = matching;
        this.duration_sec = duration_sec;
        this.duration_nsec = duration_nsec;
        this.byteCount = byteCount;
        this.packetCount = packetCount;
        this.total_count = total_count;
    }

    public String getMatching() {
        return matching;
    }

    public void setMatching(String matching) {
        this.matching = matching;
    }

    public int getDuration_sec() {
        return duration_sec;
    }

    public void setDuration_sec(int duration_sec) {
        this.duration_sec = duration_sec;
    }

    public long getDuration_nsec() {
        return duration_nsec;
    }

    public void setDuration_nsec(long duration_nsec) {
        this.duration_nsec = duration_nsec;
    }

    public int getByteCount() {
        return byteCount;
    }

    public void setByteCount(int byteCount) {
        this.byteCount = byteCount;
    }

    public int getPacketCount() {
        return packetCount;
    }

    public void setPacketCount(int packetCount) {
        this.packetCount = packetCount;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.matching);
        hash = 59 * hash + this.duration_sec;
        hash = 59 * hash + (int) (this.duration_nsec ^ (this.duration_nsec >>> 32));
        hash = 59 * hash + this.byteCount;
        hash = 59 * hash + this.packetCount;
        hash = 59 * hash + this.total_count;
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
        final SimpleFlowRemoved other = (SimpleFlowRemoved) obj;
        if (this.duration_sec != other.duration_sec) {
            return false;
        }
        if (this.duration_nsec != other.duration_nsec) {
            return false;
        }
        if (this.byteCount != other.byteCount) {
            return false;
        }
        if (this.packetCount != other.packetCount) {
            return false;
        }
        if (this.total_count != other.total_count) {
            return false;
        }
        if (!Objects.equals(this.matching, other.matching)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SimpleFlowRemoved{" + "matching=" + matching + ", duration_sec=" + duration_sec + ", duration_nsec=" + duration_nsec + ", byteCount=" + byteCount + ", packetCount=" + packetCount + ", total_count=" + total_count + '}';
    }
    
    

}
