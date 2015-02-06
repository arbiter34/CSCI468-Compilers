/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fsa;

import java.io.BufferedReader;
import micropascalcompiler.TokenContainer;

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
    
    
    public void run() {
        
        
    }
    
    
}

