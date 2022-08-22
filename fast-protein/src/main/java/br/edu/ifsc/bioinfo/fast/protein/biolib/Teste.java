/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.bioinfo.fast.protein.biolib;

import br.edu.ifsc.bioinfo.fast.protein.conversor.WolfPsortConverter;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author renato
 */
public class Teste {
    public static void main(String[] args) {
        try {
            new BiolibWoLFPSORT().runCommand(WolfPsortConverter.Type.animal, new File("clean.fasta"));
        } catch (Exception ex) {
            Logger.getLogger(Teste.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
