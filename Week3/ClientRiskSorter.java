import java.util.*;

class Client {
    String id;
    int riskScore;
    double accountBalance;

    Client(String id, int riskScore, double accountBalance) {
        this.id = id;
        this.riskScore = riskScore;
        this.accountBalance = accountBalance;
    }

    public String toString() {
        return id + ":" + riskScore + " (bal=" + accountBalance + ")";
    }
}

public class ClientRiskSorter {
    public static void bubbleSort(Client[] clients) {
        int n = clients.length;
        int swaps = 0;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (clients[j].riskScore > clients[j + 1].riskScore) {
                    Client temp = clients[j];
                    clients[j] = clients[j + 1];
                    clients[j + 1] = temp;
                    swaps++;
                }
            }
        }
        System.out.println("Bubble (asc): " + Arrays.toString(clients) + " // Swaps: " + swaps);
    }

    public static void insertionSort(Client[] clients) {
        for (int i = 1; i < clients.length; i++) {
            Client key = clients[i];
            int j = i - 1;
            while (j >= 0 && (clients[j].riskScore < key.riskScore ||
                    (clients[j].riskScore == key.riskScore && clients[j].accountBalance < key.accountBalance))) {
                clients[j + 1] = clients[j];
                j--;
            }
            clients[j + 1] = key;
        }
        System.out.println("Insertion (desc): " + Arrays.toString(clients));
    }

    public static void topRisks(Client[] clients, int k) {
        System.out.print("Top " + k + " risks: ");
        for (int i = 0; i < Math.min(k, clients.length); i++) {
            System.out.print(clients[i].id + "(" + clients[i].riskScore + ") ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of clients:");
        int n = sc.nextInt();
        Client[] clients = new Client[n];

        for (int i = 0; i < n; i++) {
            System.out.println("Enter client id:");
            String id = sc.next();
            System.out.println("Enter risk score:");
            int risk = sc.nextInt();
            System.out.println("Enter account balance:");
            double bal = sc.nextDouble();
            clients[i] = new Client(id, risk, bal);
        }

        Client[] bubbleBatch = Arrays.copyOf(clients, n);
        bubbleSort(bubbleBatch);

        Client[] insertionBatch = Arrays.copyOf(clients, n);
        insertionSort(insertionBatch);
        topRisks(insertionBatch, 10);
    }
}
