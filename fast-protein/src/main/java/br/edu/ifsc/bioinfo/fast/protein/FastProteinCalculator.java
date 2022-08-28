package br.edu.ifsc.bioinfo.fast.protein;

import br.edu.ifsc.bioinfo.fast.protein.conversor.BlastConverter;
import br.edu.ifsc.bioinfo.fast.protein.conversor.InterproScanConverter;
import br.edu.ifsc.bioinfo.fast.protein.conversor.WolfPsortConverter;
import br.edu.ifsc.bioinfo.fast.protein.conversor.SignalP5Converter;
import br.edu.ifsc.bioinfo.fast.protein.conversor.TMHMM2Converter;
import br.edu.ifsc.bioinfo.fast.protein.conversor.geneontology.GeneOntologyProcess;
import static br.edu.ifsc.bioinfo.fast.util.FileUtils.createFile;
import br.edu.ifsc.bioinfo.fast.util.GeneOntologyUtil;
import br.edu.ifsc.bioinfo.fast.util.MarkdownHelper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import static br.edu.ifsc.bioinfo.fast.util.MarkdownHelper.*;
import br.edu.ifsc.bioinfo.fast.util.MathCalculator;
import br.edu.ifsc.bioinfo.fast.util.Summary;
import java.util.Date;

/**
 *
 * @author renato
 */
public class FastProteinCalculator {

    private static Pattern patternErret = Pattern.compile("[KRHQSA][DENQ][E][L]");
    private static Pattern patternNglyc = Pattern.compile("[N][ARNDBCEQZGHILKMFSTWYV][ST]");

    public enum Output {
        csv(",", ".csv"), tsv("\t", ".txt"), txt("txt", ".txt"), sep("sep", ".txt"), raw("raw", ".tsv");
        private final String separator;
        private final String extension;

        private Output(String separator, String extension) {
            this.separator = separator;
            this.extension = extension;
        }

        public String getSeparator() {
            return separator;
        }

        public String getExtension() {
            return extension;
        }
    }

    public enum Field {

        ID("Id"),
        HEADER("Header"),
        LENGTH("Length"),
        KDA("kDa"),
        ISOELETRIC_POINT("Isoelectric Point"),
        HYDROPATHY("Hydropathy"),
        AROMATICITY("Aromaticity"),
        SUBCELLULAR_LOCALIZATION("Subcell Localization"),
        ERR_TOTAL("E.R Retention Total"),
        ERR_DOMAINS("E.R Retention Domains"),
        NGLYC_TOTAL("NGlyc Total"),
        NGLYC_DOMAINS("NGlyc Domains"),
        SEQUENCE("Sequence"),
        TRANSMEMBRANE("Transmembrane"),
        SIGNAL_P5("SignalP5"),
        BLAST_DESC("Blast description"),
        GO_ANNOTATION("Gene Ontology"),
        INTERPRO_ANNOTATION("Interpro Annotation"),
        PFAM_ANNOTATION("PFAM Annotation"),
        PANTHER_ANNOTATION("Panther Annotation");

        private String description;

        private Field(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }

    }

    public static File generateCleanFasta(ArrayList<Protein> proteins) throws IOException {
        StringBuilder sbFasta = new StringBuilder();
        for (Protein protein : proteins) {
            sbFasta.append(">" + protein.getId() + "\n");
            sbFasta.append(protein.getSequence() + "\n");
        }
        File out = new File("clean.fasta");
        FileWriter fw = new FileWriter(out);
        fw.write(sbFasta.toString());
        fw.flush();
        fw.close();
        return out;
    }

    private static void replaceSize(String value, Field field, HashMap<Field, Integer> mapSize) {
        int maxSize = mapSize.get(field);
        int length = value.length();
        if (length > maxSize) {
            mapSize.put(field, length);
        }
    }

    public static boolean createChart() {

        try {
            System.out.println("Generating charts");
            CommandRunner.run(String.format("chmod +x charts.sh"));
            CommandRunner.run(String.format("./charts.sh"));
            return true;

        } catch (Exception ex) {
            System.out.println("Error creating charts: " + ex.getMessage());
            return false;
        }

    }

