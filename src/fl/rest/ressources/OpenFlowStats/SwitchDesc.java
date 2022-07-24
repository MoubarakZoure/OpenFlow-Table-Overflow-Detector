/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats;

import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class SwitchDesc {
    
    private Desc desc;

    public SwitchDesc(Desc desc) {
        this.desc = desc;
    }

    public Desc getDesc() {
        return desc;
    }

    public void setDesc(Desc desc) {
        this.desc = desc;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.desc);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SwitchDesc other = (SwitchDesc) obj;
        if (!Objects.equals(this.desc, other.desc)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SwitchDesc{" + "desc=" + desc + '}';
    }
    
    
    
}
