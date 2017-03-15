import javax.swing.*;


public class ServerTest {

    public static void main(String[] args) {
        Server serv = new Server();
        serv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        serv.startRunning();
    }
}
