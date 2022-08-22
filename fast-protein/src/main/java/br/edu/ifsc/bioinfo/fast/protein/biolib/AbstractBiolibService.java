/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.bioinfo.fast.protein.biolib;

import br.edu.ifsc.bioinfo.fast.protein.CommandRunner;
import java.io.IOException;

/**
 *
 * @author renato
 */
public abstract class AbstractBiolibService {

    private String name;

    public AbstractBiolibService(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void runCommand(String... args) throws Exception {
        String command = String.format("biolib run %s %s", name, "".join(" ", args));
        System.out.println("Running: " + command);
        CommandRunner.run(command);
    }

    public void runSOCommand(String command) throws Exception {
        System.out.println("Running: " + command);
        CommandRunner.run(command);
    }

}
