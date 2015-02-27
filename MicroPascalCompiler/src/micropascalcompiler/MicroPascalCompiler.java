/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micropascalcompiler;

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
        Scanner scanner = new Scanner("test.txt");
        try {
            Parser parser = new Parser(scanner, new PrintWriter("output.txt"));
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        
        System.out.println("Done!");
        
        
    }
    
}
