package machine.learning.svm.training;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import libsvm.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Moubarak
 */
public class SVMTraining {

    private svm_parameter param;		// set by parse_command_line
    private svm_problem prob;		// set by read_problem
    private svm_model model;
    //  private String input_file_name;		// set by parse_command_line
    private String model_file_name = "src/machine/learning/svm/training/svm_model";		// set by parse_command_line
    private String error_msg;
    private int cross_validation;
    private int nr_fold = -1;

    private static svm_print_interface svm_print_null = new svm_print_interface() {
        public void print(String s) {
            System.out.println(s);
        }
    };

    private static void exit_with_help() {
        System.out.print(
                "Usage: svm_train [options] training_set_file [model_file]\n"
                + "options:\n"
                + "-s svm_type : set type of SVM (default 0)\n"
                + "	0 -- C-SVC\n"
                + "	1 -- nu-SVC\n"
                + "	2 -- one-class SVM\n"
                + "	3 -- epsilon-SVR\n"
                + "	4 -- nu-SVR\n"
                + "-t kernel_type : set type of kernel function (default 2)\n"
                + "	0 -- linear: u'*v\n"
                + "	1 -- polynomial: (gamma*u'*v + coef0)^degree\n"
                + "	2 -- radial basis function: exp(-gamma*|u-v|^2)\n"
                + "	3 -- sigmoid: tanh(gamma*u'*v + coef0)\n"
                + "	4 -- precomputed kernel (kernel values in training_set_file)\n"
                + "-d degree : set degree in kernel function (default 3)\n"
                + "-g gamma : set gamma in kernel function (default 1/num_features)\n"
                + "-r coef0 : set coef0 in kernel function (default 0)\n"
                + "-c cost : set the parameter C of C-SVC, epsilon-SVR, and nu-SVR (default 1)\n"
                + "-n nu : set the parameter nu of nu-SVC, one-class SVM, and nu-SVR (default 0.5)\n"
                + "-p epsilon : set the epsilon in loss function of epsilon-SVR (default 0.1)\n"
                + "-m cachesize : set cache memory size in MB (default 100)\n"
                + "-e epsilon : set tolerance of termination criterion (default 0.001)\n"
                + "-h shrinking : whether to use the shrinking heuristics, 0 or 1 (default 1)\n"
                + "-b probability_estimates : whether to train a SVC or SVR model for probability estimates, 0 or 1 (default 0)\n"
                + "-wi weight : set the parameter C of class i to weight*C, for C-SVC (default 1)\n"
                + "-v n : n-fold cross validation mode\n"
                + "-q : quiet mode (no outputs)\n"
        );
        System.exit(1);
    }

    private void do_cross_validation() {
        int i;
        int total_correct = 0;
        double total_error = 0;
        double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
        double[] target = new double[prob.l];

        svm.svm_cross_validation(prob, param, nr_fold, target);
        if (param.svm_type == svm_parameter.EPSILON_SVR
                || param.svm_type == svm_parameter.NU_SVR) {
            for (i = 0; i < prob.l; i++) {
                double y = prob.y[i];
                double v = target[i];
                total_error += (v - y) * (v - y);
                sumv += v;
                sumy += y;
                sumvv += v * v;
                sumyy += y * y;
                sumvy += v * y;
            }
            System.out.print("Cross Validation Mean squared error = " + total_error / prob.l + "\n");
            System.out.print("Cross Validation Squared correlation coefficient = "
                    + ((prob.l * sumvy - sumv * sumy) * (prob.l * sumvy - sumv * sumy))
                    / ((prob.l * sumvv - sumv * sumv) * (prob.l * sumyy - sumy * sumy)) + "\n"
            );
        } else {
            for (i = 0; i < prob.l; i++) {
                if (target[i] == prob.y[i]) {
                    ++total_correct;
                }
            }
            System.out.print("Cross Validation Accuracy = " + 100.0 * total_correct / prob.l + "%\n");
        }
    }

