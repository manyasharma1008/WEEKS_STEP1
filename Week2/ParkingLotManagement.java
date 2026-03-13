import java.util.*;

class ParkingSpot {
    String licensePlate;
    long entryTime;
    boolean occupied;

    ParkingSpot() {
        this.licensePlate = null;
        this.entryTime = 0;
        this.occupied = false;
    }
}

public class ParkingLotManagement {
    private ParkingSpot[] spots;
    private int capacity;
    private int occupiedCount;
    private int totalProbes;
    private Map<String, Long> exitDurations = new HashMap<>();

    public ParkingLotManagement(int capacity) {
        this.capacity = capacity;
        this.spots = new ParkingSpot[capacity];
        for (int i = 0; i < capacity; i++) {
            spots[i] = new ParkingSpot();
        }
    }

    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }

    public String parkVehicle(String licensePlate) {
        int index = hash(licensePlate);
        int probes = 0;
        while (spots[index].occupied) {
            probes++;
            index = (index + 1) % capacity;
        }
        spots[index].licensePlate = licensePlate;
        spots[index].entryTime = System.currentTimeMillis();
        spots[index].occupied = true;
        occupiedCount++;
        totalProbes += probes;
        return "Assigned spot #" + index + " (" + probes + " probes)";
    }

    public String exitVehicle(String licensePlate) {
        int index = hash(licensePlate);
        int probes = 0;
        while (spots[index].occupied && !spots[index].licensePlate.equals(licensePlate)) {
            probes++;
            index = (index + 1) % capacity;
        }
        if (spots[index].occupied && spots[index].licensePlate.equals(licensePlate)) {
            long duration = System.currentTimeMillis() - spots[index].entryTime;
            double hours = duration / 3600000.0;
            double fee = hours * 5.0; // $5 per hour
            spots[index].occupied = false;
            spots[index].licensePlate = null;
            occupiedCount--;
            exitDurations.put(licensePlate, duration);
            return "Spot #" + index + " freed, Duration: " + String.format("%.2f", hours) + "h, Fee: $" + String.format("%.2f", fee);
        }
        return "Vehicle not found";
    }

    public String getStatistics() {
        double occupancy = (occupiedCount * 100.0) / capacity;
        double avgProbes = occupiedCount == 0 ? 0 : (totalProbes * 1.0 / occupiedCount);
        return "Occupancy: " + String.format("%.1f", occupancy) + "%, Avg Probes: " + String.format("%.2f", avgProbes);
    }

    public static void main(String[] args) throws InterruptedException {
        ParkingLotManagement lot = new ParkingLotManagement(500);

        System.out.println(lot.parkVehicle("ABC-1234"));
        System.out.println(lot.parkVehicle("ABC-1235"));
        System.out.println(lot.parkVehicle("XYZ-9999"));

        Thread.sleep(2000); // simulate parking duration
        System.out.println(lot.exitVehicle("ABC-1234"));

        System.out.println(lot.getStatistics());
    }
}