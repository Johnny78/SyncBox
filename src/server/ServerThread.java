package server;
/**
 * used for testing client app and server on same machine
 */

public class ServerThread extends Thread{
	
	private int port;
	
	public ServerThread(int port){
		this.port = port;
	}
	
	public void run(){
		SyncBoxServer s = new SyncBoxServer();
		try{
			s.run(port);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}	
}
