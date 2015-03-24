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
public class DigitFSA extends AbstractFSA {
    
    protected static DigitFSA instance; 
    
    private DigitFSA(){
    }
   
    
    public static DigitFSA getInstance(BufferedReader inFile) {
        if (instance == null) {
            instance = new DigitFSA();
        } 
        instance.setBufferedReader(inFile);
        return instance;
    }
    
    @Override
    public void run() {
        int length = 0;
        boolean stopFSA = false;
        int state = 0;
        
        //Init TokenContainer to error
        t = new TokenContainer(TokenType.MP_ERROR, -1, 0, 0, length, true);
        
        char c;
        
        try {
            while (!stopFSA) {
                c = (char)this.inFile.read();
                switch (state) {
                    case 0:
                        if (Characters.isDigit(c)) {
                            this.t.setToken(TokenType.MP_INTEGER_LIT);
                            state = 1;
                            length++;
                        } else {
                            state = -1;
                        }
                        break;
                        
                    /* We read a digit, should always be the case since this is only
                    executed after peeking first character */
                    case 1:
                        if (Characters.isDigit(c)) {
                            length++;
                        } else if (c == '.') {
                            state = 2;
                            length++;
                        } else {
                            state = -1;
                        }
                        break;
                    case 2:
                        if (Characters.isDigit(c)) {
                            state = 3;
                            this.t.setToken(TokenType.MP_FIXED_LIT);
                            length++;
                        } else {
                            length--;
                            state = -1;
                        }
                        break;
                    case 3:
                        if (Characters.isDigit(c)) {
                            length++;
                        } else if (c == 'e' || c == 'E') {
                            state = 4;
                            length++;
                        } else {
                            state = -1;
                        }
                        break;
                    case 4:
                        if (Characters.isDigit(c)) {
                            state = 6;
                            this.t.setToken(TokenType.MP_FLOAT_LIT);
                            length++;
                        } else if (c == '+' || c == '-') {
                            state = 5;
                            length++;
                        } else {
                            length--;
                            state = -1;
                        }
                        break;
                    case 5:
                        if (Characters.isDigit(c)) {
                            state = 6;
                            this.t.setToken(TokenType.MP_FLOAT_LIT);
                            length++;
                        } else {
                            length -= 2;
                            state = -1;
                        }
                        break;
                    case 6:
                        if (Characters.isDigit(c)) {
                            length++;
                        } else {
                            state = -1;
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

