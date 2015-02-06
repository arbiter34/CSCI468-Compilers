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
	private Dispatcher dispatcher;
	private BufferedReader inFile;

	public Scanner(String fileName) {
		this.fileName = fileName;
		try {
			this.inFile = new BufferedReader(new FileReader(this.fileName));
		} catch (Exception e) {
			System.out.println("FileNotFoundException");
		}
		this.dispatcher = new Dispatcher(this.inFile);
	}

	public void run() {
		while (true) {
			TokenContainer t = this.dispatcher.nextToken();
			if (t.getError()) {
				break;
			}
			char[] buf = new char[255];
			try {
				this.inFile.read(buf, 0, t.getLength());
			} catch (Exception e) {

			}
			System.out.println(t.getToken());
		}
	}
}
