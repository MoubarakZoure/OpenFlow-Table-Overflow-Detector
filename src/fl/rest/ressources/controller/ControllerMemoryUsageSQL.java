/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.controller;

/**
 *
 * @author Moubarak
 */
public class ControllerMemoryUsageSQL extends ControllerMemoryUsage{
    
    private long time_stamp;

    public ControllerMemoryUsageSQL(long time_stamp,ControllerMemoryUsage controllerMemoryUsage) {
        super(controllerMemoryUsage.getTotal(),controllerMemoryUsage.getFree());
        this.time_stamp = time_stamp;
    }

    @Override
    public String toString() {
        return "ControllerMemoryUsageSQL{" + "time_stamp=" + time_stamp + '}';
    }
    
    
    

    public ControllerMemoryUsageSQL(long total, long free) {
        super(total, free);
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }
    
    
    
    
}
