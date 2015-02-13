/*
 * Things
 */
package fsa;


import micropascalcompiler.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 *
 * @author arbiter34
 */
public class Dispatcher {
    private FileInputStream inStream;

    private static final int markLimit = 1024;
    private BufferedReader inFile;
    private int rowCount = 0;
    private int colCount = 0;
    
    public Dispatcher(BufferedReader in) {
        try {
            this.inFile = in;
            this.inFile.mark(markLimit);
        } catch (Exception e){
            System.out.println(e.getLocalizedMessage());
        }
    }
    
    
    
    private boolean consumeWhiteSpace() {
        String c;
        int charByte;
        try {
            this.inFile.mark(markLimit);
            while ( ((charByte = this.inFile.read()) != -1) && ((c = Character.toString((char)charByte)).matches("\\s")) ) {
                this.colCount += 1;
                if (c.matches("\r|\n")) {
                    this.rowCount += 1;
                    this.colCount = 0;
                }
                this.inFile.mark(markLimit);
            }
            if (charByte == -1) {
                return false;
            }
            this.inFile.reset();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
        return true;
    }
    
    public TokenContainer nextToken() {
        this.markFile();
        int length = 0;
        boolean hasMatched = false;
        TokenType tokenType = null;
        
        if (consumeWhiteSpace()) {
            AbstractFSA a = getFSA(peek());
            if (a != null) {
                a.execute();
                TokenContainer t = a.getResult();
                t.setRow(this.rowCount);
                t.setCol(this.colCount);
                return t;
            } else {
                return new TokenContainer(TokenType.MP_ERROR, -1, rowCount, colCount, 1, true); 
            }
        } 
        
        
        return new TokenContainer(TokenType.MP_EOF, -1, rowCount, colCount, 1, true);   //placeholder
    }
    
    private char peek() {
        char c = '\0';
        try {
            this.inFile.mark(32);
            c = (char)this.inFile.read();
            this.inFile.reset();
        } catch (Exception e) {
            
        }
        return c;
    }
    
    
    private AbstractFSA getFSA(char c) {
        AbstractFSA a = null;
        
        if (Characters.isDigit(c)) {
            return DigitFSA.getInstance(inFile);
        } 
        if (Characters.isLetter(c) || c == '$' || c == '_') {
            return AlphaFSA.getInstance(inFile);
        }
        if (c == '{') {
            return CommentFSA.getInstance(inFile);
        }
        if (c == '\'') {
            return LiteralFSA.getInstance(inFile);
        }
            
        
        
        return a;
    }
    
    private void markFile() {
        try {
            this.inFile.mark(markLimit);
        } catch (Exception e) {
            
        }
    }
    
    private void rewind() {
        try {
            this.inFile.reset();
        } catch (Exception e) {
            
        }
    }
    
}
