package br.edu.ifsc.bioinfo.fast.protein;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;

/**
 *
 * @author renato
 */
public class FastProteinCalculator {

    private static Pattern patternErret = Pattern.compile("[KRHQSA][DENQ][E][L]");
    private static Pattern patternNglyc = Pattern.compile("[N][ARNDBCEQZGHILKMFSTWYV][ST]");

    private enum Output {
        CSV(","), TSV("\t"), TXT("txt");
        private String separator;

        private Output(String separator) {
            this.separator = separator;
        }

        public String getSeparator() {
            return separator;
        }
    }

    private enum Field {

        ID("Id"),
        HEADER("Header"),
        LENGTH("Length"),
        KDA("kDa"),
        ISOELETRIC_POINT("Isoelectric Point"),
        HYDROPATHY("Hydropathy"),
        AROMATICITY("Aromaticity"),
        ERR_TOTAL("E.R Retention Total"),
        ERR_DOMAINS("E.R Retention Domains"),
        NGLYC_TOTAL("NGlyc Total"),
        NGLYC_DOMAINS("NGlyc Domains"),
        SEQUENCE("Sequence");

        private String description;

        private Field(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }

    }

    private static void replaceSize(String value, Field field, HashMap<Field, Integer> mapSize) {
        int maxSize = mapSize.get(field);
        int length = value.length();
        if (length > maxSize) {
            mapSize.put(field, length);
        }
    }

    private static void showHelp() {
        System.out.println("FastProtein commands manual");
        System.out.println("USAGE");
        System.out.println("\tjava -jar fast-protein.jar [<fastaFile>] [-h] [-t <value>] [-out <output_file>]");
        System.out.println("");
        System.out.println("DESCRIPTION");
        System.out.println("\tFastProtein 1.0");
        System.out.println("");
        System.out.println("ARGUMENTS\n"
                + "\t*<fastaFile> - Input FASTA file (required as 1st argument)\n"
                + "\t-h or --help (optional)\n"
                + "\t\tPrint USAGE, DESCRIPTION and ARGUMENTS; ignore all other parameters\n"
                + "\t-v or -version (optional)\n"
                + "\t\tPrint version number;  ignore all other arguments\n"
                + "\t-t <options> (optional)\n"
                + "\t\tType of output format options:\n"
                + "\t\t\tcsv - Comma separated values,\n"
                + "\t\t\ttsv - Tab separated values,\n"
                + "\t\t\ttxt - Formatted output (default)\n"
                + "\t-out <output_file> (optional) \n"
                + "\t\tOutput file name - if not informed, the output will be displayed only in the console."
        );

        System.out.println("");
    }

