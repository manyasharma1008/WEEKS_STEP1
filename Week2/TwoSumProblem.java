import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    String account;
    long timestamp; // in ms

    Transaction(int id, int amount, String merchant, String account, long timestamp) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.timestamp = timestamp;
    }
}

public class TwoSumProblem {
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // Classic Two-Sum
    public List<int[]> findTwoSum(int target) {
        Map<Integer, Transaction> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();
        for (Transaction t : transactions) {
            int complement = target - t.amount;
            if (map.containsKey(complement)) {
                result.add(new int[]{map.get(complement).id, t.id});
            }
            map.put(t.amount, t);
        }
        return result;
    }

    // Two-Sum with time window (1 hour)
    public List<int[]> findTwoSumWithTimeWindow(int target) {
        List<int[]> result = new ArrayList<>();
        Map<Integer, List<Transaction>> map = new HashMap<>();
        for (Transaction t : transactions) {
            int complement = target - t.amount;
            if (map.containsKey(complement)) {
                for (Transaction other : map.get(complement)) {
                    if (Math.abs(t.timestamp - other.timestamp) <= 3600000) {
                        result.add(new int[]{other.id, t.id});
                    }
                }
            }
            map.computeIfAbsent(t.amount, k -> new ArrayList<>()).add(t);
        }
        return result;
    }

    // K-Sum (recursive)
    public List<List<Integer>> findKSum(int k, int target) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(transactions, k, target, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(List<Transaction> trans, int k, int target, int start,
                           List<Integer> current, List<List<Integer>> result) {
        if (k == 0 && target == 0) {
            result.add(new ArrayList<>(current));
            return;
        }
        if (k == 0 || target < 0) return;

        for (int i = start; i < trans.size(); i++) {
            current.add(trans.get(i).id);
            backtrack(trans, k - 1, target - trans.get(i).amount, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    // Duplicate detection
    public List<Map<String, Object>> detectDuplicates() {
        Map<String, Map<Integer, Set<String>>> map = new HashMap<>();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Transaction t : transactions) {
            map.computeIfAbsent(t.merchant, k -> new HashMap<>());
            Map<Integer, Set<String>> amountMap = map.get(t.merchant);
            amountMap.computeIfAbsent(t.amount, k -> new HashSet<>());
            Set<String> accounts = amountMap.get(t.amount);
            accounts.add(t.account);

            if (accounts.size() > 1) {
                Map<String, Object> duplicate = new HashMap<>();
                duplicate.put("amount", t.amount);
                duplicate.put("merchant", t.merchant);
                duplicate.put("accounts", new ArrayList<>(accounts));
                result.add(duplicate);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        TwoSumProblem fd = new TwoSumProblem();

        fd.addTransaction(new Transaction(1, 500, "Store A", "acc1", 1000));
        fd.addTransaction(new Transaction(2, 300, "Store B", "acc2", 2000));
        fd.addTransaction(new Transaction(3, 200, "Store C", "acc3", 2500));
        fd.addTransaction(new Transaction(4, 500, "Store A", "acc2", 3000));

        System.out.println("findTwoSum(target=500) → " + Arrays.deepToString(fd.findTwoSum(500).toArray()));
        System.out.println("findTwoSumWithTimeWindow(target=500) → " + Arrays.deepToString(fd.findTwoSumWithTimeWindow(500).toArray()));
        System.out.println("findKSum(k=3, target=1000) → " + fd.findKSum(3, 1000));
        System.out.println("detectDuplicates() → " + fd.detectDuplicates());
    }
}