import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.io.*;
import java.util.*;

public class Race
{
    private int raceLength;
    private int lanes;
    private Horse[] horseArray;

    public Race(int distance, int horseLanes)
    {
        // initialise instance variables
        raceLength = distance;
        lanes = horseLanes;
        horseArray = new Horse[lanes];
    }

    public void setRaceLength(int distance){
        raceLength = distance;
    }

    public void setLanes(int lanes){
        this.lanes = lanes;
    }

    public void assignHorses() {

    }

    public void addHorse(Horse theHorse, int laneNumber)
    {
        if (laneNumber > (lanes+1)){
            System.out.println("Lane number too high");
            return;
        }
        else{
        horseArray[laneNumber-1] = theHorse;
        }
    }
    

    public void startRace()
    {
        //declare a local variable to tell us when the race is finished
        boolean finished = false;
        
        //reset all the lanes (all horses not fallen and back to 0). 
        for (int i = 0; i < lanes; i++){
            if (horseArray[i]!=null) {
                horseArray[i].goBackToStart();
            }
        }
                      
        while (!finished) {
            //move each horse
            for (int i = 0; i < lanes; i++){
                if (horseArray[i]!=null){
                    moveHorse(horseArray[i]);
                }
            }
                        
            //print the race positions
            printRace();


            //if any of the three horses has won the race is finished
            for (int i = 0; i < lanes; i++){
                if (horseArray[i]!=null){
                    if (raceWonBy(horseArray[i])){
                        finished = true;
                    }
                }
            }


            //if all horses have fallen then race is finished
            if (!finished){
                finished = true; // Assume all have fallen
                for (int i = 0; i < lanes; i++) {
                    if (horseArray[i]!=null) {
                        if (!horseArray[i].hasFallen()) {
                            finished = false; // As soon as one hasn't fallen, race isn't finished
                        }
                    }
                }//end race while loop

            }





//            //wait for 100 milliseconds
//            try{
//                TimeUnit.MILLISECONDS.sleep(100);
//            }catch(Exception e){}
        }
        changeHorseConfidence();
        saveRaceResults( "raceResults.txt");
    }
    

    private void moveHorse(Horse theHorse) {
        //if the horse has fallen it cannot move, 
        //so only run if it has not fallen
        if  (!theHorse.hasFallen())
        {
            //the probability that the horse will move forward depends on the confidence;
            if (Math.random() < theHorse.getConfidence())
            {
               theHorse.moveForward();
            }
            
            //the probability that the horse will fall is very small (max is 0.1)
            //but will also will depends exponentially on confidence 
            //so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < (0.1*theHorse.getConfidence()*theHorse.getConfidence()))
            {
                theHorse.fall();
            }
        }
    }
        

    private boolean raceWonBy(Horse theHorse)
    {
        if (theHorse.getDistanceTravelled() == raceLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    

    private void printRace()
    {
        System.out.print('\u000C');  //clear the terminal window
        
        multiplePrint('=',raceLength+3); //top edge of track
        System.out.println();

        for (int i=0; i<lanes;i++){
            if (horseArray[i]!=null){
                printLane(horseArray[i]);
                System.out.println();
            }
            else {
                printEmptyLane();
            }
        }

        multiplePrint('=',raceLength+3); //bottom edge of track
        System.out.println();    
    }
    

    private void printLane(Horse theHorse)
    {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();
        
        //print a | for the beginning of the lane
        System.out.print('|');
        
        //print the spaces before the horse
        multiplePrint(' ',spacesBefore);
        
        //if the horse has fallen then print dead
        //else print the horse's symbol
        if(theHorse.hasFallen())
        {
            System.out.print('\u274C');
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }
        
        //print the spaces after the horse
        multiplePrint(' ',spacesAfter);
        
        //print the | for the end of the track
        System.out.print('|');
    }

    private void printEmptyLane(){
        System.out.print('|');
        multiplePrint(' ',raceLength+1);
        System.out.print('|');
        System.out.println();
    }

    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }

    private void saveRaceResults(String filename) {
        Map<String, Horse> existingHorses = new LinkedHashMap<>();

        // Step 1: Read existing horses from file
        File file = new File(filename);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String name = parts[0];
                        char symbol = parts[1].charAt(0);
                        double confidence = Double.parseDouble(parts[2]);
                        existingHorses.put(name, new Horse(symbol, name, confidence));
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading horse data: " + e.getMessage());
            }
        }

        // Step 2: Update or add current race horses
        for (int i = 0; i < lanes; i++) {
            Horse horse = horseArray[i];
            if (horse != null) {
                existingHorses.put(horse.getName(), horse); // replaces or adds
            }
        }

        // Step 3: Write back to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Horse horse : existingHorses.values()) {
                writer.write(horse.getName() + "," + horse.getSymbol() + "," + horse.getConfidence());
                writer.newLine();
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
                    currentConfidence *= 0.5; // decrease by 50%
                } else if (horse.getDistanceTravelled() >= raceLength) {
                    currentConfidence *= 1.2; // increase by 20%
                } else {
                    double proportionTravelled = (double) horse.getDistanceTravelled() / raceLength;
                    currentConfidence *= 1 + (proportionTravelled * 0.1); // up to 10% increase
                }

                // cap confidence at 1
                if (currentConfidence > 1) {
                    currentConfidence = 1;
                }

                horse.setConfidence(currentConfidence);
            }
        }
    }





}
