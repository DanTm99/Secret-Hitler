import java.util.*;

/**
 * This class is the implementation of the board game Secret Hitler in Java
 * Secret Hitler was designed by Mike Boxleiter, Tommy Maranges and illustrated by Mackenzie Schubert.
 * The game was produced by Max Temkin
 * @author dan Khan
 * @version 0.2
 */
public class Game {
    private List<String> players = new ArrayList<>();           // An ArrayList of the players in the game
    private Map<String, String> secretRoles = new HashMap<>();   // A HashMap mapping each player to their secret role
    private Map<String, Boolean> liberal = new HashMap<>();    // A HashMap mapping each player to if they're liberal
    private List<String> ineligiblePlayers = new ArrayList<>(); // An ArrayList of the 2 players ineligible for the next parliament
    private List<String> deck = new ArrayList<>();              // An ArrayList representing the deck of policies

    private Scanner scanner = new Scanner(System.in);
    private Random rand = new Random();

    private int numberOfPlayers;
    private int noOfFascists;
    private int noOfLiberals;
    private int presidentIndex;  // The index of the president in the Players ArrayList
    private int electionTracker; // An int representing the election tracker
    private int fascistPolicies; // An int representing the number of fascist policies in play
    private int liberalPolicies; // An int representing the number of liberal policies in play
    private int gameBoard;       // An int representing which of the 3 fascists boards is used for this game

    private String president;
    private String chancellor;

    private boolean voteSucceeds = false;
    private boolean isSpecialElection = false;
    private boolean vetoUnlocked = false;
    private boolean gameActive = true;

    /**
     * Begin the game
     */
    public void start() {
        setupPlayers();
        assignRoles();
        newDeck();

        System.out.println("There are " + numberOfPlayers + " players playing");
        System.out.println("There are " + noOfFascists + " Fascists (including Hitler) and " + noOfLiberals + " Liberals");

        nightPhase();

        presidentIndex = rand.nextInt(numberOfPlayers); // Assign presidency to a random player

        // Gameplay begins
        while (gameActive) { // Preparing for the removal of system exits
            do {
                passPresidency();
                nominateChancellor();
                vote(); // if the vote succeeds, continue. If not, pass the presidency and try again

            } while (!voteSucceeds && gameActive);
            if (gameActive) {
                legislativeSession();
            }


        }
    }

    /**
     * Ask user how many players are playing and continue if it's between 5 and 10 inclusive
     * Create an ArrayList with the names of each player (provided by the user)
     **/
    private void setupPlayers() {
        System.out.println("How many players are playing?");
        numberOfPlayers = scanner.nextInt();

        while (!(numberOfPlayers >= 5 && numberOfPlayers <= 10)) {
            System.out.println(" 5 - 10 players must be playing");
            numberOfPlayers = scanner.nextInt();
        }

        for (int i = 1; i <= numberOfPlayers; i++) {
            System.out.println("What is player " + i + "'s name?");
            players.add(scanner.next());
        }
    }

    /**
     * Set the number of liberals and fascists based on the number of players
     * Create a HashMap, mapping each player name to a role (Liberal, Fascist, Hitler) and thier party
     **/
    private void assignRoles() {
        noOfFascists = (numberOfPlayers - 1) / 2; // This includes Hitler
        noOfLiberals = numberOfPlayers - noOfFascists;
        gameBoard = noOfFascists - 1;

        ArrayList<String> roles = new ArrayList<>(); // An ArrayList of the roles in the current game

        roles.add("Hitler");

        for (int i = 1; i < noOfFascists; i++) { // Since Hitler counts as a fascist, there is 1 less normal fascist than noOfFascists
            roles.add("Fascist");
        }

        for (int i = 1; i <= noOfLiberals; i++) {
            roles.add("Liberal");
        }

        Collections.shuffle(roles); // randomizing the order of the roles

        int i = 0;
        for (String player : players) {
            secretRoles.put(player, roles.get(i)); // assign each player a role
            liberal.put(player, roles.get(i).equals("Liberal"));
            i++;
        }
    }

