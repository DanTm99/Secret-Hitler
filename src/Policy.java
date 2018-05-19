public enum Policy {
    LIBERAL("Liberal"),
    FASCIST("Fascist");

    private final String name;

    Policy(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
