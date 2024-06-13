import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import static java.lang.System.out;

public class ChatServer {
    Vector<String> users = new Vector<String>();
    // why use vector instead of arraylist?
    // 1. build up the HandleClient class
    // 2. send the message to all the clients
    Vector<HandleClient> clients = new Vector<HandleClient>(); // go back to run class to finish this
    // 3. get User name
    // 4. get the message from the client
    // 5. remove the client from the list
    // 6. send the list of users to all the clients

    public void process() throws Exception {
        // port : 5566, backlog:10
        ServerSocket server = new ServerSocket(5566, 10); // introduct backlog

        // System.out.println("Server started");
        out.println("Server started: ");

        while (true) {
            Socket client = server.accept();
            HandleClient c = new HandleClient(client);
            // pass the clients to the HandleClient class
            clients.add(c);
        }
    }

    // then build up the main method after the processing method
    public static void main(String[] args) throws Exception {

        new ChatServer().process();
        // going to send message to all the clients

    }

    public void broadcast(String user, String message) {
        // send the message to all the clients
        for (HandleClient c : clients) {
            if (!c.getUsername().equals(user)) {
                c.sendMessage(user, message);
            }
        }
    }

    class HandleClient extends Thread {
        String name = "";
        BufferedReader input;
        PrintWriter output;

        public HandleClient(Socket client) throws IOException {
            // read the input from the client
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            // write the output to the client
            output = new PrintWriter(client.getOutputStream(), true);

            name = input.readLine(); // cannot use read() because it is a byte stream, so use readLine() instead
            users.add(name);

            start();// start the thread to run the run() method
        }

        // part 2 send the message to all the clients
        public void sendMessage(String uname, String msg) {
            if (msg != null)
                output.println(uname + ": " + msg);
        }

        // part 3 get User name
        public String getUsername() {
            return name;
        }

        // part 4 get the message from the client
        public void run() {
            String line;
            try {
                while (true) {
                    line = input.readLine();
                    switch (line) {
                        case "!userlist":// 指令 列印出userlist
                            sendList(name);
                            break;
                        case "!remove":// 指令 移除使用者
                            removeClient();
                            break;
                        case "!help":// 指令 列出所有指令
                            String help = "Command:\n!userlist:Print current user ID.\n!remove:remove the user.\n";
                            output.println(help);
                            break;
                        default:
                            broadcast(name, line);
                    }
                    /*
                     * if (line.equals("exit")) {
                     * // cliet break to exit the loop
                     * // you may use break;
                     * // we use remove() instead of removeClient() to remove the client from the
                     * list
                     * clients.remove(this);
                     * users.remove(name);
                     * break;
                     * } else if (line.equals("!userlist")) {
                     * sendList(name);
                     * continue;
                     * } else if (line.equals("!remove")) {
                     * removeClient();
                     * continue;
                     * } else {
                     * broadcast(name, line);
                     * }
                     */
                }
            } catch (IOException ex) {
                out.print(ex.getMessage());
            }

        }

        // part 4 send the list of users to all the clients

        public void sendList(String user) {
            int i = 0;
            var list = new String();
            for (String userr : users) {
                ++i;
                list += i + " : " + userr + "\n";
            }
            for (HandleClient c : clients) {
                if (c.getUsername().equals(user)) {
                    output.println("System Message ! \nUserlist:\n" + list);
                }
            }
        }

        // part 5 remove the client from the list

        public void removeClient() throws IOException {
            sendList(name);
            output.println("Which user do you want to delete?\n");
            String line = input.readLine();
            int num = Integer.parseInt(line);
            num--;
            if (num >= 0 && num <= users.size()) {
                if (!clients.get(num).equals(this)) { // 檢查被選擇的客戶端是否等於當前處理的客戶端
                    clients.get(num).sendMessage("System Message", "You have been removed from the chat."); // 發送通知給被刪除的客戶端
                    clients.get(num).input.close(); // 關閉被刪除的客戶端的輸入串流
                    clients.get(num).output.close(); // 關閉被刪除的客戶端的輸出串流
                    clients.get(num).interrupt(); // 終止被刪除的客戶端的執行緒
                    clients.remove(num); // 從伺服器中刪除被選擇的客戶端
                    users.remove(num);

                } else {
                    output.println("Error input number!");
                }
            }

        }
    }
}