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
    
    SymbolTableStack() {
        stack = new Stack<SymbolTable>();
    }
    
    public void generateSymbolTable(int nestingLevel, String label) {
        stack.push(new SymbolTable(nestingLevel, label));
    }
    
    public boolean symbolExistsInLocalScope(String symbol) {
        return stack.peek().exists(symbol);
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
    
    
}
