package assegnamento3;

public class MainServer {

	public static void main(String[] args) {
		//NetworkServer n = new NetworkServer(Integer.parseInt(args[0]));
		NetworkServer n = new NetworkServer(4926);
		n.run();
	}

}
