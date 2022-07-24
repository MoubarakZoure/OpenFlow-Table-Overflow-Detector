package ids.statistics.storage;

import fl.rest.ressources.OpenFlowStats.Counters.CounterTable;
import fl.rest.ressources.OpenFlowStats.aggregate.AggregateSQL;
import fl.rest.ressources.OpenFlowStats.flows.FlowEntryStatsSQL;
import fl.rest.ressources.OpenFlowStats.flows.SimpleMatch;
import fl.rest.ressources.OpenFlowStats.flowsRemoved.FlowRemovedTable;
import fl.rest.ressources.OpenFlowStats.packetIns.PacketInsTable;
import fl.rest.ressources.OpenFlowStats.port.PortActivitySQL;
import fl.rest.ressources.OpenFlowStats.tablestats.TableStatsSQL;
import fl.rest.ressources.controller.ControllerMemoryUsageSQL;
import fl.rest.ressources.statcollector.PortBandwidthSQL;
import ids.detection.Alert;
import ids.statistics.Tables;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import machine.learning.svm.training.automatic.StatAggregat;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.apache.commons.pool.impl.GenericObjectPoolFactory;
import stats.entropy.Entropy;
import stats.entropy.FlowEntropy;

public class StatsDBInterfacePooled {

    private Properties properties;
    private DBConnectionProperties dbConnectionProperties = new DBConnectionProperties();
    private ObjectPool mySqlConnectionPool;
    private int poolSize = 10;
    private static final Logger LOG = Logger.getLogger(StatsDBInterfacePooled.class.getName());

