package utils;

public class StringUtilities
{
	public static int getIndentation(String s)
	{
		int num = 0;
		
		while (num < s.length())
		{
			if (s.charAt(num) == ' ')
				num++;
			else
				return num;
		}
		
		return num;
	}
	
	public static String getFromQuotes(String s)
	{
		int first = s.indexOf('"'), second = s.indexOf('"', first+1);
		return s.substring(first+1, second);
	}
}
