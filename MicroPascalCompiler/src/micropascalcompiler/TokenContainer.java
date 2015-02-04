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
public class TokenContainer {
    private TokenType token;
    private int error_number;
    private int col;
    private int row;
    private int length;
    private boolean isError;
    
    public TokenContainer(TokenType token, int error_number, int row, int col, int length, boolean isError) {
        this.token = token;
        this.error_number = error_number;
        this.col = col;
        this.row = row;
        this.length = length;
        this.isError = isError;
    }
    
    public TokenType getToken() {
        return this.token;
    }
    
    public void setToken(TokenType token) {
        this.token = token;
    }
    
    public int getErrorNumber() {
        return this.error_number;
    }
    
    public void setErrorNumber(int error_number) {
        this.error_number = error_number;
    }
    
    public boolean isError() {
        return this.isError;
    }
    
    public int getLength() {
        return this.length;
    }
}
