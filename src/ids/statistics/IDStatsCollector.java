/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ids.statistics;

import fl.rest.ressources.OpenFlowStats.aggregate.AggregateSQL;
import fl.rest.ressources.OpenFlowStats.aggregate.FlowsAggregate;
import fl.rest.ressources.OpenFlowStats.flows.FlowEntryStats;
import fl.rest.ressources.OpenFlowStats.flows.FlowEntryStatsSQL;
import fl.rest.ressources.OpenFlowStats.flows.SwitchFlowsStats;
import fl.rest.ressources.OpenFlowStats.tablestats.SwitchTablesStats;
import fl.rest.ressources.OpenFlowStats.tablestats.TableStats;
import fl.rest.ressources.OpenFlowStats.tablestats.TableStatsSQL;
import fl.rest.ressources.json.JsonBuilder;
import fl.rest.ressources.urls.RestUrlBuilder;
import ids.statistics.storage.DBConnectionProperties;
import ids.statistics.storage.StatsDBInterfacePooled;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 *
 * @author Moubarak
 */
public class IDStatsCollector {

    private boolean isCollecting = false;
    private static final Logger LOG = Logger.getLogger(IDStatsCollector.class.getName());
    String switchesList = "";
    private int WINDOWS = 3000;// à revoir

    private long CLEANING_FREQUENCE = 500000;
    private long CLEANING_SIZE = 500000;

    private String controller_ip = "192.168.72.131";
    private String controller_port = "8080";
    public String protocol = "http";

    DBConnectionProperties dBConnectionProperties;

    private int workersNumber = 10;

    private ScheduledExecutorService scheduler;

    StatsDBInterfacePooled dbInferface = new StatsDBInterfacePooled();

    private Map<String, ArrayList<String>> monitoringPlan = new HashMap<>();

    private boolean isAllSwitchesMonitored = true;
    private boolean isOneTableMonitored = true;

    Runnable packetInsStatsCollectionTask;
    Runnable flowRemovedStatsCollectionTask;
    Runnable tableFullStatsCollectionTask;
    Runnable aggregateStatsCollectionTask;
    Runnable flowsStatsCollectionTask;
    Runnable switchPortActivityCollectionTask;
    Runnable tableStatsCollectionTask;
    Runnable controlerMemoryUsageCollectionTask;
    Runnable portBandwithCollectionTask;

    Runnable dbCleanerTask;

    private long collectionStartTime = 0;

    public IDStatsCollector() {

    }

    public void prepareStatCollectorAgents() {

        LOG.info("Création d'un pool de " + workersNumber + " pour la collecte des statistiques ...");

        scheduler = Executors.newScheduledThreadPool(workersNumber);
        LOG.info("Success !!!");

    }

    public void setUpCollectionTasks() {

        this.dbCleanerTask = new Runnable() {
            //   private   Logger LOG = Logger.getLogger(dbCleanerTask.getClass().getName());

            @Override
            public void run() {
                long t = System.currentTimeMillis();
                long time_stamp = t - CLEANING_SIZE;

                dbInferface.cleanDB(time_stamp);

                System.out.println("Database has been cleaned");

            }
        };

        this.aggregateStatsCollectionTask = new Runnable() {

            private RestUrlBuilder restUrlBuilder = new RestUrlBuilder(controller_ip, controller_port, protocol);
            private JsonBuilder jsonBuilder = new JsonBuilder(restUrlBuilder);
            int excutionNumber = 0;

            @Override
            public void run() {

                if (isAllSwitchesMonitored) {

                    Map<String, FlowsAggregate> aggregate = null;
                    aggregate = jsonBuilder.getAllSwitchFlowsAggregateMap(switchesList);

                    ArrayList<AggregateSQL> flowAggregateSQLs = new ArrayList<>();
                    for (Map.Entry<String, FlowsAggregate> entry : aggregate.entrySet()) {

                        flowAggregateSQLs.add(new AggregateSQL(entry.getKey(), entry.getValue().getTime_stamp(), entry.getValue().getAggregate()));

                    }
                    if (flowAggregateSQLs.size() > 0) {
                        dbInferface.addToAggregateFlowsTable(flowAggregateSQLs);
                    }

                } else {

                }
                excutionNumber++;

            }
        };

        this.tableStatsCollectionTask = new Runnable() {

            private RestUrlBuilder restUrlBuilder = new RestUrlBuilder(controller_ip, controller_port, protocol);
            private JsonBuilder jsonBuilder = new JsonBuilder(restUrlBuilder);
            int executionNumber = 0;

            @Override
            public void run() {
                if (isAllSwitchesMonitored) {
                    Map<String, SwitchTablesStats> tableStats = jsonBuilder.getAllSwitchTablesStatsMap(switchesList);
                    ArrayList<TableStatsSQL> records = new ArrayList<>();

                    for (Map.Entry<String, SwitchTablesStats> e : tableStats.entrySet()) {
                        TableStats[] table = e.getValue().getTable();
                        for (TableStats t : table) {

                            if ("0".equals(t.getTableID())) { // à revoir
                                records.add(new TableStatsSQL(e.getKey(), e.getValue().getTime_stamp(), e.getValue().getVersion(), t));

                            }

                        }

                    }
                    if (records.size() > 0) {
                        dbInferface.addToTableStatsTable(records);
                    }
                } else {

                }
                executionNumber++;
            }
        };

        flowsStatsCollectionTask = new Runnable() {
            private RestUrlBuilder restUrlBuilder = new RestUrlBuilder(controller_ip, controller_port, protocol);
            private JsonBuilder jsonBuilder = new JsonBuilder(restUrlBuilder);
            int executionNumber = 0;

            @Override
            public void run() {

                if (isAllSwitchesMonitored) {
                    Map<String, SwitchFlowsStats> tableStats = jsonBuilder.getSwitchFlowsStatsMap(switchesList);

                    ArrayList<FlowEntryStatsSQL> records = new ArrayList<>();

                    for (Map.Entry<String, SwitchFlowsStats> e : tableStats.entrySet()) {
                        FlowEntryStats[] flowsEntry = e.getValue().getFlows();
                        for (FlowEntryStats t : flowsEntry) {

                          

                            records.add(new FlowEntryStatsSQL(e.getKey(), e.getValue().getTime_stamp(), t.getPacketCount()));

                        }

                    }

                    if (records.size() > 0) {

                        dbInferface.addToFlowsTable(records);
                       

                    }
                } else {

                }
                executionNumber++;

            }
        };

    }

