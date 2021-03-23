import java.util.Scanner;
import java.io.*;
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
        
        while(sc.hasNextLine()) {
          String cmd = sc.nextLine();
          String[] tokens = cmd.split(" ");

          if (tokens[0].equals("setmode")) {
            // TODO: set the mode of communication for sending commands to the server 
        	  if(tokens[1].equals("T")) {
        		  try {
        			  Tmode=true;
        			  output.write("The communication mode is set to TCP");
        			  socket = new Socket(hostAddress,7000);
        			  out = new PrintStream(socket.getOutputStream());
        			  in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        		  
        		  } catch(Exception e){
        			  e.printStackTrace();
        		  }
        	  }
        	  else if(Tmode==true) {
        		  try {
					socket.close();
					out.close();
					in.close();
					output.write("The communication mode is set to UDP");
				} catch (IOException e) {
					e.printStackTrace();
				}
        	  }
        	  else
        		  output.write("The communication mode is set to UDP");
          }
          else if (tokens[0].equals("borrow")) {
            // TODO: send appropriate command to the server and display the
            // appropriate responses form the server
        	  if(Tmode)
        	  {
        		  out.println(tokens[0]+tokens[1]+tokens[2]);
        		  String reply="";
				try {
					reply = in.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
        		  System.out.println(reply); 
        	  }
        	  else {
        		  
        	  }
        	  
          } else if (tokens[0].equals("return")) {
            // TODO: send appropriate command to the server and display the
            // appropriate responses form the server
          } else if (tokens[0].equals("inventory")) {
            // TODO: send appropriate command to the server and display the
            // appropriate responses form the server
          } else if (tokens[0].equals("list")) {
            // TODO: send appropriate command to the server and display the
            // appropriate responses form the server
          } else if (tokens[0].equals("exit")) {
            // TODO: send appropriate command to the server 
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
