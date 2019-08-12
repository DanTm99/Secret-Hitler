public class LiberalBoard extends Board {

    public static final int MAX_NO_OF_POLICIES = 5;

    public LiberalBoard(Game game) {
        super(game);
    }

    /**
     * If the number of policies on the liberal board is 6, Liberals win the game.
     */
    @Override
    protected void policyAction(boolean performAction) {
        System.out.println("A Liberal policy has been enacted\nThere are now "
                + noOfPolicies + " Liberal policies in play");
        if (noOfPolicies == MAX_NO_OF_POLICIES) game.endGame("You have enacted 5 Liberal policies. The Liberals win!");
    }
}