    /**
     * Fill the deck with 6 Liberal cards and 11 Fascist cards. Then shuffle the deck
     */
    private void newDeck() {
        if (liberalPolicies > 0 || fascistPolicies > 0) { // This will be true after any policy has been enacted
            System.out.println("A new deck is being shuffled");
            wait(5000);
        }
        deck.clear();

        for (int i = 1; i <= 6; i++) {
            deck.add("Liberal");
        }

        for (int i = 1; i <= 11; i++) {
            deck.add("Fascist");
        }

        Collections.shuffle(deck);
    }

    /**
     * Tell every player their role. If they are a normal fascist, tell them everyone else's role too
     * If they are Hitler, only tell them everyone else's role if there are at least 3 fascists (including hitler)
     */
    private void nightPhase() {
        for (String player : players) {

            devicePass(player);

            switch (secretRoles.get(player)) {
                case "Hitler":
                    if (gameBoard == 1) {
                        System.out.println("You are Hitler. The player roles are: " + secretRoles);
                    } else {
                        System.out.println("You are Hitler, there is no information for you in the night phase");
                    }
                    break;

                case "Fascist":
                    System.out.println("You are a Fascist. The player roles are: " + secretRoles);
                    break;

                case "Liberal":
                    System.out.println("You are a liberal, there is no information for you in the night phase");
                    break;
            }
            devicePassConcluded();
        }
    }

    /**
     * Tell the user to pass the device to a player and wait for that player to type their name to confirm
     * that the device has been passed
     * This is useful when information needs to be revealed to a specific player without others knowing
     * or when information needs to be revealed by a certain player
     *
     * @param playerToPass the player the device should be passed to
     */
    private void devicePass(String playerToPass) {
        System.out.println("Pass the device secretly to " + playerToPass);
        System.out.println("Type " + playerToPass + " to continue");
        boolean correct = false;
        while (!correct) {
            if (scanner.next().equals(playerToPass)) {
                correct = true;
            } else {
                System.out.println("Try again");
            }
        }
    }

    /**
     * Give the player 10 seconds to read what has been previously printed before
     * printing 20 lines of dots to hide that
     * This is useful to use after the device has been passed to a player
     */
    private void devicePassConcluded() { // Used after passing the device secretly to a player has concluded
        System.out.println("In 10 seconds this message will disappear");

        wait(10000);

        for (int i = 1; i <= 20; i++) {
            System.out.println(".");
        }
    }

    /**
     * Transfer the presidency to the next player, stating them as president elect
     * If this is a special election, announce the selected president without changing the
     * presidentIndex. This is so the normal rotation returns after the special election
     */
    private void passPresidency() {
        System.out.println("Show the device to all players");
        if(!isSpecialElection) {
            presidentIndex = (presidentIndex + 1) % numberOfPlayers;
            president = players.get(presidentIndex);
            System.out.println("The president elect is now " + president);
        } else {
            System.out.println("This is a special election. The president elect is now " + president);
            isSpecialElection = false;
        }
    }

    /**
     * Let the current president elect nominate the chancellor
     * Check if the nominated player is in the game, isn't the president themselves, and isn't ineligible
     */
    private void nominateChancellor() {
        devicePass(president);
        System.out.println("The names of the players playing are: " + players);
        if (!ineligiblePlayers.isEmpty()) {
            if (numberOfPlayers <= 5) {
                System.out.println("Note: the chancellor in the last elected parliament is illegible and is: " + ineligiblePlayers);
            } else {
                System.out.println("Note: players in the last elected parliament are illegible and are: " + ineligiblePlayers);
            }
        }
        System.out.println("Type the name of the player you want to elect as chancellor (except yourself)");

        boolean valid = false;
        while (!valid) {
            String inputName = scanner.next();
            if (players.contains(inputName)) { // Check if player is in the game
                if (president.equals(inputName)) { // Check if president chose themselves
                    System.out.println("You can't nominate yourself as chancellor! Try again");
                } else {
                    if (!ineligiblePlayers.contains(inputName)) { // Check if player weren't in the last elected parliament
                        valid = true;
                        chancellor = inputName;

                    } else {
                        System.out.println("This player was in the last parliament and is therefore ineligible. Try again");
                    }
                }
            } else {
                System.out.println("Not a valid name. Try again");
            }
        }
    }

