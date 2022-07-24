/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import fl.rest.ressources.OpenFlowStats.Counters.CounterTable;
import fl.rest.ressources.OpenFlowStats.OFStatsTypeStrings;
import fl.rest.ressources.OpenFlowStats.aggregate.FlowsAggregate;
import fl.rest.ressources.OpenFlowStats.features.SwitchFeatures;
import fl.rest.ressources.OpenFlowStats.flows.SwitchFlowsStats;
import fl.rest.ressources.OpenFlowStats.flowsRemoved.FlowRemovedTable;
import fl.rest.ressources.OpenFlowStats.packetIns.PacketInsTable;
import fl.rest.ressources.OpenFlowStats.port.SwitchPortsActivities;
import fl.rest.ressources.OpenFlowStats.portdesc.SwitchPortsDesc;
import fl.rest.ressources.OpenFlowStats.tablestats.SwitchTablesStats;
import fl.rest.ressources.controller.ControllerMemoryUsage;
import fl.rest.ressources.controller.ControllerUptime;
import fl.rest.ressources.controller.RestAPIStatus;
import fl.rest.ressources.controller.Switch;
import fl.rest.ressources.statcollector.PortBandwidth;
import fl.rest.ressources.statcollector.StatCollectionStatus;
import fl.rest.ressources.staticflowpusher.FlowName;
import fl.rest.ressources.staticflowpusher.OFFlowMod;
import fl.rest.ressources.staticflowpusher.POSTMsgStatus;
import fl.rest.ressources.staticflowpusher.SwitchFlowStructure;
import fl.rest.ressources.urls.RestUrlBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Moubarak
 */
public class JsonBuilder1 {

    private static final GsonBuilder builder = new GsonBuilder();
    private static Gson gson = builder.create();
    private static final JsonParser parser = new JsonParser();
    private RestUrlBuilder url_builder;

    public JsonBuilder1(RestUrlBuilder url_builder) {

        this.url_builder = url_builder;

        gson = builder.create();

    }

    public RestUrlBuilder getUrl_builder() {
        return url_builder;
    }

    public void setUrl_builder(RestUrlBuilder url_builder) {
        this.url_builder = url_builder;
    }

    public static String toJson(Object o) {

        String result = gson.toJson(o);
        if (o instanceof OFFlowMod) {
            result = result.replaceAll("switchID", "switch");

        }

        return result;

    }

