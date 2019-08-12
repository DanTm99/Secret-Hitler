public enum CardType {
    PLACEHOLDER("/Placeholder.png", null),
    JA("/Ja.png", "BallotBack.png"),
    NEIN("/Nein.png", "BallotBack.png"),
    LIBERAL_POLICY("/LiberalPolicy.png", "/PolicyBack.png"),
    FASCIST_POLICY("/FascistPolicy.png", "/PolicyBack.png"),
    LIBERAL_MEMBERSHIP("/LiberalMembership.png", "/MembershipBack.png"),
    FASCIST_MEMBERSHIP("/FascistMembership.png", "/MembershipBack.png"),
    LIBERAL_ROLE("/LiberalRole.png", "/RoleBack.png"),
    FASCIST_ROLE("/FascistRole.png", "/RoleBack.png"),
    HITLER_ROLE("/HitlerRole.png", "/RoleBack.png");

    private final String frontPath;
    private final String backPath;

    CardType(String frontPath, String backPath) {
        this.frontPath = frontPath;
        this.backPath = backPath;
    }

    public String getFrontPath() {
        return frontPath;
    }

    public String getBackPath() {
        return backPath;
    }
}