    public void startCollection() {

        prepareStatCollectorAgents();
        setUpCollectionTasks();

        collectionStartTime = System.currentTimeMillis();

        this.scheduler.scheduleAtFixedRate(tableStatsCollectionTask, 0, WINDOWS, TimeUnit.MILLISECONDS);
        this.scheduler.scheduleAtFixedRate(flowsStatsCollectionTask, 0, WINDOWS, TimeUnit.MILLISECONDS);
        isCollecting = true;
        LOG.info("Collecte des statistiques lancée à " + new Timestamp(collectionStartTime) + " ");
        LOG.info("Fréquence de la collecte :" + WINDOWS);

        //this.scheduler.scheduleAtFixedRate(dbCleanerTask, 0,CLEANING_FREQUENCE, TimeUnit.MILLISECONDS);
    }

    public void stopCollection(boolean now) {

        if (now) {

            scheduler.shutdownNow();
        } else {
            scheduler.shutdown();

        }
        
        isCollecting = false;
        LOG.info("Arret de la collection des statistiques");
    }

    public static void main(String[] args) {
        IDStatsCollector idsc = new IDStatsCollector();
        idsc.clearAllTables();
        idsc.setWorkersNumber(200);
        idsc.startCollection();
    }

    public void clearAllTables() {

        this.dbInferface.clearAllTables();

    }

    public String getSwitchesList() {
        return switchesList;
    }

    public void setSwitchesList(String switchesList) {
        this.switchesList = switchesList;
    }

    public int getWINDOWS() {
        return WINDOWS;
    }

    public void setWINDOWS(int WINDOWS) {
        this.WINDOWS = WINDOWS;
    }

    public long getCLEANING_FREQUENCE() {
        return CLEANING_FREQUENCE;
    }

    public void setCLEANING_FREQUENCE(long CLEANING_FREQUENCE) {
        this.CLEANING_FREQUENCE = CLEANING_FREQUENCE;
    }

    public long getCLEANING_SIZE() {
        return CLEANING_SIZE;
    }

    public void setCLEANING_SIZE(long CLEANING_SIZE) {
        this.CLEANING_SIZE = CLEANING_SIZE;
    }

    public String getController_ip() {
        return controller_ip;
    }

    public void setController_ip(String controller_ip) {
        this.controller_ip = controller_ip;
    }

    public String getController_port() {
        return controller_port;
    }

