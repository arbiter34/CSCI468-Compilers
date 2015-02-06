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
    private int errorNumber;
    private int col;
    private int row;
    private int length;
    private boolean isError;

	public TokenContainer(TokenType token, int errorNumber, int row, int col, int length, boolean isError) {
        this.token = token;
        this.errorNumber = errorNumber;
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
    
    public void setErrorNumber(int errorNumber) {
        this.errorNumber = errorNumber;
    }
    
    public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getErrorNumber() {
		return errorNumber;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	public int getLength() {
		return this.length;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}
	
	public boolean getError()
	{
		return this.isError;
	}
	
}
