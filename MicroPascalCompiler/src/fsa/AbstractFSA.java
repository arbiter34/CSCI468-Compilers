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
public abstract class AbstractFSA {
    protected BufferedReader inFile;
    protected TokenContainer t;
    
    protected AbstractFSA(){}
    
    protected void setBufferedReader(BufferedReader inFile) {
        this.inFile = inFile;
    }
    
    public final void execute() {
        if (mark()) {
            run();
            reset();
        }
    }
    
    private boolean mark() {
        try {
            inFile.mark(4096);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    private boolean reset() {
        try {
            inFile.reset();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public abstract void run();
    
    public TokenContainer getResult() {
        return this.t;
    }
    
}
