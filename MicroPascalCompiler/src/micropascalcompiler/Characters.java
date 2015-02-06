package micropascalcompiler;

/**
 * This class is to check whether a character is a letter, number, or whitespace.
 * 
 * @author Shane
 *
 */
public class Characters {
	
	private static final String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWQYZ";
	private static final String digits = "0123456789";
    private static final String whitespaces = " \r\n\t";
	
	public static boolean isLetter(char c)
	{
		return alphabet.contains(""+c);
	}
	
	public static boolean isDigit(char c)
	{
		return digits.contains(""+c);
	}
	
	public static boolean isWhitespace(char c)
	{
		return c == ' ' || c == '\t';
	}

}
