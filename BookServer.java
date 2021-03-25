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
//    Inventory Inv = new Inventory();
//    Inv.parseInventory(fileName);
    //TESTING
/*    System.out.println(Inv.listAvailable());
    System.out.println("\"The Letter\"");
    System.out.println(Inv.borrowBook("Mike", "\"The Letter\""));
    System.out.println(Inv.borrowBook("Mike", "\"Divergent\""));
    System.out.println(Inv.listBorrowed("Mike"));
    System.out.println(Inv.returnBook("1"));
    System.out.println(Inv.listBorrowed("Mike"));*/
    
   
    // TODO: handle request from clients
    //TCP
    
   // ServerSocket ss = new ServerSocket(tcpPort);
  //  byte[] buf = new byte[2048];
    Inventory Inv = new Inventory();
    Inv.parseInventory(fileName);
    DatagramSocket defaultSocket = new DatagramSocket(udpPort);
    ServerSocket TCPSocket = new ServerSocket(7000);
    while (true) {
	/*	ServerSocket listener = new ServerSocket(tcpPort);
		Socket s;
		Socket test = new Socket();
		test.connect(null, udpPort);
		while ( (s = listener.accept()) != null) {
		//	Thread t = new ServerThread(ns.table, s);
		//	t.start();
		}
		System.out.println("got a tcp connection");
	*/	
    	byte[] buf = new byte[2048];
		DatagramPacket dataPacket = new DatagramPacket(buf,buf.length);
		defaultSocket.receive(dataPacket);
		String buffer = new String(buf).trim();
		Thread t = new ClientHandler(Inv,defaultSocket,dataPacket,buffer,TCPSocket);
		t.start();
	}
    
    
  }
}
