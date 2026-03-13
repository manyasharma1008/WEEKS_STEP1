import java.util.*;

class VideoData {
    String videoId;
    String content; // placeholder for video data
    VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
    }
}

class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;
    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}

public class MultiLevelCacheSystem {
    private LRUCache<String, VideoData> L1 = new LRUCache<>(10000);
    private LRUCache<String, VideoData> L2 = new LRUCache<>(100000);
    private Map<String, VideoData> L3 = new HashMap<>(); // simulate DB
    private Map<String, Integer> accessCount = new HashMap<>();

    private int L1Hits = 0, L2Hits = 0, L3Hits = 0, totalRequests = 0;
    private long L1Time = 0, L2Time = 0, L3Time = 0;

    public VideoData getVideo(String videoId) {
        totalRequests++;
        long start = System.nanoTime();

        if (L1.containsKey(videoId)) {
            L1Hits++;
            L1Time += (System.nanoTime() - start);
            return L1.get(videoId);
        }

        if (L2.containsKey(videoId)) {
            L2Hits++;
            L2Time += (System.nanoTime() - start);
            VideoData data = L2.get(videoId);
            promoteToL1(videoId, data);
            return data;
        }

        if (L3.containsKey(videoId)) {
            L3Hits++;
            L3Time += (System.nanoTime() - start);
            VideoData data = L3.get(videoId);
            L2.put(videoId, data);
            accessCount.put(videoId, accessCount.getOrDefault(videoId, 0) + 1);
            return data;
        }

        return null; // not found
    }

    private void promoteToL1(String videoId, VideoData data) {
        int count = accessCount.getOrDefault(videoId, 0) + 1;
        accessCount.put(videoId, count);
        if (count > 5) { // threshold for promotion
            L1.put(videoId, data);
        }
    }

    public void addToDatabase(String videoId, String content) {
        L3.put(videoId, new VideoData(videoId, content));
    }

    public String getStatistics() {
        double L1HitRate = (L1Hits * 100.0) / totalRequests;
        double L2HitRate = (L2Hits * 100.0) / totalRequests;
        double L3HitRate = (L3Hits * 100.0) / totalRequests;
        double overallHitRate = ((L1Hits + L2Hits + L3Hits) * 100.0) / totalRequests;

        double avgL1Time = L1Hits == 0 ? 0 : (L1Time / L1Hits) / 1_000_000.0;
        double avgL2Time = L2Hits == 0 ? 0 : (L2Time / L2Hits) / 1_000_000.0;
        double avgL3Time = L3Hits == 0 ? 0 : (L3Time / L3Hits) / 1_000_000.0;
        double avgOverallTime = totalRequests == 0 ? 0 :
                (L1Time + L2Time + L3Time) / totalRequests / 1_000_000.0;

        return "L1: Hit Rate " + String.format("%.1f", L1HitRate) + "%, Avg Time: " + String.format("%.2f", avgL1Time) + "ms\n" +
                "L2: Hit Rate " + String.format("%.1f", L2HitRate) + "%, Avg Time: " + String.format("%.2f", avgL2Time) + "ms\n" +
                "L3: Hit Rate " + String.format("%.1f", L3HitRate) + "%, Avg Time: " + String.format("%.2f", avgL3Time) + "ms\n" +
                "Overall: Hit Rate " + String.format("%.1f", overallHitRate) + "%, Avg Time: " + String.format("%.2f", avgOverallTime) + "ms";
    }

    public static void main(String[] args) {
        MultiLevelCacheSystem cache = new MultiLevelCacheSystem();

        cache.addToDatabase("video_123", "Breaking News Video");
        cache.addToDatabase("video_999", "Sports Highlights");

        System.out.println("getVideo(\"video_123\") → " + cache.getVideo("video_123").content);
        System.out.println("getVideo(\"video_123\") → " + cache.getVideo("video_123").content);
        System.out.println("getVideo(\"video_999\") → " + cache.getVideo("video_999").content);

        System.out.println("\ngetStatistics() →\n" + cache.getStatistics());
    }
}