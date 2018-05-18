public class FascistBoard2 extends FascistBoard {

    public FascistBoard2(Game game) {
        super(game);
    }

    @Override
    protected void fascistPolicyAction() {
        switch (noOfPolicies) {
            case 1:
                break;
            case 2:
                game.investigatePlayer();
                break;
            case 3:
                game.specialElection();
                break;
            case 4:
                game.executePlayer();
                break;
            case 5:
                game.executePlayer();
                game.unlockVeto();
                break;
        }
    }

}
