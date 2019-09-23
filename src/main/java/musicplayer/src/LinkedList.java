/**
 * [LinkedList.java]
 * Implementing classes will be able to add, get, remove, shuffle, etc...
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

public interface LinkedList<E> {

    /**
     * add
     * adds new item to the list
     * @param item
     */
    void add(E item);

    /**
     * get
     * gets the item of the node at the index
     * @param index the index of the selected node
     * @return the item
     */
    E get(int index);

    /**
     * indexOf
     * returns the index of the node containing the item
     * @param item the item to search for
     * @return
     */
    int indexOf(E item);

    /**
     * remove
     * removes the node at the selected index
     * @param index the selected index
     */
    void remove(int index);

    /**
     * remove
     * removes the node with the item
     * @param item the selected item
     */
    void remove(E item);
    void clear();

    /**
     * clear
     * clears the list
     * @return
     */
    int size();

    /**
     * shuffleList
     * Shuffles the list
     */
    void shuffleList();
}
