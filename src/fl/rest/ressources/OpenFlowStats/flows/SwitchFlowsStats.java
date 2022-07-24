/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.flows;

import java.util.Arrays;

/**
 *
 * @author Moubarak
 */
public class SwitchFlowsStats {

    private FlowEntryStats[] flows;
    private long time_stamp;

    public SwitchFlowsStats(FlowEntryStats[] flows, long time_stamp) {
        this.flows = flows;
        this.time_stamp = time_stamp;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }
    
    
    

    public SwitchFlowsStats(FlowEntryStats[] flows) {
        this.flows = flows;
    }

    public FlowEntryStats[] getFlows() {
        return flows;
    }

    public void setFlows(FlowEntryStats[] flows) {
        this.flows = flows;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Arrays.deepHashCode(this.flows);
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
        final SwitchFlowsStats other = (SwitchFlowsStats) obj;
        if (!Arrays.deepEquals(this.flows, other.flows)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SwitchFlows{" + "flows=" + flows + '}';
    }

}
