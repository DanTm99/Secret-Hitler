public enum Role {
    LIBERAL("Liberal", "Liberal"),
    FASCIST("Fascist", "Fascist"),
    HITLER("Hitler", "Fascist");

    private final String role;
    private final String party;

    Role(String role, String party) {
        this.role = role;
        this.party = party;
    }

    public String toString() {
        return role;
    }

    public String getParty() {
        return party;
    }
}
