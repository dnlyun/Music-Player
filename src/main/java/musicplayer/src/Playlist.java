/**
 * [Playlist.java]
 * Has a list of songs stored in a playlist
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

import java.util.ArrayList;
import java.util.List;

public class Playlist implements QuickSort<Song> {

    private String name;
    private List<Song> songs;

    /**
     * Playlist
     * default constructor for class
     */
    Playlist() {}

    /**
     * Playlist
     * constructor for class
     * @param name name of playlist
     * @param songs List of songs
     */
    Playlist(String name, List<Song> songs) {
        if (name.equals("")) {
            this.setName("No name");
        } else {
            this.setName(name);
        }
        setSongs(songs);
        if (songs.size() > 0) {
            setSongs(quickSort(this.songs, 0, this.songs.size() - 1));
        }
    }

    /**
     * addSong
     * adds a song to the playlist
     * @param song
     */
    public void addSong(Song song) {
        if (getSongs() == null) {
            setSongs(new ArrayList<>());
        }
        getSongs().add(song);
        setSongs(quickSort(this.songs, 0, this.songs.size() - 1));
    }

    /**
     * getSongs
     * gets the list of songs from the playlist
     * @return songs List of Song objects
     */
    public List<Song> getSongs() {
        return this.songs;
    }

    /**
     * setSongs
     * sets the songs for playlist
     * @param songs List of Song objects
     */
    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    /**
     * getName
     * gets the name of the playlist
     * @return name playlist name
     */
    public String getName() {
        return this.name;
    }

    /**
     * setName
     * sets the name of the playlist
     * @param name String object for the name of the playlist
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<Song> quickSort(List<Song> songs, int start, int end) {
        int mid = (start + end) / 2;
        int left = start;
        int right = end;
        while (left <= right) {
            while (songs.get(left).compareTo(songs.get(right)) < 0) {
                left++;
            }
            while (songs.get(right).compareTo(songs.get(mid)) > 0) {
                right--;
            }
            if (left <= right) {
                Song temp = songs.get(left);
                songs.set(left, songs.get(right));
                songs.set(right, temp);
                left++;
                right--;
            }
        }
        if (start < right) {
            quickSort(songs, start, right);
        }
        if (left < end) {
            quickSort(songs, left, end);
        }
        return songs;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "name='" + name + '\'' +
                ", songs=" + songs +
                '}';
    }
}
