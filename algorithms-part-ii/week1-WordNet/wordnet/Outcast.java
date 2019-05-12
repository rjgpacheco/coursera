/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

// import java.util.HashMap;
// import java.util.HashSet;
// import edu.princeton.cs.algs4.BinarySearchST;
public class Outcast {

    // private final HashMap<Integer, HashMap<Integer, Integer>> distances = new HashMap<>();
    // private final BinarySearchST<Integer, BinarySearchST<Integer, Integer>> distances = new BinarySearchST<>();

    // private final HashMap<String, Integer> nounToInt = new HashMap<>();
    // private final HashMap<Integer, String> intToNoun = new HashMap<>();

    private WordNet wordnet;

    // constructor takes a WordNet object {
    public Outcast(WordNet wordnet) {
        if (wordnet == null) throw new java.lang.IllegalArgumentException();

        int id = 0;

        for (String noun: wordnet.nouns()) {
            id++;
            // nounToInt.put(noun, id);
            // intToNoun.put(id, noun);
        }

        this.wordnet = wordnet;
    }


    private int getDistance(String nounA, String nounB) {
        return wordnet.distance(nounA, nounB);
        /*
        int x = nounToInt.get(nounA);
        int y = nounToInt.get(nounB);

        if (x == y) {
            return 0;
        }

        int smallest = Math.min(x, y);
        int largest = Math.max(x,y);

        // Was the distance already computed?
        if (distanceAlreadyCalculated(smallest, largest)) {
            return distances.get(smallest).get(largest);
        }

        // If not, insert it and return it
        insertDistance(smallest, largest);
        return distances.get(smallest).get(largest);

         */
    }

    /*
    private void insertDistance(int smallest, int largest) {

        // Sanity check
        if (distanceAlreadyCalculated(smallest, largest)) {
            return;
        }

        int nodeDistance = wordnet.distance(intToNoun.get(smallest), intToNoun.get(largest));


        // If the smallest isn't there, initialize it
        if (!distances.containsKey(smallest)) {
            distances.put(smallest, new HashMap<>());
        }

        distances.get(smallest).put(largest, nodeDistance);
    }

     */

    /*
    private boolean distanceAlreadyCalculated(int smallest, int largest) {
        if (!distances.containsKey(smallest)             ) { return false; }
        if (!distances.get(smallest).containsKey(largest)) { return false; }
        return true;
    }

     */

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null || nouns.length == 0) throw new java.lang.IllegalArgumentException();

        int minDistance = Integer.MIN_VALUE;
        String outcast = "";

        for (String noun: nouns) {
            if (noun == null) throw new java.lang.IllegalArgumentException();

            int sumDistances = 0;

            /*
            for (String wornetNoun: nounToInt.keySet()) {
                sumDistances = sumDistances + getDistance(noun, wornetNoun);
            }

             */

            for (String wornetNoun: wordnet.nouns()) {
                sumDistances = sumDistances + getDistance(noun, wornetNoun);
            }


            if (sumDistances > minDistance) {
                minDistance = sumDistances;
                outcast = noun;
            }
        }

        return outcast;
    }

    // see test client below
    public static void main(String[] args) {

    }
}