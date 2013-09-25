import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * COMP 431 HW3 duo.zhao@unc.edu
 * Here is Client portion.
 * The Client is sending request
 * @author duozhao
 */
public class Client implements Runnable
{
	static String hostname;
	static int portnumber;
	static String pathname;

	private int clientTimeOut;
	private int timesToTry;

	/**
	 * Client Thread Constructor
	 * @param timesToTry: set the times of trials to recover
	 * @param clientTimeOut: set up the time-out exception time. in milliseconds
	 */
	public Client(int timesToTry, int clientTimeOut) { 
		this.clientTimeOut = clientTimeOut;
		this.timesToTry = timesToTry;
	}

	/**
	 * main method, initialize and start the client thread
	 * @param args, no parameter taking
	 */
	public static void main(String [] args){
		Client s = new Client(3, 10000);
		new Thread(s).start();
	}

	public void run(){
		String inputLine = new String("START HERE"); //not empty to ensure entering the while loop initially
		int triesRemaining = timesToTry;
		
		/**
		 * outer-while loop, for processing the client request
		 * sending request to server and read the feedback from server
		 * and output to standard output
		 */
		BufferedReader standardIn = new BufferedReader(new InputStreamReader(System.in));			
		while(inputLine != null && triesRemaining != 0){//break when EOF is encountered or trials are exhausted		
			/**
			 * Here is the first section, read request from standard input
			 * and checking whether request line is valid
			 */		
			try {
				inputLine = standardIn.readLine(); //Read from standard input the request info
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Failed to Read from standard input: ");
				break;
			}

			StringBuffer[] parsedURL = parsingURL(inputLine);
			
			/** processing EOF break */
			if(parsedURL == null){ 
				break;
			}
			
			/** if parsed URL is invalid, prompt user to re-try */
			int status = Integer.parseInt(parsedURL[0].toString());
			if(status == 0){
				System.out.println("Invalid URL: parsing URL failed: \n");
				continue;
			}

			/** parsed URL is valid, assign them to corresponding fields */
			hostname = parsedURL[1].toString();
			portnumber = Integer.parseInt(parsedURL[2].toString());
			pathname = parsedURL[3].toString();			

			/** debugging code  */
			//			System.out.println("hostname: " + hostname);
			//			System.out.println("portnumber: " + portnumber);
			//			System.out.println("pathname: " + pathname);

			/** 
			 * code generate the well-formatted GET requestLine and following headers  
			 */
			String getRequestLine = new String("GET /" + pathname + " HTTP/1.1\r\n");
			ArrayList<String> headers = new ArrayList<String>();
			headers.add("From: " + "duo.zhao@unc.edu" + "\r\n");
			headers.add("Host: " + hostname + ":" + portnumber + "\r\n");
			headers.add("Referer: " + inputLine + "\r\n");
			headers.add("User-Agent: " + "miniGET/1.0" + "\r\n");			
			/* 
			 * Here is RFC2616 the request Headers for reference
			  Accept                   ; Section 14.1  //not necessary
            | Accept-Charset           ; Section 14.2
            | Accept-Encoding          ; Section 14.3
            | Accept-Language          ; Section 14.4
            | Authorization            ; Section 14.8
            | Expect                   ; Section 14.20
            | From                     ; Section 14.22
            | Host                     ; Section 14.23
            | If-Match                 ; Section 14.24
            | If-Modified-Since        ; Section 14.25
            | If-None-Match            ; Section 14.26
            | If-Range                 ; Section 14.27
            | If-Unmodified-Since      ; Section 14.28
            | Max-Forwards             ; Section 14.31
            | Proxy-Authorization      ; Section 14.34
            | Range                    ; Section 14.35
            | Referer                  ; Section 14.36
            | TE                       ; Section 14.39
            | User-Agent               ; Section 14.43
			 */			

			/**
			 * Here is the second section, 
			 * sending the GET request Line and headers to server
			 * read from server feedbacks and display them to standard output
			 */
			triesRemaining = timesToTry;
			while(triesRemaining > 0){
				try{
					if(triesRemaining != timesToTry){
						System.out.println("Try to re-connect... Tries remaining after this trial: " + (triesRemaining-1) + "/" + timesToTry);
						Thread.sleep(1000); //Sleep the thread before re-try
					}
					//Creating Sockets
					Socket client = new Socket(hostname, portnumber);
					client.setSoTimeout(clientTimeOut);

					//Create the input stream and reader from the socket
					InputStream inStreamFromServer = client.getInputStream();
					BufferedReader inFromServer = new BufferedReader(new InputStreamReader(inStreamFromServer));

					//Create the output stream and writer from the socket
					OutputStream outToServer = client.getOutputStream();
					PrintWriter outWriter = new PrintWriter(new OutputStreamWriter(outToServer));

					//write to the server via socket the request line and headers
					outWriter.print(getRequestLine);
					for(Iterator<String> itrHeaders = headers.iterator(); itrHeaders.hasNext();){
						outWriter.print(itrHeaders.next());
					}
					outWriter.print("\r\n"); //write a blank line to mark the end
					outWriter.flush();

					//Read from server and output them
					for(String outputLine = null; (outputLine = inFromServer.readLine())!=null; ){
						System.out.println(outputLine);	
					}

					//task completed, close sockets and streams
					inFromServer.close();
					outWriter.close();
					client.close();
					break;
				}		
				catch(IOException ioe){
					triesRemaining --;
					if(triesRemaining == 0){ // triesRemaining is exhausted, program quit
						ioe.printStackTrace();
						System.out.println("Failed to recover... Program forced to abort.. ");
						break;
					}
					else{
						System.out.println("Failed to connect...Try to Recover: ");
						continue;
					}
				}
				catch(InterruptedException ie){
					triesRemaining --;
					ie.printStackTrace();
					System.out.println("Sleeping was interrupted...Try to re-Sleep: ");
					continue;
				}
				
			}
		}
	}

