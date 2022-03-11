/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsc.epibuilder;

/**
 *
 * @author renato
 */
public class MainTeste {
    public static void main(String[] args) throws Exception {
        String src = "/tese/projects/epifinder/";
        args = new String[]{
            "-b", src + "bepipred2-teste.csv",
//            "-o",
            "-jobid", "6057D70F000038BE571779BB",
            "-p", "emini:d;parker:d;kolaskar:d",
            "-t", "0.6",
            "-min_len", "10",
            "-max_len", "30",
            "-proteomes", "evansi=/tese/resultados/fasta/Tevansi.fasta;brucei=/tese/resultados/fasta/TriTrypDB-46_TbruceiTREU927_AnnotatedProteins.fasta;gambiense=/tese/resultados/fasta/TriTrypDB-46_TbruceigambienseDAL972_AnnotatedProteins.fasta;equiperdum=/tese/resultados/fasta/TequiperdumTxid5694.fasta;vivax=/tese/resultados/fasta/TriTrypDB-46_TvivaxY486_AnnotatedProteins.fasta",
            "-f", "jonas",
            "-rf", 
            "-blast","task=blastp-short;identity=90;cover=90;word-size=4;makeblastdb_path=/usr/local/ncbi/blast/bin/makeblastdb;blastp_path=/usr/local/ncbi/blast/bin/blastp"
        };
        Main.main(args);
    }
}
