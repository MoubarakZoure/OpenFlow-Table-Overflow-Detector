/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.port;

import java.util.Arrays;

/**
 *
 * @author Moubarak
 */
public class SwitchPortsActivities {

    private PortsActivities[] port_reply;

    public SwitchPortsActivities(PortsActivities[] port_reply) {
        this.port_reply = port_reply;
    }

    public PortsActivities[] getPort_reply() {
        return port_reply;
    }

    public void setPort_reply(PortsActivities[] port_reply) {
        this.port_reply = port_reply;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Arrays.deepHashCode(this.port_reply);
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
        final SwitchPortsActivities other = (SwitchPortsActivities) obj;
        if (!Arrays.deepEquals(this.port_reply, other.port_reply)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SwitchPortsActivities{" + "port_reply=" + port_reply + '}';
    }

  

}
