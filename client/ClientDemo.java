import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class ClientDemo {

	public static void main(String arg[]){
		try {
			Socket socket = new Socket("127.0.0.1",12345);
			//PrintWriter out = new PrintWriter(socket.getOutputStream());
			OutputStream out = socket.getOutputStream();
			InputStream in = socket.getInputStream();
			out.write(("register\n\rname2 password mail telephone\n\r").getBytes("UTF-8"));
			
			//socket.getOutputStream().write(("login\n\rname2 password \n\r").getBytes("UTF-8"));
			out.flush();
			
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
	        byte[] data = new byte[1024];  
	        int count = -1;  
	        /*while((count = in.read(data,0,1024)) != -1)  
	            outStream.write(data, 0, count); 
	        System.out.println(new String(outStream.toByteArray(),"UTF-8"));*/
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}