    public static void run(String[] args) {
        if (args.length == 0) {
            System.out.println("Fast-protein commands manual");
            System.out.println("Command");
            System.out.println("\tjava -jar fast-protein.jar [-h] [-f <fastaFile>] [-t <value>] [-out <output_file>]");
            System.out.println("Help");
            System.out.println("\tjava -jar fast-protein.jar -h");
            System.exit(0);
        } else {
            for (String arg : args) {
                if (arg.equals("-h") || arg.equals("--help")) {
                    showHelp();
                    System.exit(0);
                }
                if (arg.equals("-v") || arg.equals("-version")) {
                    System.out.println("Fast-protein 1.0");
                    System.exit(0);
                }
            }
        }

        Output out = Output.TXT;
        boolean console = true;

        String fileSource = "";
        String fileOutput = "";

        fileSource = args[0];
        LinkedHashMap<String, ProteinSequence> resp = null;
        try {
            resp = FastaReaderHelper.readFastaProteinSequence(new File(fileSource));
        } catch (IOException ex) {
            System.err.println("Invalid FASTA file. Please check your informed file " + fileSource);
            ex.printStackTrace();
            System.exit(1);
        }

        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("-t")) {
                i++;
                if (i < args.length) {
                    String typeOutput = args[i];
                    if (args[i] != null) {
                        switch (typeOutput) {
                            case "csv":
                                out = Output.CSV;
                                break;
                            case "tsv":
                                out = Output.TSV;
                                break;
                            case "txt":
                                out = Output.TXT;
                                break;
                            default:
                                System.out.println("Invalid -t option (0-csv, 1-tsv or 2-txt).");
                                System.out.println("For more information use -h or -help.");
                                System.exit(0);
                        }
                    }
                } else {
                    System.out.println("Enter a value for -t option (0-csv, 1-tsv or 2-txt).");
                    System.out.println("For more information use -h or -help.");
                    System.exit(0);
                }
            }
            if (args[i].equals("-out")) {
                console = false;
                i++;
                if (i < args.length) {
                    fileOutput = args[i];
                } else {
                    System.out.println("File not informed using -o option ");
                    System.out.println("For more information use -h or -help.");
                    System.exit(0);
                }
            }

        }

        System.out.println("#Fast-protein Software 1.0");
        System.out.println("#Developed by Renato Simões - renato.simoes@ifsc.edu.br");
        System.out.println("#Protein information software");
        System.out.println("#Obs:");
        System.out.println("#Pattern ER Retention [KRHQSA][DENQ][E][L] based on PROSITE-PS00014 https://prosite.expasy.org/  ");
        System.out.println("#Pattern N-Glyc [N][Xaa(except P)][ST] Xaa = any aminoacid");

        System.out.println("");

        ArrayList<Protein> proteins = new ArrayList<>();

        for (ProteinSequence s : resp.values()) {
            String id = s.getAccession().getID();
            String proteinId = id.split(" ")[0];

            String sequence = s.getSequenceAsString().toUpperCase();

            //Processing ER Retention
            Matcher matcher = patternErret.matcher(sequence);
            int erretTotalPerProtein = 0;
            String erretDomain = "";
            while (matcher.find()) {
                erretDomain += String.format("%s[%d-%d],", matcher.group(), matcher.start(), matcher.end());
                erretTotalPerProtein++;
            }
            if (erretTotalPerProtein > 0) {
                erretDomain = erretDomain.substring(0, erretDomain.length() - 1);
            }
            //Processing Nglyc domain
            Matcher nGlycmatcher = patternNglyc.matcher(sequence);
            int nglycTotalPerProtein = 0;
            String nglycDomain = "";
            while (nGlycmatcher.find()) {
                nglycDomain += String.format("%s[%d-%d],", nGlycmatcher.group(), nGlycmatcher.start(), nGlycmatcher.end());
                nglycTotalPerProtein++;
            }
            if (nglycTotalPerProtein > 0) {
                nglycDomain = nglycDomain.substring(0, nglycDomain.length() - 1);
            }

            Protein protein = new Protein();
            protein.setId(proteinId);
            protein.setHeader(s.getAccession().getID());
            protein.setSequence(sequence);
            protein.setErretTotal(erretTotalPerProtein);
            protein.setErretDomains(erretDomain);
            protein.setnGlycTotal(nglycTotalPerProtein);
            protein.setnGlycDomains(nglycDomain);
            proteins.add(protein);
        }
        //Calculate column size to formated report
        HashMap<Field, Integer> mapSize = new HashMap<>();
        for (Field value : Field.values()) {
            mapSize.put(value, value.toString().length());
        }
        for (Field value : Field.values()) {
            replaceSize(value.toString(), value, mapSize);
        }
        for (Protein protein : proteins) {
            try {
                replaceSize(protein.getId(), Field.ID, mapSize);
                replaceSize(protein.getLength().toString(), Field.LENGTH, mapSize);

                replaceSize(protein.getKdaStr(), Field.KDA, mapSize);
                replaceSize(protein.getIsoeletricPointAvgStr(), Field.ISOELETRIC_POINT, mapSize);
                replaceSize(protein.getHydropathyStr(), Field.HYDROPATHY, mapSize);

                replaceSize(protein.getErretTotal().toString(), Field.ERR_TOTAL, mapSize);
                replaceSize(protein.getErretDomains(), Field.ERR_DOMAINS, mapSize);
                replaceSize(protein.getnGlycTotal().toString(), Field.NGLYC_TOTAL, mapSize);
                replaceSize(protein.getnGlycDomains(), Field.NGLYC_DOMAINS, mapSize);
                replaceSize(protein.getHeader(), Field.HEADER, mapSize);
                replaceSize(protein.getSequence(), Field.SEQUENCE, mapSize);
            } catch (CompoundNotFoundException ex) {
                System.err.println("It is not possible to calculate molecular mass, isoelectric point and hydropathy of the protein" + protein.getId() + ". The sequence appears to be in error, check the contents of the FASTA file and run the program again. Check for non-amino acid characters with X and *.");
                ex.printStackTrace();
                System.exit(1);
            }
        }

        String finalOut = "";
        //Insert Header
        if (out == Output.CSV || out == Output.TSV) {
            StringBuilder sbOut = new StringBuilder();
            sbOut.append(String.join(out.getSeparator(),
                    Field.ID.toString(),
                    Field.LENGTH.toString(),
                    Field.KDA.toString(),
                    Field.ISOELETRIC_POINT.toString(),
                    Field.HYDROPATHY.toString(),
                    Field.ERR_TOTAL.toString(),
                    Field.ERR_DOMAINS.toString(),
                    Field.NGLYC_TOTAL.toString(),
                    Field.NGLYC_DOMAINS.toString(),
                    Field.HEADER.toString(),
                    Field.SEQUENCE.toString()));
            sbOut.append("\n");
            for (Protein protein : proteins) {
                try {
                    sbOut.append(String.join(out.separator,
                            protein.getId(),
                            protein.getLength().toString(),
                            protein.getKdaStr(),
                            protein.getIsoeletricPointAvgStr(),
                            protein.getHydropathyStr(),
                            protein.getAromaticityStr(),
                            protein.getErretTotal().toString(),
                            protein.getErretDomains(),
                            protein.getnGlycTotal().toString(),
                            protein.getnGlycDomains(),
                            protein.getHeader(),
                            protein.getSequence()));
                } catch (CompoundNotFoundException ex) {
                    System.err.println("It is not possible to calculate molecular mass, isoelectric point and hydropathy of the protein" + protein.getId() + ". The sequence appears to be in error, check the contents of the FASTA file and run the program again. Check for non-amino acid characters with X and *.");
                    ex.printStackTrace();
                    System.exit(1);
                }
                sbOut.append("\n");

            }
            finalOut = sbOut.toString();

        } else {
            StringBuilder sbTxt = new StringBuilder();
            sbTxt.append(String.join(" ", StringUtils.rightPad(Field.ID.toString(), mapSize.get(Field.ID), ' '),
                    StringUtils.leftPad(Field.LENGTH.toString(), mapSize.get(Field.LENGTH), ' '),
                    StringUtils.leftPad(Field.KDA.toString(), mapSize.get(Field.KDA), ' '),
                    StringUtils.leftPad(Field.ISOELETRIC_POINT.toString(), mapSize.get(Field.ISOELETRIC_POINT), ' '),
                    StringUtils.leftPad(Field.HYDROPATHY.toString(), mapSize.get(Field.HYDROPATHY), ' '),
                    StringUtils.leftPad(Field.AROMATICITY.toString(), mapSize.get(Field.AROMATICITY), ' '),
                    StringUtils.leftPad(Field.ERR_TOTAL.toString(), mapSize.get(Field.ERR_TOTAL), ' '),
                    StringUtils.rightPad(Field.ERR_DOMAINS.toString(), mapSize.get(Field.ERR_DOMAINS), ' '),
                    StringUtils.leftPad(Field.NGLYC_TOTAL.toString(), mapSize.get(Field.NGLYC_TOTAL), ' '),
                    StringUtils.rightPad(Field.NGLYC_DOMAINS.toString(), mapSize.get(Field.NGLYC_DOMAINS), ' '),
                    StringUtils.rightPad(Field.HEADER.toString(), mapSize.get(Field.HEADER), ' '),
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
                            (StringUtils.leftPad(protein.getErretTotal().toString(), mapSize.get(Field.ERR_TOTAL), ' ')),
                            (StringUtils.rightPad(protein.getErretDomains(), mapSize.get(Field.ERR_DOMAINS), ' ')),
                            (StringUtils.leftPad(protein.getnGlycTotal().toString(), mapSize.get(Field.NGLYC_TOTAL), ' ')),
                            (StringUtils.rightPad(protein.getnGlycDomains(), mapSize.get(Field.NGLYC_DOMAINS), ' ')),
                            (StringUtils.rightPad(protein.getHeader(), mapSize.get(Field.HEADER), ' ')),
                            protein.getSequence()
                    )
                    );
                } catch (CompoundNotFoundException ex) {
                    System.err.println("It is not possible to calculate molecular mass, isoelectric point and hydropathy of the protein" + protein.getId() + ". The sequence appears to be in error, check the contents of the FASTA file and run the program again. Check for non-amino acid characters with X and *.");
                    ex.printStackTrace();
                    System.exit(1);
                }
                sbTxt.append("\n");
            }
            finalOut = sbTxt.toString();
        }
        System.out.println(finalOut);
        if (console) {
            System.out.println(finalOut);
        } else {
            FileWriter fw = null;
            try {
                File outfile = new File(fileOutput);
                String fout = outfile.getAbsolutePath();
                fw = new FileWriter(outfile);
                fw.append(finalOut);
                fw.close();
                System.out.println("Generated file: " + fout);
            } catch (IOException ex) {
                System.err.println("An error occurred while generating the output file " + fileOutput + ".\nPlease, check write permissions and run again.");
                try {
                    fw.close();
                } catch (IOException ex2) {
                    System.out.println("An error occurred while closing the " + fileOutput + " file.");
                }
            }
        }

    }

    public static void main(String[] args) throws Exception {
        run(args);
    }

    
}
