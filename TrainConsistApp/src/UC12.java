import java.util.*;
import java.io.*;

public class UC12 {

    // ===== UC7: Bogie =====
    static class Bogie {
        String name;
        int capacity;

        Bogie(String name, int capacity) {
            this.name = name;
            this.capacity = capacity;
        }

        public int getCapacity() {
            return capacity;
        }

        public String getName() {
            return name;
        }

        public String toString() {
            return name + " -> " + capacity;
        }
    }

    // ===== UC11: Booking Request =====
    static class BookingRequest {
        String guestName;
        String roomType;

        BookingRequest(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    // ===== UC11: Queue =====
    static class BookingRequestQueue {
        private Queue<BookingRequest> queue = new LinkedList<>();

        public void addRequest(BookingRequest request) {
            queue.add(request);
        }

        public BookingRequest getRequest() {
            return queue.poll();
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    // ===== UC11 + UC12: Inventory =====
    static class RoomInventory {
        Map<String, Integer> rooms = new HashMap<>();

        public RoomInventory() {
            rooms.put("Single", 5);
            rooms.put("Double", 3);
            rooms.put("Suite", 2);
        }

        public boolean allocate(String type) {
            int count = rooms.getOrDefault(type, 0);
            if (count > 0) {
                rooms.put(type, count - 1);
                return true;
            }
            return false;
        }

        public void display() {
            System.out.println("\nCurrent Inventory:");
            for (String type : rooms.keySet()) {
                System.out.println(type + ": " + rooms.get(type));
            }
        }
    }

    // ===== UC11: Allocation =====
    static class RoomAllocationService {
        public void allocateRoom(BookingRequest req, RoomInventory inventory) {
            if (inventory.allocate(req.roomType)) {
                System.out.println("Booking confirmed for Guest: "
                        + req.guestName + ", Room Type: " + req.roomType);
            } else {
                System.out.println("No rooms available for " + req.guestName);
            }
        }
    }

    // ===== UC11: Thread =====
    static class ConcurrentBookingProcessor implements Runnable {

        private BookingRequestQueue queue;
        private RoomInventory inventory;
        private RoomAllocationService service;

        public ConcurrentBookingProcessor(BookingRequestQueue q,
                                          RoomInventory i,
                                          RoomAllocationService s) {
            this.queue = q;
            this.inventory = i;
            this.service = s;
        }

        public void run() {
            while (true) {

                BookingRequest req;

                synchronized (queue) {
                    if (queue.isEmpty()) break;
                    req = queue.getRequest();
                }

                synchronized (inventory) {
                    service.allocateRoom(req, inventory);
                }
            }
        }
    }

    // ===== UC12: File Persistence =====
    static class FilePersistenceService {

        public void saveInventory(RoomInventory inventory, String filePath) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

                for (Map.Entry<String, Integer> e : inventory.rooms.entrySet()) {
                    writer.write(e.getKey() + "=" + e.getValue());
                    writer.newLine();
                }

                System.out.println("Inventory saved successfully.");

            } catch (Exception e) {
                System.out.println("Error saving inventory.");
            }
        }

        public void loadInventory(RoomInventory inventory, String filePath) {
            File file = new File(filePath);

            if (!file.exists()) {
                System.out.println("No valid inventory data found. Starting fresh.");
                return;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

                String line;
                while ((line = reader.readLine()) != null) {

                    String[] parts = line.split("=");

                    if (parts.length == 2) {
                        inventory.rooms.put(parts[0], Integer.parseInt(parts[1]));
                    }
                }

                System.out.println("Inventory loaded successfully.");

            } catch (Exception e) {
                System.out.println("Error loading inventory.");
            }
        }
    }

    // ===== MAIN METHOD =====
    public static void main(String[] args) {

        // ===== UC7: Sorting =====
        System.out.println("=== UC7 Sorting ===");

        List<Bogie> bogies = new ArrayList<>();
        bogies.add(new Bogie("Sleeper", 72));
        bogies.add(new Bogie("AC Chair", 56));
        bogies.add(new Bogie("First Class", 24));

        bogies.sort(Comparator.comparingInt(b -> b.capacity));

        for (Bogie b : bogies) {
            System.out.println(b);
        }

        // ===== UC11: Concurrent Booking =====
        System.out.println("\n=== UC11 Concurrent Booking ===");

        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService service = new RoomAllocationService();

        queue.addRequest(new BookingRequest("Abhi", "Single"));
        queue.addRequest(new BookingRequest("Vanmathi", "Double"));
        queue.addRequest(new BookingRequest("Kural", "Suite"));
        queue.addRequest(new BookingRequest("Subha", "Single"));

        Thread t1 = new Thread(new ConcurrentBookingProcessor(queue, inventory, service));
        Thread t2 = new Thread(new ConcurrentBookingProcessor(queue, inventory, service));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (Exception e) {
            System.out.println("Thread interrupted");
        }

        inventory.display();

        // ===== UC12: Persistence =====
        System.out.println("\n=== UC12 Persistence ===");

        FilePersistenceService persistence = new FilePersistenceService();
        String file = "inventory.txt";

        persistence.loadInventory(inventory, file);
        inventory.display();
        persistence.saveInventory(inventory, file);
    }
}