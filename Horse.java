import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


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

      
    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, String breed, String shoe, String saddle, String colour) {
        name = horseName;
        symbol = horseSymbol;
        confidenceRating = 0.5;
        raceParticipation = false; //null
        distance = 0;              // null
        fallen = false;            // null
        speed = 0.5;
        endurance = 0.5;
        this.breed = breed;
        saddleType = saddle;
        shoeType = shoe;
        this.colour = colour;
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


    // Write Horse information to a file in the desired format
    public void writeToFile(String filename) {
        // Open file in append mode
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            // Format: name, symbol, confidence, speed, endurance, shoe, saddle, colour, breed
            writer.write(String.format("%s, %c, %.3f, %.2f, %.2f, %s, %s, %s, %s%n",
                    name, symbol, confidenceRating, speed, endurance, shoeType, saddleType, colour, breed));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
