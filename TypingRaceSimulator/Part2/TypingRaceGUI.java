package TypingRaceSimulator.Part2;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Part II graphical typing race. Start with startRaceGUI().
 */
public class TypingRaceGUI extends JFrame
{
    private JComboBox<String> passageChoice;
    private JTextArea customPassage;
    private JSlider seatCountSlider;
    private JCheckBox autocorrectBox;
    private JCheckBox caffeineBox;
    private JCheckBox nightShiftBox;
    private JComboBox<String>[] styleBoxes;
    private JComboBox<String>[] keyboardBoxes;
    private JComboBox<String>[] accessoryBoxes;
    private JTextField[] nameFields;
    private JTextField[] symbolFields;
    private JComboBox<String>[] colourBoxes;
    private JPanel racePanel;
    private JTextArea statsArea;
    private JTextArea leaderboardArea;
    private JTextArea comparisonArea;
    private ArrayList<GuiTypist> typists;
    private ArrayList<JProgressBar> progressBars;
    private ArrayList<JLabel> passageLabels;
    private ArrayList<RaceResult> history;
    private Timer timer;
    private String passage;
    private int turn;
    private boolean raceRunning;

    private static final String SHORT_PASSAGE = "The quick brown fox jumps over the lazy dog.";
    private static final String MEDIUM_PASSAGE = "Object oriented programming helps programmers model real world systems clearly.";
    private static final String LONG_PASSAGE = "Typing races reward speed, accuracy, consistency and calm decision making under pressure.";

    public TypingRaceGUI()
    {
        super("Typing Race Simulator - Part II");
        typists = new ArrayList<GuiTypist>();
        progressBars = new ArrayList<JProgressBar>();
        passageLabels = new ArrayList<JLabel>();
        history = new ArrayList<RaceResult>();
        buildInterface();
    }

