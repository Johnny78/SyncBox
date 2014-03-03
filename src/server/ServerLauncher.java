package server;
public class ServerLauncher {

	public static void main(String[] args) throws Exception {
		ServerThread t = new ServerThread(20661);
		t.run();
	}
}
