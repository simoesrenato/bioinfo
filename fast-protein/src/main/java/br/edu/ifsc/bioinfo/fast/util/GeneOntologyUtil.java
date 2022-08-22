/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.bioinfo.fast.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import org.biojava.nbio.ontology.Ontology;
import org.biojava.nbio.ontology.Term;
import org.biojava.nbio.ontology.io.OboParser;

/**
 *
 * Downloaded from http://geneontology.org/docs/download-ontology/
 *
 * @author renato
 */
public class GeneOntologyUtil {

    public enum Type {
        cellular_component,
        molecular_function,
        biological_process
    }
    private static OboParser parser = new OboParser();
    private static Ontology ontology;

    static {
        try {
            BufferedReader oboFile = new BufferedReader(new InputStreamReader(new FileInputStream("go.obo")));
            ontology = parser.parseOBO(oboFile, "Gene Ontology", "Core ontology (OBO Format)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Type getType(String go) {
        Term term = ontology.getTerm(go);
        if (term != null) {
            switch ((String) term.getAnnotation().getProperty("namespace")) {
                case "cellular_component":
                    return Type.cellular_component;
                case "molecular_function":
                    return Type.molecular_function;
                case "biological_process":
                    return Type.biological_process;
            }
        }
        return null;
    }

    public static String getOntology(String go) {
        Term term = ontology.getTerm(go);
        if (term != null) {
            return term.getDescription();
        }
        return "";
    }

    public static Term getTerm(String go) {
        return ontology.getTerm(go);
    }

    public static String getFullOntology(String go) {
        Term term = ontology.getTerm(go);
        if (term != null) {
            String type = "";
            switch ((String) term.getAnnotation().getProperty("namespace")) {
                case "cellular_component":
                    type = "C";
                    break;
                case "molecular_function":
                    type = "F";
                    break;
                case "biological_process":
                    type = "P";
                    break;
            }
            return String.format("%s:%s - %s", type, go, term.getDescription());
        }
        return "";
    }

}
