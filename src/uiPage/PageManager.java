package uiPage;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PageManager {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Map<String, JPanel> pages;

    public PageManager() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        pages = new HashMap<>();
    }

    /**
     * Adds a page to the manager.
     *
     * @param name  The unique name of the page
     * @param page  The page's JPanel
     */
    public void addPage(String name, JPanel page) {
        pages.put(name, page);
        cardPanel.add(page, name);
    }

    /**
     * Switches to a specific page.
     *
     * @param name The name of the page to display
     */
    public void showPage(String name) {
        if (pages.containsKey(name)) {
            cardLayout.show(cardPanel, name);
        } else {
            throw new IllegalArgumentException("Page not found: " + name);
        }
    }

    /**
     * Lazily loads a page if it doesn't already exist.
     *
     * @param name     The name of the page
     * @param creator  A function to create the page if needed
     */
    public void lazyLoadPage(String name, PageCreator creator) {
        if (!pages.containsKey(name)) {
            JPanel page = creator.createPage();
            addPage(name, page);
        }
        showPage(name);
    }

    /**
     * Returns the CardPanel containing all pages.
     */
    public JPanel getCardPanel() {
        return cardPanel;
    }

    /**
     * Removes a page from the manager.
     *
     * @param name The name of the page to remove
     */
    public void removePage(String name) {
        if (pages.containsKey(name)) {
            cardPanel.remove(pages.get(name));
            pages.remove(name);
        }
    }

    // Functional interface for lazy page creation
    @FunctionalInterface
    public interface PageCreator {
        JPanel createPage();
    }
}
