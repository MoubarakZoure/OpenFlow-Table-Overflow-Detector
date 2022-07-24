/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.controller;

/**
 *
 * @author Moubarak
 */
public class ControllerUptime {

    private  long systemUptimeMsec;

    public ControllerUptime(long systemUptimeMsec) {
        this.systemUptimeMsec = systemUptimeMsec;
    }

    public long getSystemUptimeMsec() {
        return systemUptimeMsec;
    }

    public void setSystemUptimeMsec(long systemUptimeMsec) {
        this.systemUptimeMsec = systemUptimeMsec;
    }

    @Override
    public String toString() {
        return "ControllerUptime{" + "systemUptimeMsec=" + systemUptimeMsec + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.systemUptimeMsec ^ (this.systemUptimeMsec >>> 32));
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
        final ControllerUptime other = (ControllerUptime) obj;
        if (this.systemUptimeMsec != other.systemUptimeMsec) {
            return false;
        }
        return true;
    }
    
    
    
    

}
