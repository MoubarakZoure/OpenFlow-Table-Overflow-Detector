package fl.rest.ressources.urls;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Moubarak
 */
public class RestUrlBuilder {

    // controller
    public static final String SWITCHES_LIST_PATH = "/wm/core/controller/switches/json"; // List of all switch DPIDs connected to the controller
    public static final String CONTROLLER_MEMORY_USAGE = "/wm/core/memory/json"; // Current controller memory usage
    public static final String REST_API_HEALTH_STATUS = "/wm/core/health/json"; //Status/Health of REST API
    public static final String CONTROLLER_UP_TIME = "/wm/core/system/uptime/json"; // Controller uptime = durée de fonctionnemnt du controlleur
    // OpenFlow Stats / Multipart APIs
    public static final String OPF_STATS_ALL = "/wm/core/switch/all"; // Retrieve aggregate stats across all switches. Some stats not supported by all all OpenFlow versions.  Meters untested
    public static final String OPF_STATS_SWITCH = "/wm/core/switch"; // Retrieve per switch stats.  Some stats not supported by all all OpenFlow versions.  Meters untested.

    //Statistics APIs
    public static final String ENABLING_STATISTICS_COLLECTION = "/wm/statistics/config/enable/json"; // Enable statistics collection POST/PUT
    public static final String DISABLING_STATISTICS_COLLECTION = "/wm/statistics/config/disable/json"; // Disable statistics POST/PUT
    public static final String SWTICH_BANDWITH = "/wm/statistics/bandwidth";
    //Static Entry Pusher APIs
    public static final String ADD_DEL_FLOW = "/wm/staticflowpusher/json";// Add Delete static flow POST/PUT
    public static final String LIST_SWITCH_FLOWS = "/wm/staticflowpusher/list"; // List static flows for a switch or all switches GET
    public static final String CLEAR_SWITCH_FLOWS = "/wm/staticflowpusher/clear"; // Clear static flows for a switch or all switches GET

    //Access Control List APIs
    public static final String ADD_RULE = "/wm/acl/rules/json";
    public static final String DEL_RULE = "/wm/acl/rules/json";
    public static final String LIST_RULES = "/wm/acl/rules/json";
    public static final String CLEAR_RULES = "/wm/acl/clear/json";
    
    public static final String PACKET_INS_STATS="/ids/stats";
    public static final String FLOW_REMOVED_STATS="/ids/stats";
     public static final String COUNTER_STATS="/ids/stats/counter";

    private String controller_ip = "127.0.0.1";
    private String controller_port = "8080";
    public String protocol = "http";

    public RestUrlBuilder(String controller_ip, String controller_port) {
        this.controller_ip = controller_ip;
        this.controller_port = controller_port;
    }

    public RestUrlBuilder(String controller_ip, String controller_port, String protocol) {
        this.controller_ip = controller_ip;
        this.controller_port = controller_port;
        this.protocol = protocol;
    }

    public RestUrlBuilder() {

    }

    /**
     *
     * @param dpid
     * @param tableID
     * @param time_stamp_min
     * @param time_stamp_max
     * @return List of all switch DPIDs connected to the controller
     */
    
    public String build_packetIns_stats_url(String dpid,String tableID,long time_stamp_min,long time_stamp_max){
    
    return this.protocol + "://" + this.controller_ip + ":" + this.controller_port+PACKET_INS_STATS+"/"+dpid+"/"+tableID+"/packetInsTable"+"/"+time_stamp_min+"/"+time_stamp_max+"/json";
    }
    
    public String build_flowRemoved_stats_url(String dpid,String tableID,long time_stamp_min,long time_stamp_max){
    
    return this.protocol + "://" + this.controller_ip + ":" + this.controller_port+FLOW_REMOVED_STATS+"/"+dpid+"/"+tableID+"/flowRemovedTable"+"/"+time_stamp_min+"/"+time_stamp_max+"/json";
    }
    
    public String build_counter_stats_url(String counterName,String dpid,String tableID,long time_stamp_min,long time_stamp_max){
    
    return this.protocol + "://" + this.controller_ip + ":" + this.controller_port+COUNTER_STATS+"/"+dpid+"/"+tableID+"/"+counterName+"/"+time_stamp_min+"/"+time_stamp_max+"/json";
    }
    
    
    
    public String build_switches_list_url() {

        return this.protocol + "://" + this.controller_ip + ":" + this.controller_port + SWITCHES_LIST_PATH;
    }

    /**
     *
     * @return Current controller memory usage
     */
    public String build_controller_memory_usage_url() {
        return this.protocol + "://" + this.controller_ip + ":" + this.controller_port + CONTROLLER_MEMORY_USAGE;

    }

    /**
     *
     * @return Status/Health of REST API
     */
    public String build_rest_api_health_status_url() {
        return this.protocol + "://" + this.controller_ip + ":" + this.controller_port + REST_API_HEALTH_STATUS;

    }

