/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.flows;

import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class Match {

    private Integer in_port = null;
    private Integer in_phy_port = null;
    private String metadata = null;
    private String eth_dst = null;
    private String eth_src = null;
    private String eth_type = null;
    private Integer vlan_vid = null;
    private String vlan_pcp = null;
    private String ip_dscp = null;
    private String ip_ecn = null;
    private String ip_proto = null;
    private String ipv4_src = null;
    private String ipv4_dst = null;
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
    //private String bsn_in_ports_128;
    // private String bsn_in_ports_512;
    //private String bsn_lag_id;
    //private String bsn_vrf;
    //private String bsn_global_vrf_allowed;
    //private String bsn_l3_Integererface_class_id;
    // private String bsn_l3_src_class_id;
    //private String bsn_l3_dst_class_id;
    //private String bsn_egr_port_group_id;
    // private String bsn_udf0;
    //private String bsn_udf1;
    // private String bsn_udf2;
    // private String bsn_udf3;
    //private String bsn_udf4;
    //private String bsn_udf5;
    //private String bsn_udf6;
    // private String bsn_udf7;
    // private String bsn_tcp_flags;
    //private String bsn_vlan_xlate_port_group_id;
    // private String bsn_l2_cache_hit;
    //private String bsn_ingress_port_group_id;
    // private String bsn_vxlan_network_id;
    // private String bsn_inner_eth_dst;
    // private String bsn_inner_eth_src;
    // private String bsn_inner_vlan_vid;

    public Match() {
    }

    public Match(Integer vlan_vid, Integer tcp_src, Integer tcp_dst) {

        this.vlan_vid = vlan_vid;
        this.tcp_src = tcp_src;
        this.tcp_dst = tcp_dst;

    }

    public Match(Integer in_port, Integer in_phy_port, String metadata, String eth_dst, String eth_src, String eth_type, Integer vlan_vid, String vlan_pcp, String ip_dscp, String ip_ecn, String ip_proto, String ipv4_src, String ipv4_dst, Integer tcp_src, Integer tcp_dst, Integer udp_src, Integer udp_dst, String sctp_src, String sctp_dst, String icmpv4_type, String icmpv4_code, String arp_op, String arp_spa, String arp_tpa, String arp_sha, String arp_tha, String ipv6_src, String ipv6_dst, String ipv6_flabel, String icmpv6_type, String icmpv6_code, String ipv6_nd_target, String ipv6_nd_sll, String ipv6_nd_tll, String mpls_label, String mpls_tc, String mpls_bos, String tunnel_id, String ipv6_exthdr, String pbb_uca, String tunnel_ipv4_src, String tunnel_ipv4_dst) {
        this.in_port = in_port;
        this.in_phy_port = in_phy_port;
        this.metadata = metadata;
        this.eth_dst = eth_dst;
        this.eth_src = eth_src;
        this.eth_type = eth_type;
        this.vlan_vid = vlan_vid;
        this.vlan_pcp = vlan_pcp;
        this.ip_dscp = ip_dscp;
        this.ip_ecn = ip_ecn;
        this.ip_proto = ip_proto;
        this.ipv4_src = ipv4_src;
        this.ipv4_dst = ipv4_dst;
        this.tcp_src = tcp_src;
        this.tcp_dst = tcp_dst;
        this.udp_src = udp_src;
        this.udp_dst = udp_dst;
        this.sctp_src = sctp_src;
        this.sctp_dst = sctp_dst;
        this.icmpv4_type = icmpv4_type;
        this.icmpv4_code = icmpv4_code;
        this.arp_op = arp_op;
        this.arp_spa = arp_spa;
        this.arp_tpa = arp_tpa;
        this.arp_sha = arp_sha;
        this.arp_tha = arp_tha;
        this.ipv6_src = ipv6_src;
        this.ipv6_dst = ipv6_dst;
        this.ipv6_flabel = ipv6_flabel;
        this.icmpv6_type = icmpv6_type;
        this.icmpv6_code = icmpv6_code;
        this.ipv6_nd_target = ipv6_nd_target;
        this.ipv6_nd_sll = ipv6_nd_sll;
        this.ipv6_nd_tll = ipv6_nd_tll;
        this.mpls_label = mpls_label;
        this.mpls_tc = mpls_tc;
        this.mpls_bos = mpls_bos;
        this.tunnel_id = tunnel_id;
        this.ipv6_exthdr = ipv6_exthdr;
        this.pbb_uca = pbb_uca;
        this.tunnel_ipv4_src = tunnel_ipv4_src;
        this.tunnel_ipv4_dst = tunnel_ipv4_dst;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.in_port);
        hash = 89 * hash + Objects.hashCode(this.in_phy_port);
        hash = 89 * hash + Objects.hashCode(this.metadata);
        hash = 89 * hash + Objects.hashCode(this.eth_dst);
        hash = 89 * hash + Objects.hashCode(this.eth_src);
        hash = 89 * hash + Objects.hashCode(this.eth_type);
        hash = 89 * hash + Objects.hashCode(this.vlan_vid);
        hash = 89 * hash + Objects.hashCode(this.vlan_pcp);
        hash = 89 * hash + Objects.hashCode(this.ip_dscp);
        hash = 89 * hash + Objects.hashCode(this.ip_ecn);
        hash = 89 * hash + Objects.hashCode(this.ip_proto);
        hash = 89 * hash + Objects.hashCode(this.ipv4_src);
        hash = 89 * hash + Objects.hashCode(this.ipv4_dst);
        hash = 89 * hash + Objects.hashCode(this.tcp_src);
        hash = 89 * hash + Objects.hashCode(this.tcp_dst);
        hash = 89 * hash + Objects.hashCode(this.udp_src);
        hash = 89 * hash + Objects.hashCode(this.udp_dst);
        hash = 89 * hash + Objects.hashCode(this.sctp_src);
        hash = 89 * hash + Objects.hashCode(this.sctp_dst);
        hash = 89 * hash + Objects.hashCode(this.icmpv4_type);
        hash = 89 * hash + Objects.hashCode(this.icmpv4_code);
        hash = 89 * hash + Objects.hashCode(this.arp_op);
        hash = 89 * hash + Objects.hashCode(this.arp_spa);
        hash = 89 * hash + Objects.hashCode(this.arp_tpa);
        hash = 89 * hash + Objects.hashCode(this.arp_sha);
        hash = 89 * hash + Objects.hashCode(this.arp_tha);
        hash = 89 * hash + Objects.hashCode(this.ipv6_src);
        hash = 89 * hash + Objects.hashCode(this.ipv6_dst);
        hash = 89 * hash + Objects.hashCode(this.ipv6_flabel);
        hash = 89 * hash + Objects.hashCode(this.icmpv6_type);
        hash = 89 * hash + Objects.hashCode(this.icmpv6_code);
        hash = 89 * hash + Objects.hashCode(this.ipv6_nd_target);
        hash = 89 * hash + Objects.hashCode(this.ipv6_nd_sll);
        hash = 89 * hash + Objects.hashCode(this.ipv6_nd_tll);
        hash = 89 * hash + Objects.hashCode(this.mpls_label);
        hash = 89 * hash + Objects.hashCode(this.mpls_tc);
        hash = 89 * hash + Objects.hashCode(this.mpls_bos);
        hash = 89 * hash + Objects.hashCode(this.tunnel_id);
        hash = 89 * hash + Objects.hashCode(this.ipv6_exthdr);
        hash = 89 * hash + Objects.hashCode(this.pbb_uca);
        hash = 89 * hash + Objects.hashCode(this.tunnel_ipv4_src);
        hash = 89 * hash + Objects.hashCode(this.tunnel_ipv4_dst);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Match other = (Match) obj;
        if (this.in_port != other.in_port) {
            return false;
        }
        if (this.in_phy_port != other.in_phy_port) {
            return false;
        }
        if (this.vlan_vid != other.vlan_vid) {
            return false;
        }
        if (this.tcp_src != other.tcp_src) {
            return false;
        }
        if (this.tcp_dst != other.tcp_dst) {
            return false;
        }
        if (this.udp_src != other.udp_src) {
            return false;
        }
        if (this.udp_dst != other.udp_dst) {
            return false;
        }
        if (!Objects.equals(this.metadata, other.metadata)) {
            return false;
        }
        if (!Objects.equals(this.eth_dst, other.eth_dst)) {
            return false;
        }
        if (!Objects.equals(this.eth_src, other.eth_src)) {
            return false;
        }
        if (!Objects.equals(this.eth_type, other.eth_type)) {
            return false;
        }
        if (!Objects.equals(this.vlan_pcp, other.vlan_pcp)) {
            return false;
        }
        if (!Objects.equals(this.ip_dscp, other.ip_dscp)) {
            return false;
        }
        if (!Objects.equals(this.ip_ecn, other.ip_ecn)) {
            return false;
        }
        if (!Objects.equals(this.ip_proto, other.ip_proto)) {
            return false;
        }
        if (!Objects.equals(this.ipv4_src, other.ipv4_src)) {
            return false;
        }
        if (!Objects.equals(this.ipv4_dst, other.ipv4_dst)) {
            return false;
        }
        if (!Objects.equals(this.sctp_src, other.sctp_src)) {
            return false;
        }
        if (!Objects.equals(this.sctp_dst, other.sctp_dst)) {
            return false;
        }
        if (!Objects.equals(this.icmpv4_type, other.icmpv4_type)) {
            return false;
        }
        if (!Objects.equals(this.icmpv4_code, other.icmpv4_code)) {
            return false;
        }
        if (!Objects.equals(this.arp_op, other.arp_op)) {
            return false;
        }
        if (!Objects.equals(this.arp_spa, other.arp_spa)) {
            return false;
        }
        if (!Objects.equals(this.arp_tpa, other.arp_tpa)) {
            return false;
        }
        if (!Objects.equals(this.arp_sha, other.arp_sha)) {
            return false;
        }
        if (!Objects.equals(this.arp_tha, other.arp_tha)) {
            return false;
        }
        if (!Objects.equals(this.ipv6_src, other.ipv6_src)) {
            return false;
        }
        if (!Objects.equals(this.ipv6_dst, other.ipv6_dst)) {
            return false;
        }
        if (!Objects.equals(this.ipv6_flabel, other.ipv6_flabel)) {
            return false;
        }
        if (!Objects.equals(this.icmpv6_type, other.icmpv6_type)) {
            return false;
        }
        if (!Objects.equals(this.icmpv6_code, other.icmpv6_code)) {
            return false;
        }
        if (!Objects.equals(this.ipv6_nd_target, other.ipv6_nd_target)) {
            return false;
        }
        if (!Objects.equals(this.ipv6_nd_sll, other.ipv6_nd_sll)) {
            return false;
        }
        if (!Objects.equals(this.ipv6_nd_tll, other.ipv6_nd_tll)) {
            return false;
        }
        if (!Objects.equals(this.mpls_label, other.mpls_label)) {
            return false;
        }
        if (!Objects.equals(this.mpls_tc, other.mpls_tc)) {
            return false;
        }
        if (!Objects.equals(this.mpls_bos, other.mpls_bos)) {
            return false;
        }
        if (!Objects.equals(this.tunnel_id, other.tunnel_id)) {
            return false;
        }
        if (!Objects.equals(this.ipv6_exthdr, other.ipv6_exthdr)) {
            return false;
        }
        if (!Objects.equals(this.pbb_uca, other.pbb_uca)) {
            return false;
        }
        if (!Objects.equals(this.tunnel_ipv4_src, other.tunnel_ipv4_src)) {
            return false;
        }
        if (!Objects.equals(this.tunnel_ipv4_dst, other.tunnel_ipv4_dst)) {
            return false;
        }
        System.out.println("true");
        return true;
    }

    @Override
    public String toString() {
        return "Match{" + "in_port=" + in_port + ", in_phy_port=" + in_phy_port + ", metadata=" + metadata + ", eth_dst=" + eth_dst + ", eth_src=" + eth_src + ", eth_type=" + eth_type + ", vlan_vid=" + vlan_vid + ", vlan_pcp=" + vlan_pcp + ", ip_dscp=" + ip_dscp + ", ip_ecn=" + ip_ecn + ", ip_proto=" + ip_proto + ", ipv4_src=" + ipv4_src + ", ipv4_dst=" + ipv4_dst + ", tcp_src=" + tcp_src + ", tcp_dst=" + tcp_dst + ", udp_src=" + udp_src + ", udp_dst=" + udp_dst + ", sctp_src=" + sctp_src + ", sctp_dst=" + sctp_dst + ", icmpv4_type=" + icmpv4_type + ", icmpv4_code=" + icmpv4_code + ", arp_op=" + arp_op + ", arp_spa=" + arp_spa + ", arp_tpa=" + arp_tpa + ", arp_sha=" + arp_sha + ", arp_tha=" + arp_tha + ", ipv6_src=" + ipv6_src + ", ipv6_dst=" + ipv6_dst + ", ipv6_flabel=" + ipv6_flabel + ", icmpv6_type=" + icmpv6_type + ", icmpv6_code=" + icmpv6_code + ", ipv6_nd_target=" + ipv6_nd_target + ", ipv6_nd_sll=" + ipv6_nd_sll + ", ipv6_nd_tll=" + ipv6_nd_tll + ", mpls_label=" + mpls_label + ", mpls_tc=" + mpls_tc + ", mpls_bos=" + mpls_bos + ", tunnel_id=" + tunnel_id + ", ipv6_exthdr=" + ipv6_exthdr + ", pbb_uca=" + pbb_uca + ", tunnel_ipv4_src=" + tunnel_ipv4_src + ", tunnel_ipv4_dst=" + tunnel_ipv4_dst + '}';
    }

   

}
