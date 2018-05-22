public class Player {
    private final String name;
    private Role role;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
