/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.bioinfo.fast.protein;

import org.biojava.nbio.core.exceptions.CompoundNotFoundException;

/**
 *
 * @author renato.simoes
 */
public class Protein {

    private String id;
    private String header;
    private String sequence;

    private Integer erretTotal;
    private String erretDomains;
    private Integer nGlycTotal;
    private String nGlycDomains;

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
        return erretTotal;
    }

    /**
     * @param erretTotal the erretTotal to set
     */
    public void setErretTotal(int erretTotal) {
        this.erretTotal = erretTotal;
    }

    /**
     * @return the erretDomains
     */
    public String getErretDomains() {
        return erretDomains;
    }

    /**
     * @param erretDomains the erretDomains to set
     */
    public void setErretDomains(String erretDomains) {
        this.erretDomains = erretDomains;
    }

    /**
     * @return the nGlycTotal
     */
    public Integer getnGlycTotal() {
        return nGlycTotal;
    }

    /**
     * @param nGlycTotal the nGlycTotal to set
     */
    public void setnGlycTotal(int nGlycTotal) {
        this.nGlycTotal = nGlycTotal;
    }

    /**
     * @return the nGlycDomains
     */
    public String getnGlycDomains() {
        return nGlycDomains;
    }

    /**
     * @param nGlycDomains the nGlycDomains to set
     */
    public void setnGlycDomains(String nGlycDomains) {
        this.nGlycDomains = nGlycDomains;
    }

    public double getAromaticity(){
        return ProteomicCalculator.getAromaticity(sequence);
    }
    public String getAromaticityStr() throws CompoundNotFoundException {
        return String.format("%.2f", getHydropathy());
    }
}
