/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */


import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private int numberItems = 0;

    private Node first;
    private Node last;

    // construct an empty deque
    public Deque() {
        first = new Node();
        last = first;
    }

    private class Node {
        public Item item;
        public Node next;
        public Node prev;
    }

    /*
    private void printAllItems() {
        System.out.println("List Contents");
        System.out.println("First: " + first.item);
        System.out.println("Last:  " + last.item);
        Node current = first;
        int c = 0;

        while (current.item != null) {
            c++;
            System.out.println("Item " + c + ":" + current.item);
            if (current.next == null)
                break;
            current = current.next;

        }
        System.out.println("");
    }
    */

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

        if (first.item == null) {
            first.item = item;
        }
        else {
            Node newFirst = new Node();
            Node oldFirst = first;

            newFirst.item = item;

            newFirst.next = oldFirst;
            oldFirst.prev = newFirst;

            first = newFirst;
        }
        numberItems++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException();

        if (last.item == null) {
            last.item = item;
        }
        else {
            Node newLast = new Node();
            Node oldLast = last;

            newLast.item = item;

            newLast.prev = oldLast;
            oldLast.next = newLast;

            last = newLast;
        }
        numberItems++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size() == 0) throw new java.util.NoSuchElementException();
        numberItems--;
        if (first.next == null) {
            Item item = first.item;
            first.item = null;
            last = first;
            return item;
        }
        else {
            Item item = first.item;
            first = first.next;
            first.prev = null;
            return item;
        }
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (size() == 0) throw new java.util.NoSuchElementException();
        numberItems--;
        if (last.prev == null) {
            Item item = last.item;
            last.item = null;
            first = last;
            return item;
        }
        else {
            Item item = last.item;

            last = last.prev;
            last.next = null;
            return item;
        }
    }


    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new LinkedListIterator();
    }


    private class LinkedListIterator implements Iterator<Item> {

        private Node current = first;

        public boolean hasNext() {
            return current != null && current.item != null;
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();

            Item item = current.item;
            current = current.next;

            return item;

        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


    public static void main(String[] args) {
        /*
        Deque<Integer> qInt = new Deque<Integer>();

        qInt.printAllItems();

        Iterator it1 = qInt.iterator();
        Iterator it2 = qInt.iterator();

        System.out.print("\nAdd:    ");
        for (int i = 0; i < 20; i++) {
            System.out.print("" + i + " ");
            qInt.addLast(i);
        }

        System.out.println("");
        while (it1.hasNext() &&  it2.hasNext())
            System.out.println(it1.next() + ", " + it2.next());

        Deque<Integer> rInt = new Deque<Integer>();

        Iterator it3 = rInt.iterator();
        System.out.println(it3.hasNext());
        */
    }
}
