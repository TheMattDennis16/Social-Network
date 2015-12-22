package socnet_client.chat;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import socnet_client.Process;

public class Message extends javax.swing.JFrame 
{
    static int currTabs = 0;
    static private ArrayList<JTextArea> listBoxes = new ArrayList();
    static private ArrayList<InetAddress> users;
    static ArrayList<ArrayList> rooms = new ArrayList<ArrayList>();
    Update update = new Update();
    UDPRead read = new UDPRead();
    
    public Message() 
    {
        initComponents();
        setup();
    }
    public void setup()
    {
        /*
            Sets up the connection with the chat server, gets the list of 
            users currently online. Starts the read thread for the UDP 
            communication with the other clients. Starts the update loop, which
            updates the list of rooms and users by periodically getting updates
            from the server.
        */
        ChatNetworking.chatSetup();
        users = Process.getList();
        DefaultListModel listModel = new DefaultListModel();
        for(int i = 0; i < users.size(); i++)
        {
            listModel.addElement(users.get(i));
        }
        jList1.setModel(listModel);
        update.run();
        read.start();
    }
    public static void addMsg(String code, String message)
    {
        /*
            Searches through the tabs on the window for the specified room code,
            inserts a new message into the jTextArea for this room.
        */
        int tab = 0;
        for(int i = 0; i < rooms.size(); i++)
        {
            if(rooms.get(i).get(0).equals(code))
            {
                tab = i;
            }
        }
        JTextArea currentList = listBoxes.get(tab);
        currentList.setText(currentList.getText()+message+"\n");
        listBoxes.set(tab, currentList);
    }
    
    public static void newRoom(String code, String user)
    {
        /*
            Generates a new tab containing a JTextArea, jTextField and jButton
            allowing the user to view and send information with other people
            in the room.
        */
        Process.addRoom(code, user);
        ArrayList userList = new ArrayList();
        userList.add(code);
        userList.add(user);
        userList.add(ChatNetworking.hostID);
        rooms.add(userList);
        
        JButton button = new JButton("Send");
        JTextArea box = new JTextArea();
        JPanel panel = new JPanel();
        JTextField txtBox = new JTextField();
        button.setSize(65,25);
        button.setLocation(226, 208);
        button.setVisible(true);
        button.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent e) 
            {
                try 
                {
                    /*
                        Gets the current tab index, adds the message to the
                        jTextArea and sends the message to the other users in
                        the current room. Resets the textfield entry point.
                    */
                    int index = jTabbedPane1.getSelectedIndex();
                    addMsg(jTabbedPane1.getTitleAt(index),txtBox.getText());
                    System.out.println("Index: "+index+" Text: "+txtBox.getText());
                    UDPWrite.write(txtBox.getText(), rooms.get(index));
                    txtBox.setText("");
                } 
                catch (IOException ex)
                {
                    System.out.println(ex);
                }
            }
        });
        
        box.setSize(290, 208);
        box.setLocation(0, 0);
        box.setEditable(false);

        txtBox.setSize(225,25);
        txtBox.setLocation(0,209);
        
        panel.setLayout(null); 
        panel.add(button);
        panel.add(box);
        panel.add(txtBox);
        jTabbedPane1.insertTab(code, null, panel, null, currTabs);
        listBoxes.add(box);
        currTabs++;
    }
 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        butAdd = new javax.swing.JButton();
        butNew = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Message");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(250, 250));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        butAdd.setText("Add");
        butAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butAddActionPerformed(evt);
            }
        });

        butNew.setText("New");
        butNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butNewActionPerformed(evt);
            }
        });

        jButton3.setText("Remove");
        jButton3.setToolTipText("Remove the current user from the room");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(butAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(butNew, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(butAdd)
                    .addComponent(butNew)
                    .addComponent(jButton3)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTabbedPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 3, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        /*
            Closes the connection. Removes the user from the servers data
            collections.
        */
        update.stop();
        ArrayList blank = new ArrayList();
        blank.add("Blank");
        ChatNetworking.sendRequest("remove", blank);
        ChatNetworking.close();
    }//GEN-LAST:event_formWindowClosed

    private void butAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butAddActionPerformed
        //Adds the currently selected user to the current room.
        if(jTabbedPane1.getSelectedIndex() >= 0)
        {
            Process.addToRoom(rooms.get(jTabbedPane1.getSelectedIndex()).get(0).toString(),
                jList1.getSelectedValue().toString());
            ArrayList room = rooms.get(jTabbedPane1.getSelectedIndex());
            room.add(jList1.getSelectedValue());
            rooms.set(jTabbedPane1.getSelectedIndex(), room);
        }
    }//GEN-LAST:event_butAddActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //Removes the currently selected user from the current room.
        if(jTabbedPane1.getSelectedIndex() >= 0)
        {
            Process.removeFromRoom(rooms.get(jTabbedPane1.getSelectedIndex())
                    .get(0).toString(), jList1.getSelectedValue().toString());
            rooms.get(jTabbedPane1.getSelectedIndex())
                .remove(jList1.getSelectedValue());
            if(jList1.getSelectedValue().equals(ChatNetworking.hostID))
            {
                jTabbedPane1.removeTabAt(jTabbedPane1.getSelectedIndex());
                currTabs--;
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void butNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butNewActionPerformed
        //Creates a new room, generates its code and updates the server.
        newRoom(UDPWrite.genCode(), jList1.getSelectedValue().toString());
    }//GEN-LAST:event_butNewActionPerformed

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
            java.util.logging.Logger.getLogger(Message.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Message.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Message.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Message.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Message().setVisible(true);
            }
        });
    }
    public static class Update extends Thread 
    {
        /*
            Timed loop to update the users and rooms information by sending
            requesrs to the server.
        */
        public void run()
        {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() 
            {
                public void run()
                {
                    rooms = Process.getRooms();
                    users = Process.getList();
                    System.out.println("Rooms: "+rooms.size()+"\nUsers: "+users.size());
                    updateUsers();
                }
            };
            timer.scheduleAtFixedRate(task, 30000, 30000);
        }
    }
    public static void updateUsers()
    {
        /*
            Updates the form based on the information returned in the Update
            class thread.
        */
        DefaultListModel model = new DefaultListModel();
        for(int i = 0; i < users.size(); i++)
        {
            model.addElement(users.get(i));
        }
        jList1.setModel(model);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton butAdd;
    private static javax.swing.JButton butNew;
    private static javax.swing.JButton jButton3;
    private static javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
