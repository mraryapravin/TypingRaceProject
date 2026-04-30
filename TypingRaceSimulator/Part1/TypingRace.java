import java.util.concurrent.TimeUnit;

public class TypingRace
{
    private int passageLength;
    private Typist seat1Typist;
    private Typist seat2Typist;
    private Typist seat3Typist;
    private Typist winner;
    private boolean seat1Mistyped;
    private boolean seat2Mistyped;
    private boolean seat3Mistyped;
    private double winnerStartingAccuracy;
    private double winnerFinalAccuracy;

    private static final double MISTYPE_BASE_CHANCE = 0.3;
    private static final int SLIDE_BACK_AMOUNT = 2;
    private static final int BURNOUT_DURATION = 3;
    private static final double WINNER_ACCURACY_BONUS = 0.02;
    private static final double BURNOUT_ACCURACY_PENALTY = 0.01;
    private static final int DELAY_MILLISECONDS = 120;
}