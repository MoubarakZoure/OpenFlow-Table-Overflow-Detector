/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ids.detection;

import ResultSetParser.ResulSetParser;
import fl.rest.ressources.OpenFlowStats.Counters.SimpleCounter;
import fl.rest.ressources.OpenFlowStats.aggregate.Aggregate;
import fl.rest.ressources.OpenFlowStats.flows.FlowEntryStats;
import fl.rest.ressources.OpenFlowStats.tablestats.TableStats;
import gui.graphics.Indicators;
import gui.graphics.SwitchStateVizualisation;
import ids.statistics.storage.StatsDBInterfacePooled;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import machine.learning.svm.prediction.SVMPredictionResult;
import machine.learning.svm.prediction.SVMPredictor;
import machine.learning.svm.training.automatic.AggregatCreator;
import machine.learning.svm.training.automatic.MissFlowStats;
import machine.learning.svm.training.automatic.StatAggregat;
import machine.learning.svm.training.automatic.StatFromTable;

/**
 *
 * @author Moubarak
 */
public class SwitchMonitorAgent {

    public String switchName = "s1";

    private String tableId = "0";

    private int tableCapacity = 8000; // à modifier

    private SVMPredictor classifier;

    private int monitoringFrequence = 10000;

    Runnable monitorAgent;

    ArrayList<String> attributList = new ArrayList<>();

    StatsDBInterfacePooled dbInferface;// a revoir

    private long nextStartingWindow = 0;

    private boolean save_alert = true;
    private boolean send_mail = true;
    private String notification_mal;
    private JTable notification_table;
    private JPanel monitoringScrean;
    private Map<Integer, Alert> alertsMap = new HashMap<>();
    private SwitchStateVizualisation viz;

    int alert_count = 0;
    //int alert_id = 0;

    public SwitchMonitorAgent(Runnable monitorAgent, StatsDBInterfacePooled dbInferface) {
        this.monitorAgent = monitorAgent;
        this.dbInferface = dbInferface;
    }

    public SwitchMonitorAgent(SVMPredictor classifier, Runnable monitorAgent, StatsDBInterfacePooled dbInferface) {
        this.classifier = classifier;
        this.monitorAgent = monitorAgent;
        this.dbInferface = dbInferface;
    }

    public SwitchMonitorAgent() {
        setUpMonitoringTask();
    }

    private void setUpMonitoringTask() {

        monitorAgent = new Runnable() {
            Logger LOG = Logger.getLogger(SwitchMonitorAgent.class.getName());

            @Override
            public void run() {
                LOG.info("Surveillance du commutateur " + switchName + " lancée avec succès\nFréquence de surveillance : " + monitoringFrequence);

                monitor();

            }
        };

    }

    private void monitor() {
        inspect();

    }

    public void save_alerte(Alert alert) {

        ArrayList<Alert> record = new ArrayList<>();
        record.add(alert);
        dbInferface.addToAlert(record);

    }

    public Alert generateAlert(long detection_time, StatAggregat detectionStats, SVMPredictionResult prediction) {

        Timestamp detection_date = new Timestamp(detection_time);
        Timestamp windows_min = new Timestamp(detectionStats.getWindows_start());
        Timestamp windows_max = new Timestamp(detectionStats.getWindows_end());

        Alert alert = new Alert();

        alert.setAlert_id(++alert_count);
        alert.setSwitch_name(switchName);
        alert.setDetection_time(detection_date);
        alert.setAttack_min_window(windows_min);
        alert.setAttack_max_window(windows_max);
        alert.setAttack_name("Flow Table Overloading Attack");
        alert.setTable_mean_occupation(detectionStats.getTableStatAggregat().getOccupation_average());
        alert.setTable_full_count((int) detectionStats.getTable_full_cout());

        alert.setProbability(prediction.getProb_estimates()[0]); // à revoir aboslument classe 1 (attack) est présente à la position 0 du tableau 
        alert.setMatching_indication(0);
        alert.setMissFlowCount(detectionStats.getMissFlowStats().getMissFlowCount());
        alert.setMissFlowRate(detectionStats.getMissFlowStats().getMissFlowRate());
        return alert;
    }

    private void refreshAlertTable(Alert alert, JTable jt) {

        DefaultTableModel tableModel = (DefaultTableModel) jt.getModel();
        Vector vector = new Vector();

        vector.add(alert.getAlert_id() + "");
        vector.add("Grave");
        vector.add(alert.getDetection_time());
        vector.add(alert.getSwitch_name());
        vector.add("Flow Table Overloading Attack");

        tableModel.addRow(vector);

    }

