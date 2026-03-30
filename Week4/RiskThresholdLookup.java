import java.util.*;

public class RiskThresholdLookup {
    public static int linearSearch(int[] risks, int target) {
        int comparisons = 0;
        for (int i = 0; i < risks.length; i++) {
            comparisons++;
            if (risks[i] == target) {
                System.out.println("Linear: threshold=" + target + " → found at index " + i + " (" + comparisons + " comps)");
                return i;
            }
        }
        System.out.println("Linear: threshold=" + target + " → not found (" + comparisons + " comps)");
        return -1;
    }

    public static void binaryFloorCeiling(int[] risks, int target) {
        int low = 0, high = risks.length - 1;
        int floor = Integer.MIN_VALUE, ceiling = Integer.MAX_VALUE;
        int comparisons = 0;

        while (low <= high) {
            int mid = (low + high) / 2;
            comparisons++;
            if (risks[mid] == target) {
                floor = risks[mid];
                ceiling = risks[mid];
                break;
            } else if (risks[mid] < target) {
                floor = risks[mid];
                low = mid + 1;
            } else {
                ceiling = risks[mid];
                high = mid - 1;
            }
        }

        String floorStr = (floor == Integer.MIN_VALUE) ? "none" : String.valueOf(floor);
        String ceilStr = (ceiling == Integer.MAX_VALUE) ? "none" : String.valueOf(ceiling);

        System.out.println("Binary floor(" + target + "): " + floorStr + ", ceiling: " + ceilStr + " (" + comparisons + " comps)");
    }

    public static void main(String[] args) {
        int[] risks = {10, 25, 50, 100};
        Arrays.sort(risks);
        System.out.println("Sorted risks: " + Arrays.toString(risks));

        linearSearch(risks, 30);
        binaryFloorCeiling(risks, 30);
    }
}
