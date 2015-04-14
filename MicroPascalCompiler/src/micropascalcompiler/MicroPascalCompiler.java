/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micropascalcompiler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author arbiter34
 */
public class MicroPascalCompiler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 1 || args.length > 1) {
            System.out.println("Invalid Usage.\n\nExample: MicroPascalCompiler.jar source.pas\n");
            System.exit(1);
        }
        System.out.println("Loading File: " + args[0]);
        try {
            FileInputStream exists = new FileInputStream(args[0]);
            exists.close();
        } catch (IOException e) {
            System.out.println(args[0] + " not found, please check your file path.\n");
        }
        Scanner scanner = new Scanner(args[0]);
        try {
            Parser parser = new Parser(scanner, new PrintWriter(args[0]+"_parse.txt"), args[0]);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        
        System.out.println("Done!");
        
        
    }
    
}
