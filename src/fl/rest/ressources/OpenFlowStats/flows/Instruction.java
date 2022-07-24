/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.flows;

import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class Instruction {
    
    private String actions;

    public Instruction(String actions) {
        this.actions = actions;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.actions);
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
        final Instruction other = (Instruction) obj;
        if (!Objects.equals(this.actions, other.actions)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Instruction{" + "actions=" + actions + '}';
    }
    
    
    
    
    
}
