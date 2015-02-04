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
        try {
            this.inFile.mark(markLimit);
            while ( (c = java.lang.Character.toString((char)this.inFile.read())).matches("\\s") ) {
                this.colCount += 1;
                if (c.matches("\r|\n")) {
                    this.rowCount += 1;
                    this.colCount = 0;
                }
                this.inFile.mark(markLimit);
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
        if (this.consumeWhiteSpace()) {
            try {
                String first_char = getNextCharAsString();
                length += 1;
                for (String key : FSA.FSAHash.keySet()) {
                    if (first_char.matches(key)) {
                        String lexeme = first_char;
                        while (true) {                            
                            boolean hasMatchedThisIteration = false;
                            int count = 0;
                            for (String hashKey : FSA.FSAHash.get(key).keySet()) {
                                count++;
                                if (lexeme.matches(hashKey)) {
                                    hasMatched = true;
                                    hasMatchedThisIteration = true;
                                    tokenType = FSA.FSAHash.get(key).get(hashKey);
                                } 
                                if (hasMatched 
                                        && !hasMatchedThisIteration 
                                        && count == FSA.FSAHash.get(key).size()) {
                                    length -= 1;
                                    lexeme = lexeme.substring(0, lexeme.length()-1);
                                    this.rewind();
                                    return new TokenContainer(tokenType, -1, 
                                        this.rowCount, this.colCount+length,
                                        length, false);
                                }

                            }
                            String s = getNextCharAsString();
                            if (lexeme.matches("\\s")) {
                                break;
                            }
                            lexeme += s;
                            length += 1;
                        }
                        if (hasMatched) {
                            this.rewind();
                            return new TokenContainer(tokenType, -1, 
                                    this.rowCount, this.colCount+length,
                                    length, false);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
        return new TokenContainer(TokenType.MP_ERROR, -1, 0, 0, 0, true);   //placeholder
    }
    
    private String getNextCharAsString() {
       String s = null;
       try {
           s = java.lang.Character.toString((char)this.inFile.read());
       } catch (Exception e) {
           
       }
       return s;
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