    public static ArrayList<Double[]> getATestSample(int size) {
        ArrayList<Double[]> data = new ArrayList<>();

        for (int i = 1; i < size + 1; i++) {

            Double[] aline = new Double[4];
            aline[0] = new Double(1);
            aline[1] = new Double(i);
            aline[2] = new Double(2 * i);
            aline[3] = new Double(2 * i);
            data.add(aline);

            Double[] aline2 = new Double[4];
            aline2[0] = new Double(0);
            aline2[1] = new Double(-i);
            aline2[2] = new Double(-2 * i);
            aline2[3] = new Double(i);
            data.add(aline2);

        }

        return data;

    }

    private void run(String argv[]) throws IOException {

        parse_command_line(argv);

        getSVMProblemFromData(getATestSample(300));// read_problem();

        error_msg = svm.svm_check_parameter(prob, param);

        if (error_msg != null) {
            System.err.print("ERROR: " + error_msg + "\n");
            System.exit(1);
        }

        if (cross_validation != 0) {
            do_cross_validation();
        } else {
            model = svm.svm_train(prob, param);
            svm.svm_save_model(model_file_name, model);
        }
    }

    public String[] constructInputParamaters(String svm_param_file) {

        svm_parameter aParam = parseParameter(svm_param_file);
        String[] result;
        ArrayList<String> params = new ArrayList<>();
        int i = -1;

        params.add(++i, "-s");
        params.add(++i, aParam.svm_type + "");

        params.add(++i, "-t");
        params.add(++i, aParam.kernel_type + "");

        params.add(++i, "-d");
        params.add(++i, aParam.degree + "");

        params.add(++i, "-g");
        params.add(++i, aParam.gamma + "");

        params.add(++i, "-r");
        params.add(++i, aParam.coef0 + "");

        params.add(++i, "-c");
        params.add(++i, aParam.C + "");

        params.add(++i, "-n");
        params.add(++i, aParam.nu + "");

        params.add(++i, "-p");
        params.add(++i, aParam.p + "");

        params.add(++i, "-e");
        params.add(++i, aParam.eps + "");

        params.add(++i, "-h");
        params.add(++i, aParam.shrinking + "");

        params.add(++i, "-m");
        params.add(++i, aParam.cache_size + "");

        if (nr_fold > 0) {
            params.add(++i, "-v");
            params.add(++i, this.nr_fold + "");
        }
        params.add(++i, "-b");
        params.add(++i, aParam.probability + "");

        double[] w = aParam.weight;
        int[] wl = aParam.weight_label;
        if ((w != null) && (w.length > 0)) {
            for (int k = 0; k < w.length; k++) {
                params.add(++i, "-w" + wl[k]);
                params.add(++i, w[k] + "");

            }
        }

        params.add(++i, "-q");
        params.add(++i, "**");

        result = new String[params.size()];

        for (int k = 0; k < params.size(); k++) {

            result[k] = params.get(k);

        }

        return result;
    }

    public String[] constructInputParamaters(svm_parameter aParam) {

        String[] result;
        ArrayList<String> params = new ArrayList<>();
        int i = -1;

        params.add(++i, "-s");
        params.add(++i, aParam.svm_type + "");

        params.add(++i, "-t");
        params.add(++i, aParam.kernel_type + "");

        params.add(++i, "-d");
        params.add(++i, aParam.degree + "");

        params.add(++i, "-g");
        params.add(++i, aParam.gamma + "");

        params.add(++i, "-r");
        params.add(++i, aParam.coef0 + "");

        params.add(++i, "-c");
        params.add(++i, aParam.C + "");

        params.add(++i, "-n");
        params.add(++i, aParam.nu + "");

        params.add(++i, "-p");
        params.add(++i, aParam.p + "");

        params.add(++i, "-e");
        params.add(++i, aParam.eps + "");

        params.add(++i, "-h");
        params.add(++i, aParam.shrinking + "");

        params.add(++i, "-m");
        params.add(++i, aParam.cache_size + "");

        if (nr_fold > 0) {
            params.add(++i, "-v");
            params.add(++i, this.nr_fold + "");
        }
        params.add(++i, "-b");
        params.add(++i, aParam.probability + "");

        double[] w = aParam.weight;
        int[] wl = aParam.weight_label;
        if ((w != null) && (w.length > 0)) {
            for (int k = 0; k < w.length; k++) {
                params.add(++i, "-w" + wl[k]);
                params.add(++i, w[k] + "");

            }
        }

        params.add(++i, "-q");
        params.add(++i, "**");

        result = new String[params.size()];

        for (int k = 0; k < params.size(); k++) {

            result[k] = params.get(k);

        }

        return result;
    }

