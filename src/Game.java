import java.util.*;

/**
 * This class is the implementation of the board game Secret Hitler in Java.
 * Secret Hitler was designed by Mike Boxleiter, Tommy Maranges and illustrated by Mackenzie Schubert.
 * The game was produced by Max Temkin.
 */
public class Game {
    private final List<Player> players = new ArrayList<>();
    private final List<Player> ineligiblePlayers = new ArrayList<>(); // A ArrayList of the 2 players ineligible for the next parliament
    private final List<Policy> drawPile = new ArrayList<>();
    private final List<Policy> discardPile = new ArrayList<>();

    private final Scanner scanner = new Scanner(System.in);
    private final Random rand = new Random();

    private Iterator<Player> presidentTracker; // An Iterator that keeps track of the president
    private int electionTracker; // An int representing the state of the election tracker

    private final LiberalBoard liberalBoard = new LiberalBoard(this);
    private FascistBoard fascistBoard;

    private Player president;
    private Player chancellor;

    private boolean isSpecialElection = false;
    private boolean vetoUnlocked = false;
    private boolean gameActive = true;

    /**
     * Begin the game.
     */
    public void start() {
        setupPlayers();
        initialiseDrawPile();
        assignRoles();
        nightPhase();

        presidentTracker = players.iterator();
        int presidentIndex = rand.nextInt(players.size());
        for (int i = 0; i < presidentIndex; i++) presidentTracker.next();
        president = presidentTracker.next();

        // Gameplay begins
        while (gameActive) { // Preparing for the removal of system exits
            do {
                passPresidency();
                nominateChancellor();
            } while (!vote() && gameActive);

            if (gameActive) legislativeSession();
        }
    }

    /**
     * Ask user how many players are playing and continue if it's between 5 and 10 inclusive,
     * then create a Player object for each player, with names provided by the user (no duplicates).
     **/
    private void setupPlayers() {
        System.out.println("How many players are playing?");
        int numberOfPlayers = scanner.nextInt();

        while (numberOfPlayers < 5 || numberOfPlayers > 10) {
            System.out.println("5 - 10 players must be playing. Try again");
            numberOfPlayers = scanner.nextInt();
        }

        Set<String> playerNames = new HashSet<>();
        for (int i = 1; i <= numberOfPlayers; i++) {
            System.out.println("What is player " + i + "'s name?");

            String playerName = scanner.next();
            while (playerNames.contains(playerName)) {
                System.out.println("A player with this name already exists. Try again");
                playerName = scanner.next();
            }
            playerNames.add(playerName);
            players.add(new Player(playerName));
        }
    }

    /**
     * Assign each player a role, the number of players with a certain role depends on the number of players.
     * Then print a message saying how many of each role are playing.
     **/
    private void assignRoles() {
        int noOfFascists = (players.size() - 1) / 2; // This includes Hitler
        int noOfLiberals = players.size() - noOfFascists;
        fascistBoard = FascistBoard.createFascistBoard(this, noOfFascists - 1);

        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.HITLER);
        for (int i = 1; i < noOfFascists; i++) roles.add(Role.FASCIST);
        for (int i = 0; i < noOfLiberals; i++) roles.add(Role.LIBERAL);

        Collections.shuffle(roles); // randomizing the order of the roles

