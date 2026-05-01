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
}