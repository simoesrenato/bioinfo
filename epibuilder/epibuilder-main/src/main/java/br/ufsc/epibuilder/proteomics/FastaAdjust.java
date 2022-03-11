/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsc.epibuilder.proteomics;

import br.ufsc.epibuilder.converter.ProteinConverter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author renato
 */
public class FastaAdjust {

    public static ArrayList<ProteinConverter> getProteins(File file) throws FileNotFoundException {
        ArrayList<ProteinConverter> proteins = new ArrayList<>();
        Scanner s = new Scanner(file);

        ProteinConverter proteina = null;
        while (s.hasNext()) {
            String ln = s.nextLine();
            if (ln.startsWith(">")) {
                proteina = new ProteinConverter(ln.substring(1));
                proteins.add(proteina);
            } else {
                proteina.append(ln);
            }
        }
        return proteins;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println("");
        //LinkedHashMap<String, ProteinSequence> a
          //      = FastaReaderHelper.readFastaProteinSequence(new File("/bioinformatic/epifinder/runs/sarscov2/novo/allprot0331.fasta.clean"));
        ArrayList<ProteinConverter> proteins = getProteins(new File("/bioinformatic/epifinder/runs/sarscov2/novo/sars-cov-2.fasta"));
        ArrayList<ProteinConverter> proteins2 = getProteins(new File("/bioinformatic/epifinder/runs/sarscov2/novo/allprot0331.fasta.clean"));
        //s FastaReaderHelper.re
        //LinkedHashMap<String, ProteinSequence> b = FastaReaderHelper.readFastaProteinSequence(new File("/bioinformatic/epifinder/runs/sarscov2/novo/spikeprot0331.fasta.clean"));
        //String header = ">Spike|hCoV-19/pangolin/Guangxi/P2V/2017|2017-00-00|EPI_ISL_410542|Vero E6|hCoV-19^^Guangxi|Manis javanica|Beijing Institute of Microbiology and Epidemiology|Beijing Institute of Microbiology and Epidemiology|Lam|China";
        //String headerEpi = header.substring(header.indexOf("EPI_ISL"));
        //String headerFinal = headerEpi.substring(0, headerEpi.indexOf("|"));
        //    System.exit(0);
        //String src = args[1];]
        ArrayList<String> srcs = new ArrayList<>();
        srcs.add("/bioinformatic/epifinder/runs/sarscov2/gsaid/spikeprot0331.fasta");
        srcs.add("/bioinformatic/epifinder/runs/sarscov2/gsaid/allprot0331.fasta");
        for (String src : srcs) {
            Scanner s = new Scanner(new File(src));
            FileWriter fw = new FileWriter(new File(src + ".clean"), true);
            while (s.hasNext()) {
                String res = s.nextLine();
                if (res.startsWith(">")) {
                    String header = res;
                    String headerEpi = header.substring(header.indexOf("EPI_ISL"));
                    String headerFinal = headerEpi.substring(0, headerEpi.indexOf("|"));

                    String name = header.substring(0, header.indexOf("|"));

                    fw.write(name + "-" + headerFinal);
                } else {
                    fw.write(res);
                }
                fw.write("\n");
                fw.flush();
            }
            fw.close();
        }
    }
}
