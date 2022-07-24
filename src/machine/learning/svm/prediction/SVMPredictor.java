package machine.learning.svm.prediction;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_print_interface;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Moubarak
 */
public class SVMPredictor {

    private svm_model model;
    //  private String input_file_name;		// set by parse_command_line
    private String model_file_name = "src/machine/learning/svm/training/svm_model";
    private int predict_probability = 1;
    private boolean isVerbose = true;

    
    
    
    
    
    

    public static svm_node[] vectorToSVMNodes(double[] aVector, int start) {
        // si start = 1 on considère que la première colonne de vector contient les classes
        // si start = 0 on considère que les données ont été fournies sans les classes

        svm_node[] result = new svm_node[(aVector.length - start)];
        for (int i = 1; i < (result.length + 1); i++) {
            result[i - 1] = new svm_node();
            result[i - 1].index = i;
            result[i - 1].value = aVector[i - 1 + start];
        }

        return result;

    }

    public SVMPredictionResult predict(double[] aVector, int start) {
        svm_node[] x = vectorToSVMNodes(aVector, start);

        int svm_type = svm.svm_get_svm_type(this.model);
        int nr_class = svm.svm_get_nr_class(this.model);
        double[] prob_estimates = null;
        double v; // predited class

        if (predict_probability == 1) {
            if (svm_type == svm_parameter.EPSILON_SVR || svm_type == svm_parameter.NU_SVR) {
                // svm_predict.info("Prob. model for test data: target value = predicted value + z,\nz: Laplace distribution e^(-|z|/sigma)/(2sigma),sigma=" + svm.svm_get_svr_probability(model) + "\n");
            } else {
                //int[] labels=new int[nr_class];
                //svm.svm_get_labels(model,labels);
                prob_estimates = new double[nr_class];

            }
        }

        if (predict_probability == 1 && (svm_type == svm_parameter.C_SVC || svm_type == svm_parameter.NU_SVC)) {
            v = svm.svm_predict_probability(model, x, prob_estimates);

        } else {
            v = svm.svm_predict(model, x);

        }

        int[] labels = new int[nr_class];
        svm.svm_get_labels(model, labels);

        return new SVMPredictionResult(v, prob_estimates, labels);
    }

    private static svm_print_interface svm_print_null = new svm_print_interface() {
        public void print(String s) {
        }
    };

    private static svm_print_interface svm_print_stdout = new svm_print_interface() {
        public void print(String s) {
            System.out.print(s);
        }
    };

    private static svm_print_interface svm_print_string = svm_print_stdout;

    static void info(String s) {
        svm_print_string.print(s);
    }

    public SVMPredictor(String modele_file, int predict_probability, boolean isVerbose) {
        this.model_file_name = modele_file;
        this.predict_probability = predict_probability;
        this.isVerbose = isVerbose;
        this.loadSVModelFromFile(model_file_name);
        if (isVerbose == false) {
            svm_print_string = svm_print_null;
        }
    }

    public SVMPredictor(String modele_file, int predict_probability) {
        this.model_file_name = modele_file;
        this.predict_probability = predict_probability;
        this.loadSVModelFromFile(model_file_name);
        if (isVerbose == false) {
            svm_print_string = svm_print_null;
        }
    }

    public SVMPredictor(svm_model model, int predict_probability, boolean isVerbose) {
        this.model = model;
        this.predict_probability = predict_probability;
        this.isVerbose = isVerbose;
        if (isVerbose == false) {
            svm_print_string = svm_print_null;
        }
    }

    public SVMPredictor(svm_model model, int predict_probability) {
        this.model = model;
        this.predict_probability = predict_probability;
        if (isVerbose == false) {
            svm_print_string = svm_print_null;
        }
    }

    private svm_model loadSVModelFromFile(String model_file) {
        try {
            this.model = svm.svm_load_model(model_file);
            return this.model;
        } catch (IOException ex) {
            Logger.getLogger(SVMPredictor.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public SVMPredictor(svm_model model) {
        this.model = model;
    }

}
