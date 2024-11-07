import javax.swing.*;
import java.awt.*;

public class WelcomePage extends JPanel {

    public WelcomePage() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Welcome to Page 1");
        add(label, BorderLayout.CENTER);
    }
    
}
