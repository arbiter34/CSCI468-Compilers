/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micropascalcompiler;

import java.util.HashMap;

/**
 *
 * @author arbiter34
 */
public class ReservedWords {
    
    /*Deal with it, this is how we're building this.*/
    private static HashMap<String, TokenType> ReservedWordHashTable = new HashMap<String, TokenType>() {{
       put("and", TokenType.MP_AND);
        put("begin", TokenType.MP_BEGIN);
        put("boolean", TokenType.MP_BOOLEAN);
        put("div", TokenType.MP_DIV);
        put("do", TokenType.MP_DO);
        put("downto", TokenType.MP_DOWNTO);
        put("else", TokenType.MP_ELSE);
        put("end", TokenType.MP_END);
        put("false", TokenType.MP_FALSE);
        put("fixed", TokenType.MP_FIXED);
        put("float", TokenType.MP_FLOAT);
        put("for", TokenType.MP_FOR);
        put("function", TokenType.MP_FUNCTION);
        put("if", TokenType.MP_IF);
        put("integer", TokenType.MP_INTEGER);
        put("mod", TokenType.MP_MOD);
        put("not", TokenType.MP_NOT);
        put("or", TokenType.MP_OR);
        put("procedure", TokenType.MP_PROCEDURE);
        put("program", TokenType.MP_PROGRAM);
        put("read", TokenType.MP_READ);
        put("repeat", TokenType.MP_REPEAT);
        put("string", TokenType.MP_STRING);
        put("then", TokenType.MP_THEN);
        put("true", TokenType.MP_TRUE);
        put("to", TokenType.MP_TO);
        put("type", TokenType.MP_TYPE);
        put("until", TokenType.MP_UNTIL);
        put("var", TokenType.MP_VAR);
        put("while", TokenType.MP_WHILE);
        put("write", TokenType.MP_WRITE);
        put("writeln", TokenType.MP_WRITELN);
 
    }};
    
    /* This function serves two fold, to check if a key exists,
     * and to return it if it does.  Coincidentally, this is 
     * the exact method used by containsKey.
     */
    public static TokenType getReservedWord(String keyString) {
        TokenType t = ReservedWordHashTable.get(keyString.toLowerCase());
            return t;
    }
    
}
