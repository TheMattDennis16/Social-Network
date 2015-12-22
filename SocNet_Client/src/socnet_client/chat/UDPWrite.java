package socnet_client.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;


public class UDPWrite
{
    public static String genCode()
    {   
        /*
            Creates a character array containing each letter, generates a random
            number and adds the selected character to the output string. Repeats
            this process six times. This can be expanded for capital letters
            and numbers should the codes need to be more complex.
        */
        char[] letters = 
        {
            'a','b','c','d','e','f','g','h','i','j','k','l',
            'm','n','o','p','q','r','s','t','u','v','w','x','y','z'
        };
        String code = "";
        Random randomNum = new Random();
        for (int i = 0; i < 6; i++)
        {
            code += letters[randomNum.nextInt(26)];
        }
        return code;
    }
    public static void write(String message, ArrayList clients) 
            throws SocketException, IOException
    {
        /*
            Loops through each client defined in the room and send the message
            to that client, with the first six characters being the room code.
            Ignores if the host address is equal to yours, preventing you from
            sending messages to yourself.
        */
        for (int i = 1; i < clients.size(); i++)
        {
            DatagramSocket socket = new DatagramSocket();
            InetAddress addr = InetAddress.getByName(clients.get(i).toString());
            if(!addr.getHostName().equals(ChatNetworking.hostID))
            {
                try
                {
                    System.out.println("Sending to: "+addr.getHostName());
                    String messageCombined = clients.get(0) + message;
                    DatagramPacket send = new DatagramPacket
                        (messageCombined.getBytes(), messageCombined.length(), addr, 8888);
                    socket.send(send);                   
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            socket.close();
        }
    }
    
}

