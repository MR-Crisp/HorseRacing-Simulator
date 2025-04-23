import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Gui extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Race race = new Race(20,5);//default race class that can be changed later through settings

    public Gui() {
        setTitle("Game Start Screen");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel mainMenu = createMainMenu(screenSize);
        JPanel customizeHorses = createCustomizeHorsesPanel(screenSize);
        JPanel horseStats = createHorseStatsPanel(screenSize);
        JPanel trackSettings = createTrackSettingsPanel( screenSize);
        JPanel betting = createViewPanel("betting", screenSize);
        JPanel startRace = createViewPanel("StartRace", screenSize);

        cardPanel.add(mainMenu, "MainMenu");
        cardPanel.add(customizeHorses, "customizeHorses");
        cardPanel.add(horseStats, "horseStats");
        cardPanel.add(trackSettings, "trackSettings");
        cardPanel.add(betting, "betting");
        cardPanel.add(startRace, "startRace");

        add(cardPanel);
        setVisible(true);
    }

    private JPanel createMainMenu(Dimension screenSize) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(screenSize);
        panel.setLayout(new GridLayout(5, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));

        JButton btn1 = new JButton("New Horse");
        JButton btn2 = new JButton("Horse Stats");
        JButton btn3 = new JButton("Track settings");
        JButton btn4 = new JButton("Betting");
        JButton btn5 = new JButton("Start Race");

        btn1.addActionListener(e -> newHorseCreation());
        btn2.addActionListener(e -> cardLayout.show(cardPanel, "horseStats"));
        btn3.addActionListener(e -> cardLayout.show(cardPanel, "trackSettings"));
        btn4.addActionListener(e -> cardLayout.show(cardPanel, "betting"));
        btn5.addActionListener(e -> cardLayout.show(cardPanel, "startRace"));

        panel.add(btn1);
        panel.add(btn2);
        panel.add(btn3);
        panel.add(btn4);
        panel.add(btn5);

        return panel;
    }

    private JPanel createViewPanel(String labelText, Dimension screenSize) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(screenSize);

        JLabel label = new JLabel(labelText, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 48));

        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Arial", Font.PLAIN, 24));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "MainMenu"));

        panel.add(label, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createHorseStatsPanel(Dimension screenSize){
        JPanel panel = new JPanel();
        panel.setPreferredSize(screenSize);
        panel.setLayout(new BorderLayout());

        JLabel title = new JLabel("Horse Stats", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        panel.add(title, BorderLayout.NORTH);

        Horse[] horses = readHorsesFromFile("raceResults.txt");

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        JComboBox<String> horseSelector = new JComboBox<>();
        for (Horse horse : horses) {
            horseSelector.addItem(horse.getName());
        }

        JTextArea statsArea = new JTextArea(10, 40);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        statsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(statsArea);

        horseSelector.addActionListener(e -> {
            int index = horseSelector.getSelectedIndex();
            if (index >= 0) {
                Horse selected = horses[index];
                String stats = String.format(
                        "Name: %s%n" +
                                "Symbol: %c%n" +
                                "Confidence: %.3f%n" +
                                "Speed: %.2f%n" +
                                "Endurance: %.2f%n" +
                                "Shoe: %s%n" +
                                "Saddle: %s%n" +
                                "Colour: %s%n" +
                                "Breed: %s%n",
                        selected.getName(),
                        selected.getSymbol(),
                        selected.getConfidence(),
                        selected.getSpeed(),
                        selected.getEndurance(),
                        selected.getShoeType(),
                        selected.getSaddleType(),
                        selected.getColour(),
                        selected.getBreed()
                );
                statsArea.setText(stats);
            }
        });

        if (horses.length > 0) {
            horseSelector.setSelectedIndex(0);
        }

        centerPanel.add(new JLabel("Select a Horse:"));
        centerPanel.add(horseSelector);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(scrollPane);

        panel.add(centerPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Arial", Font.PLAIN, 18));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "MainMenu"));
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }


    private JPanel createTrackSettingsPanel(Dimension screenSize) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(screenSize);
        panel.setLayout(new BorderLayout());

        // Title Label
        JLabel title = new JLabel("Track Settings", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        panel.add(title, BorderLayout.NORTH);

        // Panel for the input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2)); // Updated grid layout to 4x2 (including the button)

        // Label and Input for first field (Track Length)
        JLabel trackLengthLabel = new JLabel("Track Length: ");
        JTextField trackLength = new JTextField();
        inputPanel.add(trackLengthLabel);
        inputPanel.add(trackLength);

        // Label and Input for second field (Track Lanes)
        JLabel trackLanesLabel = new JLabel("Track Lanes: ");
        JTextField trackLanes = new JTextField();
        inputPanel.add(trackLanesLabel);
        inputPanel.add(trackLanes);

        // Label and ComboBox for the drop-down field (Track Surface)
        JLabel trackConditionsLabel = new JLabel("Track Surface: ");
        String[] trackConditions = { "Blazing", "Normal", "Raining", "Freezing" }; // 4 options
        JComboBox<String> trackSurfaceComboBox = new JComboBox<>(trackConditions);
        inputPanel.add(trackConditionsLabel);
        inputPanel.add(trackSurfaceComboBox);

        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get values from inputs
                String trackLengthValue = trackLength.getText();
                String trackLanesValue = trackLanes.getText();
                String trackSurfaceValue = (String) trackSurfaceComboBox.getSelectedItem();

                // Check if race object exists, if not, create a new one
                if (race == null) {
                    race = new Race(Integer.parseInt(trackLengthValue), Integer.parseInt(trackLanesValue));
                } else {
                    // Update the existing Race object
                    race.setWeatherConditions(trackSurfaceValue);
                    race.setRaceLength(Integer.parseInt(trackLengthValue));
                    race.setLanes(Integer.parseInt(trackLanesValue));
                }
                

                // Reset fields
                trackLength.setText("");
                trackLanes.setText("");
                trackSurfaceComboBox.setSelectedIndex(0); // Reset to first option
            }
        });

        // Add the submit button to the panel
        inputPanel.add(submitButton);
        // Add input panel to the center of the main panel
        panel.add(inputPanel, BorderLayout.CENTER);

        return panel;
    }



    private JPanel createCustomizeHorsesPanel(Dimension screenSize) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(screenSize);
        panel.setLayout(new BorderLayout());

        JLabel title = new JLabel("Customize Horses", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        panel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        String[] labels = {
                "Horse Name:", "Breed:", "Color:", "Saddle:", "Horse Shoe type:", "Symbol:"
        };

        JTextField horseNameField = new JTextField();
        JComboBox<String> breedBox = new JComboBox<>(new String[] {"Arabian", "Thoroughbred", "Chick Hicks", "Lightning McQueen"});
        JComboBox<String> colorBox = new JComboBox<>(new String[] {"Black", "Brown", "Chestnut", "Gray", "White"});
        JComboBox<String> saddleBox = new JComboBox<>(new String[] {"Doc Hudson", "Western", "Racing", "Mator"});
        JComboBox<String> shoeBox = new JComboBox<>(new String[] {"Diamond", "Iron", "Leather", "Chainmail"});
        JTextField symbolField = new JTextField();

        formPanel.add(new JLabel("Horse Name:"));
        formPanel.add(horseNameField);
        formPanel.add(new JLabel("Breed:"));
        formPanel.add(breedBox);
        formPanel.add(new JLabel("Color:"));
        formPanel.add(colorBox);
        formPanel.add(new JLabel("Saddle:"));
        formPanel.add(saddleBox);
        formPanel.add(new JLabel("Horse Shoe type:"));
        formPanel.add(shoeBox);
        formPanel.add(new JLabel("Symbol:"));
        formPanel.add(symbolField);

        panel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back to Menu");

        submitButton.setFont(new Font("Arial", Font.PLAIN, 18));
        backButton.setFont(new Font("Arial", Font.PLAIN, 18));

        submitButton.addActionListener(e -> {
            StringBuilder result = new StringBuilder("Created New Horse:\n");
            result.append("Horse Name: ").append(horseNameField.getText()).append("\n");
            result.append("Breed: ").append(breedBox.getSelectedItem()).append("\n");
            result.append("Color: ").append(colorBox.getSelectedItem()).append("\n");
            result.append("Saddle: ").append(saddleBox.getSelectedItem()).append("\n");
            result.append("Horse Shoe type: ").append(shoeBox.getSelectedItem()).append("\n");
            result.append("Symbol: ").append(symbolField.getText()).append("\n");
            String horseName = horseNameField.getText();
            String breed = (String) breedBox.getSelectedItem();
            String colour = (String) colorBox.getSelectedItem();
            String saddle = (String) saddleBox.getSelectedItem();
            String shoe = (String) shoeBox.getSelectedItem();
            String symbol = symbolField.getText();
            char horseSymbol = symbol.charAt(0);
            Horse h = new Horse(horseSymbol, horseName, breed, shoe, saddle, colour);
            h.writeToFile("raceResults.txt");

            JOptionPane.showMessageDialog(this, result.toString(), "Horse Info", JOptionPane.INFORMATION_MESSAGE);

            horseNameField.setText("");
            breedBox.setSelectedIndex(0);
            colorBox.setSelectedIndex(0);
            saddleBox.setSelectedIndex(0);
            shoeBox.setSelectedIndex(0);
            symbolField.setText("");
        });

        backButton.addActionListener(e -> cardLayout.show(cardPanel, "MainMenu"));

        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void newHorseCreation() {
        cardLayout.show(cardPanel, "customizeHorses");
    }

    public static Horse[] readHorsesFromFile(String filename) {
        int count = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            while (reader.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Horse[0];
        }

        Horse[] horses = new Horse[count];

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int i = 0;

            while ((line = reader.readLine()) != null && i < count) {
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

                    horses[i++] = h;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Horse[0];
        }

        return horses;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Gui::new);
    }
}
