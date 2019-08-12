public abstract class FascistBoard extends Board {

    public static final int MAX_NO_OF_POLICIES = 6;

    public FascistBoard(Game game) {
        super(game);
    }

    /**
     * Create a new FascistBoard of a specified type between 1 and 3 inclusive.
     *
     * @param boardType The type of FascistBoard. Must be between 1 and 3 inclusive
     * @return A FascistBoard of the specified type
     */
    public static FascistBoard createFascistBoard(Game game, int boardType) {
        switch (boardType) {
            case 1:
                return new FascistBoard1(game);
            case 2:
                return new FascistBoard2(game);
            case 3:
                return new FascistBoard3(game);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    protected void policyAction(boolean performAction) {
        System.out.println("A Fascist policy has been enacted\nThere are now "
                + noOfPolicies + " Fascist policies in play");
        if (noOfPolicies == MAX_NO_OF_POLICIES) game.endGame("You have enacted 6 Fascist policies. The Fascists win!");
        else if (performAction) fascistPolicyAction();
    }

    protected abstract void fascistPolicyAction();
}
