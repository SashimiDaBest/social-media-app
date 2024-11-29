package uiPage;

import javax.swing.*;

import clientPageOperation.UserPageClient;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class OtherProfilePage  extends JPanel{

    private JButton followButton;
    private JButton blockButton;
    private JButton followerButton;
    private JButton followingButton;

    private PageManager pageManager;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public OtherProfilePage(PageManager pageManager, BufferedWriter writer, BufferedReader reader, String otherUsername) {
        this.pageManager = pageManager;
        this.bufferedWriter = writer;
        this.bufferedReader = reader;
        
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Other User Profile Page", JLabel.CENTER);

        JPanel navigationPanel = new JPanel(new BorderLayout());

        followButton = new JButton("Follow/Unfollow Other");
        blockButton = new JButton("Block/Unblock Other");
        followerButton = new JButton("View Follower");
        followingButton = new JButton("View Following");
        
        JButton backButton = new JButton("Back");
        JButton nextButton = new JButton("Next");

        navigationPanel.add(followButton);
        navigationPanel.add(blockButton);
        navigationPanel.add(followerButton);
        navigationPanel.add(followingButton);
        navigationPanel.add(backButton, BorderLayout.WEST);
        navigationPanel.add(nextButton, BorderLayout.EAST);

        add(title, BorderLayout.NORTH);
        add(navigationPanel, BorderLayout.SOUTH);
        setupActionListeners();
    }


    private void setupActionListeners() {

        followButton.addActionListener(new ActionListener() {
            
            @Override 
            public void actionPerformed(ActionEvent e) {
                UserPageClient.write("1", bufferedWriter);
            }
        });
        

    }
}