    /**
     *
     * @return Controller uptime
     */
    public String build_controller_uptime_url() {
        return this.protocol + "://" + this.controller_ip + ":" + this.controller_port + CONTROLLER_UP_TIME;

    }

    /**
     *
     * @param statType aggregate, desc, flow, group, group-desc, group-features,
     * meter, meter-config, meter-features, port, port-desc, queue, table,
     * features
     * @return Retrieve aggregate stats across all switches. Some stats not
     * supported by all all OpenFlow versions. Meters untested.
     */
    public String build_opf_stats_all_url(String statType) {
        return this.protocol + "://" + this.controller_ip + ":" + this.controller_port + OPF_STATS_ALL + "/" + statType + "/json";
    }

    /**
     *
     * @param switchID Valid Switch DPID (XX:XX:XX:XX:XX:XX:XX:XX)
     * @param statType aggregate, desc, flow, group, group-desc, group-features,
     * meter, meter-config, meter-features, port, port-desc, queue,
     * table,features
     * @return Retrieve per switch stats. Some stats not supported by all all
     * OpenFlow versions. Meters untested.
     */
    public String build_opf_stats_switch_url(String switchID, String statType) {
        return this.protocol + "://" + this.controller_ip + ":" + this.controller_port + OPF_STATS_SWITCH + "/" + switchID + "/" + statType + "/json";
    }

    /**
     *
     * @return Enable statistics collection
     */
    public String build_enabling_stats_collection_url() {
        return this.protocol + "://" + this.controller_ip + ":" + this.controller_port + ENABLING_STATISTICS_COLLECTION;
    }

    /**
     *
     *
     * @return Disable statistics collection
     */
    public String build_disabling_stats_collection_url() {
        return this.protocol + "://" + this.controller_ip + ":" + this.controller_port + DISABLING_STATISTICS_COLLECTION;
    }

    /**
     *
     * @param switchID Valid Switch DPID as colon-delimited hex string or
     * integer. Use "all" for all switches.
     * @param portID Valid switch port number
     * @return Fetch RX/TX bandwidth consumption
     */
    public String build_switch_bandwith_url(String switchID, String portID) {
        return this.protocol + "://" + this.controller_ip + ":" + this.controller_port + SWTICH_BANDWITH + "/" + switchID + "/" + portID + "/json";
    }

    /**
     *
     * @return Add/Delete static flow \n HTTP POST data (add flow), HTTP DELETE
     * (for deletion)
     */
    public String build_add_del_StaticFlow_url() {
        return this.protocol + "://" + this.controller_ip + ":" + this.controller_port + ADD_DEL_FLOW;
    }

    /**
     *
     * @param switchID Valid Switch DPID as colon-delimited hex string or
     * integer. Use "all" for all switches.
     * @return List static flows for a switch or all switches
     */
    public String build_list_switchStaticFlows_url(String switchID) {
        return this.protocol + "://" + this.controller_ip + ":" + this.controller_port + LIST_SWITCH_FLOWS + "/" + switchID + "/json";
    }

    /**
     *
     * @param switchID Valid Switch DPID as colon-delimited hex string or
     * integer. Use "all" for all switches.
     * @return Clear static flows for a switch or all switches
     */
    public String build_clear_switchStaticFlows_url(String switchID) {
        return this.protocol + "://" + this.controller_ip + ":" + this.controller_port + CLEAR_SWITCH_FLOWS + "/" + switchID + "/json";
    }

    /**
     *
     * @return Add a rule '{"<key>":"<value>", "<key>":"<value>", ...}' \n
     * <key>:value> pairs can be any of:
     *
     * "nw-proto" : "any valid network protocol number"
     *
     * "src-ip" : "ip/mask"
     *
     * "dst-ip" : "ip/mask"
     *
     * "tp-dst" : "any valid transport port number"
     *
     * "action" : "ALLOW | DENY"
     */
    public String build_add_rule_url() {
        return this.protocol + "://" + this.controller_ip + ":" + this.controller_port + ADD_RULE;
    }

    /**
     *
     * @return Delete a rule. {"ruleid":"<rule>"} \n rule: The ID of the rule as
     * returned when it was added
     */
    public String build_del_rule_url() {
        return this.protocol + "://" + this.controller_ip + ":" + this.controller_port + DEL_RULE;
    }

    /**
     *
     * @return List all rules
     */
    public String build_list_all_rules_url() {
        return this.protocol + "://" + this.controller_ip + ":" + this.controller_port + LIST_RULES;
    }

    /**
     *
     * @return Delete all rules
     */
    public String build_clear_rules_url() {
        return this.protocol + "://" + this.controller_ip + ":" + this.controller_port + CLEAR_RULES;
    }

}
