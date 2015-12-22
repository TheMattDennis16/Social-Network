package socnet_server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SocNet_Server 
{
    public static ArrayList users = new ArrayList();
    private static final UI gui = new UI();
    public static Boolean end = false;
    private static ServerSocket listener = null;
    private static Read read;
    private static int clientNumber = 0;
    
    public static void main(String[] args) throws IOException 
    {
        /*
            Sets up the server, begins the read thread. Starts the UI.
        */
        listener = new ServerSocket(6666);
        gui.setVisible(true);
        gui.jTextArea1.setText("Server is running\n");
        read = new Read();
    }
    public static void forceClose() throws IOException
    {
        listener.close();
        read.stop();
    }
    public static class Read extends Thread
    {
        public Read() throws IOException
        {
            /*
                While not interrupted loop and accept new clients.
            */
            while(!end) 
            {
                new SockServer(listener.accept(), clientNumber++).start(); 				
            }
        }
    }
    public static class SockServer extends Thread 
    {
        private Socket socket;
        private int clientNumber;
        private Boolean interrupt = true;
        
        public SockServer(Socket socket, int clientNumber) 
        {
            /*
                Creates the socket.
            */
            this.socket = socket;
            this.clientNumber = clientNumber;
            gui.jTextArea1.setText(gui.jTextArea1.getText()
                    +"New connection with client #"+clientNumber
                    +" at "+socket+"\n");
        }
        public void run() 
        {
            /*
                Adds the users IP address to the user list, scans for requests
                finally removes the user from the structure.
            */
            if(!users.contains(socket.getInetAddress().toString()))
            {
               users.add(socket.getRemoteSocketAddress()); 
            }
            while(interrupt)
            {
                scanForRequest();
            }
            users.remove(socket.getInetAddress());
            users.remove(socket.getRemoteSocketAddress());
        }
        public void scanForRequest()
        {
            /*
                Reads information in untill and END line is found. Insert each
                line into an ArrayList for processing later.
            */
            try 
            {
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
                            gui.jTextArea1.setText(gui.jTextArea1.getText()+"Adding: "+currLine+"\n");
                        } 
                    }
                    else 
                    {
                        break;
                    }
                }
                readReq(data);
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
                interrupt = false;
            }
        }
        public void readReq(ArrayList request)
        {
            /*
                Determines the request code, processes the request accordingly
            */
            ArrayList returnData = new ArrayList();
            if(request.get(0) == null || request.get(0).toString().length() < 3)
            {
                request.remove(0);
            }
            String reqType = request.get(0).toString().trim();
            if(reqType.equals("login")) 
            {
                String username = request.get(1).toString().trim();
                String password = request.get(2).toString().trim();
                ArrayList temp = Process.validateLogin(username, password);
                if(temp.get(0) == "true") 
                {
                    returnData.add("true");
                    returnData.add(temp.get(1).toString());
                }
                else 
                {
                    returnData.add("false");
                }
                sendReply(returnData);
            }
            else if (reqType.equals("logout")) 
            {
                try 
                {
                    interrupt = false;
                    socket.close();
                }
                catch (IOException ex) 
                {
                    System.out.println(ex);
                }
            }
            else if (reqType.equals("register")) 
            {
                String username = request.get(1).toString().trim();
                String password = request.get(2).toString().trim();
                String email = request.get(3).toString().trim();
                Boolean state;
                if(Process.register(username, password, email).get(0) == "true")
                    state = true;
                else
                    state = false;
                if(state)
                    returnData.add("true");
                else
                    returnData.add("false");
                sendReply(returnData);
            }
            else if (reqType.equals("getProfile")) 
            {
                int id = Integer.parseInt(request.get(1).toString());
                ArrayList send = Process.getProfile(id);
                sendReply(send);
                
            }
            else if (reqType.equals("addPost")) 
            {
                int userID = Integer.parseInt(request.get(1).toString());
                String post = request.get(2).toString();
                Process.addPost(userID, post);
            }
            else if (reqType.equals("getYourPosts")) 
            {
                int userID = Integer.parseInt(request.get(1).toString());
                ArrayList data = Process.getYourPosts(userID);
                sendReply(data);
            }
            else if (reqType.equals("getFriendsPosts")) 
            {
                int userID = Integer.parseInt(request.get(1).toString());
                ArrayList data = Process.getFriendsPosts(userID);
                sendReply(data);
            }
            else if (reqType.equals("deleteProfile")) 
            {
                int userID = Integer.parseInt(request.get(1).toString());
                Process.deleteProfile(userID);
            }
            else if (reqType.equals("addFreind")) 
            {
                int from = Integer.parseInt(request.get(1).toString());
                int to = Integer.parseInt(request.get(2).toString());
                Process.addFriend(from, to);
            }
            else if (reqType.equals("acceptRequest")) 
            {
                int from = Integer.parseInt(request.get(1).toString());
                int to = Integer.parseInt(request.get(2).toString());
                Process.acceptRequest(from, to);
            }
            else if (reqType.equals("rejectRequest")) 
            {
                int from = Integer.parseInt(request.get(1).toString());
                int to = Integer.parseInt(request.get(2).toString());
                Process.rejectRequest(from, to);
            }
            else if (reqType.equals("getNotifications")) 
            {
                int userID = Integer.parseInt(request.get(1).toString());
                returnData = Process.getNotifications(userID);
                sendReply(returnData);
            }
            else if (reqType.equals("updateProfile")) 
            {
                int userID = Integer.parseInt(request.get(1).toString());
                String name = request.get(2).toString();
                String description = request.get(3).toString();
                String location = request.get(4).toString();
                Process.updateProfile(userID, name, description, location);
            }
            else if (reqType.equals("addInterest")) 
            {
                String user = request.get(1).toString();
                String interest = request.get(2).toString();
                Process.addInterest(user, interest);
            }
            else if (reqType.equals("removeInterest")) 
            {
                String user = request.get(1).toString();
                String interest = request.get(2).toString();
                Process.removeInterest(user, interest);
            }
            else if (reqType.equals("searchName")) 
            {
                String name = request.get(1).toString();
                returnData = Process.searchName(name);
                sendReply(returnData);
            }
            else if (reqType.equals("searchInterest"))
            {
                String interest = request.get(1).toString();
                returnData = Process.searchInterest(interest);
                sendReply(returnData);                
            }
            //Implement further request types
        }
        public void sendReply (ArrayList data)
        {
            /*
                Writes each element of the ArrayList as a new line.
            */
            try 
            {
                int count = 0;
                OutputStream outToClient = socket.getOutputStream();
                DataOutputStream out =
                    new DataOutputStream(outToClient);
                for (int i = 0; i < data.size(); i++)
                {
                    out.writeUTF(data.get(i).toString() + "\n");
                    gui.jTextArea1.setText(gui.jTextArea1.getText()+"Sending "+data.get(i)+"\n");
                    count++;
                }
                out.writeUTF("END\n");
                count++;
                gui.jTextArea1.setText(gui.jTextArea1.getText()+"Sending size: "+count+"\n");
                out.flush();
                outToClient.flush();
            } 
            catch (IOException e) 
            { 
                e.printStackTrace();
            }
        }
    }
}