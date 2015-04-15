package micropascalcompiler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class MPCIDEMAIN {
	public static void main(String... args)
	{
		String source = "fibonacci.pas";
		Scanner scanner = new Scanner(source);
        try {
			Parser parser = new Parser(scanner, new PrintWriter(source+"_parse.txt"), source);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
