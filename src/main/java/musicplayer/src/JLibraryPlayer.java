/**
 * [JLibraryPlayer.java]
 * Able to play and control songs and find the length, fps and name
 * Only used the JLibrary API to be able to play the actual MP3 file
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.*;

public class JLibraryPlayer implements Runnable {

    private volatile Song song;
    private volatile InputStream s;
    private volatile File file;
    private AdvancedPlayer p;
    private float fps;
    private double length;
    private int start;

    /**
     * JLibraryPlayer
     * constructor for class
     */
    JLibraryPlayer() {}

    /**
     * JLibraryPlayer
     * constructor for class
     * @param song Song object
     */
    JLibraryPlayer(Song song) {
        try {
            this.start = 0;
            this.song = song;
            this.file = song.getFile();
            s = new FileInputStream(file);
            length = song.getLength();
            fps = song.getFps();
        } catch (Exception e) {}
    }

    /**
     * JLibraryPlayer
     * constructor for class
     * @param start int for when the song should start
     * @param song Song object
     */
    JLibraryPlayer(int start, Song song) {
        try {
            this.start = start;
            this.song = song;
            this.file = song.getFile();
            s = new FileInputStream(file);
            length = song.getLength();
            fps = song.getFps();
        } catch (Exception e) {}
    }

    @Override
    public void run() {
            try {
                p = new AdvancedPlayer(s);
                p.play((int) (start * fps), (int) (length * fps * 60 * 1.1));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * stop
     * stops the song playing
     */
    public void stop() {
        p.close();
    }

    /**
     * getName
     * gets the name of the song playing
     * @return name String object
     */
    public String getName() {
        return song.getName();
    }

    /**
     * getLength
     * gets the length of the song
     * @return length double
     */
    public double getLength() {
        return length;
    }

    /**
     * getFile
     * gets the file of the song
     * @return file File object
     */
    public File getFile() {
        return file;
    }
}
