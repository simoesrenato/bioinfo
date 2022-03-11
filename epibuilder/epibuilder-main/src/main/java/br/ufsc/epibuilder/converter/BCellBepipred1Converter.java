/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufsc.epibuilder.converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author renato
 */
public class BCellBepipred1Converter {

    public static ArrayList<ProteinConverter> getAll(File f) throws FileNotFoundException {
        Scanner s = new Scanner(f);

        ArrayList<ProteinConverter> proteins = new ArrayList<>();
        ProteinConverter protein = null;
        s.nextLine();
        s.nextLine();
        s.nextLine();
        while (s.hasNext()) {
            String val = s.nextLine();
            if (val.startsWith("##Type Protein ")) {
                String id = val.replace("##Type Protein ", "").trim();
                protein = new ProteinConverter(id);
                proteins.add(protein);
                //pula a linha Position
                s.nextLine();
                s.nextLine();
                s.nextLine();
                s.nextLine();
                s.nextLine();
            } else {
                String[] values = val.split("\\s+");
                protein.addAmino(Integer.parseInt(values[3]), values[8].charAt(0)+"", Double.parseDouble(values[5]));
            }
        }
        return proteins;
    }

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<ProteinConverter> proteins = getAll(new File("/tese/epifinder/teste/bepipred1.out.txt"));
        System.out.println(proteins.size());
    }
}
