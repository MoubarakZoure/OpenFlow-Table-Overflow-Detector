/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.aggregate;

/**
 *
 * @author Moubarak
 */
public class AggregateSQL extends Aggregate {

    private String dpid;

    private long time_stamp;

    public AggregateSQL(String dpid, long time_stamp, long byteCount, int flags, int flowCount, long packetCount, String version) {
        super(byteCount, flags, flowCount, packetCount, version);
        this.dpid = dpid;

        this.time_stamp = time_stamp;
    }

    public AggregateSQL(String dpid, long time_stamp, Aggregate aggregate) {

        super(aggregate.getByteCount(), aggregate.getFlags(), aggregate.getFlowCount(), aggregate.getPacketCount(), aggregate.getVersion());
        this.dpid = dpid;

        this.time_stamp = time_stamp;
    }

    public AggregateSQL(long byteCount, int flags, int flowCount, long packetCount, String version) {
        super(byteCount, flags, flowCount, packetCount, version);
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
        return "AggregateSQL{" + "dpid=" + dpid + ", time_stamp=" + time_stamp + '}';
    }

}
