/**
 * [StorePanel.java]
 * Display a JSplitPane that will allow the user to search songs on YouTube and download them
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;

public class StorePanel {

    private JFrame frame;

    private JSplitPane splitPane;

    private JScrollPane scrollPane;

    private JPanel searchBarPanel;
    private JPanel resultsPanel;

    private JButton enter;
    private JButton[] buttons;

    private JLabel label;

    private JTextField searchTextField;

    private YoutubeSearch search;

    private ArrayList<String> titles; //an arraylist of the titles of the videos to output to the user
    private ArrayList<String> videoIds; // an arraylist of video ids to output to the user
    private ArrayList<String> thumbnailURLs; // an arraylist of thumbnail urls (in String) to output to the user

    /**
     * StorePanel
     * constructor for this class
     * @param frame JFrame
     */
    StorePanel(JFrame frame) {
        this.frame = frame;

        splitPane = new JSplitPane();
        scrollPane = new JScrollPane();
        searchBarPanel = new JPanel();
        resultsPanel = new JPanel();

        searchBarPanel.setBackground(new Color(15, 15, 15));

        resultsPanel.setBackground(new Color(15, 15, 15));
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        //Font
        Font font = new Font("Dialog", Font.BOLD, 20);

        //Text Field
        searchTextField = new JTextField(50);
        searchTextField.setFont(font);
        searchTextField.setForeground(Color.BLACK);
        TextPrompt tp = new TextPrompt("Search Store", searchTextField);
        tp.changeAlpha(0.5f);
        addListener(searchTextField);
        searchBarPanel.add(searchTextField);

        //Button
        enter = new JButton(formatIcon(new ImageIcon("src/res/Search.png"), 40,40)); // load the image from res
        enter.setForeground(Color.LIGHT_GRAY);
        addListener(enter);
        searchBarPanel.add(enter);

        scrollPane.setViewportView(resultsPanel);

        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setEnabled(false);
        splitPane.setTopComponent(searchBarPanel);
        splitPane.setBottomComponent(scrollPane);
    }

    /**
     * showStorePanel
     * shows the store panel
     */
    public void showStorePanel() {
        splitPane.setDividerLocation(frame.getHeight() / 7); //since there should be more space for the results split pane
        splitPane.setVisible(true);
        frame.add(splitPane);
        frame.revalidate();
    }

    /**
     * getVisible
     * gets if the split pane is visible
     * @return boolean
     */
    public boolean getVisible() {
        return splitPane.isVisible();
    }

    /**
     * hideStorePanel
     * hides the store panel
     */
    public void hideStorePanel() {
        splitPane.setVisible(false);
        frame.remove(splitPane);
        frame.revalidate();
    }

    /**
     * downloadVideoID
     * downloads the video from youtube using the video id
     * @param videoID String
     */
    private void downloadVideoID(String videoID) {
        Thread thread = new Thread(new DownloadThread(videoID));
        thread.start();
    }

    /**
     * getSearchResults
     * gets the search results from the youtube api
     * @param query what the user searches
     * @return SearchResult[] array
     */
    private void getSearchResults(String query) {
        titles = new ArrayList<>();
        videoIds = new ArrayList<>();
        thumbnailURLs = new ArrayList<>();
        search = new YoutubeSearch();
        search.search(query);
        this.titles =  search.getTitles();
        this.videoIds = search.getVideoIds();
        this.thumbnailURLs = search.getThumbnailURLs();
        printSearchResults();

    }

    /**
     * printSearchResults
     * prints the youtube results of the user's input
     */
    private void printSearchResults() {
        buttons = new JButton[5];
        //Font
        Font font = new Font("Dialog", Font.BOLD, 25);

        for (int i = 0; i < titles.size(); i++) {
            try {
                URL url = new URL(thumbnailURLs.get(i));
                Image image = ImageIO.read(url);
                resultsPanel.add(Box.createVerticalStrut(20));
                label = new JLabel(titles.get(i), formatIcon(new ImageIcon(image), 250, 150), BoxLayout.X_AXIS);
                label.setFont(font);
                label.setBackground(Color.LIGHT_GRAY);
                label.setForeground(Color.WHITE);
                resultsPanel.add(label);
                resultsPanel.add(Box.createVerticalStrut(20));
                buttons[i] = new JButton(formatIcon(new ImageIcon("src/res/Download.png"), 30,30));
                buttons[i].setFont(font);
                buttons[i].setBackground(Color.LIGHT_GRAY);
                addListener(buttons[i]);
                resultsPanel.add(buttons[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * formatIcon
     * formats the image passed into the method
     * @param imageIcon
     * @param w
     * @param h
     * @return
     */
    private ImageIcon formatIcon(ImageIcon imageIcon, int w, int h) {
        Image image = imageIcon.getImage();
        Image newImage = image.getScaledInstance(w, h,  Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newImage);
        return imageIcon;
    }

    /**
     * addListener
     * adding a listener to a JTextField
     * @param textField
     */
    private void addListener(JTextField textField) {
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    getSearchResults(searchTextField.getText());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    /**
     * addListener
     * adding a listener to a JButton
     * @param button
     */
    private void addListener(JButton button) {
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getSource() == button){
                    if(e.getSource() == enter) {
                        getSearchResults(searchTextField.getText());
                    }
                    else if (e.getSource() == buttons[0]) {
                        downloadVideoID(videoIds.get(0));
                    }
                    else if (e.getSource() == buttons[1]) {
                        downloadVideoID(videoIds.get(1));
                    }
                    else if (e.getSource() == buttons[2]) {
                        downloadVideoID(videoIds.get(2));
                    }
                    else if (e.getSource() == buttons[3]) {
                        downloadVideoID(videoIds.get(3));
                    }
                    else if (e.getSource() == buttons[4]) {
                        downloadVideoID(videoIds.get(4));
                    }
                    else if (e.getSource() == buttons[5]) {
                        downloadVideoID(videoIds.get(5));
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }
}
