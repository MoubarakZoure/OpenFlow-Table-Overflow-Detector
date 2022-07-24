package machine.learning.svm.training.automatic;

public class StatAggregat {

    private int table_full_cout;

    private MissFlowStats missFlowStats;

    private StatFromTable tableStatAggregat;

    private String trafficType;

    private long windows_start;
    private long windows_end;

    public StatAggregat(int table_full_cout, MissFlowStats missFlowStats, StatFromTable tableStatAggregat, String trafficType) {
        this.table_full_cout = table_full_cout;
        this.missFlowStats = missFlowStats;
        this.tableStatAggregat = tableStatAggregat;
        this.trafficType = trafficType;
    }

    @Override
    public String toString() {
        return "StatAggregat{" + "table_full_cout=" + table_full_cout + ", missFlowStats=" + missFlowStats + ", tableStatAggregat=" + tableStatAggregat + ", trafficType=" + trafficType + ", windows_start=" + windows_start + ", windows_end=" + windows_end + '}';
    }

    
    
    public String getTrafficType() {
        return trafficType;
    }

    public void setTrafficType(String trafficType) {
        this.trafficType = trafficType;
    }

    public StatAggregat(int table_full_cout, MissFlowStats missFlowStats, StatFromTable tableStatAggregat) {
        this.table_full_cout = table_full_cout;
        this.missFlowStats = missFlowStats;
        this.tableStatAggregat = tableStatAggregat;
    }

    public int getTable_full_cout() {
        return table_full_cout;
    }

    public void setTable_full_cout(int table_full_cout) {
        this.table_full_cout = table_full_cout;
    }

    public MissFlowStats getMissFlowStats() {
        return missFlowStats;
    }

    public void setMissFlowStats(MissFlowStats missFlowStats) {
        this.missFlowStats = missFlowStats;
    }

    public StatFromTable getTableStatAggregat() {
        return tableStatAggregat;
    }

    public void setTableStatAggregat(StatFromTable tableStatAggregat) {
        this.tableStatAggregat = tableStatAggregat;
    }

    public long getWindows_start() {
        return windows_start;
    }

    public void setWindows_start(long windows_start) {
        this.windows_start = windows_start;
    }

    public long getWindows_end() {
        return windows_end;
    }

    public void setWindows_end(long windows_end) {
        this.windows_end = windows_end;
    }

}
