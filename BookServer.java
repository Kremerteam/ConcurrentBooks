import java.io.*;
import java.net.*;
import java.util.*;

public class BookServer {
  public static void main (String[] args) throws IOException {
    int tcpPort;
    int udpPort;
    if (args.length != 1) {
      System.out.println("ERROR: Provide 1 argument: input file containing initial inventory");
      System.exit(-1);
    }
    String fileName = args[0];
    tcpPort = 7000;
    udpPort = 8000;

    // parse the inventory file
    ArrayList<String> InventoryBook = new ArrayList<String>();
    ArrayList<Integer> NumBook = new ArrayList<Integer>();
    
    File file = new File(fileName);
    Scanner sc = new Scanner(file);
    while (sc.hasNextLine()) 
    {
    	String line = sc.nextLine();
    	String name = line.substring(0, line.lastIndexOf("\"")+1);
    	InventoryBook.add(name);
    	String num = line.substring(line.lastIndexOf("\"")+2,line.length());
    	NumBook.add(Integer.valueOf(num));
    }
    
    
    // TODO: handle request from clients
    //TCP
    ServerSocket ss = new ServerSocket(tcpPort);
    while (true) {
		Socket clientSocket = ss.accept();
		//ClientHandler handler = new ClientHandler(this, clientSocket);
		//Thread t = new Thread(handler);
		//t.start();
		System.out.println("got a connection");
	}
    
    
  }
}
