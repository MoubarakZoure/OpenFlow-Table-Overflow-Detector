/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.staticflowpusher;

import java.util.Map;

/**
 *
 * @author Moubarak
 */
public class SwitchFlowModMap { // à revoir 

    
    private Map<String, OFFlowMod> theMap;

    public SwitchFlowModMap() {
    }

    public SwitchFlowModMap(Map<String, OFFlowMod> theMap) {
        this.theMap = theMap;
    }

    public Map<String, OFFlowMod> getMap() {
        return theMap;
    }
}
