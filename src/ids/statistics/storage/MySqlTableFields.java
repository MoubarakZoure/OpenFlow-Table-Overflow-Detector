/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ids.statistics.storage;

/**
 *
 * @author Moubarak
 */
public class MySqlTableFields {

    public static String[] AGGREGATE_FLOW_TABLE = {
        "byteCount",
        "flags",
        "flowCount",
        "packetCount"};

    public static String[] TABLE_STATS = {
        "activeCount",
        "lookUpCount",
        "matchCount"};
    public static String[] PORT_BANDWIDTH_TABLE = {
        "rx",
        "tx"};

    public static String[] CONTROLER_MEMORY_USAGE = {
        "total",
        "free"};

    public static String[] FLOW_REMOVED = {
        "total_count",
        "duration_sec",
        "duration_nsec",
        "byteCount",
        "packetCount"
    };

    public static String[] SIMPLE_COUNTER = {
        "total_count"
    };

    public static String[] SWITCH_PORT_ACTIVITY = {
        "collisions",
        "durationNsec",
        "durationSec",
        "receiveBytes",
        "receiveCRCErrors",
        "receiveDropped",
        "receiveErrors",
        "receiveFrameErrors",
        "receiveOverrunErrors",
        "receivePackets",
        "transmitBytes",
        "transmitDropped",
        "transmitErrors",
        "transmitPackets"};

    public static String[] FLOWS_TABLE = {
        "byteCount",
        "durationNSeconds",
        "durationSeconds",
        "flags",
        "packetCount"};

}
