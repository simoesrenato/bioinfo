/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.bioinfo.fast.protein;

import br.edu.ifsc.bioinfo.fast.protein.conversor.BlastConverter;
import br.edu.ifsc.bioinfo.fast.protein.conversor.SignalP5Converter;
import br.edu.ifsc.bioinfo.fast.protein.conversor.WolfPsortConverter;
import picocli.CommandLine;
import java.io.File;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "FastProtein-1", requiredOptionMarker = '*', abbreviateSynopsis = true,
        description = "Fast and simple way to know more about your proteins! :)", version = "1.0", sortOptions = false)
public class Main implements Callable<Integer> {

    public enum FileType {
        csv, tsv
    }

    @CommandLine.Option(names = {"-i", "--input"}, required = true, description = "Input file")
    File input;
    @CommandLine.Option(names = {"-t", "--type", "--type_biolib"}, description = "Type of output format options (default is all selected):\n"
            + "\t\t\tcsv - Comma separated values,\n"
            + "\t\t\ttsv - Tab separated values,\n"
            + "\t\t\tsep - Separated protein,\n"
            + "\t\t\ttxt - Formatted output\n", defaultValue = "csv,tsv,sep,txt", split = ",")
    FastProteinCalculator.Output[] outputTypes;
    @CommandLine.Option(names = {"-s", "--subcell", "--subcellular_localization"}, description = "Type of organism for wolfpsort:\n"
            + "\t\t\tanimal - Animal\n"
            + "\t\t\tfungi - Fungi\n"
            + "\t\t\tplant - Plant\n"
            + "\t\t\tnone - No prediction\n", defaultValue = "animal")
    WolfPsortConverter.Type wolfpsortType;


    @CommandLine.Option(names = {"-sp", "--signalp", "--signal_peptide_organism"}, description = "Type of organism for SignalP-5:\n"
            + "\t\t\teuk - Eukarya\n"
            + "\t\t\tarch - Archaea\n"
            + "\t\t\tgram_pos - Gram-positive\n"
            + "\t\t\tgram_neg - Gram-negative\n"
            + "\t\t\tnone - No prediction\n", defaultValue = "euk")
    SignalP5Converter.Organism signalPorg;

    @CommandLine.Option(names = {"-o", "--output"}, description = "Output generated file", defaultValue = "fastprotein")
    String outputFile;
    
    @CommandLine.Option(names = {"-wb", "--web-blast"}, 
            description = "Blast remote search (NCBI Web Blast)", 
            defaultValue = "false")
    String blast;
    
    @CommandLine.Option(names = {"-ipr", "--interpro"}, 
            description = "Interproscan remote search (EBI-WP)", 
            defaultValue = "false")
    String interpro;
    
    @CommandLine.Option(names = {"-iprLimit", "--interproLimit"}, 
            description = "Interproscan remote search (EBI-WP) limit (max 1000)", 
            defaultValue = "500")
    int interproLimit;
    
    @CommandLine.Option(names = {"-db", "--blastdb"}, description = "Database for remote query:\n"
            + "\t\t\tswissprot - Swiss-Prot\n"
            + "\t\t\tnr - NR - Non-redundant\n",
            defaultValue = "swissprot")
    BlastConverter.Database blastDb;

    @CommandLine.Option(names = {"-bto", "--blast-timeout"}, 
                description = "Blast timeout in seconds", 
                defaultValue = "30")
    Integer timeout;
    
    @Override
    public Integer call() throws Exception {
        boolean blastBol = blast.equals("true");
        boolean interpro = this.interpro.equals("true");
        if(interproLimit>1000){
            System.out.println("Interpro limit set to max = 1000");
            interproLimit=1000;
        }
        FastProteinCalculator.run(input, outputTypes, outputFile, wolfpsortType, signalPorg, blastBol, blastDb, timeout, interpro, interproLimit);
        return 0;

    }

    public static void main(String... args) {
        System.exit(new CommandLine(new Main()).execute(args));

    }

}
