/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.staticflowpusher;

/**
 *
 * @author Moubarak
 */
public class OFFlowMod {

    private String name = "flow-3";
    private String switchID = "00:00:00:00:00:00:00:01";
    private String entry_type = "flow";
    private Integer in_port = null;
    private Integer in_phy_port = null;
    private String metadata = null;
    private String eth_dst = "00:00:00:00:00:01";
    private String eth_src = "00:00:00:00:00:02";
    private String eth_type = "0x0800";
    private Integer vlan_vid = null;
    private String vlan_pcp = null;
    private String ip_dscp = null;
    private String ip_ecn = null;
    private String ip_proto = null;
    private String ipv4_src = "192.168.10.1";
    private String ipv4_dst = "192.168.10.4";
    private Integer tcp_src = null;
    private Integer tcp_dst = null;
    private Integer udp_src = null;
    private Integer udp_dst = null;
    private String sctp_src = null;
    private String sctp_dst = null;
    private String icmpv4_type = null;
    private String icmpv4_code = null;
    private String arp_op = null;
    private String arp_spa = null;
    private String arp_tpa = null;
    private String arp_sha = null;
    private String arp_tha = null;
    private String ipv6_src = null;
    private String ipv6_dst = null;
    private String ipv6_flabel = null;
    private String icmpv6_type = null;
    private String icmpv6_code = null;
    private String ipv6_nd_target = null;
    private String ipv6_nd_sll = null;
    private String ipv6_nd_tll = null;
    private String mpls_label = null;
    private String mpls_tc = null;
    private String mpls_bos = null;
    private String tunnel_id = null;
    private String ipv6_exthdr = null;
    private String pbb_uca = null;
    private String tunnel_ipv4_src = null;
    private String tunnel_ipv4_dst = null;

    private String instruction_goto_table = null;
    private String instruction_write_metadata = null;
    private String instruction_write_actions = null;
    private String instruction_apply_actions = "output=4";
    private String instruction_clear_actions = null;
    private String instruction_goto_meter = null;
    private String instruction_experimenter = null;
    private String actions = null;
    private boolean active=true;

    private String cookie="0";
    private String cookie_mask="0";
    private long hard_timeout=122;
    private long idle_timeout=1222;
    private Integer priority = 222;
    private String table;
    private String version;
    private Integer flags;

    public OFFlowMod() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSwitchID() {
        return switchID;
    }

    public void setSwitchID(String switchID) {
        this.switchID = switchID;
    }

    public String getEntry_type() {
        return entry_type;
    }

    public void setEntry_type(String entry_type) {
        this.entry_type = entry_type;
    }

    public Integer getIn_port() {
        return in_port;
    }

    public void setIn_port(Integer in_port) {
        this.in_port = in_port;
    }

    public Integer getIn_phy_port() {
        return in_phy_port;
    }

