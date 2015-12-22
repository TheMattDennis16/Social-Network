package socnet_client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class NewNetworking 
{
    private static Socket client = null;
    
    public static Boolean setup()
    {
        /*
            Initiates the connection with the specified IP address,
            return value indicates whether the connection was established
        */
        String serverName = "192.168.0.3";
        int port = 6666;
        try
        {
            client = new Socket(serverName, port);
            return true;
        } 
        catch(IOException e) 
        {
            return false;
        }
    }
    public static ArrayList read()
    {
        /*
            Reads the data sent by the server, adds it into an ArrayList.
            Returns this data structure.
        */
        ArrayList data = new ArrayList();
        try 
        {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream()));
            String currLine = null;
            while(true)
            {
                //Reads until an END string is found.
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
                Sends the data along with its request type with each element
                on a new line. Adds an end of line character, END, when finished
            */
            OutputStream outToServer = client.getOutputStream();
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
}
