import java.io.*;

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

    public int getRaceLength(){
        return raceLength;
    }

    public String getWeatherConditions() {
        return weatherConditions;
    }

    public void setRaceLength(int distance) {
        raceLength = distance;
    }

    public void setLanes(int lanes) {
        this.lanes = lanes;
        this.horseArray = new Horse[lanes];
    }

    public int getLanes(){
        return lanes;
    }

    public void setWeatherConditions(String weather) {
        weatherConditions = weather;
    }

    private void moveHorse(Horse theHorse) {
        if (!theHorse.hasFallen()) {
            double chanceToMove = (theHorse.getConfidence() + theHorse.getSpeed())/2 ;//still between 0 and 1
            if (Math.random() < chanceToMove) {
                theHorse.moveForward();
            }

            double baseProbability = 0.07;//7 percent chance cause 10 too high
            if (weatherConditions != null) {
                switch (weatherConditions.toLowerCase()) {
                    case "freezing":
                        baseProbability *= 1.6;
                        break;
                    case "raining":
                        baseProbability *= 1.3;
                        break;
                    case "blazing":
                        baseProbability *= 0.7;//reduce fall
                        break;
                    //no need for normal cause as stays same
                }
            }

            double fallProbability = baseProbability * Math.pow(theHorse.getConfidence(),2) / theHorse.getEndurance();//proportional to confidence but inverse to endurance
            if (Math.random() < fallProbability) {
                theHorse.fall();
            }
        }
    }

    private void saveRaceResults(Horse[] horseArray) {
        for (Horse horse: horseArray){
            if (horse!=null) {
                horse.writeToFile("raceResults.txt");
            }
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

    public void assignHorsesToLanes(Horse[] horses) {
        if (horses.length != lanes) {
            System.out.println("Number of horses does not match number of lanes.");
            return;
        }

        for (int i = 0; i < lanes; i++) {
            horseArray[i] = horses[i]; // Assign horses to lanes based on the array passed
        }
    }

    public boolean incrementRace() {
        // Move all horses first
        for (Horse horse : horseArray) {
            if (horse != null) {
                moveHorse(horse);
            }
        }

        // Check if any horse has won
        for (Horse horse : horseArray) {
            if (horse != null && horse.getDistanceTravelled() >= raceLength) {
                horse.setRacesWon(horse.getRacesWon()+1);//plus 1 races one
                for(Horse allHorses: horseArray){
                    if (horse!=null) {
                        allHorses.setTotalRaces(horse.getTotalRaces() + 1);//increment total races for horse
                    }
                }
                horse.setTotalRaces(horse.getTotalRaces()+1);
                changeHorseConfidence();//race over and change horse confidence
                saveRaceResults(horseArray);
                return true;
            }
        }

        // Check if all horses have fallen
        int numberOfHorses = 0;
        int numberOfFallenHorses = 0;

        for (Horse horse : horseArray) {
            if (horse != null) {
                numberOfHorses++;
                if (horse.hasFallen()) {
                    numberOfFallenHorses++;
                }
            }
        }

        // If all horses have fallen and there are horses in the race
        if (numberOfHorses > 0 && numberOfHorses == numberOfFallenHorses) {
            changeHorseConfidence();//race over and change confidence
            for (Horse horse: horseArray){
                if (horse != null) {//make sure there is horse in lane
                    horse.setTotalRaces(horse.getTotalRaces() + 1);//increment total races for each horse
                }
            }
            saveRaceResults(horseArray);
            return true;
        }

        return false;
    }


}