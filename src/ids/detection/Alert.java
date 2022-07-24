/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ids.detection;

import java.sql.Timestamp;

/**
 *
 * @author Moubarak
 */
public class Alert {

    private int alert_id;
    private String switch_name;
    private java.sql.Timestamp detection_time;
    private Timestamp attack_min_window;
    private Timestamp attack_max_window;
    private String attack_name;
    private double table_mean_occupation;
    private double matching_indication;
    private int table_full_count;
    private double probability;
    private long missFlowCount;
    private double missFlowRate;

    public Alert() {
    }

    public Alert(int alert_id, String switch_name, Timestamp detection_time, Timestamp attack_min_window, Timestamp attack_max_window, String attack_name, double table_mean_occupation, double matching_indication, int table_full_count) {
        this.alert_id = alert_id;
        this.switch_name = switch_name;
        this.detection_time = detection_time;
        this.attack_min_window = attack_min_window;
        this.attack_max_window = attack_max_window;
        this.attack_name = attack_name;
        this.table_mean_occupation = table_mean_occupation;
        this.matching_indication = matching_indication;
        this.table_full_count = table_full_count;
    }

    public Alert(int alert_id, String switch_name, Timestamp detection_time, Timestamp attack_min_window, Timestamp attack_max_window, String attack_name, double table_mean_occupation, double matching_indication, int table_full_count, double probability) {
        this.alert_id = alert_id;
        this.switch_name = switch_name;
        this.detection_time = detection_time;
        this.attack_min_window = attack_min_window;
        this.attack_max_window = attack_max_window;
        this.attack_name = attack_name;
        this.table_mean_occupation = table_mean_occupation;
        this.matching_indication = matching_indication;
        this.table_full_count = table_full_count;
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public int getAlert_id() {
        return alert_id;
    }

    public void setAlert_id(int alert_id) {
        this.alert_id = alert_id;
    }

    public String getSwitch_name() {
        return switch_name;
    }

    public void setSwitch_name(String switch_name) {
        this.switch_name = switch_name;
    }

    public Timestamp getDetection_time() {
        return detection_time;
    }

    public void setDetection_time(Timestamp detection_time) {
        this.detection_time = detection_time;
    }

    public Timestamp getAttack_min_window() {
        return attack_min_window;
    }

    public void setAttack_min_window(Timestamp attack_min_window) {
        this.attack_min_window = attack_min_window;
    }

    public Timestamp getAttack_max_window() {
        return attack_max_window;
    }

    public void setAttack_max_window(Timestamp attack_max_window) {
        this.attack_max_window = attack_max_window;
    }

    public String getAttack_name() {
        return attack_name;
    }

    public void setAttack_name(String attack_name) {
        this.attack_name = attack_name;
    }

    public double getTable_mean_occupation() {
        return table_mean_occupation;
    }

    public void setTable_mean_occupation(double table_mean_occupation) {
        this.table_mean_occupation = table_mean_occupation;
    }

    public double getMatching_indication() {
        return matching_indication;
    }

    public void setMatching_indication(double matching_indication) {
        this.matching_indication = matching_indication;
    }

    public int getTable_full_count() {
        return table_full_count;
    }

    public void setTable_full_count(int table_full_count) {
        this.table_full_count = table_full_count;
    }

    public long getMissFlowCount() {
        return missFlowCount;
    }

    public void setMissFlowCount(long missFlowCount) {
        this.missFlowCount = missFlowCount;
    }

    public double getMissFlowRate() {
        return missFlowRate;
    }

    public void setMissFlowRate(double missFlowRate) {
        this.missFlowRate = missFlowRate;
    }

}
