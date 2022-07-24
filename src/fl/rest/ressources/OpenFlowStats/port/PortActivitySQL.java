/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.port;

/**
 *
 * @author Moubarak
 */
public class PortActivitySQL extends PortActivity {

    private String dpid;
    private long time_stamp;
    private String version;

    public PortActivitySQL(String dpid, long time_stamp, String version,PortActivity portActivity) {
      
        super(portActivity.getCollisions(),portActivity.getDurationNsec(),portActivity.getDurationSec(),portActivity.getPortNumber(), 
               portActivity.getReceiveBytes(), portActivity.getReceiveCRCErrors(), portActivity.getReceiveDropped(),portActivity.getReceiveErrors(), 
               portActivity.getReceiveFrameErrors(),portActivity.getReceiveOverrunErrors(), 
               portActivity.getReceivePackets(),portActivity.getTransmitBytes(),
               portActivity.getTransmitDropped(),portActivity.getTransmitErrors(),portActivity.getTransmitPackets());
        this.dpid = dpid;
        this.time_stamp = time_stamp;
        this.version = version;
    }
    
    
    

    public PortActivitySQL(long collisions, long durationNsec, long durationSec, String portNumber, long receiveBytes, long receiveCRCErrors, long receiveDropped, long receiveErrors, long receiveFrameErrors, long receiveOverrunErrors, long receivePackets, long transmitBytes, long transmitDropped, long transmitErrors, long transmitPackets) {
        super(collisions, durationNsec, durationSec, portNumber, receiveBytes, receiveCRCErrors, receiveDropped, receiveErrors, receiveFrameErrors, receiveOverrunErrors, receivePackets, transmitBytes, transmitDropped, transmitErrors, transmitPackets);
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

    @Override
    public String toString() {
        return "PortActivitySQL{" + "dpid=" + dpid + ", time_stamp=" + time_stamp + ", version=" + version + '}';
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
    
    

}
