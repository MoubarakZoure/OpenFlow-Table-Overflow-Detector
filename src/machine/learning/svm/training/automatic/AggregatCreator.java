package machine.learning.svm.training.automatic;

import ResultSetParser.ResulSetParser;
import fl.rest.ressources.OpenFlowStats.Counters.SimpleCounter;
import fl.rest.ressources.OpenFlowStats.aggregate.Aggregate;
import fl.rest.ressources.OpenFlowStats.flows.FlowEntryStats;
import fl.rest.ressources.OpenFlowStats.tablestats.TableStats;
import ids.statistics.storage.StatsDBInterfacePooled;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AggregatCreator {

    public AggregatCreator() {
    }

    public AggregatCreator(StatsDBInterfacePooled db) {
        this.db = db;
    }

    private StatsDBInterfacePooled db;
    private int WINDOWS_SIZE = 10000;
    private long next_window_start;

    private String switchName = "s1";
    private String tableId = "0";

   public static int tableSize = 5000; // taille de la table

    private String trafficType = "ATTACK";

    
    
    
    

    public StatFromTable computeTableStats(ArrayList<TableStats> tableStatses) {
        if (tableStatses.isEmpty()) {
            return new StatFromTable();
        }

        if (tableStatses.size() == 1) {
            return new StatFromTable();
        }

        int lookUp_min = 0;
        int lookUp_max = 0;
        int match_min = 0;
        int match_max = 0;

        double somme_occupation_average = 0;

        long time_stamp_min = Long.MAX_VALUE;
        long time_stamp_max = -1;

        for (TableStats stat : tableStatses) {

            double current_occupation_average = (double) stat.getActiveCount() / tableSize;
            somme_occupation_average = somme_occupation_average + current_occupation_average;

            long time_stamp = stat.getTime_stamp();

            if (time_stamp < time_stamp_min) {
                time_stamp_min = time_stamp;
                lookUp_min = stat.getLookUpCount();
                match_min = stat.getMatchCount();

            }

            if (time_stamp > time_stamp_max) {
                time_stamp_max = time_stamp;
                lookUp_max = stat.getLookUpCount();
                match_max = stat.getMatchCount();

            }

        }

        double global_occupation_average = (double) somme_occupation_average / tableStatses.size();
        int lookUpCount = lookUp_max - lookUp_min;
        int matchCount = match_max - match_min;

        long delta_t = time_stamp_max - time_stamp_min;

        double lookUpRate = (double) lookUpCount / delta_t;
        double matchRate = (double) matchCount / delta_t;

        lookUpRate = (double) lookUpRate * 1000;
        matchRate = (double) matchRate * 1000;

        return new StatFromTable(lookUpRate, matchRate, global_occupation_average, lookUpCount, matchCount);

    }

    public MissFlowStats computeMissFlowsStats(ArrayList<FlowEntryStats> missFlows) {
        if (missFlows.isEmpty()) {

            return new MissFlowStats(0, 0);
        }

        if (missFlows.size() == 1) {

            return new MissFlowStats(0, 0);

        }

        long packetCount_min = 0;
        long packetCount_max = 0;

        long time_stamp_min = Long.MAX_VALUE;
        long time_stamp_max = -1;
        for (FlowEntryStats entry : missFlows) {
            long time_stamp = entry.getTime_stamp();

            if (time_stamp < time_stamp_min) {
                time_stamp_min = time_stamp;
                packetCount_min = entry.getPacketCount();

            }

            if (time_stamp > time_stamp_max) {
                time_stamp_max = time_stamp;
                packetCount_max = entry.getPacketCount();

            }

        }

        long missFlowsCount = packetCount_max - packetCount_min;
        double missFlowRate = (double) missFlowsCount / (time_stamp_max - time_stamp_min);
        missFlowRate = (double) missFlowRate * 1000; // convertir la vitesse en seconds
        return new MissFlowStats(missFlowsCount, missFlowRate);

    }

    public StatAggregat aggregatCurrentWindows(long current_windows_start, long current_windows_end) {
        ArrayList<TableStats> tableStat = retrieveSwitchTableStats(switchName, tableId, current_windows_start, current_windows_end);
        ArrayList<SimpleCounter> tableFullCounter = retrieveTableFullCounter(switchName, tableId, current_windows_start, current_windows_end);
        ArrayList<FlowEntryStats> missFlows = retrieveSwitchFlowsStats(switchName, current_windows_start, current_windows_end);

        int table_full_cout = tableFullCounter.size();

        MissFlowStats missFlowStats = computeMissFlowsStats(missFlows);

        StatFromTable tableStatAggregat = computeTableStats(tableStat);

        return new StatAggregat(table_full_cout, missFlowStats, tableStatAggregat);

    }

    public void save_aggregate(ArrayList<StatAggregat> records) {
        db.addToTrainingSet(records, trafficType);

    }

    public void createAggregats() {

        ArrayList<StatAggregat> list = new ArrayList<>();
        try {
            ResultSet resultSet = db.retrieveMinAndMax(switchName, tableId);
            resultSet.next();
            next_window_start = resultSet.getLong("time_stamp_min");
            long TIME_STAMP_MAX = resultSet.getLong("time_stamp_max");

            while (next_window_start <= TIME_STAMP_MAX) {

                long current_windows_start = next_window_start;
                long current_windows_end = next_window_start + WINDOWS_SIZE;

                StatAggregat aggregat = aggregatCurrentWindows(current_windows_start, current_windows_end);
                list.add(aggregat);

                next_window_start = current_windows_end;
            }
            save_aggregate(list);

        } catch (SQLException ex) {
            Logger.getLogger(AutomaticTraining.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int getWINDOWS_SIZE() {
        return WINDOWS_SIZE;
    }

    public void setWINDOWS_SIZE(int WINDOWS_SIZE) {
        this.WINDOWS_SIZE = WINDOWS_SIZE;
    }

    public long getNext_window_start() {
        return next_window_start;
    }

    public void setNext_window_start(long next_window_start) {
        this.next_window_start = next_window_start;
    }

    public StatsDBInterfacePooled getDb() {
        return db;
    }

    public void setDb(StatsDBInterfacePooled db) {
        this.db = db;
    }

    public int getWindows() {
        return WINDOWS_SIZE;
    }

    public void setWindows(int windows) {
        this.WINDOWS_SIZE = windows;
    }

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public ArrayList<SimpleCounter> retrieveTableFullCounter(String dpid, String tableId, long time_stamp_min, long time_stamp_max) {

        return toCounter(db.retrieveTableFullCounter(dpid, tableId, time_stamp_min, time_stamp_max));
    }

    public ArrayList<Aggregate> retriveFlowAggregate(String dpid, long time_stamp_min, long time_stamp_max) {

        return toAggregateList(db.retrieveFlowAggregate(dpid, time_stamp_min, time_stamp_max));

    }

    public ArrayList<TableStats> retrieveSwitchTableStats(String dpid, String tableId, long time_stamp_min, long time_stamp_max) {

        return toSwitchTableStatList(db.retrieveSwitchTableStats(dpid, tableId, time_stamp_min, time_stamp_max));

    }

    public ArrayList<FlowEntryStats> retrieveSwitchFlowsStats(String dpid, long time_stamp_min, long time_stamp_max) {

        return toSwitchFlowsStatList(db.retrieveSwitchFlowsStats(dpid, time_stamp_min, time_stamp_max));

    }

    public int getTableSize() {
        return tableSize;
    }

    public void setTableSize(int tableSize) {
        this.tableSize = tableSize;
    }

    public String getTrafficType() {
        return trafficType;
    }

    public void setTrafficType(String trafficType) {
        this.trafficType = trafficType;
    }
    
    
    

    public ArrayList<SimpleCounter> toCounter(ResultSet resultSet) {

        if (resultSet == null) {
            return null;

        }

        ArrayList<SimpleCounter> result = new ArrayList<>();

        try {
            while (resultSet.next()) {

                int total_count = resultSet.getInt("total_count");
                long time_stamp = resultSet.getLong("time_stamp");

                SimpleCounter simpleCounter = new SimpleCounter(total_count, time_stamp);

                result.add(simpleCounter);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResulSetParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        safeClose(resultSet);
        return result;
    }

    public ArrayList<Aggregate> toAggregateList(ResultSet resultSet) {
        if (resultSet == null) {
            return null;

        }

        ArrayList<Aggregate> result = new ArrayList<>();

        try {
            while (resultSet.next()) {

                int flowCount = resultSet.getInt("flowCount");
                long time_stamp = resultSet.getLong("time_stamp");

                Aggregate aggregate = new Aggregate(flowCount, time_stamp);

                result.add(aggregate);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResulSetParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        safeClose(resultSet);
        return result;
    }

    public ArrayList<TableStats> toSwitchTableStatList(ResultSet resultSet) {

        if (resultSet == null) {
            return null;

        }

        ArrayList<TableStats> result = new ArrayList<>();

        try {
            while (resultSet.next()) {

                int activeCount = resultSet.getInt("activeCount");
                int lookUpCount = resultSet.getInt("lookUpCount");
                int matchCount = resultSet.getInt("matchCount");
                long time_stamp = resultSet.getLong("time_stamp");

                TableStats tableStat = new TableStats(activeCount, lookUpCount, matchCount, time_stamp, null);

                result.add(tableStat);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResulSetParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        safeClose(resultSet);
        return result;
    }

    public ArrayList<FlowEntryStats> toSwitchFlowsStatList(ResultSet resultSet) {

        if (resultSet == null) {
            return null;

        }

        ArrayList<FlowEntryStats> result = new ArrayList<>();

        try {
            while (resultSet.next()) {

                long packetCount = resultSet.getInt("packetCount");

                long time_stamp = resultSet.getLong("time_stamp");

                FlowEntryStats flowEntryStats = new FlowEntryStats(packetCount, time_stamp);

                result.add(flowEntryStats);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResulSetParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        safeClose(resultSet);
        return result;
    }

    private void safeClose(Statement st, Connection conn) {
        safeClose(st);
        safeClose(conn);

    }

    private void safeClose(Connection conn) {
        if (conn != null) {
            try {
                db.getMySqlConnectionPool().returnObject(conn);
            } catch (Exception e) {
                //LOG.info("Failed to return the connection to the pool " + e);
            }
        }
    }

    private void safeClose(ResultSet res) {
        if (res != null) {
            try {
                res.close();
            } catch (SQLException e) {
                // LOG.info("Failed to close databse resultset " + e);
            }
        }
    }

    private void safeClose(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                // LOG.info("Failed to close databse statment " + e);
            }
        }
    }

}
