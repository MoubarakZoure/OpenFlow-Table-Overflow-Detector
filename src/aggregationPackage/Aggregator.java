/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aggregationPackage;

import fl.rest.ressources.OpenFlowStats.Counters.SimpleCounter;
import fl.rest.ressources.OpenFlowStats.aggregate.Aggregate;
import fl.rest.ressources.OpenFlowStats.flowsRemoved.SimpleFlowRemoved;
import fl.rest.ressources.OpenFlowStats.port.PortActivity;
import fl.rest.ressources.OpenFlowStats.tablestats.TableStats;
import fl.rest.ressources.controller.ControllerMemoryUsage;
import fl.rest.ressources.statcollector.PortBandwidth;
import java.util.ArrayList;
import org.apache.commons.math.stat.StatUtils;

/**
 *
 * @author Moubarak
 */
public class Aggregator {

    public static final String MIN_AGGREGATOR = "min";
    public static final String MAX_AGGREGATOR = "max";
    public static final String MEAN_AGGREGATOR = "mean";
    public static final String VARIANCE_AGGREGATOR = "variance";
    public static final String GEOMETRIC_MEAN_AGGREGATOR = "geometric_mean_aggregator";
    public static final String HARMONIC_MEAN_AGGREGATOR = "harmonic_min_aggregator";
    public static final String SUM_AGGREGATOR = "sum";
    public static final String STANDARDIZE_TRANSFORMATION = "sum";

    public Aggregate aggregate(ArrayList<Aggregate> aggregate_set, String aggregator) {
        int size = aggregate_set.size();
        double[] byteCountTab = new double[size];
        double[] packetCountTab = new double[size];
        double[] flowCountTab = new double[size];

        int i = 0;
        for (Aggregate a : aggregate_set) {

            byteCountTab[i] = a.getByteCount();
            packetCountTab[i] = a.getPacketCount();
            flowCountTab[i] = a.getFlowCount();
            i = i + 1;

        }

        long byteCount = (long) aggregateValues(byteCountTab, aggregator);
        long packetCount = (long) aggregateValues(packetCountTab, aggregator);
        int flowCount = (int) aggregateValues(flowCountTab, aggregator);

        return new Aggregate(byteCount, Integer.MIN_VALUE, flowCount, packetCount, null);

    }

    public TableStats aggregateTableStats(ArrayList<TableStats> a_set, String aggregator) {
        int size = a_set.size();
        double[] activeCountTab = new double[size];
        double[] matchCountTab = new double[size];
        double[] lookUpCountTab = new double[size];

        int i = 0;
        for (TableStats a : a_set) {
            activeCountTab[i] = a.getActiveCount();
            lookUpCountTab[i] = a.getLookUpCount();
            matchCountTab[i] = a.getMatchCount();
            i = i + 1;

        }

        int matchCount = (int) aggregateValues(matchCountTab, aggregator);
        int activeCount = (int) aggregateValues(activeCountTab, aggregator);
        int lookUpCount = (int) aggregateValues(lookUpCountTab, aggregator);

        return new TableStats(activeCount, lookUpCount, matchCount, null);

    }

    public ControllerMemoryUsage aggregateControlerMemoryUsage(ArrayList<ControllerMemoryUsage> a_set, String aggregator) {
        int size = a_set.size();
        double[] totalTab = new double[size];
        double[] freeTab = new double[size];
        double[] percentageTab = new double[size];

        int i = 0;
        for (ControllerMemoryUsage a : a_set) {
            totalTab[i] = a.getTotal();
            freeTab[i] = a.getFree();
            percentageTab[i] = a.getUsagePercentage();
            i = i + 1;

        }

        if (!aggregator.equals(Aggregator.GEOMETRIC_MEAN_AGGREGATOR)) {
            long total = (long) aggregateValues(totalTab, aggregator);
            long free = (long) aggregateValues(freeTab, aggregator);
            double percentage = aggregateValues(percentageTab, aggregator);
            ControllerMemoryUsage r = new ControllerMemoryUsage(total, free);
            r.setUsagePercentage(percentage);
            return r;
        } else {

            for (int j = 0; j < size; j++) {
                percentageTab[j] = totalTab[j] / (totalTab[j]+freeTab[j]);
            }

            double percentage = aggregateValues(percentageTab, aggregator);
            ControllerMemoryUsage r = new ControllerMemoryUsage(-1, -1);
            r.setUsagePercentage(percentage);
            return r;

        }

    }

