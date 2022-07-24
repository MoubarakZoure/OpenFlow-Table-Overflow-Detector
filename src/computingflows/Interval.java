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
public class Interval {

    private long inf=0 ;
    private long sup =0;

    public Interval(long inf, long sup) {
        this.inf = inf;
        this.sup = sup;
    }

    public Interval() {
    }

    public long getInf() {
        return inf;
    }

    public void setInf(long inf) {
        this.inf = inf;
    }

    public long getSup() {
        return sup;
    }

    public void setSup(long sup) {
        this.sup = sup;
    }

    @Override
    public String toString() {
        return "Interval[" + "inf=" + inf + ", sup=" + sup + ']';
    }

   
    
    

}
