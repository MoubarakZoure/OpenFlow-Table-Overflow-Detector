/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.graphics.SwitchStateVizualisation;
import ids.detection.IntrusionDetector;
import ids.detection.SwitchMonitorAgent;
import ids.statistics.IDStatsCollector;
import ids.statistics.storage.StatsDBInterfacePooled;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import libsvm.svm_parameter;
import machine.learning.svm.prediction.SVMPredictor;
import machine.learning.svm.training.automatic.AggregatCreator;
import machine.learning.svm.training.automatic.AutomaticTraining;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author Moubarak
 */
public class SwitchMonitoringScrean extends javax.swing.JFrame {

    private static final Logger LOG = Logger.getLogger(SwitchMonitoringScrean.class.getName());

    private StatsDBInterfacePooled db = new StatsDBInterfacePooled(20);

    private AggregatCreator aggregatCreator;
    private ArrayList<String> learningAttributsList;

    AddSwitchFrame addSwitchFrame;

    ConfigurationIDS idsConfig = new ConfigurationIDS();

    IDStatsCollector collector;

    AutomaticTraining autoTrainer;
    IntrusionDetector intrusionDetector;
    private SVMPredictor classifier;
    private String model_file_name = "src/machine/learning/svm/training/svm_model";

    public int n_fold = -1;
    public int cross_validation = 0;

    public void applyConfig() {
        db.setDbConnectionProperties(idsConfig.getdBConnectionProperties());

        collector.setController_ip(idsConfig.getController_ip());
        collector.setController_port(idsConfig.getController_port());
        collector.setProtocol(idsConfig.getProtocol());

        collector.setWINDOWS(idsConfig.getCollectionFrequence());
        collector.setCLEANING_FREQUENCE(idsConfig.getCleaningFrequence());
        collector.setCLEANING_SIZE(idsConfig.getCleaningSize());
        collector.setdBConnectionProperties(idsConfig.getdBConnectionProperties());
        collector.setWorkersNumber(idsConfig.getWorkersNumber());

        model_file_name = idsConfig.getSvmModelPath();
        classifier = new SVMPredictor(model_file_name, 1, true);

        for (Map.Entry<String, SwitchMonitorAgent> agentEntry : intrusionDetector.getSwitchesMonitors().entrySet()) {
            agentEntry.getValue().setSend_mail(idsConfig.isSend_mail());
            agentEntry.getValue().setSave_alert(idsConfig.isSave_alert());
            agentEntry.getValue().setNotification_mal(idsConfig.getNotification_mal());
            agentEntry.getValue().setClassifier(classifier);
            agentEntry.getValue().setNextStartingWindow(System.currentTimeMillis());// à revoir absolument

        }

    }

    public void changeIDSConfig() {
        idsConfig.setProtocol(north_bound_api_protocol.getSelectedItem().toString());
        idsConfig.setController_port(controler_rest_port_field.getText());
        idsConfig.setController_ip(controler_ip_field.getText());

        idsConfig.setSvmModelPath(model_path_field.getText());

        idsConfig.getdBConnectionProperties().setHost(db_server_ip_field.getText());
        idsConfig.getdBConnectionProperties().setPort(Integer.parseInt(db_server_port_field.getText()));
        idsConfig.getdBConnectionProperties().setUser(db_user_field.getText());
        idsConfig.getdBConnectionProperties().setPassword(db_acces_password_field.getText());
        idsConfig.getdBConnectionProperties().setSchema(db_schema_field.getText());

        idsConfig.setNotification_mal(alerte_notification_email.getText());
        boolean save_alert = true;
        boolean send_mail = true;
        if (alert_send_mail_cmbx.getSelectedItem().toString().equals("jamais")) {
            send_mail = false;

        }
        if (alert_save_cmbx.getSelectedItem().toString().equals("jamais")) {
            save_alert = false;

        }
        idsConfig.setSend_mail(send_mail);
        idsConfig.setSave_alert(save_alert);

        idsConfig.setWorkersNumber(Integer.parseInt(collectionWorkersNumber.getText()));
        idsConfig.setCleaningSize(Integer.parseInt(cleaning_window.getText()));
        idsConfig.setCleaningFrequence(Integer.parseInt(cleaning_frequency.getText()));
        idsConfig.setCollectionFrequence(Integer.parseInt(collection_windows.getText()) * 1000);

    }

    public void initialPosition() {

        this.setLocationRelativeTo(null);

    }

    public void onExit() {

    }

    public void setUp() {
        initialPosition();
        db = new StatsDBInterfacePooled(10);
        aggregatCreator = new AggregatCreator(db);
        learningAttributsList = new ArrayList<>(); // à revoir absolument
        addSwitchFrame = new AddSwitchFrame(this, true);
        classifier = new SVMPredictor(model_file_name, 1, true);
        autoTrainer = new AutomaticTraining(db);
        intrusionDetector = new IntrusionDetector(db);

        collector = new IDStatsCollector();

        intrusionDetector.createAgentsPool();
        vizPanel.setLayout(new GridLayout(20, 3, 0, 0));

    }

