/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fsa;

import java.util.HashMap;
import micropascalcompiler.TokenType;


/**
 *
 * @author arbiter34
 */
public class FSA {
    
    public static final HashMap<String, HashMap<String, TokenType>> FSAHash = 
            new HashMap<String, HashMap<String, TokenType>>(){{
             put("<", new HashMap<String, TokenType>() {{
                put("<", TokenType.MP_AND); 
                put("<=", TokenType.MP_BEGIN);
             }}); 
             put ("\\d", new HashMap<String, TokenType>() {{
                 put("\\d+", TokenType.MP_EOF);
                 put("\\d+(\\.)(\\d+)?", TokenType.MP_IDENTIFIER);
             }});
            }};
                           
    
}
