package fl.rest.ressources.OpenFlowStats.packetIns;

import fl.rest.ressources.OpenFlowStats.Counters.CounterTable;

public class PacketInsTable extends CounterTable {

    private String ip_src;
    private String ip_dest;
    private int port_src;
    private int port_dest;
    private String matching;

    @Override
    public String toString() {
        return "PacketInsTable [ip_src=" + ip_src + ", ip_dest=" + ip_dest
                + ", port_src=" + port_src + ", port_dest=" + port_dest
                + ", mac_src=" + mac_src + ", mac_dest=" + mac_dest
                + ", vlanID=" + vlanID + "]";
    }

    private String mac_src;
    private String mac_dest;
    private int vlanID;

    public PacketInsTable(String dpid, String tableID, long time_stamp,
            long total_count) {
        super(dpid, tableID, time_stamp, total_count);
        // TODO Auto-generated constructor stub
    }

    public PacketInsTable(String dpid, String tableID, long time_stamp,
            long total_count, String ip_src, String ip_dest, int port_src,
            int port_dest, String mac_src, String mac_dest, int vlanID, String matching) {
        super(dpid, tableID, time_stamp, total_count);
        this.ip_src = ip_src;
        this.ip_dest = ip_dest;
        this.port_src = port_src;
        this.port_dest = port_dest;
        this.mac_src = mac_src;
        this.mac_dest = mac_dest;
        this.vlanID = vlanID;
        this.matching = matching;
    }

    public String getIp_src() {
        return ip_src;
    }

    public void setIp_src(String ip_src) {
        this.ip_src = ip_src;
    }

    public String getIp_dest() {
        return ip_dest;
    }

    public void setIp_dest(String ip_dest) {
        this.ip_dest = ip_dest;
    }

    public int getPort_src() {
        return port_src;
    }

    public void setPort_src(int port_src) {
        this.port_src = port_src;
    }

    public int getPort_dest() {
        return port_dest;
    }

    public void setPort_dest(int port_dest) {
        this.port_dest = port_dest;
    }

    public String getMac_src() {
        return mac_src;
    }

    public void setMac_src(String mac_src) {
        this.mac_src = mac_src;
    }

    public String getMac_dest() {
        return mac_dest;
    }

    public void setMac_dest(String mac_dest) {
        this.mac_dest = mac_dest;
    }

    public int getVlanID() {
        return vlanID;
    }

    public void setVlanID(int vlanID) {
        this.vlanID = vlanID;
    }

    public String getMatching() {
        this.matching = "ip_src=" + ip_src + "|ip_dst=" + ip_dest + "|port_src=" + port_src + "|port_dst=" + port_dest;
        return this.matching;
    }

    public void setMatching(String matching) {
        this.matching = matching;
    }

}
