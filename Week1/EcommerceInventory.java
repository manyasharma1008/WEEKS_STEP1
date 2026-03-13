import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class EcommerceInventory {
    private Map<String, AtomicInteger> stockMap = new ConcurrentHashMap<>();
    private Map<String, Queue<Integer>> waitingList = new ConcurrentHashMap<>();

    public void addProduct(String productId, int stockCount) {
        stockMap.put(productId, new AtomicInteger(stockCount));
        waitingList.put(productId, new LinkedList<>());
    }

    public String checkStock(String productId) {
        return stockMap.get(productId).get() + " units available";
    }

    public String purchaseItem(String productId, int userId) {
        AtomicInteger stock = stockMap.get(productId);
        if (stock.get() > 0) {
            int remaining = stock.decrementAndGet();
            return "Success, " + remaining + " units remaining";
        } else {
            Queue<Integer> queue = waitingList.get(productId);
            queue.add(userId);
            return "Added to waiting list, position #" + queue.size();
        }
    }

    public static void main(String[] args) {
        EcommerceInventory manager = new EcommerceInventory();
        manager.addProduct("IPHONE15_256GB", 100);

        System.out.println(manager.checkStock("IPHONE15_256GB"));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));

        for (int i = 0; i < 98; i++) {
            manager.purchaseItem("IPHONE15_256GB", i);
        }

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));
    }
}