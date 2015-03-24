/*
 * Stuff
 */
package micropascalcompiler;

import fsa.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author arbiter34
 */
public class Scanner {
	private String fileName;
	private final Dispatcher dispatcher;
	private BufferedReader inFile;
        private TokenContainer currentToken;
        private String currentLexeme = "";

	public Scanner(String fileName) {
		this.fileName = fileName;
		try {
			this.inFile = new BufferedReader(new FileReader(this.fileName));
		} catch (Exception e) {
			System.out.println("FileNotFoundException");
		}
		this.dispatcher = new Dispatcher(this.inFile);
	}

	public TokenContainer getNextToken() {
            currentToken = this.dispatcher.nextToken();
            if (currentToken.getToken() == TokenType.MP_EOF) {
                currentLexeme = "";
                return currentToken;
            }
            char[] buf = new char[255];
            try {
                    this.inFile.read(buf, 0, currentToken.getLength());
            } catch (Exception e) {

            }
            //String s = new String(buf); // <-----this screws up the string somehow.
            currentLexeme = "";
            for (int i = 0; i < currentToken.getLength(); i++) {
                currentLexeme += buf[i];
            }
            if (currentToken.getToken() == TokenType.MP_IDENTIFIER) {
                TokenType temp = ReservedWords.getReservedWord(currentLexeme);
                if (temp != null) {
                    currentToken.setToken(temp);
                }
            }
            currentToken.setLexeme(currentLexeme);
            //System.out.println("Type: " + currentToken.getToken() + " \nLexeme: " + currentToken.getLexeme() + " \nRow: " + currentToken.getRow() + " \nCol: " + currentToken.getCol());
            return currentToken;
	}

}
