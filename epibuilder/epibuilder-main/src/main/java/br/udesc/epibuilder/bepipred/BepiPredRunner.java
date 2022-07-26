/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.epibuilder.bepipred;

import br.ufsc.epibuilder.Parameters;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 *
 * @author renato
 */
public class BepiPredRunner {

    public static File getBepiPred2Results(File fastaSequences, Parameters.BEPIPRED2_TYPE type) throws Exception {
        String s = null;
        String bepipredoutput = String.format("%s/%s-epibuilder-bepipred2_%s.csv", Parameters.DESTINATION_FOLDER, Parameters.BASENAME, type.getName());
        String[] cmd = null;
        if (type == Parameters.BEPIPRED2_TYPE.BCELL_STANDALONE) {
            cmd = new String[]{
                "python", Parameters.BEPIPRED2_BCELL_STANDALONE_PATH,
                "-m", "BepiPred-2.0",
                "-f", fastaSequences.getAbsolutePath()};
        } else if (type == Parameters.BEPIPRED2_TYPE.LOCAL) {
            System.out.println("Executing BepiPred-2.0");
            cmd = new String[]{
                Parameters.BEPIPRED2_PATH, fastaSequences.getAbsolutePath()};
        }
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            
            StringBuilder sb = new StringBuilder();
            while ((s = stdInput.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }
            while ((s = stdError.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }
            FileWriter fw = new FileWriter(bepipredoutput);
            fw.append(sb.toString());
            fw.close();
            
            return new File(bepipredoutput);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public static void main(String[] args) {
        String s = null;
        String[] cmd = null;
        cmd = new String[]{Parameters.BLASTP_PATH, "--help"};
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            StringBuilder sb = new StringBuilder();
            while ((s = stdInput.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }
            while ((s = stdError.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }
            FileWriter fw = new FileWriter("teste-res.txt");
            fw.append(sb.toString());
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
