package server;
public class Tester {

	public static void main(String[] args) throws Exception {
		ServerThread t = new ServerThread(20661);
		t.run();
	}
}
