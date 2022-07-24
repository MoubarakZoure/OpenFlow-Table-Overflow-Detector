/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.aggregate;

import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class FlowsAggregate {
    
    private Aggregate aggregate;
    private long time_stamp;

    public FlowsAggregate(Aggregate aggregate, long time_stamp) {
        this.aggregate = aggregate;
        this.time_stamp = time_stamp;
    }
    
    
    

    public FlowsAggregate(Aggregate aggregate) {
        this.aggregate = aggregate;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.aggregate);
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
        final FlowsAggregate other = (FlowsAggregate) obj;
        if (!Objects.equals(this.aggregate, other.aggregate)) {
            return false;
        }
        return true;
    }
    
    
    

    public Aggregate getAggregate() {
        return aggregate;
    }

    public void setAggregate(Aggregate aggregate) {
        this.aggregate = aggregate;
    }
    
    
    

    @Override
    public String toString() {
        return "FlowsAggregate{" + "aggregate=" + aggregate + '}';
    }
    
    
}