        for (int i = 0; i < players.size(); i++) players.get(i).setRole(roles.get(i));
        System.out.println("There are " + noOfFascists + " Fascists (including Hitler) and " + noOfLiberals + " Liberals");
    }

    /**
     * Fill the draw pile with 6 Liberal policies and 11 Fascist policies, then shuffle the draw pile.
     */
    private void initialiseDrawPile() {
        drawPile.clear();

        for (int i = 0; i < 6; i++) drawPile.add(Policy.LIBERAL);
        for (int i = 0; i < 11; i++) drawPile.add(Policy.FASCIST);

        Collections.shuffle(drawPile);
    }

    /**
     * Shuffle the discard pile into the draw pile.
     */
    private void reshuffleDrawPile() {
        System.out.println("The draw pile does not have enough policies");
        System.out.println("The discard pile is being shuffled into the draw pile");
        wait(5000);

        drawPile.addAll(discardPile);
        discardPile.clear();

        Collections.shuffle(drawPile);
    }

    /**
     * Tell every player their role. If they are a normal fascist, tell them everyone else's role too.
     * If they are Hitler, only tell them everyone else's role if there are at least 3 fascists (including Hitler).
     */
    private void nightPhase() {
        for (Player player : players) {
            devicePass(player);

            switch (player.getRole()) {
                case HITLER:
                    if (fascistBoard instanceof FascistBoard1) {
                        System.out.println("You are Hitler. The Fascists are:");
                        printFascists();
                    } else System.out.println("You are Hitler, there is no information for you in the night phase");
                    break;
                case FASCIST:
                    System.out.println("You are a Fascist. The Fascists are:");
                    printFascists();
                    break;

                case LIBERAL:
                    System.out.println("You are a liberal, there is no information for you in the night phase");
                    break;
            }
            devicePassConcluded();
        }
    }

    /**
     * Print the names of the Fascist players alongside whether they're a normal Fascist or Hitler.
     */
    private void printFascists() {
        for (Player player : players) {
            Role playerRole = player.getRole();
            if (playerRole.getParty().equals("Fascist")) System.out.println(player.getName() + " - " + playerRole);
        }
    }

    /**
     * Tell the user to pass the device to the specified player and wait for that player to type their name to confirm
     * that the device has been passed to them.
     *
     * This is useful when information needs to be revealed to a specific player without others knowing
     * or when information needs to be revealed by a certain player.
     *
     * @param playerToPass the player the device should be passed to
     */
    private void devicePass(Player playerToPass) {
        System.out.println("Pass the device secretly to " + playerToPass.getName());
        System.out.println("Type " + playerToPass.getName() + " to continue");

        while (!scanner.next().equals(playerToPass.getName())) System.out.println("Incorrect. Try again");
    }

    /**
     * Give the player 10 seconds to read what has been previously printed before
     * printing 20 lines of dots to hide it.
     * This is useful to use after the device has been passed to a player.
     */
    private void devicePassConcluded() { // Used after passing the device secretly to a player has concluded
        System.out.println("In 10 seconds this message will disappear");

        wait(10000);

        for (int i = 0; i < 20; i++) {
            System.out.println(".");
        }
    }

    /**
     * Transfer the presidency to the next player, stating them as president elect.
     * If this is a special election, announce the selected president without advancing
     * the iterator. This is so the normal rotation returns after the special election.
     */
    private void passPresidency() {
        System.out.println("Show the device to all players");
        if(!isSpecialElection) {
            if (!presidentTracker.hasNext()) presidentTracker = players.iterator();
            president = presidentTracker.next();
            System.out.println("The president elect is now " + president.getName());
        } else {
            System.out.println("This is a special election. The president elect is now " + president.getName());
            isSpecialElection = false;
        }
    }

    /**
     * Let the current president elect nominate the chancellor.
     */
    private void nominateChancellor() {
        devicePass(president);
        printPlayersPlaying();
        if (!ineligiblePlayers.isEmpty()) {
            if (players.size() <= 5) {
                System.out.println("Note: the chancellor in the last elected parliament is illegible and is: " + ineligiblePlayers);
            } else {
                System.out.println("Note: players in the last elected parliament are illegible and are: " + ineligiblePlayers);
            }
        }
        System.out.println("Type the name of the player you want to elect as chancellor");

        List<String> eligiblePlayers = new ArrayList<>();
        for (Player p : players)
            if (!(p == president || ineligiblePlayers.contains(p))) eligiblePlayers.add(p.getName());

        System.out.println("Eligible players are:");
        for (String s : eligiblePlayers) System.out.println(s);

        String inputName = scanner.next();
        while (!eligiblePlayers.contains(inputName)) {
            System.out.println("Invalid name. Try again");
            inputName = scanner.next();
        }

        chancellor = lookupName(inputName);
    }

    private void printPlayersPlaying() {
        System.out.println("The names of the players playing are:");
        for (Player p : players) System.out.println(p.getName());
    }

    private boolean checkInvalidName(String name) {
        for (Player p : players) if (p.getName().equals(name)) return false;
        return true;
    }

    private Player lookupName(String name) {
        for (Player p : players) if (p.getName().equals(name)) return p;
        return null;
    }

    /**
     * Let every player vote for the current parliament secretly before tallying up the votes then displaying
     * the number of Yes's and No's, alongside each player's vote and whether or not the vote has passed.
     *
     * If the vote passes, announce the elected parliament and set the election tracker to 0. If there are at least
     * 3 Fascist policies announce whether or not the current chancellor is Hitler. If Hitler is the chancellor
     * end the game, announcing the Fascists as the victors.
     *
     * If the vote doesn't pass increment the electron tracker and announce so.
     *
     * @return True is the vote succeeded. False otherwise.
     */
    private boolean vote() {
        HashMap<String, String> votes = new HashMap<>();
        int yesVotes = 0;
        int noVotes = 0;

        System.out.println("VOTING PHASE:");

        for (Player player : players) {
            devicePass(player);
            System.out.println("Are you in favour of this parliament [Yes/No]:");
            System.out.println("President = " + president.getName());
            System.out.println("Chancellor = " + chancellor.getName());

            String inputVote = scanner.next();
            while (!(inputVote.equals("Yes") || inputVote.equals("No"))) {
                System.out.println("Invalid input. Type Yes or No");
                inputVote = scanner.next();
            }
            if (inputVote.equals("Yes")) yesVotes++;
            else noVotes++;

            votes.put(player.getName(), inputVote);
            devicePassConcluded();
        }

        for (Player player : players) {
            if (votes.get(player.getName()).equals("Yes")) yesVotes++;
            if (votes.get(player.getName()).equals("No")) noVotes++;
        }

        System.out.println("Show the device to all players");

        System.out.println("The votes are in:");
        System.out.println(votes);

        System.out.println("Yes: " + yesVotes);
        System.out.println("No: " + noVotes);

        boolean voteSucceeds = noVotes < yesVotes;

        if (voteSucceeds) {
            System.out.println("The vote has succeeded. Your new president is " + president.getName() + " and your new chancellor is " + chancellor.getName());
            setIneligiblePlayers();
            electionTracker = 0;
        } else {
            System.out.println("The vote has failed");
            incrementElectionTracker();
        }

        wait(10000);

        if (voteSucceeds) {
            if (fascistBoard.getNoOfPolicies() >= 3) {
                System.out.println("There are at least 3 fascist policies in play. Is the new chancellor Hitler?");
                devicePass(chancellor);
                System.out.println("Show the device to the other players");
                wait(5000);
                if (chancellor.getRole().equals(Role.HITLER)) {
                    endGame("You have elected Hitler as chancellor. The fascists win!");
                } else System.out.println("The chancellor is not Hitler");
            }
        }
        return voteSucceeds;
    }

    /**
     * Set the current president and chancellor as the 2 ineligible players
     */
    private void setIneligiblePlayers() {
        ineligiblePlayers.clear();
        ineligiblePlayers.add(chancellor);

        if (players.size() > 5) ineligiblePlayers.add(president);
    }

    /**
     * Wait a given amount of time.
     *
     * @param timeMS The time to wait in milliseconds
     */
    private void wait(int timeMS) {
        try {
            Thread.sleep(timeMS); // Change timeMS to 0 to speed up testing
        } catch (InterruptedException ignored) {
        }
    }

    private void playPolicy(Policy policy, boolean performAction) {
        switch (policy) {
            case LIBERAL:
                liberalBoard.addPolicy(performAction);
                break;
            case FASCIST:
                fascistBoard.addPolicy(performAction);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void unlockVeto() {
        vetoUnlocked = true;
    }

    /**
     * The president selects a player to be executed.
     * Announce if they are Hitler or not. If they are Hitler end the game, announcing
     * the liberals as the victors. If they are not, remove them from the game.
     */
    public void executePlayer() {
        System.out.println("The president must choose a player to execute");

        devicePass(president);
        printPlayersPlaying();
        System.out.println("Type the name of the player you want to execute (except yourself)");

        String inputName = scanner.next();
        while (president.getName().equals(inputName) || checkInvalidName(inputName)) {
            System.out.println("Not a valid name. Try again");
            inputName = scanner.next();
        }

        System.out.println("The president has chosen to execute " + inputName);
        System.out.println("Was the executed player Hitler?");
        wait(5000);

        Player playerExecuted = lookupName(inputName);
        if (playerExecuted.getRole().equals(Role.HITLER)) endGame(inputName + " was Hitler. The liberals win!");
        else {
            System.out.println(inputName + " was not Hitler");
            System.out.println(inputName + " cannot participate for the rest of the game");
            removePlayer(playerExecuted);
        }
    }

    /**
     * Remove a player from the game by removing them from any relevant Collections
     *
     * @param player player to be removed
     */
    private void removePlayer(Player player) { // Check is there are any bugs if the chancellor is killed
        players.remove(player);
        ineligiblePlayers.remove(player);
    }

    /**
     * The president selects a player to be the president elect next round.
     * After that round, the rotation returns to normal.
     */
    public void specialElection() {
        System.out.println("The president must choose a player to elect another player to be the next president");

        devicePass(president);
        printPlayersPlaying();
        System.out.println("Type the name of the player you want to elect as the next president (except yourself)");

        String inputName = scanner.next();
        while (president.getName().equals(inputName) || checkInvalidName(inputName)) {
            System.out.println("Not a valid name. Try again");
            inputName = scanner.next();
        }

        isSpecialElection = true;
        System.out.println("The president has nominated " + inputName + " as the next president elect");
        president = lookupName(inputName);
    }

    /**
     * The president selects a player and their party membership is secretly revealed to them.
     */
    public void investigatePlayer() {
        System.out.println("The president must choose a player to investigate a their party membership");

        devicePass(president);
        printPlayersPlaying();
        System.out.println("Type the name of the player you want to investigate (except yourself)");

        String inputName = scanner.next();
        while (president.getName().equals(inputName) || checkInvalidName(inputName)) {
            System.out.println("Not a valid name. Try again");
            inputName = scanner.next();
        }

        Player investigatedPlayer = lookupName(inputName);

        System.out.println("Watch the device's screen while " + inputName + " has the device");
        devicePass(investigatedPlayer);
        System.out.println(inputName + " party membership is " + (investigatedPlayer.getRole().getParty()));

        devicePassConcluded();
    }

    /**
     * The top 3 policies in the draw pile are revealed to the president in order.
     */
    public void policyPeek() {
        System.out.println("The president must look at the first 3 policies in order");
        devicePass(president);

        if (drawPile.size() < 3) reshuffleDrawPile();

        System.out.println("The top 3 policies (from top to bottom) are:");
        for (int i = 0; i < 3; i++) System.out.println(drawPile.get(i));
        devicePassConcluded();
    }


    /**
     * Increment the election tracker and announce the new value. If it reaches 3,
     * play the top policy of the draw pile then reset it to 0.
     */
    private void incrementElectionTracker() {
        electionTracker++;
        System.out.println("The election tracker has been incremented by 1");
        System.out.println("The election tracker is now on " + electionTracker);

        if (electionTracker == 3) {
            System.out.println("The top policy of the draw pile will automatically be played and the election tracker will be reset");

            if (drawPile.isEmpty()) reshuffleDrawPile();
            playPolicy(drawPile.remove(0), false);

            ineligiblePlayers.clear();
            electionTracker = 0;
        }
    }

    /**
     * The president draws 3 policies from the top of the draw pile (reshuffle if it's too small),
     * then they secretly pass 2 to the chancellor who chooses 1 to play, or requests to veto if
     * the veto power is unlocked. If the president agrees with the veto, the 2 policies are discarded.
     */
    private void legislativeSession() {
        System.out.println("LEGISLATIVE SESSION");
        System.out.println("Nobody may talk until a policy is enacted");
        System.out.println("There are " + drawPile.size() + " policies remaining in the draw pile");

        ArrayList<Policy> hand = new ArrayList<>();

        if (drawPile.size() < 3) reshuffleDrawPile();

        for (int i = 0; i < 3; i++) hand.add(drawPile.remove(0));

        devicePass(president);
        System.out.println("You have drawn " + hand);
        System.out.println("Select a policy to discard [1/2/3]");

        int inputIndex = scanner.nextInt() - 1;
        while (inputIndex < 1 || inputIndex >= 3) {
            System.out.println("You must type 1, 2 or 3. Try again");
            inputIndex = scanner.nextInt() - 1;
        }

        discardPile.add(hand.remove(inputIndex));
        Collections.shuffle(hand);
        devicePassConcluded();

        boolean vetoSuccess = false;
        while (!vetoSuccess) {
            devicePass(chancellor);
            System.out.println("You have drawn " + hand);
            System.out.println("Select a policy to discard [1/2]" + (vetoUnlocked ? "\nIf you want to veto type 0" : ""));

            inputIndex = scanner.nextInt() - 1;
            while (inputIndex < (vetoUnlocked ? -1 : 0) || inputIndex >= 2) {
                System.out.println("You must type 1 or 2" + (vetoUnlocked ? " or 0" : "") + ". Try again");
                inputIndex = scanner.nextInt() - 1;
            }

            if (!(inputIndex == -1)) {
                discardPile.add(hand.remove(inputIndex));
                vetoSuccess = true;
                playPolicy(hand.remove(0), true);
                devicePassConcluded();
            } else {
                System.out.println("Before you pass the device announce that you wish to veto this agenda");
                devicePassConcluded();

                devicePass(president);
                System.out.println("Do you wish to veto this agenda? [Yes/No]");
                System.out.println("Announce your answer after typing it");

                String inputVote = scanner.next();
                while (!(inputVote.equals("Yes") || inputVote.equals("No"))) {
                    System.out.println("Invalid input. Type Yes or No");
                    inputVote = scanner.next();
                }

                if (inputVote.equals("Yes")) {
                    discardPile.addAll(hand);
                    hand.clear();

                    vetoSuccess = true;
                    incrementElectionTracker();
                } else System.out.println("The veto is unsuccessful");
            }
        }

    }

    /**
     * End the current game
     * @param message the message to display before ending the game
     */
    public void endGame(String message) {
        System.out.println(message);
        wait(10000);
        gameActive = false;
    }
}