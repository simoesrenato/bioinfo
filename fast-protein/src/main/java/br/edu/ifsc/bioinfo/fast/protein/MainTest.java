/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.bioinfo.fast.protein;

/**
 *
 * @author renato
 */
public class MainTest {

    public static void main(String[] args) {
        args = new String[]{"-i","trypanosoma_33.prodigal_annotated.faa", "-ipr", "true"};
        Main.main(args);
    }
}
