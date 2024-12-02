package uiPage;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class PageManager {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Map<String, JPanel> pages;
    private String currentPage;
    private Stack<String> history;
    private Stack<String> forwardHistory;

    public PageManager() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        pages = new HashMap<>();
        history = new Stack<>();
        forwardHistory = new Stack<>();
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
            if (name.equals("feed")) {
                // Clear history and pages before showing the "feed" page
                clearHistoryAndPagesBeforeFeed();
            } else {
                if (currentPage != null && !name.equals(currentPage)) {
                    history.push(currentPage); // Add current page to history
                    forwardHistory.clear();   // Clear forward history when a new page is shown
                }
            }
            cardLayout.show(cardPanel, name);
            currentPage = name; // Update the current page
        } else {
            throw new IllegalArgumentException("Page not found: " + name);
        }
    }

    public String getCurrentPage() {
        return currentPage; // Return the last shown page
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
        currentPage = name; // Update the current page
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
            history.remove(name);
            forwardHistory.remove(name);
        }
    }

    /**
     * Prints the contents of the history stack.
     */
    public void printHistory() {
        System.out.println("----------History Stack:----------");
        for (String page : history) {
            System.out.println(page);
        }
    }

    /**
     * Goes back to the previous page in history, if available.
     */
    public void goBack() {
        if (!history.isEmpty()) {
            String previousPage = history.pop();
            if (currentPage != null) {
                forwardHistory.push(currentPage); // Add current page to forward history
            }
            cardLayout.show(cardPanel, previousPage);
            currentPage = previousPage;
        }
    }

    /**
     * Goes forward to the next page in forward history, if available.
     */
    public void goForward() {
        if (!forwardHistory.isEmpty()) {
            String nextPage = forwardHistory.pop();
            if (currentPage != null) {
                history.push(currentPage); // Add current page to history
            }
            cardLayout.show(cardPanel, nextPage);
            currentPage = nextPage;
        }
    }

    /**
     * Clears the history and removes all pages except the "feed" page.
     */
    private void clearHistoryAndPagesBeforeFeed() {
        // Clear history and forward history
        history.clear();
        forwardHistory.clear();

        // Remove all pages except "feed"
        pages.keySet().removeIf(name -> !name.equals("feed"));

        // Rebuild the card panel with only the "feed" page
        cardPanel.removeAll();
        if (pages.containsKey("feed")) {
            cardPanel.add(pages.get("feed"), "feed");
        }
    }

    // Functional interface for lazy page creation
    @FunctionalInterface
    public interface PageCreator {
        JPanel createPage();
    }
}
