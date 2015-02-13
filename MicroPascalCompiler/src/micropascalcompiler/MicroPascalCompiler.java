/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micropascalcompiler;

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
        TokenContainer t = null;
        
        while ((t = scanner.getNextToken()).getToken() != TokenType.MP_EOF) {
            System.out.println("Token: " + t.getToken() + " Lexeme: " + scanner.getLexeme());
        }
        
        
    }
    
}
