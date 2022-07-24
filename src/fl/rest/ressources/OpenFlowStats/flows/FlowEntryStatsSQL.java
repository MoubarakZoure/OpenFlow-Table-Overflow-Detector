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
public class FlowEntryStatsSQL  extends FlowEntryStats{
    
     private String dpid;
    private long time_stamp;

    public FlowEntryStatsSQL(String dpid, long time_stamp, long packetCount) {
        super(packetCount);
        this.dpid = dpid;
        this.time_stamp = time_stamp;
    }
    
    
    

    public FlowEntryStatsSQL(String dpid, long time_stamp) {
        this.dpid = dpid;
        this.time_stamp = time_stamp;
    }

    public FlowEntryStatsSQL(String dpid, long time_stamp, long byteCount, String cookie, long durationNSeconds, long durationSeconds, int flags, int hardTimeoutSec, int idleTimeoutSec, FlowInstructiondSets instructions, Match match, long packetCount, int priority, String tableId, String version) {
        super(byteCount, cookie, durationNSeconds, durationSeconds, flags, hardTimeoutSec, idleTimeoutSec, instructions, match, packetCount, priority, tableId, version);
        this.dpid = dpid;
        this.time_stamp = time_stamp;
    }
    
    
     public FlowEntryStatsSQL(String dpid, long time_stamp,FlowEntryStats flowEntryStats) {
        super(flowEntryStats.getByteCount(),flowEntryStats.getCookie(),flowEntryStats.getDurationNSeconds(),flowEntryStats.getDurationSeconds(), 
                flowEntryStats.getFlags(), flowEntryStats.getHardTimeoutSec(),flowEntryStats.getIdleTimeoutSec(), 
                flowEntryStats.getInstructions(),flowEntryStats.getMatch(),flowEntryStats.getPacketCount(),flowEntryStats.getPriority(),
                flowEntryStats.getTableId(),flowEntryStats.getVersion());
        
        
        this.dpid = dpid;
        this.time_stamp = time_stamp;
    }

    public String getDpid() {
        return dpid;
    }

    public void setDpid(String dpid) {
        this.dpid = dpid;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

    @Override
    public String toString() {
        return "FlowEntryStatsSQL{" + "dpid=" + dpid + ", time_stamp=" + time_stamp + '}';
    }
    
    
    
    
    
    
}