    public String getJsonAt(String url) {
        try {
            return IOUtils.toString(new URL(url));
        } catch (IOException ex) {
            Logger.getLogger(JsonBuilder1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public void sendHttpPost(String url, String header) {

    }

    public static String inputStreamtoString(InputStream inputStream) {

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String res = "";

        try {
            while ((line = br.readLine()) != null) {
                res += line;

            }
        } catch (IOException ex) {
            Logger.getLogger(JsonBuilder1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                br.close();

            } catch (IOException ex) {
                Logger.getLogger(JsonBuilder1.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return res;

    }

    public static String postJsonAt(String url, String data) {

        String result = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);

        //String json = toJson(jsonObject);
        try {
            StringEntity params = new StringEntity(data);
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

            result = EntityUtils.toString(response.getEntity());

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(JsonBuilder1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JsonBuilder1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                httpClient.close();
            } catch (IOException ex) {
                Logger.getLogger(JsonBuilder1.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return result;

    }

    public static String postJsonAt(String url, Object jsonObject) {

        String json = toJson(jsonObject);

        return postJsonAt(url, json);

    }

    public static String deleteJsonAt(String url, String data) {

        String result = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpDeletev2 request = new HttpDeletev2(url);

        //String json = toJson(jsonObject);
        try {
            StringEntity params = new StringEntity(data);
            request.setEntity(params);
            request.addHeader("content-type", "application/json");

            HttpResponse response = httpClient.execute(request);

            result = EntityUtils.toString(response.getEntity());

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(JsonBuilder1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JsonBuilder1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                httpClient.close();
            } catch (IOException ex) {
                Logger.getLogger(JsonBuilder1.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return result;

    }

    public static String deleteJsonAt(String url, Object jsonObject) {
        String json = toJson(jsonObject);

        return deleteJsonAt(url, json);

    }

    public ArrayList<PacketInsTable> getPacketIns(String dpid, String tableID, long time_stamp_min, long time_stamp_max) {
        String url = url_builder.build_packetIns_stats_url(dpid, tableID, time_stamp_min, time_stamp_max);
        String json = getJsonAt(url); System.out.println("packets ins "+json);
        Type type = new TypeToken<ArrayList<PacketInsTable>>() {
        }.getType();

        return gson.fromJson(json, type);

    }

    public ArrayList<FlowRemovedTable> getFlowRemoved(String dpid, String tableID, long time_stamp_min, long time_stamp_max) {
        String url = url_builder.build_flowRemoved_stats_url(dpid, tableID, time_stamp_min, time_stamp_max);
        String json = getJsonAt(url);
        Type type = new TypeToken<ArrayList<FlowRemovedTable>>() {
        }.getType();

        return gson.fromJson(json, type);

    }

    public ArrayList<CounterTable> getCounter(String counterName, String dpid, String tableID, long time_stamp_min, long time_stamp_max) {
        String url = url_builder.build_counter_stats_url(counterName, dpid, tableID, time_stamp_min, time_stamp_max);
        String json = getJsonAt(url);
        Type type = new TypeToken<ArrayList<CounterTable>>() {
        }.getType();

        return gson.fromJson(json, type);

    }

    public ArrayList<Switch> getConnectedSwitches() {

        String url = url_builder.build_switches_list_url();
        String json = getJsonAt(url);
        JsonArray jsonArray = fromJson(json, JsonArray.class);
        return convertSimpleJsonArray(jsonArray, Switch.class);

    }

    public ControllerMemoryUsage getControllerMemoryUsage() {
        String url = url_builder.build_controller_memory_usage_url();
        String json = getJsonAt(url);

        return fromJson(json, ControllerMemoryUsage.class);

    }

    public RestAPIStatus getRestAPIStatus() {

        String url = url_builder.build_rest_api_health_status_url();
        String json = getJsonAt(url);
        return fromJson(json, RestAPIStatus.class);

    }

    public ControllerUptime getControllerUptime() {

        String url = url_builder.build_controller_uptime_url();
        String json = getJsonAt(url);
        return fromJson(json, ControllerUptime.class);

    }

    public SwitchFlowsStats getSwitchFlowsStats(String switchDPID) {
        String url = url_builder.build_opf_stats_switch_url(switchDPID, OFStatsTypeStrings.FLOW);
        String json = getJsonAt(url);
        return fromJson(json, SwitchFlowsStats.class);

    }

    public Map<String, SwitchFlowsStats> getAllSwitchFlowsStats() {

        String url = url_builder.build_opf_stats_all_url(OFStatsTypeStrings.FLOW);
        String json = getJsonAt(url);
        System.out.println(json);

        Type type = new TypeToken<Map<String, SwitchFlowsStats>>() {
        }.getType();
        Map<String, SwitchFlowsStats> resultat = gson.fromJson(json, type);

        return resultat;

    }

    public Map<String, SwitchFeatures> getAllSwitchFeatures() {

        String url = url_builder.build_opf_stats_all_url(OFStatsTypeStrings.FEATURES);
        String json = getJsonAt(url);

        Type type = new TypeToken<Map<String, SwitchFeatures>>() {
        }.getType();
        Map<String, SwitchFeatures> resultat = gson.fromJson(json, type);

        return resultat;

    }

    public SwitchFeatures getSwitchFeatures(String switchDPID) {
        String url = url_builder.build_opf_stats_switch_url(switchDPID, OFStatsTypeStrings.FEATURES);
        String json = getJsonAt(url);
        return fromJson(json, SwitchFeatures.class);

    }

    public FlowsAggregate getSwitchFlowsAggregate(String switchDPID) {

        String url = url_builder.build_opf_stats_switch_url(switchDPID, OFStatsTypeStrings.AGGREGATE);
        String json = getJsonAt(url);
        return fromJson(json, FlowsAggregate.class);

    }

    public SwitchTablesStats getSwitchTablesStats(String switchDPID) {
        String url = url_builder.build_opf_stats_switch_url(switchDPID, OFStatsTypeStrings.TABLE);
           
        String json = getJsonAt(url);
         
        return fromJson(json, SwitchTablesStats.class);

    }

    public void getAllSwitchTablesStats() {

        String url = url_builder.build_opf_stats_all_url(OFStatsTypeStrings.TABLE);
        String json = getJsonAt(url);
        System.out.println(json);

     

    }

    public void getAllSwitchFlowsAggregate() {

        String url = url_builder.build_opf_stats_all_url(OFStatsTypeStrings.AGGREGATE);
        String json = getJsonAt(url);
        System.out.println(json);

      

    }

    public static <T extends Object> ArrayList<T> convertSimpleJsonArray(JsonArray jsonArray, Class<T> Type) {

        ArrayList<T> result = new ArrayList<>();
        for (JsonElement e : jsonArray) {

            T o = gson.fromJson(e, Type);
            result.add(o);
        }

        return result;

    }

    public SwitchPortsActivities getSwitchPortsActivities(String switchDPID) {

        String url = url_builder.build_opf_stats_switch_url(switchDPID, OFStatsTypeStrings.PORT);
        String json = getJsonAt(url);
        return fromJson(json, SwitchPortsActivities.class);
    }

    public Map<String, SwitchPortsActivities> getAllSwitchPortsActivities() {

        String url = url_builder.build_opf_stats_all_url(OFStatsTypeStrings.PORT);
        String json = getJsonAt(url);

        Type type = new TypeToken<Map<String, SwitchPortsActivities>>() {
        }.getType();
        Map<String, SwitchPortsActivities> resultat = gson.fromJson(json, type);

        return resultat;

    }

    public SwitchPortsDesc getSwitchPortsDesc(String switchDPID) {

        String url = url_builder.build_opf_stats_switch_url(switchDPID, OFStatsTypeStrings.PORT_DESC);
        String json = getJsonAt(url);
        return fromJson(json, SwitchPortsDesc.class);
    }

    public Map<String, SwitchPortsDesc> getAllSwitchPortsDesc() {

        String url = url_builder.build_opf_stats_all_url(OFStatsTypeStrings.PORT_DESC);
        String json = getJsonAt(url);

        Type type = new TypeToken<Map<String, SwitchPortsDesc>>() {
        }.getType();
        Map<String, SwitchPortsDesc> resultat = gson.fromJson(json, type);

        return resultat;

    }

    //pour toutes les ports d'un switch, spécifier un switchDPID et un portID quelquequonque; pour tous les switchs, spécifier "all" comme switchID et un portID ou "all"
    public ArrayList<PortBandwidth> getSwitchPortsBandwidth(String switchID, String portID) {
        String url = url_builder.build_switch_bandwith_url(switchID, portID);
        String json = getJsonAt(url);
        json = json.replaceAll("bits-per-second-rx", "rx");
        json = json.replaceAll("bits-per-second-tx", "tx");
        json = json.replaceAll("updated", "updatedTime");
        Type type = new TypeToken<ArrayList<PortBandwidth>>() {
        }.getType();

        return gson.fromJson(json, type);

    }

    public StatCollectionStatus disableStatsCollectionModule() { // à revoir 
        String url = url_builder.build_disabling_stats_collection_url();
        String s = postJsonAt(url, "");
        return fromJson(s, StatCollectionStatus.class);

    }

    public StatCollectionStatus enableStatsCollectionModule() { // à revoir 
        String url = url_builder.build_enabling_stats_collection_url();
        String s = postJsonAt(url, "");
        return fromJson(s, StatCollectionStatus.class);

    }

    public POSTMsgStatus addStaticFlow(String switchDPID, OFFlowMod flow) {
        String url = url_builder.build_add_del_StaticFlow_url();
        String s = postJsonAt(url, flow);
        return fromJson(s, POSTMsgStatus.class);

    }

    public POSTMsgStatus deleteStaticFlow(FlowName flowName) {
        String url = url_builder.build_add_del_StaticFlow_url();
        String s = deleteJsonAt(url, flowName);
        return fromJson(s, POSTMsgStatus.class);

    }

    public POSTMsgStatus clearSwitchStaticFlows(String switchID) {

        String url = url_builder.build_clear_switchStaticFlows_url(switchID);
        String s = getJsonAt(url);

        return fromJson(s, POSTMsgStatus.class);

    }

    // si switchID=all, alors list pour touts les switches
    public Map<String, ArrayList<Map<String, SwitchFlowStructure>>> getSwitchStaticFlows(String switchID) {
        String url = url_builder.build_list_switchStaticFlows_url(switchID);
        String json = getJsonAt(url);
        Type type = new TypeToken<Map<String, ArrayList<Map<String, SwitchFlowStructure>>>>() {
        }.getType();

        return gson.fromJson(json, type);

    }

    public static <T extends Object> T fromJson(String json, Class<T> type) {

        if (type.equals(StatCollectionStatus.class)) {
            json = json.replaceAll("statistics-collection", "statistics_collection");
        }

        JsonElement element = parser.parse(json);

        if (element.isJsonObject()) {
            return gson.fromJson(element.getAsJsonObject(), type);
        }
        if (element.isJsonArray()) {
            return gson.fromJson(element.getAsJsonArray(), type);
        }

        return gson.fromJson(element, type);
    }

    public static void setGson(Gson gson) {
        JsonBuilder1.gson = gson;
    }

    public static GsonBuilder getBuilder() {
        return builder;
    }

    public static Gson getGson() {
        return gson;
    }

    public static JsonParser getParser() {
        return parser;
    }

    public static void main(String[] args) {

        RestUrlBuilder restUrlBuilder = new RestUrlBuilder("192.168.72.128", "8080", "http");
        JsonBuilder1 jsonBuilder = new JsonBuilder1(restUrlBuilder);

        System.out.println(jsonBuilder.addStaticFlow("00:00:00:00:00:00:00:02", new OFFlowMod()));


        /* Map<String, ArrayList<Map<String, SwitchFlowStructure>>> s1 = jsonBuilder.getSwitchStaticFlows("all");
        System.out.println(s1);

        for (Map.Entry<String, ArrayList<Map<String, SwitchFlowStructure>>> oneSwitchFlows : s1.entrySet()) {
            System.out.println(oneSwitchFlows.getKey());

            ArrayList<Map<String, SwitchFlowStructure>> flows = oneSwitchFlows.getValue();

            for (Map<String, SwitchFlowStructure> f : flows) {

                for (Map.Entry<String, SwitchFlowStructure> oneFlow : f.entrySet()) {
                    System.out.println(oneFlow.getKey() + "  --->> " + oneFlow.getValue());

                }

            }
        }*/
    }

}
