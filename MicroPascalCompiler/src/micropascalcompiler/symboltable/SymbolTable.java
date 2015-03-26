/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micropascalcompiler.symboltable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author arbiter34
 */
public class SymbolTable {
    private HashMap<String, SymbolTableRecord> symbolTable;
    
    private int nestingLevel;
    
    private String label;
    
    private String scopeName;
    
    private long tableSize;
    
    public SymbolTable(String scopeName, String label, int nestingLevel) {
        symbolTable = new HashMap<>();
        this.nestingLevel = nestingLevel;
        this.label = label;
        this.scopeName = scopeName;
        this.tableSize = 0;
    }
    
    public void print() {
        System.out.println("\n\nScope Name    Nesting Level    Label");
        System.out.println(this.scopeName + "           " + this.nestingLevel + "            " + this.label);
        System.out.println("\nLexeme   Type    Kind    Mode    Size    Parameters    Offset");
        for (String key : symbolTable.keySet()) {
           SymbolTableRecord r = symbolTable.get(key);
           int numParams = r.getParameters() == null ? 0 : r.getParameters().size();
           System.out.println(r.getLexeme() + "\t" + r.getType() + "\t" + 
                   r.getKind() + "\t" + r.getMode() + 
                   "\t" + r.getSize() + "\t" + numParams + "\t" + r.getOffset());
        }
        System.out.print("\n\n");
    }

    public void insert(SymbolTableRecord rec) {
        
        rec.setOffset(this.tableSize);
        long recDataSize;
        if (rec.getKind() == RecordKind.VARIABLE) {
            recDataSize = DataSize.size[rec.getType().ordinal()];
        } else if (rec.getKind() == RecordKind.FUNCTION || rec.getKind() == RecordKind.PROCEDURE) {
            recDataSize = DataSize.size[RecordType.PROCEDURE.ordinal()];
        } else {
            recDataSize = 0;
        }
        this.tableSize += recDataSize;
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
