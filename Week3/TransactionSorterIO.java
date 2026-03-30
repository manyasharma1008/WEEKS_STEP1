import java.util.*;

class Transaction {
    String id;
    double fee;
    String timestamp;

    Transaction(String id, double fee, String timestamp) {
        this.id = id;
        this.fee = fee;
        this.timestamp = timestamp;
    }

    public String toString() {
        return id + ", fee=" + fee + ", ts=" + timestamp;
    }
}

public class TransactionSorterIO {
    public static void bubbleSort(List<Transaction> txns) {
        int n = txns.size();
        boolean swapped;
        int passes = 0, swaps = 0;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            passes++;
            for (int j = 0; j < n - i - 1; j++) {
                if (txns.get(j).fee > txns.get(j + 1).fee) {
                    Collections.swap(txns, j, j + 1);
                    swaps++;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
        System.out.println("BubbleSort (fees): " + txns + " // " + passes + " passes, " + swaps + " swaps");
    }

    public static void insertionSort(List<Transaction> txns) {
        int passes = 0, shifts = 0;
        for (int i = 1; i < txns.size(); i++) {
            Transaction key = txns.get(i);
            int j = i - 1;
            passes++;
            while (j >= 0 && (txns.get(j).fee > key.fee ||
                    (txns.get(j).fee == key.fee && txns.get(j).timestamp.compareTo(key.timestamp) > 0))) {
                txns.set(j + 1, txns.get(j));
                j--;
                shifts++;
            }
            txns.set(j + 1, key);
        }
        System.out.println("InsertionSort (fee+ts): " + txns + " // " + passes + " passes, " + shifts + " shifts");
    }

    public static void flagOutliers(List<Transaction> txns) {
        for (Transaction t : txns) {
            if (t.fee > 50.0) {
                System.out.println("High-fee outlier flagged: " + t);
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Transaction> txns = new ArrayList<>();

        System.out.println("Enter number of transactions:");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {
            System.out.println("Enter transaction id:");
            String id = sc.nextLine();
            System.out.println("Enter fee:");
            double fee = sc.nextDouble();
            sc.nextLine();
            System.out.println("Enter timestamp:");
            String ts = sc.nextLine();
            txns.add(new Transaction(id, fee, ts));
        }

        if (n <= 100) {
            bubbleSort(new ArrayList<>(txns));
        } else if (n <= 1000) {
            insertionSort(new ArrayList<>(txns));
        }

        flagOutliers(txns);
    }
}
