import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FeedViewPage extends JPanel {
    public FeedViewPage(CardLayout cardLayout, JPanel cardPanel) {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        JButton profileIconButton = new JButton("Profile icon");
        profileIconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "userProfilePage");
            }
        });
        headerPanel.add(profileIconButton);

        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        JButton createChatButton = new JButton("Create chat");
        JTextField userSearchField = new JTextField(15);
        searchPanel.add(userSearchField);
        searchPanel.add(createChatButton);

        JPanel chatFeedPanel = new JPanel();
        chatFeedPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        chatFeedPanel.setLayout(new BoxLayout(chatFeedPanel, BoxLayout.X_AXIS));

        JPanel chatSelectionPanel = new JPanel();
        chatSelectionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        chatSelectionPanel.setLayout(new BoxLayout(chatSelectionPanel, BoxLayout.Y_AXIS));

        JPanel chatViewPanel = new JPanel();
        chatViewPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        chatViewPanel.setLayout(new BoxLayout(chatViewPanel, BoxLayout.X_AXIS));

        chatFeedPanel.add(chatSelectionPanel);
        chatFeedPanel.add(chatViewPanel);



        JPanel navigationPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        JButton nextButton = new JButton("Next");
        navigationPanel.add(backButton, BorderLayout.WEST);
        navigationPanel.add(nextButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
        add(chatFeedPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }
}