	/** 
	 *  Inherit from HW1 and HW2 
	 * parsedURL[0] for parse status: "1" success, "0" failure.*/
	public static StringBuffer[] parsingURL(String url){
		StringBuffer[] parsedURL = new StringBuffer[4];
		parsedURL[0] = new StringBuffer();  //parseStatus
		parsedURL[1] = new StringBuffer(); //hostname
		parsedURL[2] = new StringBuffer(); //portnumber
		parsedURL[3] = new StringBuffer(); //pathname

		if(url == null){
			return null;
		}
		int i = 0;
		while(i < url.length() && url.charAt(i)!=':' && url.charAt(i)!='/'){
			if(Character.isLetterOrDigit(url.charAt(i)) ||
					url.charAt(i) == '.' || 
					url.charAt(i) == '-' || 
					url.charAt(i) == '@'){
				parsedURL[1].append(url.charAt(i));
				i++;
				continue;
			}
			parsedURL[0] = new StringBuffer("0");
			return parsedURL;
		}

		if(i >= url.length()){
			parsedURL[0] = new StringBuffer("0");
			return parsedURL;
		}

		if(url.charAt(i) ==':'){
			i++;
			while (i < url.length() && url.charAt(i)!='/'){
				if(Character.isDigit(url.charAt(i))){
					parsedURL[2].append(url.charAt(i));
					i++;
					continue;
				}
				parsedURL[0] = new StringBuffer("0");
				return parsedURL;
			}
			
			if (parsedURL[2].length() == 0 || 
					Integer.parseInt(parsedURL[2].toString()) < 1 ||
					Integer.parseInt(parsedURL[2].toString()) > 65535){
				parsedURL[0] = new StringBuffer("0");
				return parsedURL;
			}
		}
		else{
			parsedURL[2].append("80");
		}

		if(i >= url.length() || url.charAt(i) != '/'){ // '/' is required 
			parsedURL[0] = new StringBuffer("0");
			return parsedURL;
		}
		i++;
		while(i < url.length() && url.charAt(i)!='\n'){
			if(Character.isLetterOrDigit(url.charAt(i)) ||
					url.charAt(i) == '.' || 
					url.charAt(i) == '-' || 
					url.charAt(i) == '_' ||
					url.charAt(i) == '/'){
				parsedURL[3].append(url.charAt(i));
				i++;
				continue;
			}
			parsedURL[0] = new StringBuffer("0");
			return parsedURL;
		}
		parsedURL[0] = new StringBuffer("1");
		return parsedURL;
	}
}