    public void alert(StatAggregat detectionStats, SVMPredictionResult prediction) {
        long detection_time = System.currentTimeMillis();
        Alert alert = generateAlert(detection_time, detectionStats, prediction);

        alertsMap.put(alert.getAlert_id(), alert);

        refreshAlertTable(alert, notification_table);

        if (save_alert == true) {

            save_alerte(alert);

        }

    }

    public void refreshMonitoringScrean(StatAggregat detectionStats, JPanel jp) {

    }

    private void inspect() {

        StatAggregat detectionStats = getDetectionStats();
        System.out.println(detectionStats);
        SVMPredictionResult prediction = predict(detectionStats, this.attributList);

        refreshMonitoringScrean(detectionStats, monitoringScrean);

        if (prediction != null) {

            //prediction.setNr_class(1); // à enlever
            if (prediction.getNr_class() == 1) { // attack detected   : la classe de l'attaque est égale à zéro

                alert(detectionStats, prediction);

            } else {

            }

        }

    }

    public SVMPredictionResult predict(StatAggregat aggregat, ArrayList<String> attributList) {

        double[] aline = new double[attributList.size()];

        int start = 0;
        int i = -1;

        if (attributList.contains("table_full_count")) {
            aline[++i] = new Double(aggregat.getTable_full_cout());

        }

        if (attributList.contains("missFlowCount")) {
            aline[++i] = new Double(aggregat.getMissFlowStats().getMissFlowCount());

        }

        if (attributList.contains("missFlowRate")) {
            aline[++i] = aggregat.getMissFlowStats().getMissFlowRate();

        }

        if (attributList.contains("lookUpRate")) {

            aline[++i] = aggregat.getTableStatAggregat().getLookUpRate();

        }

        if (attributList.contains("matchRate")) {
            aline[++i] = aggregat.getTableStatAggregat().getMatchRate();

        }

        if (attributList.contains("occupation_average")) {
            aline[++i] = aggregat.getTableStatAggregat().getOccupation_average();

        }

        if (attributList.contains("lookUpCount")) {
            aline[++i] = new Double(aggregat.getTableStatAggregat().getLookUpCount());

        }

        if (attributList.contains("matchCount")) {
            aline[++i] = new Double(aggregat.getTableStatAggregat().getMatchCount());

        }

        if (attributList.contains("missFlowRateUnderLookUpRate")) {
            aline[++i] = (double) aggregat.getMissFlowStats().getMissFlowRate() / aggregat.getTableStatAggregat().getLookUpRate();

        }

        if (attributList.contains("missFlowRateUnderMatchRate")) {
            aline[++i] = (double) aggregat.getMissFlowStats().getMissFlowRate() / aggregat.getTableStatAggregat().getMatchRate();

        }
        if (attributList.contains("missFlowRate/Table_Capacity")) {
            aline[++i] = (double) aggregat.getMissFlowStats().getMissFlowRate() / AggregatCreator.tableSize;

        }

        if (attributList.contains("missFlowCount/Table_Capacity")) {
            aline[++i] = (double) aggregat.getMissFlowStats().getMissFlowCount() / AggregatCreator.tableSize;

        }
        SVMPredictionResult predictionResult = null;

        if (checkDataBeforePrediction(aline) == true) {

            normalizeData();// à revoir absolument

            predictionResult = classifier.predict(aline, start);
            System.out.println(predictionResult);
        } else {

            System.out.println("No prediction in this period because of value =0");
        }

        return predictionResult;

    }

    public boolean checkDataBeforePrediction(double[] aline) {
        int i = 0;
        for (double v : aline) {
            if (attributList.contains("table_full_count")) {
                if (i > 0) {
                    if (v == 0) {
                        return false;
                    }

                }

            } else {
                if (v == 0) {
                    return false;
                }
            }

            i = i + 1;
        }

        return true;
    }

    public StatAggregat getDetectionStats() {

        long time_stamp_min = nextStartingWindow;
        long time_stamp_max = time_stamp_min + monitoringFrequence;

        StatAggregat aggregat = aggregatCurrentWindows(time_stamp_min, time_stamp_max);
        aggregat.setWindows_start(time_stamp_min);
        aggregat.setWindows_end(time_stamp_max);

        nextStartingWindow = time_stamp_max;
        return aggregat;

    }

