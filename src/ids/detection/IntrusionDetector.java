/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ids.detection;

import ids.statistics.storage.StatsDBInterfacePooled;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Moubarak
 */
public class IntrusionDetector {

    private ScheduledExecutorService scheduler;
    StatsDBInterfacePooled dbInferface;

    int workersNumber = 5;

    private Map<String, SwitchMonitorAgent> switchesMonitors = new HashMap<>();
    private int STARTING_DELAY = 10000;

    public IntrusionDetector() {

    }

    public IntrusionDetector(StatsDBInterfacePooled dbInferface) {
        this.dbInferface = dbInferface;
    }

    public IntrusionDetector(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    public void createAgentsPool() {

        scheduler = Executors.newScheduledThreadPool(workersNumber);

    }

    public void addASwitchMonitor(SwitchMonitorAgent agent) {
        switchesMonitors.put(agent.getSwitchName(), agent);

    }

    public void startASwitchMonitoring(String switchName) {
        SwitchMonitorAgent monitorAgent = switchesMonitors.get(switchName);
        if (monitorAgent != null) {
            this.scheduler.scheduleAtFixedRate(monitorAgent.getMonitorAgent(), STARTING_DELAY, monitorAgent.getMonitoringFrequence(), TimeUnit.MILLISECONDS);

        } else {
            System.out.println("Ce Switch n'est pas surveillé");

        }

    }

    public void startAllSwitchMonitoring() {

        for (String aSwitchName : switchesMonitors.keySet()) {

            startASwitchMonitoring(aSwitchName);
        }

    }

    public void stopASwitchMonitors() {

        this.scheduler.shutdownNow();

    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public void setScheduler(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    public StatsDBInterfacePooled getDbInferface() {
        return dbInferface;
    }

    public void setDbInferface(StatsDBInterfacePooled dbInferface) {
        this.dbInferface = dbInferface;
    }

    public int getWorkersNumber() {
        return workersNumber;
    }

    public void setWorkersNumber(int workersNumber) {
        this.workersNumber = workersNumber;
    }

    public Map<String, SwitchMonitorAgent> getSwitchesMonitors() {
        return switchesMonitors;
    }

    public void setSwitchesMonitors(Map<String, SwitchMonitorAgent> switchesMonitors) {
        this.switchesMonitors = switchesMonitors;
    }

    public int getSTARTING_DELAY() {
        return STARTING_DELAY;
    }

    public void setSTARTING_DELAY(int STARTING_DELAY) {
        this.STARTING_DELAY = STARTING_DELAY;
    }
    
    

}
