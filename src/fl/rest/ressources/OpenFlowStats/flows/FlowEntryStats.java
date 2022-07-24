/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.flows;

import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class FlowEntryStats {

    private long byteCount;
    private String cookie;
    private long durationNSeconds;
    private long durationSeconds;
    private int flags;
    private int hardTimeoutSec;
    private int idleTimeoutSec;
    private FlowInstructiondSets instructions;
    private Match match;
    private long packetCount;
    private int priority;
    private String tableId;
    private String version;
    
    private long time_stamp;

    public FlowEntryStats(long packetCount) {
        this.packetCount = packetCount;
    }
    
    

    public FlowEntryStats(long packetCount, long time_stamp) {
        this.packetCount = packetCount;
        this.time_stamp = time_stamp;
    }
    
    
    
    

    public FlowEntryStats() {
    }

    public FlowEntryStats(long byteCount, String cookie, long durationNSeconds, long durationSeconds, int flags, int hardTimeoutSec, int idleTimeoutSec, FlowInstructiondSets instructions, Match match, long packetCount, int priority, String tableId, String version) {

        this.byteCount = byteCount;
        this.cookie = cookie;
        this.durationNSeconds = durationNSeconds;
        this.durationSeconds = durationSeconds;
        this.flags = flags;
        this.hardTimeoutSec = hardTimeoutSec;
        this.idleTimeoutSec = idleTimeoutSec;
        this.instructions = instructions;
        this.match = match;
        this.packetCount = packetCount;
        this.priority = priority;
        this.tableId = tableId;
        this.version = version;
    }

    public long getByteCount() {
        return byteCount;
    }

    public void setByteCount(long byteCount) {
        this.byteCount = byteCount;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
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

    public int getHardTimeoutSec() {
        return hardTimeoutSec;
    }

    public void setHardTimeoutSec(int hardTimeoutSec) {
        this.hardTimeoutSec = hardTimeoutSec;
    }

    public int getIdleTimeoutSec() {
        return idleTimeoutSec;
    }

    public void setIdleTimeoutSec(int idleTimeoutSec) {
        this.idleTimeoutSec = idleTimeoutSec;
    }

    public FlowInstructiondSets getInstructions() {
        return instructions;
    }

    public void setInstructions(FlowInstructiondSets instructions) {
        this.instructions = instructions;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public long getPacketCount() {
        return packetCount;
    }

    public void setPacketCount(long packetCount) {
        this.packetCount = packetCount;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }
    
    

    @Override
    public String toString() {
        return "FlowEntry{" + "byteCount=" + byteCount + ", cookie=" + cookie + ", durationNSeconds=" + durationNSeconds + ", durationSeconds=" + durationSeconds + ", flags=" + flags + ", hardTimeoutSec=" + hardTimeoutSec + ", idleTimeoutSec=" + idleTimeoutSec + ", instructions=" + instructions + ", match=" + match + ", packetCount=" + packetCount + ", priority=" + priority + ", tableId=" + tableId + ", version=" + version + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int) (this.byteCount ^ (this.byteCount >>> 32));
        hash = 79 * hash + Objects.hashCode(this.cookie);
        hash = 79 * hash + (int) (this.durationNSeconds ^ (this.durationNSeconds >>> 32));
        hash = 79 * hash + (int) (this.durationSeconds ^ (this.durationSeconds >>> 32));
        hash = 79 * hash + this.flags;
        hash = 79 * hash + (int) (this.hardTimeoutSec ^ (this.hardTimeoutSec >>> 32));
        hash = 79 * hash + (int) (this.idleTimeoutSec ^ (this.idleTimeoutSec >>> 32));
        hash = 79 * hash + Objects.hashCode(this.instructions);
        hash = 79 * hash + Objects.hashCode(this.match);
        hash = 79 * hash + (int) (this.packetCount ^ (this.packetCount >>> 32));
        hash = 79 * hash + this.priority;
        hash = 79 * hash + Objects.hashCode(this.tableId);
        hash = 79 * hash + Objects.hashCode(this.version);
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
        final FlowEntryStats other = (FlowEntryStats) obj;
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
        if (this.hardTimeoutSec != other.hardTimeoutSec) {
            return false;
        }
        if (this.idleTimeoutSec != other.idleTimeoutSec) {
            return false;
        }
        if (this.packetCount != other.packetCount) {
            return false;
        }
        if (this.priority != other.priority) {
            return false;
        }
        if (this.tableId != other.tableId) {
            return false;
        }
        if (!Objects.equals(this.cookie, other.cookie)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.instructions, other.instructions)) {
            return false;
        }
        if (!Objects.equals(this.match, other.match)) {
            return false;
        }
        return true;
    }

}
