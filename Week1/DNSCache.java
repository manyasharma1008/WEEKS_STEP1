import java.util.*;

class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;

    DNSEntry(String domain, String ipAddress, int ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class DNSCache {
    private final int MAX_CACHE_SIZE = 5;
    private Map<String, DNSEntry> cache = new LinkedHashMap<>(16, 0.75f, true);
    private int hits = 0;
    private int misses = 0;

    public String resolve(String domain) {
        DNSEntry entry = cache.get(domain);
        if (entry != null && !entry.isExpired()) {
            hits++;
            return "Cache HIT → " + entry.ipAddress;
        } else {
            misses++;
            String ip = queryUpstream(domain);
            put(domain, ip, 5);
            return "Cache MISS → " + ip;
        }
    }

    private void put(String domain, String ip, int ttlSeconds) {
        if (cache.size() >= MAX_CACHE_SIZE) {
            Iterator<String> it = cache.keySet().iterator();
            if (it.hasNext()) {
                cache.remove(it.next());
            }
        }
        cache.put(domain, new DNSEntry(domain, ip, ttlSeconds));
    }

    private String queryUpstream(String domain) {
        return "172.217." + new Random().nextInt(255) + "." + new Random().nextInt(255);
    }

    public String getCacheStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);
        return "Hit Rate: " + hitRate + "%, Hits: " + hits + ", Misses: " + misses;
    }

    public static void main(String[] args) throws InterruptedException {
        DNSCache dnsCache = new DNSCache();

        System.out.println(dnsCache.resolve("google.com"));
        System.out.println(dnsCache.resolve("google.com"));
        Thread.sleep(6000);
        System.out.println(dnsCache.resolve("google.com"));
        System.out.println(dnsCache.getCacheStats());
    }
}