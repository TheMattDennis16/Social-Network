package socnet_client;

import java.util.ArrayList;
import socnet_client.chat.ChatNetworking;

public class Process 
{
    private static String userID = null;
    
    public static Boolean validateLogin (String username, String password)
    {
        /*
            Attempts to validate the login information with the server.
        */
        ArrayList data = new ArrayList();
        data.add(username);
        data.add(password);
        try
        {
            NewNetworking.sendRequest("login", data);
            data.clear();
            data = NewNetworking.read();
            
            System.out.println("Found: "+data.get(0));
            if (data.get(0).equals("true"))
            {
                userID = data.get(1).toString();
                return true;
            }
            else
            {
                return false;
            }
        } 
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }
    
    public static Boolean register(
            String username, String password, String email)
    {
        /*
            Registers the user information if possible, logs them in afterwards
        */
        ArrayList sentData = new ArrayList();
        ArrayList retData = new ArrayList();
        sentData.add(username);
        sentData.add(password);
        sentData.add(email);
        try
        {
            NewNetworking.sendRequest("register", sentData);
            retData = NewNetworking.read();
            if (retData.get(0).equals("true"))
            {
                return true;
            }
            else
            {
                return false;
            }
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public static void logout()
    {
        /*
            Removes the user from the active users list.
        */
        ArrayList data = new ArrayList();
        NewNetworking.sendRequest("logout", data);
    }
    
    public static ArrayList getProfileData()
    {
        /*
            Returns the current profile data for the logged in user
        */
        ArrayList data = new ArrayList();
        ArrayList sentData = new ArrayList();
        sentData.add(userID);
        try 
        {
            NewNetworking.sendRequest("getProfile", sentData);
            data = NewNetworking.read();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return data;
    }
    public static void addInterest(String interest)
    {
        //Adds a new interest to the database
        ArrayList sentData = new ArrayList();
        sentData.add(userID);
        sentData.add(interest);
        try
        {
            NewNetworking.sendRequest("addInterest", sentData);
        }
        catch(Exception e) 
        {
            System.out.println(e);
        }
    }
    public static void removeInterest(String interest)
    {
        //Removes an interest from the database
        ArrayList sentData = new ArrayList();
        sentData.add(userID);
        sentData.add(interest);
        try 
        {
            NewNetworking.sendRequest("removeInterest", sentData);
        }
        catch(Exception e) 
        {
            System.out.println(e);
        }
    }
    public static ArrayList searchName(String name)
    {
        //Searches by a specified username.
        ArrayList returnData = new ArrayList();
        ArrayList sent = new ArrayList();
        sent.add(name);
        try 
        {
            NewNetworking.sendRequest("searchName", sent);
            returnData = NewNetworking.read();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return returnData;
    }
    public static void addFriend(int userId)
    {
        //Adds the specified user by their unique ID.
        ArrayList data = new ArrayList();
        data.add(userID.trim());
        data.add(userId);
        System.out.println("Adding userID (from): "+userID);
        System.out.println("Adding userId (to): "+userId);
        try
        {
            NewNetworking.sendRequest("addFreind", data);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    public static ArrayList getNotifications()
    {
        /*
            Gets the current list of friendship requests relevant to the 
            current user.
        */
        ArrayList sentData = new ArrayList();
        ArrayList returnData = new ArrayList();
        sentData.add(userID);
        try
        {
            NewNetworking.sendRequest("getNotifications", sentData);
            returnData = NewNetworking.read();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return returnData;
    }
    public static void acceptRequest(int userId)
    {
        /*
            Accepts a friendship request sent by the specified user.
        */
        ArrayList sentData = new ArrayList();
        sentData.add(userID);
        sentData.add(userId);
        try
        {
            NewNetworking.sendRequest("acceptRequest", sentData);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    public static void rejectRequest(int userId)
    {
        //Rejects a request sent by the specified user.
        ArrayList sentData = new ArrayList();
        sentData.add(userID);
        sentData.add(userId);
        try
        {
            NewNetworking.sendRequest("rejectRequest", sentData);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    public static ArrayList searchInterest(String interest)
    {
        //Searches for the specified interest
        ArrayList returnData = new ArrayList();
        ArrayList sent = new ArrayList();
        sent.add(interest);
        try 
        {
            NewNetworking.sendRequest("searchInterest", sent);
            returnData = NewNetworking.read();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return returnData;
    }
    public static void addPost(String post)
    {
        //Adds a new post
        ArrayList send = new ArrayList();
        send.add(userID);
        send.add(post);
        try
        {
            NewNetworking.sendRequest("addPost", send);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    public static ArrayList getYourPosts()
    {
        //Gets posts by the current user
        ArrayList send = new ArrayList();
        ArrayList returned = new ArrayList();
        send.add(userID);
        try
        {
            NewNetworking.sendRequest("getYourPosts", send);
            returned = NewNetworking.read();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return returned;
    }
    public static void deleteProfile()
    {
        //Removes the users profile
        ArrayList send = new ArrayList();
        send.add(userID);
        try
        {
            NewNetworking.sendRequest("deleteProfile", send);
        }
        catch (Exception e) 
        {
            System.out.println(e);
        }
    }
    public static void updateProfile(ArrayList data)
    {
        //Updates the profile
        ArrayList send = new ArrayList();
        send.add(userID);
        for(int i = 0; i < data.size(); i++)
        {
            send.add(data.get(i));
        }
        try
        {
            NewNetworking.sendRequest("updateProfile", send);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    public static ArrayList getFriendsPosts()
    {
        //Gets posts made by friends
        ArrayList send = new ArrayList();
        ArrayList returned = new ArrayList();
        send.add(userID);
        try
        {
            NewNetworking.sendRequest("getFriendsPosts", send);
            returned = NewNetworking.read();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return returned;
    }
    public static ArrayList getList()
    {
        //Gets the list of users on the chat system.
        ArrayList send = new ArrayList();
        try
        {
            ChatNetworking.sendRequest("getList", send);
            return ChatNetworking.read();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return null;
    }
    public static ArrayList<ArrayList> getRooms()
    {
        /*
            Generates a list of rooms relevant to the user by requesting the
            rooms containing the current users IP address.
        */
        ArrayList send = new ArrayList();
        try
        {
            ChatNetworking.sendRequest("getRooms", send);
            ArrayList<ArrayList> rooms = new ArrayList();
            ArrayList items = ChatNetworking.read();
            /*
                The first loop works by searching through to find the next
                room ID code (Six characters long). It then loops from that 
                point onwards in the second loop adding all following IP
                Addresses to the new room. When the next code is found the loop
                is broken, the new room stored and the first loop index updated
                to equal the code position found by the second loop.
            
                Generates an ArrayList containing ArrayList<String>'s where the 
                String is the room code and any IP addresses.
            */
            for(int i = 0; i < items.size(); i++)
            {
                if(items.get(i).toString().length() == 6)
                {
                    ArrayList room = new ArrayList();
                    room.add(items.get(i));
                    for(int y = (i+1); y < items.size(); y++)
                    {
                        if(items.get(y).toString().length() == 6)
                        {
                            i = (y-1);
                            break;
                        }
                        else
                        {
                            room.add(items.get(y));
                        }
                    }
                    rooms.add(room);
                }
            }                       
            return rooms;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return null;
    }
    public static void addRoom(String code, String IP)
    {
        /*
            Adds a new room containing the target IP address and a randomly
            generated room ID code.
        */
        ArrayList send = new ArrayList();
        send.add(code);
        send.add(IP);
        try
        {
            ChatNetworking.sendRequest("addRoom", send);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    public static void addToRoom(String code, String IP)
    {
        //Adds the selected user to the current room.
        ArrayList send = new ArrayList();
        send.add(code);
        send.add(IP);
        try
        {
            ChatNetworking.sendRequest("addToRoom", send);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    public static void removeFromRoom(String code, String IP)
    {
        //Removes the selected user from the current room.
        ArrayList send = new ArrayList();
        send.add(code);
        send.add(IP);
        try
        {
            ChatNetworking.sendRequest("removeFromRoom", send);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}