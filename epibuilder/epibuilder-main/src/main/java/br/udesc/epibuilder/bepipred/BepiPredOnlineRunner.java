/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.epibuilder.bepipred;

import br.ufsc.epibuilder.Parameters;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * You need to download a driver https://github.com/mozilla/geckodriver/releases for your O.S.
 * @author renato
 */
public class BepiPredOnlineRunner {

    public static File getBepiPred2Results(File fastaSequences, Parameters.BEPIPRED2_TYPE type) throws Exception {
        String s = null;
        String bepipredoutput = String.format("%s/%s-%s.csv", Parameters.DESTINATION_FOLDER, Parameters.BASENAME, type.getName());
        String[] cmd = null;
        if (type == Parameters.BEPIPRED2_TYPE.BCELL_STANDALONE) {
            cmd = new String[]{
                "python", Parameters.BEPIPRED2_BCELL_STANDALONE_PATH,
                "-m", "BepiPred-2.0",
                "-f", fastaSequences.getAbsolutePath()};
        } else if (type == Parameters.BEPIPRED2_TYPE.ONLINE) {
            cmd = new String[]{
                Parameters.BEPIPRED2_PATH, fastaSequences.getAbsolutePath()};
        }
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            StringBuilder sb = new StringBuilder();
            while ((s = stdInput.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }
            while ((s = stdError.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }
            FileWriter fw = new FileWriter(bepipredoutput);
            fw.append(sb.toString());
            fw.close();

            return new File(bepipredoutput);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public static void main(String[] args) throws Exception {
        File file = getBepiPredByJobId("605775C100001ECE9B66F577");
        Scanner s = new Scanner(file);
        System.out.println(file.getAbsolutePath());
        while (s.hasNext()) {
            System.out.println(s.nextLine());
        }
    }

    public static File getBepiPredByJobId(String jobid) throws Exception {
        String filename = String.format("http://www.cbs.dtu.dk/services/BepiPred-2.0/tmp/%1$s/summary_%1$s.csv", jobid);
        try {
            URL url = new URL(filename);
            File f = new File(Parameters.DESTINATION_FOLDER + "/" + Parameters.BASENAME + "-bepipred2-" + jobid + ".csv");

            FileUtils.copyURLToFile(url, f);

            return f;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("The file " + filename + " isn't available yet.\nPlease, try in few minutes or check the progress in\n"
                    + "\n\thttp://www.cbs.dtu.dk/cgi-bin/webface2.fcgi?jobid=" + jobid
                    + "\nBepiPred-2.0 demands a lot of server resources, it can takes some while."
                    + "\nPlease, check if the given jobid is correct using the above link."
                    + "\n\nFor your convenience your last jobid (" + jobid + ") was stored. "
                    + "\nYou can close this software and open again later.");
        }
    }

    public static String submitBepipred(String sequences) {
        WebDriver driver = new FirefoxDriver();
        driver.get("http://www.cbs.dtu.dk/services/BepiPred/");
        WebElement campoPesquisado = driver.findElement(By.name("fasta"));
        campoPesquisado.clear();
        campoPesquisado.sendKeys(sequences);
        campoPesquisado.submit();
        (new WebDriverWait(driver, 20)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver objDriver) {
                return objDriver.getCurrentUrl().toLowerCase().startsWith("http://www.cbs.dtu.dk/cgi-bin/webface2.fcgi?jobid=");
            }
        });
        String jobid = driver.getCurrentUrl().replace("http://www.cbs.dtu.dk/cgi-bin/webface2.fcgi?jobid=", "").replace("&wait=20", "");
        driver.quit();
        return jobid;
    }

}
