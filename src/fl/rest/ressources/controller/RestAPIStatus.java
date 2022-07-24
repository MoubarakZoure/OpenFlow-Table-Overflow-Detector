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
public class RestAPIStatus {

    private boolean healthy;

    public RestAPIStatus(boolean healthy) {
        this.healthy = healthy;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.healthy ? 1 : 0);
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
        final RestAPIStatus other = (RestAPIStatus) obj;
        if (this.healthy != other.healthy) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RestAPIStatus{" + "healthy=" + healthy + '}';
    }
    
    

}
