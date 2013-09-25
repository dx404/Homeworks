import java.lang.*;
import java.util.StringTokenizer;
import java.io.*;

public class Parse {
	public static final String[] errMsg = {
		"ERROR -- Invalid Method token.",
		"ERROR -- Invalid Absolute-Path token.",
		"ERROR -- Invalid HTTP-Version token.",
		"ERROR -- Spurious token before CRLF."
	};
	public static boolean methodChecking(String token, String method){
		if(token.equals(method)) //Checking with designated method
			return true;
		return false;
	}
	public static boolean pathChecking(String token){ 
		//Check if path is valid 
		if(token == null)
			return false;
		if(token.charAt(0) != '/')
			return false;
		if(token.length() == 1)
			return true;
		//Checking if the first character is '/'
		for(int i = 1; i < token.length(); i++){
			char tmp = token.charAt(i);
			if(Character.isLetterOrDigit(tmp) || tmp == '.' || tmp == '_' || tmp == '/')
				continue;
			return false;
		} //Checking the rest characters
		return true;
	}
	public static boolean versionChecking(String token, String protocol){
		if(token.length() < protocol.length() + 4) 
			return false; 
		//At least 4 additional characters "/?.?" are needed 
		String headOfVersion = token.substring(0, protocol.length() + 1);
		if(!headOfVersion.equals(protocol + "/")) 
			return false;
		//Checking the header of Version
		if(!Character.isDigit(token.charAt(protocol.length() + 1)))
			return false; //the character immediate after '/' must be a digit
		boolean dotHit = false; //flag of the '.', which occurs exactly once
		for(int i = protocol.length() + 2; i < token.length() - 1; i++){
			if(!Character.isDigit(token.charAt(i))){
				if(dotHit)
					return false; //More than one dot
				if(token.charAt(i) == '.'){
					dotHit = true; //first dot is encountered. 
					continue;
				}
				return false;		
			}
		}
		if(dotHit && Character.isDigit(token.charAt(token.length()-1)))
			return true;  //Exactly one dot and the last character is a digit
		else
			return false; 
	}
	
	public static void main(String[] args) throws IOException{
		String inputLine;
		String[] tokensTank = new String[3]; //storage of tokens
		
		BufferedReader standardIn = new BufferedReader(new InputStreamReader(System.in));
		inputLine = standardIn.readLine();
		StringTokenizer inputTokens = new StringTokenizer(inputLine);
		//standard inputing
		
		System.out.println(inputLine);
		if(inputLine.length()>=1){
			if(Character.isWhitespace(inputLine.charAt(0))){
				System.out.println(errMsg[0]);
				return;
			}
		}	
	     if(inputTokens.hasMoreTokens()){
	    	 	tokensTank[0] = inputTokens.nextToken();
	    	 	if(!methodChecking(tokensTank[0], "GET")){
	    	 		System.out.println(errMsg[0]);
		    	 	return;
	    	 	}
	     }
	     else{
	    	 	System.out.println(errMsg[0]); //Missing
	    	 	return;
	     }
	     //The above code is checking the first token
	     if(inputTokens.hasMoreTokens()){
	    	 	tokensTank[1] = inputTokens.nextToken();
	    	 	if(!pathChecking(tokensTank[1])){
	    	 		System.out.println(errMsg[1]);
		    	 	return;
	    	 	}
	     }
	     else{
	    	 	System.out.println(errMsg[1]);
	    	 	return;
	     }
	   //The above code is checking the 2nd token
	     if(inputTokens.hasMoreTokens()){
	    	 	tokensTank[2] = inputTokens.nextToken();
	    	 	if(!versionChecking(tokensTank[2], "HTTP")){
	    	 		System.out.println(errMsg[2]);
		    	 	return;
	    	 	}
	     }
	     else{
	    	 	System.out.println(errMsg[2]);
	    	 	return;
	     }
	   //The above code is checking the 3rd token
	     if(inputTokens.hasMoreTokens()){
	    	 	System.out.println(errMsg[3]);
	    	 	return;
	     }
	   //The above code is checking if there is spurious token
	     System.out.println("Method = " + tokensTank[0]);
	     System.out.println("Request-URL = " + tokensTank[1]);
	     System.out.println("HTTP-Version = " + tokensTank[2]);
	}
}
