/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.bioinfo.fast.protein;

import br.edu.ifsc.bioinfo.fast.util.GeneOntologyUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;

/**
 *
 * @author renato.simoes
 */
public class Protein {

    private String id = "";
    private String header = "";
    private String sequence = "";
    private String subcellularLocalization = "";
    private Integer transmembrane = 0;
    private String signalp5 = "";
    private String blastHit = "";
    private ArrayList<Domain> nglycDomains = new ArrayList<>();
    private ArrayList<Domain> erretDomains = new ArrayList<>();

    private TreeSet<String> interpro = new TreeSet<>();
    private TreeSet<String> go = new TreeSet<>();
    private TreeSet<String> pfam = new TreeSet<>();
    private TreeSet<String> panther = new TreeSet<>();

    private HashMap<String, String> interproMap = new HashMap<>();
    private HashMap<String, String> goMap = new HashMap<>();

    public Protein(String id) {
        this.id = id;
    }

    public Protein(String id, String sequence) {
        this.id = id;
        this.sequence = sequence;
    }

    public TreeSet<String> getSeparatedGO() {
        TreeSet<String> gos = new TreeSet<>();
        for (String rawGo : go) {
            if (rawGo.contains("|")) {
                String[] goUnit = rawGo.split("[|]");
                gos.addAll(Arrays.asList(goUnit));
            } else {
                gos.add(rawGo);
            }
        }
        return gos;
    }

    public TreeSet<String> getFullSeparatedGO() {
        TreeSet<String> gos = getSeparatedGO();
        TreeSet<String> newGos = getSeparatedGO();
        for (String go1 : gos) {
            newGos.add(String.format("%s:%s - %s", GeneOntologyUtil.getType(go1), go1, GeneOntologyUtil.getOntology(go1)));
        }

        return newGos;
    }

    public String getCleanGOWegoFormat() {
        return String.format("%s\t%s", id, getCleanGO().replaceAll(", ", "\t"));
    }

    public String getCleanFullGO() {

        return String.join(", ", getFullSeparatedGO());
    }

    public String getCleanGO() {

        return String.join(", ", getSeparatedGO());
    }

    public String getCleanInterpro() {
        return String.join(", ", interpro);
    }

    public String getCleanPfam() {
        return String.join(", ", pfam);
    }

    public String getCleanPanther() {
        return String.join(", ", panther);
    }

    public void addInterpro(String interproId, String interproAnnot) {
        if (!interproId.trim().equals("-")) {
            interproMap.put(interproId, interproAnnot);
            interpro.add(String.format("%s - %s", interproId, interproAnnot));
        }
    }

    public void addGO(String go) {
        if (!go.trim().equals("-")) {
            this.go.add(go);
        }
        for (String goid : getSeparatedGO()) {
            goMap.put(goid, GeneOntologyUtil.getOntology(goid));
        }
    }

    public void addPfam(String pfam) {
        if (!pfam.trim().equals("-")) {
            this.pfam.add(pfam);
        }
    }

    public void addPanther(String panther) {
        if (!panther.trim().equals("-")) {
            this.panther.add(panther);
        }
    }

    public void setBlastHit(String blastHit) {
        this.blastHit = blastHit;
    }

    public String getBlastHit() {
        return blastHit;
    }

    public void setTransmembrane(Integer transmembrane) {
        this.transmembrane = transmembrane;
    }

    public Integer getTransmembrane() {
        return transmembrane;
    }

    public void addNglycDomain(Domain domain) {
        nglycDomains.add(domain);
    }

    public void addErretDomain(Domain domain) {
        erretDomains.add(domain);
    }

    public String getSubcellularLocalization() {
        return subcellularLocalization;
    }

