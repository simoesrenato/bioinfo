/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.bioinfo.fast.protein.biolib.dtu;

import br.edu.ifsc.bioinfo.fast.protein.biolib.AbstractBiolibService;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author renato
 */
public class BiolibDeepTMHMM extends AbstractBiolibService {
    
    public BiolibDeepTMHMM() {
        super("DTU/DeepTMHMM");
    }
    
    public void runCommand(File fasta) throws Exception {
        super.runCommand("--input", fasta.getAbsolutePath());
    }
    
    
}
