/**
 * [Node.java]
 * Node class
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

class Node<T> {
    private T item;
    private Node<T> next;

    /**
     * Node
     * constructor for this class
     * @param item T Generic object
     */
    public Node(T item) {
        this.item = item;
        this.next = null;
    }

    /**
     * Node
     * constructor for this class
     * @param item T Generic object
     * @param next Node<T> next node
     */
    public Node(T item, Node<T> next) {
        this.item = item;
        this.next = next;
    }

    /**
     * getNext
     * gets the next Node
     * @return Node<T> object
     */
    public Node<T> getNext() {
        return this.next;
    }

    /**
     * setNext
     * sets the next node
     * @param next Node<T> object
     */
    public void setNext(Node<T> next) {
        this.next = next;
    }

    /**
     * getItem
     * gets the current item
     * @return T Generic item
     */
    public T getItem() {
        return this.item;
    }
}
