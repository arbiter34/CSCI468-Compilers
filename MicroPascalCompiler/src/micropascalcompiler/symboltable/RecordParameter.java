/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micropascalcompiler.symboltable;

/**
 *
 * @author arbiter34
 */
public class RecordParameter {
    private String lexeme;
    private RecordMode mode;
    private RecordType type;
    
    public RecordParameter(String lexeme, RecordMode mode, RecordType type) {
        this.lexeme = lexeme;
        this.mode = mode;
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public RecordMode getMode() {
        return mode;
    }

    public void setMode(RecordMode mode) {
        this.mode = mode;
    }

    public RecordType getType() {
        return type;
    }

    public void setType(RecordType type) {
        this.type = type;
    }
}
