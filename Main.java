import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create a race of length 20 and 3 lanes
        Race race = new Race(20, 4);

        // Load existing horses from file
        Map<String, Horse> savedHorses = loadSavedHorses("raceResults.txt");

        for (int lane = 1; lane <= 3; lane++) {
            System.out.println("Lane " + lane + ":");
            System.out.print("Do you want to use a saved horse? (yes/no): ");
            String choice = scanner.nextLine().trim().toLowerCase();

            Horse selectedHorse;
            if (choice.equals("yes") && !savedHorses.isEmpty()) {
                // Show saved horses
                List<String> horseNames = new ArrayList<>(savedHorses.keySet());
                for (int i = 0; i < horseNames.size(); i++) {
                    Horse h = savedHorses.get(horseNames.get(i));
                    System.out.println((i + 1) + ". " + h.getName() + " (Symbol: " + h.getSymbol() + ", Confidence: " + h.getConfidence() + ")");
                }

                System.out.print("Enter the number of the horse you want to select: ");
                int selection = Integer.parseInt(scanner.nextLine());
                selectedHorse = savedHorses.get(horseNames.get(selection - 1));

            } else {
                // Create new horse
                System.out.print("Enter horse name: ");
                String name = scanner.nextLine();

                System.out.print("Enter horse symbol (single character): ");
                char symbol = scanner.nextLine().charAt(0);

                System.out.print("Enter horse confidence (0.0 - 1.0): ");
                double confidence = Double.parseDouble(scanner.nextLine());

                selectedHorse = new Horse(symbol, name, confidence);
            }

            race.addHorse(selectedHorse, lane);
        }

        race.startRace();
    }

    private static Map<String, Horse> loadSavedHorses(String filename) {
        Map<String, Horse> horses = new LinkedHashMap<>();
        File file = new File(filename);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String name = parts[0].trim();
                        char symbol = parts[1].trim().charAt(0);
                        double confidence = Double.parseDouble(parts[2].trim());
                        horses.put(name, new Horse(symbol, name, confidence));
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading horses: " + e.getMessage());
            }
        }
        return horses;
    }
}