    public void setSubcellularLocalization(String subcellularLocalization) {
        this.subcellularLocalization = subcellularLocalization;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the header
     */
    public String getHeader() {
        return header;
    }

    /**
     * @param header the header to set
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * @return the sequence
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * @param sequence the sequence to set
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * @return the length
     */
    public Integer getLength() {
        return sequence.length();
    }

    /**
     * @return the kda
     */
    public Double getKda() throws CompoundNotFoundException {
        return ProteomicCalculator.getMolecularWeight(sequence) / 1000;
    }

    public String getKdaStr() throws CompoundNotFoundException {
        return String.format("%.2f", getKda());
    }

    /**
     * @return the isoeletricPointAvg
     */
    public Double getIsoeletricPointAvg() throws CompoundNotFoundException {

        return ProteomicCalculator.getIsoelectricPoint(sequence);
    }

    public String getIsoeletricPointAvgStr() throws CompoundNotFoundException {
        return String.format("%.2f", getIsoeletricPointAvg());
    }

    /**
     * @return the hydropathy
     */
    public Double getHydropathy() throws CompoundNotFoundException {

        return ProteomicCalculator.getHydropathy(sequence);
    }

    public String getHydropathyStr() throws CompoundNotFoundException {
        return String.format("%.2f", getHydropathy());
    }

    /**
     * @return the erretTotal
     */
    public Integer getErretTotal() {
        return erretDomains.size();
    }

    /**
     * @return the erretDomains
     */
    public String getErretDomainsAsString() {
        ArrayList<String> domains = new ArrayList<>();
        for (Domain erretDomain : erretDomains) {
            domains.add(erretDomain.toString());
        }
        return String.join(";", domains);
    }

    /**
     * @return the nGlycTotal
     */
    public Integer getnGlycTotal() {
        return nglycDomains.size();
    }

    /**
     * @return the nGlycDomains
     */
    public String getnGlycDomainsAsString() {
        ArrayList<String> domains = new ArrayList<>();
        for (Domain nglycDomain : nglycDomains) {
            domains.add(nglycDomain.toString());
        }
        return String.join(";", domains);
    }

    public double getAromaticity() {
        return ProteomicCalculator.getAromaticity(sequence);
    }

    public String getAromaticityStr() throws CompoundNotFoundException {
        return String.format("%.2f", getAromaticity());
    }

    public ArrayList<Domain> getNglycDomains() {
        return nglycDomains;
    }

    public ArrayList<Domain> getErretDomains() {
        return erretDomains;
    }

    public String getSignalp5() {
        return signalp5;
    }

    public void setSignalp5(String signalp5) {
        this.signalp5 = signalp5;
    }

    public String toJson() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("header", header);
        json.put("sequence", sequence);
        json.put("subcellular_localization", subcellularLocalization);
        json.put("tm", transmembrane);
        json.put("signalp5", signalp5);
        json.put("blast_hit", blastHit);
        JSONArray nglyc = new JSONArray();
        for (Domain nglycDomain : nglycDomains) {
            nglyc.add(nglycDomain.toString());
        }
        json.put("nglyc", nglyc);
        JSONArray erret = new JSONArray();
        for (Domain erretDomain : erretDomains) {
            erret.add(erretDomain.toString());
        }
        json.put("erret", erret);

        JSONArray pfamArray = new JSONArray();
        for (String pf : pfam) {
            pfamArray.add(pf);
        }
        json.put("pfam", pfamArray);
        JSONArray pantherArray = new JSONArray();
        for (String pt : panther) {
            pantherArray.add(pt);
        }
        json.put("panther", pantherArray);

        JSONArray interproIdArray = new JSONArray();
        JSONArray interproDescArray = new JSONArray();
        for (Map.Entry<String, String> entry : interproMap.entrySet()) {
            interproIdArray.add(entry.getKey());
            interproDescArray.add(entry.getValue());
        }

        JSONArray goIdArray = new JSONArray();
        JSONArray goDescArray = new JSONArray();
        for (Map.Entry<String, String> entry : goMap.entrySet()) {
            goIdArray.add(entry.getKey());
            goDescArray.add(entry.getValue());
        }
        json.put("interpro_id", interproIdArray);
        json.put("interpro_desc", interproDescArray);
        json.put("go_id", goIdArray);
        json.put("go_desc", goDescArray);
        return json.toString();
    }

}
