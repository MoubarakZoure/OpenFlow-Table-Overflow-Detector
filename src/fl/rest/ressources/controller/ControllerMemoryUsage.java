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
public class ControllerMemoryUsage {

    private long total;
    private long free;
    private double usagePercentage = -1;

    public ControllerMemoryUsage(long total, long free) {
        this.total = total;
        this.free = free;
        computeUsagePercentage();
    }

    private double computeUsagePercentage() {
       
        double n=total;
        double d=(total+free);
       
        this.usagePercentage = n/d;
        
        return this.usagePercentage;
        

    }

    ;
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
        computeUsagePercentage();
    }

    public long getFree() {
        return free;
    }

    public void setFree(long free) {
        this.free = free;
        computeUsagePercentage();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (int) (this.total ^ (this.total >>> 32));
        hash = 79 * hash + (int) (this.free ^ (this.free >>> 32));
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
        final ControllerMemoryUsage other = (ControllerMemoryUsage) obj;
        if (this.total != other.total) {
            return false;
        }
        if (this.free != other.free) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ControllerMemoryUsage{" + "total=" + total + ", free=" + free + ", usagePercentage=" + usagePercentage + '}';
    }

    public double getUsagePercentage() {
        return usagePercentage;
    }

    public void setUsagePercentage(double usagePercentage) {
        this.usagePercentage = usagePercentage;
    }

}
