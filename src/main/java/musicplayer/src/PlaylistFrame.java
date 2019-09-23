/**
 * [PlaylistFrame.java]
 * Opens new JFrame that allows the user to create new playlists and add songs to them
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;


public class PlaylistFrame { //will run when the user clicks "New Playlist"

    private JFrame frame;
    private double screenWidth;
    private double screenHeight;

    private JPanel panel;
    private JPanel songsPanel;
    private JPanel namePanel;
    private JPanel optionPanel;

    private JSplitPane splitPane;

    private JButton enter1; //for first enter
    private JButton enter2; //for second button
    private JButton playlistButton;

    private JLabel playlistName;
    private JTextField textField;
    private JScrollPane scrollPane;
    private JCheckBox[] checkBoxes;

    private Database database;
    private Playlist playlist;
    private List<Song> songs;
    private int currentPlaylist;

    /**
     * PlaylistFrame
     * constructor for class
     * @param database Database object
     * @param optionPanel the optionPanel on the main frame
     * @param width the width of the frame
     * @param height the height of the frame
     */
    PlaylistFrame(Database database, JPanel optionPanel, double width, double height) {

        this.optionPanel = optionPanel;
        this.database = database; //to get all the data from the class for song information
        this.currentPlaylist = -1;

        this.screenWidth = width;
        this.screenHeight = height;

        frame = new JFrame("New Playlist");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(new Dimension((int) screenWidth / 3, (int) screenHeight / 7));
        frame.setLocationRelativeTo(null);

        //Font
        Font font = new Font("Dialog", Font.BOLD, 15);

        //Panel
        panel = new JPanel();
        panel.setBackground(new Color (30, 30, 30));

        //Text field
        textField = new JTextField(20);
        textField.setFont(font);
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.BLACK);
        TextPrompt tp = new TextPrompt("Playlist Name", textField);
        tp.changeAlpha(0.5f);
        addListener(textField);

        panel.add(textField);

        //Button
        enter1 = new JButton("Create Playlist");
        enter1.setFont(font);
        enter1.setBackground(new Color(0, 160, 42));
        enter1.setForeground(Color.WHITE);
        enter1.setBounds(textField.getX()+20, textField.getY()+textField.getHeight()+10, textField.getWidth()-40, textField.getHeight()-10);
        addListener(enter1);

        panel.add(enter1);

        frame.add(panel);

        frame.setVisible(true);

    }

    /**
     * PlaylistFrame
     * constructor for class
     * @param database Database object
     * @param optionPanel the optionPanel on the main frame
     * @param width the width of the frame
     * @param height the height of the frame
     * @param currentPlaylist the index of the current playlist
     */
    PlaylistFrame(Database database, JPanel optionPanel, double width, double height, int currentPlaylist) {

        this.optionPanel = optionPanel;
        this.database = database; //to get all the data from the class for song information
        this.currentPlaylist = currentPlaylist;

        this.screenWidth = width;
        this.screenHeight = height;

        frame = new JFrame("New Playlist");

        selectSongs(database.getPlaylist(currentPlaylist).getName());
    }

    /**
     * newPLaylist
     * adds a button to the optionPanel after creating a new playlist
     * @param playlist the playlist to add
     */
    public void newPlaylist(Playlist playlist) {
        playlistButton = new JButton(playlist.getName());
        playlistButton.setBackground(new Color(0,0,0,0));
        playlistButton.setForeground(Color.LIGHT_GRAY);
        playlistButton.setFont(new Font("Dialog", Font.PLAIN, 14));
        playlistButton.setBorder(new EmptyBorder(0, (int) screenWidth / 6 / 5, 0, 0));
        optionPanel.add(Box.createVerticalStrut(15));
        optionPanel.add(playlistButton);
    }

    /**
     * addSongs
     * adds the songs selected to an arraylist which then creates a playlist
     * @param checkBoxes JCheckBox array
     * @param name String
     */
    private void addSongs(JCheckBox[] checkBoxes, String name){
        songs = new ArrayList<>();
        for (int i = 0; i < checkBoxes.length; i++){
            if(checkBoxes[i].isSelected()) {
                songs.add(database.getSong(i));
            }
        }
        if (currentPlaylist == -1) {
            playlist = new Playlist(name, songs);
            database.addPlaylist(playlist);
            newPlaylist(playlist);
        } else {
            for (int i = 0; i < songs.size(); i++) {
                database.getPlaylist(currentPlaylist).addSong(songs.get(i));
            }
        }
        frame.dispose();
    }

    /**
     * addSongs
     * adds the songs selected to an arraylist which then creates a playlist
     * @param checkBoxes JCheckBox array
     * @param checkBoxes JCheckBox array
     */
    private void addSongs(JCheckBox[] checkBoxes){
        songs = new ArrayList<>();
        for (int i = 0; i < checkBoxes.length; i++){
            if(checkBoxes[i].isSelected()) {
                songs.add(database.getSong(i));
            }
        }
        for (int i = 0; i < songs.size(); i++) {
            database.getPlaylist(currentPlaylist).addSong(songs.get(i));
        }
        frame.dispose();
    }

    /**
     * selectSongs
     * selecting the songs to add to the playlist
     * @param name playlist name
     */
    private void selectSongs(String name) {
        if (currentPlaylist == -1) {
            panel.setVisible(false);
            panel = null;
        }
        frame.setVisible(false);
        frame.setSize(new Dimension((int) screenWidth / 3, (int) screenHeight / 2));
        frame.setLocationRelativeTo(null);

        //Font
        Font font = new Font("Dialog", Font.BOLD, 20);

        //Split Pane
        splitPane = new JSplitPane();

        //Top Panel
        namePanel = new JPanel();
        //Bottom Panel
        songsPanel = new JPanel();

        scrollPane = new JScrollPane();

        //setting orientation of split pane
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(frame.getHeight() / 7); //since there should be more space for the songsPanel
        splitPane.setTopComponent(namePanel);
//        splitPane.getTopComponent().setBounds(0,0,frame.getWidth(),(frame.getHeight()/6)-20);
        splitPane.setBottomComponent(scrollPane);
        splitPane.setEnabled(false);

        //Name Panel
        namePanel.setLayout(new GridLayout(0,2));
        namePanel.setBackground(new Color (30, 30, 30));
        if (currentPlaylist == -1) {
            playlistName = new JLabel(" " + name);
        } else {
            playlistName = new JLabel(" " + database.getPlaylist(currentPlaylist).getName());
        }
        playlistName.setForeground(Color.WHITE);
        playlistName.setFont(font);

        //Enter Button
        enter2 = new JButton("Add to Playlist");
        enter2.setFont(font);
        enter2.setBackground(new Color(0, 160, 42));
        enter2.setForeground(Color.WHITE);
        addListener(enter2);

        namePanel.add(playlistName);
        namePanel.add(enter2);

        //Songs Panel
        songsPanel.setLayout(new BoxLayout(songsPanel, BoxLayout.Y_AXIS));
        songsPanel.setBackground(new Color (30, 30, 30));

        checkBoxes = new JCheckBox[database.getNumSongs()];

        for (int i = 0; i < database.getNumSongs(); i++){
            songsPanel.add(Box.createVerticalStrut(10));
            checkBoxes[i] = new JCheckBox(database.getSongs().get(i).getName());
            checkBoxes[i].setForeground(Color.WHITE);
            songsPanel.add(checkBoxes[i]);
            songsPanel.add(Box.createVerticalStrut(10));
        }
        scrollPane.setViewportView(songsPanel);
        frame.add(splitPane);
        frame.setTitle(name);
        frame.setVisible(true);
    }

    /**
     * addListener
     * adds a listener to a JButton
     * @param button
     */
    private void addListener(JButton button) {
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getSource() == button) {
                    button.setBackground(new Color(0, 115, 42));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getSource() == button) {
                    button.setBackground(new Color(0, 160, 42));
                    if(e.getSource() == enter1){
                        selectSongs(textField.getText());
                    }
                    else if (e.getSource() == enter2) {
                        if (currentPlaylist == -1) {
                            addSongs(checkBoxes, textField.getText());
                        } else {
                            addSongs(checkBoxes);
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    /**
     * addListener
     * adds a listener to JTextField
     * @param textField JTextField
     */
    private void addListener(JTextField textField) {
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    selectSongs(textField.getText());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }
}
