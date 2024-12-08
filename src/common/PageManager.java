package common;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * A utility class for managing navigation between different pages (JPanels) in a Java Swing application.
 *
 * <p>The {@code PageManager} class uses a {@link CardLayout} to display one page (JPanel) at a time,
 * providing features such as:</p>
 * <ul>
 *     <li>Adding and removing pages.</li>
 *     <li>Switching between pages by name.</li>
 *     <li>Lazy-loading pages when needed.</li>
 *     <li>Maintaining navigation history for backward and forward navigation.</li>
 *     <li>Clearing history and managing specific page states, such as resetting to a "feed" page.</li>
 * </ul>
 *
 * <p>This class is particularly useful in applications where multiple views or screens are required,
 * such as a social media app, e-commerce platform, or any multi-page desktop application.</p>
 *
 * <h3>Features:</h3>
 * <ul>
 *     <li><strong>Lazy Loading:</strong> Pages can be created on-demand using a {@code PageCreator} functional interface.</li>
 *     <li><strong>Navigation History:</strong> Keeps track of visited pages to allow backward and forward navigation.</li>
 *     <li><strong>Reset Functionality:</strong> Allows clearing history and resetting the manager to a default page.</li>
 * </ul>
 *
 * <p>All pages are identified by unique string names, making it easy to manage and switch between them.
 * Pages that are no longer needed can be removed to conserve memory.</p>
 *
 * @see CardLayout
 * @see JPanel
 * @version 12/08/2024
 */
public class PageManager {
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final Map<String, JPanel> pages;
    private final Stack<String> history;
    private final Stack<String> forwardHistory;
    private String currentPage;

    /**
     * Constructs a new PageManager.
     */
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
     * @param name The unique name of the page.
     * @param page The JPanel representing the page.
     */
    public void addPage(String name, JPanel page) {
        pages.put(name, page);
        cardPanel.add(page, name);
    }

    /**
     * Switches to the specified page.
     *
     * @param name The name of the page to display.
     * @throws IllegalArgumentException if the specified page does not exist.
     */
    public void showPage(String name) {
        if (!pages.containsKey(name)) {
            throw new IllegalArgumentException("Page not found: " + name);
        }

        if ("feed".equals(name)) {
            clearHistoryAndPagesExceptFeed();
        } else if (currentPage != null && !name.equals(currentPage)) {
            history.push(currentPage); // Add current page to history
            forwardHistory.clear();   // Clear forward history
        }

        cardLayout.show(cardPanel, name);
        currentPage = name;
    }

    /**
     * Returns the name of the current page.
     *
     * @return The name of the current page, or {@code null} if no page is displayed.
     */
    public String getCurrentPage() {
        return currentPage;
    }

    /**
     * Lazily loads and displays a page if it doesn't already exist.
     *
     * @param name    The name of the page.
     * @param creator A function to create the page if it does not exist.
     */
    public void lazyLoadPage(String name, PageCreator creator) {
        if (!pages.containsKey(name)) {
            JPanel page = creator.createPage();
            addPage(name, page);
        }
        showPage(name);
    }

    /**
     * Returns the JPanel containing all pages.
     *
     * @return The main JPanel with the CardLayout.
     */
    public JPanel getCardPanel() {
        return cardPanel;
    }

    /**
     * Removes a page from the manager.
     *
     * @param name The name of the page to remove.
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
     * Prints the contents of the history stack for debugging purposes.
     */
    public void printHistory() {
        System.out.println("---------- History Stack ----------");
        for (String page : history) {
            System.out.println(page);
        }
    }

    /**
     * Navigates back to the previous page in history, if available.
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
     * Navigates forward to the next page in forward history, if available.
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
     * Clears history and removes all pages except the "feed" page.
     */
    private void clearHistoryAndPagesExceptFeed() {
        history.clear();
        forwardHistory.clear();

        // Retain only the "feed" page
        pages.keySet().removeIf(name -> !name.equals("feed"));

        cardPanel.removeAll();
        if (pages.containsKey("feed")) {
            cardPanel.add(pages.get("feed"), "feed");
        }
    }

    /**
     * Functional interface for lazy page creation.
     */
    @FunctionalInterface
    public interface PageCreator {
        /**
         * Creates a new page.
         *
         * @return A JPanel representing the page.
         */
        JPanel createPage();
    }
}
