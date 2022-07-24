/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computingflows;

import ids.statistics.storage.StatsDBInterfacePooled;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Moubarak
 */
public class FlowsFrequenciesComputing {
// on considère que les packetIns envoyées sont triées

    public static void main(String[] args) {
        StatsDBInterfacePooled dBInterfacePooled = new StatsDBInterfacePooled();
        long X = System.currentTimeMillis();
        int k = 0;

        ResultSet resultSet = dBInterfacePooled.selectFromTable("flows_table_packetCount", "all", "0x0", 0, X, 1);

        MatchingMap matchingMap = parseToMatchingMap(resultSet);

        for (Map.Entry<String, ArrayList<TimeStampedValue>> entry : matchingMap.getMap().entrySet()) {
            // System.out.print(entry.getKey() + "                                                 -------->  ");
            ArrayList<TimeStampedValue> v = entry.getValue();

            for (TimeStampedValue x : v) {

                //System.out.print(x.toString());
                k++;

            }

            System.out.println("");
        }

        System.out.println("----- " + k);

        ResultSet R2 = dBInterfacePooled.selectFromTable("packetIns_table2", "all", "0x0", 0, X, 1);
        ResultSet R3 = dBInterfacePooled.selectFromTable("flowRemoved_table_packetCount", "all", "0x0", 0, X, 1);

        Map<String, ArrayList<Long>> packettInsSorterArrivals = parseToPacketInsArrivalsMap(R2);

        MatchingMap flowsRemovedStampedValuesMap = parseToMatchingMap(R3);
        ArrayList<Long> frequencies = countFlowsFrequencies(matchingMap, packettInsSorterArrivals, flowsRemovedStampedValuesMap.getMap());
        int j=0;
        for(long f:frequencies){
            System.out.println("f"+(++j)+" : "+f);
        
        }

    }

    public static MatchingMap parseToMatchingMap(ResultSet resultSet) {
        MatchingMap matchingMap = new MatchingMap();

        try {
            while (resultSet.next()) {

                long time_stamp = resultSet.getLong("time_stamp");
                long packetCount = resultSet.getLong("packetCount") + 1;
                String matching = resultSet.getString("matching");

                ArrayList<TimeStampedValue> timeStampedValuesList = matchingMap.getKeyValue(matching);

                if (timeStampedValuesList == null) {
                    timeStampedValuesList = new ArrayList<>();

                }

                timeStampedValuesList.add(new TimeStampedValue(time_stamp, packetCount));
                matchingMap.put(matching, timeStampedValuesList);

            }

        } catch (SQLException ex) {
            Logger.getLogger(FlowsFrequenciesComputing.class.getName()).log(Level.SEVERE, null, ex);
        }

        return matchingMap;
    }

    public static Map<String, ArrayList<Long>> parseToPacketInsArrivalsMap(ResultSet resultSet) {
        Map<String, ArrayList<Long>> map = new HashMap<>();

        try {
            while (resultSet.next()) {

                long time_stamp = resultSet.getLong("time_stamp");

                String matching = resultSet.getString("matching");

                ArrayList<Long> timeStampedValuesList = map.get(matching);

                if (timeStampedValuesList == null) {
                    timeStampedValuesList = new ArrayList<>();

                }

                timeStampedValuesList.add(time_stamp);
                map.put(matching, timeStampedValuesList);

            }

        } catch (SQLException ex) {
            Logger.getLogger(FlowsFrequenciesComputing.class.getName()).log(Level.SEVERE, null, ex);
        }

        return map;
    }

    public static long[] getTimeStampsList(ArrayList<TimeStampedValue> timeStampedValues) {

        int size = timeStampedValues.size();

        if (size == 0) {
            return null;
        }

        long[] timeStampsList = new long[size];
        for (int i = 0; i < size; i++) {
            timeStampsList[i] = timeStampedValues.get(i).getTime_stamp();

        }

        return timeStampsList;
    }

    public static ArrayList<Long> countFlowsFrequencies(MatchingMap flowsMatchingMap, Map<String, ArrayList<Long>> packettInsSorterArrivals, Map<String, ArrayList<TimeStampedValue>> flowsRemovedStampedValuesMap) {
        ArrayList<Long> frequencies = new ArrayList<>();
        for (Map.Entry<String, ArrayList<TimeStampedValue>> flows : flowsMatchingMap.getMap().entrySet()) {
            String aFlowMatching = flows.getKey();

            ArrayList<Long> hisPacketsInsArrivals = packettInsSorterArrivals.get(aFlowMatching);
            ArrayList<TimeStampedValue> hisFlowsRemoved = flowsRemovedStampedValuesMap.get(aFlowMatching);
            ArrayList<TimeStampedValue> hisTimeStampedValues = flowsMatchingMap.getKeyValue(aFlowMatching);
            if (hisPacketsInsArrivals == null) { // à revoir aboslument
                hisPacketsInsArrivals = new ArrayList<>();
            }

            long f = countFlowsPacketCount(hisTimeStampedValues, hisPacketsInsArrivals, hisFlowsRemoved);

            frequencies.add(f);

        }

        //sorte
        return frequencies;
    }