    public StatAggregat aggregatCurrentWindows(long current_windows_start, long current_windows_end) {
        ArrayList<TableStats> tableStat = retrieveSwitchTableStats(switchName, tableId, current_windows_start, current_windows_end);
        ArrayList<SimpleCounter> tableFullCounter = retrieveTableFullCounter(switchName, tableId, current_windows_start, current_windows_end);
        ArrayList<FlowEntryStats> missFlows = retrieveSwitchFlowsStats(switchName, current_windows_start, current_windows_end);

        System.out.println(tableStat.size() + "; " + missFlows.size());

        int table_full_cout = tableFullCounter.size();

        viz.updateIndicator(Indicators.TABLE_FULL_COUNT, table_full_cout, current_windows_end);

        MissFlowStats missFlowStats = computeMissFlowsStats(missFlows);

        StatFromTable tableStatAggregat = computeTableStats(tableStat);

        return new StatAggregat(table_full_cout, missFlowStats, tableStatAggregat);

    }

    public void updateTimeSeries(ArrayList<TableStats> tableStat, ArrayList<SimpleCounter> tableFullCounter, ArrayList<FlowEntryStats> missFlows) {

        for (TableStats stats : tableStat) {
            double occupation = (double) stats.getActiveCount() / tableCapacity;

        }
        for (SimpleCounter counter : tableFullCounter) {

        }

        for (FlowEntryStats aMissFlow : missFlows) {

        }

    }

