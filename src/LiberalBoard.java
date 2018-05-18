public class LiberalBoard extends Board {

    public LiberalBoard(Game game) {
        super(game);
    }

    /**
     * If the number of policies on the liberal board is 6, they win the game.
     */
    @Override
    protected void policyAction(boolean performAction) {
        System.out.println("A Liberal policy has been enacted\nThere are now "
                + noOfPolicies + " Liberal policies in play");
        if (noOfPolicies == 5) game.endGame("You have enacted 5 Liberal policies. The Liberals win!");
    }
}
