package ids.statistics;

import fl.rest.ressources.OpenFlowStats.Counters.CounterTable;
import fl.rest.ressources.OpenFlowStats.aggregate.AggregateSQL;
import fl.rest.ressources.OpenFlowStats.flows.FlowEntryStatsSQL;
import fl.rest.ressources.OpenFlowStats.flowsRemoved.FlowRemovedTable;
import fl.rest.ressources.OpenFlowStats.packetIns.PacketInsTable;
import fl.rest.ressources.OpenFlowStats.port.PortActivitySQL;
import fl.rest.ressources.OpenFlowStats.tablestats.TableStatsSQL;
import fl.rest.ressources.controller.ControllerMemoryUsage;
import fl.rest.ressources.controller.ControllerMemoryUsageSQL;
import fl.rest.ressources.statcollector.PortBandwidthSQL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatsDBInterface {

    private Connection connection = null;
    private Properties properties;

    public void createAllViews() {

        try {
            if (this.connection != null && !this.connection.isClosed()) {

                //String[] list={"packetIns_table","flowRemoved_table"};
                Statement statement = this.connection.createStatement();
                statement.executeUpdate("create view if not exists packet_in_count as select  dpid,tableID,time_stamp,total_count from packetIns_table");
                statement.close();
                statement = this.connection.createStatement();
                statement.executeUpdate("create view if not exists flow_removed_count as select  dpid,tableID,time_stamp,total_count from flowRemoved_table");
                statement.close();

            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", "root");
            properties.setProperty("password", "root");
            properties.setProperty("MaxPooledStatements", "5000");
        }
        return properties;
    }

    public boolean connect(String databaseName) {

        String url = "jdbc:mysql://localhost:3306/" + databaseName;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, getProperties());
            //logger.info("Connexion à la base de données "+url+" réussie");

            if (this.connection != null) {
                this.connection.setAutoCommit(true);
                return true;
            } else {
                return false;
            }

        } catch (SQLException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    public boolean closeConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {

                this.connection.close();

                return true;

            } else {

                return false;

            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return false;
        }

    }

    public void clearAllTables() {

        try {
            if (this.connection != null && !this.connection.isClosed()) {

                for (String table : Tables.tables_list) {
                    Statement statement = this.connection.createStatement();
                    statement.executeUpdate("delete  from " + table);

                    statement.close();

                }

                System.out.println("Clearing all tables content from IDS statistics database  : " + "done succesfully !!");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public String prepareInsertRequest(String tableName, int recordsNumber) {
        String request = null;
        String colums = null;
        String valuesModal = null;
        switch (tableName) {
            case "packetIns_table": {
                colums = "(dpid,tableID,time_stamp,total_count,ip_src,ip_dest,port_src,port_dest,mac_src,mac_dest,vlanID)";
                valuesModal = "(?,?,?,?,?,?,?,?,?,?,?)";

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
                colums = "(dpid,tableID,time_stamp,byteCount,flags,flowCount,packetCount,version)";
                valuesModal = "(?,?,?,?,?,?,?,?)";
            }
            break;
            case "flows_table": {
                colums = "(dpid,tableID,time_stamp,byteCount,cookie,durationNSeconds, durationSeconds, flags,hardTimeoutSec,idleTimeoutSec, packetCount,"
                        + "priority,instructions,ip_src,ip_dest, port_src,port_dest,mac_src,mac_dest,vlanID)";
                valuesModal = "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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

        try {
            return this.connection.prepareStatement(this.prepareInsertRequest(tableName, recordsNumber));
        } catch (SQLException ex) {
            Logger.getLogger(StatsDBInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public PreparedStatement get_SelectPreparedStatement(String tableName) {

        String WHERE_CONDITION = " where (dpid = ? and tableID = ? and time_stamp >= ? and time_stamp <= ?) order by time_stamp DESC ";

        try {
            switch (tableName) {
                case "packet_in_count":
                    return connection.prepareStatement("select * from packet_in_count" + WHERE_CONDITION);
                case "packetIns_table":
                    return connection.prepareStatement("select * from packetIns_table" + WHERE_CONDITION);
                case "flow_removed_count":
                    return connection.prepareStatement("select * from flow_removed_count" + WHERE_CONDITION);
                case "flowRemoved_table":
                    return connection.prepareStatement("select * from flowRemoved_table" + WHERE_CONDITION);
                case "table_full_count":
                    return connection.prepareStatement("select * from table_full_count" + WHERE_CONDITION);
                case "packet_out_count":
                    return connection.prepareStatement("select * from packet_out_count" + WHERE_CONDITION);

                default:
                    return null;

            }

        } catch (SQLException ex) {

            return null;

        }

    }

    public ResultSet selectFromTable(String tableName, String dpid, String tableID, long time_stamp_min, long time_stamp_max) {
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

    }

    public boolean addToPacket_inTable(ArrayList<PacketInsTable> records) {

        PreparedStatement statement = get_InsertPreparedStatement("packetIns_table", records.size());

        int i = 0;

        try {
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

            }

            long t = System.currentTimeMillis();
            statement.executeUpdate();
            long t2 = System.currentTimeMillis();
            System.out.println(t2 - t);
            return true;
        } catch (SQLException e) {

            System.out.println(e.toString());

        }

        return false;

    }

    public boolean addToFlowRemovedTable(ArrayList<FlowRemovedTable> records) {

        PreparedStatement statement = get_InsertPreparedStatement("flowRemoved_table", records.size());
        try {
            int i = 0;

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

            return true;

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    public boolean addToCounter(String tableName, ArrayList<CounterTable> records) {

        PreparedStatement statement = get_InsertPreparedStatement(tableName, records.size());

        try {
            int i = 0;
            for (CounterTable r : records) {

                statement.setString(++i, r.getDpid());
                statement.setString(++i, r.getTableID());
                statement.setLong(++i, r.getTime_stamp());
                statement.setLong(++i, r.getTotal_count());

            }

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    public boolean addToAggregateFlowsTable(ArrayList<AggregateSQL> records) {
        PreparedStatement statement = get_InsertPreparedStatement("aggregate_flows_table", records.size());

        try {
            int i = 0;
            for (AggregateSQL r : records) {

                statement.setString(++i, r.getDpid());
//                statement.setString(++i, r.getTableID());
                statement.setLong(++i, r.getTime_stamp());
                statement.setLong(++i, r.getByteCount());
                statement.setInt(++i, r.getFlags());
                statement.setInt(++i, r.getFlowCount());
                statement.setLong(++i, r.getPacketCount());
                statement.setString(++i, r.getVersion());

            }

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    public boolean addToFlowsTable(ArrayList<FlowEntryStatsSQL> records) {
        PreparedStatement statement = get_InsertPreparedStatement("flows_table", records.size());

        try {
            int i = 0;
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
                statement.setString(++i, r.getInstructions().toString());
                statement.setString(++i, r.getMatch().getIpv4_src());
                statement.setString(++i, r.getMatch().getIpv4_dst());
                if (r.getMatch().getUdp_dst() == null) {
                    statement.setInt(++i, r.getMatch().getTcp_src());
                    statement.setInt(++i, r.getMatch().getTcp_dst());
                } else {
                    statement.setInt(++i, r.getMatch().getUdp_src());
                    statement.setInt(++i, r.getMatch().getUdp_dst());
                }

                statement.setString(++i, r.getMatch().getEth_src());
                statement.setString(++i, r.getMatch().getEth_dst());
                statement.setInt(++i, r.getMatch().getVlan_vid());

            }

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    public boolean addToPortActivityTable(ArrayList<PortActivitySQL> records) {
        PreparedStatement statement = get_InsertPreparedStatement("switch_ports_activities_table", records.size());

        try {
            int i = 0;
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

            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    public boolean addToTableStatsTable(ArrayList<TableStatsSQL> records) {
        PreparedStatement statement = get_InsertPreparedStatement("tableStats_table", records.size());

        try {
            int i = 0;

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

            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    public boolean addToPortBandwidthTable(ArrayList<PortBandwidthSQL> records) {
        PreparedStatement statement = get_InsertPreparedStatement("port_bandWidth_table", records.size());

        try {
            int i = 0;

            for (PortBandwidthSQL r : records) {

                statement.setString(++i, r.getDpid());
                statement.setLong(++i, r.getTime_stamp());
                statement.setLong(++i, r.getRx());
                statement.setLong(++i, r.getTx());
                statement.setString(++i, r.getPort());
                statement.setString(++i, r.getUpdatedTime());

            }

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    public boolean addToControlerMemoryUsage(ArrayList<ControllerMemoryUsageSQL> records) {
        PreparedStatement statement = get_InsertPreparedStatement("controlerMemoryUsage_table", records.size());

        try {
            int i = 0;

            for (ControllerMemoryUsageSQL r : records) {

                statement.setLong(++i, r.getTime_stamp());
                statement.setLong(++i, r.getTotal());
                statement.setLong(++i, r.getFree());

            }

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public static void main(String[] args) throws InterruptedException {

        StatsDBInterface dbInterface = new StatsDBInterface();

        ArrayList<ControllerMemoryUsageSQL> records = new ArrayList<>();
        int number = 8000;

        for (int i = 0; i < number; i++) {
            ControllerMemoryUsageSQL e = new ControllerMemoryUsageSQL(i, new ControllerMemoryUsage(i + 10, i + 20));
            records.add(e);
        }

        System.out.println(dbInterface.connect("ids_db"));
        dbInterface.clearAllTables();
      

        while (true) {
            System.out.println(records.size());
            long t1 = System.currentTimeMillis();
            dbInterface.addToControlerMemoryUsage(records);
            long t2 = System.currentTimeMillis();
            System.out.println("My duration is : " + (t2 - t1));
            Thread.sleep(500);

            for (ControllerMemoryUsageSQL r : records) {

                r.setTime_stamp(r.getTime_stamp() + number);
            }
            for (int a = 0; a < 1; a++) {
                records.remove(a);
            }

        }
        // System.exit(0);
        // System.out.println(dbInterface.prepareInsertRequest("flows_table", 8000));
        /* for (int i = 8000; i < 10000; i++) {
            System.out.print("Param == " + i);
            dbInterface.clearAllTables();
            dbInterface.test(i, true);
            dbInterface.clearAllTables();
            dbInterface.test(i, false);
            dbInterface.clearAllTables();

        }

        System.out.println(dbInterface.closeConnection());
        dbInterface.clearAllTables();*/

//dbInterface.prepareAllStatements();
        //dbInterface.clearAllTables();
        //dbInterface.addToCounter("packet_in_count","00:00:00:00:01","0x001",1L,2);
        /*result=dbInterface.addToCounter("flow_removed_count","00:00:00:00:01","0x001", System.currentTimeMillis(), 177880);
     result=dbInterface.addToCounter("table_full_count","00:00:00:00:01","0x001",System.currentTimeMillis(), 15445);*/
 /*ResultSet set=dbInterface.selectFromTable("packet_in_count", "00:00:00:00:01","0x001",0,150000000L);
	
	try {
		
		
		while(set.next()){
		     String a= set.getString(1);
		     String b= set.getString(2);
		     long c= set.getLong(3);
		     long d= set.getLong(4);
		     System.out.println(a+" "+b+" "+c+" "+d);
		     
			
			
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
 /*  if(result){
		
		System.out.println("success");
		
		
		
	}
	
	
	
	dbInterface.closeConnection();*/
    }

}
