public class FascistBoard3 extends FascistBoard {

    public FascistBoard3(Game game) {
        super(game);
    }

    @Override
    protected void fascistPolicyAction() {
        switch (noOfPolicies) {
            case 1:
                game.investigatePlayer();
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
