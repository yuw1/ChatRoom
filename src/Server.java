import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {

    public static void main(String[] args) {
        new ServerListener().start();
    }
}



class ServerListener extends Thread{

    @Override
    public void run() {

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(2056);
            while(true) {
                Socket socket = serverSocket.accept();
                ChatSocket cs = new ChatSocket(socket);
                cs.start();
                ChatManager.getChatManager().add(cs);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ChatSocket extends Thread{

    Socket socket;
    DataInputStream dis;
    DataOutputStream dos ;
    String username;
    public ChatSocket (Socket socket) throws IOException {
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos= new DataOutputStream(socket.getOutputStream());
    }

    public void out(String s,String username,ChatSocket socket) throws IOException {
        dos.writeUTF(username+" 说: "+ s);
    }

    public void outOnlineMessage(String username,ChatSocket socket) throws IOException {
            dos.writeUTF(username+" 上线了");
    }
    @Override
    public void run() {
        try {
            String s;
            username=dis.readUTF();
            ChatManager.getChatManager().onlineMessage(this,username);
            while (true) {
                s=dis.readUTF();
                System.out.println(s);
                ChatManager.getChatManager().publish(this, s,username);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ChatManager {

    Vector<ChatSocket> vector = new Vector<ChatSocket>();

    private ChatManager () { }
    private static final ChatManager cm = new ChatManager();
    public static ChatManager getChatManager() {
        return cm;
    }

    public void add(ChatSocket chatSocket) {
        vector.add(chatSocket);
    }

    public void publish(ChatSocket chatSocket,String s,String username) throws IOException {
        for(int i = 0;i < vector.size();i++) {
            ChatSocket cs = vector.get(i);
            if(!cs.equals(chatSocket)) {
                cs.out(s,username,cs);
            }
        }
    }
    public void onlineMessage(ChatSocket chatSocket,String username) throws IOException{
        for(int i = 0;i < vector.size();i++) {
            ChatSocket cs = vector.get(i);
            if(!cs.equals(chatSocket)) {
                cs.outOnlineMessage(username,cs);
            }
        }
    }
}