    private void addChartsToScrollPane(SwitchStateVizualisation switchStateVizualisation, JPanel jpanel) {
        for (Map.Entry<String, JFreeChart> entry : switchStateVizualisation.getChartsMap().entrySet()) {
            JFreeChart chart = entry.getValue();
            ChartPanel panel = new ChartPanel(chart);
            panel.setSize(615, 295);
            jpanel.add(panel);

        }

    }

    public void monitorSwitch(SwitchMonitorAgent switchMonitorAgent) {
        switchMonitorAgent.setClassifier(classifier);
        switchMonitorAgent.setAttributList(learningAttributsList);
        switchMonitorAgent.setDbInferface(db);
        switchMonitorAgent.setSend_mail(idsConfig.isSend_mail());
        switchMonitorAgent.setSave_alert(idsConfig.isSave_alert());
        switchMonitorAgent.setNotification_mal(idsConfig.getNotification_mal());
        switchMonitorAgent.setNotification_table(alert_table);

        String swString = collector.getSwitchesList();
        if (swString.equals("")) {
            collector.setSwitchesList(switchMonitorAgent.getSwitchName());

        } else {

        }
        collector.setSwitchesList(collector.getSwitchesList() + ";" + switchMonitorAgent.getSwitchName());

        if (!collector.isIsCollecting()) {
            collector.startCollection();

        }

        switchMonitorAgent.setNextStartingWindow(System.currentTimeMillis());// à revoir absolument

        intrusionDetector.addASwitchMonitor(switchMonitorAgent);

        intrusionDetector.startASwitchMonitoring(switchMonitorAgent.getSwitchName());

        SwitchStateVizualisation switchStateVizualisation = new SwitchStateVizualisation(switchMonitorAgent.getSwitchName());
        switchStateVizualisation.init();
        switchMonitorAgent.setViz(switchStateVizualisation);
        addChartsToScrollPane(switchStateVizualisation, vizPanel);

        JOptionPane.showMessageDialog(this, "Surveillance du commutateur " + switchMonitorAgent.getSwitchName() + " lancée avec succès");

    }