    /**
     * Let every player vote for the current parliament secretly before tallying up the votes then displaying
     * the number of Yes's and No's, alongside each player's vote and whether or not the vote has passed
     * <p>
     * If the vote passes, announce the elected parliament and set the election tracker to 0. If there are at least
     * 3 Fascist policies announce whether or not the current chancellor is Hitler. If Hitler is the chancellor
     * end the game, announcing the Fascists as the victors
     * <p>
     * If the vote doesn't pass increment the electron tracker and announce so
     */
    private void vote() {
        System.out.println("VOTING PHASE:");

        HashMap<String, String> votes = new HashMap<>();

        for (String player : players) {
            devicePass(player);
            System.out.println("Are you in favour of this parliament [Yes/No]:");
            System.out.println("President = " + president);
            System.out.println("Chancellor = " + chancellor);

            String inputVote = "";

            boolean valid = false;
            while (!valid) {
                inputVote = scanner.next();
                if (inputVote.equals("Yes") || inputVote.equals("No")) {
                    valid = true;
                } else {
                    System.out.println("Invalid input. Type Yes or No");
                }
            }
            votes.put(player, inputVote);
            devicePassConcluded();
        }

        int yesVotes = 0;
        int noVotes = 0;

        for (String player : players) {

            if (votes.get(player).equals("Yes")) {
                yesVotes++;
            }

            if (votes.get(player).equals("No")) {
                noVotes++;
            }

        }
        System.out.println("Show the device to all players");

        System.out.println("The votes are in:");
        System.out.println(votes);

        System.out.println("Yes: " + yesVotes);
        System.out.println("No: " + noVotes);

        voteSucceeds = !(noVotes >= yesVotes);

        if (voteSucceeds) {
            System.out.println("The vote has succeeded. Your new president is " + president + " and your new chancellor is " + chancellor);
            setIneligiblePlayers();
        } else {
            System.out.println("The vote has failed");
        }

        wait(10000);

        if (!voteSucceeds) {
            incrementElectionTracker();
        } else {

            if (fascistPolicies >= 3) {
                System.out.println("There are at least 3 fascist policies in play. Is the new chancellor Hitler?");
                devicePass(chancellor);
                System.out.println("Show the device to the other players");
                wait(5000);
                if (secretRoles.get(chancellor).equals("Hitler")) {
                    endGame("You have elected Hitler as chancellor. The fascists win!");
                } else {
                    System.out.println("The chancellor is not Hitler");
                }
            }
            electionTracker = 0;
        }
    }

    /**
     * Set the current president and chancellor as the 2 ineligible players
     */
    private void setIneligiblePlayers() {
        if (numberOfPlayers <= 5) {
            ineligiblePlayers.clear();
            ineligiblePlayers.add(chancellor);
        } else {
        ineligiblePlayers.clear();
        ineligiblePlayers.add(president);
        ineligiblePlayers.add(chancellor);
    }
    }

