/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */


import java.util.Iterator;

public class DequeOld<Item> implements Iterable<Item> {


    private int numberItems = 0;

    private final MyLinkedList<Item> myLinkedList;

    private class MyLinkedList<Item> {

        private Node first = null;
        private Node current = first;

        public MyLinkedList() {
            current = first;
        }

        private class Node {
            Item item;
            Node next;
        }

        public boolean hasNext() {
            return (current != null && current.next != null);
        }

        public Item next() {
            if (numberItems == 0) throw new java.util.NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void pushFirst(Item item) {
            Node oldFirst = first;
            first = new Node();
            first.item = item;
            first.next = oldFirst;
        }

        public void pushLast(Item item) {
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

        public Item popFirst() {
            Item item = first.item;
            first = first.next;
            return item;
        }

        public Item popLast() {
            Node nextToLast = getNextToLast();
            if (nextToLast.next == null) {
                Item lastItem = nextToLast.item;
                nextToLast = null;
                first = null;
                current = first;
                return lastItem;
            }
            else {
                Item lastItem = nextToLast.next.item;
                nextToLast.next = null;
                return lastItem;
            }
        }
    }

    // construct an empty deque
    public DequeOld() {
        myLinkedList = new MyLinkedList<Item>();
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

        myLinkedList.pushFirst(item);
        numberItems++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException();

        if (numberItems == 0)
            myLinkedList.pushFirst(item);
        else
            myLinkedList.pushLast(item);
        numberItems++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size() == 0) throw new java.util.NoSuchElementException();

        numberItems--;
        return myLinkedList.popFirst();
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (size() == 0) throw new java.util.NoSuchElementException();

        numberItems--;
        return myLinkedList.popLast();
    }


    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new LinkedListIterator();
    }


    private class LinkedListIterator implements Iterator<Item> {
        public boolean hasNext() {
            return myLinkedList.hasNext();
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return myLinkedList.next();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


    public static void main(String[] args) {
        DequeOld<Integer> qInt = new DequeOld<Integer>();

        Iterator<Integer> qIntIterator = qInt.iterator();

        int n = 20;
        for (int i = 0; i < n; i++)
            qInt.addFirst(i);



        int acc = 0;
        while (qIntIterator.hasNext()) {
            qIntIterator.next();
            acc++;
        }

        System.out.println("qInt.size(): " + qInt.size());
        System.out.println("acc: " + acc);


        int acc2 = 0;
        while (qInt.size() != 0) {
            qInt.removeFirst();
            acc2++;
        }

        System.out.println("qInt.size(): " + qInt.size());
        System.out.println("acc2: " + acc2);




    }
}
