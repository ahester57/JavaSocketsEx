
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends JFrame {

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;

    public Server() {
        super("Stin's IM");
        userText = new JTextField();
        userText.setEditable(true);
        userText.addActionListener(e -> {
            sendMessage(e.getActionCommand());
            userText.setText("");
        });
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(300, 150);
        setVisible(true);

    }

    // set up and run server
    public void startRunning() {
        try {
            server = new ServerSocket(39909);
            while (true) {
                try {
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                } catch (EOFException eof) {
                    showMessage("\nServer ended the connection. Dang.");
                } finally {
                    closeStuff();
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    // wait for connect, display info
    private void waitForConnection() throws IOException {
        showMessage(" Waiting for connection....\n");
        connection = server.accept();
        showMessage(" Now connected to " + connection.getInetAddress().getHostName());
    }

    // setup streams to send / receive
    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams are setup. \n");

    }

    // during the chat
    private void whileChatting() throws IOException {
        String message  = " You are now connected. ";
        sendMessage(message);
        ableToType(true);

        do {
            try {
                message = (String) input.readObject();
                showMessage("\n" + message);
            } catch (ClassNotFoundException cnfe) {
                showMessage("\n invalid message");
            }
        } while (!message.equals("CLIENT - END"));
    }

    // close connection
    private void closeStuff() {
        showMessage("\n Closing Connections.... \n");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    // send message to client
    private void sendMessage(String message) {
        try {
            output.writeObject("SERVER - " + message);
            output.flush();
            showMessage("\nSERVER - " + message);

        } catch (IOException ioe) {
            chatWindow.append("\n Error: Failed to send message.");
        }
    }

    // updates chatWindow
    private void showMessage(final String message) {
        SwingUtilities.invokeLater(() -> chatWindow.append(message));
    }

    private void ableToType(boolean flag) {
        SwingUtilities.invokeLater(() -> userText.setEditable(flag));

    }
}
