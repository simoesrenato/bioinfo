package br.ufsc.epibuilder;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

public class BepiPred3Runner {
    public static void execute() throws Exception {
        try{
            CommandRunner.run(String.format("python3 bepipred3_CLI.py -o %s-bepipred3 -pred vt_pred -add_seq_len -i %s",Parameters.BASENAME, Parameters.FASTA.getAbsolutePath()));
            Parameters.BEPIPRED_FILE = new File(String.format("%s-bepipred3/raw_output.csv", Parameters.BASENAME));
            if(!Parameters.BEPIPRED_FILE.exists()){
                throw new Exception("Empty BepiPred3 file, check your original FASTA");
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception("Error trying to execute BepiPred3");
        }
    }
}
