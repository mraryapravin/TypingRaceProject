/** Simple command-line checks for the required Typist behaviours. */
public class TypistTest
{
    public static void main(String[] args)
    {
        Typist test = new Typist('T', "TEST_TYPER", 0.50);

        System.out.println("Test 1: normal forward movement");
        test.typeCharacter();
        test.typeCharacter();
        System.out.println("Expected progress 2, actual progress " + test.getProgress());

        System.out.println("\nTest 2: slideBack cannot go below zero");
        test.slideBack(10);
        System.out.println("Expected progress 0, actual progress " + test.getProgress());

        System.out.println("\nTest 3: burnout countdown clears at zero");
        test.burnOut(3);
        System.out.println("Start: burnt out = " + test.isBurntOut() + ", turns = " + test.getBurnoutTurnsRemaining());
        test.recoverFromBurnout();
        System.out.println("After 1 turn: burnt out = " + test.isBurntOut() + ", turns = " + test.getBurnoutTurnsRemaining());
        test.recoverFromBurnout();
        System.out.println("After 2 turns: burnt out = " + test.isBurntOut() + ", turns = " + test.getBurnoutTurnsRemaining());
        test.recoverFromBurnout();
        System.out.println("After 3 turns: burnt out = " + test.isBurntOut() + ", turns = " + test.getBurnoutTurnsRemaining());

        System.out.println("\nTest 4: resetToStart clears progress and burnout");
        test.typeCharacter();
        test.burnOut(2);
        test.resetToStart();
        System.out.println("Expected progress 0, actual progress " + test.getProgress());
        System.out.println("Expected burnt out false, actual burnt out " + test.isBurntOut());
        System.out.println("Expected burnout turns 0, actual turns " + test.getBurnoutTurnsRemaining());

        System.out.println("\nTest 5: accuracy is clamped to 0.0-1.0");
        test.setAccuracy(1.50);
        System.out.println("Expected accuracy 1.0, actual accuracy " + test.getAccuracy());
        test.setAccuracy(-0.25);
        System.out.println("Expected accuracy 0.0, actual accuracy " + test.getAccuracy());
    }
}
