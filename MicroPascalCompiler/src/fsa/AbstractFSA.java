/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fsa;

import java.io.FileInputStream;

/**
 *
 * @author arbiter34
 */
public abstract class AbstractFSA {
    private FileInputStream inFile;
    
    AbstractFSA(FileInputStream inFile) {
        this.inFile = inFile;
    }
    
    private char getNextChar() {
        byte[] bytes;
        this.inFile.readBytes(bytes, 1, 0);
        return (char)bytes[0];
        
    }
}
