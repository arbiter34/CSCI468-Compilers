/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fsa;

import java.io.BufferedReader;
import micropascalcompiler.Characters;
import micropascalcompiler.TokenContainer;

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
    
    
    public void run() {
        int length = 0;
        boolean accept = false;
        int state = 0;
        char c;
        try {
            while (!Characters.isWhitespace(c = (char)this.inFile.read())) {
                switch (state) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
            }
        } catch (Exception e) {
            
        }
        
        
    }
    
    
}

