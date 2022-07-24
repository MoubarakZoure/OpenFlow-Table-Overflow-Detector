/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.port;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class PortsActivities {

    private PortActivity[] port;
    private String version;

    public PortActivity[] getPort() {
        return port;
    }

    public void setPort(PortActivity[] port) {
        this.port = port;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Arrays.deepHashCode(this.port);
        return hash;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {

        String r = "PortsActivities{" + "port=";

        for (PortActivity p : port) {

            r += p.toString();

        }
        r += ", version=" + version + '}';
        return r;
    }

    public PortsActivities(PortActivity[] port, String version) {
        this.port = port;
        this.version = version;
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
        final PortsActivities other = (PortsActivities) obj;
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Arrays.deepEquals(this.port, other.port)) {
            return false;
        }
        return true;
    }

}
