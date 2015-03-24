/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micropascalcompiler;

/**
 *
 * @author arbiter34
 */
public class FSAResponse {
    private String token;
    private int length;
    private int lineCount;
    
    FSAResponse(String token, int length, int lineCount) {
        this.token = token;
        this.length = length;
        this.lineCount = lineCount;
    }
}
