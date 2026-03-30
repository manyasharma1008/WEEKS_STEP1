import java.util.*;

class Asset {
    String name;
    double returnRate;
    double volatility;

    Asset(String name, double returnRate, double volatility) {
        this.name = name;
        this.returnRate = returnRate;
        this.volatility = volatility;
    }

    public String toString() {
        return name + ":" + returnRate + "% (vol=" + volatility + ")";
    }
}

public class PortfolioReturnSorting {
    public static void mergeSort(Asset[] assets, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(assets, left, mid);
            mergeSort(assets, mid + 1, right);
            merge(assets, left, mid, right);
        }
    }

    private static void merge(Asset[] assets, int left, int mid, int right) {
        Asset[] temp = new Asset[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        while (i <= mid && j <= right) {
            if (assets[i].returnRate <= assets[j].returnRate) {
                temp[k++] = assets[i++];
            } else {
                temp[k++] = assets[j++];
            }
        }
        while (i <= mid) temp[k++] = assets[i++];
        while (j <= right) temp[k++] = assets[j++];
        System.arraycopy(temp, 0, assets, left, temp.length);
    }

    public static void quickSort(Asset[] assets, int low, int high) {
        if (low < high) {
            int pi = partition(assets, low, high);
            quickSort(assets, low, pi - 1);
            quickSort(assets, pi + 1, high);
        }
    }

    private static int partition(Asset[] assets, int low, int high) {
        Asset pivot = assets[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (assets[j].returnRate > pivot.returnRate ||
                    (assets[j].returnRate == pivot.returnRate && assets[j].volatility < pivot.volatility)) {
                i++;
                Asset temp = assets[i];
                assets[i] = assets[j];
                assets[j] = temp;
            }
        }
        Asset temp = assets[i + 1];
        assets[i + 1] = assets[high];
        assets[high] = temp;
        return i + 1;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of assets:");
        int n = sc.nextInt();
        Asset[] assets = new Asset[n];

        for (int i = 0; i < n; i++) {
            System.out.println("Enter asset name:");
            String name = sc.next();
            System.out.println("Enter return rate (%):");
            double rate = sc.nextDouble();
            System.out.println("Enter volatility:");
            double vol = sc.nextDouble();
            assets[i] = new Asset(name, rate, vol);
        }

        Asset[] mergeBatch = Arrays.copyOf(assets, n);
        mergeSort(mergeBatch, 0, n - 1);
        System.out.println("Merge: " + Arrays.toString(mergeBatch));

        Asset[] quickBatch = Arrays.copyOf(assets, n);
        quickSort(quickBatch, 0, n - 1);
        System.out.println("Quick (desc): " + Arrays.toString(quickBatch));
    }
}
