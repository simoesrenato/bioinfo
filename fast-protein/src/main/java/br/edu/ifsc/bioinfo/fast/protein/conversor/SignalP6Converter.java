/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.bioinfo.fast.protein.conversor;

import br.edu.ifsc.bioinfo.fast.protein.CommandRunner;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author renato
 */
public class SignalP6Converter {

    /**
     * --organism
     */
    public enum Organism {
        euk, other
    }

    private HashMap<String, String> map = new HashMap<>();
    private File fasta;

    public SignalP6Converter(File fasta) {
        this.fasta = fasta;
    }

    public void execute(Organism org) {
        System.out.println("Executing SignalP6");
        try {
            if (fasta == null) {
                throw new IOException("Fasta file is null");
            }

            if (!(new File("signalp6/prediction_results.txt").exists())) {
                CommandRunner.run("chmod +x signalp6.sh");
                CommandRunner.run(String.format("./signalp6.sh %s %s", fasta.getAbsolutePath(), org.toString()));
            }
            Scanner s = new Scanner(new File("signalp6/prediction_results.txt"));
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
            System.out.println("SignalP6 not executed, this feature will be skipped.");
            System.err.println(ex.getMessage());
        }
    }

    public String getSignal(String id) {
        return (map.get(id) != null) ? map.get(id) : "-";
    }

}
