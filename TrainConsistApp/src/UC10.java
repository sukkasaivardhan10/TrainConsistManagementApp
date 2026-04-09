import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UC10 {


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
        public String toString() {
            return name + " -> " + capacity;
        }
        public String getName() {
            return name;
        }

    }

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("=== UC7 Sort Bogies by Capacity (Comparator) ===");
        System.out.println("=================================\n");


        List<Bogie> bogies = new ArrayList<>();


        bogies.add(new Bogie("Sleeper", 72));
        bogies.add(new Bogie("AC Chair", 56));
        bogies.add(new Bogie("First Class", 24));


        System.out.println("Before Sorting:");
        for (Bogie b : bogies) {
            System.out.println(b);
        }


        bogies.sort(Comparator.comparingInt(b -> b.capacity));


        System.out.println("\nAfter Sorting (by capacity):");
        for (Bogie b : bogies) {
            System.out.println(b);
        }


        System.out.println("\nUC7 sorting completed...");
    }
}