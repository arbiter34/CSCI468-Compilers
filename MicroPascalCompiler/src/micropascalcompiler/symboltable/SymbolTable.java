/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micropascalcompiler.symboltable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author arbiter34
 */
public class SymbolTable {
    private HashMap<String, SymbolTableRecord> symbolTable;
    
    private int nestingLevel;
    
    private String label;
    
    public SymbolTable(int nestingLevel, String label) {
        symbolTable = new HashMap<>();
        this.nestingLevel = nestingLevel;
        this.label = label;
    }
    
    public boolean exists(String key) {
        return this.symbolTable.containsKey(key);
    }

    public int getNestingLevel() {
        return nestingLevel;
    }

    public String getLabel() {
        return label;
    }
    
    public SymbolTableRecord getRecord(String key) {
        return this.symbolTable.get(key);
    }
    
    public RecordKind getRecordKind(String key) {
        return this.symbolTable.get(key).getKind();
    }
    
    public RecordType getRecordType(String key) {
        return this.symbolTable.get(key).getType();
    }
    
    public String getRecordLexeme(String key) {
        return this.symbolTable.get(key).getLexeme();
    }
    
    public RecordMode getRecordMode(String key) {
        return this.symbolTable.get(key).getMode();
    }
    
    public int getRecordSize(String key) {
        return this.symbolTable.get(key).getSize();
    }
    
    public ArrayList<RecordParameter> getRecordParameters(String key) {
        return this.symbolTable.get(key).getParameters();
    }
    
    public long getRecordOffset(String key) {
        return this.symbolTable.get(key).getOffset();
    }
}
