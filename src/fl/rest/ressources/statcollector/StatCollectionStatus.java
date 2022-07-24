/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.statcollector;

import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class StatCollectionStatus {

    private String statistics_collection;

    public StatCollectionStatus(String statistics_collection) {
        this.statistics_collection = statistics_collection;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.statistics_collection);
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
        final StatCollectionStatus other = (StatCollectionStatus) obj;
        if (!Objects.equals(this.statistics_collection, other.statistics_collection)) {
            return false;
        }
        return true;
    }

    
    
    @Override
    public String toString() {
        return "StatCollectionStatus{" + "statistics_collection=" + statistics_collection + '}';
    }

    public String getStatistics_collection() {
        return statistics_collection;
    }

    public void setStatistics_collection(String statistics_collection) {
        this.statistics_collection = statistics_collection;
    }

   

    
}
