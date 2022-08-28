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
public class TMHMM2Converter {

    private HashMap<String, Integer> map = new HashMap<>();
    private File fasta;

    public TMHMM2Converter(File fasta) {
        this.fasta = fasta;
    }

    public void execute() {
        System.out.println("Executing TMHMM-2.0c");
        try {
            if (fasta == null) {
                throw new IOException("Fasta file is null");
            }

            if (!(new File("tmhmm2.txt").exists())) {
                CommandRunner.run("chmod +x tmhmm2.sh");
                CommandRunner.run(String.format("./tmhmm2.sh %s", fasta.getAbsolutePath()));
            } else {
                System.out.println("TMHMM-2 file tmhmm2.txt already exists. This file will be processed.");

            }
            Scanner s = new Scanner(new File("tmhmm2.txt"));
            while (s.hasNext()) {
                String ln = s.nextLine();
                if (!ln.trim().isBlank()) {
                    String[] res = ln.split("\t");
                    res[4] = res[4].split("=")[1];
                    map.put(res[0], Integer.parseInt(res[4]));
                }
            }

        } catch (Exception ex) {
            System.out.println("TMHMM not executed, this feature will be skipped.");
            System.err.println(ex.getMessage());
        }
    }

    public Integer getTotalTransmembrane(String id) {
        return (map.get(id) != null) ? map.get(id) : 0;
    }

}
