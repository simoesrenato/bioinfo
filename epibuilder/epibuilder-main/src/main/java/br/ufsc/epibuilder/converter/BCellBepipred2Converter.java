/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsc.epibuilder.converter;

import br.udesc.epibuilder.bepipred.BepiPredOnlineRunner;
import br.ufsc.epibuilder.Parameters;
import br.ufsc.epibuilder.exceptions.InputException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author renato
 */
public class BCellBepipred2Converter {

    
    public static ArrayList<ProteinConverter> getAllByJobID() throws Exception {
        File bepipredFile = BepiPredOnlineRunner.getBepiPredByJobId(Parameters.BEPIPRED2_JOBID);
        return getAll(bepipredFile, Parameters.BEPIPRED2_TYPE.ONLINE);
        
    }
    
    public static ArrayList<ProteinConverter> getAll(File f, Parameters.BEPIPRED2_TYPE inputType) throws Exception {
        if (inputType == Parameters.BEPIPRED2_TYPE.ONLINE ) {
            try {

                return getBepipred2FromCsv(f);
            } catch (InputException ex) {
                throw new Exception(ex);
            }
        } if(inputType== Parameters.BEPIPRED2_TYPE.LOCAL){
            try {
                return getBepipred2FromLocalCsv(f);
            } catch (InputException ex) {
                ex.printStackTrace();
                throw new Exception(ex);
            }
        }else{
            return getBepipred2FromTsvBcell(f);
        }
        
    }
    /**
     * Convert a bcell bepipred2 file
     * 
     * @param bcellFile
     * @return
     * @throws Exception 
     */
    public static ArrayList<ProteinConverter> getBepipred2FromTsvBcell(File bcellFile) throws Exception{
        Scanner s = new Scanner(bcellFile);

        ArrayList<ProteinConverter> proteins = new ArrayList<>();
        ProteinConverter protein = null;

        try {
            while (s.hasNext()) {
                String val = s.nextLine();
                if (val.startsWith("input")) {
                    String id = val.replace("input: ", "").trim();
                    protein = new ProteinConverter(id);
                    proteins.add(protein);
                    while (!val.startsWith("Position")) {
                        val = s.nextLine();
                    }
                } else {
                    String[] values = val.split("\t");
                    protein.addAmino(Integer.parseInt(values[0]), values[1], Double.parseDouble(values[2]));
                }
            }
        } catch (Exception e) {
            throw new InputException(e);
        }
        return proteins;
    }

    /**
     * Convert a Bepipred2 file
     * 
     * @param csvFile
     * @return
     * @throws FileNotFoundException
     * @throws InputException 
     */
    public static ArrayList<ProteinConverter> getBepipred2FromCsv(File csvFile) throws FileNotFoundException, InputException {
        Scanner s = new Scanner(csvFile);
        ArrayList<ProteinConverter> proteins = new ArrayList<>();
        ProteinConverter protein = new ProteinConverter("");
        s.nextLine();
        if(Parameters.BEPIPRED2_INPUT == Parameters.BEPIPRED2_TYPE.LOCAL){
            s.nextLine();
        }
        while (s.hasNext()) {
            try {
                String val = s.nextLine();
                String[] res = val.split(",");
                String id = res[0];
                if (!protein.getId().equals(id)) {
                    protein = new ProteinConverter(id);
                    proteins.add(protein);
                }
                protein.addAmino(Integer.parseInt(res[1]) - 1, res[2], Double.parseDouble(res[8]));
            } catch (Exception e) {
                throw new InputException(e);
            }
        }
        return proteins;
    }
    /**
     * Convert a Bepipred2 file
     *
     * @param csvFile
     * @return
     * @throws FileNotFoundException
     * @throws InputException
     */
    public static ArrayList<ProteinConverter> getBepipred2FromLocalCsv(File csvFile) throws FileNotFoundException, InputException {
        Scanner s = new Scanner(csvFile);
        ArrayList<ProteinConverter> proteins = new ArrayList<>();
        ProteinConverter protein = new ProteinConverter("");
        s.nextLine();
        s.nextLine();
        while (s.hasNext()) {
            try {
                String val = s.nextLine();
                System.out.println("Processando linha:" + val);
                if(val.contains("/") || val.startsWith(" ")) {
                    System.out.println("Ignorando linha: "+val);
                }else{
                    System.out.println("Linha OK");
                    String[] res = val.split("\t");
                    String id = res[0];
                    if (!protein.getId().equals(id)) {
                        protein = new ProteinConverter(id);
                        proteins.add(protein);
                    }
                    protein.addAmino(Integer.parseInt(res[1]) - 1, res[2], Double.parseDouble(res[7]));

                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new InputException(e);
            }
        }
        return proteins;
    }
    public static void main(String[] args) throws FileNotFoundException, InputException {
        ArrayList<ProteinConverter> proteins = getBepipred2FromLocalCsv(new File("/bioinformatic/bepipred.csv"));
        System.out.println(proteins.size());
    }
}
