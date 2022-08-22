/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.bioinfo.fast.protein.conversor;

import br.edu.ifsc.bioinfo.fast.protein.Protein;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.biojava.nbio.core.sequence.io.util.IOUtils;
import org.biojava.nbio.ws.alignment.qblast.BlastOutputFormatEnum;
import org.biojava.nbio.ws.alignment.qblast.BlastProgramEnum;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastAlignmentProperties;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastOutputProperties;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastService;

/**
 *
 * @author renato
 */
public class BlastConverter {

    public enum Database {
        nr, swissprot, none
    }

    public static String getFirstHit(Protein protein, Database db, int timeoutSeconds) {
        String blastResult = "-";
        NCBIQBlastService service = new NCBIQBlastService();

        // set alignment options
        NCBIQBlastAlignmentProperties props = new NCBIQBlastAlignmentProperties();
        props.setBlastProgram(BlastProgramEnum.blastp);
        props.setBlastDatabase(db.toString());
        NCBIQBlastOutputProperties outputProps = new NCBIQBlastOutputProperties();
        outputProps.setOutputFormat(BlastOutputFormatEnum.Text);
        outputProps.setAlignmentNumber(10);
        String rid = null;          // blast request ID
        FileWriter writer = null;
        BufferedReader reader = null;
        try {
            // send blast request and save request id
            rid = service.sendAlignmentRequest(protein.getSequence(), props);

            // wait until results become available. Alternatively, one can do other computations/send other alignment requests
            while (!service.isReady(rid)) {
                System.out.println("Waiting for blast remote query for protein " + protein.getId() + ". Remaining time " + timeoutSeconds + " seconds");
                Thread.sleep(5000);
                timeoutSeconds -= 5;

                if (timeoutSeconds <= 0) {
                    blastResult = "No hit - timeout";
                    break;
                }
            }
            if (service.isReady(rid)) {
                // read results when they are ready
                InputStream in = service.getAlignmentResults(rid, outputProps);
                reader = new BufferedReader(new InputStreamReader(in));

                // write blast output to specified file
                File f = new File(protein.getId() + ".txt");
                System.out.println("Saving query results for " + protein.getId() + " in file " + f.getAbsolutePath());
                writer = new FileWriter(f);

                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line + System.getProperty("line.separator"));
                    if (line.startsWith("Sequences producing significant alignments")) {
                        line = reader.readLine();
                        writer.write(line + System.getProperty("line.separator"));
                        line = reader.readLine();
                        writer.write(line + System.getProperty("line.separator"));
                        blastResult = parseResult(line);
                    }
                }
            } else {
                System.out.println("Blast - Remote query for " + protein.getId() + " timeout.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            // clean up
            IOUtils.close(writer);
            IOUtils.close(reader);

            // delete given alignment results from blast server (optional operation)
            service.sendDeleteRequest(rid);
        }
        return blastResult;
    }

    private static String parseResult(String val) {
        try {
            return val.substring(val.indexOf(" ") + 1, 67).trim();
        } catch (Exception e) {
            System.out.println("Error converting line: " + val);
            return "-";
        }
    }
}

//uniprotkb, uniprotkb_swissprot, uniprotkb_swissprotsv, uniprotkb_trembl, uniprotkb_refprotswissprot, uniprotkb_archaea, uniprotkb_arthropoda
