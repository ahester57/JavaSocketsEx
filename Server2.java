
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server2 {

    private static JTextField userText;
    private static JTextArea chatWindow;
	private static PrintWriter output;
	private static BufferedReader input;
    private static BufferedReader serverTalk;
	private static ServerSocket server;
	private static Socket connection;
	private static boolean wChat = true;
	
	private static String userName = "";
	
  public static void main(String args[]) throws Exception {
        int cTosPortNumber = 39909;
        String str;
        ServerGUI gui = new ServerGUI();



    System.out.println("Waiting for a connection on " + cTosPortNumber);






	

      while (true) {
		
          wChat = false;
          server = new ServerSocket(cTosPortNumber);
          try {
              waitForConnection();
              setupStreams();
          } catch (Exception e) {
              e.printStackTrace();
          }

          str="";


          while (wChat) {
              System.out.println("The message: " + str);

              if (str.equalsIgnoreCase("CLIENT - END")) {
                  closeStuff();
                  break;
              } else {
                  str = whileChatting();
              }
          }


      }
  }
  
    // during the chat
    private static String whileChatting(){
        String message  = " You are now connected. ";
        String serverMes = "";
        sendMessage(message);

        do {
            try {

                message = input.readLine();
                showMessage("\n" + message);
            } catch (Exception cnfe) {
                showMessage("\n invalid message");
                closeStuff();
                break;
            }
            if (message == null)
                message = "CLIENT - END";
        } while (!message.equalsIgnoreCase("CLIENT - END"));
		
		return message;
    }
  
    // send message to client
    private static boolean sendMessage(String message) {
        if (message.equals(""))
            return false;
        try {
            output.println("SERVER - " + message);
            output.flush();
            showMessage("\nSERVER - " + message);
            return true;
        } catch (Exception ioe) {
            System.out.println("\n Error: Failed to send message.");
            return false;
        }
    }
	
	    // updates chatWindow
    private static void showMessage(final String message) {
        System.out.println(message);
        SwingUtilities.invokeLater(() -> chatWindow.append(message));

    }
  
  	    // setup streams to send / receive
    private static void setupStreams() {
		try {
			output = new PrintWriter(connection.getOutputStream(), true);
			output.flush();
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            serverTalk = new BufferedReader(new InputStreamReader(System.in));
            wChat = true;
            showMessage("\n Streams are setup. \n");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	    // wait for connect, display info
    private static void waitForConnection(){
		try {
			showMessage(" Waiting for connection... @" + InetAddress.getLocalHost().getHostAddress() + "\n");

			connection = server.accept();
			showMessage(" Now connected to " + connection.getInetAddress().getHostName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	    // close connection
    private static void closeStuff() {
        showMessage("\n Closing Connections.... \n");
		wChat = false;
        try {
            output.close();
            input.close();
            connection.close();
			server.close();

        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }

    static class ServerGUI extends JFrame {

        public ServerGUI() {
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

    }

}