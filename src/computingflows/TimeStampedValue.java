/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computingflows;

/**
 *
 * @author Moubarak
 */
public class TimeStampedValue {
    
  private long time_stamp;
  private long number; // represent the packet number

    public TimeStampedValue(long time_stamp, long number) {
        this.time_stamp = time_stamp;
        this.number = number;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "TimeStampedValue(" + "time_stamp=" + time_stamp + ", number=" + number + ')';
    }
    
    
    
}
