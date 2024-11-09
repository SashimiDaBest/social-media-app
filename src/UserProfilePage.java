import javax.swing.*;
        import java.awt.*;
        import javax.swing.border.EmptyBorder;

public class UserProfilePage extends JPanel {
    public UserProfilePage(CardLayout cardLayout, JPanel cardPanel) {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("User Profile Page", JLabel.CENTER);

        JPanel navigationPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        JButton nextButton = new JButton("Next");
        navigationPanel.add(backButton, BorderLayout.WEST);
        navigationPanel.add(nextButton, BorderLayout.EAST);

        add(title, BorderLayout.NORTH);
        add(navigationPanel, BorderLayout.SOUTH);
    }

}

