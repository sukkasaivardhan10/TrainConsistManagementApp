import java.util.HashMap;
import java.util.Map;

public class UC6 {
    public static void main(String[] args) {


        HashMap<String, Integer> bogieCapacity = new HashMap<>();


        bogieCapacity.put("Sleeper", 72);
        bogieCapacity.put("AC Chair", 54);
        bogieCapacity.put("First Class", 24);


        System.out.println("Bogie Capacity Details:");

        for (Map.Entry<String, Integer> entry : bogieCapacity.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}