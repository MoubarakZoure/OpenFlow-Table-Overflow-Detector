package fl.rest.ressources.OpenFlowStats.Counters;

public class CounterTable {

    private String dpid;
    private String tableID;
    private long time_stamp;
    private long total_count;

    public CounterTable(String dpid, String tableID, long time_stamp,
            long total_count) {
        super();
        this.dpid = dpid;
        this.tableID = tableID;
        this.time_stamp = time_stamp;
        this.total_count = total_count;
    }

    public CounterTable() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "CounterTable [dpid=" + dpid + ", tableID=" + tableID
                + ", time_stamp=" + time_stamp + ", total_count=" + total_count
                + "]";
    }

    public String getDpid() {
        return dpid;
    }

    public void setDpid(String dpid) {
        this.dpid = dpid;
    }

    public String getTableID() {
        return tableID;
    }

    public void setTableID(String tableID) {
        this.tableID = tableID;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

    public long getTotal_count() {
        return total_count;
    }

    public void setTotal_count(long total_count) {
        this.total_count = total_count;
    }

}
