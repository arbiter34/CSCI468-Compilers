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
    
    public SymbolTableStack() {
        stack = new Stack<SymbolTable>();
    }
    
    public void generateSymbolTable(String scopeName, String label) {
        stack.push(new SymbolTable(scopeName, label, nestingLevel));
        nestingLevel++;
    }
    
    public void removeSymbolTable() {
        stack.pop();
        nestingLevel--;
    }
    
    public void print() {
        for (SymbolTable st : stack) {
            st.print();
        }
    }
    
    public boolean symbolExistsInLocalScope(String symbol) {
        return stack.peek().exists(symbol);
    }
    
    public boolean scopeExists(String scopeName) {
        int depth = stack.indexOf(stack.peek());
        for (int i = depth; i >= 0; i--) {
           if (stack.get(i).getScopeName().equalsIgnoreCase(scopeName)) {
               return true;
           } 
        }
        return false;
    }
    
    public SymbolTableRecord getSymbolInScope(String symbol) {
        int depth = stack.indexOf(stack.peek());
        for (int i = depth; i >= 0; i--) {
           if (stack.get(i).exists(symbol)) {
               return stack.get(i).getRecord(symbol);
           } 
        }
        return null;
    }
    
    public void insertSymbolInScope(SymbolTableRecord rec) {
        if (!symbolExistsInLocalScope(rec.getLexeme())) {
            stack.peek().insert(rec);
        }
    }
    
}
