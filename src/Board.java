public abstract class Board {
    protected final Game game;
    protected int noOfPolicies;

    public Board(Game game) {
        this.game = game;
    }

    /**
     * Add a policy to the board an enact the relevant action.
     */
    public void addPolicy(boolean performAction) {
        this.noOfPolicies++;
        policyAction(performAction);
    }

    /**
     * Enact the relevant action for the number of policies (if any).
     */
    protected abstract void policyAction(boolean performAction);

    /**
     * Return the number of policies currently on this board.
     *
     * @return the number of policies currently on this board
     */
    public int getNoOfPolicies() {
        return this.noOfPolicies;
    }
}
