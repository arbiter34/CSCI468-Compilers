/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fsa;

import java.io.BufferedReader;
import micropascalcompiler.TokenContainer;
import micropascalcompiler.TokenType;

/**
 *
 * @author arbiter34
 */
public class SymbolFSA extends AbstractFSA {
    
    protected static SymbolFSA instance; 
    
    private SymbolFSA(){
    }
   
    
    public static SymbolFSA getInstance(BufferedReader inFile) {
        if (instance == null) {
            instance = new SymbolFSA();
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
                        if (c == ':') {
                            t.setToken(TokenType.MP_COLON);
                            length++;
                            state = 1;
                        } else if (c == ',') {
                            t.setToken(TokenType.MP_COMMA);
                            length++;
                            state = -1;
                        } else if (c == '=') {
                            t.setToken(TokenType.MP_EQUAL);
                            length++;
                            state = -1;
                        } else if (c == '/') {
                            t.setToken(TokenType.MP_FLOAT_DIVIDE);
                            length++;
                            state = -1;
                        } else if (c == '>') {
                            t.setToken(TokenType.MP_GTHAN);
                            length++;
                            state = 2;
                        } else if (c == '<') {
                            t.setToken(TokenType.MP_LTHAN);
                            length++;
                            state = 3;
                        } else if (c == '(') {
                            t.setToken(TokenType.MP_LPAREN);
                            length++;
                            state = -1;
                        } else if (c == '-') {
                            t.setToken(TokenType.MP_MINUS);
                            length++;
                            state = -1;
                        } else if (c == '.') {
                            t.setToken(TokenType.MP_PERIOD);
                            length++;
                            state = -1;
                        } else if (c == '+') {
                            t.setToken(TokenType.MP_PLUS);
                            length++;
                            state = -1;
                        } else if (c == ')') {
                            t.setToken(TokenType.MP_RPAREN);
                            length++;
                            state = -1;
                        } else if (c == ';') {
                            t.setToken(TokenType.MP_SCOLON);
                            length++;
                            state = -1;
                        } else if (c == '*') {
                            t.setToken(TokenType.MP_TIMES);
                            length++;
                            state = -1;
                        } else {
                            state = -1;
                        }
                        break;
                    case 1:
                        if (c == '=') {
                            length++;
                            t.setToken(TokenType.MP_ASSIGN);
                            state = -1;
                        } else {
                            state = -1;
                        }
                        break;
                    case 2:
                        if (c == '=') {
                            length++;
                            t.setToken(TokenType.MP_GEQUAL);
                            state = -1;
                        } else {
                            state = -1;
                        }
                        break;
                    case 3:
                        if (c == '=') {
                            length++;
                            t.setToken(TokenType.MP_LEQUAL);
                            state = -1;
                        } else if (c == '>') {
                            length++;
                            t.setToken(TokenType.MP_NEQUAL);
                            state = -1;
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

