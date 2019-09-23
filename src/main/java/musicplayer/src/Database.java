/**
 * [Database.java]
 * Stores list of songs and playlists
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Database")
@XmlAccessorType(XmlAccessType.FIELD)
public class Database implements QuickSort<Song> {

    private List<Song> songs;
    private List<Playlist> playlists;

    /**
     * Database
     * constructor for Database object
     */
    Database() {
        setSongs(new ArrayList<>());
        setPlaylists(new ArrayList<>());
    }

    /**
     * getSongs
     * gets the songs from the database
     * @return songs - a list of Song objects
     */
    public List<Song> getSongs() {
        return songs;
    }

    /**
     * setSongs
     * sets the songs in the database
     * @param songs - a list of Song objects
     */
    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    /**
     * getPlaylists
     * gets the playlist list from the database
     * @return playlists - a list of playlists objects
     */
    public List<Playlist> getPlaylists() {
        return playlists;
    }

    /**
     * setPlaylists
     * sets the playlist in the database
     * @param playlists - a list of Song objects
     */
    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    /**
     * addSong
     * adds a song to the database
     * @param song - a Song object
     */
    public void addSong(Song song) {
        getSongs().add(song);
        setSongs(quickSort(this.songs, 0, this.songs.size() - 1));
    }

    /**
     * getSong
     * gets a specific song form the database
     * @param i - the specific song this method will return (is an index)
     * @return song - Song object
     */
    public Song getSong(int i) {
        return getSongs().get(i);
    }

//    public int getIndex(Song song) {
//        Collections.sort(this.songs);
//        return getSongs().indexOf(song);
//    }

    /**
     * getNumSongs
     * gets the number of songs in the database
     * @return int - the number of songs
     */
    public int getNumSongs() {
        return getSongs().size();
    }

    /**
     * removeSong
     * removes a specific song from the database
     * @param song - a Song object
     */
    public void removeSong(Song song) {
        getSongs().remove(song);
    }

    /**
     * removeSong
     * removes a song from the database
     * @param i - the specific song to remove (is an index)
     */
    public void removeSong(int i) {
        getSongs().remove(i);
    }

    /**
     * addPlaylist
     * adds a playlist to the database
     * @param playlist - Playlist object
     */
    public void addPlaylist(Playlist playlist) {
        getPlaylists().add(playlist);
    }

    /**
     * getPlaylist
     * gets a specific playlist from the database
     * @param i - specific playlist
     * @return a Playlist object
     */
    public Playlist getPlaylist(int i) {
        return getPlaylists().get(i);
    }

    /**
     * getNumPlaylists
     * gets the number total playlists
     * @return the amount of playlists
     */
    public int getNumPlaylists() {
        return getPlaylists().size();
    }

    /**
     * removePlaylist
     * removes a playlist from the database of Playlists
     * @param i the index of the playlist to remove
     */
    public void removePlaylist(int i) {
        getPlaylists().remove(i);
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
        return "Database{" +
                "songs=" + getSongs() +
                ", playlists=" + getPlaylists() +
                '}';
    }
}
