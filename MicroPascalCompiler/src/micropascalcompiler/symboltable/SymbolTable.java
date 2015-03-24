/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micropascalcompiler.symboltable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author arbiter34
 */
public class SymbolTable {
    private HashMap<String, SymbolTableRecord> symbolTable;
    
    private int nestingLevel;
    
    private String label;
    
    private String scopeName;
    
    public SymbolTable(String scopeName, String label, int nestingLevel) {
        symbolTable = new HashMap<>();
        this.nestingLevel = nestingLevel;
        this.label = label;
        this.scopeName = scopeName;
    }
    
    public void print() {
        for (String key : symbolTable.keySet()) {
           SymbolTableRecord r = symbolTable.get(key);
           System.out.println(r.getKind() + " " + r.getLexeme() + " " + 
                   r.getClass() + " " + r.getMode() + " " + r.getOffset() + 
                   " " + r.getSize() + " " + r.getType());
        }
    }

    public void insert(SymbolTableRecord rec) {
        symbolTable.put(rec.getLexeme(), rec);
    }
    
    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
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