    public void train(svm_parameter aParam, int cross_validation, ArrayList<Double[]> data) throws IOException {
        String[] input_parameters = constructInputParamaters(aParam);
        parse_command_line(input_parameters);
        getSVMProblemFromData(data);
        error_msg = svm.svm_check_parameter(prob, param);
        if (error_msg != null) {
            System.err.print("ERROR: " + error_msg + "\n");
            System.exit(1);
        }

        if (cross_validation==1) {
            do_cross_validation();
        } else {
            model = svm.svm_train(prob, param);
            svm.svm_save_model(model_file_name, model);
        }

    }

    public static void main(String argv[]) throws IOException {

        SVMTraining t = new SVMTraining();

        String[] input_parameters = t.constructInputParamaters("svm_param");

        t.run(input_parameters);
    }

    private static double atof(String s) {
        double d = Double.valueOf(s).doubleValue();
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            System.err.print("NaN or Infinity in input\n");
            System.exit(1);
        }
        return (d);
    }

    private static int atoi(String s) {
        return Integer.parseInt(s);
    }

    private void parse_command_line(String argv[]) {
        int i;
        svm_print_interface print_func = null;	// default printing to stdout

        param = new svm_parameter();
        // default values
        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.RBF;
        param.degree = 3;
        param.gamma = 0;	// 1/num_features
        param.coef0 = 0;
        param.nu = 0.5;
        param.cache_size = 100;
        param.C = 1;
        param.eps = 1e-3;
        param.p = 0.1;
        param.shrinking = 1;
        param.probability = 0;
        param.nr_weight = 0;
        param.weight_label = new int[0];
        param.weight = new double[0];
        cross_validation = 0;

        // parse options
        for (i = 0; i < argv.length; i++) {
            if (argv[i].charAt(0) != '-') {
                break;
            }
            if (++i >= argv.length) {
                exit_with_help();
            }
            switch (argv[i - 1].charAt(1)) {
                case 's':
                    param.svm_type = atoi(argv[i]);
                    break;
                case 't':
                    param.kernel_type = atoi(argv[i]);
                    break;
                case 'd':
                    param.degree = atoi(argv[i]);
                    break;
                case 'g':
                    param.gamma = atof(argv[i]);
                    break;
                case 'r':
                    param.coef0 = atof(argv[i]);
                    break;
                case 'n':
                    param.nu = atof(argv[i]);
                    break;
                case 'm':
                    param.cache_size = atof(argv[i]);
                    break;
                case 'c':
                    param.C = atof(argv[i]);
                    break;
                case 'e':
                    param.eps = atof(argv[i]);
                    break;
                case 'p':
                    param.p = atof(argv[i]);
                    break;
                case 'h':
                    param.shrinking = atoi(argv[i]);
                    break;
                case 'b':
                    param.probability = atoi(argv[i]);
                    break;
                case 'q':
                    print_func = svm_print_null;
                    i--;
                    break;
                case 'v':
                    cross_validation = 1;
                    nr_fold = atoi(argv[i]);
                    if (nr_fold < 2) {
                        System.err.print("n-fold cross validation: n must >= 2\n");
                        exit_with_help();
                    }
                    break;
                case 'w':
                    ++param.nr_weight;
                     {
                        int[] old = param.weight_label;
                        param.weight_label = new int[param.nr_weight];
                        System.arraycopy(old, 0, param.weight_label, 0, param.nr_weight - 1);
                    }

                     {
                        double[] old = param.weight;
                        param.weight = new double[param.nr_weight];
                        System.arraycopy(old, 0, param.weight, 0, param.nr_weight - 1);
                    }

                    param.weight_label[param.nr_weight - 1] = atoi(argv[i - 1].substring(2));
                    param.weight[param.nr_weight - 1] = atof(argv[i]);
                    break;
                default:
                    System.err.print("Unknown option: " + argv[i - 1] + "\n");
                    exit_with_help();
            }
        }

        svm.svm_set_print_string_function(print_func);

        // determine filenames
        if (i >= argv.length) {
            exit_with_help();
        }

        /*  input_file_name = argv[i];
        
        if (i < argv.length - 1) {
            model_file_name = argv[i + 1];
        } else {
            int p = argv[i].lastIndexOf('/');
            ++p;	// whew...
            model_file_name = argv[i].substring(p) + ".model";
        }*/
    }

    public svm_problem getSVMProblemFromData(ArrayList<Double[]> data) {
        ArrayList<Double> classes = new ArrayList<>();
        ArrayList<svm_node[]> vx = new ArrayList<>();
        int max_index = 0;
        int i = 0;
        for (Double[] aline : data) {
            double aClass = aline[0];// la classe est la première cellule de la ligne
            classes.add(i, aClass);
            int nodeSize = aline.length - 1;
            svm_node[] aNode = new svm_node[nodeSize];

            for (int j = 1; j < aline.length; j++) {
                aNode[j - 1] = new svm_node();
                aNode[j - 1].value = aline[j];
                aNode[j - 1].index = j;

            }
            vx.add(i, aNode);
            i = i + 1;
            if (nodeSize > 0) {
                max_index = Math.max(max_index, aNode[nodeSize - 1].index);
            }
        }

        prob = new svm_problem();
        prob.l = classes.size(); // la taille des données

        prob.x = new svm_node[prob.l][];
        for (int k = 0; k < prob.l; k++) {
            prob.x[k] = vx.get(k);
        }

        prob.y = new double[prob.l];

        for (int k = 0; k < prob.l; k++) {
            prob.y[k] = classes.get(k);
        }

        if (param.gamma == 0 && max_index > 0) {
            param.gamma = 1.0 / max_index;
        }

        if (param.kernel_type == svm_parameter.PRECOMPUTED) {
            for (int k = 0; k < prob.l; k++) {
                if (prob.x[k][0].index != 0) {
                    System.err.print("Wrong kernel matrix: first column must be 0:sample_serial_number\n");
                    System.exit(1);
                }
                if ((int) prob.x[k][0].value <= 0 || (int) prob.x[k][0].value > max_index) {
                    System.err.print("Wrong input format: sample_serial_number out of range\n");
                    System.exit(1);
                }
            }
        }

        return this.prob;

    }

    public svm_parameter getParam() {
        return param;
    }

    public void setParam(svm_parameter param) {
        this.param = param;
    }

    public svm_problem getProb() {
        return prob;
    }

    public void setProb(svm_problem prob) {
        this.prob = prob;
    }

    public svm_model getModel() {
        return model;
    }

    public void setModel(svm_model model) {
        this.model = model;
    }

    public String getModel_file_name() {
        return model_file_name;
    }

    public void setModel_file_name(String model_file_name) {
        this.model_file_name = model_file_name;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public int getCross_validation() {
        return cross_validation;
    }

    public void setCross_validation(int cross_validation) {
        this.cross_validation = cross_validation;
    }

    public int getNr_fold() {
        return nr_fold;
    }

    public void setNr_fold(int nr_fold) {
        this.nr_fold = nr_fold;
    }

    public static svm_print_interface getSvm_print_null() {
        return svm_print_null;
    }

    public static void setSvm_print_null(svm_print_interface svm_print_null) {
        SVMTraining.svm_print_null = svm_print_null;
    }

    public static svm_parameter parseParameter(String paramPath) {
        String json = "";

        try {
            BufferedReader fp = new BufferedReader(new FileReader("src/machine/learning/svm/training/" + paramPath));

            while (true) {

                String aline = fp.readLine();
                if (aline == null) {
                    break;
                } else {
                    json = json + aline;

                }

            }

            svm_parameter param = fromJson(json, svm_parameter.class);

            return param;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SVMTraining.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SVMTraining.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static <T extends Object> T fromJson(String json, Class<T> type) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        JsonParser parser = new JsonParser();

        JsonElement element = parser.parse(json);

        if (element.isJsonObject()) {
            return gson.fromJson(element.getAsJsonObject(), type);
        }
        if (element.isJsonArray()) {
            return gson.fromJson(element.getAsJsonArray(), type);
        }

        return gson.fromJson(element, type);
    }

}
