import java.util.ArrayList;

public class UC2 {
    public static void main(String[] args) {
        ArrayList<String> passengerBogies = new ArrayList<>();

        passengerBogies.add("Sleeper");
        passengerBogies.add("AC Chair");
        passengerBogies.add("First Class");

        System.out.println(passengerBogies);

        passengerBogies.remove("AC Chair");

        System.out.println(passengerBogies.contains("Sleeper"));

        System.out.println(passengerBogies);
    }
}