/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.bioinfo.fast.protein.conversor;

import br.edu.ifsc.bioinfo.fast.protein.CommandRunner;
import br.edu.ifsc.bioinfo.fast.protein.Protein;
import br.edu.ifsc.bioinfo.fast.util.FASTASplitter;
import br.edu.ifsc.bioinfo.fast.util.GeneOntologyUtil;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.biojava.nbio.ontology.Term;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.csv.CsvReadOptions;

/**
 *
 * @author renato
 */
public class InterproScanConverter {

    private ArrayList<Protein> proteins;
    private int limit;

    public static final String PROTEIN_ACCESSION = "Protein_accession";
    public static final String SEQUENCE_MD5_DIGEST = "Sequence_MD5_digest";
    public static final String SEQUENCE_LENGTH = "Sequence_length";
    public static final String ANALYSIS = "Analysis";
    public static final String SIGNATURE_ACCESSION = "Signature_accession";
    public static final String SIGNATURE_DESCRIPTION = "Signature_description";
    public static final String START_LOCATION = "Start_location";
    public static final String STOP_LOCATION = "Stop_location";
    public static final String SCORE = "Score";
    public static final String STATUS = "Status";
    public static final String DATE = "Date";
    public static final String INTERPRO_ANNOTATIONS_ACCESSION = "InterPro_annotations_accession";
    public static final String INTERPRO_ANNOTATIONS_DESCRIPTION = "InterPro_annotations_description";
    public static final String GO_ANNOTATIONS = "GO_annotations";
    public static final String ADDITIONAL = "Additional";

    private static String[] header = {
        PROTEIN_ACCESSION,
        SEQUENCE_MD5_DIGEST,
        SEQUENCE_LENGTH,
        ANALYSIS,
        SIGNATURE_ACCESSION,
        SIGNATURE_DESCRIPTION,
        START_LOCATION,
        STOP_LOCATION,
        SCORE,
        STATUS,
        DATE,
        INTERPRO_ANNOTATIONS_ACCESSION,
        INTERPRO_ANNOTATIONS_DESCRIPTION,
        GO_ANNOTATIONS,
        ADDITIONAL
    };

    public InterproScanConverter(ArrayList<Protein> proteins, int limit) {
        this.proteins = proteins;
        this.limit=limit;
    }

    public void execute() {
        FileWriter fw = null;
        System.out.println("Executing Interproscan, this process calls a remote service, be patient. =)");
        ArrayList<String> lines = new ArrayList<>();
        lines.add(String.join("\t", header));
        System.out.println("Executing Interproscan");
        try {

            CommandRunner.run("chmod +x interpro.sh");

            List<File> files = FASTASplitter.subfasta(proteins, limit, "interpro");
            for (File file : files) {
                CommandRunner.run(String.format("./interpro.sh %s %s", FilenameUtils.getBaseName(file.getName()), file.getCanonicalPath()));
            }

            for (File fileSplit : files) {
                File file = new File(String.format("%s.tsv.tsv", FilenameUtils.getBaseName(fileSplit.getName())));

                // File file = new File(String.format("interpro.tsv.tsv"));
                Scanner s = new Scanner(file);
                while (s.hasNext()) {
                    String line = s.nextLine();
                    String[] cols = line.split("\t");
                    if (cols.length != header.length) {
                        String[] newLine = createEmptyArray(header.length);
                        for (int i = 0; i < cols.length; i++) {
                            newLine[i] = cols[i];
                        }
                        newLine[header.length - 1] = "-";
                        lines.add(String.join("\t", newLine));
                    } else {
                        cols[header.length - 1] = "-";
                        lines.add(String.join("\t", cols));
                    }

                }
                file.deleteOnExit();
                fileSplit.deleteOnExit();
            }
            File iprFile = new File("interpro.tsv");
            fw = new FileWriter(iprFile);
            fw.write(String.join("\n", lines));
            fw.flush();

        } catch (Exception ex) {
            System.out.println("Inteproscan not executed, this feature will be skipped.");
            System.err.println(ex.getMessage());
        }
    }

