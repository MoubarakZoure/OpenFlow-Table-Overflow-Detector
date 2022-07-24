/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ids.statistics;

import fl.rest.ressources.json.JsonBuilder;
import fl.rest.ressources.json.JsonBuilder1;
import fl.rest.ressources.urls.RestUrlBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Moubarak
 */
public class NewClass {

    public static void main(String[] args) {
        String controller_ip = "192.168.72.131";
        String controller_port = "8080";
        String protocol = "http";

        RestUrlBuilder restUrlBuilder = new RestUrlBuilder(controller_ip, controller_port, protocol);
        JsonBuilder1 jsonBuilder = new JsonBuilder1(restUrlBuilder);
        
        int i=0;
        while(true){
            
            System.out.println("----->"+(++i));
            jsonBuilder.getAllSwitchTablesStats();
            jsonBuilder.getAllSwitchFlowsAggregate();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
        }

    }

}
