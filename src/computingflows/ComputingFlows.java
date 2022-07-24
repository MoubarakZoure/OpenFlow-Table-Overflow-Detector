/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computingflows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Moubarak
 */
public class ComputingFlows {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Map<String, ArrayList<String>> map = new HashMap<>();
        ArrayList<String> x = new ArrayList<>();
        ArrayList<String> y = new ArrayList<>();
        y.add("Y Radom value");
        x.add( "FirstVal");
        map.put("akey", x);
        map.put("anotherKey", y);
        printMap(map);
         ArrayList<String> z =map.get("akey");
         z.add("Second Val ***");
         printMap(map);
         
        z.removeAll(z);
        
        printMap(map);

    }

    public static void printMap(Map<String, ArrayList<String>> aMap) {
        for (Map.Entry<String, ArrayList<String>> entry : aMap.entrySet()) {

            System.out.print(entry.getKey() + "(");
            ArrayList<String> array = entry.getValue();
            for (String s : array) {

                System.out.print(s + "|");
            }
            System.out.println(")");

        }
        
        System.out.println("********************************************* fin ************************************");

    }

}
