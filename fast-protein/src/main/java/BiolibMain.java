
import br.edu.ifsc.bioinfo.fast.protein.Main;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



public class BiolibMain {

    
    public static void main(String... args) {
        //System.out.println("FastProtein - Executing from Biolib");
        ArrayList<String> newArgs = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("--type_biolib")) {
                newArgs.add("--type");
                newArgs.add(arg.replace("--type_biolib,", ""));
            } else {
                newArgs.add(arg);
            }
        }
        args = newArgs.toArray(new String[0]);
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
        }
         
        Main.main(args);
    }


}