    public PortBandwidth aggregatePortBandwidth(ArrayList<PortBandwidth> a_set, String aggregator) {
        int size = a_set.size();
        double[] rxTab = new double[size];
        double[] txTab = new double[size];

        int i = 0;
        for (PortBandwidth a : a_set) {
            rxTab[i] = a.getRx();
            txTab[i] = a.getTx();
            i = i + 1;

        }

        long rx = (long) Aggregator.aggregateValues(rxTab, aggregator);
        long tx = (long) Aggregator.aggregateValues(txTab, aggregator);

        return new PortBandwidth(rx, tx, null, null, null);

    }

    public PortActivity aggregatePortActivity(ArrayList<PortActivity> a_set, String aggregator) {
        int size = a_set.size();

        double[] collisionsTab = new double[size];
        double[] durationNsecTab = new double[size];
        double[] durationSecTab = new double[size];
        double[] receiveBytesTab = new double[size];
        double[] receiveCRCErrorsTab = new double[size];
        double[] receiveDroppedTab = new double[size];
        double[] receiveErrorsTab = new double[size];
        double[] receiveFrameErrorsTab = new double[size];
        double[] receiveOverrunErrorsTab = new double[size];
        double[] receivePacketsTab = new double[size];
        double[] transmitBytesTab = new double[size];
        double[] transmitDroppedTab = new double[size];
        double[] transmitErrorsTab = new double[size];
        double[] transmitPacketsTab = new double[size];

        int i = 0;
        for (PortActivity a : a_set) {
            collisionsTab[i] = a.getCollisions();
            durationNsecTab[i] = a.getDurationNsec();
            durationSecTab[i] = a.getDurationSec();
            receiveBytesTab[i] = a.getReceiveBytes();
            receiveCRCErrorsTab[i] = a.getReceiveCRCErrors();
            receiveDroppedTab[i] = a.getReceiveDropped();
            receiveErrorsTab[i] = a.getReceiveErrors();
            receiveFrameErrorsTab[i] = a.getReceiveFrameErrors();
            receiveOverrunErrorsTab[i] = a.getReceiveOverrunErrors();
            receivePacketsTab[i] = a.getReceivePackets();
            transmitBytesTab[i] = a.getTransmitBytes();
            transmitDroppedTab[i] = a.getTransmitDropped();
            transmitErrorsTab[i] = a.getTransmitErrors();
            transmitPacketsTab[i] = a.getTransmitPackets();

            i = i + 1;

        }
        long collisions = (long) aggregateValues(collisionsTab, aggregator);
        long durationNsec = (long) aggregateValues(durationNsecTab, aggregator);
        long durationSec = (long) aggregateValues(durationSecTab, aggregator);
        long receiveBytes = (long) aggregateValues(receiveBytesTab, aggregator);
        long receiveCRCErrors = (long) aggregateValues(receiveCRCErrorsTab, aggregator);
        long receiveDropped = (long) aggregateValues(receiveDroppedTab, aggregator);
        long receiveErrors = (long) aggregateValues(receiveErrorsTab, aggregator);
        long receiveFrameErrors = (long) aggregateValues(receiveFrameErrorsTab, aggregator);
        long receiveOverrunErrors = (long) aggregateValues(receiveOverrunErrorsTab, aggregator);
        long receivePackets = (long) aggregateValues(receivePacketsTab, aggregator);
        long transmitBytes = (long) aggregateValues(transmitBytesTab, aggregator);
        long transmitDropped = (long) aggregateValues(transmitDroppedTab, aggregator);
        long transmitErrors = (long) aggregateValues(transmitErrorsTab, aggregator);
        long transmitPackets = (long) aggregateValues(transmitPacketsTab, aggregator);

        return new PortActivity(collisions, durationNsec, durationSec, null, receiveBytes, receiveCRCErrors, receiveDropped, receiveErrors,
                receiveFrameErrors, receiveOverrunErrors, receivePackets, transmitBytes, transmitDropped, transmitErrors, transmitPackets);

    }

    public SimpleCounter aggregateSimpleCosunter(ArrayList<SimpleCounter> a_set, String aggregator) {

        /* double[] totalCountTab = new double[size];
        
        int i = 0;
        for (SimpleCounter a : a_set) {
            totalCountTab[i] = a.getTotal_count();
            i = i + 1;
            
        }*/
        int totalCount = a_set.size();

        return new SimpleCounter(totalCount);

    }

