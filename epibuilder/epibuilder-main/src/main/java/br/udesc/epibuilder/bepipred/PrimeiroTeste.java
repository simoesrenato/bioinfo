/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.epibuilder.bepipred;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PrimeiroTeste {
    public static String submitBepipred(String sequences){
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
    
    private static void exemploBepipred() {
        WebDriver driver = new FirefoxDriver();
        driver.get("http://www.cbs.dtu.dk/services/BepiPred/");

        WebElement campoPesquisado = driver.findElement(By.name("fasta"));
        campoPesquisado.clear();
        campoPesquisado.sendKeys(">5C0N_A Chain A of Development of a therapeutic monoclonal antibody that targets secreted fatty acid-binding protein aP2 to treat type 2 diabetes.\n"
                + "CDAFVGTWKLVSSENFDDYMKEVGVGFATRKVAGMAKPNMIISVNGDLVTIRSESTFKNTEISFKLGVEFDEITADDRKVKSIITLDGGALVQVQKWDGKSTTIKRKRDGDKLVVECVMKGVTSTRVYERA");

        System.out.println("O título da página é: " + driver.getTitle());
        campoPesquisado.submit();
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver objDriver) {
//                System.out.println("obj - "+ objDriver.);
                return objDriver.getCurrentUrl().toLowerCase().startsWith("http://www.cbs.dtu.dk/cgi-bin/webface2.fcgi?jobid=");
            }
        });
        System.out.println("Jobid "+ driver.getCurrentUrl().replace("http://www.cbs.dtu.dk/cgi-bin/webface2.fcgi?jobid=", "").replace("&wait=20", ""));
        
        System.out.println("O título da página é: " + driver.getTitle());

        driver.quit();
    }

    private static void exemploGoogleQuePesquisaPor(final String stringPesquisa) {
        WebDriver driver = new FirefoxDriver();
        driver.get("http://www.google.com");

        WebElement campoPesquisado = driver.findElement(By.name("q"));
        campoPesquisado.clear();
        campoPesquisado.sendKeys(stringPesquisa);

        System.out.println("O título da página é: " + driver.getTitle());

        campoPesquisado.submit();

        (new WebDriverWait(driver, 100)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver objDriver) {
                System.out.println("Pagina: ");
                return objDriver.getTitle().toLowerCase().startsWith("Your job ");
            }
        });

        System.out.println("O título da página é: " + driver.getTitle());

        driver.quit();
    }

    public static void googleExemploQueijo() {
        exemploGoogleQuePesquisaPor("Queijo!");
    }

    public static void googleExemploLeite() {
        exemploGoogleQuePesquisaPor("Leite!");
    }

    public static void main(String[] args) {
        exemploBepipred();
    }
}
