import java.util.Scanner;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.*;
public class BookClient {
  public static void main (String[] args) throws IOException {
    String hostAddress;
    int tcpPort;
    int udpPort;
    int clientId;

    if (args.length != 2) {
      System.out.println("ERROR: Provide 2 arguments: commandFile, clientId");
      System.out.println("\t(1) <command-file>: file with commands to the server");
      System.out.println("\t(2) client id: an integer between 1..9");
      System.exit(-1);
    }

    String commandFile = args[0];
    clientId = Integer.parseInt(args[1]);
    hostAddress = "localhost";
    tcpPort = 7000;// hardcoded -- must match the server's tcp port
    udpPort = 8000;// hardcoded -- must match the server's udp port
    boolean Tmode = false;
    Socket socket = null;
    PrintStream out = null;
    BufferedReader in = null;
    FileWriter output = new FileWriter("out_1.txt");
    try {
        Scanner sc = new Scanner(new FileReader(commandFile));
        byte[] buf = new byte[1024];
        DatagramSocket UDPSocket = new DatagramSocket(udpPort);
        DatagramPacket dataPacket = new DatagramPacket(buf,buf.length);
        DatagramPacket sendPacket = new DatagramPacket(buf,buf.length);
        DatagramPacket recievePacket = new DatagramPacket(buf,buf.length);
        
        while(sc.hasNextLine()) {
          String cmd = sc.nextLine();
          String[] tokens = cmd.split(" ");

          if (tokens[0].equals("setmode")) {
			  String command = tokens[0]+'$'+tokens[1];
            // TODO: set the mode of communication for sending commands to the server 
        	  if(tokens[1].equals("T")) {
				  if (Tmode == true) {
					  out.println(command);
					  String reply="";
					  try {
						  reply = in.readLine();
					  } catch (IOException e) {
						  e.printStackTrace();
					  }
					  System.out.println(reply);
				  }else{
					  try {
						  Tmode = true;
						  //output.write("The communication mode is set to TCP");
						  UDPSocket.close();
						  socket = new Socket(hostAddress, 7000);
						  out = new PrintStream(socket.getOutputStream());
						  in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						  out.println(command);
						  String reply="";
						  try {
							  reply = in.readLine();
							  System.out.println(reply);
						  } catch (IOException e) {
							  e.printStackTrace();
						  }
					  } catch (Exception e) {
						  e.printStackTrace();

					  }

				  }
        	  }else if(Tmode==true) {
        		  try {
					socket.close();
					out.close();
					in.close();
					output.write("The communication mode is set to UDP");
					buf = new byte[1024];
					UDPSocket = new DatagramSocket(udpPort);
					dataPacket = new DatagramPacket(buf,buf.length);
					sendPacket = new DatagramPacket(buf,buf.length);
					recievePacket = new DatagramPacket(buf,buf.length);
					//send message
				    buf = command.getBytes();
				    sendPacket = new DatagramPacket(buf,buf.length);
				    UDPSocket.send(dataPacket);
				    buf = new byte[buf.length];
				    recievePacket = new DatagramPacket(buf,buf.length);
				    UDPSocket.receive(recievePacket);
				    String response = new String(recievePacket.getData(),0,recievePacket.getLength());
				    System.out.println(response);
				} catch (IOException e) {
					e.printStackTrace();
				}
        	  }
        	  else{
				  buf = command.getBytes();
				  sendPacket = new DatagramPacket(buf,buf.length);
				  UDPSocket.send(dataPacket);
				  buf = new byte[buf.length];
				  recievePacket = new DatagramPacket(buf,buf.length);
				  UDPSocket.receive(recievePacket);
				  String response = new String(recievePacket.getData(),0,recievePacket.getLength());
				  System.out.println(response);
			  }

          }
          else if (tokens[0].equals("borrow")) {
            // TODO: send appropriate command to the server and display the
            // appropriate responses form the server
        	  String command = tokens[0]+'$'+tokens[1]+'$'+tokens[2];
        	  if(Tmode)
        	  {
        		  out.println(command);
        		  String reply="";
				try {
					reply = in.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
        		  System.out.println(reply); 
        	  }
        	  else {
        		  //EX HERE===================================================================================================
        		  buf = command.getBytes();
        		  sendPacket = new DatagramPacket(buf,buf.length);
        		  UDPSocket.send(dataPacket);
        		  buf = new byte[buf.length];
        		  recievePacket = new DatagramPacket(buf,buf.length);
        		  UDPSocket.receive(recievePacket);
        		  String response = new String(recievePacket.getData(),0,recievePacket.getLength());
        		  System.out.println(response);
        	  }
        	  
          } else if (tokens[0].equals("return")) {
            // TODO: send appropriate command to the server and display the
            // appropriate responses form the server
			  String command = tokens[0]+'$'+tokens[1];
        	  if(Tmode) //TCP
        	  {
				  out.println(command);
				  String reply="";
				  try {
					  reply = in.readLine();
				  } catch (IOException e) {
					  e.printStackTrace();
				  }
        		  System.out.println(reply); //OUTPUT FILE*******************************************
        	  }
        	  else { //UDP
				  buf = command.getBytes();
				  sendPacket = new DatagramPacket(buf,buf.length);
				  UDPSocket.send(dataPacket);
				  buf = new byte[buf.length];
				  recievePacket = new DatagramPacket(buf,buf.length);
				  UDPSocket.receive(recievePacket);
				  String response = new String(recievePacket.getData(),0,recievePacket.getLength());
				  System.out.println(response);
        		  
        	  }
        	  
          } else if (tokens[0].equals("inventory")) {
            // TODO: send appropriate command to the server and display the
            // appropriate responses form the server
			  String command = tokens[0];
        	  if(Tmode)
        	  {
				  out.println(command);
				  String reply="";
				  try {
					  reply = in.readLine();
				  } catch (IOException e) {
					  e.printStackTrace();
				  }
				  System.out.println(reply); //OUTPUT FILE*******************************************
        	  }
        	  else {
				  buf = command.getBytes();
				  sendPacket = new DatagramPacket(buf,buf.length);
				  UDPSocket.send(dataPacket);
				  buf = new byte[buf.length];
				  recievePacket = new DatagramPacket(buf,buf.length);
				  UDPSocket.receive(recievePacket);
				  String response = new String(recievePacket.getData(),0,recievePacket.getLength());
				  System.out.println(response);
        	  }
        	  
          } else if (tokens[0].equals("list")) {
            // TODO: send appropriate command to the server and display the
            // appropriate responses form the server
			  String command = tokens[0]+'$'+tokens[1];
        	  if(Tmode)
        	  {
        		  out.println(command);
        		  String reply="";
				try {
					reply = in.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
        		  System.out.println(reply); 
        	  }
        	  else {
				  buf = command.getBytes();
				  sendPacket = new DatagramPacket(buf,buf.length);
				  UDPSocket.send(dataPacket);
				  buf = new byte[buf.length];
				  recievePacket = new DatagramPacket(buf,buf.length);
				  UDPSocket.receive(recievePacket);
				  String response = new String(recievePacket.getData(),0,recievePacket.getLength());
				  System.out.println(response);
        	  }
        	  
          } else if (tokens[0].equals("exit")) {
            // TODO: send appropriate command to the server
			  String command = tokens[0];
        	  if(Tmode)
        	  {
        		  out.println(tokens[0]);
        		  out.close();
        		  in.close();
        		  socket.close();
        	  }
        	  else {
				  buf = command.getBytes();
				  sendPacket = new DatagramPacket(buf,buf.length);
				  UDPSocket.send(dataPacket);
				  UDPSocket.close();
        	  }
        	  
          } else {
            System.out.println("ERROR: No such command");
          }
        }
    } catch (FileNotFoundException e) {
	e.printStackTrace();
    }
    output.close();
  }
}
