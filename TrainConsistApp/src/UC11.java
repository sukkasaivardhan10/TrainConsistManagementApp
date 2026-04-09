import java.util.*;

public class UC11{


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



    static class BookingRequest {
        String guestName;
        String roomType;

        BookingRequest(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

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

    static class RoomInventory {
        private Map<String, Integer> rooms = new HashMap<>();

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
            System.out.println("\nRemaining Inventory:");
            for (String type : rooms.keySet()) {
                System.out.println(type + ": " + rooms.get(type));
            }
        }
    }

    static class RoomAllocationService {
        public void allocateRoom(BookingRequest request, RoomInventory inventory) {
            boolean success = inventory.allocate(request.roomType);

            if (success) {
                System.out.println("Booking confirmed for Guest: "
                        + request.guestName + ", Room Type: " + request.roomType);
            } else {
                System.out.println("No rooms available for " + request.guestName);
            }
        }
    }

    static class ConcurrentBookingProcessor implements Runnable {

        private BookingRequestQueue bookingQueue;
        private RoomInventory inventory;
        private RoomAllocationService allocationService;

        public ConcurrentBookingProcessor(
                BookingRequestQueue bookingQueue,
                RoomInventory inventory,
                RoomAllocationService allocationService) {

            this.bookingQueue = bookingQueue;
            this.inventory = inventory;
            this.allocationService = allocationService;
        }

        @Override
        public void run() {
            while (true) {

                BookingRequest request;

                // synchronized queue access
                synchronized (bookingQueue) {
                    if (bookingQueue.isEmpty()) {
                        break;
                    }
                    request = bookingQueue.getRequest();
                }

                // synchronized inventory update
                synchronized (inventory) {
                    allocationService.allocateRoom(request, inventory);
                }
            }
        }
    }



    public static void main(String[] args) {


        System.out.println("=== UC7 Sort Bogies ===");

        List<Bogie> bogies = new ArrayList<>();
        bogies.add(new Bogie("Sleeper", 72));
        bogies.add(new Bogie("AC Chair", 56));
        bogies.add(new Bogie("First Class", 24));

        System.out.println("Before Sorting:");
        for (Bogie b : bogies) {
            System.out.println(b);
        }

        bogies.sort(Comparator.comparingInt(b -> b.capacity));

        System.out.println("\nAfter Sorting:");
        for (Bogie b : bogies) {
            System.out.println(b);
        }


        System.out.println("\n=== UC11 Concurrent Booking Simulation ===");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();


        bookingQueue.addRequest(new BookingRequest("Abhi", "Single"));
        bookingQueue.addRequest(new BookingRequest("Vanmathi", "Double"));
        bookingQueue.addRequest(new BookingRequest("Kural", "Suite"));
        bookingQueue.addRequest(new BookingRequest("Subha", "Single"));


        Thread t1 = new Thread(new ConcurrentBookingProcessor(
                bookingQueue, inventory, allocationService));

        Thread t2 = new Thread(new ConcurrentBookingProcessor(
                bookingQueue, inventory, allocationService));


        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }


        inventory.display();
    }
}