    public void setController_port(String controller_port) {
        this.controller_port = controller_port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public DBConnectionProperties getdBConnectionProperties() {
        return dBConnectionProperties;
    }

    public void setdBConnectionProperties(DBConnectionProperties dBConnectionProperties) {
        this.dBConnectionProperties = dBConnectionProperties;
    }

    public StatsDBInterfacePooled getDbInferface() {
        return dbInferface;
    }

    public void setDbInferface(StatsDBInterfacePooled dbInferface) {
        this.dbInferface = dbInferface;
    }

    public Map<String, ArrayList<String>> getMonitoringPlan() {
        return monitoringPlan;
    }

    public void setMonitoringPlan(Map<String, ArrayList<String>> monitoringPlan) {
        this.monitoringPlan = monitoringPlan;
    }

    public boolean isIsAllSwitchesMonitored() {
        return isAllSwitchesMonitored;
    }

    public void setIsAllSwitchesMonitored(boolean isAllSwitchesMonitored) {
        this.isAllSwitchesMonitored = isAllSwitchesMonitored;
    }

    public boolean isIsOneTableMonitored() {
        return isOneTableMonitored;
    }

    public void setIsOneTableMonitored(boolean isOneTableMonitored) {
        this.isOneTableMonitored = isOneTableMonitored;
    }

    public Runnable getPacketInsStatsCollectionTask() {
        return packetInsStatsCollectionTask;
    }

    public void setPacketInsStatsCollectionTask(Runnable packetInsStatsCollectionTask) {
        this.packetInsStatsCollectionTask = packetInsStatsCollectionTask;
    }

    public Runnable getFlowRemovedStatsCollectionTask() {
        return flowRemovedStatsCollectionTask;
    }

    public void setFlowRemovedStatsCollectionTask(Runnable flowRemovedStatsCollectionTask) {
        this.flowRemovedStatsCollectionTask = flowRemovedStatsCollectionTask;
    }

    public Runnable getTableFullStatsCollectionTask() {
        return tableFullStatsCollectionTask;
    }

    public void setTableFullStatsCollectionTask(Runnable tableFullStatsCollectionTask) {
        this.tableFullStatsCollectionTask = tableFullStatsCollectionTask;
    }

    public Runnable getAggregateStatsCollectionTask() {
        return aggregateStatsCollectionTask;
    }

    public void setAggregateStatsCollectionTask(Runnable aggregateStatsCollectionTask) {
        this.aggregateStatsCollectionTask = aggregateStatsCollectionTask;
    }

    public Runnable getFlowsStatsCollectionTask() {
        return flowsStatsCollectionTask;
    }

    public void setFlowsStatsCollectionTask(Runnable flowsStatsCollectionTask) {
        this.flowsStatsCollectionTask = flowsStatsCollectionTask;
    }

    public Runnable getSwitchPortActivityCollectionTask() {
        return switchPortActivityCollectionTask;
    }

    public void setSwitchPortActivityCollectionTask(Runnable switchPortActivityCollectionTask) {
        this.switchPortActivityCollectionTask = switchPortActivityCollectionTask;
    }

    public Runnable getTableStatsCollectionTask() {
        return tableStatsCollectionTask;
    }

    public void setTableStatsCollectionTask(Runnable tableStatsCollectionTask) {
        this.tableStatsCollectionTask = tableStatsCollectionTask;
    }

    public Runnable getControlerMemoryUsageCollectionTask() {
        return controlerMemoryUsageCollectionTask;
    }

    public void setControlerMemoryUsageCollectionTask(Runnable controlerMemoryUsageCollectionTask) {
        this.controlerMemoryUsageCollectionTask = controlerMemoryUsageCollectionTask;
    }

    public Runnable getPortBandwithCollectionTask() {
        return portBandwithCollectionTask;
    }

    public void setPortBandwithCollectionTask(Runnable portBandwithCollectionTask) {
        this.portBandwithCollectionTask = portBandwithCollectionTask;
    }

    public Runnable getDbCleanerTask() {
        return dbCleanerTask;
    }

    public void setDbCleanerTask(Runnable dbCleanerTask) {
        this.dbCleanerTask = dbCleanerTask;
    }

    public long getCollectionStartTime() {
        return collectionStartTime;
    }

    public void setCollectionStartTime(long collectionStartTime) {
        this.collectionStartTime = collectionStartTime;
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public void setScheduler(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    public int getWorkersNumber() {
        return workersNumber;
    }

    public void setWorkersNumber(int workersNumber) {
        this.workersNumber = workersNumber;
    }

    public Runnable getPacketInsStatsCollection() {
        return packetInsStatsCollectionTask;
    }

    public void setPacketInsStatsCollection(Runnable packetInsStatsCollection) {
        this.packetInsStatsCollectionTask = packetInsStatsCollection;
    }

    public boolean isIsCollecting() {
        return isCollecting;
    }

    public void setIsCollecting(boolean isCollecting) {
        this.isCollecting = isCollecting;
    }

}