    public SimpleFlowRemoved aggregateFlowRemoved(ArrayList<SimpleFlowRemoved> a_set, String aggregator) {
        int size = a_set.size();
        double[] duration_secTab = new double[size];
        double[] duration_nsecTab = new double[size];
        double[] byteCountTab = new double[size];
        double[] packetCountTab = new double[size];
        //  double[] totalCountTab = new double[size];

        int i = 0;
        for (SimpleFlowRemoved a : a_set) {
            duration_secTab[i] = a.getDuration_sec();
            duration_nsecTab[i] = a.getDuration_nsec();
            byteCountTab[i] = a.getByteCount();
            packetCountTab[i] = a.getPacketCount();

            i = i + 1;

        }

        int duration_sec = (int) aggregateValues(duration_secTab, aggregator);
        long duration_nsec = (long) aggregateValues(duration_nsecTab, aggregator);
        int byteCount = (int) aggregateValues(byteCountTab, aggregator);
        int packetCount = (int) aggregateValues(packetCountTab, aggregator);
        int total_count = size;

        return new SimpleFlowRemoved(null, duration_sec, duration_nsec, byteCount, packetCount, total_count);

    }

    public static double aggregateValues(double[] values, String aggregator) {
        switch (aggregator) {
            case Aggregator.MIN_AGGREGATOR:
                return min(values);
            case Aggregator.MAX_AGGREGATOR:
                return max(values);
            case Aggregator.MEAN_AGGREGATOR:
                return mean(values);
            case Aggregator.VARIANCE_AGGREGATOR:
                return variance(values);
            case Aggregator.SUM_AGGREGATOR:
                return sum(values);
            case Aggregator.GEOMETRIC_MEAN_AGGREGATOR:
                return geometricMean(values);
            case Aggregator.HARMONIC_MEAN_AGGREGATOR:
                return harmonicMean(values);
            default: {

                return Double.NEGATIVE_INFINITY;
            }

        }

    }

    public static double[] transformData(double[] values, String transformator) {

        switch (transformator) {

            case Aggregator.STANDARDIZE_TRANSFORMATION:
                return normalize(values);
            default:
                return null;

        }

    }

    public static double min(double[] values) {

        return StatUtils.min(values);

    }

    public static double max(double[] values) {

        return StatUtils.max(values);

    }

    public static double variance(double[] values) {

        double n = values.length;
        n = (n - 1) / n;

        return n * StatUtils.variance(values);

    }

    public static double mean(double[] values) {

        return StatUtils.mean(values);

    }

    public static double sum(double[] values) {

        return StatUtils.sum(values);

    }

    public static double geometricMean(double[] values) {

        return StatUtils.geometricMean(values);

    }

    public static double[] normalize(double[] values) {

        return StatUtils.normalize(values);

    }

    public static double harmonicMean(double[] values) {

        double[] inversed_values = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            inversed_values[i] = 1 / values[i];

        }

        double mean_of_inversed_values = StatUtils.mean(inversed_values);

        double harmonic_mean = 1 / mean_of_inversed_values;

        return harmonic_mean;

    }

    public static void main(String[] args) {
        int[] tab = new int[]{1, 2, 3, 4, 5, 6, 7};
        double[] values = new double[tab.length];

        for (int i = 0; i < tab.length; i++) {

            values[i] = tab[i];

        }

        double min = Aggregator.aggregateValues(values, MIN_AGGREGATOR);
        double max = Aggregator.aggregateValues(values, MAX_AGGREGATOR);
        double mean = Aggregator.aggregateValues(values, MEAN_AGGREGATOR);
        double geo_mean = Aggregator.aggregateValues(values, GEOMETRIC_MEAN_AGGREGATOR);
        double h_mean = Aggregator.aggregateValues(values, HARMONIC_MEAN_AGGREGATOR);
        double variance = Aggregator.aggregateValues(values, VARIANCE_AGGREGATOR);
        double sum = Aggregator.aggregateValues(values, SUM_AGGREGATOR);

        double[] normalized_values = Aggregator.transformData(values, STANDARDIZE_TRANSFORMATION);
        System.out.println(" Original Data");

        for (double v : values) {

            System.out.print(v + "  ");

        }
        System.out.println(" Normalized Data");

        for (double v : normalized_values) {

            System.out.print(v + "  ");

        }

        System.out.println(" Min value " + min);
        System.out.println(" Max value " + max);
        System.out.println(" Mean value " + mean);
        System.out.println(" geao_mean value " + geo_mean);
        System.out.println(" h_mean value " + h_mean);
        System.out.println(" sum value " + sum);

        double n = values.length;

        System.out.println(" variance value " + variance + " ");

    }

}
