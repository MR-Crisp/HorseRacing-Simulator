import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        // Create a race of length 20
        Race race = new Race(0);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter race length: ");
        race.setRaceLength(scanner.nextInt());

        System.out.println("Enter amount of lanes: ");
        race.setLanes(scanner.nextInt());



        // Create three horses with different symbols, names, and confidence levels
        Horse horse1 = new Horse('A', "Thunder", 0.6);
        Horse horse2 = new Horse('B', "Lightning", 0.5);
        Horse horse3 = new Horse('C', "Storm", 0.5);

        // Add horses to the race
        race.addHorse(horse1, 1);
        race.addHorse(horse2, 2);
        race.addHorse(horse3, 3);

        // Start the race
        race.startRace();
    }
}
