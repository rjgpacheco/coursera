/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
// import edu.princeton.cs.algs4.Stopwatch;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final double MAX_NULL_PERCENTAGE = 0.75;

    private int numberNull;
    private int capacity;
    private int currentLast; // Keep track of the last non null element
    private Item[] array;

    // construct an empty randomized queue
    public RandomizedQueue() {
        capacity = 1;
        numberNull = capacity;
        currentLast = -1; // Will be incremented when item is added
        array = (Item[]) new Object[capacity];
    }

    private int getRandomIndex() {
        return StdRandom.uniform(currentLast+1);
    }


    /*
    private void showAll() {
        for (int i = 0; i < (capacity-1); i++)
            System.out.println("Index: " + i + " :: " + array[i]);

        if (currentLast > -1)
            System.out.println("Current last: array[" + currentLast + "] = " + array[currentLast]);
    }
    */

    /*
    private void queueStats() {
        int nNull = 0;
        int nItems = 0;
        for (int i = 0; i < (capacity); i++) {
            if (array[i] == null)
                nNull++;
            else
                nItems++;
        }

        System.out.println("Number items: " + nItems + "; number null: " + nNull + "; total capacity: " + capacity + " items");
        // System.out.println("Number items: " + size() + "; number null: " + numberNull + "; total capacity: " + capacity + " items");
    }
    */

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException();
        insert(item);
    }

    // remove and return a random item
    public Item dequeue() {
        if (size() == 0) throw new java.util.NoSuchElementException();
        int idx = getRandomIndex();
        Item item = array[idx];
        remove(idx);
        return item;
    }

    public Item sample() {
        if (size() == 0) throw new java.util.NoSuchElementException();
        return array[getRandomIndex()];
    }

    private void insert(Item item) {
        numberNull--;
        currentLast++;
        array[currentLast] = item;
        resizeIfNeeded();
    }

    private void remove(int position) {
        numberNull++;
        array[position] = array[currentLast];
        array[currentLast] = null;
        currentLast--;
        resizeIfNeeded();
    }

    private void resizeIfNeeded() {
        if (numberNull == 0) {
            resize(capacity *2);
        }

        else if ((((double) numberNull) / capacity) > MAX_NULL_PERCENTAGE) {
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
        return new RandomizedQueueIterator(size());
    }


    private class RandomizedQueueIterator implements Iterator<Item> {

        private int currentIndex = 0;
        private final Item[] iteratorArray;
        private final int maxIndex;

        private RandomizedQueueIterator(int n) {
            maxIndex = n-1;
            iteratorArray = (Item[]) new Object[n];
            int j = 0;
            for (int i = 0; i < capacity; i++)
                if (array[i] != null)
                    iteratorArray[j++] = array[i];
            StdRandom.shuffle(iteratorArray);
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


    public static void main(String[] args) {

        /*
        int numberItems = Integer.parseInt(args[0]);
        // int sampleSize = Integer.parseInt(args[1]);

        RandomizedQueue<Integer> qInt = new RandomizedQueue<Integer>();

        Stopwatch timer1 = new Stopwatch();

        System.out.println("Inserting " + numberItems + " items...");
        for (int i = 0; i < numberItems; i++)
            qInt.enqueue(i);
        qInt.queueStats();


        for (int i = 0; i < numberItems; i++) {
            qInt.dequeue();
            qInt.dequeue();
            qInt.enqueue(i);
            qInt.enqueue(i);
        }


        double time1 = timer1.elapsedTime();

        System.out.println("n = " + numberItems + " , t = " + time1);
        */

        /*
        System.out.println("Removing all but one item...");
        for (int i = 0; i < (numberItems-1); i++)
            qInt.dequeue();
        qInt.queueStats();
        */






        // System.out.println("\n");



    }
}
