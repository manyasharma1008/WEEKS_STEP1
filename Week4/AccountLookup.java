import java.util.*;

public class AccountLookup {
    public static int linearFirst(String[] logs, String target) {
        int comparisons = 0;
        for (int i = 0; i < logs.length; i++) {
            comparisons++;
            if (logs[i].equals(target)) {
                System.out.println("Linear first " + target + ": index " + i + " (" + comparisons + " comparisons)");
                return i;
            }
        }
        System.out.println("Linear first " + target + ": not found (" + comparisons + " comparisons)");
        return -1;
    }

    public static int linearLast(String[] logs, String target) {
        int comparisons = 0;
        int lastIndex = -1;
        for (int i = 0; i < logs.length; i++) {
            comparisons++;
            if (logs[i].equals(target)) {
                lastIndex = i;
            }
        }
        if (lastIndex != -1) {
            System.out.println("Linear last " + target + ": index " + lastIndex + " (" + comparisons + " comparisons)");
        } else {
            System.out.println("Linear last " + target + ": not found (" + comparisons + " comparisons)");
        }
        return lastIndex;
    }

    public static int binarySearch(String[] logs, String target) {
        int low = 0, high = logs.length - 1, comparisons = 0;
        while (low <= high) {
            int mid = (low + high) / 2;
            comparisons++;
            if (logs[mid].equals(target)) {
                System.out.println("Binary " + target + ": index " + mid + " (" + comparisons + " comparisons)");
                return mid;
            } else if (logs[mid].compareTo(target) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        System.out.println("Binary " + target + ": not found (" + comparisons + " comparisons)");
        return -1;
    }

    public static int countOccurrences(String[] logs, String target) {
        int count = 0;
        for (String log : logs) {
            if (log.equals(target)) count++;
        }
        return count;
    }

    public static void main(String[] args) {
        String[] logs = {"accB", "accA", "accB", "accC"};
        Arrays.sort(logs);
        System.out.println("Sorted logs: " + Arrays.toString(logs));

        linearFirst(logs, "accB");
        linearLast(logs, "accB");

        int idx = binarySearch(logs, "accB");
        if (idx != -1) {
            int count = countOccurrences(logs, "accB");
            System.out.println("Binary " + "accB" + ": index " + idx + ", count=" + count);
        }
    }
}
