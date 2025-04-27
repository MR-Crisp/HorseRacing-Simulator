import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Gui extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Race race = new Race(20, 3); // default race class

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
        JPanel trackSettings = createTrackSettingsPanel(screenSize);
        JPanel startRace = createStartingScreenRace();

        cardPanel.add(mainMenu, "MainMenu");
        cardPanel.add(customizeHorses, "customizeHorses");
        cardPanel.add(horseStats, "horseStats");
        cardPanel.add(trackSettings, "trackSettings");
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
        JButton btn5 = new JButton("Start Race");

        btn1.addActionListener(e -> newHorseCreation());
        btn2.addActionListener(e -> {
            cardPanel.remove(cardPanel.getComponent(2)); // index 2 = horseStats
            JPanel updatedHorseStats = createHorseStatsPanel(getSize());
            cardPanel.add(updatedHorseStats, "horseStats");
            cardLayout.show(cardPanel, "horseStats");
        });
        btn3.addActionListener(e -> cardLayout.show(cardPanel, "trackSettings"));
        btn5.addActionListener(e -> cardLayout.show(cardPanel, "startRace"));

        panel.add(btn1);
        panel.add(btn2);
        panel.add(btn3);
        panel.add(btn5);

        return panel;
    }

    private JPanel createViewPanel(String labelText, Dimension screenSize) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(screenSize);

        JLabel label = new JLabel(labelText, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 48));

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 24));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "MainMenu"));

        panel.add(label, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

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

        JTextField horseNameField = new JTextField();
        JComboBox<String> breedBox = new JComboBox<>(new String[]{"Arabian", "Thoroughbred", "Chick Hicks", "Lightning McQueen"});
        JComboBox<String> colorBox = new JComboBox<>(new String[]{"Black", "Brown", "Chestnut", "Gray", "White"});
        JComboBox<String> saddleBox = new JComboBox<>(new String[]{"Doc Hudson", "Western", "Racing", "Mator"});
        JComboBox<String> shoeBox = new JComboBox<>(new String[]{"Diamond", "Iron", "Leather", "Chainmail"});
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
        JButton submitButton = new JButton("Enter");
        JButton backButton = new JButton("Back");

        submitButton.setFont(new Font("Arial", Font.PLAIN, 18));
        backButton.setFont(new Font("Arial", Font.PLAIN, 18));

        submitButton.addActionListener(e -> {
            String horseName = horseNameField.getText();
            String breed = (String) breedBox.getSelectedItem();
            String colour = (String) colorBox.getSelectedItem();
            String saddle = (String) saddleBox.getSelectedItem();
            String shoe = (String) shoeBox.getSelectedItem();
            String symbol = symbolField.getText();

            if (horseName.isEmpty() || symbol.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Horse name and symbol cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            char horseSymbol = symbol.charAt(0);//takes the first symbol

            Horse h = new Horse(horseSymbol, horseName, breed, shoe, saddle, colour);
            h.writeToFile("raceResults.txt");

            JOptionPane.showMessageDialog(this, "Horse created and saved", "Success", JOptionPane.INFORMATION_MESSAGE);

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

    private JPanel createHorseStatsPanel(Dimension screenSize) {
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
            if (horse != null) {
                horseSelector.addItem(horse.getName());
            }
        }

        JTextArea statsArea = new JTextArea(10, 40);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        statsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(statsArea);

        horseSelector.addActionListener(e -> {
            int index = horseSelector.getSelectedIndex();
            if (index >= 0 && index < horses.length && horses[index] != null) {
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
                                "Breed: %s%n" +
                                "Total Races: %d%n" +  // Display total races
                                "Races Won: %d%n",      // Display races won
                        selected.getName(),
                        selected.getSymbol(),
                        selected.getConfidence(),
                        selected.getSpeed(),
                        selected.getEndurance(),
                        selected.getShoeType(),
                        selected.getSaddleType(),
                        selected.getColour(),
                        selected.getBreed(),
                        selected.getTotalRaces(),    // Assuming you have a getter for totalRaces
                        selected.getRacesWon()       // Assuming you have a getter for racesWon
                );
                statsArea.setText(stats);
            }
        });


        if (horseSelector.getItemCount() > 0) {
            horseSelector.setSelectedIndex(0);
        }

        centerPanel.add(new JLabel("Select a Horse:"));
        centerPanel.add(horseSelector);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(scrollPane);

        panel.add(centerPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 18));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "MainMenu"));
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTrackSettingsPanel(Dimension screenSize) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(screenSize);

        JLabel title = new JLabel("Track Settings", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        panel.add(title, BorderLayout.NORTH);

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        // Track configuration section
        JPanel configPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        // Track length
        JLabel lengthLabel = new JLabel("Track Length:");
        JTextField lengthField = new JTextField(String.valueOf(race.getRaceLength()));
        configPanel.add(lengthLabel);
        configPanel.add(lengthField);

        // Number of lanes
        JLabel lanesLabel = new JLabel("Number of Lanes:");
        JTextField lanesField = new JTextField(String.valueOf(race.getLanes()));
        configPanel.add(lanesLabel);
        configPanel.add(lanesField);

        // Weather conditions
        JLabel weatherLabel = new JLabel("Track Surface:");
        String[] weatherOptions = {"Blazing", "Normal", "Raining", "Freezing"};
        JComboBox<String> weatherCombo = new JComboBox<>(weatherOptions);
        weatherCombo.setSelectedItem(race.getWeatherConditions() != null ? race.getWeatherConditions() : "Normal");
        configPanel.add(weatherLabel);
        configPanel.add(weatherCombo);
        contentPanel.add(configPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Lane assignments section
        JPanel lanesPanel = new JPanel();
        lanesPanel.setLayout(new BoxLayout(lanesPanel, BoxLayout.Y_AXIS));
        lanesPanel.setBorder(BorderFactory.createTitledBorder("Assign Horses to Lanes"));

        // Get available horses
        Horse[] availableHorses = readHorsesFromFile("raceResults.txt");

        // Create lane assignment components
        JComboBox<String>[] horseSelectors = new JComboBox[race.getLanes()];

        for (int i = 0; i < race.getLanes(); i++) {
            JPanel lanePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            lanePanel.add(new JLabel("Lane " + (i + 1) + ":"));

            horseSelectors[i] = new JComboBox<>();
            horseSelectors[i].addItem("select");
            for (Horse horse : availableHorses) {
                if (horse != null) {
                    horseSelectors[i].addItem(horse.getName());
                }
            }

            // Preselect if horse already assigned
            if (i < race.getHorseArray().length && race.getHorseArray()[i] != null) {
                horseSelectors[i].setSelectedItem(race.getHorseArray()[i].getName());
            }

            lanePanel.add(horseSelectors[i]);
            lanesPanel.add(lanePanel);
        }

        contentPanel.add(lanesPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Update lanes button
        JButton updateLanesButton = new JButton("Update Lanes");
        updateLanesButton.addActionListener(e -> {
            try {
                int newLaneCount = Integer.parseInt(lanesField.getText());
                race.setLanes(newLaneCount);
                // Rebuild the panel with new lane count
                cardPanel.remove(cardPanel.getComponent(3));
                cardPanel.add(createTrackSettingsPanel(screenSize), "trackSettings");
                cardLayout.show(cardPanel, "trackSettings");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Save settings button
        JButton saveButton = new JButton("Save Settings");
        saveButton.addActionListener(e -> {
            try {
                // Update track length
                race.setRaceLength(Integer.parseInt(lengthField.getText()));

                // Update weather
                race.setWeatherConditions((String) weatherCombo.getSelectedItem());

                // Get selected horses
                Horse[] selectedHorses = new Horse[race.getLanes()];
                for (int i = 0; i < horseSelectors.length; i++) {
                    String selected = (String) horseSelectors[i].getSelectedItem();
                    if (!selected.equals("Horse Selection")) {
                        for (Horse horse : availableHorses) {
                            if (horse != null && horse.getName().equals(selected)) {
                                selectedHorses[i] = horse;
                                break;
                            }
                        }
                    }
                }

                // Assign horses to lanes
                race.assignHorsesToLanes(selectedHorses);

                JOptionPane.showMessageDialog(this, "Settings saved!", "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Back button
        JButton back = new JButton("Back");
        back.addActionListener(e -> cardLayout.show(cardPanel, "MainMenu"));

        buttonPanel.add(updateLanesButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(back);

        contentPanel.add(buttonPanel);

        // Add content to main panel
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStartingScreenRace() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics graphic) {
                super.paintComponent(graphic);
                Graphics2D graphics2DPen = (Graphics2D) graphic;
                graphics2DPen.setStroke(new BasicStroke(2));

                int panelWidth = getWidth();
                int panelHeight = getHeight();

                Horse[] horses = race.getHorseArray();
                int numLanes = race.getLanes();
                int raceLength = race.getRaceLength();

                if (numLanes == 0 || horses == null) return;

                int marginX = (int) (panelWidth * 0.1); // 10% margin on sides
                int marginY = (int) (panelHeight * 0.1); // 10% margin on top/bottom

                int usableWidth = panelWidth - 2 * marginX;
                int usableHeight = panelHeight - 2 * marginY;

                int laneHeight = usableHeight / numLanes;

                // Draw lanes as rectangles
                for (int i = 0; i < numLanes; i++) {
                    int laneTop = marginY + i * laneHeight;

                    // Lane background depending on the weather.
                    switch (race.getWeatherConditions()) {
                        case "Blazing":
                            graphics2DPen.setColor(new Color(255, 36, 0)); // Red for blazing
                            break;
                        case "Normal":
                            graphics2DPen.setColor(new Color(255, 239, 184)); // Beige for normal
                            break;
                        case "Raining":
                            graphics2DPen.setColor(new Color(113, 138, 150)); // Grayish blue for raining
                            break;
                        case "Freezing":
                            graphics2DPen.setColor(new Color(173, 216, 230)); // Light blue for freezing
                            break;
                    }

                    graphics2DPen.fillRect(marginX, laneTop, usableWidth, laneHeight);

                    // Lane boundary
                    graphics2DPen.setColor(Color.BLACK);
                    graphics2DPen.drawRect(marginX, laneTop, usableWidth, laneHeight);

                    // If a horse is assigned to this lane
                    if (i < horses.length && horses[i] != null) {
                        Horse horse = horses[i];
                        int distance = horse.getDistanceTravelled();
                        int x = marginX + Math.min(distance * usableWidth / raceLength, usableWidth);

                        int centerY = laneTop + laneHeight / 2;

                        // Draw the horse name at the start of the lane (fixed position)
                        graphics2DPen.setColor(Color.BLACK);
                        graphics2DPen.setFont(new Font("Arial", Font.BOLD, laneHeight / 15));
                        graphics2DPen.drawString(horse.getName(), marginX - 150, centerY + 5); // Adjust `-80` for name spacing


                        int horseSize = laneHeight / 2;

                        // Set colour for horse depending on the coat color
                        switch (horse.getColour().toLowerCase()) {
                            case "black":
                                graphics2DPen.setColor(Color.BLACK);
                                break;
                            case "brown":
                                graphics2DPen.setColor(new Color(233, 116, 81)); // Burnt sienna
                                break;
                            case "gray":
                                graphics2DPen.setColor(Color.GRAY);
                                break;
                            case "white":
                                graphics2DPen.setColor(Color.WHITE);
                                break;
                            case "chestnut":
                                graphics2DPen.setColor(Color.YELLOW);
                        }

                        // If fallen, draw a black cross where the horse is supposed to be
                        if (horse.hasFallen()) {
                            graphics2DPen.setColor(Color.RED);
                            int size = laneHeight / 3;
                            graphics2DPen.fillOval(x - size, centerY - size, size * 2, size * 2);
                            graphics2DPen.drawLine(x - size, centerY - size, x + size, centerY + size);
                            graphics2DPen.drawLine(x - size, centerY + size, x + size, centerY - size);
                        } else {
                            // Horse shape by breed
                            horseSize = laneHeight / 2;
                            switch (horse.getBreed().toLowerCase()) {
                                case "thoroughbred":
                                    graphics2DPen.fillOval(x, centerY - horseSize / 2, horseSize, horseSize);
                                    break;
                                case "arabian":
                                    graphics2DPen.fillRect(x, centerY - horseSize / 2, horseSize, horseSize);
                                    break;
                                case "chick hicks":
                                    int[] triangleX = {x, x + horseSize / 2, x + horseSize};
                                    int[] triangleY = {centerY + horseSize / 2, centerY - horseSize / 2, centerY + horseSize / 2};
                                    graphics2DPen.fillPolygon(triangleX, triangleY, 3);
                                    break;
                                case "lightning mcqueen":
                                    int[] pentagonX = {x + horseSize / 2, x + horseSize, x + 3 * horseSize / 4, x + horseSize / 4, x};
                                    int[] pentagonY = {centerY - horseSize / 2, centerY, centerY + horseSize / 2, centerY + horseSize / 2, centerY};
                                    graphics2DPen.fillPolygon(pentagonX, pentagonY, 5);
                                    break;
                            }

                            // Draw the horse's symbol inside the shape
                            graphics2DPen.setColor(Color.RED); // Symbol color
                            graphics2DPen.setFont(new Font("Arial", Font.BOLD, horseSize / 3));
                            int symbolX = x + horseSize / 4; // Center the symbol horizontally
                            int symbolY = centerY + horseSize / 4; // Center the symbol vertically
                            graphics2DPen.drawString(String.valueOf(horse.getSymbol()), symbolX, symbolY);
                        }
                    }
                }

                // Draw finish line ONLY from the top of the first lane to the bottom of the last lane
                int finishX = marginX + usableWidth;
                int finishTop = marginY;
                int finishBottom = marginY + laneHeight * numLanes;

                graphics2DPen.setColor(Color.RED);
                graphics2DPen.setStroke(new BasicStroke(4));
                graphics2DPen.drawLine(finishX, finishTop, finishX, finishBottom);
            }
        };

        // Buttons panel
        JPanel btnPanel = new JPanel();
        JButton startBtn = new JButton("Start Race");
        JButton back = new JButton("Go back");

        startBtn.setFont(new Font("Arial", Font.PLAIN, 24));
        back.setFont(new Font("Arial", Font.PLAIN, 24));

        // Race animation
        Timer timer = new Timer(100, e -> {
            boolean finished = race.incrementRace();
            panel.repaint();

            if (finished) {
                ((Timer) e.getSource()).stop();
                StringBuilder winners = new StringBuilder("WINNER:\n");
                Horse[] horses = race.getHorseArray();

                for (Horse h : horses) {
                    if (h != null && h.getDistanceTravelled() >= race.getRaceLength()) {
                        winners.append(h.getName()).append("\n");
                    }
                }

                JOptionPane.showMessageDialog(panel, winners.toString(), "Race Results", JOptionPane.INFORMATION_MESSAGE);

                // Reset horses after OK button is pressed
                for (Horse h : horses) {
                    if (h != null) {
                        h.goBackToStart();
                    }
                }

                startBtn.setEnabled(true);
            }
        });

        startBtn.addActionListener(e -> {
            timer.start();
            startBtn.setEnabled(false);
        });

        back.addActionListener(e -> {
            timer.stop();
            cardLayout.show(cardPanel, "MainMenu");
        });

        btnPanel.add(startBtn);
        btnPanel.add(back);

        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }


    private void newHorseCreation() {
        cardLayout.show(cardPanel, "customizeHorses");
    }

    public static Horse[] readHorsesFromFile(String filename) {
        int count = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            while (reader.readLine() != null) count++;
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

                if (parts.length == 11) {
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

                    horses[i] = new Horse(symbol, name, breed, shoe, saddle, colour);
                    horses[i].setTotalRaces(totalRaces);
                    horses[i].setRacesWon(racesWon);
                    i++;//increment
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return horses;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Gui::new);
    }
}