/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fsa;

import java.io.BufferedReader;
import micropascalcompiler.Characters;
import micropascalcompiler.TokenContainer;
import micropascalcompiler.TokenType;

/**
 *
 * @author arbiter34
 */
public class LiteralFSA extends AbstractFSA {
    
    protected static LiteralFSA instance; 
    
    private LiteralFSA(){
    }
   
    
    public static LiteralFSA getInstance(BufferedReader inFile) {
        if (instance == null) {
            instance = new LiteralFSA();
        } 
        instance.setBufferedReader(inFile);
        return instance;
    }
    
    @Override
    public void run() {
        int length = 0;
        boolean stopFSA = false;
        int state = 0;
        char c;
        
        //Init TokenContainer to error
        t = new TokenContainer(TokenType.MP_ERROR, -1, 0, 0, length, true);
        
        try {
            while (!stopFSA) {
                c = (char)this.inFile.read();
                switch (state) {
                    case 0:
                        if (c == '\'') {
                            length++;
                            state = 1;
                        } else {
                            state = -1;
                        }
                        break;
                    case 1:
                        if (c == '\'') {
                            length++;
                            t.setToken(TokenType.MP_STRING);
                            state = -1;
                        } else if (c == -1) {
                            t.setToken(TokenType.MP_RUN_STRING);
                            
                        } else {
                            if (Characters.isNewLine(c)) {
                                t.setCol(0);
                                t.setRow(t.getRow() + 1);
                            }
                            if (Characters.isSpace(c)) {
                                t.setCol(t.getCol() + 1);
                            }
                            if (Characters.isTab(c)) {
                                t.setCol(t.getCol() + 4);
                            }
                            length++;
                        }
                        break;
                    default:
                        stopFSA = true;
                        break;
                }
            }
        } catch (Exception e) {
            
        }
        this.t.setLength(length);
        this.t.setError((t.getToken() == TokenType.MP_ERROR));
        
        
    }
    
    
}