    public ResultSet retrieveSwitchFlowsStats(String dpid, long time_stamp_min, long time_stamp_max) {
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            String WHERE_CONDITION = " where (dpid = ?  and time_stamp >= ? and time_stamp < ?)";
            connection = (Connection) mySqlConnectionPool.borrowObject();
            statement = connection.prepareStatement("select time_stamp,packetCount from flows_table" + WHERE_CONDITION);
            int i = 0;

            statement.setString(++i, dpid);
            // statement.setString(++i, tableId);
            statement.setLong(++i, time_stamp_min);
            statement.setLong(++i, time_stamp_max);
            ResultSet result = statement.executeQuery();
            safeClose(connection);
            return result;

        } catch (Exception ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
            safeClose(statement, connection);
            return null;
        }

    }

    public ResultSet retrieveMinAndMax(String dpid, String tableId) {
        PreparedStatement statement = null;
        Connection connection = null;
        try {

            connection = (Connection) mySqlConnectionPool.borrowObject();
            statement = connection.prepareStatement("select min(time_stamp) as time_stamp_min,max(time_stamp) as time_stamp_max from tableStats_table"
                    + " where dpid=? and tableId=?");
            int i = 0;

            statement.setString(++i, dpid);
            statement.setString(++i, tableId);
            ResultSet result = statement.executeQuery();
            safeClose(connection);
            return result;

        } catch (Exception ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
            safeClose(statement, connection);
            return null;
        }

    }

    public ResultSet retrieveSwitchTableStats(String dpid, String tableId, long time_stamp_min, long time_stamp_max) {
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            String WHERE_CONDITION = " where (dpid = ? and tableId = ? and time_stamp >= ? and time_stamp < ?)";
            connection = (Connection) mySqlConnectionPool.borrowObject();
            statement = connection.prepareStatement("select time_stamp,activeCount,lookUpCount,matchCount from tableStats_table" + WHERE_CONDITION);
            int i = 0;

            statement.setString(++i, dpid);
            statement.setString(++i, tableId);
            statement.setLong(++i, time_stamp_min);
            statement.setLong(++i, time_stamp_max);
            ResultSet result = statement.executeQuery();
            safeClose(connection);
            return result;

        } catch (Exception ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
            safeClose(statement, connection);
            return null;
        }

    }

    public ResultSet retrieveFlowAggregate(String dpid, long time_stamp_min, long time_stamp_max) {
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            String WHERE_CONDITION = " where (dpid = ? and time_stamp >= ? and time_stamp < ?)";
            connection = (Connection) mySqlConnectionPool.borrowObject();
            statement = connection.prepareStatement("select time_stamp,flowCount from aggregate_flows_table" + WHERE_CONDITION);
            int i = 0;

            statement.setString(++i, dpid);
            statement.setLong(++i, time_stamp_min);
            statement.setLong(++i, time_stamp_max);
            ResultSet result = statement.executeQuery();
            safeClose(connection);
            return result;

        } catch (Exception ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
            safeClose(statement, connection);
            return null;
        }

    }

    public ResultSet retrieveTableFullCounter(String dpid, String tableId, long time_stamp_min, long time_stamp_max) {

        String WHERE_CONDITION = " where (dpid = ? and tableID = ? and time_stamp >= ? and time_stamp < ?)"; // à revoir <=

        PreparedStatement statement = null;
        Connection connection = null;
        try {

            connection = (Connection) mySqlConnectionPool.borrowObject();
            statement = connection.prepareStatement("select * from table_full_count" + WHERE_CONDITION);
            int i = 0;

            statement.setString(++i, dpid);
            statement.setString(++i, tableId);
            statement.setLong(++i, time_stamp_min);
            statement.setLong(++i, time_stamp_max);

            ResultSet result = statement.executeQuery();
            safeClose(connection);
            return result;

        } catch (Exception ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
            safeClose(statement, connection);
            return null;
        }

    }

    public ResultSet retrieveTrainingDataSet() {

        PreparedStatement statement = null;
        Connection connection = null;
        try {

            connection = (Connection) mySqlConnectionPool.borrowObject();
            statement = connection.prepareStatement("select * from training_set");

            ResultSet result = statement.executeQuery();
            safeClose(connection);
            return result;

        } catch (Exception ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
            safeClose(statement, connection);
            return null;
        }

    }

    public StatsDBInterfacePooled(int poolSize) {
        this.poolSize = poolSize;
        createMySqlConnectionPool(poolSize);
    }

    public StatsDBInterfacePooled() {

        createMySqlConnectionPool(poolSize);
    }

    public void createAllViews() {
        Connection connection = null;
        Statement statement = null;

        try {

            connection = (Connection) mySqlConnectionPool.borrowObject();
            statement = connection.createStatement();
            statement.executeUpdate("create or replace view  packet_in_count as select  dpid,tableID,time_stamp,total_count from packetIns_table");
            safeClose(statement);
            statement = connection.createStatement();
            statement.executeUpdate("create or replace view  flow_removed_count as select  dpid,tableID,time_stamp,total_count from flowRemoved_table");

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            safeClose(statement);
            safeClose(connection);

        }

    }

    public String prepareInsertRequest(String tableName, int recordsNumber) {
        String request = null;
        String colums = null;
        String valuesModal = null;
        switch (tableName) {
            case "packetIns_table": {
                colums = "(dpid,tableID,time_stamp,total_count,ip_src,ip_dest,port_src,port_dest,mac_src,mac_dest,vlanID,matching)";
                valuesModal = "(?,?,?,?,?,?,?,?,?,?,?,?)";

            }
            break;

            case "flowRemoved_table": {
                colums = "(dpid,tableID,time_stamp,total_count,matching,match_hash,duration_sec,"
                        + "duration_nsec,byteCount,packetCount,idleTimeout,hardTimeout)";
                valuesModal = "(?,?,?,?,?,?,?,?,?,?,?,?)";

            }
            break;

            case "table_full_count": {
                colums = "(dpid,tableID,time_stamp,total_count)";
                valuesModal = "(?,?,?,?)";
            }
            break;

            case "packet_in_count": {
                colums = "(dpid,tableID,time_stamp,total_count)";
                valuesModal = "(?,?,?,?)";
            }
            break;
            case "packet_out_count": {
                colums = "(dpid,tableID,time_stamp,total_count)";
                valuesModal = "(?,?,?,?)";
            }
            break;

            case "flow_removed_count": {
                colums = "(dpid,tableID,time_stamp,total_count)";
                valuesModal = "(?,?,?,?)";
            }
            break;
            case "aggregate_flows_table": {
                colums = "(dpid,time_stamp,byteCount,flags,flowCount,packetCount,version)";
                valuesModal = "(?,?,?,?,?,?,?)";
            }
            break;
            case "flows_table": {
                colums = "(dpid,tableID,time_stamp,byteCount,cookie,durationNSeconds, durationSeconds, flags,hardTimeoutSec,idleTimeoutSec, packetCount,"
                        + "priority,instructions,ip_src,ip_dest, port_src,port_dest,mac_src,mac_dest,vlanID,matching)";
                valuesModal = "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }
            break;
            case "switch_ports_activities_table": {
                colums = "(dpid,time_stamp,collisions,durationNsec,durationSec,portNumber,receiveBytes,receiveCRCErrors,"
                        + "receiveDropped,receiveErrors,receiveFrameErrors,receiveOverrunErrors,receivePackets,transmitBytes,transmitDropped,"
                        + "transmitErrors, transmitPackets)";
                valuesModal = "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }
            break;
            case "tableStats_table": {
                colums = "(dpid,time_stamp,activeCount,lookUpCount,matchCount,tableId,version)";
                valuesModal = "(?,?,?,?,?,?,?)";
            }
            break;
            case "port_bandWidth_table": {
                colums = "(dpid,time_stamp,rx,tx,port_,updatedTime)";
                valuesModal = "(?,?,?,?,?,?)";
            }
            break;
            case "controlerMemoryUsage_table": {
                colums = "(time_stamp,total,free)";
                valuesModal = "(?,?,?)";
            }
            break;

            case "alert": {

                colums = "(alert_id,switch_name,detection_time,attack_min_window,attack_max_window,attack_name,table_mean_occupation,matching_indication,table_full_count,probability,missFlowCount,missFlowRate)";
                valuesModal = "(?,?,?,?,?,?,?,?,?,?,?,?)";

            }
            break;

            case "training_set": {

                colums = "(id ,table_full_count , missFlowCount , missFlowRate , lookUpRate , matchRate ,occupation_average ,lookUpCount , matchCount,trafficType)";
                valuesModal = "(?,?,?,?,?,?,?,?,?,?)";

            }
            break;

            default:
                return null;

        }
        String values = "";
        for (int i = 1; i <= recordsNumber; i++) {

            values += valuesModal + ",";

        }

        values = values.substring(0, values.length() - 1);
        request = "insert into " + tableName + " " + colums + " values " + values;

        return request;

    }

    public PreparedStatement get_InsertPreparedStatement(String tableName, int recordsNumber) {
        Connection connection = null;

        try {
            connection = (Connection) mySqlConnectionPool.borrowObject();
            return connection.prepareStatement(this.prepareInsertRequest(tableName, recordsNumber));
        } catch (SQLException ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }


    /*public ResultSet selectFromTable(String tableName, String dpid, String tableID, long time_stamp_min, long time_stamp_max) {
        PreparedStatement statement = get_SelectPreparedStatement(tableName);
        try {
            statement.setString(1, dpid);
            statement.setString(2, tableID);
            statement.setLong(3, time_stamp_min);
            statement.setLong(4, time_stamp_max);
            ResultSet result = statement.executeQuery();

            return result;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }*/
    public boolean addToTrainingSet(ArrayList<StatAggregat> records, String trafficType) {

        PreparedStatement statement = get_InsertPreparedStatement("training_set", records.size());
        Connection connection = null;
        int i = 0;
        try {
            connection = statement.getConnection();
            for (StatAggregat aggregat : records) {

                statement.setNull(++i, java.sql.Types.INTEGER);
                statement.setInt(++i, aggregat.getTable_full_cout());
                statement.setLong(++i, aggregat.getMissFlowStats().getMissFlowCount());
                statement.setDouble(++i, aggregat.getMissFlowStats().getMissFlowRate());
                statement.setDouble(++i, aggregat.getTableStatAggregat().getLookUpRate());
                statement.setDouble(++i, aggregat.getTableStatAggregat().getMatchRate());
                statement.setDouble(++i, aggregat.getTableStatAggregat().getOccupation_average());
                statement.setInt(++i, aggregat.getTableStatAggregat().getLookUpCount());
                statement.setInt(++i, aggregat.getTableStatAggregat().getMatchCount());
                statement.setString(++i, trafficType);
            }
            statement.executeUpdate();
            safeClose(statement, connection);
            return true;

        } catch (SQLException ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
            safeClose(statement, connection);
            return false;
        }

    }

    public boolean addToAlert(ArrayList<Alert> records) {

        PreparedStatement statement = get_InsertPreparedStatement("alert", records.size());
        Connection connection = null;
        int i = 0;
        try {
            connection = statement.getConnection();
            for (Alert alert : records) {

                statement.setNull(++i, java.sql.Types.INTEGER);
                statement.setString(++i, alert.getSwitch_name());
                statement.setTimestamp(++i, alert.getDetection_time());
                statement.setTimestamp(++i, alert.getAttack_min_window());
                statement.setTimestamp(++i, alert.getAttack_max_window());
                statement.setString(++i, alert.getAttack_name());
                statement.setDouble(++i, alert.getTable_mean_occupation());
                statement.setDouble(++i, alert.getMatching_indication());
                statement.setDouble(++i, alert.getTable_full_count());
                statement.setDouble(++i, alert.getProbability());
                statement.setLong(++i, alert.getMissFlowCount());
                statement.setDouble(++i, alert.getMissFlowRate());
               
               

            }
            statement.executeUpdate();
            safeClose(statement, connection);
            return true;

        } catch (SQLException ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
            safeClose(statement, connection);
            return false;
        }

    }

    public boolean addToPacket_inTable(ArrayList<PacketInsTable> records) {

        PreparedStatement statement = get_InsertPreparedStatement("packetIns_table", records.size());
        Connection connection = null;

        int i = 0;

        try {
            connection = statement.getConnection();
            for (PacketInsTable p : records) {
                i = i + 1;
                statement.setString(i, p.getDpid());
                i = i + 1;
                statement.setString(i, p.getTableID());
                i = i + 1;
                statement.setLong(i, p.getTime_stamp());
                i = i + 1;
                statement.setLong(i, p.getTotal_count());
                i = i + 1;
                statement.setString(i, p.getIp_src());
                i = i + 1;
                statement.setString(i, p.getIp_dest());
                i = i + 1;
                statement.setInt(i, p.getPort_src());
                i = i + 1;
                statement.setInt(i, p.getPort_dest());
                i = i + 1;
                statement.setString(i, p.getMac_src());
                i = i + 1;
                statement.setString(i, p.getMac_dest());
                i = i + 1;
                statement.setInt(i, p.getVlanID());
                i = i + 1;
                statement.setString(i, p.getMatching());
                // System.out.println(p);

            }

            statement.executeUpdate();
            safeClose(statement, connection);
            return true;
        } catch (SQLException e) {

            safeClose(statement, connection);

        }

        return false;

    }

    public boolean addToFlowRemovedTable(ArrayList<FlowRemovedTable> records) {

        PreparedStatement statement = get_InsertPreparedStatement("flowRemoved_table", records.size());
        Connection connection = null;
        try {
            int i = 0;
            connection = statement.getConnection();

            for (FlowRemovedTable r : records) {
                statement.setString(++i, r.getDpid());
                statement.setString(++i, r.getTableID());
                statement.setLong(++i, r.getTime_stamp());
                statement.setLong(++i, r.getTotal_count());
                statement.setString(++i, r.getMatching());
                statement.setString(++i, r.getMatch_hash());
                statement.setInt(++i, r.getDuration_sec());
                statement.setLong(++i, r.getDuration_nsec());
                statement.setInt(++i, r.getByteCount());
                statement.setInt(++i, r.getPacketCount());
                statement.setInt(++i, r.getIdleTimeout());
                statement.setInt(++i, r.getHardTimeout());

            }

            statement.executeUpdate();
            safeClose(statement, connection);

            return true;

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            safeClose(statement, connection);
            return false;
        }

    }

    public boolean addToCounter(String tableName, ArrayList<CounterTable> records) {

        PreparedStatement statement = get_InsertPreparedStatement(tableName, records.size());
        Connection connection = null;

        try {
            int i = 0;
            connection = statement.getConnection();
            for (CounterTable r : records) {

                statement.setString(++i, r.getDpid());
                statement.setString(++i, r.getTableID());
                statement.setLong(++i, r.getTime_stamp());
                statement.setLong(++i, r.getTotal_count());

            }

            statement.executeUpdate();
            safeClose(statement, connection);

            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            safeClose(statement, connection);
            return false;
        }

    }

    public boolean addToAggregateFlowsTable(ArrayList<AggregateSQL> records) {
        PreparedStatement statement = get_InsertPreparedStatement("aggregate_flows_table", records.size());
        Connection connection = null;

        try {
            int i = 0;
            connection = statement.getConnection();
            for (AggregateSQL r : records) {

                statement.setString(++i, r.getDpid());
                statement.setLong(++i, r.getTime_stamp());
                statement.setLong(++i, r.getByteCount());
                statement.setInt(++i, r.getFlags());
                statement.setInt(++i, r.getFlowCount());
                statement.setLong(++i, r.getPacketCount());
                statement.setString(++i, r.getVersion());

            }

            statement.executeUpdate();
            safeClose(statement, connection);

            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            safeClose(statement, connection);
            return false;
        }

    }

    public boolean addToFlowsTable(ArrayList<FlowEntryStatsSQL> records) {
        PreparedStatement statement = get_InsertPreparedStatement("flows_table", records.size());
        Connection connection = null;

        try {
            int i = 0;

            connection = statement.getConnection();

            int j = 0;
            String ip_src = null, ip_dest = null;
            Integer port_src = null, port_dest = null;

            for (FlowEntryStatsSQL r : records) {

                statement.setString(++i, r.getDpid());
                statement.setString(++i, r.getTableId());
                statement.setLong(++i, r.getTime_stamp());
                statement.setLong(++i, r.getByteCount());
                statement.setString(++i, r.getCookie());
                statement.setLong(++i, r.getDurationNSeconds());
                statement.setLong(++i, r.getDurationSeconds());
                statement.setInt(++i, r.getFlags());
                statement.setInt(++i, r.getHardTimeoutSec());
                statement.setInt(++i, r.getIdleTimeoutSec());
                statement.setLong(++i, r.getPacketCount());
                statement.setInt(++i, r.getPriority());

                statement.setString(++i, null);

                statement.setString(++i, ip_src);

                statement.setString(++i, ip_dest);

                statement.setInt(++i, -1);
                statement.setInt(++i, -1);

                statement.setString(++i, null);

                statement.setString(++i, null);

                Integer vlan_id = null;
                if (vlan_id == null) {
                    vlan_id = -1;
                }

                statement.setInt(++i, vlan_id);

                //  statement.setString(++i, toFlowMatch(ip_src, ip_dest,  port_src, port_dest));
                statement.setString(++i, null);
                System.out.println("*****************");

            }

            statement.executeUpdate();
            safeClose(statement, connection);

            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            safeClose(statement, connection);
            return false;
        }

    }

    public boolean addToPortActivityTable(ArrayList<PortActivitySQL> records) {
        PreparedStatement statement = get_InsertPreparedStatement("switch_ports_activities_table", records.size());
        Connection connection = null;

        try {
            int i = 0;
            connection = statement.getConnection();
            for (PortActivitySQL r : records) {

                statement.setString(++i, r.getDpid());
                statement.setLong(++i, r.getTime_stamp());
                statement.setLong(++i, r.getCollisions());
                statement.setLong(++i, r.getDurationNsec());
                statement.setLong(++i, r.getDurationSec());
                statement.setString(++i, r.getPortNumber());
                statement.setLong(++i, r.getReceiveBytes());
                statement.setLong(++i, r.getReceiveCRCErrors());
                statement.setLong(++i, r.getReceiveDropped());
                statement.setLong(++i, r.getReceiveErrors());
                statement.setLong(++i, r.getReceiveFrameErrors());
                statement.setLong(++i, r.getReceiveOverrunErrors());
                statement.setLong(++i, r.getReceivePackets());
                statement.setLong(++i, r.getTransmitBytes());
                statement.setLong(++i, r.getTransmitDropped());
                statement.setLong(++i, r.getTransmitErrors());
                statement.setLong(++i, r.getTransmitPackets());

            }

            statement.executeUpdate();
            safeClose(statement, connection);

            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            safeClose(statement, connection);
            return false;
        }

    }

    public boolean addToTableStatsTable(ArrayList<TableStatsSQL> records) {
        PreparedStatement statement = get_InsertPreparedStatement("tableStats_table", records.size());
        Connection connection = null;

        try {
            int i = 0;
            connection = statement.getConnection();

            for (TableStatsSQL r : records) {

                statement.setString(++i, r.getDpid());
                statement.setLong(++i, r.getTime_stamp());
                statement.setInt(++i, r.getActiveCount());
                statement.setInt(++i, r.getLookUpCount());
                statement.setInt(++i, r.getMatchCount());
                statement.setString(++i, r.getTableID());
                statement.setString(++i, r.getVersion());

            }

            statement.executeUpdate();
            safeClose(statement, connection);
            System.out.println("*****************");
            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            safeClose(statement, connection);
            return false;
        }

    }

    public boolean addToPortBandwidthTable(ArrayList<PortBandwidthSQL> records) {
        PreparedStatement statement = get_InsertPreparedStatement("port_bandWidth_table", records.size());
        Connection connection = null;

        try {
            int i = 0;
            connection = statement.getConnection();

            for (PortBandwidthSQL r : records) {

                statement.setString(++i, r.getDpid());
                statement.setLong(++i, r.getTime_stamp());
                statement.setLong(++i, r.getRx());
                statement.setLong(++i, r.getTx());
                statement.setString(++i, r.getPort());
                statement.setString(++i, r.getUpdatedTime());

            }

            statement.executeUpdate();
            safeClose(statement, connection);

            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            safeClose(statement, connection);
            return false;
        }

    }

    public boolean addToControlerMemoryUsage(ArrayList<ControllerMemoryUsageSQL> records) {
        PreparedStatement statement = get_InsertPreparedStatement("controlerMemoryUsage_table", records.size());
        Connection connection = null;

        try {
            connection = statement.getConnection();
            int i = 0;

            for (ControllerMemoryUsageSQL r : records) {

                statement.setLong(++i, r.getTime_stamp());
                statement.setLong(++i, r.getTotal());
                statement.setLong(++i, r.getFree());

            }

            statement.executeUpdate();

            safeClose(statement, connection);

            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            safeClose(statement, connection);
            return false;
        }

    }

    public void cleanDB(long time_stamp) {
        Statement statement = null;
        Connection connection = null;

        try {

            for (String table : Tables.tables_list) {

                connection = (Connection) mySqlConnectionPool.borrowObject();

                statement = connection.createStatement();
                statement.executeUpdate("delete  from " + table + " where (time_stamp <= " + time_stamp + ")");
                safeClose(statement, connection);

            }

            //LOG.info("Clearing all tables content from IDS statistics database  : " + "done succesfully !!");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

        }

    }

    public void clearAllTables() {
        Statement statement = null;
        Connection connection = null;

        try {

            for (String table : Tables.tables_list) {

                connection = (Connection) mySqlConnectionPool.borrowObject();

                statement = connection.createStatement();
                statement.executeUpdate("delete  from " + table + " where (time_stamp > -1)");
                safeClose(statement, connection);

            }

            LOG.info("Clearing all tables content from IDS statistics database  : " + "done succesfully !!");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

        }

    }

    public static void main(String[] args) throws InterruptedException {

        String result = getSqlAggregateParam(MySqlTableFields.TABLE_STATS, MySqlFuntions.GEOMETRIC_MEAN_FUNCTION);
        // System.out.println(result);
        //  System.exit(0);

        System.out.println(Entropy.Log2(0.0221005802) * 0.0221005802);

        StatsDBInterfacePooled dbInterface = new StatsDBInterfacePooled();

        long x = new Long("20000000000000");

        ResultSet set = dbInterface.selectEntropy("packetInsTable", "00:00:00:00:00:00:00:01", "0x0", 0, x, true);
        System.out.println(ResultSetParser.ResulSetParser.parseToEntropy(set));
        ResultSet set2 = dbInterface.selectFromTable("packetIns_table", "00:00:00:00:00:00:00:01", "0x0", 0, x, 1);
        ArrayList<SimpleMatch> matchs = ResultSetParser.ResulSetParser.resultSetToPacketInsMatches(set2);
        System.out.println(FlowEntropy.computeFlowEntropy(matchs, false));
        System.out.println();
        dbInterface.get_AggregationPreparedStatement("tableStats_table", true, 0, MySqlFuntions.MEAN_FUNCTION);
        DBConnectionProperties db = new DBConnectionProperties();

        ObjectPool pool = dbInterface.createMySqlConnectionPool(20);

        String[] table_list = new String[]{"packet_in_count", "packet_out_count", "table_full_count",
            "flow_removed_count", "packetIns_table", "flowRemoved_table", "flows_table", "aggregate_flows_table",
            "flows_table", "switch_ports_activities_table", "tableStats_table", "port_bandWidth_table",
            "controlerMemoryUsage_table"};
        ResultSet resultSet = null;

        for (String table : table_list) {
            if (table.equals("aggregate_flows_table") || table.equals("switch_ports_activities_table") || table.equals("port_bandWidth_table")) {
                resultSet = dbInterface.aggregateTable(table, "all", 0, Long.MAX_VALUE, 1, MySqlFuntions.MEAN_FUNCTION);

            } else {
                if (table.equals("controlerMemoryUsage_table")) {
                    resultSet = dbInterface.aggregateTable(table, 0, Long.MAX_VALUE, 1, MySqlFuntions.MEAN_FUNCTION);

                } else {
                    resultSet = dbInterface.aggregateTable(table, "all", "0x0", 0, Long.MAX_VALUE, 1, MySqlFuntions.MEAN_FUNCTION);
                }

            }

            switch (table) {
                case "packet_in_count":

                    break;

                case "packetIns_table":

                    break;

                case "flow_removed_count":

                    break;
                case "flowRemoved_table":
                    System.out.println(ResultSetParser.ResulSetParser.resultSetToFlowRemoved(resultSet).toString());
                    System.out.println("******************");

                    break;
                case "table_full_count":
                    System.out.println(ResultSetParser.ResulSetParser.resultSetToCounter(resultSet).toString());
                    System.out.println("******************");
                    break;
                case "packet_out_count":

                    break;
                case "flows_table":
                    System.out.println(ResultSetParser.ResulSetParser.resultSetToFlowsEntryStats(resultSet).toString());
                    System.out.println("******************");

                    break;
                case "aggregate_flows_table": {
                    System.out.println(ResultSetParser.ResulSetParser.resultSetToAggregate(resultSet).toString());
                    System.out.println("******************");
                    break;

                }
                case "switch_ports_activities_table": {
                    System.out.println(ResultSetParser.ResulSetParser.resultSetToPortActivitys(resultSet).toString());
                    System.out.println("******************");

                    break;
                }
                case "tableStats_table": {

                    System.out.println(ResultSetParser.ResulSetParser.resultSetToTableStats(resultSet).toString());
                    System.out.println("******************");

                    break;
                }

                case "port_bandWidth_table": {

                    System.out.println(ResultSetParser.ResulSetParser.resultSetToPortBandwidths(resultSet).toString());
                    System.out.println("******************");

                    break;

                }
                case "controlerMemoryUsage_table":

                    System.out.println(ResultSetParser.ResulSetParser.resultSetToControlerMemoryUsage(resultSet).toString());
                    System.out.println("******************");
                    break;

            }

        }

    }

    private void safeClose(Statement st, Connection conn) {
        safeClose(st);
        safeClose(conn);

    }

    private void safeClose(Connection conn) {
        if (conn != null) {
            try {
                mySqlConnectionPool.returnObject(conn);
            } catch (Exception e) {
                LOG.info("Failed to return the connection to the pool " + e);
            }
        }
    }

    private void safeClose(ResultSet res) {
        if (res != null) {
            try {
                res.close();
            } catch (SQLException e) {
                LOG.info("Failed to close databse resultset " + e);
            }
        }
    }

    private void safeClose(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                LOG.info("Failed to close databse statment " + e);
            }
        }
    }

    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", dbConnectionProperties.getUser());
            properties.setProperty("password", dbConnectionProperties.getPassword());
            properties.setProperty("MaxPooledStatements", dbConnectionProperties.getMaxPooledStatements() + "");
        }
        return properties;
    }

    public ObjectPool createMySqlConnectionPool(int poolSize) {

        PoolableObjectFactory mySqlPoolableObjectFactory = new MySqlPoolableObjectFactory(dbConnectionProperties);
        Config config = new GenericObjectPool.Config();
        config.maxActive = poolSize;
        config.testOnBorrow = true;
        config.testWhileIdle = true;
        config.timeBetweenEvictionRunsMillis = 60000;
        config.minEvictableIdleTimeMillis = 60000;
        GenericObjectPoolFactory genericObjectPoolFactory = new GenericObjectPoolFactory(mySqlPoolableObjectFactory, config);
        this.mySqlConnectionPool = genericObjectPoolFactory.createPool();
        LOG.info("Pool de " + poolSize + " connections SQL crée");

        return this.mySqlConnectionPool;

    }

    public void clearPool() {
        try {
            this.mySqlConnectionPool.clear();
            this.mySqlConnectionPool.close();
        } catch (Exception ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ResultSet selectEntropy(String tableName, String dpid, String tableID, long min_val, long max_val, boolean isTimeStamp) {
        PreparedStatement statement = null;
        Connection connection = null;
        statement = get_EntropyPreparedStatement(tableName, isTimeStamp);
        int i = 0;
        try {
            connection = statement.getConnection();
            statement.setString(++i, dpid);
            statement.setString(++i, tableID);
            statement.setLong(++i, min_val);
            statement.setLong(++i, max_val);
            return statement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public PreparedStatement get_EntropyPreparedStatement(String tableName, boolean isTimeStamp) {

        try {
            Connection connection = (Connection) mySqlConnectionPool.borrowObject();
            if (isTimeStamp) {

                return connection.prepareStatement("select flow_match_entropy(?,?,?,?) as entropy");

            } else {
                return null;

            }
        } catch (Exception ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public ResultSet selectFromTable(String tableName, String dpid, String tableID, long time_stamp_min, long time_stamp_max, int isTimeStamp) {
        PreparedStatement statement = null;
        Connection connection = null;
        if (dpid.equals("all")) {

            statement = get_SelectPreparedStatement(tableName, true, isTimeStamp);

        } else {

            statement = get_SelectPreparedStatement(tableName, false, isTimeStamp);

        }
        try {
            connection = statement.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
        }

        int i = 0;

        try {

            if (!dpid.equals("all")) {
                statement.setString(++i, dpid);
            }
            statement.setString(++i, tableID);
            statement.setLong(++i, time_stamp_min);
            statement.setLong(++i, time_stamp_max);
            ResultSet result = statement.executeQuery();

            // safeClose(statement, connection);
            return result;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            safeClose(statement, connection);
            return null;
        }

    }

    public ResultSet selectFromTable(String tableName, String dpid, long time_stamp_min, long time_stamp_max, int isTimeStamp) {
        PreparedStatement statement = null;
        Connection connection = null;
        if (dpid.equals("all")) {

            statement = get_SelectPreparedStatement(tableName, true, isTimeStamp);

        } else {

            statement = get_SelectPreparedStatement(tableName, false, isTimeStamp);

        }
        try {
            connection = statement.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
        }

        int i = 0;

        try {

            if (!dpid.equals("all")) {
                statement.setString(++i, dpid);
            }

            statement.setLong(++i, time_stamp_min);
            statement.setLong(++i, time_stamp_max);
            ResultSet result = statement.executeQuery();

            // safeClose(statement, connection);
            return result;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            safeClose(statement, connection);
            return null;
        }

    }

    public ResultSet selectFromTable(String tableName, long time_stamp_min, long time_stamp_max, int isTimeStamp) {
        PreparedStatement statement = null;
        Connection connection = null;

        statement = get_SelectPreparedStatement(tableName, false, isTimeStamp);

        try {
            connection = statement.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
        }

        int i = 0;

        try {

            statement.setLong(++i, time_stamp_min);
            statement.setLong(++i, time_stamp_max);
            ResultSet result = statement.executeQuery();

            // safeClose(statement, connection);
            return result;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            safeClose(statement, connection);
            return null;
        }

    }

    public PreparedStatement get_SelectPreparedStatement(String tableName, boolean isAllDPID, int isTimeStamp) { // 1--> is time_stamp 0 --> total_count

        String WHERE_CONDITION = " where (dpid = ? and tableID = ? and time_stamp >= ? and time_stamp <= ?) order by time_stamp DESC ";

        String WHERE_CONDITION2 = " where (tableID = ? and time_stamp >= ? and time_stamp <= ?) order by time_stamp DESC ";

        if (isAllDPID) {

            WHERE_CONDITION = WHERE_CONDITION2;
        }

        if (isTimeStamp == 0) {
            WHERE_CONDITION = WHERE_CONDITION.replaceAll("time_stamp", "total_count");

        }

        try {
            Connection connection = (Connection) mySqlConnectionPool.borrowObject();

            switch (tableName) {
                case "packet_in_count":
                    return connection.prepareStatement("select * from packet_in_count" + WHERE_CONDITION);

                case "packetIns_table":

                    return connection.prepareStatement("select * from packetIns_table" + WHERE_CONDITION);
                case "packetIns_table2":

                    return connection.prepareStatement("select matching,time_stamp from packetIns_table" + WHERE_CONDITION);
                case "flow_removed_count":
                    return connection.prepareStatement("select * from flow_removed_count" + WHERE_CONDITION);
                case "flowRemoved_table":
                    return connection.prepareStatement("select * from flowRemoved_table" + WHERE_CONDITION);
                case "flowRemoved_table_packetCount":
                    return connection.prepareStatement("select matching,time_stamp,packetCount from flowRemoved_table" + WHERE_CONDITION);
                case "table_full_count":
                    return connection.prepareStatement("select * from table_full_count" + WHERE_CONDITION);
                case "packet_out_count":
                    return connection.prepareStatement("select * from packet_out_count" + WHERE_CONDITION);
                case "flows_table":
                    return connection.prepareStatement("select * from flows_table" + WHERE_CONDITION);
                case "flows_table_packetCount":

                    return connection.prepareStatement("select matching,time_stamp,packetCount from flows_table" + WHERE_CONDITION);
                case "aggregate_flows_table": {
                    if (!isAllDPID) {
                        WHERE_CONDITION = " where (dpid = ?  and time_stamp >= ? and time_stamp <= ?) order by time_stamp DESC ";
                    } else {

                        WHERE_CONDITION = " where (time_stamp >= ? and time_stamp <= ?) order by time_stamp DESC ";
                    }
                    if (isTimeStamp == 0) {
                        WHERE_CONDITION = WHERE_CONDITION.replaceAll("time_stamp", "total_count");

                    }

                    return connection.prepareStatement("select * from aggregate_flows_table" + WHERE_CONDITION);

                }
                case "switch_ports_activities_table": {
                    if (!isAllDPID) {
                        WHERE_CONDITION = " where (dpid = ?  and time_stamp >= ? and time_stamp <= ?) order by time_stamp DESC ";
                    } else {

                        WHERE_CONDITION = " where (time_stamp >= ? and time_stamp <= ?) order by time_stamp DESC ";
                    }

                    if (isTimeStamp == 0) {
                        WHERE_CONDITION = WHERE_CONDITION.replaceAll("time_stamp", "total_count");

                    }
                    return connection.prepareStatement("select * from switch_ports_activities_table" + WHERE_CONDITION);
                }
                case "tableStats_table": {

                    return connection.prepareStatement("select * from tableStats_table" + WHERE_CONDITION);
                }

                case "port_bandWidth_table": {
                    if (!isAllDPID) {
                        WHERE_CONDITION = " where (dpid = ?  and time_stamp >= ? and time_stamp <= ?) order by time_stamp DESC ";
                    } else {

                        WHERE_CONDITION = " where (time_stamp >= ? and time_stamp <= ?) order by time_stamp DESC ";
                    }

                    if (isTimeStamp == 0) {
                        WHERE_CONDITION = WHERE_CONDITION.replaceAll("time_stamp", "total_count");

                    }

                    return connection.prepareStatement("select * from port_bandWidth_table" + WHERE_CONDITION);
                }
                case "controlerMemoryUsage_table": {

                    WHERE_CONDITION = " where (time_stamp >= ? and time_stamp <= ?) order by time_stamp DESC ";

                    if (isTimeStamp == 0) {
                        WHERE_CONDITION = WHERE_CONDITION.replaceAll("time_stamp", "total_count");

                    }

                    return connection.prepareStatement("select * from controlerMemoryUsage_table" + WHERE_CONDITION);
                }

                default:
                    return null;

            }

        } catch (SQLException ex) {

            return null;

        } catch (Exception ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public PreparedStatement get_AggregationPreparedStatement(String tableName, boolean isAllDPID, int isTimeStamp, String aggregator) { // 1--> is time_stamp 0 --> total_count

        String WHERE_CONDITION = " where (dpid = ? and tableID = ? and time_stamp >= ? and time_stamp <= ?) group by dpid ";

        String WHERE_CONDITION2 = " where (tableID = ? and time_stamp >= ? and time_stamp <= ?) group by dpid ";

        if (isAllDPID) {

            WHERE_CONDITION = WHERE_CONDITION2;
        }

        if (isTimeStamp == 0) {
            WHERE_CONDITION = WHERE_CONDITION.replaceAll("time_stamp", "total_count");

        }

        try {
            Connection connection = (Connection) mySqlConnectionPool.borrowObject();
            String field = null;

            switch (tableName) {
                case "packet_in_count":
                    field = getSqlAggregateParam(MySqlTableFields.SIMPLE_COUNTER, aggregator);
                    break;

                case "packetIns_table":
                    return connection.prepareStatement("select * from packetIns_table" + WHERE_CONDITION);
                case "flow_removed_count":
                    field = getSqlAggregateParam(MySqlTableFields.SIMPLE_COUNTER, aggregator);
                    break;
                case "flowRemoved_table":
                    field = getSqlAggregateParam(MySqlTableFields.FLOW_REMOVED, aggregator);
                    break;
                case "table_full_count":
                    field = getSqlAggregateParam(MySqlTableFields.SIMPLE_COUNTER, aggregator);
                    break;
                case "packet_out_count":
                    field = getSqlAggregateParam(MySqlTableFields.SIMPLE_COUNTER, aggregator);
                    break;
                case "flows_table":
                    field = getSqlAggregateParam(MySqlTableFields.FLOWS_TABLE, aggregator);
                    break;
                case "aggregate_flows_table": {
                    if (!isAllDPID) {
                        WHERE_CONDITION = " where (dpid = ?  and time_stamp >= ? and time_stamp <= ?) group by dpid ";
                    } else {

                        WHERE_CONDITION = " where (time_stamp >= ? and time_stamp <= ?) group by dpid";
                    }
                    if (isTimeStamp == 0) {
                        WHERE_CONDITION = WHERE_CONDITION.replaceAll("time_stamp", "total_count");

                    }

                    field = getSqlAggregateParam(MySqlTableFields.AGGREGATE_FLOW_TABLE, aggregator);
                    break;

                }
                case "switch_ports_activities_table": {
                    if (!isAllDPID) {
                        WHERE_CONDITION = " where (dpid = ?  and time_stamp >= ? and time_stamp <= ?) group by dpid ";
                    } else {

                        WHERE_CONDITION = " where (time_stamp >= ? and time_stamp <= ?) group by dpid ";
                    }

                    if (isTimeStamp == 0) {
                        WHERE_CONDITION = WHERE_CONDITION.replaceAll("time_stamp", "total_count");

                    }
                    field = getSqlAggregateParam(MySqlTableFields.SWITCH_PORT_ACTIVITY, aggregator);

                    break;
                }
                case "tableStats_table": {

                    field = getSqlAggregateParam(MySqlTableFields.TABLE_STATS, aggregator);
                    break;
                }

                case "port_bandWidth_table": {
                    if (!isAllDPID) {
                        WHERE_CONDITION = " where (dpid = ?  and time_stamp >= ? and time_stamp <= ?) group by dpid ";
                    } else {

                        WHERE_CONDITION = " where (time_stamp >= ? and time_stamp <= ?) group by dpid ";
                    }

                    if (isTimeStamp == 0) {
                        WHERE_CONDITION = WHERE_CONDITION.replaceAll("time_stamp", "total_count");

                    }

                    field = getSqlAggregateParam(MySqlTableFields.PORT_BANDWIDTH_TABLE, aggregator);
                    break;

                }
                case "controlerMemoryUsage_table": {

                    WHERE_CONDITION = " where (time_stamp >= ? and time_stamp <= ?) ";

                    if (isTimeStamp == 0) {
                        WHERE_CONDITION = WHERE_CONDITION.replaceAll("time_stamp", "total_count");

                    }

                    field = getSqlAggregateParam(MySqlTableFields.CONTROLER_MEMORY_USAGE, aggregator);
                    break;

                }

                default:
                    return null;

            }

            String sqlRequest = "select dpid, " + field + " from " + tableName + " " + WHERE_CONDITION;

            if (tableName.equals("controlerMemoryUsage_table")) {
                sqlRequest = sqlRequest.replaceAll("dpid,", "");

            }

            // System.out.println(sqlRequest);
            return connection.prepareStatement(sqlRequest);

            //   return connection.prepareStatement("select * from packetIns_table" + WHERE_CONDITION);
        } catch (SQLException ex) {

            return null;

        } catch (Exception ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public StatsDBInterfacePooled(Properties properties, ObjectPool mySqlConnectionPool) {
        this.properties = properties;
        this.mySqlConnectionPool = mySqlConnectionPool;
    }

    public DBConnectionProperties getDbConnectionProperties() {
        return dbConnectionProperties;
    }

    public void setDbConnectionProperties(DBConnectionProperties dbConnectionProperties) {
        this.dbConnectionProperties = dbConnectionProperties;
    }

    public ObjectPool getMySqlConnectionPool() {
        return mySqlConnectionPool;
    }

    public void setMySqlConnectionPool(ObjectPool mySqlConnectionPool) {
        this.mySqlConnectionPool = mySqlConnectionPool;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public static String getSqlAggregateParam(String[] tableFieldsList, String aggregatorFunction) {
        String result = "";

        for (String field : tableFieldsList) {
            result = result + "," + aggregatorFunction.replaceAll("param", field) + " as " + field;

        }

        return result.substring(1);
    }

    public ResultSet aggregateTable(String tableName, String dpid, String tableID, long time_stamp_min, long time_stamp_max, int isTimeStamp, String aggregator) {
        PreparedStatement statement = null;
        Connection connection = null;
        if (dpid.equals("all")) {

            statement = get_AggregationPreparedStatement(tableName, true, isTimeStamp, aggregator);

        } else {

            statement = get_AggregationPreparedStatement(tableName, false, isTimeStamp, aggregator);

        }
        try {
            connection = statement.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
        }

        int i = 0;

        try {

            if (!dpid.equals("all")) {
                statement.setString(++i, dpid);
            }
            statement.setString(++i, tableID);
            statement.setLong(++i, time_stamp_min);
            statement.setLong(++i, time_stamp_max);
            ResultSet result = statement.executeQuery();

            // safeClose(statement, connection);
            return result;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            safeClose(statement, connection);
            return null;
        }

    }

    public ResultSet aggregateTable(String tableName, String dpid, long time_stamp_min, long time_stamp_max, int isTimeStamp, String aggregator) {
        PreparedStatement statement = null;
        Connection connection = null;
        if (dpid.equals("all")) {

            statement = get_AggregationPreparedStatement(tableName, true, isTimeStamp, aggregator);

        } else {

            statement = get_AggregationPreparedStatement(tableName, false, isTimeStamp, aggregator);

        }
        try {
            connection = statement.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
        }

        int i = 0;

        try {

            if (!dpid.equals("all")) {
                statement.setString(++i, dpid);
            }

            statement.setLong(++i, time_stamp_min);
            statement.setLong(++i, time_stamp_max);
            ResultSet result = statement.executeQuery();

            // safeClose(statement, connection);
            return result;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            safeClose(statement, connection);
            return null;
        }

    }

    public ResultSet aggregateTable(String tableName, long time_stamp_min, long time_stamp_max, int isTimeStamp, String aggregator) {
        PreparedStatement statement = null;
        Connection connection = null;

        statement = get_AggregationPreparedStatement(tableName, false, isTimeStamp, aggregator);

        try {
            connection = statement.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(StatsDBInterfacePooled.class.getName()).log(Level.SEVERE, null, ex);
        }

        int i = 0;

        try {

            statement.setLong(++i, time_stamp_min);
            statement.setLong(++i, time_stamp_max);
            ResultSet result = statement.executeQuery();

            // safeClose(statement, connection);
            return result;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            safeClose(statement, connection);
            return null;
        }

    }

    public static String toFlowMatch(String ip_src, String ip_dest, int port_src, int port_dest) {
        String matching = "ip_src=" + ip_src + "|ip_dst=" + ip_dest + "|port_src=" + port_src + "|port_dst=" + port_dest;

        return matching;
    }

}
