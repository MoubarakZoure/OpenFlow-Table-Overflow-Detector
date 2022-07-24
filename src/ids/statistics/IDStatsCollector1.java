/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ids.statistics;

import fl.rest.ressources.OpenFlowStats.Counters.CounterTable;
import fl.rest.ressources.OpenFlowStats.aggregate.AggregateSQL;
import fl.rest.ressources.OpenFlowStats.aggregate.FlowsAggregate;
import fl.rest.ressources.OpenFlowStats.flows.FlowEntryStats;
import fl.rest.ressources.OpenFlowStats.flows.FlowEntryStatsSQL;
import fl.rest.ressources.OpenFlowStats.flows.SwitchFlowsStats;
import fl.rest.ressources.OpenFlowStats.flowsRemoved.FlowRemovedTable;
import fl.rest.ressources.OpenFlowStats.packetIns.PacketInsTable;
import fl.rest.ressources.OpenFlowStats.port.PortActivity;
import fl.rest.ressources.OpenFlowStats.port.PortActivitySQL;
import fl.rest.ressources.OpenFlowStats.port.PortsActivities;
import fl.rest.ressources.OpenFlowStats.port.SwitchPortsActivities;
import fl.rest.ressources.OpenFlowStats.tablestats.SwitchTablesStats;
import fl.rest.ressources.OpenFlowStats.tablestats.TableStats;
import fl.rest.ressources.OpenFlowStats.tablestats.TableStatsSQL;
import fl.rest.ressources.controller.ControllerMemoryUsage;
import fl.rest.ressources.controller.ControllerMemoryUsageSQL;
import fl.rest.ressources.json.JsonBuilder;
import fl.rest.ressources.statcollector.PortBandwidth;
import fl.rest.ressources.statcollector.PortBandwidthSQL;
import fl.rest.ressources.urls.RestUrlBuilder;
import ids.statistics.storage.DBConnectionProperties;
import ids.statistics.storage.StatsDBInterfacePooled;
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
public class IDStatsCollector1 {

    private ScheduledExecutorService scheduler;
    private int workersNumber = 60;
    private String controller_ip = "192.168.72.131";
    private String controller_port = "8080";
    public String protocol = "http";
    DBConnectionProperties dBConnectionProperties;
    StatsDBInterfacePooled dbInferface = new StatsDBInterfacePooled();
    private Map<String, ArrayList<String>> monitoringPlan = new HashMap<>();

    private boolean isAllSwitchesMonitored = true;
    private boolean isOneTableMonitored = true;
    private long CLEANING_FREQUENCE = 500000;
    private long CLEANING_SIZE = 500000;
    

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

    private int WINDOWS = 1000;

    private long collectionStartTime = 0;
    
    String switchesList="s1";

