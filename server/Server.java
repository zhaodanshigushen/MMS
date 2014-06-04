import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
	
	private ExecutorService cachedThreadPool;  //线程池
	private static ServerSocket server = null;
	
	public Server(int port){
		cachedThreadPool = Executors.newCachedThreadPool();
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String arg[]){
		try {
			InetAddress  addr = InetAddress.getLocalHost();
			System.out.print(addr.getHostAddress().toString());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MyAuthenticator sendingMailThread = new MyAuthenticator();
		new Thread(sendingMailThread).start();
		Server ser = new Server(12345);
		ser.start();
	}
	
	public void start(){	
		try {
			while(true){
				Socket incoming = server.accept();
				System.out.println(incoming.getInetAddress());
				CSConnection conn = new CSConnection(incoming);
				cachedThreadPool.execute(conn);	
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}  
	
}
