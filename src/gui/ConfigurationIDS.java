/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import ids.statistics.storage.DBConnectionProperties;

/**
 *
 * @author Moubarak
 */
public class ConfigurationIDS {

    String switchesList = "s1";
    private int collectionFrequence = 4000;

    private long cleaningFrequence = 500000;
    private long cleaningSize = 500000;

    private String controller_ip = "192.168.72.131";
    private String controller_port = "8080";
    public String protocol = "http";

    DBConnectionProperties dBConnectionProperties;

    private int workersNumber = 60;
    String svmModelPath = "src/machine/learning/svm/training/svm_model";
    private boolean save_alert = true;
    private boolean send_mail = true;
    private String notification_mal;

    public ConfigurationIDS() {
        this.dBConnectionProperties = new DBConnectionProperties();
    }

    public String getSwitchesList() {
        return switchesList;
    }

    public void setSwitchesList(String switchesList) {
        this.switchesList = switchesList;
    }

    public int getCollectionFrequence() {
        return collectionFrequence;
    }

    public void setCollectionFrequence(int collectionFrequence) {
        this.collectionFrequence = collectionFrequence;
    }

    public long getCleaningFrequence() {
        return cleaningFrequence;
    }

    public void setCleaningFrequence(long cleaningFrequence) {
        this.cleaningFrequence = cleaningFrequence;
    }

    public long getCleaningSize() {
        return cleaningSize;
    }

    public void setCleaningSize(long cleaningSize) {
        this.cleaningSize = cleaningSize;
    }

    public String getController_ip() {
        return controller_ip;
    }

    public void setController_ip(String controller_ip) {
        this.controller_ip = controller_ip;
    }

    public String getController_port() {
        return controller_port;
    }

    public void setController_port(String controller_port) {
        this.controller_port = controller_port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public DBConnectionProperties getdBConnectionProperties() {
        return dBConnectionProperties;
    }

    public void setdBConnectionProperties(DBConnectionProperties dBConnectionProperties) {
        this.dBConnectionProperties = dBConnectionProperties;
    }

    public int getWorkersNumber() {
        return workersNumber;
    }

    public void setWorkersNumber(int workersNumber) {
        this.workersNumber = workersNumber;
    }

    public String getSvmModelPath() {
        return svmModelPath;
    }

    public void setSvmModelPath(String svmModelPath) {
        this.svmModelPath = svmModelPath;
    }

    public boolean isSave_alert() {
        return save_alert;
    }

    public void setSave_alert(boolean save_alert) {
        this.save_alert = save_alert;
    }

    public boolean isSend_mail() {
        return send_mail;
    }

    public void setSend_mail(boolean send_mail) {
        this.send_mail = send_mail;
    }

    public String getNotification_mal() {
        return notification_mal;
    }

    public void setNotification_mal(String notification_mal) {
        this.notification_mal = notification_mal;
    }

}