    public static void run(File fileSource,
            Output out[],
            String fileOutput, WolfPsortConverter.Type psortType, SignalP5Converter.Organism signalPOption, boolean blast, BlastConverter.Database blastDb, int blastTimeout, boolean interpro, int interproLimit) {
        StringBuilder outputmd = new StringBuilder();
        LinkedHashMap<String, ProteinSequence> resp = null;

        ArrayList<String[]> goP = new ArrayList<>();
        ArrayList<String[]> goF = new ArrayList<>();
        ArrayList<String[]> goC = new ArrayList<>();
        String[] goHeader = {"GO", "Description", "Total"};
        Integer[] goHeaderAlignments = {LEFT, LEFT, RIGHT};

        try {
            resp = FastaReaderHelper.readFastaProteinSequence(fileSource);
        } catch (IOException ex) {
            System.err.println("Invalid FASTA file. Please check your informed file " + fileSource);
            ex.printStackTrace();
            System.exit(1);
        }

        System.out.println("#Fast-protein Software 1.0");
        System.out.println("#Developed - Magic Quintet - Bioinformatic Laboratory - UFSC and IFSC");
        System.out.println("#An automated pipeline for proteomic analysis");
        System.out.println("#Pattern ER Retention [KRHQSA][DENQ][E][L]");
        System.out.println("#Pattern N-Glyc [N][Xaa(except P)][ST] | Xaa = any aminoacid");
        System.out.println("#Questions and issues: renato.simoes@ifsc.edu.br");

        ArrayList<Protein> proteins = new ArrayList<>();

        for (ProteinSequence s : resp.values()) {
            String id = s.getAccession().getID();
            String proteinId = id.split(" ")[0];

            String sequence = s.getSequenceAsString().toUpperCase();
            Protein protein = new Protein(proteinId, sequence);
            protein.setHeader(s.getAccession().getID());

            //Processing ER Retention
            Matcher erretMatcher = patternErret.matcher(sequence);
            while (erretMatcher.find()) {
                protein.addErretDomain(new Domain(erretMatcher.group(), erretMatcher.start(), erretMatcher.end()));
            }
            //Processing Nglyc domain
            Matcher nGlycmatcher = patternNglyc.matcher(sequence);
            while (nGlycmatcher.find()) {
                protein.addNglycDomain(new Domain(nGlycmatcher.group(), nGlycmatcher.start(), nGlycmatcher.end()));
            }

            if (blast) {
                protein.setBlastHit(BlastConverter.getFirstHit(protein, blastDb, blastTimeout));
            }

            proteins.add(protein);
        }
        //Generate erret and n-glyc files
        StringBuilder sbErret = new StringBuilder();
        StringBuilder sbNglyc = new StringBuilder();

        sbErret.append("#Search for Endoplasmic Reticulum Retention sites by sequence [KRHQSA]-[DENQ]-E-L>.\n");
        sbErret.append("#Reference: https://prosite.expasy.org/PS00014\n");
        sbErret.append("ID\tDomains\n");

        sbNglyc.append("#Search for N-glycosylation sites by sequence N-{P}-[ST]-{P}.\n");
        sbNglyc.append("#Reference: https://prosite.expasy.org/PS00001\n");
        sbNglyc.append("ID\tDomains\n");
        for (Protein protein : proteins) {
            sbErret.append(String.format("%s\t%s\n", protein.getId(), protein.getErretDomainsAsString()));
            sbNglyc.append(String.format("%s\t%s\n", protein.getId(), protein.getnGlycDomainsAsString()));
        }

        try {
            createFile(sbErret.toString(), "erret.txt");
            createFile(sbNglyc.toString(), "nglyc.txt");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            //Generate clean fasta
            File cleanFasta = generateCleanFasta(proteins);
            //System.out.println("Clean fasta generated");

            WolfPsortConverter wolfpsort = new WolfPsortConverter(cleanFasta);
            if (psortType != WolfPsortConverter.Type.none) {
                wolfpsort.execute(psortType);
            }

            //Executing TMHMM2.0c
            TMHMM2Converter tmhmm2 = new TMHMM2Converter(cleanFasta);
            tmhmm2.execute();

            SignalP5Converter signalp5 = new SignalP5Converter(cleanFasta);
            if (signalPOption != SignalP5Converter.Organism.none) {
                signalp5.execute(SignalP5Converter.Organism.euk, proteins);
            } else {
                System.out.println("Skipping SignalP-5 prediction");
            }

            Summary summary = new Summary();

            //Executing InteproScan
            if (interpro) {
                InterproScanConverter iprScan = new InterproScanConverter(proteins, interproLimit);
                iprScan.execute();
                iprScan.updateProteins(proteins);

                //creating files for type of GO's
                HashMap<String, Integer> mapP = sortByValue(GeneOntologyProcess.getMap(proteins, GeneOntologyUtil.Type.biological_process));
                HashMap<String, Integer> mapC = sortByValue(GeneOntologyProcess.getMap(proteins, GeneOntologyUtil.Type.cellular_component));
                HashMap<String, Integer> mapF = sortByValue(GeneOntologyProcess.getMap(proteins, GeneOntologyUtil.Type.molecular_function));

                for (Map.Entry<String, Integer> go : mapP.entrySet()) {

                    String description = GeneOntologyUtil.getOntology(go.getKey());
                    goP.add(new String[]{go.getKey(), description, go.getValue().toString()});
                    summary.addGoP(go.getKey(), description, go.getValue());

                }

                for (Map.Entry<String, Integer> go : mapF.entrySet()) {
                    String description = GeneOntologyUtil.getOntology(go.getKey());

                    goF.add(new String[]{go.getKey(), description, go.getValue().toString()});
                    summary.addGoF(go.getKey(), description, go.getValue());
                }

                for (Map.Entry<String, Integer> go : mapC.entrySet()) {
                    String description = GeneOntologyUtil.getOntology(go.getKey());

                    goC.add(new String[]{go.getKey(), description, go.getValue().toString()});
                    summary.addGoC(go.getKey(), description, go.getValue());
                }

                StringBuilder sbgop = new StringBuilder();
                sbgop.append(String.join("\t", goHeader)).append("\n");
                for (String[] cols : goP) {
                    sbgop.append(String.join("\t", cols)).append("\n");
                }
                createFile(sbgop.toString(), String.format("go-%s.txt", GeneOntologyUtil.Type.biological_process));

                StringBuilder sbgoc = new StringBuilder();
                sbgoc.append(String.join("\t", goHeader)).append("\n");
                for (String[] cols : goC) {
                    sbgoc.append(String.join("\t", cols)).append("\n");
                }
                createFile(sbgoc.toString(), String.format("go-%s.txt", GeneOntologyUtil.Type.cellular_component));

                StringBuilder sbgof = new StringBuilder();
                sbgof.append(String.join("\t", goHeader)).append("\n");
                for (String[] cols : goF) {
                    sbgof.append(String.join("\t", cols)).append("\n");
                }
                createFile(sbgof.toString(), String.format("go-%s.txt", GeneOntologyUtil.Type.molecular_function));

            }

            //Calculate column size to formated report
            HashMap<Field, Integer> mapSize = new HashMap<>();
            for (Field value : Field.values()) {
                mapSize.put(value, value.toString().length());
            }
            for (Field value : Field.values()) {
                replaceSize(value.toString(), value, mapSize);
            }

            int totalWithER = 0;
            int totalWithNglyc = 0;
            int totalTransmembrane = 0;
            int totalSignalp = 0;
            HashMap<String, Integer> subcellTotalMap = new HashMap<>();
            HashMap<String, Integer> erretMap = new HashMap<>();
            HashMap<String, Integer> nglycMap = new HashMap<>();

            ArrayList<Double> listKda = new ArrayList<>();
            ArrayList<Double> listIso = new ArrayList<>();
            ArrayList<Double> listHydro = new ArrayList<>();
            ArrayList<Double> listAro = new ArrayList<>();

            for (Protein protein : proteins) {
                try {
                    listKda.add(protein.getKda());
                    listIso.add(protein.getIsoeletricPointAvg());
                    listHydro.add(protein.getHydropathy());
                    listAro.add(protein.getAromaticity());

                    protein.setSubcellularLocalization(wolfpsort.getLocation(protein.getId()));
                    protein.setTransmembrane(tmhmm2.getTotalTransmembrane(protein.getId()));
                    protein.setSignalp5(signalp5.getSignal(protein.getId()));
                    if (protein.getErretTotal() > 0) {
                        totalWithER++;
                    }
                    if (protein.getnGlycTotal() > 0) {
                        totalWithNglyc++;
                    }
                    if (protein.getTransmembrane() > 0) {
                        totalTransmembrane++;
                    }
                    if (!protein.getSignalp5().trim().equalsIgnoreCase("OTHER") && !protein.getSignalp5().trim().equalsIgnoreCase("-")) {
                        totalSignalp++;
                    }

                    if (subcellTotalMap.containsKey(protein.getSubcellularLocalization())) {
                        subcellTotalMap.computeIfPresent(protein.getSubcellularLocalization(), (k, v) -> v + 1);
                    } else {
                        subcellTotalMap.put(protein.getSubcellularLocalization(), 1);
                    }

                    for (Domain erretDomain : protein.getErretDomains()) {
                        if (erretMap.containsKey(erretDomain.getSequence())) {
                            erretMap.computeIfPresent(erretDomain.getSequence(), (k, v) -> v + 1);
                        } else {
                            erretMap.put(erretDomain.getSequence(), 1);
                        }
                    }
                    for (Domain domain : protein.getNglycDomains()) {
                        if (nglycMap.containsKey(domain.getSequence())) {
                            nglycMap.computeIfPresent(domain.getSequence(), (k, v) -> v + 1);
                        } else {
                            nglycMap.put(domain.getSequence(), 1);
                        }
                    }

                    replaceSize(protein.getId(), Field.ID, mapSize);
                    replaceSize(protein.getLength().toString(), Field.LENGTH, mapSize);

                    replaceSize(protein.getKdaStr(), Field.KDA, mapSize);
                    replaceSize(protein.getIsoeletricPointAvgStr(), Field.ISOELETRIC_POINT, mapSize);
                    replaceSize(protein.getHydropathyStr(), Field.HYDROPATHY, mapSize);
                    replaceSize(protein.getSubcellularLocalization(), Field.SUBCELLULAR_LOCALIZATION, mapSize);
                    replaceSize(protein.getTransmembrane().toString(), Field.TRANSMEMBRANE, mapSize);
                    replaceSize(protein.getSignalp5(), Field.SIGNAL_P5, mapSize);
                    replaceSize(protein.getErretTotal().toString(), Field.ERR_TOTAL, mapSize);
                    replaceSize(protein.getErretDomainsAsString(), Field.ERR_DOMAINS, mapSize);
                    replaceSize(protein.getnGlycTotal().toString(), Field.NGLYC_TOTAL, mapSize);
                    replaceSize(protein.getnGlycDomainsAsString(), Field.NGLYC_DOMAINS, mapSize);
                    replaceSize(protein.getHeader(), Field.HEADER, mapSize);
                    replaceSize(protein.getSequence(), Field.SEQUENCE, mapSize);

                } catch (CompoundNotFoundException ex) {
                    System.err.println("It is not possible to calculate molecular mass, isoelectric point and "
                            + "hydropathy of the protein" + protein.getId() + ". "
                            + "The sequence appears to be in error, check the contents of the FASTA file "
                            + "and run the program again. Check for non-amino acid characters with X and *.");
                    ex.printStackTrace();
                    System.exit(1);
                }
            }

            //FastProtein Summary
            StringBuilder sbSummary = new StringBuilder();
            int qtd = proteins.size();
            MathCalculator mathKda = new MathCalculator(listKda);
            MathCalculator mathIso = new MathCalculator(listIso);
            MathCalculator mathHydro = new MathCalculator(listHydro);
            MathCalculator mathAro = new MathCalculator(listAro);

            summary.setProteins(proteins);
            summary.setKdaMean(mathKda.mean());
            summary.setKdaSd(mathKda.sd());
            summary.setIsoMean(mathIso.mean());
            summary.setIsoSd(mathIso.sd());
            summary.setHydroMean(mathHydro.mean());
            summary.setHydroSd(mathHydro.sd());
            summary.setAromacityMean(mathAro.mean());
            summary.setAromacitySd(mathAro.sd());
            summary.setTotalMembrane(totalTransmembrane);
            summary.setTotalSp(totalSignalp);
            summary.setTotalER(totalWithER);
            summary.setTotalNGlyc(totalWithNglyc);

            sbSummary.append("#FastProtein-1.0 Summary\n");
            sbSummary.append(String.format("\nProcessed proteins: %s", qtd));
            sbSummary.append(String.format("\nMolecular mass (kda) mean: %.2f ± %.2f", summary.getKdaMean(), summary.getKdaSd()));
            sbSummary.append(String.format("\nIsoeletric point mean: %.2f ± %.2f", summary.getIsoMean(), summary.getIsoSd()));
            sbSummary.append(String.format("\nHydrophicity mean: %.2f ± %.2f", summary.getHydroMean(), summary.getHydroSd()));
            sbSummary.append(String.format("\nAromaticity mean: %.2f ± %.2f", summary.getAromacityMean(), summary.getAromacitySd()));
            sbSummary.append(String.format("\nProteins with TM: %s", summary.getTotalMembrane()));
            sbSummary.append(String.format("\nProteins with SP: %s", summary.getTotalSp()));
            sbSummary.append(String.format("\nProteins with E.R Retention domains: %s", summary.getTotalER()));
            sbSummary.append(String.format("\nProteins with NGlycosylation domains: %s", summary.getTotalNGlyc()));

            MarkdownHelper.appendHeader(outputmd, "Fast-protein Software 1.0", 1);
            MarkdownHelper.appendHeader(outputmd, "Protein information software", 5);
            outputmd.append("\n");

            MarkdownHelper.appendLine(outputmd);
            MarkdownHelper.appendHeader(outputmd, "Summary", 3);
            LinkedHashMap<String, String> mapOutputmd = new LinkedHashMap<>();
            mapOutputmd.put("Processed proteins", String.format("%s", qtd));
            mapOutputmd.put("Molecular mass (kda) mean", String.format("%.2f &#177; %.2f", mathKda.mean(), mathKda.sd()));
            mapOutputmd.put("Isoeletric point mean", String.format("%.2f &#177; %.2f", mathIso.mean(), mathIso.sd()));
            mapOutputmd.put("Hydrophicity mean", String.format("%.2f &#177; %.2f", mathHydro.mean(), mathHydro.sd()));
            mapOutputmd.put("Aromaticity mean", String.format("%.2f &#177; %.2f", mathAro.mean(), mathAro.sd()));
            mapOutputmd.put("Proteins with TM", String.format("%s", totalTransmembrane));
            mapOutputmd.put("Proteins with SP", String.format("%s", totalSignalp));
            mapOutputmd.put("Proteins with E.R Retention domains", String.format("%s", totalWithER));
            mapOutputmd.put("Proteins with NGlycosylation domains", String.format("%s", totalWithNglyc));

            MarkdownHelper.appendTableFromMap(outputmd, new String[]{"Information", "Value"}, mapOutputmd);

            //File use to build charts
            StringBuilder kdaisoSb = new StringBuilder();
            for (Protein protein : proteins) {
                try {
                    kdaisoSb.append(String.join(Output.csv.separator,
                            protein.getKdaStr(),
                            protein.getIsoeletricPointAvgStr()));
                    kdaisoSb.append("\n");
                } catch (CompoundNotFoundException ex) {
                    System.err.println("It is not possible to calculate molecular mass, isoelectric "
                            + "point, aromaticity and hydropathy of the protein" + protein.getId() + ". "
                            + "The sequence appears to be in error, check the contents of the FASTA "
                            + "file and run the program again. Check for non-amino acid characters with X and *.");
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
            createFile(kdaisoSb.toString(), "kdaiso.csv");

            boolean charts = createChart();
            if (charts) {
                MarkdownHelper.appendHeader(outputmd, "Molecular mass (kDa) vs Isoelectric point (pH)", 3);
                MarkdownHelper.appendImage(outputmd, "Molecular mass (kDa) vs Isoelectric point (pH)", "kda-vs-pi.png");
                outputmd.append(MarkdownHelper.getLink("Download in 300dpi", "kda-vs-pi-300dpi.png")).append("\n");

                outputmd.append("\n");
                MarkdownHelper.appendHeader(outputmd, "Molecular mass (kDa) histogram", 3);
                MarkdownHelper.appendImage(outputmd, "Molecular mass (kDa) histogram", "his-kda.png");
                outputmd.append(MarkdownHelper.getLink("Download in 300dpi", "his-kda-300dpi.png")).append("\n").append("\n");

            }

            MarkdownHelper.appendLine(outputmd);

            MarkdownHelper.appendHeader(outputmd, "Subcellular localization (by WolfPSort) - Organism: " + psortType, 3);
            if (psortType != WolfPsortConverter.Type.none) {
                sbSummary.append("\nSubcellular localization (by WolfPSort) - Organism: " + psortType + " summary:\n");
                HashMap<String, Integer> mapOrdSubcell = sortByValue(subcellTotalMap);
                for (String subcell : mapOrdSubcell.keySet()) {
                    sbSummary.append(String.format("\t%s: %s\n", subcell, mapOrdSubcell.get(subcell)));
                }

                summary.setSubcellLocalizations(mapOrdSubcell);
                MarkdownHelper.appendTableFromMap(outputmd, new String[]{"Subcellular localization", "Quantity"}, mapOrdSubcell);

                MarkdownHelper.appendLine(outputmd);
            }

            if (totalWithER > 0) {
                MarkdownHelper.appendHeader(outputmd, "E.R Retention domain summary", 3);

                sbSummary.append("\nE.R Retention domain summary:\n");
                HashMap<String, Integer> mapOrdErret = sortByValue(erretMap);
                int max = mapOrdErret.keySet().size() > 10 ? 10 : mapOrdErret.size();
                String[] domains = mapOrdErret.keySet().toArray(new String[]{});
                HashMap<String, Integer> mapErret = new HashMap<>();
                for (int i = 0; i < mapOrdErret.keySet().size(); i++) {
                    String domain = domains[i];
                    if (i < max) {
                        mapErret.put(domain, mapOrdErret.get(domain));
                    }
                    sbSummary.append(String.format("\t%s: %s\n", domain, mapOrdErret.get(domain)));
                }
                summary.setErret(mapOrdErret);

                MarkdownHelper.appendTableFromMap(outputmd, new String[]{"Domain", "Quantity"}, mapErret);
                if (domains.length > 10) {
                    outputmd.append("Only top 10\n").append("\n");
                }
                MarkdownHelper.appendLine(outputmd);
            }
            if (totalWithNglyc > 0) {

                MarkdownHelper.appendHeader(outputmd, "NGlyc domain summary", 3);

                sbSummary.append("\nNGlyc Retention domain summary:\n");
                HashMap<String, Integer> mapOrdNglyc = sortByValue(nglycMap);
                int max = (mapOrdNglyc.keySet().size() > 10) ? 10 : mapOrdNglyc.size();
                String[] domains = mapOrdNglyc.keySet().toArray(new String[]{});

                HashMap<String, Integer> mapNGlyc = new HashMap<>();
                for (int i = 0; i < mapOrdNglyc.keySet().size(); i++) {
                    String domain = domains[i];
                    if (i < max) {
                        mapNGlyc.put(domain, mapOrdNglyc.get(domain));
                    }
                    sbSummary.append(String.format("\t%s: %s\n", domain, mapOrdNglyc.get(domain)));
                }

                summary.setNglyc(mapOrdNglyc);
                MarkdownHelper.appendTableFromMap(outputmd, new String[]{"Domain", "Quantity"}, mapNGlyc);
                if (domains.length > 10) {
                    outputmd.append("Only top 10\n").append("\n");
                }

                MarkdownHelper.appendLine(outputmd);
            }
            //Generate output.md
            String[] headerMd = {
                Field.ID.toString(),
                Field.LENGTH.toString(),
                Field.KDA.toString(),
                Field.ISOELETRIC_POINT.toString(),
                Field.HYDROPATHY.toString(),
                Field.AROMATICITY.toString(),
                Field.SUBCELLULAR_LOCALIZATION.toString(),
                Field.TRANSMEMBRANE.toString(),
                Field.SIGNAL_P5.toString(),
                Field.ERR_TOTAL.toString(),
                Field.ERR_DOMAINS.toString(),
                Field.NGLYC_TOTAL.toString(),
                Field.NGLYC_DOMAINS.toString(),
                Field.HEADER.toString(),
                Field.BLAST_DESC.toString(),
                Field.GO_ANNOTATION.toString(),
                Field.INTERPRO_ANNOTATION.toString(),
                Field.PFAM_ANNOTATION.toString(),
                Field.PANTHER_ANNOTATION.toString()
            };
            Integer[] alignMd = {
                LEFT, RIGHT, RIGHT, RIGHT, RIGHT, RIGHT, CENTER, CENTER, CENTER, RIGHT, LEFT, RIGHT, LEFT, RIGHT, RIGHT, LEFT, LEFT, LEFT, LEFT};

            ArrayList<String[]> dataOutpumd = new ArrayList<>();
            int totalOutmod = 0;
            for (Protein protein : proteins) {
                try {
                    dataOutpumd.add(new String[]{
                        protein.getId(),
                        protein.getLength().toString(),
                        protein.getKdaStr(),
                        protein.getIsoeletricPointAvgStr(),
                        protein.getHydropathyStr(),
                        protein.getAromaticityStr(),
                        protein.getSubcellularLocalization(),
                        protein.getTransmembrane().toString(),
                        protein.getSignalp5(),
                        protein.getErretTotal().toString(),
                        protein.getErretDomainsAsString(),
                        protein.getnGlycTotal().toString(),
                        protein.getnGlycDomainsAsString(),
                        protein.getHeader(),
                        protein.getBlastHit(),
                        protein.getCleanFullGO(),
                        protein.getCleanInterpro(),
                        protein.getCleanPfam(),
                        protein.getCleanPanther()

                    });
                    totalOutmod++;

                } catch (CompoundNotFoundException ex) {
                    System.err.println("It is not possible to calculate molecular mass, isoelectric point,"
                            + " aromaticity and hydropathy of the protein" + protein.getId() + ". "
                            + "The sequence appears to be in error, check the contents of the FASTA file "
                            + "and run the program again. Check for non-amino acid characters with X and *.");
                    ex.printStackTrace();
                    System.exit(1);
                }
                if (totalOutmod > 25) {
                    break;
                }
            }

            createFile(sbSummary.toString(), "fast-protein-summary.txt");

            StringBuilder sbTxt = new StringBuilder();

            LinkedHashMap<String, String> outMdfiles = new LinkedHashMap<>();

            for (Output output : out) {

                String finalOut = "";
                //Insert Header
                if (output == Output.csv || output == Output.tsv) {
                    StringBuilder sbOut = new StringBuilder();
                    sbOut.append(String.join(output.getSeparator(),
                            Field.ID.toString(),
                            Field.LENGTH.toString(),
                            Field.KDA.toString(),
                            Field.ISOELETRIC_POINT.toString(),
                            Field.HYDROPATHY.toString(),
                            Field.AROMATICITY.toString(),
                            Field.SUBCELLULAR_LOCALIZATION.toString(),
                            Field.TRANSMEMBRANE.toString(),
                            Field.SIGNAL_P5.toString(),
                            Field.ERR_TOTAL.toString(),
                            Field.ERR_DOMAINS.toString(),
                            Field.NGLYC_TOTAL.toString(),
                            Field.NGLYC_DOMAINS.toString(),
                            Field.HEADER.toString(),
                            Field.BLAST_DESC.toString(),
                            Field.GO_ANNOTATION.toString(),
                            Field.INTERPRO_ANNOTATION.toString(),
                            Field.PFAM_ANNOTATION.toString(),
                            Field.PANTHER_ANNOTATION.toString(),
                            Field.SEQUENCE.toString()
                    ));
                    sbOut.append("\n");
                    for (Protein protein : proteins) {
                        try {
                            sbOut.append(String.join(output.separator,
                                    protein.getId(),
                                    protein.getLength().toString(),
                                    protein.getKdaStr(),
                                    protein.getIsoeletricPointAvgStr(),
                                    protein.getHydropathyStr(),
                                    protein.getAromaticityStr(),
                                    protein.getSubcellularLocalization(),
                                    protein.getTransmembrane().toString(),
                                    protein.getSignalp5(),
                                    protein.getErretTotal().toString(),
                                    protein.getErretDomainsAsString(),
                                    protein.getnGlycTotal().toString(),
                                    protein.getnGlycDomainsAsString(),
                                    protein.getHeader(),
                                    protein.getBlastHit(),
                                    protein.getCleanFullGO(),
                                    protein.getCleanInterpro(),
                                    protein.getCleanPfam(),
                                    protein.getCleanPanther(),
                                    protein.getSequence()));

                            sbOut.append("\n");
                        } catch (CompoundNotFoundException ex) {
                            System.err.println("It is not possible to calculate molecular mass, isoelectric "
                                    + "point, aromaticity and hydropathy of the protein" + protein.getId() + ". "
                                    + "The sequence appears to be in error, check the contents of the FASTA "
                                    + "file and run the program again. Check for non-amino acid characters with X and *.");
                            ex.printStackTrace();
                            System.exit(1);
                        }
                    }
                    finalOut = sbOut.toString();

                } else if (output == Output.txt) {

                    sbTxt.append(String.join(" ",
                            StringUtils.rightPad(Field.ID.toString(), mapSize.get(Field.ID), ' '),
                            StringUtils.leftPad(Field.LENGTH.toString(), mapSize.get(Field.LENGTH), ' '),
                            StringUtils.leftPad(Field.KDA.toString(), mapSize.get(Field.KDA), ' '),
                            StringUtils.leftPad(Field.ISOELETRIC_POINT.toString(), mapSize.get(Field.ISOELETRIC_POINT), ' '),
                            StringUtils.leftPad(Field.HYDROPATHY.toString(), mapSize.get(Field.HYDROPATHY), ' '),
                            StringUtils.leftPad(Field.AROMATICITY.toString(), mapSize.get(Field.AROMATICITY), ' '),
                            StringUtils.leftPad(Field.SUBCELLULAR_LOCALIZATION.toString(), mapSize.get(Field.SUBCELLULAR_LOCALIZATION), ' '),
                            StringUtils.leftPad(Field.TRANSMEMBRANE.toString(), mapSize.get(Field.TRANSMEMBRANE), ' '),
                            StringUtils.center(Field.SIGNAL_P5.toString(), mapSize.get(Field.SIGNAL_P5), ' '),
                            StringUtils.leftPad(Field.ERR_TOTAL.toString(), mapSize.get(Field.ERR_TOTAL), ' '),
                            StringUtils.rightPad(Field.ERR_DOMAINS.toString(), mapSize.get(Field.ERR_DOMAINS), ' '),
                            StringUtils.leftPad(Field.NGLYC_TOTAL.toString(), mapSize.get(Field.NGLYC_TOTAL), ' '),
                            StringUtils.rightPad(Field.NGLYC_DOMAINS.toString(), mapSize.get(Field.NGLYC_DOMAINS), ' '),
                            StringUtils.rightPad(Field.HEADER.toString(), mapSize.get(Field.HEADER), ' '),
                            StringUtils.rightPad(Field.BLAST_DESC.toString(), mapSize.get(Field.BLAST_DESC), ' '),
                            StringUtils.rightPad(Field.GO_ANNOTATION.toString(), mapSize.get(Field.GO_ANNOTATION), ' '),
                            StringUtils.rightPad(Field.INTERPRO_ANNOTATION.toString(), mapSize.get(Field.INTERPRO_ANNOTATION), ' '),
                            StringUtils.rightPad(Field.PFAM_ANNOTATION.toString(), mapSize.get(Field.PFAM_ANNOTATION), ' '),
                            StringUtils.rightPad(Field.PANTHER_ANNOTATION.toString(), mapSize.get(Field.PANTHER_ANNOTATION), ' '),
                            Field.SEQUENCE.toString()
                    ));
                    sbTxt.append("\n");
                    for (Protein protein : proteins) {
                        try {
                            sbTxt.append(String.join(" ", (StringUtils.rightPad(protein.getId(), mapSize.get(Field.ID), ' ')),
                                    (StringUtils.leftPad(protein.getLength().toString(), mapSize.get(Field.LENGTH), ' ')),
                                    (StringUtils.leftPad(protein.getKdaStr(), mapSize.get(Field.KDA), ' ')),
                                    (StringUtils.leftPad(protein.getIsoeletricPointAvgStr(), mapSize.get(Field.ISOELETRIC_POINT), ' ')),
                                    (StringUtils.leftPad(protein.getHydropathyStr(), mapSize.get(Field.HYDROPATHY), ' ')),
                                    (StringUtils.leftPad(protein.getAromaticityStr(), mapSize.get(Field.AROMATICITY), ' ')),
                                    (StringUtils.leftPad(protein.getSubcellularLocalization(), mapSize.get(Field.SUBCELLULAR_LOCALIZATION), ' ')),
                                    (StringUtils.leftPad(protein.getTransmembrane().toString(), mapSize.get(Field.TRANSMEMBRANE), ' ')),
                                    (StringUtils.center(protein.getSignalp5(), mapSize.get(Field.SIGNAL_P5), ' ')),
                                    (StringUtils.leftPad(protein.getErretTotal().toString(), mapSize.get(Field.ERR_TOTAL), ' ')),
                                    (StringUtils.rightPad(protein.getErretDomainsAsString(), mapSize.get(Field.ERR_DOMAINS), ' ')),
                                    (StringUtils.leftPad(protein.getnGlycTotal().toString(), mapSize.get(Field.NGLYC_TOTAL), ' ')),
                                    (StringUtils.rightPad(protein.getnGlycDomainsAsString(), mapSize.get(Field.NGLYC_DOMAINS), ' ')),
                                    (StringUtils.rightPad(protein.getHeader(), mapSize.get(Field.HEADER), ' ')),
                                    (StringUtils.rightPad(protein.getBlastHit(), mapSize.get(Field.BLAST_DESC), ' ')),
                                    (StringUtils.rightPad(protein.getCleanFullGO(), mapSize.get(Field.GO_ANNOTATION), ' ')),
                                    (StringUtils.rightPad(protein.getCleanInterpro(), mapSize.get(Field.INTERPRO_ANNOTATION), ' ')),
                                    (StringUtils.rightPad(protein.getCleanPfam(), mapSize.get(Field.PFAM_ANNOTATION), ' ')),
                                    (StringUtils.rightPad(protein.getCleanPanther(), mapSize.get(Field.PANTHER_ANNOTATION), ' ')),
                                    protein.getSequence()
                            )
                            );

                            sbTxt.append("\n");
                        } catch (CompoundNotFoundException ex) {
                            System.err.println("It is not possible to calculate molecular mass, isoelectric point,"
                                    + " aromaticity and hydropathy of the protein" + protein.getId() + ". "
                                    + "The sequence appears to be in error, check the contents of the FASTA file "
                                    + "and run the program again. Check for non-amino acid characters with X and *.");
                            ex.printStackTrace();
                            System.exit(1);
                        }
                    }
                    finalOut = sbTxt.toString();
                } else {
                    StringBuilder sbSep = new StringBuilder();

                    for (Protein protein : proteins) {
                        try {
                            sbSep.append(String.format("%s: %s\n", Field.ID.toString(), protein.getId()));
                            sbSep.append(String.format("%s: %s\n", Field.LENGTH.toString(), protein.getLength().toString()));
                            sbSep.append(String.format("%s: %s\n", Field.KDA.toString(), protein.getKdaStr()));
                            sbSep.append(String.format("%s: %s\n", Field.ISOELETRIC_POINT.toString(), protein.getIsoeletricPointAvgStr()));
                            sbSep.append(String.format("%s: %s\n", Field.HYDROPATHY.toString(), protein.getHydropathyStr()));
                            sbSep.append(String.format("%s: %s\n", Field.AROMATICITY.toString(), protein.getAromaticityStr()));
                            sbSep.append(String.format("%s: %s\n", Field.SUBCELLULAR_LOCALIZATION.toString(), protein.getSubcellularLocalization()));
                            sbSep.append(String.format("%s: %s\n", Field.TRANSMEMBRANE.toString(), protein.getTransmembrane().toString()));
                            sbSep.append(String.format("%s: %s\n", Field.SIGNAL_P5.toString(), protein.getSignalp5()));
                            sbSep.append(String.format("%s: %s\n", Field.ERR_TOTAL.toString(), protein.getErretTotal().toString()));
                            sbSep.append(String.format("%s: %s\n", Field.ERR_DOMAINS.toString(), protein.getErretDomainsAsString()));
                            sbSep.append(String.format("%s: %s\n", Field.NGLYC_TOTAL.toString(), protein.getnGlycTotal().toString()));
                            sbSep.append(String.format("%s: %s\n", Field.NGLYC_DOMAINS.toString(), protein.getnGlycDomainsAsString()));
                            sbSep.append(String.format("%s: %s\n", Field.HEADER.toString(), protein.getHeader()));
                            sbSep.append(String.format("%s: %s\n", Field.BLAST_DESC.toString(), protein.getBlastHit()));
                            sbSep.append(String.format("%s: %s\n", Field.GO_ANNOTATION.toString(), protein.getCleanFullGO()));
                            sbSep.append(String.format("%s: %s\n", Field.INTERPRO_ANNOTATION.toString(), protein.getCleanInterpro()));
                            sbSep.append(String.format("%s: %s\n", Field.PFAM_ANNOTATION.toString(), protein.getCleanPfam()));
                            sbSep.append(String.format("%s: %s\n", Field.PANTHER_ANNOTATION.toString(), protein.getCleanPanther()));
                            sbSep.append(String.format("%s: %s\n", Field.SEQUENCE.toString(), protein.getSequence()));
                            sbSep.append("\n");
                        } catch (CompoundNotFoundException ex) {
                            System.err.println("It is not possible to calculate molecular mass, isoelectric point,"
                                    + " aromaticity and hydropathy of the protein" + protein.getId() + ". "
                                    + "The sequence appears to be in error, check the contents of the FASTA "
                                    + "file and run the program again. Check for non-amino acid characters with X and *.");
                            ex.printStackTrace();
                            System.exit(1);
                        }
                    }
                    finalOut = sbSep.toString();
                }

                FileWriter fw = null;
                try {
                    String file = String.format("%s-%s%s", fileOutput, output, output.getExtension());

                    createFile(finalOut, file);
                    outMdfiles.put(file, MarkdownHelper.getLink("Download", file));

                } catch (IOException ex) {
                    System.err.println("An error occurred while generating the output file " + fileOutput + ".\n"
                            + "Please, check write permissions and run again.");
                    try {
                        fw.close();
                    } catch (IOException ex2) {
                        System.out.println("An error occurred while closing the " + fileOutput + " file.");
                    }
                }

            }

            if (interpro) {
                MarkdownHelper.appendHeader(outputmd, "Gene Ontology", 2);
                if (!goF.isEmpty()) {
                    MarkdownHelper.appendHeader(outputmd, "Molecular Function", 4);
                    MarkdownHelper.appendTable(outputmd, goHeader, goHeaderAlignments, goF);
                }
                if (!goC.isEmpty()) {
                    MarkdownHelper.appendHeader(outputmd, "Cellular Component", 4);
                    MarkdownHelper.appendTable(outputmd, goHeader, goHeaderAlignments, goC);
                }

                if (!goP.isEmpty()) {
                    MarkdownHelper.appendHeader(outputmd, "Biological Process", 4);
                    MarkdownHelper.appendTable(outputmd, goHeader, goHeaderAlignments, goP);
                }
                MarkdownHelper.appendLine(outputmd);
            }

            MarkdownHelper.appendTable(outputmd, headerMd, alignMd, dataOutpumd);
            if (proteins.size() > 25) {
                MarkdownHelper.appendHeader(outputmd, "Only top 25 proteins\n", 5);
            }

            MarkdownHelper.appendLine(outputmd);

            MarkdownHelper.appendHeader(outputmd, "Generated files", 3);

            outMdfiles.put("wolfpsort.txt", MarkdownHelper.getLink("Download", "wolfpsort.txt"));
            outMdfiles.put("tmhmm2.txt", MarkdownHelper.getLink("Download", "tmhmm2.txt"));
            outMdfiles.put("signalp5.txt", MarkdownHelper.getLink("Download", "signalp5.txt"));

            MarkdownHelper.appendTableFromMap(outputmd, new String[]{"File", "Link"}, outMdfiles);

            outputmd.append("\n");
            outputmd.append("\n");
            MarkdownHelper.appendHeader(outputmd, "Do you have a question or tips? Please contact us! E-mail: renato.simoes@ifsc.edu.br", 5);

            outputmd.append("Generated time: " + new Date());
            createFile(outputmd.toString(), "output.md");

            createFile(summary.getJSON(), "fast-protein-summary.json");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("An error occurred when saving some file. " + ex.getMessage());
        }

    }

    // function to sort hashmap by values
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer>> list
                = new LinkedList<Map.Entry<String, Integer>>(
                        hm.entrySet());

        // Sort the list using lambda expression
        Collections.sort(
                list,
                (i1,
                        i2) -> i2.getValue().compareTo(i1.getValue()));

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp
                = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

}
