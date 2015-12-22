package socnet_client.chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class ChatNetworking 
{
    private static Socket chatClient = null;
    public static String hostID = null;
    
    public static void chatSetup()
    {
        /*
            Sets up the connection with the chat server, gets the hostID
            for the current user.
        */
        String serverName = "192.168.0.3";
        int port = 7777;
        try
        {
            chatClient = new Socket(serverName, port);
            ArrayList returnData = new ArrayList();
            sendRequest("getIP",returnData);
            hostID = read().get(0).toString(); 
        } 
        catch(IOException e) 
        {
            e.printStackTrace();
        }
    }
    public static ArrayList read()
    {
        /*
            Reads in from the port into an ArrayList where the details
            can be processed as needed.
        */
        ArrayList data = new ArrayList();
        try 
        {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(chatClient.getInputStream()));
            String currLine = null;
            while(true)
            {
                /*
                    Loop terminates when an end of stream character (END) 
                    is found.
                */
                currLine = in.readLine().trim();
                if(!currLine.equals("END"))
                {
                    if(!currLine.equals(""))
                    {
                        data.add(currLine);
                        System.out.println("Adding: "+currLine);
                    }
                } 
                else
                {
                    break;
                }
            }
            return data;
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
        return data;
    }

    public static void sendRequest(String reqType, ArrayList reqData)
    {
        try 
        {
            /*
                Writes the request type and relevant information to the output
                stream, along with an END character to signify end of stream.
            */
            OutputStream outToServer = chatClient.getOutputStream();
            DataOutputStream out =
                new DataOutputStream(outToServer);
            out.writeUTF(reqType+"\n");
            for (int i = 0; i < reqData.size(); )
            {
                out.writeUTF(reqData.get(i).toString()+"\n");
                System.out.println(reqData.get(i));
                i++;
            }
            out.writeUTF("END\n");
            out.flush();
            outToServer.flush();
        } 
        catch(IOException e)
        {
            System.out.println(e);
        }
    }
    public static void close()
    {
        //Closes the connection. Resets the data members.
        try 
        {
            chatClient.close();
            chatClient = null;
            hostID = null;
        }
        catch (IOException ex)
        {
            System.out.println(ex);
        }
    }
}
