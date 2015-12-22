package socnet_chat_server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SocNet_Chat_Server 
{
    private static Boolean end = false;
    private static ArrayList users = new ArrayList();
    private static ArrayList<ArrayList<String>> rooms = new ArrayList();
    private static ServerSocket listener;
    private static UI gui = null;
    private static Read read = null;
    public static void main(String[] args) throws IOException 
    {
        listener = new ServerSocket(7777);
        gui = new UI();
        gui.setVisible(true);
        gui.jTextArea1.setText("Waiting for client:\n");
        read = new Read();
    }
    public static class Read extends Thread
    {
        public Read() throws IOException
        {
            while(!end)
            {
                new ServeList(listener.accept()).start();
            }
        }
    }
    public static void forceClose() throws IOException
    {
        listener.close();
        read.stop();
    }
    public static class ServeList extends Thread 
    {
        private Socket socket;
        private Boolean interrupt = true;
        private String ip;
        public ServeList(Socket newSocket)
        {
            //Obtains user ID, opens connection with the client
            socket = newSocket;
            ip = socket.getInetAddress().getHostName();
            gui.jTextArea1.setText(gui.jTextArea1.getText()
                    +"New connection with: "+ip+"\n");
        }
        public void run()
        {
            /*
                Adds user host-name (NetID) into the active users data structure
                Scans for requests, removes the user when the loop is interrupted
            */
            users.add(ip);
            while(interrupt)
            {
                scanForRequest();
            }
            users.remove(socket.getInetAddress());
        }
        public void scanForRequest()
        {
            try 
            {
                /*
                    Reads user-sent data into an ArrayList, this is then passed 
                    into the read request (readReq) function, allowing for the
                    data to be processed.
                */
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                ArrayList data = new ArrayList();
                String currLine;
                while (true) 
                {
                    currLine = in.readLine().trim();
                    if(!currLine.equals("END"))
                    {
                        if(!currLine.equals(""))
                        {
                            data.add(currLine);
                            gui.jTextArea1.setText(gui.jTextArea1.getText()
                                    +"Adding: "+currLine+"\n");
                        } 
                    }
                    else { break; }
                }
                readReq(data);
            } catch (Exception e) {
                e.printStackTrace();
                interrupt = false;
            }  
        }
        public void readReq(ArrayList request)
        {
            /*
                Extracts request code in ArrayList index 0, processes the 
                rest of the data structure according to this code.
            */
            ArrayList data = new ArrayList();
            String reqType = request.get(0).toString();
            if(reqType.equals("getList"))
            {
                //Returns list of users.
                data = users;
                sendReply(data);
            }
            else if(reqType.equals("getIP"))
            {
                //Returns the users net ID, this most commonly an IP address
                ArrayList ret = new ArrayList();
                ret.add(ip);
                sendReply(ret);
            }
            else if (reqType.equals("getRooms"))
            {
                /*
                    Searches through rooms structure, checks to see if the 
                    users is in that room. If so adds that room to a new structure
                    for returning.
                */
                ArrayList items = new ArrayList();
                for(int i = 0; i < rooms.size(); i++)
                {
                    if(rooms.get(i).contains(ip))
                    {
                        for(int y = 0; y < rooms.get(i).size(); y++)
                        {
                            items.add(rooms.get(i).get(y));
                        }
                    }
                }
                sendReply(items);
            }
            else if (reqType.equals("addToRoom"))
            {
                /*
                    Inserts a specified user into a specified room. 
                */
                String code = request.get(1).toString();
                String ip = request.get(2).toString();
                for(int i = 0; i < rooms.size(); i++)
                {
                    ArrayList currRoom = rooms.get(i);
                    if(currRoom.get(0).equals(code))
                    {
                        rooms.get(i).add(ip);
                    }
                }
            }
            else if (reqType.equals("removeFromRoom"))
            {
                /*
                    Removes a specified user from a specific room
                    If there is more than one user in the room deletes the 
                    specified IP, else removes the now empty room
                */
                String code = request.get(1).toString();
                String IP = request.get(2).toString();
                for(int i = 0; i < rooms.size(); i++)
                {
                    if(rooms.get(i).get(0).equals(code)) //Code check
                    {
                        if(rooms.get(i).size() > 2)
                        {
                            rooms.get(i).remove(IP);
                        }
                        else
                        {
                            rooms.remove(rooms.get(i));
                        }
                    }
                }   
            }
            else if (reqType.equals("addRoom"))
            {
                /*
                    Checks to see if a room code is in use, otherwise creates
                    a new room based on the send information.
                */
                ArrayList<String> newRoom = new ArrayList();
                newRoom.add(request.get(1).toString());
                newRoom.add(ip);
                newRoom.add(request.get(2).toString());
                Boolean isFound = false;
                for(int i = 0; i < rooms.size(); i++)
                {
                    if(rooms.get(i).get(0).equals(newRoom.get(0)))
                    {
                        isFound = true;
                    }
                }
                if(!isFound)
                {
                    rooms.add(newRoom);
                }
            }
            else if (reqType.equals("remove"))
            {
                /*
                    Removes the specified user from the system and any rooms
                    they were in.
                */
                users.remove(ip);
                for(int a = 0; a < rooms.size(); a++)
                {
                    if(rooms.get(a).contains(ip))
                    {
                        rooms.get(a).remove(ip);
                    }
                }
                interrupt = false;
            }
        }
        public void sendReply(ArrayList data)
        {
            try 
            {
                int count = 0;
                OutputStream outToClient = socket.getOutputStream();
                DataOutputStream out =
                    new DataOutputStream(outToClient);
                
                /*
                    Writes the data to be sent out as a new line.
                */
                for (int i = 0; i < data.size(); i++)
                {
                    out.writeUTF(data.get(i).toString() + "\n");
                    gui.jTextArea1.setText(gui.jTextArea1.getText()
                            +"Sending "+data.get(i)+"\n");
                    count++;
                }
                out.writeUTF("END\n"); //Add end character stream notifier
                count++;
                gui.jTextArea1.setText(gui.jTextArea1.getText()
                        +"Sending size: "+count+"\n");
                out.flush();
                outToClient.flush();
            } 
            catch (IOException e) 
            { 
                System.out.println(e);
            }
        }
    }
}