    public MissFlowStats computeMissFlowsStats(ArrayList<FlowEntryStats> missFlows) {
        if (missFlows.isEmpty()) {

            return new MissFlowStats(0, 0);
        }

        if (missFlows.size() == 1) {
            
            viz.updateIndicator(Indicators.MISS_FLOW_COUNT, missFlows.get(0).getPacketCount(), missFlows.get(0).getTime_stamp());
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

        viz.updateIndicator(Indicators.MISS_FLOW_COUNT, packetCount_min, time_stamp_min);
        viz.updateIndicator(Indicators.MISS_FLOW_COUNT, packetCount_max, time_stamp_max);

        return new MissFlowStats(missFlowsCount, missFlowRate);

    }

    public ArrayList<FlowEntryStats> retrieveSwitchFlowsStats(String dpid, long time_stamp_min, long time_stamp_max) {

        return toSwitchFlowsStatList(dbInferface.retrieveSwitchFlowsStats(dpid, time_stamp_min, time_stamp_max));

    }

    public ArrayList<String> getAttributList() {
        return attributList;
    }

    public void setAttributList(ArrayList<String> attributList) {
        this.attributList = attributList;
    }

    public boolean isSave_alert() {
        return save_alert;
    }

    public void setSave_alert(boolean save_alert) {
        this.save_alert = save_alert;
    }

    public boolean isSend_mail() {
        return send_mail;
    }

    public void setSend_mail(boolean send_mail) {
        this.send_mail = send_mail;
    }

    public String getNotification_mal() {
        return notification_mal;
    }

    public void setNotification_mal(String notification_mal) {
        this.notification_mal = notification_mal;
    }

    public JTable getNotification_table() {
        return notification_table;
    }

    public void setNotification_table(JTable notification_table) {
        this.notification_table = notification_table;
    }

    public JPanel getMonitoringScrean() {
        return monitoringScrean;
    }

    public void setMonitoringScrean(JPanel monitoringScrean) {
        this.monitoringScrean = monitoringScrean;
    }

    public Map<Integer, Alert> getAlertsMap() {
        return alertsMap;
    }

    public void setAlertsMap(Map<Integer, Alert> alertsMap) {
        this.alertsMap = alertsMap;
    }

    public int getAlert_count() {
        return alert_count;
    }

    public void setAlert_count(int alert_count) {
        this.alert_count = alert_count;
    }

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

            double current_occupation_average = (double) stat.getActiveCount() / tableCapacity;
            somme_occupation_average = somme_occupation_average + current_occupation_average;
            long time_stamp = stat.getTime_stamp();

            viz.updateIndicator(Indicators.TABLE_OCCUPATION, current_occupation_average, time_stamp);

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

    private DetectionStats compute_flow_table_mean_occupation(ArrayList<TableStats> tableStatses) {
        if (tableStatses.size() == 0) {

            return new DetectionStats(0, 0, null);

        }

        double flow_table_mean_occupation;
        double matching_indication;
        Map<Long, PonctualFlowOcuupation> tableOccupationMap = new HashMap<>();

        if (tableStatses.size() == 1) {

            TableStats aTableStats = tableStatses.get(0);
            flow_table_mean_occupation = (double) aTableStats.getActiveCount() / tableCapacity;

            matching_indication = (double) aTableStats.getMatchCount() / aTableStats.getLookUpCount();

            tableOccupationMap.put(aTableStats.getTime_stamp(), new PonctualFlowOcuupation(flow_table_mean_occupation));

            return new DetectionStats(flow_table_mean_occupation, matching_indication, tableOccupationMap);

        }

        double somme_of_ocuupation_average = 0;

        long max_time_stamp = -1; // à revoir
        long min_time_stamp = Long.MAX_VALUE;

        int maxTimeStampLookUpCount = 0;
        int minTimeStampLookUpCount = 0;
        int maxTimeStampMatchCount = 0;
        int minTimeStampMatchCount = 0;

        for (TableStats aTableStat : tableStatses) {
            double occupation_average = (double) aTableStat.getActiveCount() / tableCapacity;

            somme_of_ocuupation_average = somme_of_ocuupation_average + occupation_average;

            long time_stamp = aTableStat.getTime_stamp();

            tableOccupationMap.put(time_stamp, new PonctualFlowOcuupation(occupation_average));

            if (time_stamp > max_time_stamp) {
                max_time_stamp = time_stamp;

                maxTimeStampLookUpCount = aTableStat.getLookUpCount();
                maxTimeStampMatchCount = aTableStat.getMatchCount();

            }

            if (time_stamp < min_time_stamp) {
                min_time_stamp = time_stamp;

                minTimeStampLookUpCount = aTableStat.getLookUpCount();
                minTimeStampMatchCount = aTableStat.getMatchCount();
            }

        }

        System.out.println(maxTimeStampMatchCount + " - " + minTimeStampMatchCount + "/   " + maxTimeStampLookUpCount + "-" + minTimeStampLookUpCount);

        double lookUpInPeriod = (maxTimeStampLookUpCount - minTimeStampLookUpCount);
        if (lookUpInPeriod == 0) {
            matching_indication = 0;

        } else {
            matching_indication = (double) (maxTimeStampMatchCount - minTimeStampMatchCount) / lookUpInPeriod;
        }

        flow_table_mean_occupation = (double) somme_of_ocuupation_average / tableStatses.size();

        nextStartingWindow = max_time_stamp;

        return new DetectionStats(flow_table_mean_occupation, matching_indication, tableOccupationMap);
    }

    public SVMPredictor getClassifier() {
        return classifier;
    }

    public void setClassifier(SVMPredictor classifier) {
        this.classifier = classifier;
    }

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

    public int getMonitoringFrequence() {
        return monitoringFrequence;
    }

    public void setMonitoringFrequence(int monitoringFrequence) {
        this.monitoringFrequence = monitoringFrequence;
    }

    public Runnable getMonitorAgent() {
        return monitorAgent;
    }

    public void setMonitorAgent(Runnable monitorAgent) {
        this.monitorAgent = monitorAgent;
    }

    public StatsDBInterfacePooled getDbInferface() {
        return dbInferface;
    }

    public void setDbInferface(StatsDBInterfacePooled dbInferface) {
        this.dbInferface = dbInferface;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public int getTableCapacity() {
        return tableCapacity;
    }

    public void setTableCapacity(int tableCapacity) {
        this.tableCapacity = tableCapacity;
    }

    public long getNextStartingWindow() {
        return nextStartingWindow;
    }

    public void setNextStartingWindow(long nextStartingWindow) {
        this.nextStartingWindow = nextStartingWindow;
    }

    public ArrayList<SimpleCounter> retrieveTableFullCounter(String dpid, String tableId, long time_stamp_min, long time_stamp_max) {

        return toCounter(dbInferface.retrieveTableFullCounter(dpid, tableId, time_stamp_min, time_stamp_max));
    }

    public ArrayList<Aggregate> retriveFlowAggregate(String dpid, long time_stamp_min, long time_stamp_max) {

        return toAggregateList(dbInferface.retrieveFlowAggregate(dpid, time_stamp_min, time_stamp_max));

    }

    public ArrayList<TableStats> retrieveSwitchTableStats(String dpid, String tableId, long time_stamp_min, long time_stamp_max) {

        return toSwitchTableStatList(dbInferface.retrieveSwitchTableStats(dpid, tableId, time_stamp_min, time_stamp_max));

    }

    public ArrayList<FlowEntryStats> retrieveSwitchFlowsStats(String dpid, String tableId, long time_stamp_min, long time_stamp_max) {

        return toSwitchFlowsStatList(dbInferface.retrieveSwitchFlowsStats(dpid, time_stamp_min, time_stamp_max));

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
                dbInferface.getMySqlConnectionPool().returnObject(conn);
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

    private void normalizeData() {

    }

    public class PonctualFlowOcuupation {

        double flow_table_mean_ocuupation;

        public PonctualFlowOcuupation(double flow_table_mean_ocuupation) {
            this.flow_table_mean_ocuupation = flow_table_mean_ocuupation;
        }

        public double getFlow_table_mean_ocuupation() {
            return flow_table_mean_ocuupation;
        }

        public void setFlow_table_mean_ocuupation(double flow_table_mean_ocuupation) {
            this.flow_table_mean_ocuupation = flow_table_mean_ocuupation;
        }

    }

    public class DetectionStats {

        long min_time_stamp;
        long max_time_stamp;

        double flow_table_mean_ocuupation;
        double matching_indicator;
        double table_full_count;
        Map<Long, PonctualFlowOcuupation> tableOccupationMap;

        public DetectionStats(double flow_table_mean_ocuupation, double matching_indicator, double table_full_count, Map<Long, PonctualFlowOcuupation> tableOccupationMap) {
            this.flow_table_mean_ocuupation = flow_table_mean_ocuupation;
            this.matching_indicator = matching_indicator;
            this.table_full_count = table_full_count;
            this.tableOccupationMap = tableOccupationMap;
        }

        public long getMin_time_stamp() {
            return min_time_stamp;
        }

        public void setMin_time_stamp(long min_time_stamp) {
            this.min_time_stamp = min_time_stamp;
        }

        public long getMax_time_stamp() {
            return max_time_stamp;
        }

        public void setMax_time_stamp(long max_time_stamp) {
            this.max_time_stamp = max_time_stamp;
        }

        public double getTable_full_count() {
            return table_full_count;
        }

        public void setTable_full_count(double table_full_count) {
            this.table_full_count = table_full_count;
        }

        public DetectionStats(double flow_table_mean_ocuupation, double matching_indicator, Map<Long, PonctualFlowOcuupation> tableOccupationMap) {
            this.flow_table_mean_ocuupation = flow_table_mean_ocuupation;
            this.matching_indicator = matching_indicator;
            this.tableOccupationMap = tableOccupationMap;
        }

        public DetectionStats(double flow_table_mean_ocuupation, double matching_indicator) {
            this.flow_table_mean_ocuupation = flow_table_mean_ocuupation;
            this.matching_indicator = matching_indicator;
        }

        public double getFlow_table_mean_ocuupation() {
            return flow_table_mean_ocuupation;
        }

        public void setFlow_table_mean_ocuupation(double flow_table_mean_ocuupation) {
            this.flow_table_mean_ocuupation = flow_table_mean_ocuupation;
        }

        public double getMatching_indicator() {
            return matching_indicator;
        }

        public void setMatching_indicator(double matching_indicator) {
            this.matching_indicator = matching_indicator;
        }

        public Map<Long, PonctualFlowOcuupation> getTableOccupationMap() {
            return tableOccupationMap;
        }

        public void setTableOccupationMap(Map<Long, PonctualFlowOcuupation> tableOccupationMap) {
            this.tableOccupationMap = tableOccupationMap;
        }

        @Override
        public String toString() {
            return "DetectionStats{" + "flow_table_mean_ocuupation=" + flow_table_mean_ocuupation + ", matching_indicator=" + matching_indicator + '}';
        }

    }

    public SwitchStateVizualisation getViz() {
        return viz;
    }

    public void setViz(SwitchStateVizualisation viz) {
        this.viz = viz;
    }

    @Override
    public String toString() {
        return "SwitchMonitorAgent{" + "switchName=" + switchName + ", tableId=" + tableId + ", tableCapacity=" + tableCapacity + ", classifier=" + classifier + ", monitoringFrequence=" + monitoringFrequence + ", monitorAgent=" + monitorAgent + ", attributList=" + attributList + ", dbInferface=" + dbInferface + ", nextStartingWindow=" + nextStartingWindow + ", save_alert=" + save_alert + ", send_mail=" + send_mail + ", notification_mal=" + notification_mal + ", notification_table=" + notification_table + ", monitoringScrean=" + monitoringScrean + ", alertsMap=" + alertsMap + ", alert_count=" + alert_count + '}';
    }

}
