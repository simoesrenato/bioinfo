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
public class WolfPsortConverter {

    public enum Type {
        animal, fungi, plant, none;
    }

    private HashMap<String, String> map = new HashMap<>();
    private File fasta;

    public WolfPsortConverter(File fasta) {
        this.fasta = fasta;
    }

    public void execute(Type type) {

        if (type == Type.none) {
            System.out.println("Skipping subcellular predition");
            return;
        }

        System.out.println("Executing WoLFPSORT");

        try {
            if (fasta == null) {
                throw new IOException("Fasta file is null");
            }

            if (!(new File("wolfpsort.txt").exists())) {
                CommandRunner.run("chmod +x wolfpsort.sh");
                CommandRunner.run(String.format("./wolfpsort.sh %s %s", type, fasta.getAbsolutePath()));
            } else {
                System.out.println("WoLF PSORT file wolfpsort.txt already exists. This file will be processed.");

            }
            Scanner s = new Scanner(new File("wolfpsort.txt"));

            while (s.hasNext()) {
                String line = s.nextLine();
                if (!line.startsWith("#") && !line.trim().isBlank()) {
                    String[] val = line.split(" ");
                    map.put(val[0], val[1]);
                }
            }
        } catch (Exception ex) {
            System.out.println("WolfPsort not executed, this feature will be skipped.");
            System.err.println(ex.getMessage());
        }
    }

    public String getLocation(String id) {
        return (map.get(id) != null) ? map.get(id) : "-";
    }

}
