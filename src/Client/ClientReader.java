package Client;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;

class ClientReader extends Thread{
    private DataInputStream dis;
    private JTextArea output;
    public ClientReader(DataInputStream dis, JTextArea output){
        this.dis=dis;
        this.output=output;
    }
    public void run(){
        String msg;
        try {
            while (true){
                msg = dis.readUTF();
                output.append(msg+"\n");
            }
        }catch (IOException e){
            System.out.println(e);
        }

    }
}