import java.util.*;
import java.util.concurrent.*;

class AnalyticsDashboard {
    private Map<String, Integer> pageViews = new ConcurrentHashMap<>();
    private Map<String, Set<String>> uniqueVisitors = new ConcurrentHashMap<>();
    private Map<String, Integer> trafficSources = new ConcurrentHashMap<>();

    public void processEvent(String url, String userId, String source) {
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);
        uniqueVisitors.computeIfAbsent(url, k -> ConcurrentHashMap.newKeySet()).add(userId);
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    public void getDashboard() {
        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());
        pq.addAll(pageViews.entrySet());

        System.out.println("Top Pages:");
        int rank = 1;
        while (!pq.isEmpty() && rank <= 10) {
            Map.Entry<String, Integer> entry = pq.poll();
            String url = entry.getKey();
            int views = entry.getValue();
            int uniques = uniqueVisitors.getOrDefault(url, Collections.emptySet()).size();
            System.out.println(rank + ". " + url + " - " + views + " views (" + uniques + " unique)");
            rank++;
        }

        int totalSources = trafficSources.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println("\nTraffic Sources:");
        for (Map.Entry<String, Integer> entry : trafficSources.entrySet()) {
            double percent = (entry.getValue() * 100.0) / totalSources;
            System.out.println(entry.getKey() + ": " + String.format("%.1f", percent) + "%");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AnalyticsDashboard dashboard = new AnalyticsDashboard();

        dashboard.processEvent("/article/breaking-news", "user_123", "google");
        dashboard.processEvent("/article/breaking-news", "user_456", "facebook");
        dashboard.processEvent("/sports/championship", "user_789", "direct");
        dashboard.processEvent("/sports/championship", "user_123", "google");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(dashboard::getDashboard, 0, 5, TimeUnit.SECONDS);
    }
}