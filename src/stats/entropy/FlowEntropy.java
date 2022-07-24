/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stats.entropy;

import fl.rest.ressources.OpenFlowStats.flows.SimpleMatch;
import java.util.ArrayList;

/**
 *
 * @author Moubarak
 */
public class FlowEntropy {

    public static double computeFlowEntropy(ArrayList<SimpleMatch> matchesList, boolean isBidirectionalFlows) {

        double e;
        ArrayList<String> list = matchToString(matchesList, isBidirectionalFlows);
        Entropy<String> entropy = new Entropy<>(list);
        e = entropy.entropy();

        return e;

    }

    public static ArrayList<String> matchToString(ArrayList<SimpleMatch> matchesList, boolean isBidirectionalFlows) {

        if (isBidirectionalFlows == false) {
            
            ArrayList<String> list = new ArrayList<>();

            for (SimpleMatch m : matchesList) {

                list.add(m.toString());

            }
            
            return list;
        } else {
            ArrayList<String> list = new ArrayList<>();
            for (SimpleMatch m : matchesList) {
                ArrayList<SimpleMatch> matches = countFlowMatch(m, matchesList, true);
                for (int i = 0; i < matches.size(); i++) {

                    list.add(m.toString());

                }

                matchesList.removeAll(matches);

            }
            return list;

        }
    }

    public static ArrayList<SimpleMatch> countFlowMatch(SimpleMatch flowMatch, ArrayList<SimpleMatch> matchesList, boolean isBidirectionalFlows) {

        ArrayList<SimpleMatch> result = new ArrayList<>();
        for (SimpleMatch m : matchesList) {
            if (SimpleMatch.isEqualsMatch(flowMatch, m, isBidirectionalFlows)) {

                result.add(m);
            }

        }

        return result;

    }

    public static void main(String[] args) {

        SimpleMatch m1 = new SimpleMatch("192.168.10.1", "192.168.10.3", 1000, 2000);
        SimpleMatch m2 = new SimpleMatch("192.168.10.1", "192.168.10.3", 1000, 2000);
        SimpleMatch m3 = new SimpleMatch("192.168.10.3", "192.168.10.3", 1000, 2000);
        SimpleMatch m4 = new SimpleMatch("192.168.10.4", "192.168.10.3", 1000, 2000);
        SimpleMatch m5 = new SimpleMatch("192.168.10.5", "192.168.10.3", 1000, 2000);

        if (SimpleMatch.isEqualsMatch(m1, m2, false)) {
            System.out.println(" Yesssssssssssssss !!!!!!!!!!!!!");
        } else {
            System.out.println("Noooooo");
        }

        ArrayList<SimpleMatch> list = new ArrayList<>();
        list.add(m1); list.add(m2); list.add(m3); list.add(m4); list.add(m5); 

        double result = FlowEntropy.computeFlowEntropy(list, false);

        System.out.println("valeur de l'entropie = " + result);

    }

}
