/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author renato
 */
public class BiolibMainTest {
    public static void main(String[] args) {
        String dir = "/tese/others/amanda/epitopos/";
        args = new String[] {"-i","/tese/others/amanda/epitopos/biolib_results/raw_output.csv",
                "-search",
                "direct_blast",
                "-so", "macos",
                "-t", "0.20",
                "-b", "vesiculas-0.20",
                "--proteome1-alias", "evansi",
                "--proteome1", dir+"TriTrypDB-60_TevansiSTIB805_AnnotatedProteins.fasta",
                "--proteome2-alias", "brucei",
                "--proteome2", dir+"TriTrypDB-60_TbruceiTREU927_AnnotatedProteins.fasta",
                "--proteome3-alias", "vivax",
                "--proteome3", dir+"TriTrypDB-60_TvivaxY486_AnnotatedProteins.fasta",
                "--proteome4-alias", "equiperdum",
                "--proteome4", dir+"TriTrypDB-60_TequiperdumOVI_AnnotatedProteins.fasta"
        };
        BiolibMain.main(args);
    }
}
