import java.util.*;

class PlagiarismDetector {
    private Map<String, Set<String>> nGramIndex = new HashMap<>();
    private int hits = 0, misses = 0;

    private List<String> tokenize(String text) {
        return Arrays.asList(text.toLowerCase().split("\\s+"));
    }

    private List<String> extractNGrams(List<String> words, int n) {
        List<String> nGrams = new ArrayList<>();
        for (int i = 0; i <= words.size() - n; i++) {
            nGrams.add(String.join(" ", words.subList(i, i + n)));
        }
        return nGrams;
    }

    public void indexDocument(String docId, String content, int n) {
        List<String> words = tokenize(content);
        for (String nGram : extractNGrams(words, n)) {
            nGramIndex.computeIfAbsent(nGram, k -> new HashSet<>()).add(docId);
        }
    }

    public Map<String, Double> analyzeDocument(String docId, String content, int n) {
        List<String> words = tokenize(content);
        List<String> nGrams = extractNGrams(words, n);
        Map<String, Integer> matchCount = new HashMap<>();

        for (String nGram : nGrams) {
            if (nGramIndex.containsKey(nGram)) {
                hits++;
                for (String otherDoc : nGramIndex.get(nGram)) {
                    if (!otherDoc.equals(docId)) {
                        matchCount.put(otherDoc, matchCount.getOrDefault(otherDoc, 0) + 1);
                    }
                }
            } else {
                misses++;
            }
        }

        Map<String, Double> similarity = new HashMap<>();
        for (Map.Entry<String, Integer> entry : matchCount.entrySet()) {
            double percent = (entry.getValue() * 100.0) / nGrams.size();
            similarity.put(entry.getKey(), percent);
        }
        return similarity;
    }

    public String getStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);
        return "Hit Rate: " + hitRate + "%, Hits: " + hits + ", Misses: " + misses;
    }

    public static void main(String[] args) {
        PlagiarismDetector detector = new PlagiarismDetector();

        detector.indexDocument("essay_089", "this is a sample essay with some unique content", 5);
        detector.indexDocument("essay_092", "this is a sample essay with some plagiarized content repeated many times", 5);

        Map<String, Double> result = detector.analyzeDocument("essay_123",
                "this is a sample essay with some plagiarized content repeated many times", 5);

        for (Map.Entry<String, Double> entry : result.entrySet()) {
            System.out.println("Found matching n-grams with \"" + entry.getKey() + "\" → Similarity: " + entry.getValue() + "%");
        }

        System.out.println(detector.getStats());
    }
}