    public void startRaceGUI()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 760);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildInterface()
    {
        setLayout(new BorderLayout(8, 8));
        add(buildConfigurationPanel(), BorderLayout.WEST);
        add(buildRacePanel(), BorderLayout.CENTER);
        add(buildResultsPanel(), BorderLayout.EAST);
    }

    private JPanel buildConfigurationPanel()
    {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Race configuration"));

        JPanel top = new JPanel(new GridLayout(0, 1, 4, 4));
        passageChoice = new JComboBox<String>(new String[] {"Short", "Medium", "Long", "Custom"});
        customPassage = new JTextArea(3, 18);
        customPassage.setLineWrap(true);
        customPassage.setText("Custom typing passages can be entered here.");
        seatCountSlider = new JSlider(2, 6, 3);
        seatCountSlider.setMajorTickSpacing(1);
        seatCountSlider.setPaintLabels(true);
        seatCountSlider.setPaintTicks(true);
        autocorrectBox = new JCheckBox("Autocorrect: halves slide back");
        caffeineBox = new JCheckBox("Caffeine: early boost, later burnout risk");
        nightShiftBox = new JCheckBox("Night Shift: reduces accuracy");

        top.add(new JLabel("Passage"));
        top.add(passageChoice);
        top.add(new JScrollPane(customPassage));
        top.add(new JLabel("Typists: 2-6"));
        top.add(seatCountSlider);
        top.add(autocorrectBox);
        top.add(caffeineBox);
        top.add(nightShiftBox);

        panel.add(top, BorderLayout.NORTH);
        panel.add(buildTypistOptions(), BorderLayout.CENTER);

        JButton start = new JButton("Start Race");
        start.addActionListener((ActionEvent e) -> startConfiguredRace());
        panel.add(start, BorderLayout.SOUTH);
        return panel;
    }

    @SuppressWarnings("unchecked")
    private JPanel buildTypistOptions()
    {
        JPanel panel = new JPanel(new GridLayout(6, 1, 4, 4));
        styleBoxes = new JComboBox[6];
        keyboardBoxes = new JComboBox[6];
        accessoryBoxes = new JComboBox[6];
        nameFields = new JTextField[6];
        symbolFields = new JTextField[6];
        colourBoxes = new JComboBox[6];

        String[] styles = {"Touch Typist", "Hunt & Peck", "Phone Thumbs", "Voice-to-Text"};
        String[] keyboards = {"Mechanical", "Membrane", "Touchscreen", "Stenography"};
        String[] accessories = {"None", "Wrist Support", "Energy Drink", "Noise-Cancelling Headphones"};
        String[] colours = {"Blue", "Green", "Orange", "Red", "Purple", "Black"};
        String[] defaultSymbols = {"①", "②", "③", "④", "⑤", "⑥"};

        for (int i = 0; i < 6; i++)
        {
            JPanel row = new JPanel(new GridLayout(0, 1));
            row.setBorder(BorderFactory.createTitledBorder("Typist " + (i + 1)));
            nameFields[i] = new JTextField("Typist_" + (i + 1));
            symbolFields[i] = new JTextField(defaultSymbols[i]);
            styleBoxes[i] = new JComboBox<String>(styles);
            keyboardBoxes[i] = new JComboBox<String>(keyboards);
            accessoryBoxes[i] = new JComboBox<String>(accessories);
            colourBoxes[i] = new JComboBox<String>(colours);
            colourBoxes[i].setSelectedIndex(i);
            row.add(nameFields[i]);
            row.add(symbolFields[i]);
            row.add(styleBoxes[i]);
            row.add(keyboardBoxes[i]);
            row.add(accessoryBoxes[i]);
            row.add(colourBoxes[i]);
            panel.add(row);
        }
        return panel;
    }

    private JPanel buildRacePanel()
    {
        racePanel = new JPanel(new GridLayout(6, 1, 6, 6));
        racePanel.setBorder(BorderFactory.createTitledBorder("Live race"));
        return racePanel;
    }

    private JPanel buildResultsPanel()
    {
        JPanel panel = new JPanel(new GridLayout(3, 1, 6, 6));
        statsArea = makeArea("Statistics and analytics");
        leaderboardArea = makeArea("Leaderboard and badges");
        comparisonArea = makeArea("Comparison view");
        panel.add(new JScrollPane(statsArea));
        panel.add(new JScrollPane(leaderboardArea));
        panel.add(new JScrollPane(comparisonArea));
        return panel;
    }

    private JTextArea makeArea(String title)
    {
        JTextArea area = new JTextArea();
        area.setBorder(BorderFactory.createTitledBorder(title));
        area.setEditable(false);
        return area;
    }

    private void startConfiguredRace()
    {
        if (timer != null)
        {
            timer.stop();
        }
        typists.clear();
        progressBars.clear();
        passageLabels.clear();
        racePanel.removeAll();
        turn = 0;
        raceRunning = true;
        passage = selectedPassage();
        int seats = seatCountSlider.getValue();

        for (int i = 0; i < seats; i++)
        {
            GuiTypist typist = createTypist(i);
            typists.add(typist);
            racePanel.add(createLane(typist));
        }
        racePanel.revalidate();
        racePanel.repaint();
        updateAnalyticsText("Race started. Attribute impacts are applied from style, keyboard, accessory and global modifiers.");

        timer = new Timer(180, (ActionEvent e) -> runTurn());
        timer.start();
    }

    private String selectedPassage()
    {
        String choice = (String) passageChoice.getSelectedItem();
        if ("Short".equals(choice)) return SHORT_PASSAGE;
        if ("Medium".equals(choice)) return MEDIUM_PASSAGE;
        if ("Long".equals(choice)) return LONG_PASSAGE;
        if (customPassage.getText().trim().length() == 0) return SHORT_PASSAGE;
        return customPassage.getText().trim();
    }

    private GuiTypist createTypist(int index)
    {
        GuiTypist t = new GuiTypist();
        t.name = nameFields[index].getText().trim();
        if (t.name.length() == 0) t.name = "Typist_" + (index + 1);
        String symbol = symbolFields[index].getText();
        t.symbol = symbol.length() == 0 ? ("" + (index + 1)) : symbol.substring(0, 1);
        t.colour = colourFor((String) colourBoxes[index].getSelectedItem());
        t.style = (String) styleBoxes[index].getSelectedItem();
        t.keyboard = (String) keyboardBoxes[index].getSelectedItem();
        t.accessory = (String) accessoryBoxes[index].getSelectedItem();
        t.accuracy = 0.62;
        t.speed = 1;
        t.burnoutRisk = 0.025;
        applyStyle(t);
        applyKeyboard(t);
        applyAccessory(t);
        if (nightShiftBox.isSelected()) t.accuracy -= 0.08;
        t.clampAccuracy();
        return t;
    }

    private void applyStyle(GuiTypist t)
    {
        if ("Touch Typist".equals(t.style)) { t.accuracy += 0.18; t.speed += 1; t.burnoutRisk += 0.020; }
        else if ("Hunt & Peck".equals(t.style)) { t.accuracy -= 0.10; t.burnoutRisk -= 0.010; }
        else if ("Phone Thumbs".equals(t.style)) { t.accuracy -= 0.04; t.speed += 1; t.burnoutRisk += 0.005; }
        else if ("Voice-to-Text".equals(t.style)) { t.accuracy += 0.08; t.speed += 2; t.burnoutRisk += 0.030; }
    }

    private void applyKeyboard(GuiTypist t)
    {
        if ("Mechanical".equals(t.keyboard)) { t.accuracy += 0.06; t.speed += 1; }
        else if ("Membrane".equals(t.keyboard)) { t.accuracy -= 0.01; }
        else if ("Touchscreen".equals(t.keyboard)) { t.accuracy -= 0.05; t.burnoutRisk -= 0.005; }
        else if ("Stenography".equals(t.keyboard)) { t.accuracy += 0.10; t.speed += 2; t.burnoutRisk += 0.025; }
    }

    private void applyAccessory(GuiTypist t)
    {
        if ("Wrist Support".equals(t.accessory)) t.burnoutDuration = 2;
        else if ("Energy Drink".equals(t.accessory)) { t.energyDrink = true; t.burnoutRisk += 0.015; }
        else if ("Noise-Cancelling Headphones".equals(t.accessory)) t.mistypeReduction = 0.08;
    }

    private JPanel createLane(GuiTypist t)
    {
        JPanel lane = new JPanel(new BorderLayout(4, 4));
        lane.setBorder(BorderFactory.createLineBorder(t.colour));
        JLabel info = new JLabel(t.symbol + " " + t.name + " | " + t.style + ", " + t.keyboard + ", " + t.accessory);
        JProgressBar bar = new JProgressBar(0, passage.length());
        bar.setStringPainted(true);
        JLabel text = new JLabel(highlightedPassage(t), SwingConstants.LEFT);
        progressBars.add(bar);
        passageLabels.add(text);
        lane.add(info, BorderLayout.NORTH);
        lane.add(bar, BorderLayout.CENTER);
        lane.add(text, BorderLayout.SOUTH);
        return lane;
    }

    private void runTurn()
    {
        if (!raceRunning) return;
        turn++;
        for (GuiTypist t : typists)
        {
            advance(t);
        }
        refreshRaceDisplay();
        GuiTypist winner = findWinner();
        if (winner != null)
        {
            finishRace(winner);
        }
    }

    private void advance(GuiTypist t)
    {
        if (t.finished) return;
        if (t.burnoutRemaining > 0)
        {
            t.burnoutRemaining--;
            return;
        }
        double currentAccuracy = t.accuracy;
        if (caffeineBox.isSelected() && turn <= 10) currentAccuracy += 0.10;
        if (t.energyDrink && t.progress < passage.length() / 2) currentAccuracy += 0.08;
        if (t.energyDrink && t.progress >= passage.length() / 2) currentAccuracy -= 0.06;
        if (currentAccuracy < 0.0) currentAccuracy = 0.0;
        if (currentAccuracy > 1.0) currentAccuracy = 1.0;

        int attempts = t.speed;
        if (caffeineBox.isSelected() && turn <= 10) attempts++;
        for (int i = 0; i < attempts && !t.finished; i++)
        {
            t.keystrokes++;
            if (Math.random() < currentAccuracy)
            {
                t.correctKeystrokes++;
                t.progress++;
                if (t.progress >= passage.length())
                {
                    t.finished = true;
                    t.finishTurn = turn;
                }
            }
            else
            {
                t.mistypes++;
                int slide = autocorrectBox.isSelected() ? 1 : 2;
                if (Math.random() < 0.30 - t.mistypeReduction)
                {
                    t.progress -= slide;
                    if (t.progress < 0) t.progress = 0;
                }
            }
        }
        double risk = t.burnoutRisk;
        if (caffeineBox.isSelected() && turn > 10) risk += 0.035;
        if (Math.random() < risk)
        {
            t.burnoutCount++;
            t.burnoutRemaining = t.burnoutDuration;
            t.accuracy -= 0.01;
            t.clampAccuracy();
        }
    }

    private void refreshRaceDisplay()
    {
        for (int i = 0; i < typists.size(); i++)
        {
            GuiTypist t = typists.get(i);
            progressBars.get(i).setValue(Math.min(t.progress, passage.length()));
            progressBars.get(i).setString(t.name + " " + t.progress + "/" + passage.length() + (t.burnoutRemaining > 0 ? " BURNT OUT" : ""));
            passageLabels.get(i).setText(highlightedPassage(t));
        }
    }

    private String highlightedPassage(GuiTypist t)
    {
        int position = Math.min(t.progress, passage.length());
        String done = escape(passage.substring(0, position));
        String rest = escape(passage.substring(position));
        return "<html><span style='background:#d0ffd0;'>" + done + "</span><span>" + t.symbol + "</span>" + rest + "</html>";
    }

    private String escape(String value)
    {
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private GuiTypist findWinner()
    {
        for (GuiTypist t : typists)
        {
            if (t.finished) return t;
        }
        return null;
    }

    private void finishRace(GuiTypist winner)
    {
        raceRunning = false;
        timer.stop();
        int position = 1;
        ArrayList<GuiTypist> ordered = new ArrayList<GuiTypist>(typists);
        Collections.sort(ordered, new Comparator<GuiTypist>() {
            public int compare(GuiTypist a, GuiTypist b) { return b.progress - a.progress; }
        });
        for (GuiTypist t : ordered)
        {
            int points = Math.max(0, 7 - position * 2);
            points += calculateWpm(t) >= 35 ? 2 : 0;
            points -= t.burnoutCount;
            if (points < 0) points = 0;
            t.points += points;
            t.consecutiveWins = t == winner ? t.consecutiveWins + 1 : 0;
            t.racesWithoutBurnout = t.burnoutCount == 0 ? t.racesWithoutBurnout + 1 : 0;
            t.bestWpm = Math.max(t.bestWpm, calculateWpm(t));
            history.add(new RaceResult(t.name, position, calculateWpm(t), accuracyPercentage(t), t.burnoutCount));
            position++;
        }
        winner.accuracy += 0.02;
        winner.clampAccuracy();
        updateAnalyticsText("Winner: " + winner.name);
    }

    private int calculateWpm(GuiTypist t)
    {
        int turnsTaken = t.finishTurn > 0 ? t.finishTurn : turn;
        double minutes = Math.max(1.0, turnsTaken) * 0.18 / 60.0;
        double words = passage.length() / 5.0;
        return (int) Math.round(words / minutes);
    }

    private double accuracyPercentage(GuiTypist t)
    {
        if (t.keystrokes == 0) return 0.0;
        return (100.0 * t.correctKeystrokes) / t.keystrokes;
    }

    private void updateAnalyticsText(String heading)
    {
        StringBuilder stats = new StringBuilder(heading + "\n\n");
        for (GuiTypist t : typists)
        {
            stats.append(t.name).append("\n")
                .append("WPM: ").append(calculateWpm(t)).append("\n")
                .append("Accuracy %: ").append(String.format("%.1f", accuracyPercentage(t))).append("\n")
                .append("Burnouts: ").append(t.burnoutCount).append("\n")
                .append("Rating now: ").append(String.format("%.2f", t.accuracy)).append("\n")
                .append("Personal best WPM: ").append(t.bestWpm).append("\n\n");
        }
        statsArea.setText(stats.toString());

        ArrayList<GuiTypist> board = new ArrayList<GuiTypist>(typists);
        Collections.sort(board, new Comparator<GuiTypist>() {
            public int compare(GuiTypist a, GuiTypist b) { return b.points - a.points; }
        });
        StringBuilder leaders = new StringBuilder();
        for (GuiTypist t : board)
        {
            leaders.append(t.name).append(" - ").append(t.points).append(" pts");
            if (t.consecutiveWins >= 3) leaders.append(" | Speed Demon");
            if (t.racesWithoutBurnout >= 5) leaders.append(" | Iron Fingers");
            leaders.append("\n");
        }
        leaderboardArea.setText(leaders.toString());

        StringBuilder comp = new StringBuilder("Side-by-side WPM comparison\n");
        for (GuiTypist t : typists)
        {
            comp.append(t.name).append(": ").append(calculateWpm(t)).append(" WPM, ")
                .append(String.format("%.1f", accuracyPercentage(t))).append("% accurate\n");
        }
        comparisonArea.setText(comp.toString());
    }

    private Color colourFor(String name)
    {
        if ("Green".equals(name)) return Color.GREEN.darker();
        if ("Orange".equals(name)) return Color.ORANGE.darker();
        if ("Red".equals(name)) return Color.RED.darker();
        if ("Purple".equals(name)) return new Color(128, 0, 128);
        if ("Black".equals(name)) return Color.BLACK;
        return Color.BLUE;
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { new TypingRaceGUI().startRaceGUI(); }
        });
    }
}

class GuiTypist
{
    String name;
    String symbol;
    String style;
    String keyboard;
    String accessory;
    Color colour;
    int progress;
    int speed;
    int burnoutRemaining;
    int burnoutDuration = 3;
    int burnoutCount;
    int keystrokes;
    int correctKeystrokes;
    int mistypes;
    int finishTurn;
    int points;
    int consecutiveWins;
    int racesWithoutBurnout;
    int bestWpm;
    boolean finished;
    boolean energyDrink;
    double accuracy;
    double burnoutRisk;
    double mistypeReduction;

    void clampAccuracy()
    {
        if (accuracy < 0.0) accuracy = 0.0;
        if (accuracy > 1.0) accuracy = 1.0;
    }
}

class RaceResult
{
    String typistName;
    int position;
    int wpm;
    double accuracyPercentage;
    int burnouts;

    RaceResult(String typistName, int position, int wpm, double accuracyPercentage, int burnouts)
    {
        this.typistName = typistName;
        this.position = position;
        this.wpm = wpm;
        this.accuracyPercentage = accuracyPercentage;
        this.burnouts = burnouts;
    }
}
