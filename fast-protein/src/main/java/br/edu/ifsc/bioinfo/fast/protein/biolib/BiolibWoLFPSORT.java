/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.bioinfo.fast.protein.biolib;

import br.edu.ifsc.bioinfo.fast.protein.conversor.WolfPsortConverter;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author renato
 */
public class BiolibWoLFPSORT extends AbstractBiolibService{

    public BiolibWoLFPSORT() {
        super("simoesrenato/WoLFPSORT");
    }

    public void runCommand(WolfPsortConverter.Type type, File fasta) throws Exception {
        super.runCommand("--input",fasta.getAbsolutePath(),"--orgtype",type.toString()); 
    }
    
}
