public class Test {
    public static void main(String[] args) {
        testConstructorAndGetters();
        testSetConfidenceTruncation();
        testMoveForward();
        testFallAndReset();
        testSetSymbol();
    }

    public static void testConstructorAndGetters() {
        Horse horse = new Horse('H', "Blaze", 0.789456);
        System.out.println("== testConstructorAndGetters ==");
        System.out.println("Expected Name: Blaze, Actual: " + horse.getName());
        System.out.println("Expected Symbol: H, Actual: " + horse.getSymbol());
        System.out.println("Expected Confidence: 0.789456, Actual: " + horse.getConfidence());
        System.out.println();
    }

    public static void testSetConfidenceTruncation() {
        Horse horse = new Horse('S', "Storm", 0.999999);
        horse.setConfidence(0.999999);
        System.out.println("== testSetConfidenceTruncation ==");
        System.out.println("Expected Confidence: 0.999, Actual: " + horse.getConfidence());
        System.out.println();
    }

    public static void testMoveForward() {
        Horse horse = new Horse('T', "Thunder", 0.5);
        horse.moveForward();
        horse.moveForward();
        System.out.println("== testMoveForward ==");
        System.out.println("Expected Distance: 2, Actual: " + horse.getDistanceTravelled());
        System.out.println();
    }

    public static void testFallAndReset() {
        Horse horse = new Horse('L', "Lightning", 0.5);
        horse.moveForward();
        horse.fall();
        System.out.println("== testFallAndReset ==");
        System.out.println("Expected Fallen: true, Actual: " + horse.hasFallen());
        horse.goBackToStart();
        System.out.println("Expected Distance after reset: 0, Actual: " + horse.getDistanceTravelled());
        System.out.println("Expected Fallen after reset: false, Actual: " + horse.hasFallen());
        System.out.println();
    }

    public static void testSetSymbol() {
        Horse horse = new Horse('X', "Shadow", 0.5);
        horse.setSymbol('Z');
        System.out.println("== testSetSymbol ==");
        System.out.println("Expected Symbol: Z, Actual: " + horse.getSymbol());
        System.out.println();
    }
}
