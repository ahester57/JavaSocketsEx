import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class Client extends JFrame {

    private JTextField userText;
    private JTextArea chatWindow;
    private PrintWriter output;
    private BufferedReader input;
    private String message = "";
    private String serverIP;
    private Socket connection;

    public Client(String host) {
        super("Chat Client");
        serverIP = host;

        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(e -> {
            sendMessage(e.getActionCommand());
            userText.setText("");
        });
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(300, 150);
        setVisible(true);
    }

    // connect to server
    public void startRunning() {
        try {
            connectToServer();
            setupStreams();
            whileChatting();
        } catch (EOFException eof) {
            showMessage("\nClient terminated connection.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            closeStuff();
        }
    }

    // connect to server
    private void connectToServer() throws IOException {
        showMessage("Attempting connection...\n");
        connection = new Socket(InetAddress.getByName(serverIP), 39909);
        showMessage("Connected to: " + connection.getInetAddress().getHostName());
    }

    // set up streams for send / receive
    private void setupStreams() throws IOException {
        output = new PrintWriter(connection.getOutputStream(), true);
        output.flush();
        input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        showMessage("\n Streams all set up. \n");
    }

    // while chatting with the server
    private void whileChatting() throws IOException {
        ableToType(true);

        while (!message.equals("SERVER - END")) {
            try {
                message = input.readLine();
                showMessage("\n" + message);
            } catch (Exception cnfe) {
                showMessage("\n Invalid message.");
            }
        }
    }

    // close stuff
    private void closeStuff() {
        showMessage("\n Closing connection... ");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    // sends messages to server
    private void sendMessage(String message) {
        if (message.equals(""))
            return;
        try {
            output.println("CLIENT - " + message);
            output.flush();
            showMessage("\nCLIENT - " + message);
        } catch (Exception ioe) {
            chatWindow.append("\n something messed up sending meesage hoss!");
        }
    }

    // change / update chatWindow
    private void showMessage(final String m) {
        SwingUtilities.invokeLater(() -> chatWindow.append(m));
    }

    // gives user permission to type stuff
    private void ableToType(final boolean tof) {
        SwingUtilities.invokeLater(() -> userText.setEditable(tof));
    }

}