    public SwitchMonitoringScrean() {
        initComponents();
        setUp();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        tabbedPane = new javax.swing.JTabbedPane();
        secondScrean = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        vizPanel = new javax.swing.JPanel();
        thirdScrean = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        alert_table = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        controlerConfigPanel1 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        fenetre_aggregation_field = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        capacite_table_field = new javax.swing.JTextField();
        type_traffic_cmbx = new javax.swing.JComboBox<>();
        aggregerButton = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        list_switch_field = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        fonction_noyau_cmbx = new javax.swing.JComboBox<>();
        svm_degre_field = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        svm_epsilon = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        svm_gamma_field = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        svm_c_field = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        svm_coef0_field = new javax.swing.JTextField();
        svm_nu_field = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        svm_cache_size_field = new javax.swing.JTextField();
        svm_poids_attack_field = new javax.swing.JTextField();
        svm_type_cmbx = new javax.swing.JComboBox<>();
        jLabel44 = new javax.swing.JLabel();
        svm_shrinking_check_box = new javax.swing.JCheckBox();
        svm_probability_check_box = new javax.swing.JCheckBox();
        jLabel45 = new javax.swing.JLabel();
        svm_poids_normal_field = new javax.swing.JTextField();
        launchTraining_bttn = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        svm_epsilon_critere_arret_field = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        svm_n_fold_field = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        tableFullCount_check_box = new javax.swing.JCheckBox();
        missFlowRate_table_capacity_check_box = new javax.swing.JCheckBox();
        missFlowCount_check_box = new javax.swing.JCheckBox();
        matchCount_check_box = new javax.swing.JCheckBox();
        lookUpCount_check_box = new javax.swing.JCheckBox();
        lookUpRate_check_box = new javax.swing.JCheckBox();
        matchRate_check_box = new javax.swing.JCheckBox();
        matchIndication_check_box = new javax.swing.JCheckBox();
        occupation_average_check_box = new javax.swing.JCheckBox();
        missFlowRate_check_box = new javax.swing.JCheckBox();
        missFlowCount_table_capacity_check_box = new javax.swing.JCheckBox();
        firstScrean = new javax.swing.JPanel();
        databaseConfigPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        db_server_ip_field = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        db_server_port_field = new javax.swing.JTextField();
        db_user_field = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        db_acces_password_field = new javax.swing.JPasswordField();
        jLabel10 = new javax.swing.JLabel();
        db_schema_field = new javax.swing.JTextField();
        controlerConfigPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        controler_ip_field = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        controler_rest_port_field = new javax.swing.JTextField();
        north_bound_api_protocol = new javax.swing.JComboBox<>();
        databaseConfigPanel1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        collection_windows = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        cleaning_frequency = new javax.swing.JTextField();
        cleaning_window = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        collectionWorkersNumber = new javax.swing.JTextField();
        databaseConfigPanel2 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        alerte_notification_email = new javax.swing.JTextField();
        alert_save_cmbx = new javax.swing.JComboBox<>();
        alert_send_mail_cmbx = new javax.swing.JComboBox<>();
        databaseConfigPanel5 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        model_path_field = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        alert_send_mail_cmbx4 = new javax.swing.JComboBox<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        addSwitchMenu = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        validerConfigurationMenu = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout vizPanelLayout = new javax.swing.GroupLayout(vizPanel);
        vizPanel.setLayout(vizPanelLayout);
        vizPanelLayout.setHorizontalGroup(
            vizPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1336, Short.MAX_VALUE)
        );
        vizPanelLayout.setVerticalGroup(
            vizPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 679, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(vizPanel);

        javax.swing.GroupLayout secondScreanLayout = new javax.swing.GroupLayout(secondScrean);
        secondScrean.setLayout(secondScreanLayout);
        secondScreanLayout.setHorizontalGroup(
            secondScreanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, secondScreanLayout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1027, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );
        secondScreanLayout.setVerticalGroup(
            secondScreanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(secondScreanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 623, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Tableau de bord", secondScrean);

        alert_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Sévérité", "Temps", "Switch", "Message"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        alert_table.setToolTipText("");
        jScrollPane2.setViewportView(alert_table);

        javax.swing.GroupLayout thirdScreanLayout = new javax.swing.GroupLayout(thirdScrean);
        thirdScrean.setLayout(thirdScreanLayout);
        thirdScreanLayout.setHorizontalGroup(
            thirdScreanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(thirdScreanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1091, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        thirdScreanLayout.setVerticalGroup(
            thirdScreanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(thirdScreanLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 76, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Evenements", thirdScrean);

        controlerConfigPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel20.setFont(new java.awt.Font("HP Simplified", 1, 20)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Paramètres d'aggrégation");

        jLabel21.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel21.setText("Type de Traffic");

        jLabel22.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel22.setText("Capacité de table de flux");

        fenetre_aggregation_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        fenetre_aggregation_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fenetre_aggregation_field.setText("10");
        fenetre_aggregation_field.setToolTipText("");

        jLabel23.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel23.setText("Fenêtre d'aggrégation (sec)");

        capacite_table_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        capacite_table_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        capacite_table_field.setText("4000");
        capacite_table_field.setToolTipText("");

        type_traffic_cmbx.setFont(new java.awt.Font("HP Simplified", 0, 11)); // NOI18N
        type_traffic_cmbx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NORMAL", "ATTACK" }));

        aggregerButton.setText("Aggréger");
        aggregerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aggregerButtonActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel24.setText("Liste des commutateurs");

        list_switch_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        list_switch_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        list_switch_field.setText("s1");
        list_switch_field.setToolTipText("");

        javax.swing.GroupLayout controlerConfigPanel1Layout = new javax.swing.GroupLayout(controlerConfigPanel1);
        controlerConfigPanel1.setLayout(controlerConfigPanel1Layout);
        controlerConfigPanel1Layout.setHorizontalGroup(
            controlerConfigPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(controlerConfigPanel1Layout.createSequentialGroup()
                .addGap(140, 140, 140)
                .addComponent(aggregerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controlerConfigPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controlerConfigPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(controlerConfigPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                        .addComponent(fenetre_aggregation_field, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(controlerConfigPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(capacite_table_field, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(controlerConfigPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(type_traffic_cmbx, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(controlerConfigPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(list_switch_field, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(74, 74, 74))
        );
        controlerConfigPanel1Layout.setVerticalGroup(
            controlerConfigPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlerConfigPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(controlerConfigPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fenetre_aggregation_field, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(controlerConfigPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(capacite_table_field, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(controlerConfigPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(type_traffic_cmbx, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(controlerConfigPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(list_switch_field, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addComponent(aggregerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel31.setFont(new java.awt.Font("HP Simplified", 1, 20)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("Paramètres d'apprentissage du modèle SVM");

        jLabel32.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel32.setText("type SVM");

        jLabel33.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel33.setText("fonction noyau");

        fonction_noyau_cmbx.setFont(new java.awt.Font("HP Simplified", 0, 11)); // NOI18N
        fonction_noyau_cmbx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "linéaire", "polynomiale", "rbf", "sigmoid", "précalculé" }));

        svm_degre_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        svm_degre_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        svm_degre_field.setText("3");
        svm_degre_field.setToolTipText("");

        jLabel34.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel34.setText("degre");

        jLabel35.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel35.setText("gamma");

        svm_epsilon.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        svm_epsilon.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        svm_epsilon.setText("0.1");
        svm_epsilon.setToolTipText("");

        jLabel36.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel36.setText("epsilon");

        svm_gamma_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        svm_gamma_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        svm_gamma_field.setText("1");
        svm_gamma_field.setToolTipText("");

        jLabel37.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel37.setText("C");

        svm_c_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        svm_c_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        svm_c_field.setText("1");
        svm_c_field.setToolTipText("");

        jLabel38.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel38.setText("nu");

        jLabel39.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel39.setText("coef0");

        svm_coef0_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        svm_coef0_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        svm_coef0_field.setText("0");
        svm_coef0_field.setToolTipText("");

        svm_nu_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        svm_nu_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        svm_nu_field.setText("0.5");
        svm_nu_field.setToolTipText("");

        jLabel41.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel41.setText("taille du cache");

        svm_cache_size_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        svm_cache_size_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        svm_cache_size_field.setText("100");
        svm_cache_size_field.setToolTipText("");

        svm_poids_attack_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        svm_poids_attack_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        svm_poids_attack_field.setText("1");
        svm_poids_attack_field.setToolTipText("");

        svm_type_cmbx.setFont(new java.awt.Font("HP Simplified", 0, 11)); // NOI18N
        svm_type_cmbx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "C-SVC", "nu-SVC", "one-class SVM", "epsilon-SVR", "nu-SVR" }));

        jLabel44.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel44.setText("poids classe attack");

        svm_shrinking_check_box.setSelected(true);
        svm_shrinking_check_box.setText("contraction");

        svm_probability_check_box.setSelected(true);
        svm_probability_check_box.setText("probabilité");

        jLabel45.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel45.setText("poids classe normal");

        svm_poids_normal_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        svm_poids_normal_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        svm_poids_normal_field.setText("1");
        svm_poids_normal_field.setToolTipText("");

        launchTraining_bttn.setText("Apprentissage");
        launchTraining_bttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                launchTraining_bttnActionPerformed(evt);
            }
        });

        jLabel46.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel46.setText("n_fold");

        svm_epsilon_critere_arret_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        svm_epsilon_critere_arret_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        svm_epsilon_critere_arret_field.setText("0.001");
        svm_epsilon_critere_arret_field.setToolTipText("");

        jLabel47.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel47.setText("Critère d'arret");

        svm_n_fold_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        svm_n_fold_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        svm_n_fold_field.setText("0");
        svm_n_fold_field.setToolTipText("");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(svm_n_fold_field, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(svm_shrinking_check_box, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(svm_probability_check_box, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(svm_type_cmbx, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fonction_noyau_cmbx, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(svm_degre_field, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(svm_gamma_field, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(svm_epsilon, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(svm_c_field, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(svm_poids_attack_field, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(svm_cache_size_field, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(svm_poids_normal_field, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(svm_epsilon_critere_arret_field, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(29, 29, 29)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(svm_coef0_field, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(svm_nu_field, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                                        .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(launchTraining_bttn, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(193, 193, 193))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(svm_type_cmbx, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(fonction_noyau_cmbx, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(svm_degre_field, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(svm_gamma_field, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(svm_c_field, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(svm_epsilon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(svm_epsilon_critere_arret_field, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(svm_coef0_field, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(svm_nu_field, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(svm_cache_size_field, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(svm_poids_attack_field, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(svm_poids_normal_field, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(svm_n_fold_field, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(svm_shrinking_check_box)
                    .addComponent(svm_probability_check_box))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(launchTraining_bttn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel40.setFont(new java.awt.Font("HP Simplified", 1, 20)); // NOI18N
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("Liste des attributs");

        tableFullCount_check_box.setText("table_full_count");

        missFlowRate_table_capacity_check_box.setText("MissFlowRate/Table Capacity");

        missFlowCount_check_box.setText("missFlowCount");

        matchCount_check_box.setText("matchCount");

        lookUpCount_check_box.setText("lookUpCount");

        lookUpRate_check_box.setText("lookUpRate");

        matchRate_check_box.setText("matchRate");

        matchIndication_check_box.setText("matchIndication");

        occupation_average_check_box.setText("occupation_average");

        missFlowRate_check_box.setText("missFlowRate");

        missFlowCount_table_capacity_check_box.setText("missFlowCount/TableCapacity");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(29, 29, 29))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tableFullCount_check_box, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(missFlowCount_check_box, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(missFlowRate_check_box, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(78, 78, 78))
                    .addComponent(missFlowRate_table_capacity_check_box, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lookUpRate_check_box, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lookUpCount_check_box, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(matchIndication_check_box, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(missFlowCount_table_capacity_check_box))
                .addGap(99, 99, 99)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(occupation_average_check_box, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(matchCount_check_box, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(matchRate_check_box, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(178, 178, 178))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lookUpRate_check_box, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addComponent(lookUpCount_check_box, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(matchIndication_check_box, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(missFlowCount_table_capacity_check_box))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(missFlowRate_check_box, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(52, 52, 52)
                                        .addComponent(missFlowCount_check_box, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tableFullCount_check_box, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                                .addComponent(missFlowRate_table_capacity_check_box, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(8, 8, 8))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(matchRate_check_box, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(matchCount_check_box, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(occupation_average_check_box, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41))))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(controlerConfigPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(122, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(controlerConfigPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        tabbedPane.addTab("Apprentissage", jPanel1);

        databaseConfigPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("HP Simplified", 1, 20)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Base de données");

        jLabel2.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel2.setText("Utilisateur");

        jLabel3.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel3.setText("Port Serveur");

        db_server_ip_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        db_server_ip_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        db_server_ip_field.setText("localhost");
        db_server_ip_field.setToolTipText("");

        jLabel4.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel4.setText("Adresse IP");

        db_server_port_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        db_server_port_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        db_server_port_field.setText("3306");
        db_server_port_field.setToolTipText("");

        db_user_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        db_user_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        db_user_field.setText("root");
        db_user_field.setToolTipText("");

        jLabel9.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel9.setText("Mot de passe");

        db_acces_password_field.setText("root");

        jLabel10.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel10.setText("Schema");

        db_schema_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        db_schema_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        db_schema_field.setText("ids_db");
        db_schema_field.setToolTipText("");

        javax.swing.GroupLayout databaseConfigPanelLayout = new javax.swing.GroupLayout(databaseConfigPanel);
        databaseConfigPanel.setLayout(databaseConfigPanelLayout);
        databaseConfigPanelLayout.setHorizontalGroup(
            databaseConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(databaseConfigPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(databaseConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, databaseConfigPanelLayout.createSequentialGroup()
                        .addGroup(databaseConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(databaseConfigPanelLayout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(db_schema_field))
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(databaseConfigPanelLayout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(db_acces_password_field, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(87, Short.MAX_VALUE))
                    .addGroup(databaseConfigPanelLayout.createSequentialGroup()
                        .addGroup(databaseConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(db_user_field, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(databaseConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(databaseConfigPanelLayout.createSequentialGroup()
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(db_server_port_field))
                                .addGroup(databaseConfigPanelLayout.createSequentialGroup()
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(db_server_ip_field, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        databaseConfigPanelLayout.setVerticalGroup(
            databaseConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(databaseConfigPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(databaseConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(db_server_ip_field, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(databaseConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(db_server_port_field, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(databaseConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(db_user_field, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(databaseConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(db_acces_password_field, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(databaseConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(db_schema_field, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(104, Short.MAX_VALUE))
        );

        controlerConfigPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("HP Simplified", 1, 20)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Controleur OpenFlow");

        jLabel6.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel6.setText("Protocole");

        jLabel7.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel7.setText("Port Serveur Rest");

        controler_ip_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        controler_ip_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        controler_ip_field.setText("192.168.72.131");
        controler_ip_field.setToolTipText("");

        jLabel8.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel8.setText("Adresse IP");

        controler_rest_port_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        controler_rest_port_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        controler_rest_port_field.setText("8080");
        controler_rest_port_field.setToolTipText("");

        north_bound_api_protocol.setFont(new java.awt.Font("HP Simplified", 0, 11)); // NOI18N
        north_bound_api_protocol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "http", "https" }));

        javax.swing.GroupLayout controlerConfigPanelLayout = new javax.swing.GroupLayout(controlerConfigPanel);
        controlerConfigPanel.setLayout(controlerConfigPanelLayout);
        controlerConfigPanelLayout.setHorizontalGroup(
            controlerConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controlerConfigPanelLayout.createSequentialGroup()
                .addGroup(controlerConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(controlerConfigPanelLayout.createSequentialGroup()
                        .addGroup(controlerConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(controlerConfigPanelLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(controlerConfigPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(controlerConfigPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(controlerConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controlerConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(controler_rest_port_field, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(controler_ip_field, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                            .addComponent(north_bound_api_protocol, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)))
                .addGap(54, 54, 54))
        );
        controlerConfigPanelLayout.setVerticalGroup(
            controlerConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlerConfigPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(controlerConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(controler_ip_field, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(controlerConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(controler_rest_port_field, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(controlerConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(north_bound_api_protocol, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(170, Short.MAX_VALUE))
        );

        databaseConfigPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel11.setFont(new java.awt.Font("HP Simplified", 1, 20)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Collecte des données");

        jLabel12.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel12.setText("Fenetre de nettoyage");

        jLabel13.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel13.setText("Fréquence de nettoyage");

        collection_windows.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        collection_windows.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        collection_windows.setText("3");
        collection_windows.setToolTipText("");

        jLabel14.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel14.setText("Fréquence (secondes)");

        cleaning_frequency.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        cleaning_frequency.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cleaning_frequency.setText("60");
        cleaning_frequency.setToolTipText("");

        cleaning_window.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        cleaning_window.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cleaning_window.setText("1500");
        cleaning_window.setToolTipText("");

        jLabel16.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel16.setText("Taille du pool");

        collectionWorkersNumber.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        collectionWorkersNumber.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        collectionWorkersNumber.setText("60");
        collectionWorkersNumber.setToolTipText("");

        javax.swing.GroupLayout databaseConfigPanel1Layout = new javax.swing.GroupLayout(databaseConfigPanel1);
        databaseConfigPanel1.setLayout(databaseConfigPanel1Layout);
        databaseConfigPanel1Layout.setHorizontalGroup(
            databaseConfigPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(databaseConfigPanel1Layout.createSequentialGroup()
                .addGroup(databaseConfigPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(databaseConfigPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(databaseConfigPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(databaseConfigPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(databaseConfigPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18))
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(databaseConfigPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(databaseConfigPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cleaning_window, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(cleaning_frequency)
                            .addComponent(collection_windows)
                            .addComponent(collectionWorkersNumber))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)))
                .addContainerGap())
        );
        databaseConfigPanel1Layout.setVerticalGroup(
            databaseConfigPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(databaseConfigPanel1Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(databaseConfigPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(collection_windows, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(databaseConfigPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cleaning_frequency, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(databaseConfigPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cleaning_window, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(databaseConfigPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(collectionWorkersNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        databaseConfigPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel15.setFont(new java.awt.Font("HP Simplified", 1, 20)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Alerte");

        jLabel17.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel17.setText("Sauvegarde des alertes");

        jLabel18.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel18.setText("Email de notification");

        jLabel19.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel19.setText("Envoyer mail ");

        alerte_notification_email.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        alerte_notification_email.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        alerte_notification_email.setText("MoubarakZoure@gmail.com");
        alerte_notification_email.setToolTipText("");

        alert_save_cmbx.setFont(new java.awt.Font("HP Simplified", 0, 11)); // NOI18N
        alert_save_cmbx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "toujours", "jamais" }));

        alert_send_mail_cmbx.setFont(new java.awt.Font("HP Simplified", 0, 11)); // NOI18N
        alert_send_mail_cmbx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "toujours", "jamais" }));

        javax.swing.GroupLayout databaseConfigPanel2Layout = new javax.swing.GroupLayout(databaseConfigPanel2);
        databaseConfigPanel2.setLayout(databaseConfigPanel2Layout);
        databaseConfigPanel2Layout.setHorizontalGroup(
            databaseConfigPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(databaseConfigPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(databaseConfigPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(databaseConfigPanel2Layout.createSequentialGroup()
                        .addGroup(databaseConfigPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(databaseConfigPanel2Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel18)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(databaseConfigPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(alert_send_mail_cmbx, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(alerte_notification_email, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                            .addComponent(alert_save_cmbx, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 37, Short.MAX_VALUE))
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1))
        );
        databaseConfigPanel2Layout.setVerticalGroup(
            databaseConfigPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(databaseConfigPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(databaseConfigPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alert_send_mail_cmbx))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(databaseConfigPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alerte_notification_email, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(databaseConfigPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alert_save_cmbx, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(63, Short.MAX_VALUE))
        );

        databaseConfigPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel28.setFont(new java.awt.Font("HP Simplified", 1, 20)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Modèle SVM");

        jLabel29.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel29.setText("Charger le modèle");

        model_path_field.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        model_path_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        model_path_field.setText("src/machine/learning/svm/training/svm_model");
        model_path_field.setToolTipText("");

        jLabel30.setFont(new java.awt.Font("HP Simplified", 0, 14)); // NOI18N
        jLabel30.setText("Prédire les probabilités");

        alert_send_mail_cmbx4.setFont(new java.awt.Font("HP Simplified", 0, 11)); // NOI18N
        alert_send_mail_cmbx4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "toujours", "jamais" }));

        javax.swing.GroupLayout databaseConfigPanel5Layout = new javax.swing.GroupLayout(databaseConfigPanel5);
        databaseConfigPanel5.setLayout(databaseConfigPanel5Layout);
        databaseConfigPanel5Layout.setHorizontalGroup(
            databaseConfigPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(databaseConfigPanel5Layout.createSequentialGroup()
                .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(databaseConfigPanel5Layout.createSequentialGroup()
                .addGroup(databaseConfigPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(databaseConfigPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(databaseConfigPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(alert_send_mail_cmbx4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(model_path_field, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(181, 181, 181))
        );
        databaseConfigPanel5Layout.setVerticalGroup(
            databaseConfigPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(databaseConfigPanel5Layout.createSequentialGroup()
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addGroup(databaseConfigPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(model_path_field, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(92, 92, 92)
                .addGroup(databaseConfigPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alert_send_mail_cmbx4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout firstScreanLayout = new javax.swing.GroupLayout(firstScrean);
        firstScrean.setLayout(firstScreanLayout);
        firstScreanLayout.setHorizontalGroup(
            firstScreanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(firstScreanLayout.createSequentialGroup()
                .addGroup(firstScreanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(firstScreanLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(controlerConfigPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, firstScreanLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(databaseConfigPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(firstScreanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(firstScreanLayout.createSequentialGroup()
                        .addComponent(databaseConfigPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(databaseConfigPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(databaseConfigPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        firstScreanLayout.setVerticalGroup(
            firstScreanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, firstScreanLayout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(firstScreanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(controlerConfigPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(databaseConfigPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(databaseConfigPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(firstScreanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(databaseConfigPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(databaseConfigPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        tabbedPane.addTab("Configuration", null, firstScrean, "");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jMenu1.setText("File");

        exitMenuItem.setText("quitter");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(exitMenuItem);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Outils");

        addSwitchMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        addSwitchMenu.setText("Ajouter commutateur");
        addSwitchMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSwitchMenuActionPerformed(evt);
            }
        });
        jMenu2.add(addSwitchMenu);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Configuration");

        validerConfigurationMenu.setText("Valider configuration");
        validerConfigurationMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validerConfigurationMenuActionPerformed(evt);
            }
        });
        jMenu3.add(validerConfigurationMenu);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        // TODO add your handling code here:
        onExit();
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void launchTraining_bttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_launchTraining_bttnActionPerformed
        // TODO add your handling code here:

        svm_parameter parameters = get_svm_parameter();
        learningAttributsList.removeAll(learningAttributsList);
        learningAttributsList = getAttributsList();

        autoTrainer.train(parameters, learningAttributsList, cross_validation, n_fold);

    }//GEN-LAST:event_launchTraining_bttnActionPerformed

    private void aggregerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aggregerButtonActionPerformed
        // TODO add your handling code here:

        aggregation();
    }//GEN-LAST:event_aggregerButtonActionPerformed

    private void addSwitchMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSwitchMenuActionPerformed
        // TODO add your handling code here:
        SwitchMonitorAgent switchMonitorAgent = new SwitchMonitorAgent();
        this.addSwitchFrame.shows(switchMonitorAgent, this);
    }//GEN-LAST:event_addSwitchMenuActionPerformed

    private void validerConfigurationMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validerConfigurationMenuActionPerformed
        // TODO add your handling code here:
        changeIDSConfig();
        applyConfig();

    }//GEN-LAST:event_validerConfigurationMenuActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        collector.stopCollection(true);
        intrusionDetector.stopASwitchMonitors();
        collector.getDbInferface().clearPool();
        intrusionDetector.getDbInferface().clearPool();
        LOG.info("closed !!");

    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        if (collector.getScheduler() != null) {
            collector.stopCollection(true);
        }
        if (intrusionDetector.getScheduler() != null) {
            intrusionDetector.stopASwitchMonitors();
        }
        collector.getDbInferface().clearPool();
        intrusionDetector.getDbInferface().clearPool();
        LOG.info("closed !!");
    }//GEN-LAST:event_formWindowClosing

    public void aggregation() {
        int window = Integer.parseInt(fenetre_aggregation_field.getText()) * 1000;
        int table_capacity = Integer.parseInt(capacite_table_field.getText());
        String trafficType = type_traffic_cmbx.getSelectedItem().toString();

        aggregatCreator.setWINDOWS_SIZE(window);
        aggregatCreator.setTableSize(table_capacity);
        aggregatCreator.setTrafficType(trafficType);

        ArrayList<String> list_switch = new ArrayList<>();
        String str = list_switch_field.getText();
        StringTokenizer tokenizer = new StringTokenizer(str, ";");
        while (tokenizer.hasMoreTokens()) {

            list_switch.add(tokenizer.nextToken());

        }

        for (String aSwitch : list_switch) {
            aggregatCreator.setSwitchName(aSwitch);
            aggregatCreator.createAggregats();

        }

    }

    ArrayList<String> getAttributsList() {

        ArrayList<String> list = new ArrayList<>();

        if (missFlowRate_check_box.isSelected()) {
            list.add("missFlowRate");
        }

        if (missFlowRate_table_capacity_check_box.isSelected()) {
            list.add("missFlowRate/Table_Capacity");
        }
        if (missFlowCount_table_capacity_check_box.isSelected()) {
            list.add("missFlowCount/Table_Capacity");
        }

        if (missFlowCount_check_box.isSelected()) {

            list.add("missFlowCount");

        }
        if (lookUpRate_check_box.isSelected()) {
            list.add("lookUpRate");

        }

        if (lookUpCount_check_box.isSelected()) {
            list.add("lookUpCount");

        }

        if (matchRate_check_box.isSelected()) {

            list.add("matchRate");

        }

        if (matchCount_check_box.isSelected()) {
            list.add("matchCount");

        }

        if (tableFullCount_check_box.isSelected()) {
            list.add("table_full_count");

        }

        if (occupation_average_check_box.isSelected()) {
            list.add("occupation_average");

        }

        if (matchIndication_check_box.isSelected()) {
            list.add("missFlowRateUnderLookUpRate");

        }

        return list;

    }

    public svm_parameter get_svm_parameter() {
        svm_parameter aParam = new svm_parameter();
        aParam.svm_type = getSVMType(this.svm_type_cmbx.getSelectedItem().toString());
        aParam.kernel_type = getKernelType(this.fonction_noyau_cmbx.getSelectedItem().toString());
        aParam.degree = Integer.parseInt(svm_degre_field.getText());
        aParam.gamma = Double.parseDouble(svm_gamma_field.getText());
        aParam.coef0 = Double.parseDouble(svm_coef0_field.getText());
        aParam.C = Double.parseDouble(svm_c_field.getText());
        aParam.nu = Double.parseDouble(svm_nu_field.getText());
        aParam.eps = Double.parseDouble(svm_epsilon.getText());
        aParam.cache_size = Double.parseDouble(svm_cache_size_field.getText());
        if (svm_shrinking_check_box.isSelected()) {
            aParam.shrinking = 1;

        } else {
            aParam.shrinking = 0;
        }
        if (svm_probability_check_box.isSelected()) {

            aParam.probability = 1;
        } else {

            aParam.probability = 0;
        }

        if ((!svm_poids_attack_field.getText().equals("")) && (!svm_poids_normal_field.getText().equals(""))) {
            aParam.nr_weight = 2;
            aParam.weight = new double[2];
            aParam.weight_label = new int[]{0, 1};
            aParam.weight[0] = Double.parseDouble(svm_poids_normal_field.getText());
            aParam.weight[1] = Double.parseDouble(svm_poids_attack_field.getText());

        } else {
            aParam.nr_weight = 0;
            aParam.weight = new double[0];
            aParam.weight_label = new int[0];

        }

        aParam.p = 0.1;
        int n = 0;

        if (!svm_n_fold_field.getText().equals("")) {
            n = Integer.parseInt(svm_n_fold_field.getText());

        }
        if (n >= 2) {
            this.n_fold = n;
            this.cross_validation = 1;
        } else {
            this.n_fold = n;
            this.cross_validation = 0;
        }

        return aParam;

    }

    public int getKernelType(String str) {
        switch (str) {

            case "linéaire": {
                return 0;
            }

            case "polynomiale": {
                return 1;
            }

            case "rbf": {
                return 2;
            }

            case "sigmoid": {

                return 3;

            }

            case "précalculé": {

                return 4;

            }

            default: {
                return -10;
            }

        }

    }

    public int getSVMType(String str) {
        switch (str) {

            case "C-SVC": {
                return 0;
            }

            case "nu-SVC": {
                return 1;
            }

            case "one-class SVM": {
                return 2;
            }

            case "epsilon-SVR": {

                return 3;

            }

            case "nu-SVR": {

                return 4;

            }

            default: {
                return -10;
            }

        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SwitchMonitoringScrean.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SwitchMonitoringScrean.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SwitchMonitoringScrean.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SwitchMonitoringScrean.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SwitchMonitoringScrean().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addSwitchMenu;
    private javax.swing.JButton aggregerButton;
    private javax.swing.JComboBox<String> alert_save_cmbx;
    private javax.swing.JComboBox<String> alert_send_mail_cmbx;
    private javax.swing.JComboBox<String> alert_send_mail_cmbx4;
    private javax.swing.JTable alert_table;
    private javax.swing.JTextField alerte_notification_email;
    private javax.swing.JTextField capacite_table_field;
    private javax.swing.JTextField cleaning_frequency;
    private javax.swing.JTextField cleaning_window;
    private javax.swing.JTextField collectionWorkersNumber;
    private javax.swing.JTextField collection_windows;
    private javax.swing.JPanel controlerConfigPanel;
    private javax.swing.JPanel controlerConfigPanel1;
    private javax.swing.JTextField controler_ip_field;
    private javax.swing.JTextField controler_rest_port_field;
    private javax.swing.JPanel databaseConfigPanel;
    private javax.swing.JPanel databaseConfigPanel1;
    private javax.swing.JPanel databaseConfigPanel2;
    private javax.swing.JPanel databaseConfigPanel5;
    private javax.swing.JPasswordField db_acces_password_field;
    private javax.swing.JTextField db_schema_field;
    private javax.swing.JTextField db_server_ip_field;
    private javax.swing.JTextField db_server_port_field;
    private javax.swing.JTextField db_user_field;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JTextField fenetre_aggregation_field;
    private javax.swing.JPanel firstScrean;
    private javax.swing.JComboBox<String> fonction_noyau_cmbx;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton launchTraining_bttn;
    private javax.swing.JTextField list_switch_field;
    private javax.swing.JCheckBox lookUpCount_check_box;
    private javax.swing.JCheckBox lookUpRate_check_box;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JCheckBox matchCount_check_box;
    private javax.swing.JCheckBox matchIndication_check_box;
    private javax.swing.JCheckBox matchRate_check_box;
    private javax.swing.JCheckBox missFlowCount_check_box;
    private javax.swing.JCheckBox missFlowCount_table_capacity_check_box;
    private javax.swing.JCheckBox missFlowRate_check_box;
    private javax.swing.JCheckBox missFlowRate_table_capacity_check_box;
    private javax.swing.JTextField model_path_field;
    private javax.swing.JComboBox<String> north_bound_api_protocol;
    private javax.swing.JCheckBox occupation_average_check_box;
    private javax.swing.JPanel secondScrean;
    private javax.swing.JTextField svm_c_field;
    private javax.swing.JTextField svm_cache_size_field;
    private javax.swing.JTextField svm_coef0_field;
    private javax.swing.JTextField svm_degre_field;
    private javax.swing.JTextField svm_epsilon;
    private javax.swing.JTextField svm_epsilon_critere_arret_field;
    private javax.swing.JTextField svm_gamma_field;
    private javax.swing.JTextField svm_n_fold_field;
    private javax.swing.JTextField svm_nu_field;
    private javax.swing.JTextField svm_poids_attack_field;
    private javax.swing.JTextField svm_poids_normal_field;
    private javax.swing.JCheckBox svm_probability_check_box;
    private javax.swing.JCheckBox svm_shrinking_check_box;
    private javax.swing.JComboBox<String> svm_type_cmbx;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JCheckBox tableFullCount_check_box;
    private javax.swing.JPanel thirdScrean;
    private javax.swing.JComboBox<String> type_traffic_cmbx;
    private javax.swing.JMenuItem validerConfigurationMenu;
    private javax.swing.JPanel vizPanel;
    // End of variables declaration//GEN-END:variables
}
