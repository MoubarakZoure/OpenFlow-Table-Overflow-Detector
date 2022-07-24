/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computingflows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Moubarak
 */
public class MatchingMap {

    Map<String, ArrayList<TimeStampedValue>> map = new HashMap<>();

    public MatchingMap() {
    }

    public void put(String key, ArrayList<TimeStampedValue> value) {
        this.map.put(key, value);

    }

    public ArrayList<TimeStampedValue> getKeyValue(String matching) {

        return this.map.get(matching);
    }

    public boolean containKey(String matching) {

        return this.map.containsKey(matching);

    }

    public long[] getTheMatchingTimeStampesList(String matching, boolean order) {
        long[] result;
        ArrayList<TimeStampedValue> timeStamedValues = getTheMatchingTimeStampedValues(matching);
        if (timeStamedValues == null) {
            return null;
        }
        result = new long[timeStamedValues.size()];
        int k = 0;
        for (TimeStampedValue v : timeStamedValues) {
            result[k] = v.getTime_stamp();
            k = k + 1;

        }
        if (order) {

            Arrays.sort(result);
        }

        return result;
    }

    public ArrayList<TimeStampedValue> getTheMatchingTimeStampedValues(String matching) {

        return map.get(matching);
    }

    public Map<String, ArrayList<TimeStampedValue>> getMap() {
        return map;
    }

    public void setMap(Map<String, ArrayList<TimeStampedValue>> map) {
        this.map = map;
    }

    @Override
    public String toString() {

        return "MatchingMap{" + "map=" + map + '}';
    }

}
