/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.Counters;

/**
 *
 * @author Moubarak
 */
public class SimpleCounter {
    int total_count;
    private long time_stamp;

    public SimpleCounter(int total_count, long time_stamp) {
        this.total_count = total_count;
        this.time_stamp = time_stamp;
    }
    
    

    public SimpleCounter(int total_count) {
        this.total_count = total_count;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.total_count;
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
        final SimpleCounter other = (SimpleCounter) obj;
        if (this.total_count != other.total_count) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SimpleCounter{" + "total_count=" + total_count + '}';
    }
    
    
    
    
}
