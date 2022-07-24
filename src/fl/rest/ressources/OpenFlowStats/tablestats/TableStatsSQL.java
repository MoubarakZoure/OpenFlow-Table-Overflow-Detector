/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.tablestats;

/**
 *
 * @author Moubarak
 */
public class TableStatsSQL extends TableStats {

    private String dpid;
    private long time_stamp;
    private String version;

    public TableStatsSQL(int activeCount, int lookUpCount, int matchCount, String tableID) {
        super(activeCount, lookUpCount, matchCount, tableID);
    }

    public TableStatsSQL(String dpid, long time_stamp, String version, int activeCount, int lookUpCount, int matchCount, String tableID) {
        super(activeCount, lookUpCount, matchCount, tableID);
        this.dpid = dpid;
        this.time_stamp = time_stamp;
        this.version = version;
    }

    public TableStatsSQL(String dpid, long time_stamp, String version, TableStats tableStats) {
        super(tableStats.getActiveCount(), tableStats.getLookUpCount(), tableStats.getMatchCount(), tableStats.getTableID());
        this.dpid = dpid;
        this.time_stamp = time_stamp;
        this.version = version;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "TableStatsSQL{" + "dpid=" + dpid + ", time_stamp=" + time_stamp + ", version=" + version + '}';
    }

}
