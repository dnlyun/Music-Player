/**
 * [SongPanel.java]
 * Displays playlist name, songs (name, length, date added), and allows user to play selected songs
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.BorderFactory.createEmptyBorder;

public class SongPanel extends JPanel {

    private Database database;
    private List<Song> currentList;
    private Song song;
    private boolean changeSong;
    private boolean isPlaying;
    private int rowPlaying;
    private String searched;
    private int selectedRow;

    private JPopupMenu popupMenu;
    private JMenuItem deleteItem;
    private List<JMenuItem> menuItem;
    private JLabel title;
    private String titleName;
    private JTable table;
    private DefaultTableModel model;

    SongPanel(Database d, List<Song> cl, double width, double height, SearchBar searchBar) {
        this.database = d;
        this.currentList = cl;
        isPlaying = false;
        selectedRow = 0;

        this.setBackground(new Color(15, 15, 15));
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension((int) (width / 6 * 5), (int) height));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEADING));
        header.setOpaque(false);
        header.setPreferredSize(new Dimension((int) (width / 6 * 5), (int) (height / 5)));
        titleName = "Your Songs";
        title = new JLabel(titleName);
        title.setFont(new Font("Dialog", Font.PLAIN, 100));
        title.setForeground(Color.WHITE);
        title.setOpaque(false);
        header.add(title);

        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        String[] columnNames = {"Song Name", "Length", "Times Played", "Date Added"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table,
                        value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? new Color(15, 15, 15) : new Color(30, 30, 30));

                if (isPlaying) {
                    if (row == rowPlaying) {
                        c.setForeground(Color.GREEN);
                    } else {
                        c.setForeground(Color.WHITE);
                    }
                }

                return c;
            };
        });
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    if (searched.equals("")) {
                        int row = table.rowAtPoint(me.getPoint());
                        changeSong = true;
                        isPlaying = true;
                        rowPlaying = row;
                        song = currentList.get(row);
                        selectedRow = row;
                    } else {
                        int row = table.rowAtPoint(me.getPoint());
                        changeSong = true;
                        isPlaying = true;
                        for (int i = 0; i < currentList.size(); i++) {
                            Song tempSong = currentList.get(i);
                            if (tempSong.getName().equals(model.getValueAt(row, 0))) {
                                rowPlaying = i;
                            }
                        }
                        song = currentList.get(rowPlaying);
                    }
                } else if (me.getClickCount() == 1 && table.getSelectedRow() != -1) {
                    selectedRow = table.rowAtPoint(me.getPoint());
                    try {
                        table.setRowSelectionInterval(selectedRow, selectedRow);
                    } catch (Exception e) {}
                }
                table.repaint();
            }
        });
        popupMenu = new JPopupMenu();
        menuItem = new ArrayList<>();
        for (int i = 0; i < database.getNumPlaylists(); i++) {
            JMenuItem mi = new JMenuItem(database.getPlaylist(i).getName());
            mi.setFont(new Font("Dialog", Font.PLAIN, 15));
            mi.setForeground(new Color(31, 115, 40));
            mi.addActionListener(new MenuListener());
            popupMenu.add(mi);
            menuItem.add(mi);
        }
        popupMenu.addSeparator();
        deleteItem = new JMenuItem("Delete Song from Library");
        deleteItem.setFont(new Font("Dialog", Font.PLAIN, 15));
        deleteItem.setForeground(new Color(145, 0, 0));
        deleteItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row > -1) {
                    if (row < rowPlaying) {
                        rowPlaying--;
                    } else if (row == rowPlaying) {
                        rowPlaying = -1;
                    }
                    song = currentList.get(row);
                    currentList.remove(song);
                    if (currentList.equals(database.getSongs())) {
                        for (int i = 0; i < database.getNumPlaylists(); i++) {
                            for (int j = 0; j < database.getPlaylist(i).getSongs().size(); j++) {
                                if (database.getPlaylist(i).getSongs().get(j).equals(song)) {
                                    database.getPlaylist(i).getSongs().remove(song);
                                }
                            }
                        }
                    }
                    try {
                        song = currentList.get(rowPlaying);
                    } catch (Exception ex) {}
                }
                updateTb();
            }
        });
        popupMenu.add(deleteItem);
        table.setComponentPopupMenu(popupMenu);

        table.setSelectionModel(new ForceListSelectionModel());
        table.setFont(new Font("Dialog", Font.PLAIN, 20));
        table.setForeground(Color.WHITE);
        table.setRowHeight(table.getRowHeight() + 15);
        table.setBackground(new Color(15, 15, 15));
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setFont(new Font("Dialog", Font.PLAIN, 20));
        updateTb();
        table.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
        TableColumn columnA = table.getColumn("Song Name");
        columnA.setMinWidth((int) (width / 6 * 5 / 7 * 4));
        columnA.setMaxWidth((int) (width / 6 * 5 / 7 * 4));
        TableColumn columnC = table.getColumn("Length");
        columnC.setMinWidth((int) (width / 6 * 5 / 7));
        columnC.setMaxWidth((int) (width / 6 * 5 / 7));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        table.setFillsViewportHeight(true);
        listPanel.add(scrollPane);
//        listPanel.setBorder(new EmptyBorder(0, 50, 0, 50));

        this.add(header, BorderLayout.PAGE_START);
        this.add(listPanel, BorderLayout.CENTER);

        changeSong = false;

        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                searched = searchBar.getSearched();
                if (!searched.equals("")) {
                    updateTb(searched);
                } else {
                    updateTb();
                }
                if (menuItem.size() != database.getNumPlaylists()) {
                    menuItem.clear();
                    popupMenu.removeAll();
                    for (int i = 0; i < database.getNumPlaylists(); i++) {
                        JMenuItem mi = new JMenuItem(database.getPlaylist(i).getName());
                        mi.setFont(new Font("Dialog", Font.PLAIN, 15));
                        mi.setForeground(new Color(31, 115, 40));
                        mi.addActionListener(new MenuListener());
                        popupMenu.add(mi);
                        menuItem.add(mi);
                    }
                    popupMenu.addSeparator();
                    popupMenu.add(deleteItem);
                }
            }
        };
        Timer timer = new Timer(1000, taskPerformer);
        timer.start();
    }

    /**
     * updateTb
     * updates the table with search screen
     * @param searched the song being searched
     */
    public void updateTb(String searched) {
        if (currentList != null) {
            model.setRowCount(0);
            for (int i = 0; i < currentList.size(); i++) {
                Song tempSong = currentList.get(i);
                if (containsStr(tempSong.getName().toLowerCase(), searched.toLowerCase())) {
                    String time = secToString((int) tempSong.getLength());
                    int times = tempSong.getCounter();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedDate = df.format(tempSong.getDate());
                    Object[] data = {tempSong.getName(), time, times, formattedDate};
                    model.addRow(data);
                }
            }
            if (song != null) {
                for (int i = 0; i < model.getRowCount(); i++) {
                    String o = (String) model.getValueAt(i, 0);
                    if ((o).equals(song.getName())) {
                        rowPlaying = i;
                    }
                }
            }
            if (model.getRowCount() > 0) {
                table.setRowSelectionInterval(0, 0);
            }
            table.repaint();
        } else {
            model.setRowCount(0);
            table.repaint();
        }
    }

    /**
     * updateTb
     * updates the table
     */
    public void updateTb() {
        if (currentList != null) {
            model.setRowCount(0);
            for (int i = 0; i < currentList.size(); i++) {
                Song tempSong = currentList.get(i);
                String time = secToString((int) tempSong.getLength());
                int times = tempSong.getCounter();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = df.format(tempSong.getDate());
                Object[] data = {tempSong.getName(), time, times, formattedDate};
                model.addRow(data);
            }
            if (song != null) {
                rowPlaying = currentList.indexOf(song);
            }
            if (model.getRowCount() != 0) {
                if (model.getRowCount() >= selectedRow) {
                    try {
                        table.setRowSelectionInterval(selectedRow, selectedRow);
                    } catch (Exception e) {
                    }
                }
            }
            table.repaint();
        } else {
            model.setRowCount(0);
            table.repaint();
        }
    }

    /**
     * containsStr
     * Checks if the str2 is contained in str1
     * @param str1 string 1
     * @param str2 string 2
     * @return true or false
     */
    public boolean containsStr(String str1, String str2) {
        if (str2 == null || str2.length() == 0) {
            return true;
        } else {
            if (str1.length() == 0) {
                return false;
            } else if (str1.charAt(0) == str2.charAt(0)) {
                return containsStr(str1.substring(1), str2.substring(1));
            } else if (str1.charAt(0) != str2.charAt(0)) {
                return containsStr(str1.substring(1), str2);
            }
        }
        return false;
    }

    /**
     * updatePlay
     * updates the current playing song
     * @param i index of the song
     */
    public void updatePlay(int i) {
        rowPlaying += i;
        song = currentList.get(rowPlaying);
        updateTb();
    }

    /**
     * secToString
     * converts seconds to a string
     * @param s the int in seconds
     * @return the string of seconds for a song
     */
    public String secToString(int s) {
        if (s < 60) {
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
     * setTitle
     * sets the title of a song
     * @param name the song name
     */
    public void setTitle(String name) {
        try {
            this.titleName = name;
            title.setText(titleName);
        } catch (Exception e) {
            title.setText("No song selected");
        }
    }

    /**
     * setCurrentList
     * sets the current list of songs to be displayed
     * @param songs the songs in the list
     */
    public void setCurrentList(List<Song> songs) {
        if (songs == null) {
            deleteItem.setText("");
        } else if (songs.equals(database.getSongs())) {
            deleteItem.setText("Delete Song from Library");
        } else {
            deleteItem.setText("Delete Song from Playlist");
        }
        this.currentList = songs;
    }

    /**
     * getSong
     * gets the song being played
     * @return the soneing played
     */
    public Song getSong() {
        return song;
    }

    /**
     * getCheck
     * checks if song should be changed
     * @return changeSong true or false
     */
    public boolean getCheck() {
        return changeSong;
    }

    /**
     * setCheck
     * sets the change of song to true or false
     * @param b boolean true or false
     */
    public void setCheck(boolean b) {
        changeSong = b;
    }

    /**
     * getRowPlaying
     * gets the row being used to play the song
     * @return the row number
     */
    public int getRowPlaying() {
        return rowPlaying;
    }

    /**
     * setRowPlaying
     * sets the row number being played
     * @param row an int for the row number
     */
    public void setRowPlaying(int row) {
        isPlaying = true;
        rowPlaying = row;
        song = currentList.get(row);
        table.repaint();
    }

    /**
     * MenuListener
     * Listens for action taken on the table
     */
    class MenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < database.getNumPlaylists(); i++) {
                if (e.getSource() == menuItem.get(i)) {
                    int row = table.getSelectedRow();
                    if (row > -1) {
                        database.getPlaylist(i).addSong(database.getSong(row));
                    }
                }
            }
        }
    }

    /**
     * ForceListSelectionModel
     * Forces the table to be able to select one row at a time
     */
    class ForceListSelectionModel extends DefaultListSelectionModel {

        public ForceListSelectionModel () {
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

        @Override
        public void clearSelection() {}

        @Override
        public void removeSelectionInterval(int index0, int index1) {}
    }
}
