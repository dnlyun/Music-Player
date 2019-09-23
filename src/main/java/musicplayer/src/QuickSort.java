/**
 * [QuickSort.java]
 * Implementing classes must implement the quickSort method
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

import java.util.List;

public interface QuickSort<E> {

    /**
     * quickSort
     * sorts the list from smallest value to greatest
     * @param list the list to start
     * @param start the start index
     * @param end the end index
     * @return the sorted list
     */
    List<E> quickSort(List<E> list, int start, int end);
}
