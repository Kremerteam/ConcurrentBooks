import java.io.IOException;
import java.net.*;

public class ClientHandler extends Thread {
	
	private DatagramSocket UDPSocket;
	private Inventory Inv;
	private DatagramPacket dataPacket;
	private String message;
	
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
				
			}
			else
			{
				try {
					UDPSocket.receive(dataPacket);
					
					
					
					
					
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
