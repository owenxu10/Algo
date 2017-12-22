import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;

public class WordNet {
    private final Map<String, List<Integer>> nouns;
    private final Map<Integer, String> map;
    private final Digraph digraph;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new java.lang.IllegalArgumentException();

        // set synsets
        map = new HashMap<Integer, String>();
        nouns = new HashMap<String, List<Integer>>();

        In in = new In(synsets);
        String line = null;

        while (in.hasNextLine()) {
            line = in.readLine();
            String[] words = line.split(",");
            int v = Integer.parseInt(words[0]);
            String[] syns = words[1].split(" ");

            map.put(v, words[1]);
            for (String syn : syns) {
                if (nouns.containsKey(syn)) {
                    nouns.get(syn).add(v);
                } else {
                    List<Integer> list = new LinkedList<Integer>();
                    list.add(v);
                    nouns.put(syn, list);
                }
            }
        }

        // set hypernyms
        digraph = new Digraph(map.size());
        boolean[] isNotRoot = new boolean[map.size()];
        in = new In(hypernyms);

        while (in.hasNextLine()) {
            line = in.readLine();
            String[] vertex = line.split(",");
            int source = Integer.parseInt(vertex[0]);
            isNotRoot[source] = true;
            for (int i = 1; i < vertex.length; i++) {
                int v = Integer.parseInt(vertex[i]);
                digraph.addEdge(source, v);
            }
        }

        // test for root: no more than one candidate root
        int rootCount = 0;
        for (int i = 0; i < map.size(); i++) {
            if (!isNotRoot[i])
                rootCount++;
        }

        DirectedCycle directedcycle = new DirectedCycle(digraph);
        if (rootCount > 1 || directedcycle.hasCycle())
            throw new java.lang.IllegalArgumentException();

        this.sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new java.lang.IllegalArgumentException();
        return nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new java.lang.IllegalArgumentException();
        if (!nouns.containsKey(nounA) || !nouns.containsKey(nounB))
            throw new java.lang.IllegalArgumentException();

        return sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new java.lang.IllegalArgumentException();
        if (!nouns.containsKey(nounA) || !nouns.containsKey(nounB))
            throw new java.lang.IllegalArgumentException();

        int iAncester = sap.ancestor(nouns.get(nounA), nouns.get(nounB));
        return map.get(iAncester);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        System.out.println(wordnet.isNoun(args[0]));
    }
}
