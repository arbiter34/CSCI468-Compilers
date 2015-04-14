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
import micropascalcompiler.TokenType;
import micropascalcompiler.symboltable.*;

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
    private void addF(String src1, String src2, String dst)
    {
        out.println("addf " + src1 + " " + src2 + " " + dst);
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
    
    public void gen_label(String label) {
        label(label);
    }
    
    public void gen_activation_rec() {

            SymbolTable tbl = symbolTableStack.getCurrentTable();
            int nestingLevel = tbl.getNestingLevel();
            String block = "" + nestingLevel;
            int variableCount = tbl.getVariableCount();
            int parameterCount = tbl.getParameterCount();
            String register = null;
            String offset = null;

            comment(tbl.getScopeName() + " start"); //; start
            add("SP", "#" + variableCount, "SP"); //reserveSpace for the variables in the program
            register = "D" + (tbl.getNestingLevel());
            if (nestingLevel == 0) {
                sub("SP", "#" + (variableCount + parameterCount), register);
            } else {
                offset = "-" + (variableCount + parameterCount + 2) + "(SP)"; //one slot for return address, one slot for the old register value
                move(register, offset); //moves the current register into the space reserved for the old register
                sub("SP", "#" + (variableCount + parameterCount + 2), register); //calculates the new register value as the first position in the AR
                comment("activation end");                
            }
            comment("activation end");                        
    }

    public void gen_deactivation_rec()
    {
        SymbolTable tbl = symbolTableStack.getCurrentTable();
        int variableCount = tbl.getVariableCount();
        int parameterCount = tbl.getParameterCount();
        int nestingLevel = tbl.getNestingLevel();
        String register = "D" + nestingLevel;
        String offset = "-" + (variableCount + parameterCount + 2) + "(SP)"; //one slot for return address, one slot for the old register value
        comment("deactivation start");
        move("D" + nestingLevel, "SP"); //removes the variables
        if (nestingLevel == 0) {
        } else {
            move(offset, register); //moves the old register value back into the register       
            ret();     
        }
        comment(tbl.getScopeName() + " end"); //; Program1 end
    }
    
    public void gen_branch_false(String label) {
        branchFalse(label);
    }
    
    public void gen_mul_op(TokenType t, RecordType r) {
        switch (t) {
            case MP_AND:
                and();
                break;
                
            case MP_MOD:
                if (r == RecordType.FLOAT) {
                    modStackF();
                } else {
                    modStackI();
                }
                break;
             
            case MP_FLOAT_DIVIDE:
                divStackF();
                break;
                
                
            case MP_DIV:
                divStackI();
                break;
                
                
            case MP_TIMES:
                if (r == RecordType.FLOAT) {
                    mulStackF();
                } else {
                    mulStackI();
                }
                break;
            
            default:
                //semanticError("");
        }
    }
    
    public void gen_add_op(TokenType t, RecordType r) {
        switch (t) {
            case MP_OR: //85 AddingOperator -> mp_or
                or();
                break;
            case MP_MINUS: //84 AddingOperator -> mp_minus
                if (r == RecordType.FLOAT) {
                    subStackF();
                } else {
                    subStackI();
                }
                break;
            case MP_PLUS: //83 AddingOperator -> mp_plus
                if (r == RecordType.FLOAT) {
                    addStackF();
                } else {
                    addStackI();
                }
                break;
            default:
                
        }
    }
    
    public void gen_bool_expr(TokenType t, RecordType r) {
        switch (t) {
            case MP_NEQUAL:
                if (r == RecordType.FLOAT) {
                    notEqualF();
                } else if (r == RecordType.INTEGER) {
                    notEqualI();
                }
                break;
            case MP_GEQUAL:
                if (r == RecordType.FLOAT) {
                    greaterEqualF();
                } else if (r == RecordType.INTEGER) {
                    greaterEqualI();
                }
                break;
            case MP_LEQUAL:
                if (r == RecordType.FLOAT) {
                    lessEqualF();
                } else if (r == RecordType.INTEGER) {
                    lessEqualI();
                }
                break;
            case MP_GTHAN:
                if (r == RecordType.FLOAT) {
                    greaterThanF();
                } else if (r == RecordType.INTEGER) {
                    greaterThanI();
                }
                break;
            case MP_LTHAN:
                if (r == RecordType.FLOAT) {
                    lessThanF();
                } else if (r == RecordType.INTEGER) {
                    lessThanI();
                }
                break;
            case MP_EQUAL: 
                if (r == RecordType.FLOAT) {
                    equalF();
                } else if (r == RecordType.INTEGER) {
                    equalI();
                }
                break;
        }
    }
    
    public void gen_branch_unconditional(String branchLabel) {
        branchUnconditional(branchLabel);
    }
    
    public String getNestingLevelString(int nestingLevel) {
        return "(D" + Integer.toString(nestingLevel) + ")";
    }
    
    public void gen_id_increment(RecordType r, long offset, int nestingLevel) {
        if (r == RecordType.FLOAT) {
            addF(Long.toString(offset) + getNestingLevelString(nestingLevel), "#1.0", Long.toString(offset) + getNestingLevelString(nestingLevel));
        } else if (r == RecordType.INTEGER) {
            add(Long.toString(offset) + getNestingLevelString(nestingLevel), "#1", Long.toString(offset) + getNestingLevelString(nestingLevel));
        }
    }
    
    public void gen_id_push(long offset, int nestingLevel) {
        push(Long.toString(offset) + getNestingLevelString(nestingLevel));
    }
    
    public void gen_id_pop(long offset, int nestingLevel) {
        pop(Long.toString(offset) + getNestingLevelString(nestingLevel));
    }
    
    public void gen_lit_push(String value) {
        push("#" + value);
    }


    /**
     * 
     */
    public void gen_halt()
    {
        halt();
    }
    
    public void gen_read_op(RecordType r, long offset, int nestingLevel) {
        if (r == RecordType.INTEGER) {
            readI(Long.toString(offset) + getNestingLevelString(nestingLevel));
        } else if (r == RecordType.FLOAT) {
            readF(Long.toString(offset) + getNestingLevelString(nestingLevel));
        } else if (r == RecordType.STRING) {
            readS(Long.toString(offset) + getNestingLevelString(nestingLevel));
        }
    }
    
    public void gen_write_op() {
        writeStack();
    }
}
