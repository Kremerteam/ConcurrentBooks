
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.net.InetAddress;
public class ClientHandler extends Thread {

	private DatagramSocket UDPSocket;
	private Inventory Inv;
	DatagramPacket dataPacket;
	private String buffer;
	private ServerSocket TCPSocket;
	BufferedReader in;
	PrintStream out;
	int udpPort=8000;

	
	public ClientHandler(Inventory Inv, DatagramSocket defaultSocket, DatagramPacket dataPacket, String buf) {
		UDPSocket = defaultSocket;
		this.Inv = Inv;
		this.dataPacket = dataPacket;
		this.buffer = buf;
	}

	public void run() {
		boolean quit = false;
		boolean TCP = false;
		boolean first = true;
		
		while (!quit) {
			if (TCP) {
				try {
					Socket socket = TCPSocket.accept();
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out = new PrintStream(socket.getOutputStream());
					String message = in.readLine();
					String response = "error";
					if (message.substring(0, message.indexOf("$")).equals("setmode")) {
						String mode = message.substring(message.indexOf("$") + 1);
						if (mode.equals("T")) {
							response = "The communication mode is set to TCP";
							out.println(response);
							out.flush();
							// TODO
						} else {
							response = "The communication mode is set to UDP";
							out.println(response);
							out.flush();
							socket.close();
							TCPSocket.close();
							TCP = false;
						}
					} else if (message.substring(0, message.indexOf("$")).equals("borrow")){
						String name = message.substring(message.indexOf("$")+1, message.lastIndexOf("$"));
						String book = message.substring(message.lastIndexOf("$")+1);
						response = Inv.borrowBook(name, book);
						out.println(response);
						out.flush();
					}
					else if (message.substring(0, message.indexOf("$")).equals("return")){
						String id = message.substring(message.indexOf("$")+1);
						response = Inv.returnBook(id);
						out.println(response);
						out.flush();
					}

					else if (message.substring(0, message.indexOf("$")).equals("list")){
						String name = message.substring(message.indexOf("$")+1);
						response = Inv.listBorrowed(name);
						out.println(response);
						out.flush();
					}

					else if (message.substring(0, message.indexOf("$")).equals("exit")){
						response = Inv.listAvailable();
						out.println(response);
						out.flush();
						quit = true;
					}

					else if (message.substring(0, message.indexOf("$")).equals("inventory")){
						response = Inv.listAvailable();
						out.println(response);
						out.flush();
					}


				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {

				try {
					InetAddress ia = InetAddress.getByName("localhost");
					String message="";


					if(!first) {
						byte[] buf = new byte[2048];
						dataPacket = new DatagramPacket(buf, buf.length);
						UDPSocket.receive(dataPacket);
						message = new String(dataPacket.getData(), 0, dataPacket.getLength());
					}
					else {
						first=false;
						message=buffer;
					}
			//		String message = new String(dataPacket.getData(), 0, dataPacket.getLength());
					System.out.println(message);
					String response = "error";
					if (message.substring(0, message.indexOf("$")).equals("setmode")) {
						String mode = message.substring(message.indexOf("$") + 1);
						if (mode.equals("T")) {
							response = "The communication mode is set to TCP";
							byte[] buf = response.getBytes();
							buf = new byte[1024];
							dataPacket = new DatagramPacket(buf, buf.length);
							DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, ia, udpPort);
							UDPSocket.send(sendPacket);
							UDPSocket.close();
							TCPSocket = new ServerSocket(7000);
							TCP = true;
						} else {
							response = "The communication mode is set to UDP";
							byte[] buf = response.getBytes();
							System.out.println("Port"+dataPacket.getPort());
							DatagramPacket sendPacket = new DatagramPacket(buf,buf.length, dataPacket.getAddress(), dataPacket.getPort());
							UDPSocket.send(sendPacket);
							TCP=false;
						}
					} else if (message.substring(0, message.indexOf("$")).equals("borrow")) {
						String name = message.substring(message.indexOf("$")+1, message.lastIndexOf("$"));
						String book = message.substring(message.lastIndexOf("$")+1);
						System.out.println(book);
						response = Inv.borrowBook(name, book);
						byte[] buf = response.getBytes();
						System.out.println("response:"+ response);
						DatagramPacket sendPacket = new DatagramPacket(buf,buf.length, dataPacket.getAddress(), dataPacket.getPort());
						UDPSocket.send(sendPacket);
					}
					else if (message.substring(0, message.indexOf("$")).equals("return")) {
						String id = message.substring(message.indexOf("$")+1);
						response = Inv.returnBook(id);
						byte[] buf = response.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(buf,buf.length, dataPacket.getAddress(), dataPacket.getPort());
						UDPSocket.send(sendPacket);
					}
					else if (message.substring(0, message.indexOf("$")).equals("list"))
					{
						String name = message.substring(message.indexOf("$")+1,message.length());
						response = Inv.listBorrowed(name);
						byte[] buf = response.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(buf,buf.length, dataPacket.getAddress(), dataPacket.getPort());
						UDPSocket.send(sendPacket);
					}
					else if (message.substring(0, message.indexOf("$")).equals("exit"))
					{
						response = Inv.listAvailable();
						byte[] buf = response.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(buf,buf.length, dataPacket.getAddress(), dataPacket.getPort());
						UDPSocket.send(sendPacket);
						quit=true;
					}
					else if (message.substring(0, message.indexOf("$")).equals("inventory"))
					{
						response = Inv.listAvailable();
						byte[] buf = response.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(buf,buf.length, dataPacket.getAddress(), dataPacket.getPort());
						UDPSocket.send(sendPacket);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
