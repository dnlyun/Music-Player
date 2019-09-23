/**
 * [Display.java]
 * The main frame that contains all the swing components which the user will interact with
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Display {

    private JFrame frame;
    private double screenWidth;
    private double screenHeight;

    private JPanel optionPanel;

    private ImageIcon playIcon;
    private ImageIcon pauseIcon;

    private Font subFont;
    private Font mainFont;
    private EmptyBorder subBorder;
    private EmptyBorder mainBorder;

    private JLabel browse;
    private JLabel yourMusic;
    private JLabel yourPlaylist;
    private JButton store;
    private JButton localSongs;
    private JButton showSongs;
    private JButton showArtists;
    private JButton newPlaylist;
    private List<JButton> playlists;

    private FileDialog dialog;
    private PlaylistFrame playlistFrame;

    private ControlButton playPause;
    private boolean isPLaying;
    private boolean timeChanged;
    private int loopLevel; //0 - no loop, 1 - loop song, 2 - loop playlist
    private boolean isShuffle;
    private StackedLinkedList<Song> played;
    private StackedLinkedList<Song> toPlay;

    private JSlider scrubber;
    private int sliderValue;

    private JButton favourite;
    private JButton playlistAddSongs;
    private JButton deletePlaylist;
    private ControlButton forSkip;
    private ControlButton backSkip;
    private ControlButton shuffle;
    private ControlButton loop;
    private JLabel lowerVolume;
    private JLabel raiseVolume;
    private JSlider volumeSlider;

    private SearchBar searchBar;

    private VolumeChanger vc;

    private MarqueePanel marqueePanel;

    private SongPanel songPanel;
    private StorePanel storePanel;

    private Song song;
    private JLibraryPlayer player;
    private Thread t;
    private ActionListener taskPerformer;
    private Timer timer;
    private boolean start;
    private int songIndex;
    private int currentPlaylist;

    private String name;
    private double length;

    private Database database;
    private List<Song> currentList;

    /**
     * Display
     * constructor for class
     * @param d Database object
     */
    Display(Database d) {
        this.database = d;
        currentList = database.getSongs();

        frame = new JFrame("Spotify");
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setBackground(new Color(15, 15, 15));
        frame.setIconImage(new ImageIcon("src/res/Icon.png").getImage());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenHeight = screenSize.getHeight();
        screenWidth = screenSize.getWidth();

        optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
        optionPanel.setBackground(new Color (30, 30, 30));
        optionPanel.setPreferredSize(new Dimension((int) screenWidth / 6, (int) screenHeight));

        subFont = new Font("Dialog", Font.BOLD, 16);
        mainFont = new Font("Dialog", Font.PLAIN, 14);
        subBorder = new EmptyBorder(0,(int) screenWidth / 6 / 15,0,0);
        mainBorder = new EmptyBorder(0, (int) screenWidth / 6 / 5, 0, 0);

        browse = new JLabel("BROWSE");
        browse.setForeground(Color.WHITE);
        browse.setFont(subFont);
        browse.setBorder(subBorder);

        store = new JButton("Store", formatIcon(new ImageIcon("src/res/Store.png"), 15, 15));
        store.setForeground(Color.LIGHT_GRAY);
        store.setFont(mainFont);
        store.setBorder(mainBorder);
        addListener(store);

        localSongs = new JButton("Local Songs", formatIcon(new ImageIcon("src/res/Local.png"), 14, 14));
        localSongs.setForeground(Color.LIGHT_GRAY);
        localSongs.setFont(mainFont);
        localSongs.setBorder(mainBorder);
        addListener(localSongs);

        yourMusic = new JLabel("YOUR MUSIC");
        yourMusic.setForeground(Color.WHITE);
        yourMusic.setFont(subFont);
        yourMusic.setBorder(subBorder);

        showSongs = new JButton("Songs", formatIcon(new ImageIcon("src/res/Song.png"), 15, 15));
        showSongs.setForeground(Color.LIGHT_GRAY);
        showSongs.setFont(mainFont);
        showSongs.setBorder(mainBorder);
        addListener(showSongs);

        showArtists = new JButton("Artists", formatIcon(new ImageIcon("src/res/Artist.png"), 12, 20));
        showArtists.setForeground(Color.LIGHT_GRAY);
        showArtists.setFont(mainFont);
        showArtists.setBorder(mainBorder);
        addListener(showArtists);

        yourPlaylist = new JLabel("PLAYLIST");
        yourPlaylist.setForeground(Color.WHITE);
        yourPlaylist.setFont(subFont);
        yourPlaylist.setBorder(subBorder);

        newPlaylist = new JButton("New Playlist", formatIcon(new ImageIcon("src/res/Add.png"), 13, 13));
        newPlaylist.setForeground(Color.LIGHT_GRAY);
        newPlaylist.setFont(mainFont);
        newPlaylist.setBorder(mainBorder);
        addListener(newPlaylist);

        optionPanel.add(Box.createVerticalStrut(10));
        optionPanel.add(browse);
        optionPanel.add(Box.createVerticalStrut(5));
        optionPanel.add(store);
        optionPanel.add(Box.createVerticalStrut(5));
        optionPanel.add(localSongs);
        optionPanel.add(Box.createVerticalStrut(40));
        optionPanel.add(yourMusic);
        optionPanel.add(Box.createVerticalStrut(5));
        optionPanel.add(showSongs);
        optionPanel.add(Box.createVerticalStrut(5));
        optionPanel.add(showArtists);
        optionPanel.add(Box.createVerticalStrut(40));
        optionPanel.add(yourPlaylist);
        optionPanel.add(Box.createVerticalStrut(5));
        optionPanel.add(newPlaylist);

        favourite = new JButton("Favourites");
        favourite.setForeground(Color.LIGHT_GRAY);
        favourite.setFont(mainFont);
        favourite.setBorder(mainBorder);
        addListener1(favourite);
        optionPanel.add(Box.createVerticalStrut(7));
        optionPanel.add(favourite);

        playlists = new ArrayList<>();
        for (int i = 0; i < database.getNumPlaylists(); i++) {
            if (!database.getPlaylist(i).getName().equals("Favourites")) {
                JButton button = new JButton(database.getPlaylist(i).getName());
                button.setForeground(Color.LIGHT_GRAY);
                button.setFont(mainFont);
                button.setBorder(mainBorder);
                addListener1(button);
                optionPanel.add(Box.createVerticalStrut(7));
                optionPanel.add(button);
                playlists.add(button);
            }
        }

        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        //searchPanel.add(Box.createHorizontalStrut((int) screenWidth / 15));
        searchPanel.setBackground(new Color(30, 30, 30));
        searchPanel.setBorder(new LineBorder(new Color (0, 0, 0)));
        searchBar = new SearchBar();

        JPanel currentSong = new JPanel();
        currentSong.setOpaque(false);
        JLabel currentlyPlaying = new JLabel("Currently Playing: ");
        currentlyPlaying.setForeground(Color.WHITE);
        currentlyPlaying.setFont(new Font("Dialog", Font.BOLD + Font.ITALIC, 20));
        currentlyPlaying.setOpaque(false);
        marqueePanel = new MarqueePanel(name);
        currentSong.add(currentlyPlaying);
        currentSong.add(marqueePanel);
        marqueePanel.start();
        playlistAddSongs = new JButton("Add Songs", formatIcon(new ImageIcon("src/res/AddSong.png"), 17, 17));
        playlistAddSongs.setFont(new Font("Dialog", Font.PLAIN, 15));
        addListener1(playlistAddSongs);
        deletePlaylist = new JButton("Delete Playlist", formatIcon(new ImageIcon("src/res/RedX.png"), 17, 17));
        deletePlaylist.setFont(new Font("Dialog", Font.PLAIN, 15));
        addListener1(deletePlaylist);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        searchPanel.add(currentSong, gbc);
        searchPanel.add(playlistAddSongs, gbc);
        searchPanel.add(deletePlaylist, gbc);
        gbc.anchor = GridBagConstraints.LINE_END;
        searchPanel.add(searchBar, gbc);
        deletePlaylist.setVisible(false);
        playlistAddSongs.setVisible(false);

        JPanel playPanel = new JPanel();
        playPanel.setBackground(new Color(30, 30, 30));
        playPanel.setBorder(new LineBorder(new Color (0, 0, 0)));
        playPanel.setPreferredSize(new Dimension((int) screenWidth, 80));
        playPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

        try {
            for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(laf.getName())) {
                    UIManager.put("nimbusBlueGrey", Color.LIGHT_GRAY);
                    UIManager.put("nimbusBase", Color.WHITE);
                    UIManager.setLookAndFeel(laf.getClassName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        scrubber = new MusicScrubber().getSlider((int) screenWidth);
        addListener(scrubber);
        sliderValue = 0;

        playIcon = new ImageIcon("src/res/Controls/play.png");
        pauseIcon = new ImageIcon("src/res/Controls/pause.png");

        playPause = new ControlButton(formatIcon(pauseIcon, 30, 30));
        addListener(playPause);
        isPLaying = true;
        timeChanged = false;
        forSkip = new ControlButton(formatIcon(new ImageIcon("src/res/Controls/forwards.png"), 30, 30));
        addListener(forSkip);
        backSkip = new ControlButton(formatIcon(new ImageIcon("src/res/Controls/previous.png"), 30, 30));
        addListener(backSkip);
        loop = new ControlButton(formatIcon(new ImageIcon("src/res/Controls/loop.png"), 30, 30));
        addListener(loop);
        shuffle = new ControlButton(formatIcon(new ImageIcon("src/res/Controls/shuffle.png"), 30, 30));
        addListener(shuffle);
        lowerVolume = new JLabel(formatIcon(new ImageIcon("src/res/Controls/low-volume.png"), 20, 20));
        raiseVolume = new JLabel(formatIcon(new ImageIcon("src/res/Controls/volume-adjustment.png"), 20, 20));
        volumeSlider = new MusicScrubber().getSlider((int) screenWidth / 11);
        addListener(volumeSlider);

        loopLevel = 0;
        isShuffle = false;

        vc = new VolumeChanger();
        vc.setGain(0.5f); //0-1
        volumeSlider.setValue(50);

        JLabel albumArt = new JLabel(formatIcon(new ImageIcon("src/res/Disk.png"), 65, 65));
        albumArt.setPreferredSize(new Dimension(70, 70));
        playPanel.add(albumArt);
        playPanel.add(Box.createHorizontalStrut(5));

        playPanel.add(backSkip);
        playPanel.add(playPause);
        playPanel.add(forSkip);

        playPanel.add(Box.createHorizontalStrut(5));
        JLabel currentTime = new JLabel("0:00");
        currentTime.setForeground(Color.WHITE);
        playPanel.add(currentTime);
        playPanel.add(scrubber);
        JLabel timeLeft = new JLabel("0:00");
        timeLeft.setForeground(Color.WHITE);
        playPanel.add(timeLeft);
        playPanel.add(Box.createHorizontalStrut(5));

        playPanel.add(loop);
        playPanel.add(shuffle);
        playPanel.add(Box.createHorizontalStrut(30));
        playPanel.add(lowerVolume);
        playPanel.add(volumeSlider);
        playPanel.add(raiseVolume);

        songPanel = new SongPanel(this.database, currentList, screenWidth, screenHeight, searchBar);

        storePanel = new StorePanel(frame);

        dialog = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
        dialog.setMultipleMode(true);

        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(songPanel, BorderLayout.EAST);
        frame.add(playPanel, BorderLayout.SOUTH);
        frame.add(optionPanel, BorderLayout.WEST);
        frame.setVisible(true);

        start = false;
        scrubber.setValue(0);
        taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (start && isPLaying) {
                    scrubber.setValue((int) (sliderValue * 100 / length));
                    sliderValue++;
                    currentTime.setText(secToString(sliderValue));
                    timeLeft.setText(secToString((int) (length - sliderValue)));
                }
                if (songPanel.getCheck()) {
                    if (start) {
                        t.resume();
                    }
                    if (!isPLaying) {
                        isPLaying = true;
                        playPause.setIcon(formatIcon(pauseIcon, 30, 30));
                    }
                    if (isShuffle) {
                        isShuffle = false;
                        shuffle.setBackground(new Color(0, 0, 0, 0f));
                    }
                    song = songPanel.getSong();
                    song.setCounter(song.getCounter() + 1);
                    songIndex = songPanel.getRowPlaying();
                    playNewSong();
                } else if (isPLaying && scrubber.getValue() == 100) {
                    if (isShuffle) {
                        if (loopLevel == 1) {
                            play(0);
                        } else if (toPlay.size() > 0) {
                            played.push(song);
                            song = toPlay.pop();
                            playNew(song);
                            setRow(song);
                        } else {
                            if (loopLevel == 2) {
                                played.push(song);
                                while (played.size() > 0) {
                                    Song temp = played.pop();
                                    toPlay.push(temp);
                                }
                                song = toPlay.pop();
                                playNew(song);
                                setRow(song);
                            } else {
                                t.suspend();
                                player.stop();
                                isPLaying = false;
                                timeLeft.setText("0:00");
                            }
                        }
                    } else if (songIndex < currentList.size() - 1) {
                        if (loopLevel == 1) {
                            play(0);
                        } else {
                            songIndex++;
                            song = currentList.get(songIndex);
                            playNewSong();
                            songPanel.updatePlay(1);
                        }
                    } else if (songIndex == currentList.size() - 1) {
                        if (loopLevel == 1) {
                            play(0);
                        } else if (loopLevel == 2) {
                            playNew(currentList.get(0));
                            songPanel.setRowPlaying(0);
                        } else if (loopLevel == 0) {
                            t.suspend();
                            player.stop();
                            isPLaying = false;
                            timeLeft.setText("0:00");
                        }
                    }
                }
                if (playlists.size() != database.getNumPlaylists()) {
                    optionPanel.removeAll();
                    optionPanel.add(Box.createVerticalStrut(10));
                    optionPanel.add(browse);
                    optionPanel.add(Box.createVerticalStrut(5));
                    optionPanel.add(store);
                    optionPanel.add(Box.createVerticalStrut(5));
                    optionPanel.add(localSongs);
                    optionPanel.add(Box.createVerticalStrut(40));
                    optionPanel.add(yourMusic);
                    optionPanel.add(Box.createVerticalStrut(5));
                    optionPanel.add(showSongs);
                    optionPanel.add(Box.createVerticalStrut(5));
                    optionPanel.add(showArtists);
                    optionPanel.add(Box.createVerticalStrut(40));
                    optionPanel.add(yourPlaylist);
                    optionPanel.add(Box.createVerticalStrut(5));
                    optionPanel.add(newPlaylist);
                    optionPanel.add(Box.createVerticalStrut(7));
                    optionPanel.add(favourite);
                    playlists.clear();
                    for (int i = 0; i < database.getNumPlaylists(); i++) {
                        JButton button = new JButton(database.getPlaylist(i).getName());
                        button.setBackground(new Color(0, 0, 0, 0));
                        button.setForeground(Color.LIGHT_GRAY);
                        button.setFont(mainFont);
                        button.setBorder(mainBorder);
                        addListener1(button);
                        optionPanel.add(Box.createVerticalStrut(7));
                        optionPanel.add(button);
                        playlists.add(button);
                    }
                    frame.repaint();
                }
            }
        };
        timer = new Timer(1000, taskPerformer);
        timer.start();

        marqueePanel.setPreferredSize(new Dimension(marqueePanel.getWidth(), marqueePanel.getHeight()));
    }

    /**
     * formatIcon
     * formats the images used in GUI
     * @param imageIcon the image used
     * @param w the width
     * @param h the height
     * @return the resized image to be used
     */
    public ImageIcon formatIcon(ImageIcon imageIcon, int w, int h) {
        Image image = imageIcon.getImage();
        Image newImage = image.getScaledInstance(w, h,  Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newImage);
        return imageIcon;
    }

    /**
     * playNewSong
     * Called on when it is the first time playing a song
     */
    public void playNewSong() {
        playNew(song);
        songPanel.setCheck(false);
        if (!start) {
            start = true;
        }
    }

    /**
     * play
     * Playing a song for the 2nd or more times once it has been played before
     * @param value - Time to play from
     */
    public void play(int value) {
        try {
            player.stop();
            value = (int) ((value / 100.0) * length);
            sliderValue = value;

            player = new JLibraryPlayer(value, song);
            t = new Thread(player);
            t.start();
        } catch (Exception ex) {}
    }

    /**
     * playNew
     * plays a new song from the beginning of it
     * @param song the song to play
     */
    public void playNew(Song song) {
        try {
            this.song = song;

            if (start) {
                player.stop();
            }
            sliderValue = 0;

            player = new JLibraryPlayer(song);
            t = new Thread(player);
            t.start();
            length = player.getLength();
            name = player.getName();
            marqueePanel.setName(name);
        } catch (Exception ex) {}
    }

    /**
     * secToString
     * turns seconds into a string to display
     * @param s the seconds
     * @return the string containing seconds
     */
    public String secToString(int s) {
        if (s < 0) {
            return "0:00";
        } else if (s < 60) {
            if (s < 10) {
                return ("0:0" + s);
            }
            return ("0:" + s);
        } else if (s < 3600) {
            int m = s / 60;
            int se = s % 60;

            String sec;
            if (se == 0) {
                sec = "00";
            } else if (se < 10) {
                sec = "0" + se;
            } else {
                sec = se + "";
            }
            return m + ":" + sec;
        }
        return "0:00";
    }

    /**
     * addListener
     * adds a listener to a JSlider
     * @param slider JSlider object
     */
    public void addListener(JSlider slider) {
        slider.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getSource() == scrubber) {
                    if (isPLaying) {
                        int value = scrubber.getValue();
                        play(value);
                    } else {
                        timeChanged = true;
                    }
                } else {
                    int value = volumeSlider.getValue();
                    vc.setGain(value / 100f);

                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });

        slider.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getSource() == scrubber) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        if (start) {
                            if (isPLaying) {
                                t.suspend();
                                isPLaying = false;
                                playPause.setIcon(formatIcon(playIcon, 30, 30));
                            } else {
                                t.resume();
                                isPLaying = true;
                                if (timeChanged) {
                                    int value = scrubber.getValue();
                                    play(value);
                                    timeChanged = false;
                                }
                                playPause.setIcon(formatIcon(pauseIcon, 30, 30));
                            }
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_UP) {
                        scrubber.setValue(scrubber.getValue() + 2);
                        if (isPLaying) {
                            int value = scrubber.getValue();
                            play(value);
                        } else {
                            timeChanged = true;
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_DOWN) {
                        scrubber.setValue(scrubber.getValue() - 2);
                        if (isPLaying) {
                            int value = scrubber.getValue();
                            play(value);
                        } else {
                            timeChanged = true;
                        }
                    }
                } else {
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_UP) {
                        vc.setGain(vc.getGain() + 0.1f); //0-1
                        volumeSlider.setValue(volumeSlider.getValue() + 10);
                    } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_DOWN) {
                        vc.setGain(vc.getGain() - 0.1f); //0-1
                        volumeSlider.setValue(volumeSlider.getValue() - 10);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }

    /**
     * addListener
     * adds a listener to a JButton
     * @param button JButton object
     */
    public void addListener(JButton button) {
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(new Color(0, 115, 42));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(new Color(0, 160, 42));
                if (e.getSource() == playPause) {
                    if (start) {
                        if (isPLaying) {
                            t.suspend();
                            isPLaying = false;
                            button.setIcon(formatIcon(playIcon, 30, 30));
                        } else {
                            t.resume();
                            isPLaying = true;
                            if (timeChanged) {
                                int value = scrubber.getValue();
                                play(value);
                                timeChanged = false;
                            }
                            button.setIcon(formatIcon(pauseIcon, 30, 30));
                        }
                    }
                } else if (e.getSource() == forSkip) {
                    if (start && isPLaying) {
                        if (!isShuffle) {
                            if (songIndex + 1 > currentList.size() - 1) {
                                songIndex = 0;
                                playNew(currentList.get(0));
                                songPanel.updatePlay(-1 * songPanel.getRowPlaying());
                            } else {
                                songIndex++;
                                playNew(currentList.get(songIndex));
                                songPanel.updatePlay(1);
                            }
                        } else {
                            if (toPlay.size() > 0) {
                                played.push(song);
                                song = toPlay.pop();
                                playNew(song);
                                setRow(song);
                            } else {
                                played.push(song);
                                while (played.size() > 0) {
                                    Song temp = played.pop();
                                    toPlay.push(temp);
                                }
                                song = toPlay.pop();
                                playNew(song);
                                setRow(song);
                            }
                        }
                    }
                } else if (e.getSource() == backSkip) {
                    if (start && isPLaying) {
                        if (!isShuffle) {
                            if (sliderValue > 6) {
                                play(0);
                            } else {
                                if (songIndex == 0) {
                                    songIndex = currentList.size() - 1;
                                    playNew(currentList.get(songIndex));
                                    songPanel.updatePlay(songIndex);
                                } else {
                                    songIndex--;
                                    playNew(currentList.get(songIndex));
                                    songPanel.updatePlay(-1);
                                }
                            }
                        } else {
                            if (sliderValue > 6) {
                                play(0);
                            } else {
                                if (played.size() > 0) {
                                    toPlay.push(song);
                                    song = played.pop();
                                    playNew(song);
                                    setRow(song);
                                } else {
                                    toPlay.push(song);
                                    while (toPlay.size() > 0) {
                                        Song temp = toPlay.pop();
                                        played.push(temp);
                                    }
                                    song = played.pop();
                                    playNew(song);
                                    setRow(song);
                                }
                            }
                        }
                    }
                } else if (e.getSource() == localSongs) {
                    dialog.setVisible(true);
                    File[] files = dialog.getFiles();
                    Song[] songs = new Song[files.length];
                    for (int i = 0; i < files.length; i++) {
                        songs[i] = new Song(files[i], new Date());
                        database.addSong(songs[i]);
                    }
                    songPanel.updateTb();
                } else if (e.getSource() == newPlaylist) {
                    playlistFrame = new PlaylistFrame(database, optionPanel, screenWidth, screenHeight);
                } else if (e.getSource() == showSongs) {
                    searchBar.setText("");
                    if (storePanel.getVisible()) {
                        storePanel.hideStorePanel();
                        songPanel.setVisible(true);
                        frame.add(songPanel);
                    }
                    currentList = database.getSongs();
                    songPanel.setTitle("Your Songs");
                    songPanel.setCurrentList(currentList);
                    songPanel.revalidate();
                    songPanel.updateTb();
                    deletePlaylist.setVisible(false);
                    playlistAddSongs.setVisible(false);
                    frame.revalidate();
                } else if (e.getSource() == loop) {
                    if (loopLevel == 0) {
                        loopLevel = 1;
                    } else if (loopLevel == 1){
                        loopLevel = 2;
                    } else {
                        loopLevel = 0;
                    }
                } else if (e.getSource() == shuffle) {
                    if (isShuffle) {
                        isShuffle = false;
                        played.clear();
                        toPlay.clear();
                    } else {
                        isShuffle = true;
                        createShuffled();
                        song = toPlay.pop();
                        setRow(song);
                        if (!start) {
                            playNewSong();
                        } else {
                            playNew(song);
                        }
                    }
                } else if (e.getSource() == store) {
                    searchBar.setText("");
                    deletePlaylist.setVisible(false);
                    playlistAddSongs.setVisible(false);
                    songPanel.setVisible(false);
                    frame.remove(songPanel);
                    storePanel.showStorePanel();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if(e.getSource() == button) {
                    button.setBackground(new Color(0, 160, 42));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(e.getSource() == button) {
                    if (e.getSource() == loop) {
                        if (loopLevel == 1) {
                            button.setBackground(new Color(0, 160, 42));
                        } else if (loopLevel == 2) {
                            button.setBackground(Color.BLUE);
                        } else {
                            button.setBackground(new Color(0, 0, 0, 0f));
                        }
                    } else if (e.getSource() == shuffle) {
                        if (isShuffle) {
                            button.setBackground(new Color(0, 160, 42));
                        } else {
                            button.setBackground(new Color(0, 0, 0, 0f));
                        }
                    } else {
                        button.setBackground(new Color(0, 0, 0, 0f));
                    }
                }
            }
        });
    }

    /**
     * addListener1
     * adds a(nother) listener to a JButton
     * @param button JButton object
     */
    public void addListener1(JButton button) {
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getSource() == deletePlaylist) {
                    searchBar.setText("");
                    database.removePlaylist(currentPlaylist);
                    currentList = database.getSongs();
                    songPanel.setTitle("Your Songs");
                    songPanel.setCurrentList(currentList);
                    songPanel.revalidate();
                    songPanel.updateTb();
                    deletePlaylist.setVisible(false);
                    playlistAddSongs.setVisible(false);
                } else if (e.getSource() == playlistAddSongs) {
                    playlistFrame = new PlaylistFrame(database, optionPanel, screenWidth, screenHeight, currentPlaylist);
                    currentList = database.getPlaylist(currentPlaylist).getSongs();
                    songPanel.setCurrentList(currentList);
                    songPanel.revalidate();
                    songPanel.updateTb();
                } else if (e.getSource() == favourite) {
                    List<Song> fav = new ArrayList<>();
                    for (int i = 0; i < database.getNumSongs(); i++) {
                        if (database.getSong(i).getCounter() > 4) {
                            fav.add(database.getSong(i));
                        }
                    }
                    currentList = fav;
                    songPanel.setTitle("Your Favourites");
                    songPanel.setCurrentList(currentList);
                    songPanel.revalidate();
                    songPanel.updateTb();
                } else {
                    searchBar.setText("");
                    if (storePanel.getVisible()) {
                        storePanel.hideStorePanel();
                        songPanel.setVisible(true);
                        frame.add(songPanel);
                    }
                    for (int i = 0; i < database.getNumPlaylists(); i++) {
                        if (e.getSource() == playlists.get(i)) {
                            currentPlaylist = i;
                            currentList = database.getPlaylist(i).getSongs();
                            songPanel.setTitle(database.getPlaylist(i).getName());
                            songPanel.setCurrentList(currentList);
                            songPanel.revalidate();
                            songPanel.updateTb();
                            deletePlaylist.setVisible(true);
                            playlistAddSongs.setVisible(true);
                        }
                    }
                    frame.revalidate();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    /**
     * createShiffled
     * Creates a shuffled stacked list
     */
    public void createShuffled() {
        played = new StackedLinkedList<>();
        toPlay = new StackedLinkedList<>();

        if (currentList == null) {
            currentList = database.getSongs();
            for (int i = 0; i < database.getNumSongs(); i++) {
                toPlay.push(database.getSong(i));
            }
        } else {
            for (int i = 0; i < currentList.size(); i++) {
                toPlay.push(currentList.get(i));
            }
        }
        toPlay.shuffleList();
    }

    /**
     * setRow
     * sets the song currently playing to show to the user (green box around song)
     * @param song Song object
     */
    public void setRow(Song song) {
        for (int i = 0; i < currentList.size(); i++) {
            if (song.equals(currentList.get(i))) {
                songPanel.setRowPlaying(i);
                songIndex = i;
            }
        }
    }
}
