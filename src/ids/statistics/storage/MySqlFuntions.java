/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ids.statistics.storage;

/**
 *
 * @author Moubarak
 */
public class MySqlFuntions {

    public static String MEAN_FUNCTION = "avg(param)";
    public static String COUNT_FUNCTION = "count(param)";
    public static String MIN_FUNCTION = "min(param)";
    public static String MAX_FUNCTION = "max(param)";
    public static String VARIANCE_POPULATION_FUNCTION = "variance(param)";
    public static String VARIANCE_SAMPLE_FUNCTION = "var_samp(param)";
    public static String HARMONIC_MEAN_FUNCTION = "count(param)/sum(1/param)";
    public static String GEOMETRIC_MEAN_FUNCTION = "exp(sum(log(param))/count(param))";
    public static String SUM_FUNCTION = "sum(param)";

}
