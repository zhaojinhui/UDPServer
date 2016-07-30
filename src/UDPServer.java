import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;



public class UDPServer {
	public static void main(String[] args) {
		
		try {
			//initialize some variable
			InetAddress[] ipaddress=new InetAddress[100];
			int[] port=new int[100];
			int number=0;
			
			//get the listening port
			System.out.println("please input port number:");
			String message;
			BufferedReader inputBufferedReader=new BufferedReader(new InputStreamReader(System.in));
			message=inputBufferedReader.readLine();
			
			//open a socket
			int serverport=Integer.parseInt(message);
			DatagramSocket socket= new DatagramSocket(serverport);
			
			try{
				
			//receive message
			while(true)
			{
				//read the message
				byte[] recbyte=new byte [1024];
				DatagramPacket recPacket=new DatagramPacket(recbyte, recbyte.length);
				socket.receive(recPacket);
				String messgeString=new String(recbyte,0,recPacket.getLength());
				
				//come from a new host
				if(messgeString.equals("greeting"))
				{
					System.out.println("new connection");
					
					//record the host port and address
					ipaddress[number]=recPacket.getAddress();
					port[number]=recPacket.getPort();
					String sendmessage="connected";
					byte[] sendbyte=sendmessage.getBytes();
					DatagramPacket sendPacket=new DatagramPacket(sendbyte, sendbyte.length,ipaddress[number],port[number]);
					socket.send(sendPacket);
					number++;
				}
				//come from the existing host
				else {
					String sendmessage="<From "+recPacket.getAddress()+":"+recPacket.getPort()+">:"+messgeString;
					System.out.println(sendmessage);
					byte[] sendbyte=sendmessage.getBytes();
					
					//send the message to all the host
					for(int i=0;i<number;i++)
					{
						DatagramPacket sendPacket=new DatagramPacket(sendbyte, sendbyte.length,ipaddress[i],port[i]);
						socket.send(sendPacket);
					}
				}
			}
			}
			finally{
				socket.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
