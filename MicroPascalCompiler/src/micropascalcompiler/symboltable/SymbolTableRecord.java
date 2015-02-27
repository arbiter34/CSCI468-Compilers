/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micropascalcompiler.symboltable;

import java.util.ArrayList;

/**
 *
 * @author arbiter34
 */
public class SymbolTableRecord {
    private String lexeme;

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public RecordType getType() {
        return type;
    }

    public void setType(RecordType type) {
        this.type = type;
    }

    public RecordKind getKind() {
        return kind;
    }

    public void setKind(RecordKind kind) {
        this.kind = kind;
    }

    public RecordMode getMode() {
        return mode;
    }

    public void setMode(RecordMode mode) {
        this.mode = mode;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<RecordParameter> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<RecordParameter> parameters) {
        this.parameters = parameters;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
    private RecordType type;
    private RecordKind kind;
    private RecordMode mode;
    private int size;
    private ArrayList<RecordParameter> parameters;
    private long offset;
}
