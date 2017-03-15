import javax.swing.*;

public class ClientTest {

    public static void main(String[] args) {
	if (args.length > 0) {
		
		Client alpha;
		alpha = new Client(args[0]);
		alpha.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		alpha.startRunning();
	} else {
	        Client albert;
	        albert = new Client("127.0.0.1");
	        albert.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	albert.startRunning();
	}
    }
}
