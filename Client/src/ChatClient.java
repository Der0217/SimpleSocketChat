import java.awt.*;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStreamReader;
import java.net.Socket;
import static java.lang.System.out;
import javax.swing.*;

public class ChatClient extends JFrame implements ActionListener {
    // setup of the Jframe
    String username;
    PrintWriter pw;

    // message box setup
    BufferedReader br;
    JTextArea taMessages; // show 所有使用者輸入訊息的地方
    JTextField tfInput; // 輸入訊息的地方
    JButton btnSend; // send button
    JButton btnExit; // exit button
    Socket client;

    // chat client conversion
    public ChatClient(String username, String serverName) throws Exception {
        super(username);
        this.username = username;

        client = new Socket(serverName, 5566);
        br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        pw = new PrintWriter(client.getOutputStream(), true);
        pw.println(username);

        // build the interface
        buildInterface();

        // message box setup
        new MessageThread().start();

    }

    // build interface to client to type in
    public void buildInterface() {
        // 劃出 使用者開始傳訊息的畫面
        // use two buttons
        btnSend = new JButton("Send");
        btnExit = new JButton("Exit");
        // use text area
        taMessages = new JTextArea(10, 30);
        taMessages.setRows(10);
        taMessages.setColumns(30);
        taMessages.setEditable(false);
        // use text field
        tfInput = new JTextField(30);
        JScrollPane sp = new JScrollPane(taMessages, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(sp, "Center");
        JPanel bp = new JPanel(new FlowLayout());
        bp.add(tfInput);
        bp.add(btnSend);
        bp.add(btnExit);
        add(bp, "South"); // what is South
        btnSend.addActionListener(this);
        btnExit.addActionListener(this);
        setSize(550, 300);
        setVisible(true);

        taMessages.setFont(new Font("MS Gothic", Font.PLAIN, 16));
        pack();

    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        // TODO Auto-generated method stub
        if (evt.getSource() == btnExit) {
            // send message to server
            pw.println("exit");
            System.exit(0);
        } else {
            // client press the send button
            String str = tfInput.getText();
            if (!(str.equals(""))) {
                taMessages.append("Me " + ":" + tfInput.getText() + '\n');
                pw.println(tfInput.getText());
                tfInput.setText("");
            }
        }
    }

    // client connect to the server
    public static void main(String[] args) throws Exception {
        String name = JOptionPane.showInputDialog(null, "Enter your name: ", "Chat App", JOptionPane.PLAIN_MESSAGE);
        String serverName = "localhost";
        try {
            new ChatClient(name, serverName);
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }

    // send message
    class MessageThread extends Thread {
        public void run() {
            String line;
            try {
                while (true) {
                    // client send message
                    line = br.readLine();
                    taMessages.append(line + "\n");

                    //伺服端傳關閉該客戶端
                    if (line.equals("System Message: You have been removed from the chat.")) {
                        br.close();
                        pw.close();
                        tfInput.setEditable(false);
                    }

                }
            } catch (Exception e) {
                out.println("Error: " + e.getMessage());
            }
        }
    }

}