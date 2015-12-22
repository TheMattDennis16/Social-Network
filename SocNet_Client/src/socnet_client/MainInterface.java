package socnet_client;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JTextArea;
import socnet_client.chat.Message;

public class MainInterface extends javax.swing.JFrame {
    static Boolean signedIn = false;
    public MainInterface() {
        initComponents();
        createLogin();
    }
    JPanel panel = new JPanel();
    JTextField txtUsername = new JTextField();
    JPasswordField txtPass = new JPasswordField();
    JTextField txtEmail = new JTextField();
    static JList otherPosts = new JList();
    static JList yourPosts = new JList();
        
    private void createLogin()
    {
        /*
            Creates the default login form. Attempts to setup a connection 
            with the server.
        */
        this.setSize(300, 400);
        if(signedIn = false)
        {
            signedIn = NewNetworking.setup();
        }
        JLabel title = new JLabel();
        JButton signin = new JButton();
        JButton register = new JButton();
        JButton chat = new JButton("Start chat client");
        chat.setLocation(40, 300);
        chat.setSize(200, 40);
        chat.setVisible(true);
        chat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                Message message = new Message();
                message.setVisible(true);
            }
        });
        panel.setSize(300, 400);
        panel.setLocation(0, 0);
        
        txtUsername.setSize(200,25);
        txtUsername.setLocation(40,140);
        txtUsername.setToolTipText("Enter your username here.");
        txtUsername.setVisible(true);
        
        txtPass.setSize(200,25);
        txtPass.setLocation(40, 180);
        txtPass.setToolTipText("Enter your password here.");
        txtPass.setVisible(true);
        
        title.setText("Please sign in");
        title.setSize(100,80);
        title.setLocation(90,0);
        title.setVisible(true);

        signin.setText("Sign in");
        signin.setToolTipText("Sign in to the network");
        signin.setSize(80, 40);
        signin.setLocation(40,250);
        signin.setVisible(true);
        signin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                /*
                    Checks to see whether a connection exists, attempts the 
                    login process.
                */
                if(!signedIn) 
                {
                    NewNetworking.setup();
                }
                if(Process.validateLogin(txtUsername.getText(), txtPass.getText()))
                {
                    clearForm();
                    createMain();
                }
                else
                {
                    System.out.println("Couldn's sign in!");
                }
            }
        });
        
        register.setText("Register");
        register.setToolTipText("Register a new account in the network");
        register.setSize(80,40);
        register.setLocation(160,250);
        register.setVisible(true);
        register.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                //Clears the current form, creates the register form.
                clearForm();
                createRegister();
            }
        });     

        panel.setLayout(null);
        panel.add(chat);
        panel.add(title);
        panel.add(signin);
        panel.add(register);
        panel.add(txtUsername);
        panel.add(txtPass);
        panel.setVisible(true);
        this.add(panel);
    }
    private void clearForm()
    {
        //Clears all components from the form.
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }
    private void createRegister()
    {      
        //Creates the register form.
        JButton signin = new JButton();
        signin.setText("Submit");
        signin.setToolTipText("Submit your details to the network!");
        signin.setSize(210, 40);
        signin.setLocation(40,300);
        signin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                //Checks connection, attempts to register
                if(!signedIn) 
                {
                    NewNetworking.setup();
                }
                if(Process.register(txtUsername.getText(),txtPass.getText(), txtEmail.getText()))
                {
                    System.out.println("Registered!");
                    clearForm();
                    createMain();
                }
                else
                {
                    System.out.println("Not registered!");
                }
            }
        });
        signin.setVisible(true);
        
        JLabel signInLbl = new JLabel("Enter your username:");
        signInLbl.setLocation(43,43);
        signInLbl.setSize(210,100);
        signInLbl.setVisible(true);
        txtUsername.setSize(210,25);
        txtUsername.setLocation(40,100);
        txtUsername.setToolTipText("Enter your username here.");
        txtUsername.setVisible(true);
        
        JLabel passLbl = new JLabel("Enter your password:");
        passLbl.setLocation(43, 83);
        passLbl.setSize(210,100);
        passLbl.setVisible(true);
        txtPass.setSize(210,25);
        txtPass.setLocation(40, 140);
        txtPass.setToolTipText("Enter your password here.");
        txtPass.setVisible(true);
        
        JLabel emailLbl = new JLabel("Enter your email address:");
        emailLbl.setLocation(43, 123);;
        emailLbl.setSize(200,100);
        emailLbl.setVisible(true);
        txtEmail.setSize(210,25);
        txtEmail.setLocation(40, 180);
        txtEmail.setToolTipText("Enter your email address here.");
        txtEmail.setVisible(true);
   
        panel.add(emailLbl);
        panel.add(passLbl);
        panel.add(txtEmail);
        panel.add(txtUsername);
        panel.add(txtPass);
        panel.add(signin);
        panel.add(signInLbl);
    }
    
    static JList yourInterests = new JList();
    static JTextField yourName = new JTextField();
    static JTextField location = new JTextField();
    static JTextArea description = new JTextArea(); 
    
    public void createMain()
    {
        /*
            Creates main form.
        */
        this.setSize(700, 600); 
        this.add(panel);
        JLabel yourNameLbl = new JLabel("Your name:");
        JLabel yourPostsLbl = new JLabel("Your Posts:");
        JLabel otherPostsLbl = new JLabel("Friends Posts:");
        JLabel yourInterestsLbl = new JLabel("Your Interests:");
        JLabel locationLbl = new JLabel("Your location:");
        JLabel descriptionLbl = new JLabel("Your description:");
        
        JButton update = new JButton("Save Profile");
        JButton notification = new JButton();
        JButton sendMessage = new JButton("Start chatting");
        JButton addPost = new JButton("Add Post");
        JButton newInterest = new JButton("Add");
        JButton removeInterest = new JButton("Remove");
        JButton searchForUser = new JButton("Search for user");
        JButton deleteProfile = new JButton();
        
        update.setSize(180,30);
        update.setLocation(490, 280);
        update.setVisible(true);
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                ArrayList data = new ArrayList();
                /*
                    Checks to see whether the user has entered data, inserts 
                    a blank space character otherwise, this prevents 
                    OutOfBounds Exception errors when the server reads in
                    the sent data.
                */
                if(yourName.getText().equals(""))
                {
                    data.add(" ");
                }
                else
                {
                    data.add(yourName.getText());
                }
                if(description.getText().equals(""))
                {
                    data.add(" ");
                }
                else
                {
                    data.add(description.getText());    
                }
                if(location.getText().equals(""))
                {
                    data.add(" ");
                }
                else
                {
                    data.add(location.getText());
                }
                Process.updateProfile(data);
            }
        });
        
        deleteProfile.setSize(180, 30);
        deleteProfile.setText("Delete Account");
        deleteProfile.setLocation(490,360);
        deleteProfile.setVisible(true);
        deleteProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                //Deletes the users account, returns them to the login form.
                Process.deleteProfile();
                clearForm();
                createLogin();
            }
        });
        
        searchForUser.setSize(180,30);
        searchForUser.setLocation(490,70);
        searchForUser.setVisible(true);
        searchForUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                //Opens the FindFriend window, letting the user search for people
                FindFriend addFriend = new FindFriend();
                addFriend.setVisible(true);
            }
        });
        
        sendMessage.setSize(180,30);
        sendMessage.setLocation(490, 140);
        sendMessage.setVisible(true);
        sendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                //Opens the message window
                Message msg = new Message();
                msg.setVisible(true);
            }
        });
                
        newInterest.setSize(100,35);
        newInterest.setLocation(270,240);
        newInterest.setVisible(true);
        newInterest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                //Opens the add interest window.
                AddInterest addInterest = new AddInterest();
                addInterest.setVisible(true);
            }
        });
        removeInterest.setSize(100,35);
        removeInterest.setLocation(370, 240);
        removeInterest.setVisible(true);
        removeInterest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                //Removes the currently selected interest
                Process.removeInterest(yourInterests.getSelectedValue().toString());
            }
        });
        
        locationLbl.setSize(100,25);
        locationLbl.setLocation(273, 0);
        locationLbl.setVisible(true);
        location.setSize(200, 25);
        location.setLocation(270, 20);
        location.setText("");
        location.setVisible(true);
        
        yourNameLbl.setLocation(13,0);
        yourNameLbl.setSize(100, 25);
        yourNameLbl.setVisible(true);
        yourName.setSize(200, 25);
        yourName.setText(""); 
        yourName.setLocation(10,20);
        yourName.setVisible(true);
        
        descriptionLbl.setSize(200,25);
        descriptionLbl.setLocation(13, 50);
        descriptionLbl.setVisible(true);
        description.setSize(250, 200);
        description.setLocation(10, 75);
        description.setVisible(true);
        
        yourInterestsLbl.setSize(100, 25);
        yourInterestsLbl.setLocation(273, 50);
        yourInterests.setSize(200, 165);
        yourInterests.setLocation(270, 75);
        yourInterests.setVisible(true);
        
        yourPostsLbl.setLocation(13, 297);
        yourPostsLbl.setSize(100,25);
        yourPostsLbl.setVisible(true);
        yourPosts.setLocation(13, 320);
        yourPosts.setSize(240,200);
        yourPosts.setVisible(true);
                
        otherPostsLbl.setLocation(270, 297);
        otherPostsLbl.setSize(100,25);
        otherPostsLbl.setVisible(true);
        otherPosts.setSize(200, 165);
        otherPosts.setLocation(270, 320);
        otherPosts.setVisible(true);
        
        addPost.setLocation(270, 490);
        addPost.setSize(200,35);
        addPost.setVisible(true);
        addPost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                //Opens new post form
                NewPost newPost = new NewPost();
                newPost.setVisible(true);
            }
        });
        
        notification.setSize(180,30);
        notification.setText("View notifications");
        notification.setLocation(490, 210);
        notification.setVisible(true);
        notification.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                //Views the current list of friend requests
                ViewRequests view = new ViewRequests();
                view.setVisible(true);
            }
        });
        
        panel.setVisible(true);
        panel.setLocation(0, 0);
        panel.setSize(700, 600);
        panel.setLayout(null);
        panel.add(notification);
        panel.add(yourInterestsLbl);
        panel.add(locationLbl);
        panel.add(descriptionLbl);
        panel.add(yourNameLbl);
        panel.add(yourPostsLbl);
        panel.add(otherPostsLbl);
        panel.add(otherPosts);
        panel.add(yourPosts);
        panel.add(addPost);
        panel.add(yourInterests);
        panel.add(yourName);
        panel.add(location);
        panel.add(description);
        panel.add(update);
        panel.add(sendMessage);
        panel.add(newInterest);
        panel.add(removeInterest);
        panel.add(searchForUser);
        panel.add(deleteProfile);
        this.setTitle("Your profile");
        
        //Updates in form information, starts to update on a timer.
        
        updateProfile();
        updatePosts();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() 
        {
            public void run()
            {
                updateProfile();
                updatePosts();
            }
        };
        timer.scheduleAtFixedRate(task, 60000, 60000);
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 411, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public static void updateProfile()
    {
        //Updates the profile information
        ArrayList profile = Process.getProfileData();
        yourName.setText(profile.get(0).toString());
        location.setText(profile.get(1).toString());
        description.setText(profile.get(2).toString());
        if(!profile.isEmpty())
        {
            if(profile.size() > 3)
            {
                DefaultListModel listModel = new DefaultListModel();
                for(int i = 3; i < profile.size(); i++)
                {
                    listModel.addElement(profile.get(i));
                }
                yourInterests.setModel(listModel);
            }
        }
    }
    public static void updatePosts()
    {
        //Updates the list of posts
        ArrayList yourPostCollection = Process.getYourPosts();
        ArrayList yourFriendsPost = Process.getFriendsPosts();
        if(!yourPostCollection.isEmpty())
        {
            DefaultListModel listModel = new DefaultListModel();
            for(int i = 0; i < yourPostCollection.size(); i++)
            {
                listModel.addElement(yourPostCollection.get(i));
            }
            yourPosts.setModel(listModel);
        }

        if(!yourFriendsPost.isEmpty())
        {
            DefaultListModel listModel = new DefaultListModel();
            for(int i = 0; i < yourFriendsPost.size(); i++)
            {
                listModel.addElement(yourFriendsPost.get(i));
            }
            otherPosts.setModel(listModel);
        }   
    }
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        //Removes the user from the system.
        try 
        {
            Process.logout();
        } 
        catch (Exception e) 
        {
            System.out.println("Could not contact server");
        }
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainInterface().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}