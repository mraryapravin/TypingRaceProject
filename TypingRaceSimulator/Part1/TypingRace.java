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

    private void printRace()
    {
        System.out.print("\f");
        System.out.println("  TYPING RACE - passage length: " + passageLength + " chars");
        multiplePrint('=', passageLength + 3);
        System.out.println();
        printSeat(seat1Typist, seat1Mistyped);
        System.out.println();
        printSeat(seat2Typist, seat2Mistyped);
        System.out.println();
        printSeat(seat3Typist, seat3Mistyped);
        System.out.println();
        multiplePrint('=', passageLength + 3);
        System.out.println();
        System.out.println("  [~] = burnt out    [<] = just mistyped");
    }

    private void printSeat(Typist theTypist, boolean justMistyped)
    {
        int displayProgress = theTypist.getProgress();
        if (displayProgress > passageLength)
        {
            displayProgress = passageLength;
        }

        int markerWidth = 1;
        if (theTypist.isBurntOut())
        {
            markerWidth = markerWidth + 1;
        }
        if (justMistyped)
        {
            markerWidth = markerWidth + 3;
        }

        int spacesBefore = displayProgress;
        int spacesAfter = passageLength - displayProgress - markerWidth + 1;
        if (spacesAfter < 0)
        {
            spacesAfter = 0;
        }

        System.out.print('|');
        multiplePrint(' ', spacesBefore);
        System.out.print(theTypist.getSymbol());
        if (theTypist.isBurntOut())
        {
            System.out.print('~');
        }
        if (justMistyped)
        {
            System.out.print("[<]");
        }
        multiplePrint(' ', spacesAfter);
        System.out.print("| ");
        System.out.printf("%s (Accuracy: %.2f)", theTypist.getName(), theTypist.getAccuracy());
        if (theTypist.isBurntOut())
        {
            System.out.print(" BURNT OUT (" + theTypist.getBurnoutTurnsRemaining() + " turns)");
        }
        if (justMistyped)
        {
            System.out.print(" <- just mistyped");
        }
    }

    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }

    public void startRace()
    {
        validateRaceCanStart();
        resetRace();

        boolean finished = false;
        while (!finished)
        {
            seat1Mistyped = advanceTypist(seat1Typist);
            seat2Mistyped = advanceTypist(seat2Typist);
            seat3Mistyped = advanceTypist(seat3Typist);

            printRace();
            winner = findWinner();
            finished = winner != null;

            try
            {
                TimeUnit.MILLISECONDS.sleep(DELAY_MILLISECONDS);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                finished = true;
            }
        }

        if (winner != null)
        {
            winnerStartingAccuracy = winner.getAccuracy();
            winner.setAccuracy(winner.getAccuracy() + WINNER_ACCURACY_BONUS);
            winnerFinalAccuracy = winner.getAccuracy();
            printWinner();
        }
    }

    private void printWinner()
    {
        System.out.println();
        System.out.println("And the winner is... " + winner.getName() + "!");
        System.out.printf("Final accuracy: %.2f (improved from %.2f)%n", winnerFinalAccuracy, winnerStartingAccuracy);
    }

    public static void main(String[] args)
    {
        TypingRace race = new TypingRace(40);
        race.addTypist(new Typist('1', "TURBOFINGERS", 0.85), 1);
        race.addTypist(new Typist('2', "QWERTY_QUEEN", 0.60), 2);
        race.addTypist(new Typist('3', "HUNT_N_PECK", 0.30), 3);
        race.startRace();
    }


}