    public IDStatsCollector1() {
        this.dbInferface.clearAllTables();
        ArrayList<String> a = new ArrayList<String>();
        a.add("0x0");

        monitoringPlan.put("00:00:00:00:00:00:00:01", a);
        monitoringPlan.put("00:00:00:00:00:00:00:03", a);
        monitoringPlan.put("00:00:00:00:00:00:00:02", a);

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

    public void prepareStatCollectorAgents() {

        scheduler = Executors.newScheduledThreadPool(workersNumber);

    }

    public void setUpCollectionTasks() {

        this.dbCleanerTask = new Runnable() {
         //   private   Logger LOG = Logger.getLogger(dbCleanerTask.getClass().getName());
            
            @Override
            public void run() {
                long t = System.currentTimeMillis();
                long time_stamp=t-CLEANING_SIZE; 
               
                
                dbInferface.cleanDB(time_stamp);
                
                 System.out.println("Database has been cleaned");
                
               
                

            }
        };

     

        this.flowRemovedStatsCollectionTask = new Runnable() {
            private RestUrlBuilder restUrlBuilder = new RestUrlBuilder(controller_ip, controller_port, protocol);
            private JsonBuilder jsonBuilder = new JsonBuilder(restUrlBuilder);
            int requestNumber = 0;

            @Override
            public void run() {
                System.out.println("*******************************************************************");
                requestNumber++;
                long t = System.currentTimeMillis();
                ArrayList<FlowRemovedTable> result = jsonBuilder.getFlowRemoved("all", "0x0", t - WINDOWS, t);
                for(FlowRemovedTable r : result){
                    System.out.println(r.getDpid()+" "+r.getTotal_count()+"   "+ r.getTime_stamp());
                
                }
                if (result.size() > 0) {
                    dbInferface.addToFlowRemovedTable(result);
                }

            }
        };

        this.tableFullStatsCollectionTask = new Runnable() {
            private RestUrlBuilder restUrlBuilder = new RestUrlBuilder(controller_ip, controller_port, protocol);
            private JsonBuilder jsonBuilder = new JsonBuilder(restUrlBuilder);
            int requestNumber = 0;

            @Override
            public void run() {
                requestNumber++;
                long t = System.currentTimeMillis();
                ArrayList<CounterTable> result = jsonBuilder.getCounter("table_full_count", "all", "0x0", t - WINDOWS, t);
                if (result.size() > 0) {
                    dbInferface.addToCounter("table_full_count", result);

                }

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
                    aggregate = jsonBuilder.getAllSwitchFlowsAggregate();

                    ArrayList<AggregateSQL> flowAggregateSQLs = new ArrayList<>();
                    for (Map.Entry<String, FlowsAggregate> entry : aggregate.entrySet()) {

                        flowAggregateSQLs.add(new AggregateSQL(entry.getKey(), System.currentTimeMillis(), entry.getValue().getAggregate()));

                    }
                    dbInferface.addToAggregateFlowsTable(flowAggregateSQLs);

                } else {

                    for (Map.Entry<String, ArrayList<String>> e : monitoringPlan.entrySet()) {
                        FlowsAggregate a = jsonBuilder.getSwitchFlowsAggregate(e.getKey());
                        ArrayList<AggregateSQL> sql = new ArrayList<>();
                        sql.add(new AggregateSQL(e.getKey(), System.currentTimeMillis(), a.getAggregate()));
                        dbInferface.addToAggregateFlowsTable(sql);

                    }

                }
                excutionNumber++;

            }
        };

        this.flowsStatsCollectionTask = new Runnable() {
            private RestUrlBuilder restUrlBuilder = new RestUrlBuilder(controller_ip, controller_port, protocol);
            private JsonBuilder jsonBuilder = new JsonBuilder(restUrlBuilder);
            int excutionNumber = 0;

            @Override
            public void run() {

                if (isAllSwitchesMonitored) {
                    Map<String, SwitchFlowsStats> allSwitchFlowStatsMap = jsonBuilder.getAllSwitchFlowsStats();
                    long time_stamp=System.currentTimeMillis();
                    ArrayList<FlowEntryStatsSQL> records = new ArrayList<>();

                    for (Map.Entry<String, SwitchFlowsStats> oneSwitchFlowStats : allSwitchFlowStatsMap.entrySet()) {
                        FlowEntryStats[] thisSwitchFlowEntryStat = oneSwitchFlowStats.getValue().getFlows();

                        for (FlowEntryStats oneFlowEntryStats : thisSwitchFlowEntryStat) {
                            records.add(new FlowEntryStatsSQL(oneSwitchFlowStats.getKey(), time_stamp, oneFlowEntryStats));

                        }

                    }

                    dbInferface.addToFlowsTable(records);

                } else {
                    

                    for (Map.Entry<String, ArrayList<String>> p : monitoringPlan.entrySet()) {
                        ArrayList<FlowEntryStatsSQL> records = new ArrayList<>();

                        SwitchFlowsStats switchFlowsStats = jsonBuilder.getSwitchFlowsStats(p.getKey());
                         long time_stamp=System.currentTimeMillis();
                        FlowEntryStats[] thisSwitchFlowEntryStat = switchFlowsStats.getFlows();

                        for (FlowEntryStats oneFlowEntryStats : thisSwitchFlowEntryStat) {
                            records.add(new FlowEntryStatsSQL(p.getKey(), time_stamp, oneFlowEntryStats));

                        }

                        dbInferface.addToFlowsTable(records);
                    }

                }

                excutionNumber++;
            }

        };

        this.portBandwithCollectionTask = new Runnable() {

            private RestUrlBuilder restUrlBuilder = new RestUrlBuilder(controller_ip, controller_port, protocol);
            private JsonBuilder jsonBuilder = new JsonBuilder(restUrlBuilder);
            int excutionNumber = 0;

            public void run() {
                if (isAllSwitchesMonitored) {

                    ArrayList<PortBandwidth> portStats = jsonBuilder.getSwitchPortsBandwidth("all", "all");
                    ArrayList<PortBandwidthSQL> records = new ArrayList<>();
                    for (PortBandwidth p : portStats) {
                        records.add(new PortBandwidthSQL(System.currentTimeMillis(), p));

                    }
                    dbInferface.addToPortBandwidthTable(records);

                } else {
                    ArrayList<PortBandwidth> portStats = null;
                    ArrayList<PortBandwidthSQL> records = new ArrayList<>();
                    for (Map.Entry<String, ArrayList<String>> p : monitoringPlan.entrySet()) {
                        records.removeAll(records);
                        portStats = jsonBuilder.getSwitchPortsBandwidth(p.getKey(), "all");
                        for (PortBandwidth s : portStats) {
                            records.add(new PortBandwidthSQL(System.currentTimeMillis(), s));

                        }
                        dbInferface.addToPortBandwidthTable(records);

                    }

                }

                excutionNumber++;
            }
        };

        this.switchPortActivityCollectionTask = new Runnable() {

            private RestUrlBuilder restUrlBuilder = new RestUrlBuilder(controller_ip, controller_port, protocol);
            private JsonBuilder jsonBuilder = new JsonBuilder(restUrlBuilder);
            int executionNumber = 0;

            @Override
            public void run() {
                if (isAllSwitchesMonitored) {
                    ArrayList<PortActivitySQL> records = new ArrayList<>();
                    Map<String, SwitchPortsActivities> portsActivies = jsonBuilder.getAllSwitchPortsActivities();
                    for (Map.Entry<String, SwitchPortsActivities> oneSwitchPortActivities : portsActivies.entrySet()) {

                        for (PortsActivities portActivities : oneSwitchPortActivities.getValue().getPort_reply()) {
                            for (PortActivity portActivity : portActivities.getPort()) {

                                records.add(new PortActivitySQL(oneSwitchPortActivities.getKey(),
                                        System.currentTimeMillis(), portActivities.getVersion(), portActivity));
                            }

                        }

                    }
                    dbInferface.addToPortActivityTable(records);

                } else {
                    for (Map.Entry<String, ArrayList<String>> p : monitoringPlan.entrySet()) {
                        ArrayList<PortActivitySQL> records = new ArrayList<>();
                        SwitchPortsActivities switchPortsActivities = jsonBuilder.getSwitchPortsActivities(p.getKey());
                        for (PortsActivities portActivities : switchPortsActivities.getPort_reply()) {
                            for (PortActivity portActivity : portActivities.getPort()) {

                                records.add(new PortActivitySQL(p.getKey(),
                                        System.currentTimeMillis(), portActivities.getVersion(), portActivity));
                            }

                        }

                        dbInferface.addToPortActivityTable(records);

                    }
                }

                executionNumber++;
            }
        };

        this.controlerMemoryUsageCollectionTask = new Runnable() {

            private RestUrlBuilder restUrlBuilder = new RestUrlBuilder(controller_ip, controller_port, protocol);
            private JsonBuilder jsonBuilder = new JsonBuilder(restUrlBuilder);
            int excutionNumber = 0;

            @Override
            public void run() {

                ControllerMemoryUsage controllerMemoryUsage = jsonBuilder.getControllerMemoryUsage();
                ControllerMemoryUsageSQL controllerMemoryUsageSQL = new ControllerMemoryUsageSQL(System.currentTimeMillis(), controllerMemoryUsage);
                ArrayList<ControllerMemoryUsageSQL> recordSQLs = new ArrayList<>();
                recordSQLs.add(controllerMemoryUsageSQL);
                dbInferface.addToControlerMemoryUsage(recordSQLs);
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
                    Map<String, SwitchTablesStats> tableStats = jsonBuilder.getAllSwitchTablesStats();
                    ArrayList<TableStatsSQL> records = new ArrayList<>();

                    for (Map.Entry<String, SwitchTablesStats> e : tableStats.entrySet()) {
                        TableStats[] table = e.getValue().getTable();
                        for (TableStats t : table) {
                            if ("0x0".equals(t.getTableID())) {
                                records.add(new TableStatsSQL(e.getKey(), System.currentTimeMillis(), e.getValue().getVersion(), t));
                            }

                        }

                    }

                    dbInferface.addToTableStatsTable(records);
                } else {
                    ArrayList<TableStatsSQL> records = new ArrayList<>();
                    for (Map.Entry<String, ArrayList<String>> p : monitoringPlan.entrySet()) {
                        records.removeAll(records);
                        SwitchTablesStats switchTablesStats = jsonBuilder.getSwitchTablesStats(p.getKey());
                        TableStats[] table = switchTablesStats.getTable();

                        for (TableStats t : table) {
                            if ("0x0".equals(t.getTableID())) {
                                records.add(new TableStatsSQL(p.getKey(), System.currentTimeMillis(), switchTablesStats.getVersion(), t));
                                System.out.println(p.getKey() + " " + t + " " + (executionNumber + 1));
                            }

                        }

                        dbInferface.addToTableStatsTable(records);
                    }

                }
                executionNumber++;
            }
        };

    }

    public void startCollection() {

        prepareStatCollectorAgents();
        setUpCollectionTasks();

        collectionStartTime = System.currentTimeMillis();
        
        this.scheduler.scheduleAtFixedRate(aggregateStatsCollectionTask, 0, WINDOWS, TimeUnit.MILLISECONDS);
     
        this.scheduler.scheduleAtFixedRate(tableStatsCollectionTask, 0, WINDOWS, TimeUnit.MILLISECONDS);
     
       // this.scheduler.scheduleAtFixedRate(dbCleanerTask, 0,CLEANING_FREQUENCE, TimeUnit.MILLISECONDS);

    }

    public void stopCollection(boolean now) {

        if (now) {

            scheduler.shutdownNow();
        } else {
            scheduler.shutdown();

        }
    }

    public static void main(String[] args) {
        IDStatsCollector1 idsc = new IDStatsCollector1();
        idsc.setWorkersNumber(200);
        idsc.startCollection();
    }

}
