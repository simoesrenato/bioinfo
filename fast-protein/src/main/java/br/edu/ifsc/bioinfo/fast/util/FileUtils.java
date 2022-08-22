/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.bioinfo.fast.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
}
