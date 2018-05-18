public class FascistBoard1 extends FascistBoard {

    public FascistBoard1(Game game) {
        super(game);
    }

    @Override
    protected void fascistPolicyAction() {
        switch (noOfPolicies) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                game.policyPeek();
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
