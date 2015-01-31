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
public class Token {
    private String token;
    private int error_number;
    private int col;
    private int row;
    private boolean isError;
    
    Token(String token, int error_number, int col, int row, boolean isError) {
        this.token = token;
        this.error_number = error_number;
        this.col = col;
        this.row = row;
        this.isError = isError;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public int getErrorNumber() {
        return this.error_number;
    }
    
    public void setErrorNumber(int error_number) {
        this.error_number = error_number;
    }
}