    public void setIn_phy_port(Integer in_phy_port) {
        this.in_phy_port = in_phy_port;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getEth_dst() {
        return eth_dst;
    }

    public void setEth_dst(String eth_dst) {
        this.eth_dst = eth_dst;
    }

    public String getEth_src() {
        return eth_src;
    }

    public void setEth_src(String eth_src) {
        this.eth_src = eth_src;
    }

    public String getEth_type() {
        return eth_type;
    }

    public void setEth_type(String eth_type) {
        this.eth_type = eth_type;
    }

    public Integer getVlan_vid() {
        return vlan_vid;
    }

    public void setVlan_vid(Integer vlan_vid) {
        this.vlan_vid = vlan_vid;
    }

    public String getVlan_pcp() {
        return vlan_pcp;
    }

    public void setVlan_pcp(String vlan_pcp) {
        this.vlan_pcp = vlan_pcp;
    }

    public String getIp_dscp() {
        return ip_dscp;
    }

    public void setIp_dscp(String ip_dscp) {
        this.ip_dscp = ip_dscp;
    }

    public String getIp_ecn() {
        return ip_ecn;
    }

    public void setIp_ecn(String ip_ecn) {
        this.ip_ecn = ip_ecn;
    }

    public String getIp_proto() {
        return ip_proto;
    }

    public void setIp_proto(String ip_proto) {
        this.ip_proto = ip_proto;
    }

    public String getIpv4_src() {
        return ipv4_src;
    }

    public void setIpv4_src(String ipv4_src) {
        this.ipv4_src = ipv4_src;
    }

    public String getIpv4_dst() {
        return ipv4_dst;
    }

    public void setIpv4_dst(String ipv4_dst) {
        this.ipv4_dst = ipv4_dst;
    }

    public Integer getTcp_src() {
        return tcp_src;
    }

    public void setTcp_src(Integer tcp_src) {
        this.tcp_src = tcp_src;
    }

    public Integer getTcp_dst() {
        return tcp_dst;
    }

    public void setTcp_dst(Integer tcp_dst) {
        this.tcp_dst = tcp_dst;
    }

    public Integer getUdp_src() {
        return udp_src;
    }

    public void setUdp_src(Integer udp_src) {
        this.udp_src = udp_src;
    }

    public Integer getUdp_dst() {
        return udp_dst;
    }

    public void setUdp_dst(Integer udp_dst) {
        this.udp_dst = udp_dst;
    }

    public String getSctp_src() {
        return sctp_src;
    }

    public void setSctp_src(String sctp_src) {
        this.sctp_src = sctp_src;
    }

    public String getSctp_dst() {
        return sctp_dst;
    }

    public void setSctp_dst(String sctp_dst) {
        this.sctp_dst = sctp_dst;
    }

    public String getIcmpv4_type() {
        return icmpv4_type;
    }

    public void setIcmpv4_type(String icmpv4_type) {
        this.icmpv4_type = icmpv4_type;
    }

    public String getIcmpv4_code() {
        return icmpv4_code;
    }

    public void setIcmpv4_code(String icmpv4_code) {
        this.icmpv4_code = icmpv4_code;
    }

    public String getArp_op() {
        return arp_op;
    }

    public void setArp_op(String arp_op) {
        this.arp_op = arp_op;
    }

    public String getArp_spa() {
        return arp_spa;
    }

    public void setArp_spa(String arp_spa) {
        this.arp_spa = arp_spa;
    }

    public String getArp_tpa() {
        return arp_tpa;
    }

    public void setArp_tpa(String arp_tpa) {
        this.arp_tpa = arp_tpa;
    }

    public String getArp_sha() {
        return arp_sha;
    }

    public void setArp_sha(String arp_sha) {
        this.arp_sha = arp_sha;
    }

    public String getArp_tha() {
        return arp_tha;
    }

    public void setArp_tha(String arp_tha) {
        this.arp_tha = arp_tha;
    }

    public String getIpv6_src() {
        return ipv6_src;
    }

    public void setIpv6_src(String ipv6_src) {
        this.ipv6_src = ipv6_src;
    }

    public String getIpv6_dst() {
        return ipv6_dst;
    }

    public void setIpv6_dst(String ipv6_dst) {
        this.ipv6_dst = ipv6_dst;
    }

    public String getIpv6_flabel() {
        return ipv6_flabel;
    }

    public void setIpv6_flabel(String ipv6_flabel) {
        this.ipv6_flabel = ipv6_flabel;
    }

    public String getIcmpv6_type() {
        return icmpv6_type;
    }

    public void setIcmpv6_type(String icmpv6_type) {
        this.icmpv6_type = icmpv6_type;
    }

    public String getIcmpv6_code() {
        return icmpv6_code;
    }

    public void setIcmpv6_code(String icmpv6_code) {
        this.icmpv6_code = icmpv6_code;
    }

    public String getIpv6_nd_target() {
        return ipv6_nd_target;
    }

    public void setIpv6_nd_target(String ipv6_nd_target) {
        this.ipv6_nd_target = ipv6_nd_target;
    }

    public String getIpv6_nd_sll() {
        return ipv6_nd_sll;
    }

    public void setIpv6_nd_sll(String ipv6_nd_sll) {
        this.ipv6_nd_sll = ipv6_nd_sll;
    }

    public String getIpv6_nd_tll() {
        return ipv6_nd_tll;
    }

    public void setIpv6_nd_tll(String ipv6_nd_tll) {
        this.ipv6_nd_tll = ipv6_nd_tll;
    }

    public String getMpls_label() {
        return mpls_label;
    }

    public void setMpls_label(String mpls_label) {
        this.mpls_label = mpls_label;
    }

    public String getMpls_tc() {
        return mpls_tc;
    }

    public void setMpls_tc(String mpls_tc) {
        this.mpls_tc = mpls_tc;
    }

    public String getMpls_bos() {
        return mpls_bos;
    }

    public void setMpls_bos(String mpls_bos) {
        this.mpls_bos = mpls_bos;
    }

    public String getTunnel_id() {
        return tunnel_id;
    }

    public void setTunnel_id(String tunnel_id) {
        this.tunnel_id = tunnel_id;
    }

    public String getIpv6_exthdr() {
        return ipv6_exthdr;
    }

    public void setIpv6_exthdr(String ipv6_exthdr) {
        this.ipv6_exthdr = ipv6_exthdr;
    }

    public String getPbb_uca() {
        return pbb_uca;
    }

    public void setPbb_uca(String pbb_uca) {
        this.pbb_uca = pbb_uca;
    }

    public String getTunnel_ipv4_src() {
        return tunnel_ipv4_src;
    }

    public void setTunnel_ipv4_src(String tunnel_ipv4_src) {
        this.tunnel_ipv4_src = tunnel_ipv4_src;
    }

    public String getTunnel_ipv4_dst() {
        return tunnel_ipv4_dst;
    }

    public void setTunnel_ipv4_dst(String tunnel_ipv4_dst) {
        this.tunnel_ipv4_dst = tunnel_ipv4_dst;
    }

    public String getInstruction_goto_table() {
        return instruction_goto_table;
    }

    public void setInstruction_goto_table(String instruction_goto_table) {
        this.instruction_goto_table = instruction_goto_table;
    }

    public String getInstruction_write_metadata() {
        return instruction_write_metadata;
    }

    public void setInstruction_write_metadata(String instruction_write_metadata) {
        this.instruction_write_metadata = instruction_write_metadata;
    }

    public String getInstruction_write_actions() {
        return instruction_write_actions;
    }

    public void setInstruction_write_actions(String instruction_write_actions) {
        this.instruction_write_actions = instruction_write_actions;
    }

    public String getInstruction_apply_actions() {
        return instruction_apply_actions;
    }

    public void setInstruction_apply_actions(String instruction_apply_actions) {
        this.instruction_apply_actions = instruction_apply_actions;
    }

    public String getInstruction_clear_actions() {
        return instruction_clear_actions;
    }

    public void setInstruction_clear_actions(String instruction_clear_actions) {
        this.instruction_clear_actions = instruction_clear_actions;
    }

    public String getInstruction_goto_meter() {
        return instruction_goto_meter;
    }

    public void setInstruction_goto_meter(String instruction_goto_meter) {
        this.instruction_goto_meter = instruction_goto_meter;
    }

    public String getInstruction_experimenter() {
        return instruction_experimenter;
    }

    public void setInstruction_experimenter(String instruction_experimenter) {
        this.instruction_experimenter = instruction_experimenter;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getCookie_mask() {
        return cookie_mask;
    }

    public void setCookie_mask(String cookie_mask) {
        this.cookie_mask = cookie_mask;
    }

    public long getHard_timeout() {
        return hard_timeout;
    }

    public void setHard_timeout(long hard_timeout) {
        this.hard_timeout = hard_timeout;
    }

    public long getIdle_timeout() {
        return idle_timeout;
    }

    public void setIdle_timeout(long idle_timeout) {
        this.idle_timeout = idle_timeout;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getFlags() {
        return flags;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }

}
