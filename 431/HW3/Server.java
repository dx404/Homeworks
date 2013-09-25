import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * COMP 431 HW3 duo.zhao@unc.edu
 * Here is Server portion.
 * The Server is sending request
 * @author duozhao
 */
public class Server implements Runnable{

	private ServerSocket serverSocket;
	private String requestLine;
	private ArrayList<String> requestHeaders;

	private static final int serverTimeOut = Integer.MAX_VALUE;
	public static final int timesToTry = 3;

	public Server(int port) throws IOException{ //constructor
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(serverTimeOut);
	}

	/**
	 * main method, try to create server socket and start server thread
	 * @param args args[0] is for the listening socket port number
	 */
	public static void main(String [] args)
	{
		if(args.length==0){ //checking if socket number is provided
			System.out.println("port number must be specified: ");
			System.exit(0);
		}
		int port = Integer.parseInt(args[0]);

		/**
		 * try to create server socket if it fails, try to recover
		 */
		int triesRemaining = timesToTry;
		while(triesRemaining > 0){
			try{
				if(triesRemaining != timesToTry){
					System.out.println("Try to re-create Server Socket at port " + port + " Trial: " + (triesRemaining) + "/" + timesToTry);
					Thread.sleep(1000);
				}
				Server t = new Server(port);
				new Thread(t).start();
				break;
			}catch(IOException e){
				triesRemaining--;
				if(triesRemaining == 0){
					e.printStackTrace();
					System.out.println("Server Socket Creation Failed");
					System.exit(0);
				}
				else{
					System.out.println("Failed to create Server Socket...Try to Recover: ");
					continue;
				}
			}catch(InterruptedException e) {
				triesRemaining--;
				e.printStackTrace();
				System.out.println("Sleeping was interrupted...Try to re-Sleep: ");
				continue;

			}
		}
	}

	public void run(){
		while(true){
			//creating server response headers
			Calendar cal = Calendar.getInstance();
			ArrayList<String> responseHeaders = new ArrayList<String>();
			responseHeaders.add("Date: " + cal.getTime());
			responseHeaders.add("Apache/2.2.15 (" + System.getProperty("os.name") + ")");
			responseHeaders.add("Vary: Accept-Encoding");
			responseHeaders.add("Connection: close");
			responseHeaders.add("Content-Type: text/html; charset=iso-8859-1");

			requestLine = new String();
			requestHeaders = new ArrayList<String>();
			try{
				//create a socket from the listening server socket
				Socket server = serverSocket.accept();

				//Create input stream and readers
				DataInputStream in = new DataInputStream(server.getInputStream());
				BufferedReader socketIn = new BufferedReader(new InputStreamReader(in));

				//Crate output stream and writers
				OutputStream outToClient = server.getOutputStream();
				PrintWriter outWriter = new PrintWriter(new OutputStreamWriter(outToClient));

				//read from client the requestLine and the header line
				requestLine = socketIn.readLine(); 
				for(String headerLine = socketIn.readLine(); 
						!headerLine.isEmpty();
						headerLine = socketIn.readLine()){
					requestHeaders.add(headerLine + "\r\n");
				}

				/** invoke toPreAnalyzeRequest to analyze the request line and headers
				 * Splitting the returned info to statusCode and statusSupplement
				 */
				String statusInfo = toPreAnalyzeRequest(requestLine, requestHeaders);
				int statusCode = Integer.parseInt(statusInfo.substring(0, 3));
				String statusSupplement = (statusInfo.length() > 3) ?
						new String(statusInfo.substring(4)) : new String();

				switch(statusCode){
				case 200:{ //200 OK case
					outWriter.println("HTTP/1.1 200 OK");
					File fileToScan = new File(statusSupplement);
					responseHeaders.add("Content-Length: " + fileToScan.length());
					for(Iterator<String> itrResponseHeaders = responseHeaders.iterator(); 
							itrResponseHeaders.hasNext();){
						outWriter.println(itrResponseHeaders.next());
					}
					outWriter.print("\r\n");

					try{
						Scanner inputStream = new Scanner(fileToScan);
						while(inputStream.hasNextLine()){
							outWriter.println(inputStream.nextLine());
						}
					}
					catch(FileNotFoundException e){
						outWriter.print("HTTP/1.1 404 Not Found: " + fileToScan.getPath() +"\r\n\r\n");
					}
				}
				break;

				case 400:{
					int errorCategory = Integer.parseInt(statusSupplement);
					outWriter.print("HTTP/1.1 400 Bad Request: " + errMsg[errorCategory] + "\r\n");
					for(Iterator<String> itrResponseHeaders = responseHeaders.iterator(); 
							itrResponseHeaders.hasNext();){
						outWriter.println(itrResponseHeaders.next());
					}
					outWriter.print("\r\n");
				}
				break;

				case 404:{
					outWriter.print("HTTP/1.1 404 Not Found: " + statusSupplement + "\r\n");
					for(Iterator<String> itrResponseHeaders = responseHeaders.iterator(); 
							itrResponseHeaders.hasNext();){
						outWriter.println(itrResponseHeaders.next());
					}
					outWriter.print("\r\n");
				}
				break;

				case 501:{
					outWriter.print("HTTP/1.1 501 Not Implemented: "+ statusSupplement + "\r\n");
					for(Iterator<String> itrResponseHeaders = responseHeaders.iterator(); 
							itrResponseHeaders.hasNext();){
						outWriter.println(itrResponseHeaders.next());
					}
					outWriter.print("\r\n");
				}
				break;

				default:{
					outWriter.print("HTTP/1.1 "+ "Undefined response number: " + statusCode + "\r\n");
					for(Iterator<String> itrResponseHeaders = responseHeaders.iterator(); 
							itrResponseHeaders.hasNext();){
						outWriter.println(itrResponseHeaders.next());
					}
					outWriter.print("\r\n");
				}
				break;
				}

				outWriter.flush();

				socketIn.close();
				outWriter.close();
				server.close();
			}
			catch(SocketTimeoutException s){
				s.printStackTrace();
				System.out.println("Socket creation timed out! (>" + serverTimeOut + ")");
				continue;
			}
			catch(IOException e){
				e.printStackTrace();
				continue;
			}
		}
	}

