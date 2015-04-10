/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micropascalcompiler.semanticanalyzer;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import micropascalcompiler.symboltable.SymbolTable;
import micropascalcompiler.symboltable.SymbolTableStack;

/**
 *
 * @author alperst
 */
public class SemanticAnalyzer {
    private PrintWriter out;
    private SymbolTableStack symbolTableStack;
    
    public SemanticAnalyzer(SymbolTableStack stack, String fileName) {
        this.symbolTableStack = stack;
        try {
            out = new PrintWriter(new FileOutputStream(fileName), true);
        } catch(Exception e) {
            System.out.println("Whoops! Couldn't open output file.");
            System.exit(1);
        }
    }
    
    /*
     * VM ASM methods section begin
     */

    /**
     * Pushes the value at the memory location to the stack
     * 
     * @param memLoc
     *            string with format from VM definition e.g (0(D0), #"string", etc)
     */
    private void push(String memLoc)
    {
        out.println("push " + memLoc);
    }

    /**
     * Pops the value from the stack into the memory location
     * 
     * @param memLoc
     *            string with format from VM definition e.g (0(D0), #"string", etc)
     */
    private void pop(String memLoc)
    {
        out.println("pop " + memLoc);
    }

    /**
     * 
     * @param src
     * @param dst
     */
    private void move(String src, String dst)
    {
        out.println("mov " + src + " " + dst);
    }

    /**
     * 
     * @param src1
     * @param src2
     * @param dst
     */
    private void add(String src1, String src2, String dst)
    {
        out.println("add " + src1 + " " + src2 + " " + dst);
    }

    /**
     * 
     * @param src1
     * @param src2
     * @param dst
     */
    private void sub(String src1, String src2, String dst) {
        out.println("sub " + src1 + " " + src2 + " " + dst);
    }

    /**
     * 
     */
    private void not()
    {
        out.println("nots");
    }

    /**
     * 
     */
    private void and()
    {
        out.println("ands");
    }

    /**
     * 
     */
    private void or()
    {
        out.println("ors");
    }

    private void mulStackI()
    {
        out.println("muls");
    }

    private void divStackI()
    {
        out.println("divs");
    }

    private void modStackI()
    {
        out.println("mods");
    }

    private void mulStackF()
    {
        out.println("mulsf");
    }

    private void divStackF()
    {
        out.println("divsf");
    }

    private void modStackF()
    {
        out.println("modsf");
    }

    private void subStackI()
    {
        out.println("subs");
    }

    private void addStackI()
    {
        out.println("adds");
    }

    private void subStackF()
    {
        out.println("subsf");
    }

    private void addStackF()
    {
        out.println("addsf");
    }

    private void negateStackI()
    {
        out.println("negs");
    }

    private void negateStackF()
    {
        out.println("negsf");
    }

    private void notEqualI()
    {
        out.println("cmpnes");
    }

    private void greaterEqualI()
    {
        out.println("cmpges");
    }

    private void lessEqualI()
    {
        out.println("cmples");
    }

    private void greaterThanI()
    {
        out.println("cmpgts");
    }

    private void lessThanI()
    {
        out.println("cmplts");
    }

    private void equalI()
    {
        out.println("cmpeqs");
    }

    private void notEqualF()
    {
        out.println("cmpnesf");
    }

    private void greaterEqualF()
    {
        out.println("cmpgesf");
    }

    private void lessEqualF()
    {
        out.println("cmplesf");
    }

    private void greaterThanF()
    {
        out.println("cmpgtsf");
    }

    private void lessThanF()
    {
        out.println("cmpltsf");
    }

    private void equalF()
    {
        out.println("cmpeqsf");
    }

    private void castStackToI()
    {
        out.println("castsi");
    }

    private void castStackToF()
    {
        out.println("castsf");
    }

    private void writeStack()
    {
        out.println("wrts");
    }

    private void writelnStack()
    {
        out.println("wrtlns");
    }

    private void readI(String offset)
    {
        out.println("rd " + offset);
    }

    private void readF(String offset)
    {
        out.println("rdf " + offset);
    }

    private void readS(String offset)
    {
        out.println("rds " + offset);
    }

    /**
     * Halts program execution
     */
    private void halt()
    {
        out.println("hlt");
    }

    private void label(String label) {
        out.println(label + ":");
    }

    private void branchTrue(String label) {
        out.println("brts " + label);
    }

    private void branchFalse(String label) {
        out.println("brfs " + label);
    }

    private void branchUnconditional(String label) {
        out.println("br " + label);
    }

    private void ret()
    {
        out.println("ret");
    }

    private void call(String label)
    {
        out.println("call " + label);
    }

    /**
     * Prints a comment to the VM code (for human readability)
     * 
     * @param comment
     */
    private void comment(String comment)
    {
        out.println("\t; " + comment);
    }

    /*
     * VM ASM methods section end
     */
    
    public void gen_activation_rec() {

            SymbolTable tbl = symbolTableStack.getCurrentTable();
            String block = "" + tbl.getNestingLevel();
            int variableCount = tbl.getVariableCount();
            int parameterCount = tbl.getParameterCount();
            String register = null;
            String offset = null;

            comment(tbl.getScopeName() + " start"); //; Program1 start
            add("SP", "#" + variableCount, "SP"); //reserveSpace for the variables in the program
            register = "D" + tbl.getNestingLevel();
            offset = "-" + (variableCount + parameterCount + 2) + "(SP)"; //one slot for return address, one slot for the old register value
            move(register, offset); //moves the current register into the space reserved for the old register
            sub("SP", "#" + (variableCount + parameterCount + 2), register); //calculates the new register value as the first position in the AR
            comment("activation end");

        
    }

    public void gen_deactivation_rec()
    {
        SymbolTable tbl = symbolTableStack.getCurrentTable();
        int variableCount = tbl.getVariableCount();
        int parameterCount = tbl.getParameterCount();
        String register = "D" + tbl.getNestingLevel();
        String offset = "-" + (variableCount + parameterCount + 2) + "(SP)"; //one slot for return address, one slot for the old register value
        comment("deactivation start");
        move(offset, register); //moves the old register value back into the register
        sub("SP", "#" + (variableCount), "SP"); //removes the variables
        ret();
        comment(tbl.getScopeName() + " end"); //; Program1 end
    }
    
    public void gen_branch_false(String label) {
        branchFalse(label);
    }


    /**
     * 
     */
    public void gen_halt()
    {
        halt();
    }
}
