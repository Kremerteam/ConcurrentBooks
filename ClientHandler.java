import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;

public class ClientHandler extends Thread {

	private DatagramSocket UDPSocket;
	private Inventory Inv;
	private DatagramPacket dataPacket;
	private String buf;
	private ServerSocket TCPSocket;
	BufferedReader in;
	PrintStream out;

	public ClientHandler(Inventory Inv, DatagramSocket defaultSocket, DatagramPacket dataPacket, String buf) {
		UDPSocket = defaultSocket;
		this.Inv = Inv;
		this.dataPacket = dataPacket;
		this.buf = buf;
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
					String message="";
					if(!first) {
						UDPSocket.receive(dataPacket);
						message = new String(dataPacket.getData(), 0, dataPacket.getLength());
					}
					else {
						first=false;
						message=buf;
					}
			//		String message = new String(dataPacket.getData(), 0, dataPacket.getLength());
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
					else if (message.substring(0, message.indexOf("$")).equals("return")) {
						String id = message.substring(message.indexOf("$")+1);
						response = Inv.returnBook(id);
						byte[] buf = response.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(buf, buf.length);
						UDPSocket.send(sendPacket);
					}
					else if (message.substring(0, message.indexOf("$")).equals("list"))
					{
						String name = message.substring(message.indexOf("$")+1);
						response = Inv.listBorrowed(name);
						byte[] buf = response.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(buf, buf.length);
						UDPSocket.send(sendPacket);
					}
					else if (message.substring(0, message.indexOf("$")).equals("exit"))
					{
						response = Inv.listAvailable();
						byte[] buf = response.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(buf, buf.length);
						UDPSocket.send(sendPacket);
						quit=true;
					}
					else if (message.substring(0, message.indexOf("$")).equals("inventory"))
					{
						response = Inv.listAvailable();
						byte[] buf = response.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(buf, buf.length);
						UDPSocket.send(sendPacket);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