	public static final String[] errMsg = {
		"ERROR -- Invalid Method token.",
		"ERROR -- Invalid Absolute-Path token.",
		"ERROR -- Invalid HTTP-Version token.",
		"ERROR -- Spurious token before CRLF."
	};

	/**
	 * PreAnalyzeRequest method to generate the response code
	 * currently supporting 200, 400, 404, 501
	 * @param requestLine: the GET request line
	 * @param requestHeaders: e.g Host: From: ect
	 * @return format [0-9][0-9][0-9]-[0-9a-zA-z/_.]* 
	 * the first three characters are the status code
	 * and the following are the supplement information
	 */
	String toPreAnalyzeRequest(String requestLine, ArrayList<String> requestHeaders){
		String[] requestLineTokens = requestLine.trim().split("\\s+");

		if(requestLineTokens.length < 1 ||
				!methodChecking(requestLineTokens[0], "GET")){
			return "400-0"; //ERROR -- Invalid Method token.
		}
		if(requestLineTokens.length < 2 ||
				!pathChecking(requestLineTokens[1])){
			return "400-1"; //ERROR -- Invalid Absolute-Path token.
		}
		if(requestLineTokens.length < 3 ||
				!versionChecking(requestLineTokens[2], "HTTP")){
			return "400-2"; //ERROR -- Invalid HTTP-Version token.
		}
		if(requestLineTokens.length > 3){
			return "400-3"; //ERROR -- Spurious token before CRLF.
		}

		//display the parsed info to server standard output
		System.out.println("Method = " + requestLineTokens[0]);
		System.out.println("Request-URL = " + requestLineTokens[1]);
		System.out.println("HTTP-Version = " + requestLineTokens[2]);
		System.out.print("\r\n");

		//further checking if file format is supported and if file exists
		String interpretURL = "." + requestLineTokens[1];
		int mid = interpretURL.lastIndexOf(".");
		String ext = new String(interpretURL.substring(mid+1,interpretURL.length()));
		if(!ext.equalsIgnoreCase("txt") 
				&& !ext.equalsIgnoreCase("htm")
				&& !ext.equalsIgnoreCase("html") ){
			return "501-" + interpretURL;
		}

		File file = new File(interpretURL);
		if(file.canRead()){
			return "200-" + interpretURL;
		}
		else{
			return "404-" + interpretURL;
		}
	}

	/**
	 * checking if the token agrees with the supported method
	 * @param token 
	 * @param method
	 * @return
	 */
	public static boolean methodChecking(String token, String method){
		if(token.equals(method)) //Checking with designated method
			return true;
		return false;
	}
	/**
	 * Checking if the token represent a valid path
	 * @param token
	 * @return true for valid
	 */
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

	/**
	 * Checking if HTTP version is supported
	 * @param token Version header followed by version number
	 * @param protocol may be HTTP, FTP, ...
	 */
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
}