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
public class SimpleMatch {

    private String ipv4_src = null;
    private String ipv4_dst = null;
    private Integer port_src = null;
    private Integer port_dst = null;

    public SimpleMatch(String ip_src, String ip_dst, Integer port_src, Integer port_dst) {

        this.ipv4_src = ip_src;
        this.ipv4_dst = ip_dst;
        this.port_src = port_src;
        this.port_dst = port_dst;

    }
    
    public static boolean isEqualsMatch(SimpleMatch m1, SimpleMatch m2, boolean isBidirectionalFlow) {
        if (isBidirectionalFlow==false) {

            if (!m1.getIpv4_src().equals(m2.getIpv4_src())) {
                return false;
            }

            if (!m1.getIpv4_dst().equals(m2.getIpv4_dst())) {
                return false;
            }
            if (!Objects.equals(m1.getPort_src(), m2.getPort_src())) {

                return false;
            }
            if (!Objects.equals(m1.getPort_dst(), m2.getPort_dst())) {

                return false;
            }

            return true;

        } else {
            return false;
        }

    }

    public SimpleMatch() {
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

    public Integer getPort_src() {
        return port_src;
    }

    public void setPort_src(Integer port_src) {
        this.port_src = port_src;
    }

    public Integer getPort_dst() {
        return port_dst;
    }

    public void setPort_dst(Integer port_dst) {
        this.port_dst = port_dst;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.ipv4_src);
        hash = 67 * hash + Objects.hashCode(this.ipv4_dst);
        hash = 67 * hash + Objects.hashCode(this.port_src);
        hash = 67 * hash + Objects.hashCode(this.port_dst);
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
        final SimpleMatch other = (SimpleMatch) obj;
        if (!Objects.equals(this.ipv4_src, other.ipv4_src)) {
            return false;
        }
        if (!Objects.equals(this.ipv4_dst, other.ipv4_dst)) {
            return false;
        }
        if (!Objects.equals(this.port_src, other.port_src)) {
            return false;
        }
        if (!Objects.equals(this.port_dst, other.port_dst)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ipv4_src=" + ipv4_src + "|ipv4_dst=" + ipv4_dst + "|port_src=" + port_src + "|port_dst=" + port_dst;
    }

}
