package socnet_server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Process {
    public static ArrayList validateLogin(String username, String password)
    {
        /*
            Queries the database to check if the login details are correct.
            Does this by checking to see if a single row is returned by the
            select statement.
        */
        ArrayList returnData = new ArrayList();
        Connection conn = null;
        try 
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/socnet?" +
                "user=root&password=root");
            String statement = "SELECT userID FROM users WHERE "
                + "username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(statement);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            int count = 0;
            while(rs.next()) 
            {
                System.out.println(rs.getRow());
                count++;
            }
            rs.absolute(1);
            if(count == 1)
            {
                returnData.add("true");
                returnData.add((rs.getString("userID")));
                return returnData;
            }
            close(rs,stmt,conn);
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        } 
        catch (ClassNotFoundException 
                | InstantiationException | IllegalAccessException ex) 
        {
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
        }
        returnData.add("false");
        return returnData;
    }
    public static ArrayList register (
            String username, String password, String email)
    {
        /*
            Attempts to insert a new row into the users table then checks
            to see whether that new row can be found. If found then the 
            registration is confirmed, else the registration failed.
        */
        Connection conn = null;
        try 
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/socnet?" +
                "user=root&password=root");
            String statement = "INSERT INTO `users`" +
                "(`username`," +
                "`password`," +
                "`email`) " +
                "VALUES" +
                "(?,?,?);";
            PreparedStatement stmt = conn.prepareStatement(statement);
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, email);
                stmt.execute();
                
            ArrayList validated = validateLogin(username,password);
            if(validated.get(0) == "true")
            {
                String state2 = "INSERT INTO profiles VALUES (?,'','','')";
                PreparedStatement stmt2 = conn.prepareStatement(state2);
                stmt2.setInt(1, Integer.parseInt(validated.get(1).toString()));
                stmt2.execute();
                
                return validated;
            }
            close(null,stmt,conn);
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException | InstantiationException | 
                IllegalAccessException ex) 
        {
            Logger.getLogger(SocNet_Server.class.getName()
                ).log(Level.SEVERE, null, ex);
        }
        ArrayList retFalse = new ArrayList();
        retFalse.add("false");
        return retFalse;        
    }
    public static void acceptRequest(int from, int to)
    {
        /*
            Accepts the request based on the userID's of the sender and 
            recipient. Inserts the users information into the frienships
            table.
        */
        Connection conn = null;
        try 
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/socnet?" +
                "user=root&password=root");
            String statement = "INSERT INTO friendships (user, friend) "
                    + "VALUES (?, ?);";
            PreparedStatement stmt = conn.prepareStatement(statement);
            stmt.setInt(1,from);
            stmt.setInt(2,to);
            stmt.execute();
            
            String statement2 = "INSERT INTO friendships (user, friend) "
                    + "VALUES (?, ?);";
            PreparedStatement stmt2 = conn.prepareStatement(statement2);
            stmt2.setInt(1, to);
            stmt2.setInt(2, from);
            stmt2.execute();

            rejectRequest(from,to);
            close(null,stmt,conn);
        } 
        catch (SQLException e)
        {
            e.printStackTrace();
        } 
        catch (ClassNotFoundException | InstantiationException | 
                IllegalAccessException ex) 
        {
            Logger.getLogger(SocNet_Server.class.getName()
                ).log(Level.SEVERE, null, ex);
        }
    }
    public static void rejectRequest(int from, int to)
    {
        /*
            Rejects the specified request, does this by simply deleting the
            request from the notifications table.
        */
        Connection conn = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/socnet?" +
                "user=root&password=root");
            String statement = "DELETE FROM notifications "
                + "WHERE nFrom = ? AND nTo = ? OR nTo = ? AND nFrom = ?";
            PreparedStatement stmt = conn.prepareStatement(statement);
            stmt.setInt(1, from);
            stmt.setInt(2, to);
            stmt.setInt(3, from);
            stmt.setInt(4, to);
            stmt.execute();
            
            close(null,stmt,conn);
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        } 
        catch (ClassNotFoundException | InstantiationException | 
                IllegalAccessException ex)
        {
            Logger.getLogger(SocNet_Server.class.getName()
                ).log(Level.SEVERE, null, ex);
        }
    }
    public static void addInterest(String username, String interest) 
    {
        /*
            Inserts a new row into the interests table containing the userID
            and the interest.
        */
        Connection conn = null;
        try 
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/socnet?" +
                "user=root&password=root");
            String statement = "INSERT INTO interests (userID, interest) VALUES (?,?);";
            PreparedStatement stmt = conn.prepareStatement(statement);
            stmt.setString(1,username);
            stmt.setString(2,interest);
            stmt.execute();
                
            close(null,stmt,conn);
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException | InstantiationException | 
                IllegalAccessException ex) 
        {
            Logger.getLogger(SocNet_Server.class.getName()
                ).log(Level.SEVERE, null, ex);
        }
    }
    public static void removeInterest(String user, String interest)
    {
        /*
            Deletes the specified interest for the specified user from the
            interests table.
        */
        Connection conn = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/socnet?" +
                "user=root&password=root");
            String statement = "DELETE FROM interests WHERE userID = ? AND interest = ? ;";
            PreparedStatement stmt = conn.prepareStatement(statement);
            stmt.setString(1,user);
            stmt.setString(2,interest);
            stmt.execute();
            close(null,stmt,conn);
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        } 
        catch (ClassNotFoundException | InstantiationException | 
                IllegalAccessException ex) 
        {
            Logger.getLogger(SocNet_Server.class.getName()
                ).log(Level.SEVERE, null, ex);
        }
    }
    public static ArrayList searchName(String name)
    {
        /*
            Searches the users table for the specified username. Any results
            are added to the return data ArrayList
        */
        ArrayList retData = new ArrayList();
        Connection conn = null;
        try 
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/socnet?" +
                "user=root&password=root");
            String statement = "SELECT username, userID FROM users WHERE "
                + "username LIKE ?;";
            PreparedStatement stmt = conn.prepareStatement(statement);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                retData.add(rs.getString("username"));
                retData.add(rs.getString("userID"));
            }
            close(rs,stmt,conn);
            return retData;
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        } 
        catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException ex) 
        {
            Logger.getLogger(SocNet_Server.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        retData.add("false");
        return retData;
    }
    public static ArrayList searchInterest(String interest) 
    {
        /*
            Searches the interests table for the specified interest, returns the
            userID and username of the individual results.
        */
        ArrayList retData = new ArrayList();
        Connection conn = null;
        try 
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/socnet?" +
                "user=root&password=root");
            String statement = "SELECT users.username, users.userID FROM users, interests"
                + " WHERE users.userID = interests.userID AND"
                + " interests.interest LIKE ?;";
            PreparedStatement stmt = conn.prepareStatement(statement);
            stmt.setString(1, interest);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                retData.add(rs.getString("users.username"));
                retData.add(rs.getString("users.userID"));
            }
            close(rs,stmt,conn);
            return retData;
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        } 
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException ex) 
        {
            Logger.getLogger(SocNet_Server.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        retData.add("false");
        return retData;
    }
            
    public static void addFriend(int from, int to)
    {
        /*
            Sends a request to the selected user.
        */
        Connection conn = null;
        try 
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/socnet?" +
                "user=root&password=root");
            String check = "SELECT nTo FROM notifications WHERE (nFrom = ? "
                + "AND nTo = ?) OR (nFrom = ? AND nTo = ?);";
            PreparedStatement stmtCheck = conn.prepareStatement(check);
            stmtCheck.setInt(1, from);
            stmtCheck.setInt(2, to ); 
            stmtCheck.setInt(3, to);
            stmtCheck.setInt(4, from);
            ResultSet rs = stmtCheck.executeQuery();
            int count = 0;
            while(rs.next())
            {
                count++;
            }
            close(rs,stmtCheck,null);
            if(count == 0)
            {
                String statement = "INSERT INTO notifications (nFrom, nTo) "
                    + "VALUES (?,?);";
                PreparedStatement stmt = conn.prepareStatement(statement);
                stmt.setInt(1, from);
                stmt.setInt(2, to );
                stmt.execute();
                stmt.close();
            }
            conn.close();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        } 
        catch (ClassNotFoundException | InstantiationException | 
            IllegalAccessException ex) 
        {
            Logger.getLogger(SocNet_Server.class.getName()
                ).log(Level.SEVERE, null, ex);
        }
    }
    
    public static ArrayList getNotifications(int userID)
    {
        /*
            Gets the list of notifications for the userID specified.
        */
        ArrayList retData = new ArrayList();
        Connection conn = null;
        try 
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/socnet?" +
                "user=root&password=root");
            String statement = "SELECT username, userID "
                + "FROM users, notifications WHERE notifications.nFrom = userID "
                + "AND notifications.nTo = ?";
            PreparedStatement stmt = conn.prepareStatement(statement);
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                retData.add(rs.getString("username"));
                retData.add(rs.getString("userID"));
            }
            close(rs,stmt,conn);
            return retData;
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        } catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException ex) 
        {
            Logger.getLogger(SocNet_Server.class.getName()).log(
                Level.SEVERE, null, ex);
        }
        return retData;
    }
    
    public static ArrayList getProfile(int userID)
    {
        /*
            Gets the profile information for the specified user. Any empty 
            fields are given blank space characters as a replacement.
        */
        ArrayList retData = new ArrayList();
        Connection conn = null;
        try 
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/socnet?" +
                "user=root&password=root");
            String statement = "SELECT name, location, description FROM "
                + "profiles WHERE userID = ?";
            PreparedStatement stmt = conn.prepareStatement(statement);
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                if(rs.getString("name").equals(""))
                {
                    retData.add(" ");
                }
                else
                {
                    retData.add(rs.getString("name"));
                }
                if(rs.getString("location").equals(""))
                {
                    retData.add(" ");
                }
                else
                {
                    retData.add(rs.getString("location"));
                }
                if(rs.getString("description").equals(""))
                {
                    retData.add(" ");
                }
                else
                {
                    retData.add(rs.getString("description"));
                }
            }
            String stmt2 = "SELECT interest FROM interests WHERE userID = ?";
            stmt = conn.prepareStatement(stmt2);
            stmt.setInt(1, userID);
            rs = stmt.executeQuery();
            while(rs.next())
            {
                retData.add(rs.getString("interest"));
            }
            close(rs,stmt,conn);
            return retData;
        } 
        catch (SQLException e)
        {
            e.printStackTrace();
        } 
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException ex)
        {
            Logger.getLogger(SocNet_Server.class.getName()).log(
                Level.SEVERE, null, ex);
        }
        return retData;
    }
    
    public static void addPost(int userID, String post)
    {
        /*
            Adds a new post into the posts table.
        */
        Connection conn = null;
        try 
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/socnet?" +
                "user=root&password=root");
            String statement = "INSERT INTO posts (userID, post) VALUES (?,?);";
            PreparedStatement stmt = conn.prepareStatement(statement);
            stmt.setInt(1, userID);
            stmt.setString(2, post);
            stmt.execute();
            close(null,stmt,conn);
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        } 
        catch (ClassNotFoundException | InstantiationException | 
            IllegalAccessException ex) 
        {
            Logger.getLogger(SocNet_Server.class.getName()
                ).log(Level.SEVERE, null, ex);
        }
    }
    
    public static ArrayList getYourPosts(int userID)
    {
        /*
            Returns a list of your posts from the database.
        */
        ArrayList returned = new ArrayList();
        Connection conn = null;
        try 
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/socnet?" +
                "user=root&password=root");
            String statement = "SELECT post FROM posts WHERE userID = ?";
            PreparedStatement stmt = conn.prepareStatement(statement);
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                returned.add(rs.getString("post"));
            }
            close(rs,stmt,conn);
            return returned;
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        } 
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException ex) 
        {
            Logger.getLogger(SocNet_Server.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        return returned;
    }
    
    public static void deleteProfile(int userID)
    {
        /*
            Deletes the user and their associated data 
        */
        Connection conn = null;
        try 
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/socnet?" +
                "user=root&password=root");
            String statement = "DELETE FROM profiles WHERE userID = ?";
            PreparedStatement stmt = conn.prepareStatement(statement);
            stmt.setInt(1, userID);
            stmt.execute();
            String statement2 = "DELETE FROM posts WHERE userID = ?";
            PreparedStatement stmt2 = conn.prepareStatement(statement2);
            stmt2.setInt(1, userID);
            stmt2.execute();
            String statement3 = "DELETE FROM friendships "
                + "WHERE user = ? OR friend = ?";
            PreparedStatement stmt3 = conn.prepareStatement(statement3);
            stmt3.setInt(1, userID);
            stmt3.setInt(2, userID);
            stmt3.execute();
            String statement4 = "DELETE FROM notifications "
                + "WHERE nTo = ? OR nFrom = ?";
            PreparedStatement stmt4 = conn.prepareStatement(statement4);
            stmt4.setInt(1, userID);
            stmt4.setInt(2, userID);
            stmt4.execute();
            String statement5 = "DELETE FROM users WHERE userID = ?";
            PreparedStatement stmt5 = conn.prepareStatement(statement5);
            stmt5.setInt(1, userID);
            stmt5.execute();
            String statement6 = "DELETE FROM interests WHERE userID = ?";
            PreparedStatement stmt6 = conn.prepareStatement(statement6);
            stmt6.setInt(1, userID);
            stmt6.execute();
            stmt2.close();
            stmt3.close();
            stmt4.close();
            stmt5.close();
            stmt6.close();
            close(null,stmt,conn);
        } 
        catch (SQLException e)
        {
            e.printStackTrace();
        } 
        catch (ClassNotFoundException | InstantiationException | 
            IllegalAccessException ex) 
        {
            Logger.getLogger(SocNet_Server.class.getName()
                ).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void updateProfile(int userID, String name, 
        String description, String location)
    {
        /*
            Saves the profile information, updates the row for the specified
            user.
        */
        Connection conn = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/socnet?" +
                "user=root&password=root");
            String statement = "UPDATE profiles SET name = ?, description = ?, "
                + "location = ? WHERE userID = ?";
            PreparedStatement stmt = conn.prepareStatement(statement);
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setString(3, location);
            stmt.setInt(4, userID);
            stmt.execute();
            close(null,stmt,conn);
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException | InstantiationException | 
            IllegalAccessException ex)
        {
            Logger.getLogger(SocNet_Server.class.getName()
                ).log(Level.SEVERE, null, ex);
        }
    }
    public static ArrayList getFriendsPosts(int userID)
    {
        /*
            Queries the database and returns posts submitted by your friends.
            Limits posts returned to five.
        */
        ArrayList returned = new ArrayList();
        Connection conn = null;
        try 
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/socnet?" +
                "user=root&password=root");
            String statement = "SELECT friend FROM friendships WHERE user = ?";
            PreparedStatement stmt = conn.prepareStatement(statement);
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                String statement2 = "SELECT post FROM posts "
                    + "WHERE userID = ? LIMIT 0,5";
                PreparedStatement stmt2 = conn.prepareStatement(statement2);
                stmt2.setInt(1, rs.getInt("friend"));
                ResultSet rs2 = stmt2.executeQuery();
                while(rs2.next())
                {
                    returned.add(rs2.getString("post"));
                }
            }
            close(rs,stmt,conn);
            return returned;
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException ex) 
        {
            Logger.getLogger(SocNet_Server.class.getName()).log(
                Level.SEVERE, null, ex);
        }
        return returned;
    }
    public static void close(ResultSet rs, PreparedStatement stmt, Connection conn)
    {
        /*
            Closes the results set, prepared statement and connection.
        */
        try
        {
            if(rs != null)
            {
                rs.close();
            }
            if(stmt != null)
            {
                stmt.close();
            }
            if(conn != null)
            {
                conn.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}