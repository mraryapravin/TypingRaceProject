/**
 * Represents one competitor in the textual typing race simulation.
 */
public class Typist
{
    private String name;
    private char symbol;
    private int progress;
    private boolean burntOut;
    private int burnoutTurnsRemaining;
    private double accuracy;

    public Typist(char typistSymbol, String typistName, double typistAccuracy)
    {
        symbol = typistSymbol;
        name = typistName;
        progress = 0;
        burntOut = false;
        burnoutTurnsRemaining = 0;
        setAccuracy(typistAccuracy);
    }

    public void burnOut(int turns)
    {
        if (turns > 0)
        {
            burntOut = true;
            burnoutTurnsRemaining = turns;
        }
        else
        {
            burntOut = false;
            burnoutTurnsRemaining = 0;
        }
    }

    public void recoverFromBurnout()
    {
        if (burntOut)
        {
            burnoutTurnsRemaining = burnoutTurnsRemaining - 1;
            if (burnoutTurnsRemaining <= 0)
            {
                burnoutTurnsRemaining = 0;
                burntOut = false;
            }
        }
    }

    public double getAccuracy()
    {
        return accuracy;
    }

    public int getProgress()
    {
        return progress;
    }

    public String getName()
    {
        return name;
    }

    public char getSymbol()
    {
        return symbol;
    }

    public int getBurnoutTurnsRemaining()
    {
        if (burntOut)
        {
            return burnoutTurnsRemaining;
        }
        return 0;
    }

    public void resetToStart()
    {
        progress = 0;
        burntOut = false;
        burnoutTurnsRemaining = 0;
    }

    public boolean isBurntOut()
    {
        return burntOut;
    }

    public void typeCharacter()
    {
        progress = progress + 1;
    }

    public void slideBack(int amount)
    {
        if (amount > 0)
        {
            progress = progress - amount;
            if (progress < 0)
            {
                progress = 0;
            }
        }
    }

    public void setAccuracy(double newAccuracy)
    {
        if (newAccuracy < 0.0)
        {
            accuracy = 0.0;
        }
        else if (newAccuracy > 1.0)
        {
            accuracy = 1.0;
        }
        else
        {
            accuracy = newAccuracy;
        }
    }

    public void setSymbol(char newSymbol)
    {
        symbol = newSymbol;
    }
}
