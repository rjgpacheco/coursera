/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

import edu.princeton.cs.algs4.Digraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class WordNet {

    private HashMap<String, Integer> synsetNounToId = new HashMap<>();
    private HashMap<Integer, String> synsetIdToSynset = new HashMap<>();

    // For each noun, keeptrack of which synsets it appears on
    private HashMap<String, LinkedList<Integer>> synsetNounToIdList = new HashMap<>();

    // private HashMap<Integer, String> SynsetIdToNoun = new HashMap<>();

    private Digraph G;
    private SAP sapG;

    private int numberOfSynsets;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms)  {
        readSynset(synsets);
        readHypernym(hypernyms);
        this.sapG = new SAP(G);
    }

    private void readSynset(String synsets) {
        int id; // just initialize
        In in = new In(synsets);
        LinkedList<Integer> nounSynsetList;

        numberOfSynsets = 0;
        while (in.hasNextLine()) {
            numberOfSynsets++;
            String[] fields = in.readLine().split(",");
            id = Integer.parseInt(fields[0]);
            this.synsetIdToSynset.put(id, fields[1]);
            for (String noun : fields[1].split(" ")) {
                this.synsetNounToId.put(noun, id);
                if (!synsetNounToIdList.containsKey(noun)) {
                    nounSynsetList = new LinkedList<Integer>();
                    nounSynsetList.add(id);
                } else {
                    nounSynsetList = synsetNounToIdList.get(noun);
                    nounSynsetList.add(id);
                }
                synsetNounToIdList.put(noun, nounSynsetList);

            }
        }

        this.G = new Digraph(numberOfSynsets);
    }

    private void readHypernym(String hypernyms) {
        In in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] fields = in.readLine().split(",");
            int v = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int w = Integer.parseInt(fields[i]);
                this.G.addEdge(v, w);
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        // SynsetMap.
        return synsetNounToId.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return synsetNounToId.keySet().contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return this.sapG.length(synsetNounToIdList.get(nounA), synsetNounToIdList.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        int synsetId = this.sapG.ancestor(synsetNounToIdList.get(nounA), synsetNounToIdList.get(nounB));
        return synsetIdToSynset.get(synsetId);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // java-algs4 WordNet synsets.txt hypernyms.txt

        // synsets.txt:
        // 0,ABO_antibodies,blood type antibodies
        // synsets3.txt

        // hypernyms.txt
        // 0,79541
        // 1,34093
        // hypernyms100K.txt

        // String id = fields[0];
        // String synset = fields[1]; // nouns are space seperated here
        // String gloss = fields[2]; // Not relevant


        String synsets = args[0];
        String hypernyms = args[1];


        WordNet wordnet = new WordNet(synsets, hypernyms);



        System.out.println(synsets);
        System.out.println(hypernyms);

        System.out.println(wordnet.distance("jump", "parachuting"));
        System.out.println(wordnet.G.V());
        System.out.println(wordnet.synsetNounToIdList.size());




        // test out hasmaps

        // HashMap<Integer, ArrayList<Integer>> maplist= new HashMap<>();

        /*
        HashMap<Integer, LinkedList<Integer>> maplist2= new HashMap<>();
        for (int key: keys) {
            System.out.printf("\nkey: %d", key);
            for (int value: values) {
                value = value+key;
                if (!maplist2.containsKey(key)) {
                    ll = new LinkedList<Integer>();
                    ll.add(value);
                    maplist2.put(key, ll);
                } else {
                    maplist2.compute(key, (keyn, val) -> val.add(value));
                }
            }
        }

         */




        System.out.printf("\n");

        /*
        value.add(20);
        value.add(30);
        maplist.put(key, value);


        for (int i : maplist.get(key)) {
            System.out.println(i);
        }

         */



        /*
        int counter = 0;
        for (String noun: wordnet.nouns()) {
            if (++counter > 10) break;
            System.out.printf("%6d %s\n", counter, noun);
        }

        int n = 10;
        String[] test = new String[n];

        for (int i = 0; i < n; i++) {
            test[i] = Integer.toString(i);
        }

        System.out.printf("First entry: %s\n", test[0]);
        for (int i = 1; i < test.length; i++) {
            System.out.printf("test[%d]: %s\n", i, test[i]);
        }

         */

    }
}

