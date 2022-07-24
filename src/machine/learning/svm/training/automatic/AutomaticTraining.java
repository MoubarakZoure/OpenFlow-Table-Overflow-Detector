/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machine.learning.svm.training.automatic;

import ids.statistics.storage.StatsDBInterfacePooled;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import libsvm.svm_parameter;
import machine.learning.svm.training.SVMTraining;

/**
 *
 * @author Moubarak
 */
public class AutomaticTraining {

    StatsDBInterfacePooled db;
    SVMTraining trainer = new SVMTraining();

    public AutomaticTraining(StatsDBInterfacePooled db) {
        this.db = db;
    }

    public AutomaticTraining() {
        db = new StatsDBInterfacePooled(3);
    }

    public static void main(String[] args) {
        StatsDBInterfacePooled db = new StatsDBInterfacePooled(3);
        AggregatCreator ac = new AggregatCreator(db);
        ac.createAggregats();
    }

    public void train(svm_parameter aParam, ArrayList<String> attributList, int crossValidation, int n_fold) {

        ArrayList<StatAggregat> dataset = retrieveTrainingDataSet();
        ArrayList<Double[]> data = formatData(dataset, attributList);
        try {
            if (crossValidation == 1) {

                trainer.setNr_fold(n_fold);
            }
            trainer.train(aParam, crossValidation, data);
        } catch (IOException ex) {
            Logger.getLogger(AutomaticTraining.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ArrayList<StatAggregat> retrieveTrainingDataSet() {

        return toStatAggregats(db.retrieveTrainingDataSet());

    }

    public ArrayList<Double[]> formatData(ArrayList<StatAggregat> trainingData, ArrayList<String> attributList) {

        int attributNumber = attributList.size();

        ArrayList<Double[]> result = new ArrayList<>();

        for (StatAggregat aggregat : trainingData) {
            Double[] aline = new Double[attributNumber + 1];
            Double traffiClass = null; // 0 : normal 1 : attack 
            if (aggregat.getTrafficType().equals("ATTACK")) {
                traffiClass = new Double(1);

            } else if (aggregat.getTrafficType().equals("NORMAL")) {
                traffiClass = new Double(0);

            }

            aline[0] = traffiClass;

            int i = 0;

            if (attributList.contains("table_full_count")) {
                aline[++i] = new Double(aggregat.getTable_full_cout());

            }

            if (attributList.contains("missFlowCount")) {
                aline[++i] = new Double(aggregat.getMissFlowStats().getMissFlowCount());

            }

            if (attributList.contains("missFlowRate")) {
                aline[++i] = aggregat.getMissFlowStats().getMissFlowRate();

            }

            if (attributList.contains("lookUpRate")) {

                aline[++i] = aggregat.getTableStatAggregat().getLookUpRate();

            }

            if (attributList.contains("matchRate")) {
                aline[++i] = aggregat.getTableStatAggregat().getMatchRate();

            }

            if (attributList.contains("occupation_average")) {
                aline[++i] = aggregat.getTableStatAggregat().getOccupation_average();

            }

            if (attributList.contains("lookUpCount")) {
                aline[++i] = new Double(aggregat.getTableStatAggregat().getLookUpCount());

            }

            if (attributList.contains("matchCount")) {
                aline[++i] = new Double(aggregat.getTableStatAggregat().getMatchCount());

            }

            if (attributList.contains("missFlowRateUnderLookUpRate")) {
                aline[++i] = (double) aggregat.getMissFlowStats().getMissFlowRate() / aggregat.getTableStatAggregat().getLookUpRate();

            }

            if (attributList.contains("missFlowRateUnderMatchRate")) {
                aline[++i] = (double) aggregat.getMissFlowStats().getMissFlowRate() / aggregat.getTableStatAggregat().getMatchRate();

            }
            if (attributList.contains("missFlowRate/Table_Capacity")) {
                aline[++i] = (double) aggregat.getMissFlowStats().getMissFlowRate() / AggregatCreator.tableSize;

            }

            if (attributList.contains("missFlowCount/Table_Capacity")) {
                aline[++i] = (double) aggregat.getMissFlowStats().getMissFlowCount() / AggregatCreator.tableSize;

            }

            result.add(aline);

        }

        return result;
    }

    ArrayList<StatAggregat> toStatAggregats(ResultSet resultSet) {
        ArrayList<StatAggregat> list = new ArrayList<>();

        try {
            while (resultSet.next()) {
                int table_full_cout = resultSet.getInt("table_full_count");

                MissFlowStats missFlowStats = new MissFlowStats(resultSet.getLong("missFlowCount"), resultSet.getDouble("missFlowRate"));

                StatFromTable tableStatAggregat = new StatFromTable();
                tableStatAggregat.setLookUpCount(resultSet.getInt("lookUpCount"));
                tableStatAggregat.setMatchCount(resultSet.getInt("matchCount"));
                tableStatAggregat.setLookUpRate(resultSet.getDouble("lookUpRate"));
                tableStatAggregat.setMatchRate(resultSet.getDouble("matchRate"));
                tableStatAggregat.setOccupation_average(resultSet.getDouble("occupation_average"));

                String trafficType = resultSet.getString("trafficType");

                StatAggregat aggregat = new StatAggregat(table_full_cout, missFlowStats, tableStatAggregat, trafficType);
                list.add(aggregat);

            }
        } catch (SQLException ex) {
            Logger.getLogger(AutomaticTraining.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public StatsDBInterfacePooled getDb() {
        return db;
    }

    public void setDb(StatsDBInterfacePooled db) {
        this.db = db;
    }

    public SVMTraining getTrainer() {
        return trainer;
    }

    public void setTrainer(SVMTraining trainer) {
        this.trainer = trainer;
    }
    
    
    

}
