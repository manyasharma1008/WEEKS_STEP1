import java.util.*;

class Trade {
    String id;
    int volume;

    Trade(String id, int volume) {
        this.id = id;
        this.volume = volume;
    }

    public String toString() {
        return id + ":" + volume;
    }
}

public class TradeVolumeAnalysis {
    public static void mergeSort(Trade[] trades, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(trades, left, mid);
            mergeSort(trades, mid + 1, right);
            merge(trades, left, mid, right);
        }
    }

    private static void merge(Trade[] trades, int left, int mid, int right) {
        Trade[] temp = new Trade[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        while (i <= mid && j <= right) {
            if (trades[i].volume <= trades[j].volume) {
                temp[k++] = trades[i++];
            } else {
                temp[k++] = trades[j++];
            }
        }
        while (i <= mid) temp[k++] = trades[i++];
        while (j <= right) temp[k++] = trades[j++];
        System.arraycopy(temp, 0, trades, left, temp.length);
    }

    public static void quickSort(Trade[] trades, int low, int high) {
        if (low < high) {
            int pi = partition(trades, low, high);
            quickSort(trades, low, pi - 1);
            quickSort(trades, pi + 1, high);
        }
    }

    private static int partition(Trade[] trades, int low, int high) {
        int pivot = trades[high].volume;
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (trades[j].volume > pivot) {
                i++;
                Trade temp = trades[i];
                trades[i] = trades[j];
                trades[j] = temp;
            }
        }
        Trade temp = trades[i + 1];
        trades[i + 1] = trades[high];
        trades[high] = temp;
        return i + 1;
    }

    public static Trade[] mergeLists(Trade[] morning, Trade[] afternoon) {
        Trade[] merged = new Trade[morning.length + afternoon.length];
        int i = 0, j = 0, k = 0;
        while (i < morning.length && j < afternoon.length) {
            if (morning[i].volume <= afternoon[j].volume) {
                merged[k++] = morning[i++];
            } else {
                merged[k++] = afternoon[j++];
            }
        }
        while (i < morning.length) merged[k++] = morning[i++];
        while (j < afternoon.length) merged[k++] = afternoon[j++];
        return merged;
    }

    public static int totalVolume(Trade[] trades) {
        int sum = 0;
        for (Trade t : trades) sum += t.volume;
        return sum;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of trades:");
        int n = sc.nextInt();
        Trade[] trades = new Trade[n];
        for (int i = 0; i < n; i++) {
            System.out.println("Enter trade id:");
            String id = sc.next();
            System.out.println("Enter volume:");
            int vol = sc.nextInt();
            trades[i] = new Trade(id, vol);
        }

        Trade[] mergeBatch = Arrays.copyOf(trades, n);
        mergeSort(mergeBatch, 0, n - 1);
        System.out.println("MergeSort: " + Arrays.toString(mergeBatch) + " // Stable");

        Trade[] quickBatch = Arrays.copyOf(trades, n);
        quickSort(quickBatch, 0, n - 1);
        System.out.println("QuickSort (desc): " + Arrays.toString(quickBatch));

        Trade[] morning = { new Trade("trade1", 100), new Trade("trade2", 300) };
        Trade[] afternoon = { new Trade("trade3", 500) };
        Trade[] merged = mergeLists(morning, afternoon);
        System.out.println("Merged morning+afternoon total: " + totalVolume(merged));
    }
}
