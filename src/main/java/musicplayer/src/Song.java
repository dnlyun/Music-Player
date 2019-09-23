/**
 * [Song.java]
 * Song objects represents the songs in the database
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

public class Song implements Comparable<Song> {

    private File file;
    private String name;
    private double length;
    private float fps;
    private Date date;
    private int counter;

    /**
     * Song
     * constructor for class
     */
    Song() {}

    /**
     * Song
     * constructor for this class
     * @param file File object
     */
    Song(File file) {
        try {
            this.setFile(file);
            this.setName(formatName());
            FileInputStream s = new FileInputStream(file);
            this.setLength(getLength(s));
            this.setCounter(0);
        } catch (Exception e) {}
    }

    /**
     * Song
     * constructor for this class
     * @param file File object
     * @param date Data object
     */
    Song(File file, Date date) {
        try {
            this.setFile(file);
            this.setName(formatName());
            FileInputStream s = new FileInputStream(file);
            this.setLength(getLength(s));
            this.setDate(date);
            this.setCounter(0);
        } catch (Exception e) {}
    }

    /**
     * formatName
     * formats the song name for use in GUI
     * @return the song name without .mp3
     */
    private String formatName() {
        return (getFile().getName()).replaceFirst("[.][^.]+$", "");
    }

    /**
     * getLength
     * gets the length of the song in seconds
     * @param s file input sream
     * @return the length of the song in seconds
     */
    private double getLength(FileInputStream s) {
        try {
            Header header = new Bitstream(s).readFrame();
            setFps(1 / (header.ms_per_frame()) * 1000);
            long tn = getFile().length();
            setLength(header.total_ms((int) (tn * 0.965)) / 1000);
            return getLength();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * getFile
     * gets the music file
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * setFile
     * sets file music file
     * @param file the file of the music
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * getName
     * gets the name of the song
     * @return the song name
     */
    public String getName() {
        return name;
    }

    /**
     * setName
     * Sets the name of the song
     * @param name the name of the song
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getLength
     * returns the song length
     * @return length the length of the song
     */
    public double getLength() {
        return length;
    }

    /**
     * setLength
     * sets the length of the song
     * @param length the song length
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * getFps
     * gets the frames per second of the audio file
     * @return fps the frames per second of the song
     */
    public float getFps() {
        return fps;
    }

    /**
     * setFps
     * sets the fps of the song
     * @param fps the frames per second
     */
    public void setFps(float fps) {
        this.fps = fps;
    }

    /**
     * getDate
     * gets the date of file addition to library
     * @return date , the date of the song addition
     */
    public Date getDate() {
        return date;
    }

    /**
     * counter
     * counts the number of times a song is played
     */

    /**
     * setDate
     * sets the date of the song added to the library
     * @param date the date of the song added to library
     */
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Song{" +
                "file=" + file +
                ", name='" + name + '\'' +
                ", length=" + length +
                ", fps=" + fps +
                ", date=" + date +
                '}';
    }

    @Override
    public int compareTo(Song o) {
        return this.getName().compareTo(o.getName());
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
