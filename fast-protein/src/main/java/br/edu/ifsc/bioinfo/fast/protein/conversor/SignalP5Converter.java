/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.bioinfo.fast.protein.conversor;

import br.edu.ifsc.bioinfo.fast.protein.CommandRunner;
import br.edu.ifsc.bioinfo.fast.protein.Protein;
import br.edu.ifsc.bioinfo.fast.util.FASTASplitter;
import br.edu.ifsc.bioinfo.fast.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author renato
 */
public class SignalP5Converter {

    /**
     * --organism
     */
    public enum Organism {
        euk,
        arch,
        gram_pos,
        gram_neg,
        none
    }
    
    private HashMap<String, String> map = new HashMap<>();
    private File fasta;
    
    public SignalP5Converter(File fasta) {
        this.fasta = fasta;
    }
    
    public void execute(Organism org, ArrayList<Protein> proteins) {
        System.out.println("Executing SignalP-5");
        try {
            
            if (!(new File("signalp5.txt").exists())) {
                List<File> files = FASTASplitter.subfasta(proteins, 3000, "signalp");
                CommandRunner.run("chmod +x signalp5.sh");
                ArrayList<File> generated = new ArrayList<>();
                for (File file : files) {
                    CommandRunner.run(String.format("./signalp5.sh %s %s", file.getAbsolutePath(), org.toString()));
                    String arqResult = FilenameUtils.removeExtension(file.getName()) + "_summary.signalp5";
                    generated.add(new File(arqResult));
                    file.deleteOnExit();
                }
                StringBuilder signalPout = new StringBuilder();
                signalPout.append("# SignalP-5.0	Organism: euk	").append("\n");
                signalPout.append("# ID	Prediction	SP(Sec/SPI)	OTHER	CS Position").append("\n");
                for (File file : generated) {
                    if (file.exists()) {
                        Scanner s = new Scanner(file);
                        while (s.hasNext()) {
                            String ln = s.nextLine();
                            if (!ln.startsWith("#")) {
                                signalPout.append(ln).append("\n");
                            }
                        }
                    }
                    file.deleteOnExit();
                }
                
                FileUtils.createFile(signalPout.toString(), "signalp5.txt");
            } else {
                System.out.println("Signalp5 file signalp5.txt already exists. This file will be processed.");
            }
            
            Scanner s = new Scanner(new File("signalp5.txt"));
            s.nextLine();
            s.nextLine();
            while (s.hasNext()) {
                String ln = s.nextLine();
                if (!ln.trim().isBlank() || !ln.startsWith("#")) {
                    String[] res = ln.split("\t");
                    map.put(res[0], res[1]);
                }
            }
        } catch (Exception ex) {
            System.out.println("SignalP5 not executed, this feature will be skipped.");
            System.err.println(ex.getMessage());
        }
    }
    
    public void execute(Organism org) {
        System.out.println("Executing SignalP-5");
        try {
            if (fasta == null) {
                throw new IOException("Fasta file is null");
            }
            
            if (!(new File("signalp5.txt").exists())) {
                CommandRunner.run("chmod +x signalp5.sh");
                CommandRunner.run(String.format("./signalp5.sh %s %s", fasta.getAbsolutePath(), org.toString()));
            } else {
                System.out.println("Signalp5 file signalp5.txt already exists. This file will be processed.");
            }
            
            Scanner s = new Scanner(new File("signalp5.txt"));
            s.nextLine();
            s.nextLine();
            while (s.hasNext()) {
                String ln = s.nextLine();
                if (!ln.trim().isBlank() || !ln.startsWith("#")) {
                    String[] res = ln.split("\t");
                    
                    map.put(res[0], res[1]);
                }
            }
        } catch (Exception ex) {
            System.out.println("SignalP5 not executed, this feature will be skipped.");
            System.err.println(ex.getMessage());
        }
    }
    
    public String getSignal(String id) {
        return (map.get(id) != null) ? map.get(id) : "-";
    }
}
