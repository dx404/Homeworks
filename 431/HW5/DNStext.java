import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author duozhao
 *
 */
public class DNStext {
	String hostName;
	InetAddress resolvedAddress;
	String firstReadLine;
	long t0, t1, t2, t3;

	public boolean entryValidBit;

	public DNStext(){
		hostName = null;
		resolvedAddress = null;
		firstReadLine = null;
		t0 = System.currentTimeMillis();
		t1 = 0;
		t2 = 0;
		t3 = 0;
		entryValidBit = true;
	}


	public static void main(String[] args) {
		DNStext dns = new DNStext();
		BufferedReader stdinReader = 
				new BufferedReader(
						new InputStreamReader(System.in));

		String inputHost = null;
		boolean isTestOK = true;

		while (isTestOK) {
			dns.entryValidBit = true;
			try {
				inputHost = stdinReader.readLine();
				if (inputHost == null){
					isTestOK = false;
					System.exit(0);
				}
				dns.DNSTimeTracking(inputHost);
			}
			catch (UnknownHostException uhe){
				dns.entryValidBit = false;
				dns.firstReadLine = "UnknownHostException";
				continue;
			}
			catch (IOException e){
				dns.entryValidBit = false;
				dns.firstReadLine = "Other IOEception";
				continue;
			}
			finally {
				if (args.length == 0){
					dns.printRecord();
				}
				else{
					System.out.println(dns.getCSVentry(", "));
				}
			}
		}
	}

	public int DNSTimeTracking(String hostName) throws IOException {
		this.hostName = hostName; 
		t1 = System.currentTimeMillis();
		resolvedAddress = InetAddress.getByName(hostName);
		t2 = System.currentTimeMillis();
		Socket sk = new Socket(resolvedAddress, 80);
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(sk.getInputStream()));
		PrintWriter writer = new PrintWriter(
				new OutputStreamWriter(sk.getOutputStream()));
		writer.print("GET /index.html HTTP/1.0 \r\n\r\n");
		writer.flush();
		firstReadLine = reader.readLine();
		t3 = System.currentTimeMillis();
		while (reader.readLine() != null); 

		writer.close();
		reader.close();
		sk.close();

		return 0;
	}

	public String getCSVentry(String delimiter){
		Calendar enterStamp = new GregorianCalendar();
		enterStamp.setTimeInMillis(t0);
		Date readableEnterTime = enterStamp.getTime();

		String resolvedIPString = (resolvedAddress == null)? 
				null : resolvedAddress.getHostAddress();

		return (entryValidBit + delimiter + 
				readableEnterTime + delimiter + 
				hostName + delimiter + 
				resolvedIPString + delimiter + 
				firstReadLine + delimiter + 
				t1 + delimiter + 
				t2 + delimiter + 
				t3 + delimiter + 
				(t2 - t1) + delimiter + 
				(t3 - t2)
				);
	}

	public void printRecord(){ //for single entry debug testing
		Calendar enterStamp = new GregorianCalendar();
		enterStamp.setTimeInMillis(t0);
		Date readableEnterTime = enterStamp.getTime();

		String resolvedIPString = 
				(resolvedAddress == null)? 
						null : resolvedAddress.getHostAddress();

		System.out.println("entryValidBit = " + entryValidBit);
		System.out.println("t0 = " + readableEnterTime);
		System.out.println("hostName = " + hostName);
		System.out.println("IP Addr = " + resolvedIPString);
		System.out.println("First Line = " + firstReadLine);
		System.out.println("t1 = " + t1);
		System.out.println("t2 = " + t2);
		System.out.println("t3 = " + t3);
		System.out.println("(t2 - t1) = " + (t2 - t1));
		System.out.println("(t3 - t2) = " + (t3 - t2));
	}

}

