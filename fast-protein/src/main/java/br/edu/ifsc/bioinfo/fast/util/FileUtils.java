/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.bioinfo.fast.util;

import br.edu.ifsc.bioinfo.fast.protein.Protein;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author renato
 */
public class FileUtils {

    public static void copy(String source, String dest) throws IOException {
        Path copied = Paths.get(dest);
        Path originalPath = Paths.get(source);
        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void createFile(String content, String fileOut) throws IOException {
        FileWriter fw = null;
        File outfile = new File(fileOut);
        fw = new FileWriter(outfile);
        fw.append(content);
        fw.close();
    }

    
}
