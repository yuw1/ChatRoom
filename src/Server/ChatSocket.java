package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatSocket extends Thread{

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
