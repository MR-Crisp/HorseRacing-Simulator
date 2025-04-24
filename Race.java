import java.util.concurrent.TimeUnit;
import java.io.*;
import java.util.*;

public class Race {
    private int raceLength;
    private int lanes;
    private Horse[] horseArray;
    private String weatherConditions;

    public Race(int distance, int horseLanes) {
        raceLength = distance;
        lanes = horseLanes;
        horseArray = new Horse[lanes];
    }

    public void setRaceLength(int distance) {
        raceLength = distance;
    }

    public void setLanes(int lanes) {
        this.lanes = lanes;
    }

    public void setWeatherConditions(String weather) {
        weatherConditions = weather;
    }

    public void addHorse(Horse theHorse, int laneNumber) {
        if (laneNumber > lanes || laneNumber < 1) {
            System.out.println("Lane number out of range");
            return;
        }
        horseArray[laneNumber - 1] = theHorse;
    }

    public void startRace() {
        boolean finished = false;

        for (int i = 0; i < lanes; i++) {
            if (horseArray[i] != null) {
                horseArray[i].goBackToStart();
            }
        }

        while (!finished) {
            for (int i = 0; i < lanes; i++) {
                if (horseArray[i] != null) {
                    moveHorse(horseArray[i]);
                }
            }

            printRace();

            for (int i = 0; i < lanes; i++) {
                if (horseArray[i] != null && raceWonBy(horseArray[i])) {
                    finished = true;
                }
            }

            if (!finished) {
                finished = true;
                for (int i = 0; i < lanes; i++) {
                    if (horseArray[i] != null && !horseArray[i].hasFallen()) {
                        finished = false;
                    }
                }
            }

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception e) {
            }
        }

        changeHorseConfidence();
        saveRaceResults("raceResults.txt");
    }

    private void moveHorse(Horse theHorse) {
        if (!theHorse.hasFallen()) {
            double chanceToMove = theHorse.getConfidence() * theHorse.getSpeed();
            if (Math.random() < chanceToMove) {
                theHorse.moveForward();
            }

            double fallProbability = 0.1 * theHorse.getConfidence() * theHorse.getConfidence() / theHorse.getEndurance();
            if (Math.random() < fallProbability) {
                theHorse.fall();
            }
        }
    }

    private boolean raceWonBy(Horse theHorse) {
        return theHorse.getDistanceTravelled() >= raceLength;
    }

    private void printRace() {
        System.out.print('\u000C');
        multiplePrint('=', raceLength + 3);
        System.out.println();

        for (int i = 0; i < lanes; i++) {
            if (horseArray[i] != null) {
                printLane(horseArray[i]);
                System.out.println();
            } else {
                printEmptyLane();
            }
        }

        multiplePrint('=', raceLength + 3);
        System.out.println();
    }

    private void printLane(Horse theHorse) {
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();
        System.out.print('|');
        multiplePrint(' ', spacesBefore);
        System.out.print(theHorse.hasFallen() ? '\u274C' : theHorse.getSymbol());
        multiplePrint(' ', spacesAfter);
        System.out.print('|');
    }

    private void printEmptyLane() {
        System.out.print('|');
        multiplePrint(' ', raceLength + 1);
        System.out.print('|');
        System.out.println();
    }

    private void multiplePrint(char aChar, int times) {
        for (int i = 0; i < times; i++) {
            System.out.print(aChar);
        }
    }

    private void saveRaceResults(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Horse horse : horseArray) {
                if (horse != null) {
                    // Save extended info (same format as Horse.writeToFile)
                    writer.write(String.format("%s, %c, %.3f, %.2f, %.2f, %s, %s, %s, %s%n",
                            horse.getName(), horse.getSymbol(), horse.getConfidence(),
                            horse.getSpeed(), horse.getEndurance(),
                            horse.getShoeType(), horse.getSaddleType(), horse.getColour(), horse.getBreed()));
                }
            }
            System.out.println("Horse data saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error writing horse data: " + e.getMessage());
        }
    }

    private void changeHorseConfidence() {
        for (int i = 0; i < lanes; i++) {
            Horse horse = horseArray[i];
            if (horse != null) {
                double currentConfidence = horse.getConfidence();
                if (horse.hasFallen()) {
                    currentConfidence *= 0.85;
                } else if (horse.getDistanceTravelled() >= raceLength) {
                    currentConfidence *= 1.2;
                } else {
                    double proportion = (double) horse.getDistanceTravelled() / raceLength;
                    currentConfidence *= 1 + (proportion * 0.1);
                }
                horse.setConfidence(Math.min(1.0, currentConfidence));
            }
        }
    }

    public Horse[] getHorseArray() {
        return horseArray;
    }

    public Horse getWinner() {
        Horse[] horses = getHorseArray();
        for (Horse horse : horses) {
            if (horse != null && horse.getDistanceTravelled() >= raceLength) {
                return horse;
            }
        }
        return null;  // Return null if no horse has won yet
    }

}
