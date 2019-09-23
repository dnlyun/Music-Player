/**
 * [StackedLinkedList.java]
 * Stacked linked list to create lists for shuffling songs
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

import java.util.Random;

public class StackedLinkedList<E> implements LinkedList<E> {
    private Node<E> head;

    /**
     * push
     * adds an item to the list
     * @param item the item being added of type E
     */
    public void push(E item) {
        head = new Node<>(item, head);
    }

    /**
     * pop
     * pops off the most recently pushed item
     * @return item
     */
    public E pop() {
        E item = head.getItem();
        head = head.getNext();
        return item;
    }

    @Override
    public void add(E item) {
        Node<E> tempNode = head;

        if (head == null) {
            head = new Node<>(item);
        } else {
            while(tempNode.getNext()!= null) {
                tempNode = tempNode.getNext();
            }
            tempNode.setNext(new Node<>(item));
        }
    }

    @Override
    public E get(int index) {
        Node<E> tempNode = head;

        for (int i = 0; i < index; i++) {
            tempNode = tempNode.getNext();
        }

        return tempNode.getItem();
    }

    @Override
    public int indexOf(E item) {
        Node<E> tempNode = head;
        int index = 0;

        while (tempNode.getNext() != null) {
            if (tempNode.getItem().equals(item)) {
                return index;
            }
            index++;
            tempNode = tempNode.getNext();
        }
        return -1;
    }

    @Override
    public void remove(int index) {
        Node<E> tempNode = head;

        if (index == 0) {
            head = tempNode.getNext();
        } else {
            for (int i = 0; i < index - 1; i++) {
                tempNode = tempNode.getNext();
            }
            tempNode.setNext(tempNode.getNext().getNext());
        }
    }

    @Override
    public void remove(E item) {
        remove(indexOf(item));
    }

    @Override
    public void clear() {
        head = null;
    }

    @Override
    public int size() {
        Node<E> tempNode = head;
        int size = 0;

        while (tempNode != null) {
            size++;
            tempNode = tempNode.getNext();
        }
        return size;
    }

    @Override
    public void shuffleList() {
        Random rand = new Random();
        for (int i = 0; i < size(); i++) {
            int randI = rand.nextInt(size());
            E temp = get(randI);
            remove(randI);
            push(temp);
        }
    }

}