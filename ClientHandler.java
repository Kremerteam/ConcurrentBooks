import java.io.IOException;
import java.net.*;

public class ClientHandler extends Thread {
	
	private DatagramSocket UDPSocket;
	private Inventory Inv;
	private DatagramPacket dataPacket;
	private String message;
	private ServerSocket TCPSocket;

	public ClientHandler(Inventory Inv, DatagramSocket defaultSocket, DatagramPacket dataPacket, String buf) {
	ServerSocket TCPSocket;
	PrintStream out = null;
	BufferedReader in = null;

	public ClientHandler(Inventory Inv,DatagramSocket defaultSocket,DatagramPacket dataPacket,String buf) {
		UDPSocket = defaultSocket;
		this.Inv = Inv;
		this.dataPacket = dataPacket;
		this.message=buf;
	}
	
	public void run()
	{
		boolean quit=false;
		boolean TCP=false;
		
		while(!quit)
		{
			if(TCP)
			{
				try {
					Socket socket = TCPSocket.accept();
					//TCPSocket.accept();
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out = new PrintStream(socket.getOutputStream());
					String message = in.readLine();
					String response = "error";
					if(message.substring(0, message.indexOf("$")).equals("setmode"))
					{
						String mode = message.substring(message.indexOf("$")+1);
						if(mode.equals("T"))
						{
							response = "The communication mode is set to TCP";
							//TODO
						}
						else {
							
						}
					}
					else if(message.substring(0, message.indexOf("$")).equals("borrow"))
						System.out.println(message);
					else if(message.substring(0, message.indexOf("$")).equals("return"))
						System.out.println(message);
					else if(message.substring(0, message.indexOf("$")).equals("list"))
						System.out.println(message);
					else if(message.substring(0, message.indexOf("$")).equals("exit"))
						System.out.println(message);
					else if(message.substring(0, message.indexOf("$")).equals("inventory"))
						System.out.println(message);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					UDPSocket.receive(dataPacket);
					String message = new String(dataPacket.getData(), 0, dataPacket.getLength());
					System.out.println(message);
					String response = "error";

					if (message.substring(0, message.indexOf("$")).equals("setmode")) {
						String mode = message.substring(message.indexOf("$") + 1);
						if (mode.equals("T")) {
							response = "The communication mode is set to TCP";
							byte[] buf = response.getBytes();
							DatagramPacket sendPacket = new DatagramPacket(buf, buf.length);
							UDPSocket.send(sendPacket);
							UDPSocket.close();
							TCPSocket = new ServerSocket(7000);
							TCP = true;
						} else {
							response = "The communication mode is set to UDP";
							byte[] buf = response.getBytes();
							DatagramPacket sendPacket = new DatagramPacket(buf, buf.length);
							UDPSocket.send(sendPacket);
							TCP=false;
						}
					} else if (message.substring(0, message.indexOf("$")).equals("borrow")) {
						String name = message.substring(message.indexOf("$")+1, message.lastIndexOf("$"));
						String book = message.substring(message.lastIndexOf("$")+1);
						response = Inv.borrowBook(name, book);
						byte[] buf = response.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(buf, buf.length);
						UDPSocket.send(sendPacket);
					}
					else if(message.substring(0, message.indexOf("$")).equals("borrow"))
						System.out.println(message);
					else if(message.substring(0, message.indexOf("$")).equals("return"))
						System.out.println(message);
					else if(message.substring(0, message.indexOf("$")).equals("list"))
						System.out.println(message);
					else if(message.substring(0, message.indexOf("$")).equals("exit"))
						System.out.println(message);
					else if(message.substring(0, message.indexOf("$")).equals("inventory"))
						System.out.println(message);
					
					
					
					
					
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
