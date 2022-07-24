package machine.learning.svm.prediction;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Moubarak
 */
public class SVMPredictionResult {

    private double nr_class; // class à laquelle appartient 
    private double[] prob_estimates = null; // prob_estimates[i] est la probabilité d'apparetenace à la classe i
    // prob_estimates[nr_class] est la plus grande probabilité d'appartenance
    private int[] labels = null;

    public SVMPredictionResult(double nr_class, double[] prob_estimates, int[] labelsVal) {
        this.nr_class = nr_class;
        this.prob_estimates = prob_estimates;
        this.labels = labelsVal;
    }

    public SVMPredictionResult(double nr_class) {
        this.nr_class = nr_class;
        this.prob_estimates = null;
    }

    public double getNr_class() {
        return nr_class;
    }

    public void setNr_class(double nr_class) {
        this.nr_class = nr_class;
    }

    public double[] getProb_estimates() {
        return prob_estimates;
    }

    public void setProb_estimates(double[] prob_estimates) {
        this.prob_estimates = prob_estimates;
    }

    public int[] getLabels() {
        return labels;
    }

    public void setLabels(int[] labels) {
        this.labels = labels;
    }

    @Override
    public String toString() {
        return "SVMPredictionResult{" + "nr_class=" + nr_class + ", prob_estimates={" + prob_estimates[0] + " , " + prob_estimates[1] + "}, labels=" + labels + '}';
    }

}