    /**
     * Wait a given amount of time
     *
     * @param timeMS The time to wait in milliseconds
     */
    private void wait(int timeMS) {
        try {
            Thread.sleep(timeMS); // Change timeMS to 0 to speed up testing
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Play the top from a given stack on the board
     * Activate any win conditions necessary
     * If this is due to a successful vote, activate any executive actions necessary
     */
    private void playPolicyFromStack(List<String> stack) {
        if (stack.get(0).equals("Liberal")) {
            liberalPolicies++;
            System.out.println("A Liberal policy has been enacted");
            System.out.println("There are now " + liberalPolicies + " Liberal policies in play");
        } else if (stack.get(0).equals("Fascist")) {
            fascistPolicies++;
            System.out.println("A Fascist policy has been enacted");
            System.out.println("There are now " + fascistPolicies + " Fascist policies in play");
        }
        stack.remove(0);

        if (liberalPolicies == 5) {
            endGame("You have enacted 5 liberal policies. The liberals win!");
        }

        if (fascistPolicies == 6) {
            endGame("You have enacted 6 fascist policies. The fascist win!");
        }

        if (voteSucceeds && gameActive) {

            switch (gameBoard) {
                case 1:
                    switch (fascistPolicies) {
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:
                            policyPeek();
                            break;
                        case 4:
                            executePlayer();
                            break;
                        case 5:
                            executePlayer();
                            vetoUnlocked = true;
                            break;
                    }
                    break;
                case 2:
                    switch (fascistPolicies) {
                        case 1:
                            break;
                        case 2:
                            investigatePlayer();
                            break;
                        case 3:
                            specialElection();
                            break;
                        case 4:
                            executePlayer();
                            break;
                        case 5:
                            executePlayer();
                            vetoUnlocked = true;
                            break;
                    }
                        break;
                case 3:
                    switch (fascistPolicies) {
                        case 1:
                            investigatePlayer();
                            break;
                        case 2:
                            investigatePlayer();
                            break;
                        case 3:
                            specialElection();
                            break;
                        case 4:
                            executePlayer();
                            break;
                        case 5:
                            executePlayer();
                            vetoUnlocked = true;
                            break;
                    }
                    break;
            }
        }
    }

    /**
     * The president selects a player to be executed
     * Announce if they are Hitler or not. If they are Hitler end the game, announcing
     * the liberals as the victors. If they are not, remove them from the game.
     */
    private void executePlayer() {
        System.out.println("The president must choose a player to execute");

        devicePass(president);
        System.out.println("The names of the players playing are: " + players);
        System.out.println("Type the name of the player you want to execute (except yourself)");

        String playerExecuted = "";
        boolean valid = false;
        while (!valid) {
            String inputName = scanner.next();
            if (players.contains(inputName)) { // Check if player is in the game
                if (president.equals(inputName)) { // Check if president chose themselves
                    System.out.println("You can't execute yourself! Try again");
                } else {
                    valid = true;
                    playerExecuted = inputName;
                }
            } else {
                System.out.println("Not a valid name. Try again");
            }
        }

        System.out.println("The president has chosen to execute " + playerExecuted);
        System.out.println("Was the executed player Hitler?");
        wait(5000);

        if (secretRoles.get(playerExecuted).equals("Hitler")) {
            endGame(playerExecuted + " was Hitler. The liberals win!");
        } else {
            System.out.println(playerExecuted + " was not Hitler");
        }
        System.out.println(playerExecuted + " cannot participate for the rest of the game");
        removePlayer(playerExecuted);
    }

    /**
     * Remove a player from the game by remove them from any relevant ArrayLists and HashMaps
     * Adjust the presidentIndex if necessary, as necessary
     * @param player player to be removed
     */
    private void removePlayer(String player) { // Check is there are any bugs if the chancellor is killed

        int playerIndex = players.indexOf(player);

        players.remove(player);
        numberOfPlayers--;
        secretRoles.remove(player);
        liberal.remove(player);

        if (ineligiblePlayers.contains(player)) {
            ineligiblePlayers.remove(player);
        }

        if (playerIndex < presidentIndex) {
            presidentIndex--;
        }
    }

    /**
     * The president selects a player to be the president elect next round
     * After that round, the rotation returns to normal
     */
    private void specialElection() {
        System.out.println("The president must choose a player to elect another player to be the next president");

        devicePass(president);
        System.out.println("The names of the players playing are: " + players);
        System.out.println("Type the name of the player you want to elect as the next president (except yourself)");

        boolean valid = false;
        while (!valid) {
            String inputName = scanner.next();
            if (players.contains(inputName)) { // Check if player is in the game
                if (president.equals(inputName)) { // Check if president chose themselves
                    System.out.println("You can't nominate yourself as the next president elect! Try again");
                } else {
                    valid = true;
                    isSpecialElection = true;
                    System.out.println("The president has nominated " + inputName + " as the next president elect");
                    president = inputName;
                }
            } else {
                System.out.println("Not a valid name. Try again");
            }
        }
    }

    /**
     * The president selects a player and their party membership is secretly revealed to them
     */
    private void investigatePlayer() {
        System.out.println("The president must choose a player to investigate a their party membership");

        devicePass(president);
        System.out.println("The names of the players playing are: " + players);
        System.out.println("Type the name of the player you want to investigate (except yourself)");

        String playerInvestigated = "";
        boolean valid = false;
        while (!valid) {
            String inputName = scanner.next();
            if (players.contains(inputName)) { // Check if player is in the game
                if (president.equals(inputName)) { // Check if president chose themselves
                    System.out.println("You can't investigate yourself! Try again");
                } else {
                    valid = true;
                    playerInvestigated = inputName;
                }
            } else {
                System.out.println("Not a valid name. Try again");
            }
        }

        System.out.println("Watch the device's screen while " + playerInvestigated + " has the device");
        devicePass(playerInvestigated);
        System.out.println(playerInvestigated + " party membership is " + (liberal.get(playerInvestigated) ? "Liberal" : "Fascist"));

        devicePassConcluded();
    }

    /**
     * The top 3 policies in the deck are revealed to the president in order
     */
    private void policyPeek() {
        System.out.println("The president must look at the first 3 policies in order");

        ArrayList<String> peek = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            peek.add(deck.get(i));
        }
        devicePass(president);
        System.out.println("The top 3 cards (from top to bottom) are: " + peek);
        devicePassConcluded();
    }


    /**
     * Increment the election tracker and announce the new value. If it reaches 3,
     * play the top policy in the deck then reset it to 0
     */
    private void incrementElectionTracker() {
        electionTracker++;
        System.out.println("The election tracker has been incremented by 1");
        System.out.println("The election tracker is now on " + electionTracker);
        if (electionTracker == 3) {
            System.out.println("The top policy in the deck will automatically be played");
            playPolicyFromStack(deck);
            ineligiblePlayers.clear();
            electionTracker = 0;
        }
    }

    /**
     *
     */
    private void legislativeSession() {
        System.out.println("LEGISLATIVE SESSION");
        System.out.println("Nobody may talk until a policy is enacted");
        System.out.println("There are " + deck.size() + " policies remaining in the deck");

        ArrayList<String> hand = new ArrayList<>();

        if (deck.size() < 3) {
            newDeck();
            System.out.println("There are now " + deck.size() + " policies remaining in the deck");
        }
        for (int i = 0; i < 3; i++) {
            hand.add(deck.get(0));
            deck.remove(0);
        }

        devicePass(president);
        System.out.println("You have drawn " + hand);
        System.out.println("Select a policy to discard [1/2/3]");

        int inputIndex = 0;
        boolean valid = false;
        while (!valid) {
            inputIndex = scanner.nextInt() - 1;
            if (inputIndex >= 0 && inputIndex < 3) {
                valid = true;
            } else {
                System.out.println("You must type 1, 2 or 3. Try again");
            }
        }
        hand.remove(inputIndex);
        Collections.shuffle(hand);
        devicePassConcluded();

        boolean vetoSuccess = false;
        while (!vetoSuccess) {
            devicePass(chancellor);
            System.out.println("You have drawn " + hand);
            System.out.println("Select a policy to discard [1/2]" + (vetoUnlocked ? " If you want to veto type 3" : ""));

            valid = false;
            while (!valid) {
                inputIndex = scanner.nextInt() - 1;
                if (inputIndex >= 0 && inputIndex < (vetoUnlocked ? 3 : 2)) {
                    valid = true;
                } else {
                    System.out.println("You must type 1 or 2" + (vetoUnlocked ? " or 3. " : ". ") + "Try again");
                }
            }
            if (!(inputIndex == 2)) {
                hand.remove(inputIndex);
                vetoSuccess = true;
                playPolicyFromStack(hand);
                devicePassConcluded();
            } else {
                System.out.println("After this message disappears announce that you wish to veto this agenda");
                devicePassConcluded();

                devicePass(president);
                System.out.println("Do you wish to veto this agenda? [Yes/No]");
                System.out.println("Announce your answer after typing it");

                String inputVote = "";

                valid = false;
                while (!valid) {
                    inputVote = scanner.next();
                    if (inputVote.equals("Yes") || inputVote.equals("No")) {
                        valid = true;
                    } else {
                        System.out.println("Invalid input. Type Yes or No");
                    }
                }

                if (inputVote.equals("Yes")) {
                    hand.clear();
                    vetoSuccess = true;
                    incrementElectionTracker();
                }
            }
        }

    }

    /**
     * End the current game
     * @param message the message to display before ending the game in the format
     *                "winCondition. The winningTeam win"
     */
    private void endGame(String message) {
        System.out.println(message);
        wait(10000);
        gameActive = false;
    }
}