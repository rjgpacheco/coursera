/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */


import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private int numberItems = 0;

    private Node first = null;

    // construct an empty deque
    public Deque() { }

    private class Node {
        public Item item;
        public Node next;
    }

    private void pushFirst(Item item) {
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
    }

    private void pushLast(Item item) {
        Node oldLast = first;
        while (oldLast.next != null)
            oldLast = oldLast.next;
        Node newLast = new Node();
        newLast.item = item;
        oldLast.next = newLast;
    }

    private Node getNextToLast() {
        Node nextToLast = first;
        while (nextToLast.next != null && nextToLast.next.next != null) {
            nextToLast = nextToLast.next;
        }
        return nextToLast;
    }

    private Item popFirst() {
        Item item = first.item;
        first = first.next;
        return item;
    }

    private Item popLast() {
        Node nextToLast = getNextToLast();
        if (nextToLast.next == null) {
            Item lastItem = nextToLast.item;
            first = null;
            return lastItem;
        }
        else {
            Item lastItem = nextToLast.next.item;
            nextToLast.next = null;
            return lastItem;
        }
    }

    // is the deque empty?
    public boolean isEmpty() {
        return numberItems == 0;
    }

    // return the number of items on the deque
    public int size() {
        return numberItems;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException();

        pushFirst(item);
        numberItems++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException();

        if (numberItems == 0)
            pushFirst(item);
        else
            pushLast(item);
        numberItems++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size() == 0) throw new java.util.NoSuchElementException();

        numberItems--;
        return popFirst();
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (size() == 0) throw new java.util.NoSuchElementException();

        numberItems--;
        return popLast();
    }


    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new LinkedListIterator();
    }


    private class LinkedListIterator implements Iterator<Item> {
        public boolean hasNext() {
            return !isEmpty();
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return removeFirst();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


    public static void main(String[] args) {
        Deque<Integer> qInt = new Deque<Integer>();

        Iterator it1 = qInt.iterator();
        Iterator it2 = qInt.iterator();

        for (int i = 0; i < 20; i++) {
            qInt.addLast(i);
        }

        for (int i = 0; i < 20; i++) {
            System.out.println("it1: " + it1.next());
        }


        for (int i = 0; i < 20; i++) {
            System.out.println("it2: " + it2.next());
        }



    }
}
