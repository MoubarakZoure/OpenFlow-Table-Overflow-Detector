/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.statcollector;

/**
 *
 * @author Moubarak
 */
public class PortBandwidthSQL extends PortBandwidth {

    private long time_stamp;

    public PortBandwidthSQL(long time_stamp, long rx, long tx, String dpid, String port, String updatedTime) {
        super(rx, tx, dpid, port, updatedTime);

        this.time_stamp = time_stamp;

    }

    public PortBandwidthSQL(long time_stamp, PortBandwidth portBandwidth) {
        super(portBandwidth.getRx(), portBandwidth.getTx(), portBandwidth.getDpid(), portBandwidth.getPort(), portBandwidth.getUpdatedTime());

        this.time_stamp = time_stamp;

    }

    public PortBandwidthSQL(long rx, long tx, String dpid, String port, String updatedTime) {
        super(rx, tx, dpid, port, updatedTime);
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

}