    public static long countFlowsPacketCount(ArrayList<TimeStampedValue> sorteTimeStampedValues, ArrayList<Long> packettInsSorterArrivals, ArrayList<TimeStampedValue> thisFlowRemovedStampedValues) {

        if (packettInsSorterArrivals.size() <= 0) {

            int lastIndex = sorteTimeStampedValues.size() - 1;
            return sorteTimeStampedValues.get(lastIndex).getNumber() - sorteTimeStampedValues.get(0).getNumber();
        }

        long[] timeStampsList = getTimeStampsList(sorteTimeStampedValues);

        ArrayList<Interval> sortedIntervals = makeIntervals(timeStampsList, packettInsSorterArrivals);

        long totalCount = 0;
        for (Interval anInterval : sortedIntervals) {
            totalCount += getFlowPacketCountInVirtualInterval(anInterval, sorteTimeStampedValues, thisFlowRemovedStampedValues);

        }

       totalCount = totalCount - sorteTimeStampedValues.get(0).getNumber();
        return totalCount;

    }

    public static ArrayList<Interval> makeIntervals(long[] timeStampsList, ArrayList<Long> packettInsSortedArrivals) {
        Collections.sort(packettInsSortedArrivals);
        Arrays.sort(timeStampsList);

        ArrayList<Interval> intervalList = new ArrayList<>();
        if (packettInsSortedArrivals.isEmpty()) {
            long inf = timeStampsList[0];
            long sup = timeStampsList[timeStampsList.length - 1];
            intervalList.add(new Interval(inf, sup));
            return intervalList;

        }

        Interval firstInterval = new Interval(timeStampsList[0], packettInsSortedArrivals.get(0));
        
         System.out.print("[ "+firstInterval.getInf()+" , "+firstInterval.getSup()+"] || ");
        int i ;
        for (i = 0; i < packettInsSortedArrivals.size() - 1; i++) {
            Interval anInterval = new Interval();
            long inf = nextTimeSampAfterPacketIn(packettInsSortedArrivals.get(i), timeStampsList);
            long sup = packettInsSortedArrivals.get(i + 1);
            anInterval.setInf(inf);
            anInterval.setSup(sup);
            intervalList.add(i, anInterval);
            System.out.print("] "+inf+" , "+sup+"] || ");
        }
        long a = nextTimeSampAfterPacketIn(packettInsSortedArrivals.get(packettInsSortedArrivals.size() - 1), timeStampsList);
        long b = timeStampsList[timeStampsList.length - 1]; // last element

        Interval lastInterval = new Interval(a, b);
        //System.out.println("computingflows.FlowsFrequenciesComputing.makeIntervals()");
        //intervalList.add(++i, lastInterval);
        
        System.out.println("] "+a+" , "+b+"] || ");
        
        System.out.println("*************************************************************");
        intervalList.add(lastInterval);

        return intervalList;
    }

    public static long checkIfFlowRemovedIn(Interval aInterval, ArrayList<TimeStampedValue> flowRemovedStampedValues) {
        if (flowRemovedStampedValues == null) {
            return -1;
        }
        for (TimeStampedValue v : flowRemovedStampedValues) {
            if (((v.getTime_stamp() <= aInterval.getSup()) && (v.getTime_stamp() >= aInterval.getInf()))) {
                return v.getNumber();
            }

        }
        return -1;
     
    }

    public static long getFlowPacketCountInVirtualInterval(Interval aInterval, ArrayList<TimeStampedValue> thisFlowStampedValues,
            ArrayList<TimeStampedValue> thisFlowRemovedStampedValues) {
        if (aInterval.getInf() == aInterval.getSup()) {
            System.out.println("computingflows.FlowsFrequenc******************************iesComputing.getFlowPacketCountInVirtualInterval()");
            return 0;
        } else {
            long v = checkIfFlowRemovedIn(aInterval, thisFlowRemovedStampedValues);
            if (v >= 0) {
                return v;
            }
            return getMax(aInterval, thisFlowStampedValues);
        }
    }

    public static boolean isTimeSampedValueInInterval(Interval aInterval, TimeStampedValue value) {
        return ((value.getTime_stamp() <= aInterval.getSup()) && (value.getTime_stamp() >= aInterval.getInf()));
    }

    public static long getMax(Interval aInterval, ArrayList<TimeStampedValue> thisFlowStampedValues) {
        long max = 0;

        for (TimeStampedValue value : thisFlowStampedValues) {
            if (isTimeSampedValueInInterval(aInterval, value)) {
                if (value.getNumber() > max) {
                    max = value.getNumber();
                }

            }

        }
        return max;
    }

    private static long nextTimeSampAfterPacketIn(long packetInsArrival, long[] timeStampList) {
        long result = timeStampList[timeStampList.length - 1];
        for (long timeStamp : timeStampList) {
            if (timeStamp > packetInsArrival) {

                return result;
            }
        }
        return result;
    }

}
