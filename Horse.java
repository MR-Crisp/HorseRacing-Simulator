
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

      
    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence) {
        name = horseName;
        symbol = horseSymbol;
        confidenceRating = horseConfidence;
        raceParticipation = false; //null
        distance = 0;              // null
        fallen = false;            // null
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

    public void setConfidence(double newConfidence) {
        confidenceRating = newConfidence;
    }
    
    public void setSymbol(char newSymbol) {
        symbol = newSymbol;
    }

}
