/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micropascalcompiler;

import java.io.IOException;

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
            Parser parser = new Parser(scanner);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        
        System.out.println("Done!");
        
        
    }
    
}
