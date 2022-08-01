package br.ufsc.epibuilder;

import static br.ufsc.epibuilder.Parameters.MAX_LENGTH_BEPIPRED2;
import static br.ufsc.epibuilder.Parameters.MIN_LENGTH_BEPIPRED2;

import br.ufsc.epibuilder.entity.Proteome;
import br.ufsc.epibuilder.entity.SoftwareBcellEnum;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import static br.ufsc.epibuilder.Parameters.BEPIPRED_FILE;
import static br.ufsc.epibuilder.Parameters.THRESHOLD_BEPIPRED;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author renato
 */
public class Main {

    public static void main(String[] args) throws Exception {
        
        LinkedHashMap<String, String> map = new LinkedHashMap<>();


        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-f")) {
                i++;
                Parameters.BASENAME=args[i];
            }
            if (args[i].equals("-b")) {
                i++;
                if (!new File(args[i]).exists()) {
                    throw new Exception("File not found: " + args[i]);
                } else {
                    BEPIPRED_FILE = new File(args[i]);
                }

            } else if (args[i].equals("-o")) {
                Parameters.BEPIPRED_INPUT= Parameters.BEPIPRED_TYPE.ONLINE;
            } else if (args[i].equals("-jobid")){
                Parameters.BEPIPRED_INPUT= Parameters.BEPIPRED_TYPE.JOB_ID;
                Parameters.BEPIPRED2_JOBID = args[i+1];
            } else if(args[i].equals("-l")){
                i++;
                Parameters.BEPIPRED_INPUT= Parameters.BEPIPRED_TYPE.LOCAL;
                String arquivo = args[i];
                File arquivoFasta = new File (arquivo);
                Parameters.FASTA = arquivoFasta; 
            }
           
            else if (args[i].equals("-p")) {
                i++;
                String metodos = args[i];
                String[] ms = metodos.split(";");
                for (int j = 0; j < ms.length; j++) {
                    ms[j] = ms[j].trim();
                    String[] pMethod = ms[j].split(":");
                    map.put(pMethod[0], pMethod[1]);
                }
            } else if (args[i].equals("-t")) {
                i++;
                try {
                    THRESHOLD_BEPIPRED = Double.parseDouble(args[i]);
                } catch (Exception e) {
                    throw new Exception("-t requires a number: " + args[i]);
                }
            } else if (args[i].equals("-min_len")) {
                i++;
                try {
                    MIN_LENGTH_BEPIPRED2 = Integer.parseInt(args[i]);
                } catch (Exception e) {
                    throw new Exception("-min_len requires a integer number: " + args[i]);
                }
            } else if (args[i].equals("-max_len")) {
                i++;
                try {
                    MAX_LENGTH_BEPIPRED2 = Integer.parseInt(args[i]);
                } catch (Exception e) {
                    throw new Exception("-max_len requires a integer number: " + args[i]);
                }
            } else if (args[i].equals("-proteomes")) {
                i++;
                try {
                    if (args[i].endsWith(";")) {
                        args[i] = args[i].substring(0, args[i].length() - 1);
                    }

                    args[i] = args[i].trim();
                    String[] proteomas = args[i].split(";");

                    ArrayList<Proteome> proteomeFiles = new ArrayList<>();
                    for (String proteoma : proteomas) {
                        proteoma = proteoma.trim();
                        String[] st = proteoma.split("=");
                        File f = new File(st[1].trim());
                        if (!f.exists()) {
                            throw new Exception("File #" + st[1].trim() + "# not found");
                        } else {
                            proteomeFiles.add(new Proteome(st[0].trim(), f));
                        }
                    }
                    Parameters.PROTEOMES = proteomeFiles;

                } catch (Exception e) {
                    throw new Exception(e);
                }
                
            } else if (args[i].equals("-blast")){
                Parameters.SEARCH_BLAST=true;
                String blastParams = args[i+1];
                String[] params = blastParams.split(";");
                for (String param : params) {
                    String[] mapParam = param.split("=");
                    switch(mapParam[0]){
                        case "task":Parameters.BLAST_TASK=mapParam[1];break;
                        case "identity":Parameters.BLAST_IDENTITY=Double.parseDouble(mapParam[1]);break;
                        case "cover":Parameters.BLAST_COVER=Double.parseDouble(mapParam[1]);break;
                        case "word-size":Parameters.BLAST_WORD_SIZE=Integer.parseInt(mapParam[1]);break;
                        case "makeblastdb_path":Parameters.MAKEBLASTDB_PATH=mapParam[1];break;
                        case "blastp_path":Parameters.BLASTP_PATH=mapParam[1];break;
                    }
                }
            } else if(args[i].equals("-outfile")){
                Parameters.OUTPUT_FILE=true;
            }

        }
        //Parsing Map
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Double threshold = null;
            SoftwareBcellEnum software = null;
            if(!entry.getValue().equals("d")){
                threshold = Double.parseDouble(entry.getValue());
            }
            switch (entry.getKey().toUpperCase()) {
                case "PARKER": software = SoftwareBcellEnum.PARKER;break;
                case "CHOU_FOSMAN":software = SoftwareBcellEnum.CHOU_FOSMAN;break;
                case "EMINI":software = SoftwareBcellEnum.EMINI;break;
                case "KARPLUS_SCHULZ":software = SoftwareBcellEnum.KARPLUS_SCHULZ;break;
                case "KOLASKAR":software = SoftwareBcellEnum.KOLASKAR;break;
                default: {System.out.println("Unrecognized method: "+entry.getKey());System.exit(0);}
            }
            Parameters.MAP_SOFTWARES.put(software, threshold);
        }


      /*  for (Proteome proteome : Parameters.PROTEOMES) {
            proteome.load();
        }*/

        String res = EpitopeFinder.process();
        //System.out.println(res);
    }
}
