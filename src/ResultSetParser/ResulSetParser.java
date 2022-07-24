/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ResultSetParser;

import aggregationPackage.Aggregator;
import fl.rest.ressources.OpenFlowStats.Counters.SimpleCounter;
import fl.rest.ressources.OpenFlowStats.aggregate.Aggregate;
import fl.rest.ressources.OpenFlowStats.flows.SimpleFlowStats;
import fl.rest.ressources.OpenFlowStats.flows.SimpleMatch;
import fl.rest.ressources.OpenFlowStats.flowsRemoved.SimpleFlowRemoved;
import fl.rest.ressources.OpenFlowStats.port.PortActivity;
import fl.rest.ressources.OpenFlowStats.tablestats.TableStats;
import fl.rest.ressources.controller.ControllerMemoryUsage;
import fl.rest.ressources.statcollector.PortBandwidth;
import ids.statistics.storage.DBConnectionProperties;
import ids.statistics.storage.StatsDBInterfacePooled;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.pool.ObjectPool;
import stats.entropy.Entropy;

/**
 *
 * @author Moubarak
 */
public final class ResulSetParser {

    public static ArrayList<SimpleMatch> resultSetToPacketInsMatches(ResultSet resultSet) {
        if (resultSet == null) {
            return null;

        }

        ArrayList<SimpleMatch> result = new ArrayList<>();

        try {
            while (resultSet.next()) {

                String ip_src = resultSet.getString("ip_src");
                String ip_dst = resultSet.getString("ip_dest");
                Integer port_src = resultSet.getInt("port_src");
                Integer port_dst = resultSet.getInt("port_dest");

                SimpleMatch simpleMatch = new SimpleMatch(ip_src, ip_dst, port_src, port_dst);
                result.add(simpleMatch);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResulSetParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
       
        return result;
    }

    public static ArrayList<Aggregate> resultSetToAggregate(ResultSet resultSet) {
        if (resultSet == null) {
            return null;

        }

        ArrayList<Aggregate> result = new ArrayList<>();

        try {
            while (resultSet.next()) {

                long byteCount = resultSet.getLong("byteCount");
                int flags = resultSet.getInt("flags");
                int flowCount = resultSet.getInt("flowCount");
                long packetCount = resultSet.getLong("packetCount");

                Aggregate aggregate = new Aggregate(byteCount, flags, flowCount, packetCount, null);

                result.add(aggregate);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResulSetParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return result;
    }

    public static ArrayList<TableStats> resultSetToTableStats(ResultSet resultSet) {

        if (resultSet == null) {
            return null;

        }

        ArrayList<TableStats> result = new ArrayList<>();

        try {
            while (resultSet.next()) {

                int activeCount = resultSet.getInt("activeCount");
                int lookUpCount = resultSet.getInt("lookUpCount");
                int matchCount = resultSet.getInt("matchCount");

                TableStats tableStat = new TableStats(activeCount, lookUpCount, matchCount, null);

                result.add(tableStat);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResulSetParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return result;
    }

    public static ArrayList<ControllerMemoryUsage> resultSetToControlerMemoryUsage(ResultSet resultSet) {

        if (resultSet == null) {
            return null;

        }

        ArrayList<ControllerMemoryUsage> result = new ArrayList<>();

        try {
            while (resultSet.next()) {

                long total = resultSet.getLong("total");
                long free = resultSet.getLong("free");

                ControllerMemoryUsage controllerMemoryUsage = new ControllerMemoryUsage(total, free);

                result.add(controllerMemoryUsage);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResulSetParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return result;
    }

    public static ArrayList<PortBandwidth> resultSetToPortBandwidths(ResultSet resultSet) {

        if (resultSet == null) {
            return null;

        }

        ArrayList<PortBandwidth> result = new ArrayList<>();

        try {
            while (resultSet.next()) {

                long rx = resultSet.getLong("rx");
                long tx = resultSet.getLong("tx");
                //  String port = resultSet.getString("port_");
                //  String updateTime = resultSet.getString("updatedTime");

                PortBandwidth portBandwidth = new PortBandwidth(rx, tx, null, null, null);

                result.add(portBandwidth);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResulSetParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return result;
    }

    public static ArrayList<PortActivity> resultSetToPortActivitys(ResultSet resultSet) {

        if (resultSet == null) {
            return null;

        }

        ArrayList<PortActivity> result = new ArrayList<>();

        try {
            while (resultSet.next()) {

                long collisions = resultSet.getLong("collisions");
                long durationNsec = resultSet.getLong("durationNsec");
                long durationSec = resultSet.getLong("durationSec");
                //  String portNumber = resultSet.getString("portNumber");
                long receivedBytes = resultSet.getLong("receiveBytes");
                long receiveCRCErrors = resultSet.getLong("receiveCRCErrors");
                long receiveDropped = resultSet.getLong("receiveDropped");
                long receiveErrors = resultSet.getLong("receiveErrors");
                long receiveFrameErrors = resultSet.getLong("receiveFrameErrors");
                long receiveOverrunErrors = resultSet.getLong("receiveOverrunErrors");
                long receivePackets = resultSet.getLong("receivePackets");

                long transmitBytes = resultSet.getLong("transmitBytes");
                long transmitDropped = resultSet.getLong("transmitDropped");
                long transmitErrors = resultSet.getLong("transmitErrors");
                long transmitPackets = resultSet.getLong("transmitPackets");

                PortActivity portActivity
                        = new PortActivity(collisions, durationNsec, durationSec, null,
                                receivedBytes, receiveCRCErrors, receiveDropped, receiveErrors, receiveFrameErrors,
                                receiveOverrunErrors, receivePackets, transmitBytes, transmitDropped, transmitErrors,
                                transmitPackets);

                result.add(portActivity);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResulSetParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return result;
    }

    public static ArrayList<SimpleFlowStats> resultSetToFlowsEntryStats(ResultSet resultSet) {

        if (resultSet == null) {
            return null;

        }

        ArrayList<SimpleFlowStats> result = new ArrayList<>();

        try {
            while (resultSet.next()) {

                long byteCount = resultSet.getLong("byteCount");
                int flags = resultSet.getInt("flags");
                long packetCount = resultSet.getLong("packetCount");
                long durationNsec = resultSet.getLong("durationNSeconds");
                long durationSec = resultSet.getLong("durationSeconds");

                SimpleFlowStats simpleFlowStats = new SimpleFlowStats(byteCount, durationNsec, durationSec, flags, packetCount);

                result.add(simpleFlowStats);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResulSetParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return result;
    }

    public static ArrayList<SimpleFlowRemoved> resultSetToFlowRemoved(ResultSet resultSet) {

        if (resultSet == null) {
            return null;

        }

        ArrayList<SimpleFlowRemoved> result = new ArrayList<>();

        try {
            while (resultSet.next()) {

                int byteCount = resultSet.getInt("byteCount");
                int total_count = resultSet.getInt("total_count");
                int packetCount = resultSet.getInt("packetCount");
                long durationNsec = resultSet.getLong("duration_nsec");
                int durationSec = resultSet.getInt("duration_sec");
                //  String matching = resultSet.getString("matching");

                SimpleFlowRemoved simpleFlowRemoved = new SimpleFlowRemoved(null, durationSec, durationNsec, byteCount, packetCount, total_count);

                result.add(simpleFlowRemoved);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResulSetParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return result;
    }

    public static ArrayList<SimpleCounter> resultSetToCounter(ResultSet resultSet) {

        if (resultSet == null) {
            return null;

        }

        ArrayList<SimpleCounter> result = new ArrayList<>();

        try {
            while (resultSet.next()) {

                int total_count = resultSet.getInt("total_count");

                SimpleCounter simpleCounter = new SimpleCounter(total_count);

                result.add(simpleCounter);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResulSetParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return result;
    }

    public static double parseToEntropy(ResultSet resultSet) {
        double entropy;
        try {
            resultSet.next();
            entropy = resultSet.getDouble("entropy");
            
            return entropy;
        } catch (SQLException ex) {
            Logger.getLogger(ResulSetParser.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

    }

    public static void main(String[] args) {

        StatsDBInterfacePooled dbInterface = new StatsDBInterfacePooled();
        DBConnectionProperties db = new DBConnectionProperties();

        ObjectPool pool = dbInterface.createMySqlConnectionPool(20);
        Aggregator aggregator = new Aggregator();
       

        String[] table_list = new String[]{"packet_in_count", "packet_out_count", "table_full_count",
            "flow_removed_count", "packetIns_table", "flowRemoved_table", "flows_table", "aggregate_flows_table",
            "flows_table", "switch_ports_activities_table", "tableStats_table", "port_bandWidth_table",
            "controlerMemoryUsage_table"};
        ResultSet resultSet = null;

        for (String table : table_list) {
            if (table.equals("aggregate_flows_table") || table.equals("switch_ports_activities_table") || table.equals("port_bandWidth_table")) {
                resultSet = dbInterface.selectFromTable(table, "00:00:00:00:00:00:00:01", 0, Long.MAX_VALUE, 1);
                switch (table) {
                    case "aggregate_flows_table": {
                        ArrayList<Aggregate> list = ResulSetParser.resultSetToAggregate(resultSet);
                        Aggregate agg = aggregator.aggregate(list, Aggregator.MEAN_AGGREGATOR);
                        System.out.println("*** " + agg.toString() + "**");

                    }
                    ;
                    break;
                    case "switch_ports_activities_table": {
                        ArrayList<PortActivity> list = ResulSetParser.resultSetToPortActivitys(resultSet);
                        PortActivity agg = aggregator.aggregatePortActivity(list, Aggregator.MEAN_AGGREGATOR);
                        System.out.println("*** " + agg.toString() + "**");

                    }
                    case "port_bandWidth_table": {
                        ArrayList<PortBandwidth> list = ResulSetParser.resultSetToPortBandwidths(resultSet);
                        PortBandwidth agg = aggregator.aggregatePortBandwidth(list, Aggregator.MEAN_AGGREGATOR);
                        System.out.println("*** " + agg.toString() + "**");

                    }
                    break;

                }

            } else {
                if (table.equals("controlerMemoryUsage_table")) {
                    resultSet = dbInterface.selectFromTable(table, 0, Long.MAX_VALUE, 1);
                    ArrayList<ControllerMemoryUsage> list = ResulSetParser.resultSetToControlerMemoryUsage(resultSet);
                    ControllerMemoryUsage agg = aggregator.aggregateControlerMemoryUsage(list, Aggregator.GEOMETRIC_MEAN_AGGREGATOR);
                    System.out.println("*** " + agg.toString() + "**");

                } else {
                    resultSet = dbInterface.selectFromTable(table, "00:00:00:00:00:00:00:01", "0x0", 0, Long.MAX_VALUE, 1);
                    switch (table) {
                        case "packetIns_table":
                            System.out.println("PacketIns" + ResulSetParser.resultSetToPacketInsMatches(resultSet).size());
                            break;
                        case "flowRemoved_table": {
                            ArrayList<SimpleFlowRemoved> list = ResulSetParser.resultSetToFlowRemoved(resultSet);
                            SimpleFlowRemoved agg = aggregator.aggregateFlowRemoved(list, Aggregator.MEAN_AGGREGATOR);
                            System.out.println("*** " + agg.toString() + "**");
                            break;
                        }
                        case "flows_table":
                            System.out.println("flows_table" + ResulSetParser.resultSetToFlowsEntryStats(resultSet).size());
                            break;

                        case "tableStats_table": {
                            ArrayList<TableStats> list = ResulSetParser.resultSetToTableStats(resultSet);
                            TableStats agg = aggregator.aggregateTableStats(list, Aggregator.MEAN_AGGREGATOR);
                            System.out.println("*** " + agg.toString() + "**");
                        }
                        break;
                        case "table_full_count":
                            System.out.println("table_full" + ResulSetParser.resultSetToCounter(resultSet).size());
                            break;

                    }
                }

            }

        }

    }

}
