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
}