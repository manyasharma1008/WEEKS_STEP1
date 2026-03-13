import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEndOfWord;
    int frequency;
}

public class AutocompleteSystem {
    private TrieNode root = new TrieNode();
    private Map<String, Integer> globalFrequency = new HashMap<>();

    public void insert(String query) {
        TrieNode node = root;
        for (char c : query.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isEndOfWord = true;
        node.frequency++;
        globalFrequency.put(query, globalFrequency.getOrDefault(query, 0) + 1);
    }

    public List<String> search(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            node = node.children.get(c);
            if (node == null) return Collections.emptyList();
        }
        List<String> results = new ArrayList<>();
        dfs(node, new StringBuilder(prefix), results);
        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> globalFrequency.get(b) - globalFrequency.get(a));
        pq.addAll(results);

        List<String> topResults = new ArrayList<>();
        int count = 0;
        while (!pq.isEmpty() && count < 10) {
            String q = pq.poll();
            topResults.add(q + " (" + globalFrequency.get(q) + " searches)");
            count++;
        }
        return topResults;
    }

    private void dfs(TrieNode node, StringBuilder prefix, List<String> results) {
        if (node.isEndOfWord) results.add(prefix.toString());
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            prefix.append(entry.getKey());
            dfs(entry.getValue(), prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public void updateFrequency(String query) {
        insert(query);
        System.out.println("Frequency of \"" + query + "\": " + globalFrequency.get(query));
    }

    public static void main(String[] args) {
        AutocompleteSystem system = new AutocompleteSystem();

        system.insert("java tutorial");
        system.insert("javascript");
        system.insert("java download");
        system.insert("java 21 features");
        system.insert("java 21 features");

        System.out.println("search(\"jav\") →");
        for (String suggestion : system.search("jav")) {
            System.out.println(suggestion);
        }

        system.updateFrequency("java 21 features");
    }
}