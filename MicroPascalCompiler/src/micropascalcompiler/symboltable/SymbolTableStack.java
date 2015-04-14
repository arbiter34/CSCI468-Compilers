/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micropascalcompiler.symboltable;

import java.util.Stack;

/**
 *
 * @author arbiter34
 */
public class SymbolTableStack {
    private Stack<SymbolTable> stack = null;
    private int nestingLevel = 0;
    private int previousRecordNestingLevel = -1;

    public int getPreviousRecordNestingLevel() {
        int ret = previousRecordNestingLevel;
        previousRecordNestingLevel = -1;
        return ret;
    }
    
    public SymbolTableStack() {
        stack = new Stack<SymbolTable>();
    }
    
    public void generateSymbolTable(String scopeName, String label) {        
        stack.push(new SymbolTable(scopeName.toLowerCase(), label.toLowerCase(), nestingLevel));
        nestingLevel++;
    }
    
    public void removeSymbolTable() {
        stack.pop();
        nestingLevel--;
    }
    
    public void print() {
        for (SymbolTable st : stack) {
            st.print();
            System.out.println("");
        }
    }
    
    public SymbolTable getCurrentTable() {
        return stack.peek();
    }
    
    public void printCurrentTable() {
        stack.peek().print();
    }
    
    public boolean symbolExistsInLocalScope(String symbol) {
        symbol = symbol.toLowerCase();
        if (stack.empty()) {
            return false;
        }
        return stack.peek().exists(symbol);
    }
    
    public boolean scopeExists(String scopeName) {
        scopeName = scopeName.toLowerCase();
        if (stack.empty()) {
            return false;
        }
        int depth = stack.indexOf(stack.peek());
        for (int i = depth; i >= 0; i--) {
           if (stack.get(i).getScopeName().equalsIgnoreCase(scopeName)) {
               return true;
           } 
        }
        return false;
    }
    
    public SymbolTableRecord getSymbolInScope(String symbol) {
        symbol = symbol.toLowerCase();
        if (stack.empty()) {
            return null;
        }
        int depth = stack.indexOf(stack.peek());
        for (int i = depth; i >= 0; i--) {
           if (stack.get(i).exists(symbol)) {
               previousRecordNestingLevel = stack.get(i).getNestingLevel();
               return stack.get(i).getRecord(symbol);
           } 
        }
        return null;
    }
    
    public void insertSymbolInScope(SymbolTableRecord rec) {
        if (!symbolExistsInLocalScope(rec.getLexeme().toLowerCase())) {
            rec.setLexeme(rec.getLexeme().toLowerCase());
            stack.peek().insert(rec);
        }
    }
    
    public String getScopeLabel(String scopeName) {
        scopeName = scopeName.toLowerCase();
        if (stack.empty()) {
            return null;
        }
        int depth = stack.indexOf(stack.peek());
        for (int i = depth; i >= 0; i--) {
           if (stack.get(i).getScopeName() == scopeName) {
               previousRecordNestingLevel = stack.get(i).getNestingLevel();
               return stack.get(i).getLabel();
           } 
        }
        return null;
    }
    
}
