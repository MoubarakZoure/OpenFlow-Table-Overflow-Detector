/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.tablestats;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class SwitchTablesStats {

    private TableStats[] table; // toutes les tables du switch
    private String version;
     private long time_stamp;

    public SwitchTablesStats(TableStats[] table, String version, long time_stamp) {
        this.table = table;
        this.version = version;
        this.time_stamp = time_stamp;
    }
     
     
     

    public SwitchTablesStats(TableStats[] table, String version) {
        this.table = table;
        this.version = version;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

    
    
    
    
    public TableStats[] getTable() {
        return table;
    }

    public void setTable(TableStats[] table) {
        this.table = table;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Arrays.deepHashCode(this.table);
        hash = 23 * hash + Objects.hashCode(this.version);
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
        final SwitchTablesStats other = (SwitchTablesStats) obj;
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Arrays.deepEquals(this.table, other.table)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SwitchTablesStats{" + "table=" + table + ", version=" + version + '}';
    }

}
