import java.util.concurrent.TimeUnit;

/**
 * Textual typing race simulation for Part I.
 */
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

    public TypingRace(int passageLength)
    {
        if (passageLength < 1)
        {
            this.passageLength = 1;
        }
        else
        {
            this.passageLength = passageLength;
        }
        seat1Typist = null;
        seat2Typist = null;
        seat3Typist = null;
        winner = null;
    }

    public void addTypist(Typist theTypist, int seatNumber)
    {
        if (theTypist == null)
        {
            System.out.println("Cannot add a null typist.");
            return;
        }

        if (seatNumber == 1)
        {
            seat1Typist = theTypist;
        }
        else if (seatNumber == 2)
        {
            seat2Typist = theTypist;
        }
        else if (seatNumber == 3)
        {
            seat3Typist = theTypist;
        }
        else
        {
            System.out.println("Cannot seat typist at seat " + seatNumber + " - there is no such seat.");
        }
    }

    private void validateRaceCanStart()
    {
        if (seat1Typist == null || seat2Typist == null || seat3Typist == null)
        {
            throw new IllegalStateException("All three seats must contain a typist before the race starts.");
        }
        if (seat1Typist.getSymbol() == seat2Typist.getSymbol()
                || seat1Typist.getSymbol() == seat3Typist.getSymbol()
                || seat2Typist.getSymbol() == seat3Typist.getSymbol())
        {
            throw new IllegalStateException("Each typist must have a different symbol.");
        }
    }

    private void resetRace()
    {
        seat1Typist.resetToStart();
        seat2Typist.resetToStart();
        seat3Typist.resetToStart();
        seat1Mistyped = false;
        seat2Mistyped = false;
        seat3Mistyped = false;
        winner = null;
    }

    private boolean advanceTypist(Typist theTypist)
    {
        boolean mistyped = false;

        if (theTypist.isBurntOut())
        {
            theTypist.recoverFromBurnout();
            return false;
        }

        if (Math.random() < theTypist.getAccuracy())
        {
            theTypist.typeCharacter();
        }

        if (Math.random() < (1.0 - theTypist.getAccuracy()) * MISTYPE_BASE_CHANCE)
        {
            theTypist.slideBack(SLIDE_BACK_AMOUNT);
            mistyped = true;
        }

        if (Math.random() < 0.05 * theTypist.getAccuracy() * theTypist.getAccuracy())
        {
            theTypist.burnOut(BURNOUT_DURATION);
            theTypist.setAccuracy(theTypist.getAccuracy() - BURNOUT_ACCURACY_PENALTY);
        }

        return mistyped;
    }


    private Typist findWinner()
    {
        if (raceFinishedBy(seat1Typist))
        {
            return seat1Typist;
        }
        if (raceFinishedBy(seat2Typist))
        {
            return seat2Typist;
        }
        if (raceFinishedBy(seat3Typist))
        {
            return seat3Typist;
        }
        return null;
    }

    private boolean raceFinishedBy(Typist theTypist)
    {
        return theTypist != null && theTypist.getProgress() >= passageLength;
    }
}