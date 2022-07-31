package br.ufsc.epibuilder;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "EpiBuilder-1.1", requiredOptionMarker = '*', abbreviateSynopsis = true,
        description = "Does something useful.", version = "1.0", sortOptions = false)
public class BiolibMain implements Callable<Integer> {
   
    @Option(names = {"-s", "--source"}, required = true, description = "Source of file (choose one): ${COMPLETION-CANDIDATES}.", defaultValue = "fasta")
    Source source;
    @Option(names = {"-i", "--input"}, required = true, description = "Input file")
    File input;
    @Option(names = {"-min", "--min-length"}, description = "Minimum epitope length. Default: ${DEFAULT-VALUE}", defaultValue = "10")
    Integer minLength;
    @Option(names = {"-max", "--max-length"}, description = "Max epitope length. Default: ${DEFAULT-VALUE}", defaultValue = "15")
    Integer maxLength;
    @Option(names = {"-f", "--features"}, description = "Peptides chemical properties: ${COMPLETION-CANDIDATES}")
    Feature[] features;
    @Option(names = {"-b", "--basename"}, description = "The common base name for the initial output files. Default: ${DEFAULT-VALUE}", defaultValue = "epibuilder")
    String basename;
    @Option(names = {"-search", "--search"}, description = "Method of search in the given proteome(s): ${COMPLETION-CANDIDATES}. Default: ${DEFAULT-VALUE}", defaultValue = "none")
    Search search;
    @Option(names = {"-bt", "--task"}, description = "Blast -task argment(blastp-short, blastp, blastp-fast). Default: ${DEFAULT-VALUE}", defaultValue = "blastp-short")
    String blastTask;
    @Option(names = {"-bi", "--ident"}, description = "Minimum identity cutoff. Default: ${DEFAULT-VALUE}", defaultValue = "90")
    Integer blastIdentity;
    @Option(names = {"-bc", "--cover"}, description = "Minimum cover cutoff. Default: ${DEFAULT-VALUE}", defaultValue = "90")
    Integer blastCover;
    @Option(names = {"-ws", "--word-size"}, description = "Word-size. Default: ${DEFAULT-VALUE}", defaultValue = "4")
    Integer blastWordsize;
    @Option(names = {"-p1", "--proteome1"}, description = "Proteome 1 file")
    File proteome1;
    @Option(names = {"-p1a", "--proteome1-alias"}, description = "Proteome 1 alias - appears in the report file")
    String proteome1Alias;
    @Option(names = {"-p2", "--proteome2"}, description = "Proteome 2 file")
    File proteome2;
    @Option(names = {"-p2a", "--proteome2-alias"}, description = "Proteome 2 alias - appears in the report file")
    String proteome2Alias;
    @Option(names = {"-p3", "--proteome3"}, description = "Proteome 3 file")
    File proteome3;
    @Option(names = {"-p3a", "--proteome3-alias"}, description = "Proteome 3 alias - appears in the report file")
    String proteome3Alias;
    @Option(names = {"-p4", "--proteome4"}, description = "Proteome 4 file")
    File proteome4;
    @Option(names = {"-p4a", "--proteome4-alias"}, description = "Proteome 4 alias - appears in the report file")
    String proteome4Alias;
    @Option(names = {"-p5", "--proteome5"}, description = "Proteome 5 file")
    File proteome5;
    @Option(names = {"-p5a", "--proteome5-alias"}, description = "Proteome 5 alias - appears in the report file")
    String proteome5Alias;

    @Override
    public Integer call() {

        return 0;
    }

    public static void main(String... args) {
        System.exit(new CommandLine(new BiolibMain()).execute(args));
    }

    private enum Source {
        fasta("FASTA file (protein). This option calls BepiPred-3.0 and process it.", true),
        b3online("BepiPred-3 - online (csv)", false),
        b3local("BepiPred-3 - local (csv)", false),
        b2online("BepiPred-2 - online (csv)", false),
        b2local("BepiPred-2 - local (csv)", false),
        bcell("BCell stand alone - BepiPred2 - local (csv)", false);

        private String description;
        private boolean defaultValue;

        private Source(String description, boolean defaultValue) {
            this.description = description;
            this.defaultValue = defaultValue;
        }

        public String getDescription() {
            return description;
        }

        public boolean isDefaultValue() {
            return defaultValue;
        }
        
        public String getAll(){
            String res = "";
            for (Source value : Source.values()) {
                res += String.format("\t%s %s\n", value.toString(), (value.isDefaultValue()?"*":""));
            }
            return res;
        }

    }

    private enum Feature {
        emini,
        kolaskar,
        chou,
        karplus,
        parker
    }

    private enum Search {
        direct,
        blastp,
        none
    }

}