    public void updateProteins(List<Protein> proteins) {
        File iprFile = new File("interpro.tsv");
        updateProteins(proteins, iprFile);
    }

    public void updateProteins(List<Protein> proteins, File iprFile) {
        if (iprFile.exists()) {
            try {
                //Parsing interpro final file
                CsvReadOptions options
                        = CsvReadOptions.builder(iprFile)
                                .separator('\t').
                                build();
                Table tabela = Table.read().csv(options);
                ArrayList<String> wego = new ArrayList<>();
                for (Protein protein : proteins) {
                    Table result = tabela.where(t -> t.stringColumn(PROTEIN_ACCESSION).equalsIgnoreCase(protein.getId()));
                    for (Row row : result) {
                        if (row.getString(ANALYSIS).equalsIgnoreCase("pfam")) {
                            protein.addPfam(row.getString(SIGNATURE_DESCRIPTION));
                        }
                        if (row.getString(ANALYSIS).equalsIgnoreCase("panther")) {
                            protein.addPanther(row.getString(SIGNATURE_DESCRIPTION));
                        }

                        protein.addInterpro(row.getString(INTERPRO_ANNOTATIONS_ACCESSION), row.getString(INTERPRO_ANNOTATIONS_DESCRIPTION));
                        protein.addGO(row.getString(GO_ANNOTATIONS));
                    }
                    wego.add(String.format("%s\t%s", protein.getId(), String.join("\t", protein.getSeparatedGO())));
                }
                if (!wego.isEmpty()) {
                    FileWriter fwego = new FileWriter(new File("wego.txt"));
                    fwego.append(String.join("\n", wego));
                    fwego.flush();
                    fwego.close();

                }
            } catch (IOException ex) {
                Logger.getLogger(InterproScanConverter.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public static String[] createEmptyArray(int size) {
        String[] array = new String[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = "-";
        }
        return array;
    }

    public static void main(String[] args) throws Exception {
        /*ArrayList<Protein> proteins = new ArrayList<>();
            proteins.add(new Protein("Q9BYF1"));
            proteins.add(new Protein("Q8N884"));
            proteins.add(new Protein("P17301"));
            proteins.add(new Protein("P04439"));
            File iprFile = new File("/Users/renato/Documents/GitHub/bioinfo/fast-protein/biolib/interpro.tsv");
            new InterproScanConverter(null).updateProteins(proteins, iprFile);
            System.out.println("");*/

        String[] gos = {"GO:0008152", "GO:0044281", "GO:0044238", "GO:0019319"};

        for (String go : gos) {
            Term term = GeneOntologyUtil.getTerm(go);

            System.out.println("");
        }
        Term term1 = GeneOntologyUtil.getTerm(gos[0]);
        Term term2 = GeneOntologyUtil.getTerm(gos[1]);
        Term term3 = GeneOntologyUtil.getTerm(gos[2]);
        Term term4 = GeneOntologyUtil.getTerm(gos[3]);
        System.out.println(term1.getOntology().containsTerm(gos[1]));
        System.out.println(term1.getOntology().containsTerm(gos[2]));
        System.out.println(term1.getOntology().containsTerm(gos[3]));
        System.out.println(term2.getOntology().containsTerm(gos[1]));
        System.out.println(term2.getOntology().containsTerm(gos[2]));
        System.out.println(term2.getOntology().containsTerm(gos[3]));
        System.out.println(term3.getOntology().containsTerm(gos[1]));
        System.out.println(term3.getOntology().containsTerm(gos[2]));
        System.out.println(term3.getOntology().containsTerm(gos[3]));
        System.out.println(term4.getOntology().containsTerm(gos[1]));
        System.out.println(term4.getOntology().containsTerm(gos[2]));
        System.out.println(term4.getOntology().containsTerm(gos[3]));
        for (Object remoteTerm : term1.getSynonyms()) {
            System.out.println(remoteTerm);
        }

    }
}
