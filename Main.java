import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("What distance do you want the race to be?");
        int distance = Integer.parseInt(scanner.nextLine());

        System.out.println("How many lanes do you want?");
        int lanes = Integer.parseInt(scanner.nextLine());

        Race race = new Race(distance, lanes);

        // Load existing horses from file
        Map<String, Horse> savedHorses = loadSavedHorses("raceResults.txt");

        for (int laneNumber = 1; laneNumber <= lanes; laneNumber++) {
            System.out.println("\nLane " + laneNumber + ":");
            System.out.print("Do you want to use a saved horse? (yes/no): ");
            String choice = scanner.nextLine().trim().toLowerCase();

            Horse selectedHorse;
            if (choice.equals("yes") && !savedHorses.isEmpty()) {
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

                System.out.print("Enter horse breed: ");
                String breed = scanner.nextLine();

                System.out.print("Enter horse shoe type: ");
                String shoe = scanner.nextLine();

                System.out.print("Enter horse saddle type: ");
                String saddle = scanner.nextLine();

                System.out.print("Enter horse colour: ");
                String colour = scanner.nextLine();

                selectedHorse = new Horse(symbol, name, breed, shoe, saddle, colour);
            }

            race.addHorse(selectedHorse, laneNumber);
        }

        race.startRace();

        // Save all horses back to file (merging new ones with old ones)
        for (Horse h : race.getHorseArray()) {
            if (h != null) {
                savedHorses.put(h.getName(), h); // overwrite or add new
            }
        }
        saveHorses(savedHorses, "raceResults.txt");
    }

    private static Map<String, Horse> loadSavedHorses(String filename) {
        Map<String, Horse> horses = new LinkedHashMap<>();
        File file = new File(filename);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 9) {
                        String name = parts[0].trim();
                        char symbol = parts[1].trim().charAt(0);
                        double confidence = Double.parseDouble(parts[2].trim());
                        double speed = Double.parseDouble(parts[3].trim());
                        double endurance = Double.parseDouble(parts[4].trim());
                        String shoe = parts[5].trim();
                        String saddle = parts[6].trim();
                        String colour = parts[7].trim();
                        String breed = parts[8].trim();

                        Horse h = new Horse(symbol, name, breed, shoe, saddle, colour);
                        h.setConfidence(confidence);
                        h.setSpeed(speed);
                        h.setEndurance(endurance);

                        horses.put(name, h);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading horses: " + e.getMessage());
            }
        }
        return horses;
    }

    private static void saveHorses(Map<String, Horse> horses, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Horse h : horses.values()) {
                writer.write(String.join(",",
                        h.getName(),
                        String.valueOf(h.getSymbol()),
                        String.valueOf(h.getConfidence()),
                        String.valueOf(h.getSpeed()),
                        String.valueOf(h.getEndurance()),
                        h.getShoeType(),
                        h.getSaddleType(),
                        h.getColour(),
                        h.getBreed()
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving horses: " + e.getMessage());
        }
    }
}
