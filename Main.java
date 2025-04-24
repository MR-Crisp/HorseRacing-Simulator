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
                if (selection >= 1 && selection <= horseNames.size()) {
                    selectedHorse = savedHorses.get(horseNames.get(selection - 1));
                } else {
                    System.out.println("Invalid selection. Creating a new horse.");
                    selectedHorse = createNewHorse(scanner);
                }
            } else {
                // Create new horse
                selectedHorse = createNewHorse(scanner);
            }

            race.addHorse(selectedHorse, laneNumber);
        }

        race.startRace();

        // Update stats after race
        Horse winner = race.getWinner();
        for (Horse h : race.getHorseArray()) {
            if (h != null) {
                h.setTotalRaces(h.getTotalRaces() + 1);
                if (h == winner) {
                    h.setRacesWon(h.getRacesWon() + 1);
                }
                savedHorses.put(h.getName(), h);
            }
        }

        saveHorses(savedHorses, "raceResults.txt");
    }

    private static Horse createNewHorse(Scanner scanner) {
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

        return new Horse(symbol, name, breed, shoe, saddle, colour);
    }

    private static Map<String, Horse> loadSavedHorses(String filename) {
        Map<String, Horse> horses = new LinkedHashMap<>();
        File file = new File(filename);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 11) {
                        try {
                            String name = parts[0].trim();
                            char symbol = parts[1].trim().charAt(0);
                            double confidence = Double.parseDouble(parts[2].trim());
                            double speed = Double.parseDouble(parts[3].trim());
                            double endurance = Double.parseDouble(parts[4].trim());
                            String shoe = parts[5].trim();
                            String saddle = parts[6].trim();
                            String colour = parts[7].trim();
                            String breed = parts[8].trim();
                            int racesWon = Integer.parseInt(parts[9].trim());
                            int totalRaces = Integer.parseInt(parts[10].trim());

                            Horse h = new Horse(symbol, name, breed, shoe, saddle, colour);
                            h.setConfidence(confidence);
                            h.setSpeed(speed);
                            h.setEndurance(endurance);
                            h.setRacesWon(racesWon);
                            h.setTotalRaces(totalRaces);

                            horses.put(name, h);
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing data for horse: " + parts[0]);
                        }
                    } else {
                        System.err.println("Incorrect data format in file: " + Arrays.toString(parts));
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
                writer.write(String.format("%s, %c, %.3f, %.2f, %.2f, %s, %s, %s, %s, %d, %d%n",
                        h.getName(),
                        h.getSymbol(),
                        h.getConfidence(),
                        h.getSpeed(),
                        h.getEndurance(),
                        h.getShoeType(),
                        h.getSaddleType(),
                        h.getColour(),
                        h.getBreed(),
                        h.getRacesWon(),
                        h.getTotalRaces()));
            }
        } catch (IOException e) {
            System.err.println("Error saving horses: " + e.getMessage());
        }
    }
}
