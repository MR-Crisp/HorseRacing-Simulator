import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.*;
import java.util.*;

/**
 * Write a description of class Horse here.
 *
 * @AamirAmin
 * @version (a version number or a date)
 */
public class Horse
{
    //Fields of class Horse
    private String name;
    private char symbol;
    private boolean raceParticipation;
    private double confidenceRating;
    private int distance;
    private boolean fallen;
    private double speed;
    private double endurance;
    private String breed;
    private String shoeType;
    private String saddleType;
    private String colour;
    private int racesWon;
    private int totalRaces;


    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, String breed, String shoe, String saddle, String colour) {
        name = horseName;
        symbol = horseSymbol;
        confidenceRating = 0.5;
        raceParticipation = false;//null
        distance = 0;// null
        fallen = false;// null
        this.breed = breed;
        calcBreed();//this sets the speed and endurance
        saddleType = saddle;
        calcSaddle();
        shoeType = shoe;
        calcShoe();
        this.colour = colour;


    }



    private void calcBreed(){
        switch (breed) {
            case "Arabian":
                speed = 0.85;
                endurance = 0.9;
                break;
            case "Thoroughbred":
                speed = 0.95;
                endurance = 0.7;
                break;
            case "Chick Hicks":
                speed = 0.7;
                endurance = 0.8;
                break;
            case "Lightning McQueen":
                speed = 1.0;  //speed, i am speed
                endurance = 0.6;
                break;
            default:
                speed = 0.8;
                endurance = 0.8;
                break;
        }

    }

    private void calcSaddle(){
        switch (saddleType) {
            case "Doc Hudson":
                endurance = endurance * 1.3;
                speed = speed * 0.8;
                break;
            case "Western":
                endurance = endurance * 1.2;
                speed = speed * 0.85;
                break;
            case "Racing":
                endurance = endurance * 0.8;
                speed = speed * 1.2;
                break;
            case "Mator":
                endurance = endurance * 0.9;
                speed = speed * 0.9;
                break;
        }
        //cant have them above 1
        if (endurance > 1) {
            endurance = 1.0;
        }
        if (speed > 1) {
            speed = 1.0;
        }

    }

    private void calcShoe(){
        //calculates what the shoe does to different stats
        // Shoe: D,I,L,C
        switch (shoeType){
            case "Diamond":
                endurance = endurance*1.4;
                speed = speed*0.7;
                break;
            case "Iron":
                endurance = endurance*1.3;
                speed = speed*0.8;
                break;
            case "Leather":
                endurance = endurance*1.1;
                speed = speed*1.05;//not 10 percent on purpose
                break;
            case "Chainmail":
                endurance = endurance*1.2;
                speed = speed*0.9;
                break;
        }
        //make sure they arent bigger than 1
        if (endurance>1){
            endurance = 1.0;
        }
        if (speed>1){
            speed = 1;
        }
    }

    public void setRacesWon(int racesWon){
        this.racesWon = racesWon;
    }

    public void setTotalRaces(int totalRaces){
        this.totalRaces = totalRaces;
    }

    public int getRacesWon(){
        return racesWon;
    }

    public int getTotalRaces(){
        return totalRaces;
    }

    //Other methods of class Horse
    public void fall() {
        fallen = true;
    }

    public double getConfidence() {
        return confidenceRating;
    }

    public int getDistanceTravelled() {
        return distance;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }

    public void goBackToStart() {
        distance = 0;
        fallen = false;
    }

    public boolean hasFallen() {
        return fallen;
    }

    public void moveForward() {
        distance += 1;
    }

    public double getSpeed(){
        return speed;
    }

    public double getEndurance(){
        return endurance;
    }

    public void setSpeed(double newSpeed) {
        speed = newSpeed;
    }

    public String getShoeType() {
        return shoeType;
    }

    public String getSaddleType() {
        return saddleType;
    }

    public String getColour() {
        return colour;
    }

    public String getBreed() {
        return breed;
    }

    public void setEndurance(double newEndurance) {
        endurance = newEndurance;
    }

    public void setConfidence(double newConfidence) {

        confidenceRating = Math.floor(newConfidence * 1000) / 1000.0;//makes it 3 decimal place
    }

    public void setSymbol(char newSymbol) {
        symbol = newSymbol;
    }


    public void writeToFile(String filename) {
        Map<String, String> horseDataMap = new LinkedHashMap<>(); // Preserve order

        // Step 1: Read existing data
        File file = new File(filename);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", 2);
                    if (parts.length == 2) {
                        String horseName = parts[0].trim();
                        horseDataMap.put(horseName, line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Step 2: Update or add this horse
        String newHorseData = String.format("%s, %c, %.3f, %.2f, %.2f, %s, %s, %s, %s, %d, %d",
                name, symbol, confidenceRating, speed, endurance, shoeType, saddleType, colour, breed, racesWon, totalRaces);//if gotten to here then increment the total races
        horseDataMap.put(name, newHorseData);

        // Step 3: Write back everything
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : horseDataMap.values()) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
