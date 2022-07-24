/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.tablestats;

import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class TableStats {

    private int activeCount;
    private int lookUpCount;
    private int matchCount;
    private long time_stamp;
    private String tableID;

    public TableStats(int activeCount, int lookUpCount, int matchCount, long time_stamp, String tableID) {
        this.activeCount = activeCount;
        this.lookUpCount = lookUpCount;
        this.matchCount = matchCount;
        this.time_stamp = time_stamp;
        this.tableID = tableID;
    }
    
    

    public TableStats(int activeCount, int lookUpCount, int matchCount, String tableID) {
        this.activeCount = activeCount;
        this.lookUpCount = lookUpCount;
        this.matchCount = matchCount;
        this.tableID = tableID;
    }

    public int getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(int activeCount) {
        this.activeCount = activeCount;
    }

    public int getLookUpCount() {
        return lookUpCount;
    }

    public void setLookUpCount(int lookUpCount) {
        this.lookUpCount = lookUpCount;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(int matchCount) {
        this.matchCount = matchCount;
    }

    public String getTableID() {
        return tableID;
    }

    public void setTableID(String tableID) {
        this.tableID = tableID;
    }

   

    public void setTableId(String tableId) {
        this.tableID = tableId;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.activeCount;
        hash = 17 * hash + this.lookUpCount;
        hash = 17 * hash + this.matchCount;
        hash = 17 * hash + Objects.hashCode(this.tableID);
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
        final TableStats other = (TableStats) obj;
        if (this.activeCount != other.activeCount) {
            return false;
        }
        if (this.lookUpCount != other.lookUpCount) {
            return false;
        }
        if (this.matchCount != other.matchCount) {
            return false;
        }
        if (!Objects.equals(this.tableID, other.tableID)) {
            return false;
        }
        return true;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

    @Override
    public String toString() {
        return "TableStats{" + "activeCount=" + activeCount + ", lookUpCount=" + lookUpCount + ", matchCount=" + matchCount + ", time_stamp=" + time_stamp + ", tableID=" + tableID + '}';
    }

    

  

    
    

}
