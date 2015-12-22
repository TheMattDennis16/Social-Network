package socnet_client.chat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 *
 * @author Matt
 */
public class UDPRead extends Thread
{
    public void run()
    {
        /*
            Read thread, runs along side the message client. Receives incoming 
            messages and either creates a new room or adds the message to the 
            requires jTextArea.
        */
        try 
        {
            DatagramSocket socket = new DatagramSocket(8888);
            while(true)
            {
                /*
                    Creates a new buffer called receiveData of size 1024 bytes.
                    Any data outside this range is discarded. Fills the packet
                    With data received from the transmitting client. Stores 
                    the user host address and splits the message out into
                    code and the message. Code is compared to the current list 
                    of Rooms. If code not found a new room is created, else the
                    message is added the room for that code.
                */
                byte[] receiveData = new byte[1024];
                DatagramPacket receive = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receive);
                String sentence = new String(receiveData, 0, receive.getLength());
                String user = receive.getAddress().getHostName();
                System.out.println("Got "+sentence.length()+" from "+user);
                if(sentence.length() >= 6)
                {
                    String code = sentence.substring(0, 6);
                    String message = sentence.substring(6, sentence.length());
                    Boolean codeFound = false;

                    //Searches through rooms to compare codes.
                    for(int i = 0; (i < Message.rooms.size() 
                        && Message.rooms.size() > 0); i++)
                    {
                        ArrayList<String> room = Message.rooms.get(i);
                        if(room.get(0).equals(code)) 
                        {
                            codeFound = true; 
                        }
                    }
                    if(codeFound)
                    {
                        Message.addMsg(code, message);
                    }
                    else 
                    {
                        Message.newRoom(code, user);
                        Message.addMsg(code, message);
                    }                    
                }
            }
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
