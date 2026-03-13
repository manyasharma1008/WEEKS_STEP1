import java.util.concurrent.*;

class TokenBucket {
    private final int maxTokens;
    private final int refillRate; // tokens per hour
    private int tokens;
    private long lastRefillTime;

    public TokenBucket(int maxTokens, int refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    private void refill() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTime;
        long hours = elapsed / 3600000; // convert ms to hours
        if (hours > 0) {
            tokens = maxTokens;
            lastRefillTime = now;
        }
    }

    public synchronized boolean allowRequest() {
        refill();
        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    public synchronized int getRemainingTokens() {
        refill();
        return tokens;
    }

    public synchronized long getResetTime() {
        return lastRefillTime + 3600000;
    }
}

public class APIGateway {
    private final ConcurrentHashMap<String, TokenBucket> clientBuckets = new ConcurrentHashMap<>();
    private final int maxTokens = 1000;
    private final int refillRate = 1000;

    public String checkRateLimit(String clientId) {
        clientBuckets.putIfAbsent(clientId, new TokenBucket(maxTokens, refillRate));
        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.getRemainingTokens() + " requests remaining)";
        } else {
            long retryAfter = (bucket.getResetTime() - System.currentTimeMillis()) / 1000;
            return "Denied (0 requests remaining, retry after " + retryAfter + "s)";
        }
    }

    public String getRateLimitStatus(String clientId) {
        TokenBucket bucket = clientBuckets.get(clientId);
        if (bucket == null) return "No requests made yet";
        int used = maxTokens - bucket.getRemainingTokens();
        return "{used: " + used + ", limit: " + maxTokens + ", reset: " + bucket.getResetTime() + "}";
    }

    public static void main(String[] args) {
        APIGateway limiter = new APIGateway();

        for (int i = 0; i < 1002; i++) {
            System.out.println("checkRateLimit(clientId=\"abc123\") → " + limiter.checkRateLimit("abc123"));
        }

        System.out.println("getRateLimitStatus(\"abc123\") → " + limiter.getRateLimitStatus("abc123"));
    }
}