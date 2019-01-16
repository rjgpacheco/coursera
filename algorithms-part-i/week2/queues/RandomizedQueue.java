/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int numberNull;
    private int capacity;
    private Item[] array;

    // construct an empty randomized queue
    public RandomizedQueue() {
        capacity = 1;
        numberNull = capacity;
        array = (Item[]) new Object[capacity];
    }

    private int getRandomIndex() {
        return StdRandom.uniform(capacity);
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException();
        int idx = getRandomIndex();
        while (array[idx] != null)
            idx = getRandomIndex();
        insert(idx, item);
    }

    // remove and return a random item
    public Item dequeue() {
        if (size() == 0) throw new java.util.NoSuchElementException();
        int idx = getRandomIndex();
        while (array[idx] == null)
            idx = getRandomIndex();
        Item item = array[idx];
        remove(idx);
        return item;
    }

    public Item sample() {
        if (size() == 0) throw new java.util.NoSuchElementException();
        int idx = getRandomIndex();
        while (array[idx] == null)
            idx = getRandomIndex();
        return array[idx];
    }

    private void insert(int position, Item item) {
        if (array[position] == null)
            numberNull--;
        array[position] = item;
        resizeIfNeeded();
    }

    private void remove(int position) {
        numberNull++;
        array[position] = null;
        resizeIfNeeded();
    }

    private void resizeIfNeeded() {
        if (numberNull == 0) {
            resize(capacity *2);
        }

        if (numberNull / capacity == 4) {
            resize(capacity /2);
        }
    }

    private void resize(int newSize) {
        Item[] newArray = (Item[]) new Object[newSize];
        int newI = 0;
        for (int i = 0; i < capacity; i++) {
            if (array[i] != null) {
                newArray[newI++] = array[i];
            }
        }
        capacity = newSize;
        numberNull = capacity - newI;
        array = newArray;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return capacity - numberNull;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        // return new RandomizedQueueIterator(size());
        return new RandomizedQueueIteratorSimple();
    }


    private class RandomizedQueueIterator implements Iterator<Item> {

        private int currentIndex = 0;
        private final Item[] iteratorArray;
        private int maxIndex;

        private RandomizedQueueIterator(int n) {
            maxIndex = n-1;
            iteratorArray = (Item[]) new Object[n];
            int j = 0;
            for (int i = 0; i < capacity; i++)
                if (array[i] != null)
                    iteratorArray[j++] = array[i];

        }

        public boolean hasNext() {
            return currentIndex <= maxIndex;
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return iteratorArray[currentIndex++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


    private class RandomizedQueueIteratorSimple implements Iterator<Item> {

        public boolean hasNext() {
            return !isEmpty();
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return dequeue();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        int numberItems = Integer.parseInt(args[0]);
        // int sampleSize = Integer.parseInt(args[1]);

        RandomizedQueue<Integer> qInt = new RandomizedQueue<Integer>();

        for (int i = 0; i < numberItems; i++)
            qInt.enqueue(i);


        Iterator it1 = qInt.iterator();
        Iterator it2 = qInt.iterator();

        System.out.println("\nIterator 1:");
        while (it1.hasNext())
            System.out.print(it1.next() + " ");

        System.out.println("\nIterator 2:");
        while (it2.hasNext())
            System.out.print(it2.next() + " ");

        System.out.println("\n");
    }
}
