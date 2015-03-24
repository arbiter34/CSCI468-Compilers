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
public class AlphaFSA extends AbstractFSA {
    
    protected static AlphaFSA instance; 
    
    private AlphaFSA(){
    }
   
    
    public static AlphaFSA getInstance(BufferedReader inFile) {
        if (instance == null) {
            instance = new AlphaFSA();
        } 
        instance.setBufferedReader(inFile);
        return instance;
    }
    
    @Override
    public void run() {
        int length = 0;
        boolean stopFSA = false;
        int state = 0;
        
        t = new TokenContainer(TokenType.MP_ERROR, -1, 0, 0, length, true);
        char c;
        try {
            while (!stopFSA) {
                c = (char)this.inFile.read();
                switch (state) {
                    case 0:
                    	/* Our first character of a variable should be a letter, $, or _ if it is set
                    	 * our state to 1 to include digits*/
                    	if(Characters.isLetter(c) || c == '$' || c == '_') {
                    		this.t.setToken(TokenType.MP_IDENTIFIER);
                    		state = 1;
                    		length++;
                    	}
                    	/* If its a digit or anything else, error */
                    	else {
                    		state = -1;
                    	}
                        break;
                    case 1:
                    	/* If we passed state 0, we can now include digits in our variables */
                    	if(Characters.isDigit(c) || Characters.isLetter(c) || c == '$' || c == '_') {
                    		state = 1;
                    		length++;
                    	}
                    	else {
                    		state = -1;
                    	}
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
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

