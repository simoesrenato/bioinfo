/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsc.epibuilder.gui;

import br.udesc.epibuilder.bepipred.BepiPredOnlineRunner;
import br.udesc.epibuilder.blast.CommandTest;
import br.ufsc.epibuilder.EpitopeFinder;
import br.ufsc.epibuilder.Parameters;
import br.ufsc.epibuilder.converter.FileHelper;
import br.ufsc.epibuilder.converter.ProteinConverter;
import br.ufsc.epibuilder.entity.Proteome;
import br.ufsc.epibuilder.entity.SoftwareBcellEnum;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicScrollBarUI;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;

/**
 *
 * @author renato
 */
public class Main extends javax.swing.JFrame {

    /**
     * Creates new form Main
     */
    private Clipboard clipboard = getToolkit().getSystemClipboard();
    private File lastFile = new File(".");
    private Vector<Proteome> vectorProteome = new Vector<>();
    private int fontSize = 12;

    private File properties = new File("properties.conf");

    public void loadProperties() {
        try {
            Properties config = new Properties();
            config.load(new FileReader(properties));
            String src = ".";
            String srcProp = config.getProperty("last_file");
            if (srcProp != null && !srcProp.trim().equals("")) {
                src = srcProp;
            }
            lastFile = new File(src);
            if (config.getProperty("makeblastdb") != null) {
                jtMakeblastdb.setText(config.getProperty("makeblastdb"));
            }
            if (config.getProperty("blastp") != null) {
                jtBlastp.setText(config.getProperty("blastp"));
            }

            jtBepiPredJobId.setText(config.getProperty("jobid"));

        } catch (FileNotFoundException ex) {
            saveProperties();
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void saveProperties() {
        FileWriter fw = null;
        try {
            fw = new FileWriter(properties);
            Properties config = new Properties();
            String lastFileSrc = lastFile.getAbsolutePath();
            if (lastFile == null) {
                lastFileSrc = ".";
            }
            config.put("last_file", lastFileSrc);
            config.put("makeblastdb", Parameters.MAKEBLASTDB_PATH);
            config.put("blastp", Parameters.BLASTP_PATH);
            config.put("jobid", jtBepiPredJobId.getText());
            config.store(fw, "Epibuilder configuration");

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Main() {
        initComponents();

        listProteomes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        bgBepipred.add(jrBepipredOnline);
        bgBepipred.add(jrIEDBStd);
        bgBepipred.add(jrBepipredJobid);

        bgEmini.add(jrEminiDefault);
        bgEmini.add(jrEminiThreshold);

        bgParker.add(jrParkerDefault);
        bgParker.add(jrParkerThreshold);

        bgKolaskar.add(jrKolaskarDefault);
        bgKolaskar.add(jrKolaskarThreshold);

        bgKarplus.add(jrKarplusDefault);
        bgKarplus.add(jrKarplusThreshold);

        bgChou.add(jrChouDefault);
        bgChou.add(jrChouThreshold);

        jbRemoveProteome.setEnabled(false);

        listProteomes.setListData(vectorProteome);

        setColorScroll(jScrollPane1);
        setColorScroll(jScrollPane2);

        jtMakeblastdb.setText(Parameters.MAKEBLASTDB_PATH);
        jtBlastp.setText(Parameters.BLASTP_PATH);
        jtDestinationFolder.setText(Parameters.DESTINATION_FOLDER);
        loadProperties();
        adjustBlastSearchItems();
        pack();
        setTitle("Epibuilder - BCell Epitope Tool - Version 1.1");

    }

    public void setColorScroll(JScrollPane js) {
        js.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.ORANGE;
            }
        });

        js.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.ORANGE;
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgBepipred = new javax.swing.ButtonGroup();
        bgEmini = new javax.swing.ButtonGroup();
        bgParker = new javax.swing.ButtonGroup();
        bgChou = new javax.swing.ButtonGroup();
        bgKarplus = new javax.swing.ButtonGroup();
        bgKolaskar = new javax.swing.ButtonGroup();
        bgSearch = new javax.swing.ButtonGroup();
        tabbedPane = new javax.swing.JTabbedPane();
        jpMain = new javax.swing.JPanel();
        jpBepipred = new javax.swing.JPanel();
        jtBepPredFile = new javax.swing.JTextField();
        jbOpenFile = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jtBepipredThreshold = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jtBepiPredMinLength = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jtBepiPredMaxLength = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jrBepipredOnline = new javax.swing.JRadioButton();
        jrIEDBStd = new javax.swing.JRadioButton();
        jrBepipredJobid = new javax.swing.JRadioButton();
        jtBepiPredJobId = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jrEminiDefault = new javax.swing.JRadioButton();
        jrEminiThreshold = new javax.swing.JRadioButton();
        jtEminiThreshold = new javax.swing.JTextField();
        jcEmini = new javax.swing.JCheckBox();
        jlEminiThreshold = new javax.swing.JLabel();
        jrParkerDefault = new javax.swing.JRadioButton();
        jrParkerThreshold = new javax.swing.JRadioButton();
        jtParkerThreshold = new javax.swing.JTextField();
        jcParker = new javax.swing.JCheckBox();
        jlParkerThreshold = new javax.swing.JLabel();
        jrChouDefault = new javax.swing.JRadioButton();
        jrChouThreshold = new javax.swing.JRadioButton();
        jtChouThreshold = new javax.swing.JTextField();
        jcChou = new javax.swing.JCheckBox();
        jlChouThreshold = new javax.swing.JLabel();
        jrKarplusDefault = new javax.swing.JRadioButton();
        jrKarplusThreshold = new javax.swing.JRadioButton();
        jtKarplusThreshold = new javax.swing.JTextField();
        jcKarplus = new javax.swing.JCheckBox();
        jlKarplusThreshold = new javax.swing.JLabel();
        jrKolaskarDefault = new javax.swing.JRadioButton();
        jrKolaskarThreshold = new javax.swing.JRadioButton();
        jtKolaskarThreshold = new javax.swing.JTextField();
        jcKolaskar = new javax.swing.JCheckBox();
        jlKolaskarThreshold = new javax.swing.JLabel();
        jcSelectAll = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        jbAddProteome = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listProteomes = new javax.swing.JList<>();
        jbRemoveProteome = new javax.swing.JButton();
        jlMakeblastdb = new javax.swing.JLabel();
        jlBlastp = new javax.swing.JLabel();
        jtMakeblastdb = new javax.swing.JTextField();
        jtBlastp = new javax.swing.JTextField();
        jlBlastpIdentity = new javax.swing.JLabel();
        jtBlastpIdentity = new javax.swing.JTextField();
        jlBlastpCover = new javax.swing.JLabel();
        jtBlastpCover = new javax.swing.JTextField();
        jlBlastpWordsize = new javax.swing.JLabel();
        jtBlastpWordsize = new javax.swing.JTextField();
        jcBlastSearch = new javax.swing.JCheckBox();
        jLabel14 = new javax.swing.JLabel();
        jlBlastTask = new javax.swing.JLabel();
        jcbBlastTask = new javax.swing.JComboBox<>();
        jbTestBlastP = new javax.swing.JButton();
        jbTestMakeblastdb = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jcAppendAccession = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jtBasename = new javax.swing.JTextField();
        jbRun = new javax.swing.JButton();
        jbSaveConfig = new javax.swing.JButton();
        jbLoadConfig = new javax.swing.JButton();
        jtMessage = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jtDestinationFolder = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtFastabepipred = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jbSubmitBepipred2 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        JbCheckFasta = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jpResults = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtResults = new javax.swing.JTextArea();
        jbCopy = new javax.swing.JButton();
        jbSave = new javax.swing.JButton();
        jbClear = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jpBepipred.setBorder(javax.swing.BorderFactory.createTitledBorder("BepiPred"));

        jtBepPredFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtBepPredFileActionPerformed(evt);
            }
        });

        jbOpenFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/openfile.png"))); // NOI18N
        jbOpenFile.setText("Open File");
        jbOpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOpenFileActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("File");

        jtBepipredThreshold.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtBepipredThreshold.setText("0.6");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Threshold");

        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setText("Online prediction returns a csv file, command-line based returns a tsv file");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Min epitope length");

        jtBepiPredMinLength.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtBepiPredMinLength.setText("10");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Max epitope length");

        jtBepiPredMaxLength.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtBepiPredMaxLength.setText("30");

        jLabel6.setText("Input files");

        jrBepipredOnline.setText("csv file from BepiPred-2.0");
        jrBepipredOnline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrBepipredOnlineActionPerformed(evt);
            }
        });

        jrIEDBStd.setSelected(true);
        jrIEDBStd.setText("text file from IEDB BCell Standalone");
        jrIEDBStd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrIEDBStdActionPerformed(evt);
            }
        });

        jrBepipredJobid.setText("JobID from BepiPred-2.0 server");
        jrBepipredJobid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrBepipredJobidActionPerformed(evt);
            }
        });

        jtBepiPredJobId.setText("6057855000000FF4DE45CC45");
        jtBepiPredJobId.setEnabled(false);

        javax.swing.GroupLayout jpBepipredLayout = new javax.swing.GroupLayout(jpBepipred);
        jpBepipred.setLayout(jpBepipredLayout);
        jpBepipredLayout.setHorizontalGroup(
            jpBepipredLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpBepipredLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpBepipredLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpBepipredLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtBepPredFile, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbOpenFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpBepipredLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jpBepipredLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpBepipredLayout.createSequentialGroup()
                                .addGroup(jpBepipredLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jrIEDBStd)
                                    .addComponent(jrBepipredOnline))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jpBepipredLayout.createSequentialGroup()
                                .addComponent(jrBepipredJobid)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jtBepiPredJobId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpBepipredLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel3)
                        .addGroup(jpBepipredLayout.createSequentialGroup()
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jtBepipredThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jtBepiPredMinLength, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jtBepiPredMaxLength, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpBepipredLayout.setVerticalGroup(
            jpBepipredLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpBepipredLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpBepipredLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtBepPredFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbOpenFile))
                .addGap(1, 1, 1)
                .addGroup(jpBepipredLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(jrBepipredOnline))
                .addGap(3, 3, 3)
                .addComponent(jrIEDBStd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpBepipredLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jrBepipredJobid)
                    .addComponent(jtBepiPredJobId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpBepipredLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(jtBepipredThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jtBepiPredMinLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jtBepiPredMaxLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Softwares"));

        jrEminiDefault.setSelected(true);
        jrEminiDefault.setText("Default");
        jrEminiDefault.setToolTipText("Calculated by method");
        jrEminiDefault.setEnabled(false);
        jrEminiDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrEminiDefaultActionPerformed(evt);
            }
        });

        jrEminiThreshold.setEnabled(false);
        jrEminiThreshold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrEminiThresholdActionPerformed(evt);
            }
        });

        jtEminiThreshold.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtEminiThreshold.setText("0.0");
        jtEminiThreshold.setEnabled(false);

        jcEmini.setText("Emini Surface Accessibility Prediction");
        jcEmini.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcEminiActionPerformed(evt);
            }
        });

        jlEminiThreshold.setForeground(new java.awt.Color(153, 153, 153));
        jlEminiThreshold.setText("Threshold:");

        jrParkerDefault.setSelected(true);
        jrParkerDefault.setText("Default");
        jrParkerDefault.setToolTipText("Calculated by method");
        jrParkerDefault.setEnabled(false);
        jrParkerDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrParkerDefaultActionPerformed(evt);
            }
        });

        jrParkerThreshold.setEnabled(false);
        jrParkerThreshold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrParkerThresholdActionPerformed(evt);
            }
        });

        jtParkerThreshold.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtParkerThreshold.setText("0.0");
        jtParkerThreshold.setEnabled(false);

        jcParker.setText("Parker Hydrophilicity Prediction");
        jcParker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcParkerActionPerformed(evt);
            }
        });

        jlParkerThreshold.setForeground(new java.awt.Color(153, 153, 153));
        jlParkerThreshold.setText("Threshold:");

        jrChouDefault.setSelected(true);
        jrChouDefault.setText("Default");
        jrChouDefault.setToolTipText("Calculated by method");
        jrChouDefault.setEnabled(false);
        jrChouDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrChouDefaultActionPerformed(evt);
            }
        });

        jrChouThreshold.setEnabled(false);
        jrChouThreshold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrChouThresholdActionPerformed(evt);
            }
        });

        jtChouThreshold.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtChouThreshold.setText("0.0");
        jtChouThreshold.setEnabled(false);

        jcChou.setText("Chou & Fasman Beta-Turn Prediction");
        jcChou.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcChouActionPerformed(evt);
            }
        });

        jlChouThreshold.setForeground(new java.awt.Color(153, 153, 153));
        jlChouThreshold.setText("Threshold:");

        jrKarplusDefault.setSelected(true);
        jrKarplusDefault.setText("Default");
        jrKarplusDefault.setToolTipText("Calculated by method");
        jrKarplusDefault.setEnabled(false);
        jrKarplusDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrKarplusDefaultActionPerformed(evt);
            }
        });

        jrKarplusThreshold.setEnabled(false);
        jrKarplusThreshold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrKarplusThresholdActionPerformed(evt);
            }
        });

        jtKarplusThreshold.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtKarplusThreshold.setText("0.0");
        jtKarplusThreshold.setEnabled(false);

        jcKarplus.setText("Karplus & Schulz Flexibility Prediction");
        jcKarplus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcKarplusActionPerformed(evt);
            }
        });

        jlKarplusThreshold.setForeground(new java.awt.Color(153, 153, 153));
        jlKarplusThreshold.setText("Threshold:");

        jrKolaskarDefault.setSelected(true);
        jrKolaskarDefault.setText("Default");
        jrKolaskarDefault.setToolTipText("Calculated by method");
        jrKolaskarDefault.setEnabled(false);
        jrKolaskarDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrKolaskarDefaultActionPerformed(evt);
            }
        });

        jrKolaskarThreshold.setEnabled(false);
        jrKolaskarThreshold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrKolaskarThresholdActionPerformed(evt);
            }
        });

        jtKolaskarThreshold.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtKolaskarThreshold.setText("0.0");
        jtKolaskarThreshold.setEnabled(false);
        jtKolaskarThreshold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtKolaskarThresholdActionPerformed(evt);
            }
        });

        jcKolaskar.setText("Kolaskar & Tongaonkar Antigenicity");
        jcKolaskar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcKolaskarActionPerformed(evt);
            }
        });

        jlKolaskarThreshold.setForeground(new java.awt.Color(153, 153, 153));
        jlKolaskarThreshold.setText("Threshold:");

        jcSelectAll.setText("Select all methods");
        jcSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcSelectAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcKolaskar)
                            .addComponent(jcParker)
                            .addComponent(jcEmini)
                            .addComponent(jcChou)
                            .addComponent(jcKarplus))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jlEminiThreshold)
                            .addComponent(jlParkerThreshold)
                            .addComponent(jlChouThreshold)
                            .addComponent(jlKarplusThreshold)
                            .addComponent(jlKolaskarThreshold))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jrEminiDefault)
                            .addComponent(jrParkerDefault)
                            .addComponent(jrChouDefault)
                            .addComponent(jrKarplusDefault)
                            .addComponent(jrKolaskarDefault))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jrEminiThreshold)
                            .addComponent(jrParkerThreshold)
                            .addComponent(jrChouThreshold)
                            .addComponent(jrKarplusThreshold)
                            .addComponent(jrKolaskarThreshold))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jtKarplusThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtKolaskarThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtParkerThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtEminiThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtChouThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jcSelectAll))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jcSelectAll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jcEmini)
                    .addComponent(jlEminiThreshold)
                    .addComponent(jrEminiDefault)
                    .addComponent(jrEminiThreshold)
                    .addComponent(jtEminiThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jrParkerDefault)
                    .addComponent(jrParkerThreshold)
                    .addComponent(jtParkerThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcParker)
                    .addComponent(jlParkerThreshold))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jrChouDefault)
                    .addComponent(jrChouThreshold)
                    .addComponent(jtChouThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcChou)
                    .addComponent(jlChouThreshold))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jrKarplusDefault)
                    .addComponent(jrKarplusThreshold)
                    .addComponent(jtKarplusThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcKarplus)
                    .addComponent(jlKarplusThreshold))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jrKolaskarDefault)
                    .addComponent(jrKolaskarThreshold)
                    .addComponent(jtKolaskarThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcKolaskar)
                    .addComponent(jlKolaskarThreshold)))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Epitope Search"));

        jbAddProteome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/plusfile.png"))); // NOI18N
        jbAddProteome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddProteomeActionPerformed(evt);
            }
        });

        jLabel11.setForeground(new java.awt.Color(153, 153, 153));
        jLabel11.setText("Add a set of a protein fasta file to search for generated epitopes");

        listProteomes.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listProteomesValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(listProteomes);

        jbRemoveProteome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/deletefile.png"))); // NOI18N
        jbRemoveProteome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRemoveProteomeActionPerformed(evt);
            }
        });

        jlMakeblastdb.setText("MakeblastDB Path");

        jlBlastp.setText("BlastP Path");

        jtMakeblastdb.setEnabled(false);
        jtMakeblastdb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtMakeblastdbFocusLost(evt);
            }
        });

        jtBlastp.setEnabled(false);
        jtBlastp.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtBlastpFocusLost(evt);
            }
        });

        jlBlastpIdentity.setText("Identity");

        jtBlastpIdentity.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtBlastpIdentity.setText("90");
        jtBlastpIdentity.setEnabled(false);

        jlBlastpCover.setText("Cover");

        jtBlastpCover.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtBlastpCover.setText("90");
        jtBlastpCover.setEnabled(false);
        jtBlastpCover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtBlastpCoverActionPerformed(evt);
            }
        });

        jlBlastpWordsize.setText("Word-size");

        jtBlastpWordsize.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtBlastpWordsize.setText("4");
        jtBlastpWordsize.setEnabled(false);

        jcBlastSearch.setText("BlastP Search");
        jcBlastSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcBlastSearchActionPerformed(evt);
            }
        });

        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setText("Choose this option if you want to add a blast search for epitopes in proteomes");

        jlBlastTask.setText("Task");

        jcbBlastTask.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "blastp-short", "blastp-fast", "blastp" }));
        jcbBlastTask.setEnabled(false);

        jbTestBlastP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/press.png"))); // NOI18N
        jbTestBlastP.setText("Test");
        jbTestBlastP.setPreferredSize(new java.awt.Dimension(115, 36));
        jbTestBlastP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbTestBlastPActionPerformed(evt);
            }
        });

        jbTestMakeblastdb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/press.png"))); // NOI18N
        jbTestMakeblastdb.setText("Test");
        jbTestMakeblastdb.setPreferredSize(new java.awt.Dimension(115, 36));
        jbTestMakeblastdb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbTestMakeblastdbActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Options"));

        jcAppendAccession.setSelected(true);
        jcAppendAccession.setText("Show accession ID");
        jcAppendAccession.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcAppendAccessionActionPerformed(evt);
            }
        });

        jLabel10.setText("on search result files");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel10))
                    .addComponent(jcAppendAccession))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jcAppendAccession)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jlMakeblastdb)
                            .addComponent(jlBlastp))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jtBlastp, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                            .addComponent(jtMakeblastdb))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbTestMakeblastdb, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbTestBlastP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(222, 222, 222))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jcBlastSearch)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jbAddProteome)
                                            .addComponent(jbRemoveProteome))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(jlBlastpIdentity)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtBlastpIdentity, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlBlastpCover)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtBlastpCover, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlBlastpWordsize)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtBlastpWordsize, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlBlastTask)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbBlastTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jbAddProteome)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbRemoveProteome))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcBlastSearch)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jlMakeblastdb)
                    .addComponent(jtMakeblastdb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbTestMakeblastdb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jlBlastp)
                    .addComponent(jtBlastp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbTestBlastP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jlBlastpIdentity)
                    .addComponent(jtBlastpIdentity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlBlastpCover)
                    .addComponent(jtBlastpCover, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlBlastpWordsize)
                    .addComponent(jtBlastpWordsize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlBlastTask)
                    .addComponent(jcbBlastTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Results"));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Basename");

        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setText("Provide a name for this run (if this field is empty a random basename will be generated)");

        jbRun.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jbRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/search.png"))); // NOI18N
        jbRun.setText("Run!");
        jbRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRunActionPerformed(evt);
            }
        });

        jbSaveConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/save.png"))); // NOI18N
        jbSaveConfig.setText("Save Configuration");
        jbSaveConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSaveConfigActionPerformed(evt);
            }
        });

        jbLoadConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/openfile.png"))); // NOI18N
        jbLoadConfig.setText("Load Configuration");
        jbLoadConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbLoadConfigActionPerformed(evt);
            }
        });

        jtMessage.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jtMessage.setForeground(new java.awt.Color(0, 51, 255));

        jLabel7.setText("Destination folder");

        jtDestinationFolder.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtDestinationFolderFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jtMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(6, 6, 6))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jbSaveConfig)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jbLoadConfig)
                                .addGap(18, 18, 18)
                                .addComponent(jbRun, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(161, 161, 161))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jtBasename, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(363, 363, 363))
                                    .addComponent(jtDestinationFolder)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jLabel9)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel7)
                    .addComponent(jtDestinationFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel8)
                    .addComponent(jtBasename, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jbSaveConfig)
                    .addComponent(jbLoadConfig)
                    .addComponent(jbRun))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jpMainLayout = new javax.swing.GroupLayout(jpMain);
        jpMain.setLayout(jpMainLayout);
        jpMainLayout.setHorizontalGroup(
            jpMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpBepipred, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jpMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 648, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpMainLayout.setVerticalGroup(
            jpMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpMainLayout.createSequentialGroup()
                        .addComponent(jpBepipred, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpMainLayout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(437, 437, 437))
        );

        tabbedPane.addTab("Main", jpMain);

        jLabel12.setText("<html>Here you can perform BepiPred-2.0 search from a webserver request. This request will generate an <b>JOBID</b> that you can monitoring in the <b>Main Tab</b>  or even in BepiPred-2.0 website.<html>");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Fasta"));

        jtFastabepipred.setColumns(20);
        jtFastabepipred.setFont(new java.awt.Font("Courier New", 0, 13)); // NOI18N
        jtFastabepipred.setRows(5);
        jScrollPane3.setViewportView(jtFastabepipred);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/openfile.png"))); // NOI18N
        jButton2.setText("Open File");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel16.setText("At most 50 sequences and 300,000 amino acids per submission; each sequence not less than 10 and not more than 6000 amino acids.  ");

        jLabel15.setText("<html>Your request will be processed on server <a href='http://www.cbs.dtu.dk/services/BepiPred/'>http://www.cbs.dtu.dk/services/BepiPred/</a> under follow restrictions:</html>");

        jbSubmitBepipred2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/search.png"))); // NOI18N
        jbSubmitBepipred2.setText("Submit");
        jbSubmitBepipred2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSubmitBepipred2ActionPerformed(evt);
            }
        });

        jLabel18.setForeground(new java.awt.Color(153, 153, 153));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Open or paste your FASTA");

        JbCheckFasta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ok.png"))); // NOI18N
        JbCheckFasta.setText("Check your FASTA");
        JbCheckFasta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JbCheckFastaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 931, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jLabel16)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(JbCheckFasta))
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbSubmitBepipred2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jButton2)
                    .addComponent(JbCheckFasta, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jbSubmitBepipred2)
                .addGap(31, 31, 31))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {JbCheckFasta, jButton2});

        jLabel13.setText("<html><i>Notes: Before the submition  we remove the follow characteres: <b>*, U or X</b></i></html>");

        jLabel17.setText("<html><i>You must have Firefox installed in your system.</id></html>");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel12)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 1132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jLabel17)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Online submission", jPanel1);

        jpResults.setBorder(javax.swing.BorderFactory.createTitledBorder("Results"));

        jtResults.setColumns(20);
        jtResults.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jtResults.setRows(5);
        jScrollPane1.setViewportView(jtResults);

        jbCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copy.png"))); // NOI18N
        jbCopy.setText("Copy to clipboard");
        jbCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCopyActionPerformed(evt);
            }
        });

        jbSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/save.png"))); // NOI18N
        jbSave.setText("Save as file");
        jbSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSaveActionPerformed(evt);
            }
        });

        jbClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/clear.png"))); // NOI18N
        jbClear.setText("Clear");
        jbClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbClearActionPerformed(evt);
            }
        });

        jButton1.setText("+");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("-");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpResultsLayout = new javax.swing.GroupLayout(jpResults);
        jpResults.setLayout(jpResultsLayout);
        jpResultsLayout.setHorizontalGroup(
            jpResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpResultsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jpResultsLayout.createSequentialGroup()
                        .addComponent(jbCopy)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbClear)
                        .addGap(113, 113, 113)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 676, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jpResultsLayout.setVerticalGroup(
            jpResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpResultsLayout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addGap(24, 24, 24)
                .addGroup(jpResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbCopy)
                    .addComponent(jbSave)
                    .addComponent(jbClear)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        tabbedPane.addTab("Results", jpResults);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 12, Short.MAX_VALUE)
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1279, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jtResults.setFont(new java.awt.Font("Courier New", 1, --fontSize));
        jtResults.updateUI();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jtResults.setFont(new java.awt.Font("Courier New", 1, ++fontSize));
        jtResults.updateUI();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jbClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbClearActionPerformed
        jtResults.setText("");
    }//GEN-LAST:event_jbClearActionPerformed

    private void jbSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveActionPerformed
        JFileChooser fileChooser = new JFileChooser(lastFile);
        fileChooser.setDialogTitle("Specify a file to save the content");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                FileWriter fw = new FileWriter(fileToSave);
                fw.append(jtResults.getText());
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jbSaveActionPerformed

    private void jbCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCopyActionPerformed
        String selection = jtResults.getText();
        if (selection == null) {
            return;
        }
        StringSelection clipString = new StringSelection(selection);
        clipboard.setContents(clipString, clipString);
    }//GEN-LAST:event_jbCopyActionPerformed

    private void jtDestinationFolderFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtDestinationFolderFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jtDestinationFolderFocusLost

    private void jbLoadConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbLoadConfigActionPerformed
        String dir = ".";
        if (lastFile != null) {
            dir = lastFile.getParent();
        }
        JFileChooser jf = new JFileChooser(dir);
        int res = jf.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            lastFile = jf.getSelectedFile();
            jtBepPredFile.setText(lastFile.getAbsolutePath());
            try {
                Properties config = new Properties();
                config.load(new FileReader(lastFile));
                setConfig(config);
                showMessage("Configuration load from file: " + lastFile.getAbsolutePath());
            } catch (Exception ex) {
                showError("Load configuration error: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_jbLoadConfigActionPerformed

    private void jbSaveConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveConfigActionPerformed
        JFileChooser fileChooser = new JFileChooser(lastFile);
        fileChooser.setDialogTitle("Specify a file to save the content");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                FileWriter fw = new FileWriter(fileToSave);

                Properties config = getConfig();
                config.store(fw, "Epibuilder configuration");

                fw.close();
                showMessage("Configuration saved: " + fileToSave.getAbsolutePath());
            } catch (IOException ex) {
                showError("Save configuration error: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_jbSaveConfigActionPerformed

    private void jbRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRunActionPerformed
        String start = Calendar.getInstance().getTime().toString();
        jtResults.setText("");
        if (jcBlastSearch.isSelected()) {
            boolean testMakeblastdb = testMakeblastDb();
            boolean testBlastp = testBlastp();
            boolean run = true;
            if (!testMakeblastdb) {
                showError("Command " + jtMakeblastdb.getText() + " not found.");
                jtResults.setText("Command " + jtMakeblastdb.getText() + " not found.\nCheck the full path of your makeblastdb command or deselect the Blastp search option.\n");
                run = false;
            }
            if (!testBlastp) {
                showError("Command " + jtBlastp.getText() + " not found.");
                jtResults.setText(jtResults.getText() + "Command " + jtMakeblastdb.getText() + " not found.\nCheck the full path of your blastp command or deselect the Blastp search option.");
                run = false;
            }
            tabbedPane.setSelectedComponent(jpResults);
            if (!run) {
                return;
            }

        }
        Parameters.OUTPUT_FILE = false;
        Parameters.MAP_SOFTWARES = new LinkedHashMap<>();
        Parameters.BEPIPRED_FILE = new File(jtBepPredFile.getText());

        if (jrBepipredOnline.isSelected()) {
            Parameters.BEPIPRED_INPUT = Parameters.BEPIPRED_TYPE.ONLINE;
        } else if (jrIEDBStd.isSelected()) {
            Parameters.BEPIPRED_INPUT = Parameters.BEPIPRED_TYPE.BCELL_STANDALONE;
        } else if (jrBepipredJobid.isSelected()) {
            Parameters.BEPIPRED_INPUT = Parameters.BEPIPRED_TYPE.JOB_ID;
            Parameters.BEPIPRED2_JOBID = jtBepiPredJobId.getText();
        }

        Parameters.THRESHOLD_BEPIPRED = Double.parseDouble(jtBepipredThreshold.getText());
        Parameters.MIN_LENGTH_BEPIPRED2 = Integer.parseInt(jtBepiPredMinLength.getText());
        Parameters.MAX_LENGTH_BEPIPRED2 = Integer.parseInt(jtBepiPredMaxLength.getText());
        if (Parameters.MIN_LENGTH_BEPIPRED2 >= Parameters.MAX_LENGTH_BEPIPRED2) {
            showError("Min epitope length greater than Max epitope length.");
            jtBepiPredMinLength.requestFocus();
            jtBepiPredMinLength.selectAll();
            return;
        }

        Parameters.BASENAME = jtBasename.getText();
        if (Parameters.BASENAME.trim().equals("")) {
            String base = lastFile.getName();
            if (base.contains(".")) {
                jtBasename.setText(base.substring(0, base.lastIndexOf('.')));
            } else {
                jtBasename.setText(base);
            }
        }
        Parameters.DESTINATION_FOLDER = jtDestinationFolder.getText();
        Parameters.BASENAME = jtBasename.getText();

        Parameters.PROTEOMES = new ArrayList<>(vectorProteome);

        addSoftware(SoftwareBcellEnum.EMINI, jcEmini, jrEminiThreshold, jtEminiThreshold);
        addSoftware(SoftwareBcellEnum.PARKER, jcParker, jrParkerThreshold, jtParkerThreshold);
        addSoftware(SoftwareBcellEnum.CHOU_FOSMAN, jcChou, jrChouThreshold, jtChouThreshold);
        addSoftware(SoftwareBcellEnum.KARPLUS_SCHULZ, jcKarplus, jrKarplusThreshold, jtKarplusThreshold);
        addSoftware(SoftwareBcellEnum.KOLASKAR, jcKolaskar, jrKolaskarThreshold, jtKolaskarThreshold);

        /*for (Proteome proteome : Parameters.PROTEOMES) {
            try {
                proteome.load();
            } catch (Exception ex) {
                showError("Error: " + ex.getMessage());
            }
        }*/
        //Search options
        Parameters.SEARCH_BLAST = jcBlastSearch.isSelected();
        Parameters.BLASTP_PATH = jtBlastp.getText();
        Parameters.MAKEBLASTDB_PATH = jtMakeblastdb.getText();
        Parameters.BLAST_IDENTITY = Double.parseDouble(jtBlastpIdentity.getText());
        Parameters.BLAST_COVER = Double.parseDouble(jtBlastpCover.getText());
        Parameters.BLAST_WORD_SIZE = Integer.parseInt(jtBlastpWordsize.getText());
        Parameters.BLAST_TASK = jcbBlastTask.getSelectedItem() + "";
        
        Parameters.HIT_ACCESSION=jcAppendAccession.isSelected();
        try {
            jtResults.setText("");
            String res = EpitopeFinder.process();
            StringBuilder sb = new StringBuilder(res);
            sb.append("\n\nStart: " + start);
            sb.append("\nEnd: " + Calendar.getInstance().getTime());
            jtResults.setText(sb.toString());

            tabbedPane.setSelectedComponent(jpResults);

            showMessage("Success!");
        } catch (Exception ex) {
            showMessage("Running error: " + ex.getMessage());
            jtResults.setText(ex.getMessage());
        }
    }//GEN-LAST:event_jbRunActionPerformed

    private void jbTestMakeblastdbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbTestMakeblastdbActionPerformed
        testMakeblastDb();
    }//GEN-LAST:event_jbTestMakeblastdbActionPerformed

    private void jbTestBlastPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbTestBlastPActionPerformed
        testBlastp();
    }//GEN-LAST:event_jbTestBlastPActionPerformed

    private void jcBlastSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcBlastSearchActionPerformed
        adjustBlastSearchItems();
    }//GEN-LAST:event_jcBlastSearchActionPerformed

    private void jtBlastpCoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtBlastpCoverActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBlastpCoverActionPerformed

    private void jtBlastpFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtBlastpFocusLost
        testBlastp();
    }//GEN-LAST:event_jtBlastpFocusLost

    private void jtMakeblastdbFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtMakeblastdbFocusLost
        testMakeblastDb();
    }//GEN-LAST:event_jtMakeblastdbFocusLost

    private void jbRemoveProteomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRemoveProteomeActionPerformed
        Proteome item = listProteomes.getSelectedValue();
        if (item != null) {
            vectorProteome.removeElement(item);
            listProteomes.updateUI();
            if (vectorProteome.isEmpty()) {
                jbRemoveProteome.setEnabled(false);
            }
            updateList();
        }
    }//GEN-LAST:event_jbRemoveProteomeActionPerformed

    private void listProteomesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listProteomesValueChanged
        int item = listProteomes.getSelectedIndex();
        if (item >= 0) {
            jbRemoveProteome.setEnabled(true);
        } else {
            jbRemoveProteome.setEnabled(false);
        }
    }//GEN-LAST:event_listProteomesValueChanged

    private void jbAddProteomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddProteomeActionPerformed
        String proteome = JOptionPane.showInputDialog(this, "Please, inform the name of proteome", "Proteome", JOptionPane.INFORMATION_MESSAGE);

        if (proteome.trim().length() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Empty name", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            String dir = ".";
            if (lastFile != null) {
                dir = lastFile.getParent();
            }
            JFileChooser jf = new JFileChooser(dir);
            int res = jf.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                lastFile = jf.getSelectedFile();

                Proteome item = new Proteome(proteome, lastFile);
                vectorProteome.addElement(item);
                updateList();
                saveProperties();
            }
        }
    }//GEN-LAST:event_jbAddProteomeActionPerformed

    private void jrIEDBStdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrIEDBStdActionPerformed
        jtBepPredFile.setEnabled(true);
        jbOpenFile.setEnabled(true);
        jtBepiPredJobId.setEnabled(false);

    }//GEN-LAST:event_jrIEDBStdActionPerformed

    private void jbOpenFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOpenFileActionPerformed
        String dir = ".";
        if (lastFile != null) {
            dir = lastFile.getParent();
        }
        JFileChooser jf = new JFileChooser(dir);
        int res = jf.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            lastFile = jf.getSelectedFile();
            String base = lastFile.getName();
            if (base.contains(".")) {
                jtBasename.setText(base.substring(0, base.lastIndexOf('.')));
            } else {
                jtBasename.setText(base);
            }
            jtDestinationFolder.setText(lastFile.getParent());
            jtBepPredFile.setText(lastFile.getAbsolutePath());
        }
        saveProperties();
    }//GEN-LAST:event_jbOpenFileActionPerformed

    private void jtBepPredFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtBepPredFileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBepPredFileActionPerformed

    private void jcSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcSelectAllActionPerformed
        jcEmini.setSelected(jcSelectAll.isSelected());
        jcParker.setSelected(jcSelectAll.isSelected());
        jcChou.setSelected(jcSelectAll.isSelected());
        jcKarplus.setSelected(jcSelectAll.isSelected());
        jcKolaskar.setSelected(jcSelectAll.isSelected());
        jcEminiActionPerformed(evt);
        jcParkerActionPerformed(evt);
        jcChouActionPerformed(evt);
        jcKarplusActionPerformed(evt);
        jcKolaskarActionPerformed(evt);
    }//GEN-LAST:event_jcSelectAllActionPerformed

    private void jcKolaskarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcKolaskarActionPerformed
        activateSoftware(jcKolaskar, jlKolaskarThreshold, jrKolaskarDefault, jrKolaskarThreshold, jtKolaskarThreshold);
    }//GEN-LAST:event_jcKolaskarActionPerformed

    private void jtKolaskarThresholdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtKolaskarThresholdActionPerformed
        activateSoftware(jcKolaskar, jlKolaskarThreshold, jrKolaskarDefault, jrKolaskarThreshold, jtKolaskarThreshold);
    }//GEN-LAST:event_jtKolaskarThresholdActionPerformed

    private void jrKolaskarDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrKolaskarDefaultActionPerformed
        selectSoftwareDefault(jrKolaskarDefault, jtKolaskarThreshold);

    }//GEN-LAST:event_jrKolaskarDefaultActionPerformed

    private void jcKarplusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcKarplusActionPerformed
        activateSoftware(jcKarplus, jlKarplusThreshold, jrKarplusDefault, jrKarplusThreshold, jtKarplusThreshold);
    }//GEN-LAST:event_jcKarplusActionPerformed

    private void jrKarplusDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrKarplusDefaultActionPerformed
        selectSoftwareDefault(jrKarplusDefault, jtKarplusThreshold);

    }//GEN-LAST:event_jrKarplusDefaultActionPerformed

    private void jcChouActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcChouActionPerformed
        activateSoftware(jcChou, jlChouThreshold, jrChouDefault, jrChouThreshold, jtChouThreshold);
    }//GEN-LAST:event_jcChouActionPerformed

    private void jrChouDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrChouDefaultActionPerformed
        selectSoftwareDefault(jrChouDefault, jtChouThreshold);
    }//GEN-LAST:event_jrChouDefaultActionPerformed

    private void jcParkerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcParkerActionPerformed
        activateSoftware(jcParker, jlParkerThreshold, jrParkerDefault, jrParkerThreshold, jtParkerThreshold);
    }//GEN-LAST:event_jcParkerActionPerformed

    private void jrParkerDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrParkerDefaultActionPerformed
        selectSoftwareDefault(jrParkerDefault, jtParkerThreshold);
    }//GEN-LAST:event_jrParkerDefaultActionPerformed

    private void jcEminiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcEminiActionPerformed
        activateSoftware(jcEmini, jlEminiThreshold, jrEminiDefault, jrEminiThreshold, jtEminiThreshold);
    }//GEN-LAST:event_jcEminiActionPerformed

    private void jrEminiDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrEminiDefaultActionPerformed
        selectSoftwareDefault(jrEminiDefault, jtEminiThreshold);
    }//GEN-LAST:event_jrEminiDefaultActionPerformed

    private void selectSoftwareDefault(JRadioButton rb, JTextField value) {
        value.setEnabled(!rb.isSelected());
    }
    private void jbSubmitBepipred2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSubmitBepipred2ActionPerformed

        String fasta = jtFastabepipred.getText();
        String[] res = fasta.split("\n");
        StringBuilder finalFasta = new StringBuilder();
        for (int i = 0; i < res.length; i++) {
            if (!res[i].startsWith(">")) {
                res[i] = res[i].replace("[*]", "").replace("U", "").replace("X", "");
            }
            finalFasta.append(res[i]);
            finalFasta.append("\n");
        }

        String jobid = BepiPredOnlineRunner.submitBepipred(finalFasta.toString());

        JOptionPane.showMessageDialog(this, "Your FASTA was submitted to BepiPred-2.0 with job id " + jobid + ".\nWe will check your job every 20 seconds.", "Success", JOptionPane.INFORMATION_MESSAGE);
        jtBepiPredJobId.setText(jobid);
        tabbedPane.setSelectedComponent(jpMain);
        jtBepiPredJobId.requestFocus();
        jrBepipredJobid.setSelected(true);
        jrBepipredJobidActionPerformed(evt);
        Parameters.BEPIPRED2_JOBID = jobid;
        Parameters.BEPIPRED_INPUT = Parameters.BEPIPRED_TYPE.JOB_ID;
        saveProperties();
        waiterBepipredOnline();
    }//GEN-LAST:event_jbSubmitBepipred2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String dir = ".";
        if (lastFile != null) {
            dir = lastFile.getParent();
        }
        JFileChooser jf = new JFileChooser(dir);
        int res = jf.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            lastFile = jf.getSelectedFile();
            String arq;
            try {
                arq = FileHelper.readFile(lastFile);
                jtFastabepipred.setText(arq);

            } catch (FileNotFoundException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        saveProperties();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void JbCheckFastaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JbCheckFastaActionPerformed
        FileWriter fw = null;
        try {
            String src = Calendar.getInstance().getTime() + "-fasta-bepipred.fasta";
            File file = new File(src);
            fw = new FileWriter(file);
            fw.append(jtFastabepipred.getText());
            fw.close();
            Proteome temp = new Proteome("bepipred", file);
            temp.load();
            ArrayList<ProteinConverter> proteins = temp.getProteins();
            int totalAA = 0;
            StringBuilder errors = new StringBuilder();
            errors.append("Identified errors:\n");
            boolean hasError = false;
            if (proteins.isEmpty()) {
                errors.append("\n0 protein identified\n");
                hasError = true;
            }

            if (proteins.size() > 50) {
                errors.append("More than 50 proteins - " + proteins.size() + " identified proteins\n");
                hasError = true;
            }

            StringBuilder size = new StringBuilder();
            for (ProteinConverter protein : proteins) {
                int length = protein.getSequence().length();
                totalAA += length;
                if (length > 6000 || length < 10) {
                    hasError = true;
                    size.append("Protein " + protein.getId() + " has " + length + " amino acids\n");
                }
            }

            if (totalAA >= 300000) {
                hasError = true;
                errors.append("More than 300.000 amino acids\n");
            }
            errors.append("\t" + size.toString());
            if (hasError) {
                tabbedPane.setSelectedComponent(jpResults);
                jtResults.setText(errors.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Your FASTA is ready to be submitted", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            jtFastabepipred.setText(ex.getMessage());
            ex.printStackTrace();
        }

    }//GEN-LAST:event_JbCheckFastaActionPerformed

    private void jrBepipredJobidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrBepipredJobidActionPerformed
        jtBepPredFile.setEnabled(false);
        jbOpenFile.setEnabled(false);
        jtBepiPredJobId.setEnabled(true);
    }//GEN-LAST:event_jrBepipredJobidActionPerformed

    private void jrBepipredOnlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrBepipredOnlineActionPerformed
        jtBepPredFile.setEnabled(true);
        jbOpenFile.setEnabled(true);
        jtBepiPredJobId.setEnabled(false);
    }//GEN-LAST:event_jrBepipredOnlineActionPerformed

    private void jrEminiThresholdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrEminiThresholdActionPerformed
        selectSoftwareDefault(jrEminiDefault, jtEminiThreshold);
    }//GEN-LAST:event_jrEminiThresholdActionPerformed

    private void jrParkerThresholdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrParkerThresholdActionPerformed
        selectSoftwareDefault(jrParkerDefault, jtParkerThreshold);
    }//GEN-LAST:event_jrParkerThresholdActionPerformed

    private void jrChouThresholdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrChouThresholdActionPerformed
        selectSoftwareDefault(jrChouDefault, jtChouThreshold);
    }//GEN-LAST:event_jrChouThresholdActionPerformed

    private void jrKarplusThresholdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrKarplusThresholdActionPerformed
        selectSoftwareDefault(jrKarplusDefault, jtKarplusThreshold);
    }//GEN-LAST:event_jrKarplusThresholdActionPerformed

    private void jrKolaskarThresholdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrKolaskarThresholdActionPerformed
        selectSoftwareDefault(jrKolaskarDefault, jtKolaskarThreshold);
    }//GEN-LAST:event_jrKolaskarThresholdActionPerformed

    private void jcAppendAccessionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcAppendAccessionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcAppendAccessionActionPerformed

    public void updateList() {
        listProteomes.updateUI();
    }

    private void addSoftware(SoftwareBcellEnum softenum, JCheckBox software, JRadioButton jrThreshold, JTextField jtThreshold) {
        if (software.isSelected()) {
            Double threshold = null;
            if (jrThreshold.isSelected()) {
                threshold = Double.parseDouble(jtThreshold.getText());
            }
            Parameters.MAP_SOFTWARES.put(softenum, threshold);
        }
    }

    private void showError(String msg) {
        if (msg.length() > 75) {
            msg = msg.substring(0, 75);
        }
        jtMessage.setForeground(new java.awt.Color(255, 0, 0));
        jtMessage.setText(msg + "...");
        jtMessage.setToolTipText(msg);
    }

    private void showMessage(String msg) {
        if (msg.length() > 75) {
            msg = msg.substring(0, 75);
        }
        jtMessage.setForeground(new java.awt.Color(0, 0, 255));
        jtMessage.setText(msg + "...");
        jtMessage.setToolTipText(msg);
    }

    public void adjustBlastSearchItems() {
        activateComponent(jcBlastSearch, jtMakeblastdb, jlMakeblastdb);
        activateComponent(jcBlastSearch, jtBlastp, jlBlastp);
        activateComponent(jcBlastSearch, jtBlastpIdentity, jlBlastpIdentity);
        activateComponent(jcBlastSearch, jtBlastpCover, jlBlastpCover);
        activateComponent(jcBlastSearch, jtBlastpWordsize, jlBlastpWordsize);
        activateComponent(jcBlastSearch, jcbBlastTask, jlBlastTask);
        jbTestBlastP.setEnabled(jcBlastSearch.isSelected());
        jbTestMakeblastdb.setEnabled(jcBlastSearch.isSelected());
    }

    public void activateComponent(JCheckBox radio, JComponent txt, JLabel lbl) {
        txt.setEnabled(radio.isSelected());
        if (radio.isSelected()) {
            txt.setForeground(Color.BLACK);
            lbl.setForeground(Color.BLACK);
        } else {
            txt.setForeground(new java.awt.Color(153, 153, 153));
            lbl.setForeground(new java.awt.Color(153, 153, 153));
        }
    }

    public Properties getConfig() {
        Properties p = new Properties();
        p.put("bepipred-file", jtBepPredFile.getText());
        p.put("bepipred-online", jrBepipredOnline.isSelected() + "");
        p.put("bepipred-iedb", jrIEDBStd.isSelected() + "");
        p.put("bepipred-threshold", jtBepipredThreshold.getText());
        p.put("bepipred-min-length", jtBepiPredMinLength.getText());
        p.put("bepipred-max-length", jtBepiPredMaxLength.getText());

        p.put("emini", jcEmini.isSelected() + "");
        p.put("emini-default", jrEminiDefault.isSelected() + "");
        p.put("emini-threshold", jrEminiThreshold.isSelected() + "");
        p.put("emini-threshold-value", jtEminiThreshold.getText());

        p.put("parker", jcParker.isSelected() + "");
        p.put("parker-default", jrParkerDefault.isSelected() + "");
        p.put("parker-threshold", jrParkerThreshold.isSelected() + "");
        p.put("parker-threshold-value", jtParkerThreshold.getText());

        p.put("chou", jcChou.isSelected() + "");
        p.put("chou-default", jrChouDefault.isSelected() + "");
        p.put("chou-threshold", jrChouThreshold.isSelected() + "");
        p.put("chou-threshold-value", jtChouThreshold.getText());

        p.put("karplus", jcKarplus.isSelected() + "");
        p.put("karplus-default", jrKarplusDefault.isSelected() + "");
        p.put("karplus-threshold", jrKarplusThreshold.isSelected() + "");
        p.put("karplus-threshold-value", jtKarplusThreshold.getText());

        p.put("kolaskar", jcKolaskar.isSelected() + "");
        p.put("kolaskar-default", jrKolaskarDefault.isSelected() + "");
        p.put("kolaskar-threshold", jrKolaskarThreshold.isSelected() + "");
        p.put("kolaskar-threshold-value", jtKolaskarThreshold.getText());

        String proteomes = "";
        for (Proteome proteome : vectorProteome) {
            proteomes += proteome + ";";
        }
        p.put("proteomes", proteomes);
        p.put("basename", jtBasename.getText());
        p.put("destination-folder", jtDestinationFolder.getText());
        //Blast Search
        p.put("search-blast", jcBlastSearch.isSelected() + "");
        p.put("search-blast-makeblastdb", jtMakeblastdb.getText());
        p.put("search-blast-blastp", jtBlastp.getText());
        p.put("search-blast-identity", jtBlastpIdentity.getText());
        p.put("search-blast-cover", jtBlastpCover.getText());
        p.put("search-blast-wordsize", jtBlastpWordsize.getText());
        p.put("search-blast-task-blastpsort", jcbBlastTask.getSelectedItem() + "");
        p.put("hit-accession", jcAppendAccession.isSelected()+"");
        return p;
    }

    private void setItemList(Properties p, JComboBox t, String key) {
        t.setSelectedItem((String) p.getProperty(key));
    }

    private void setText(Properties p, JTextField t, String key) {
        t.setText((String) p.getProperty(key));
    }

    private void setBoolean(Properties p, JToggleButton t, String key) {
        t.setSelected(Boolean.parseBoolean((String) p.getProperty(key)));
    }

    public void setConfig(Properties p) {
        setText(p, jtBepPredFile, "bepipred-file");
        setBoolean(p, jrBepipredOnline, "bepipred-online");
        setBoolean(p, jrIEDBStd, "bepipred-iedb");
        setText(p, jtBepipredThreshold, "bepipred-threshold");
        setText(p, jtBepiPredMinLength, "bepipred-min-length");
        setText(p, jtBepiPredMaxLength, "bepipred-max-length");

        setBoolean(p, jcEmini, "emini");
        setBoolean(p, jrEminiDefault, "emini-default");
        setBoolean(p, jrEminiThreshold, "emini-threshold");
        setText(p, jtEminiThreshold, "emini-threshold-value");
        jcEminiActionPerformed(null);

        setBoolean(p, jcParker, "parker");
        setBoolean(p, jrParkerDefault, "parker-default");
        setBoolean(p, jrParkerThreshold, "parker-threshold");
        setText(p, jtParkerThreshold, "parker-threshold-value");
        jcParkerActionPerformed(null);

        setBoolean(p, jcChou, "chou");
        setBoolean(p, jrChouDefault, "chou-default");
        setBoolean(p, jrChouThreshold, "chou-threshold");
        setText(p, jtChouThreshold, "chou-threshold-value");
        jcChouActionPerformed(null);

        setBoolean(p, jcKarplus, "karplus");
        setBoolean(p, jrKarplusDefault, "karplus-default");
        setBoolean(p, jrKarplusThreshold, "karplus-threshold");
        setText(p, jtKarplusThreshold, "karplus-threshold-value");
        jcKarplusActionPerformed(null);

        setBoolean(p, jcKolaskar, "kolaskar");
        setBoolean(p, jrKolaskarDefault, "kolaskar-default");
        setBoolean(p, jrKolaskarThreshold, "kolaskar-threshold");
        setText(p, jtKolaskarThreshold, "kolaskar-threshold-value");
        jcKolaskarActionPerformed(null);

        String proteomes = (String) p.get("proteomes");
        vectorProteome = new Vector<>();
        if (proteomes != null && proteomes.trim().length() != 0) {
            String[] prot = proteomes.split(";");
            for (String set : prot) {
                String[] resp = set.split("=");
                vectorProteome.add(new Proteome(resp[0], new File(resp[1])));
            }
        }
        listProteomes.setListData(vectorProteome);

        updateList();

        setText(p, jtBasename, "basename");
        setText(p, jtDestinationFolder, "destination-folder");

        setBoolean(p, jcBlastSearch, "search-blast");
        setText(p, jtMakeblastdb, "search-blast-makeblastdb");
        setText(p, jtBlastp, "search-blast-blastp");
        setText(p, jtBlastpIdentity, "search-blast-identity");
        setText(p, jtBlastpCover, "search-blast-cover");
        setText(p, jtBlastpWordsize, "search-blast-wordsize");

        setItemList(p, jcbBlastTask, "search-blast-task-blastpsort");
        setBoolean(p,jcAppendAccession, "hit-accession");
        
        adjustBlastSearchItems();
        testMakeblastDb();
        testBlastp();
    }

    private void activateSoftware(JCheckBox software, JLabel jlSoftware, JRadioButton jrDefault, JRadioButton jrThreshold, JTextField jtVal) {
        if (!software.isSelected()) {
            jlSoftware.setForeground(new java.awt.Color(153, 153, 153));

        } else {
            jlSoftware.setForeground(new java.awt.Color(0, 0, 0));
        }
        jrDefault.setEnabled(software.isSelected());
        jrThreshold.setEnabled(software.isSelected());
        jtVal.setEnabled(software.isSelected());
        selectSoftwareDefault(jrDefault, jtVal);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JbCheckFasta;
    private javax.swing.ButtonGroup bgBepipred;
    private javax.swing.ButtonGroup bgChou;
    private javax.swing.ButtonGroup bgEmini;
    private javax.swing.ButtonGroup bgKarplus;
    private javax.swing.ButtonGroup bgKolaskar;
    private javax.swing.ButtonGroup bgParker;
    private javax.swing.ButtonGroup bgSearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton jbAddProteome;
    private javax.swing.JButton jbClear;
    private javax.swing.JButton jbCopy;
    private javax.swing.JButton jbLoadConfig;
    private javax.swing.JButton jbOpenFile;
    private javax.swing.JButton jbRemoveProteome;
    private javax.swing.JButton jbRun;
    private javax.swing.JButton jbSave;
    private javax.swing.JButton jbSaveConfig;
    private javax.swing.JButton jbSubmitBepipred2;
    private javax.swing.JButton jbTestBlastP;
    private javax.swing.JButton jbTestMakeblastdb;
    private javax.swing.JCheckBox jcAppendAccession;
    private javax.swing.JCheckBox jcBlastSearch;
    private javax.swing.JCheckBox jcChou;
    private javax.swing.JCheckBox jcEmini;
    private javax.swing.JCheckBox jcKarplus;
    private javax.swing.JCheckBox jcKolaskar;
    private javax.swing.JCheckBox jcParker;
    private javax.swing.JCheckBox jcSelectAll;
    private javax.swing.JComboBox<String> jcbBlastTask;
    private javax.swing.JLabel jlBlastTask;
    private javax.swing.JLabel jlBlastp;
    private javax.swing.JLabel jlBlastpCover;
    private javax.swing.JLabel jlBlastpIdentity;
    private javax.swing.JLabel jlBlastpWordsize;
    private javax.swing.JLabel jlChouThreshold;
    private javax.swing.JLabel jlEminiThreshold;
    private javax.swing.JLabel jlKarplusThreshold;
    private javax.swing.JLabel jlKolaskarThreshold;
    private javax.swing.JLabel jlMakeblastdb;
    private javax.swing.JLabel jlParkerThreshold;
    private javax.swing.JPanel jpBepipred;
    private javax.swing.JPanel jpMain;
    private javax.swing.JPanel jpResults;
    private javax.swing.JRadioButton jrBepipredJobid;
    private javax.swing.JRadioButton jrBepipredOnline;
    private javax.swing.JRadioButton jrChouDefault;
    private javax.swing.JRadioButton jrChouThreshold;
    private javax.swing.JRadioButton jrEminiDefault;
    private javax.swing.JRadioButton jrEminiThreshold;
    private javax.swing.JRadioButton jrIEDBStd;
    private javax.swing.JRadioButton jrKarplusDefault;
    private javax.swing.JRadioButton jrKarplusThreshold;
    private javax.swing.JRadioButton jrKolaskarDefault;
    private javax.swing.JRadioButton jrKolaskarThreshold;
    private javax.swing.JRadioButton jrParkerDefault;
    private javax.swing.JRadioButton jrParkerThreshold;
    private javax.swing.JTextField jtBasename;
    private javax.swing.JTextField jtBepPredFile;
    private javax.swing.JTextField jtBepiPredJobId;
    private javax.swing.JTextField jtBepiPredMaxLength;
    private javax.swing.JTextField jtBepiPredMinLength;
    private javax.swing.JTextField jtBepipredThreshold;
    private javax.swing.JTextField jtBlastp;
    private javax.swing.JTextField jtBlastpCover;
    private javax.swing.JTextField jtBlastpIdentity;
    private javax.swing.JTextField jtBlastpWordsize;
    private javax.swing.JTextField jtChouThreshold;
    private javax.swing.JTextField jtDestinationFolder;
    private javax.swing.JTextField jtEminiThreshold;
    private javax.swing.JTextArea jtFastabepipred;
    private javax.swing.JTextField jtKarplusThreshold;
    private javax.swing.JTextField jtKolaskarThreshold;
    private javax.swing.JTextField jtMakeblastdb;
    private javax.swing.JLabel jtMessage;
    private javax.swing.JTextField jtParkerThreshold;
    private javax.swing.JTextArea jtResults;
    private javax.swing.JList<Proteome> listProteomes;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

    private boolean testMakeblastDb() {
        if (!CommandTest.test(jtMakeblastdb.getText())) {
            jbTestMakeblastdb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/error.png"))); // NOI18N 
            showError("Command " + jtMakeblastdb.getText() + " not found.");
            return false;
        } else {
            jbTestMakeblastdb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ok.png"))); // NOI18N  
            showMessage(("Command " + jtMakeblastdb.getText() + " found."));
            saveProperties();
            return true;
        }
    }

    private boolean testBlastp() {
        if (!CommandTest.test(jtBlastp.getText())) {
            jbTestBlastP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/error.png"))); // NOI18N 
            showError("Command " + jtBlastp.getText() + " not found.");

            return false;
        } else {
            jbTestBlastP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ok.png"))); // NOI18N  
            showMessage(("Command " + jtBlastp.getText() + " found."));
            saveProperties();
            return true;
        }
    }

    public void waiterBepipredOnline() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10000);
                        BepiPredOnlineRunner.getBepiPredByJobId(Parameters.BEPIPRED2_JOBID);
                        if (JOptionPane.showConfirmDialog(rootPane, "Your job " + Parameters.BEPIPRED2_JOBID + " is ready. Do you want perform the search?", "Job" + Parameters.BEPIPRED2_JOBID + " ready!",
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            tabbedPane.setSelectedComponent(jpResults);
                            jbRunActionPerformed(null);
                        } else {
                            tabbedPane.setSelectedComponent(jpMain);
                        }
                        return;
                    } catch (InterruptedException ex) {
                    } catch (Exception ex) {
                        jtResults.setText("Your job: " + Parameters.BEPIPRED2_JOBID + " is not ready yet.");
                        tabbedPane.setSelectedComponent(jpResults);
                    }
                }

            }
        }.start();
    }
}
