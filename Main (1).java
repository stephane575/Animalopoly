import java.util.*;

public class Main {
    ArrayList<Player> players = new ArrayList<Player>();
    Animal[] animals;
    Board board;
    Card card;
    Dice dice;

    public static void main(String[] args) {
        Main main = new Main();
        main.game();
    }

    public void game() {
        int playerCount;
        Player playerAdd;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of players in the game: ");
        playerCount = scanner.nextInt();//takes in number of players in game
        for (int i = 0; i < playerCount; i++) {
            playerAdd = addPlayer(i);//creates players one by one
            players.add(playerAdd);//adds player to array list
            System.out.println("Player " + i + " has been added to the game.");
        }

        // Assign all other attributes
        board = new Board(players.size());
        card = new Card();
        dice = new Dice();

        animals = new Animal[26];
        animals[0] = (new Animal("Alligator", 1, 500, 100));
        animals[1] = (new Animal("Buffalo", 2, 550, 125));
        animals[2] = (new Animal("Cat", 3, 600, 150));
        animals[3] = (new Animal("Dog", 4, 650, 175));
        animals[4] = (new Animal("Elephant", 5, 700, 200));
        animals[5] = (new Animal("Fish", 6, 750, 225));
        animals[6] = (new Animal("Giraffe", 7, 800, 250));
        animals[7] = (new Animal("Horse", 8, 850, 275));
        animals[8] = (new Animal("Iguana", 9, 900, 300));
        animals[9] = (new Animal("Jellyfish", 10, 950, 325));
        animals[10] = (new Animal("Koala", 11, 1000, 350));
        animals[11] = (new Animal("Lizard", 12, 1050, 375));
        animals[12] = (new Animal("Null", 13, 0, 0));
        animals[13] = (new Animal("Mongoose", 14, 1100, 400));
        animals[14] = (new Animal("Ostrich", 15, 1150, 425));
        animals[15] = (new Animal("Panda", 16, 1200, 450));
        animals[16] = (new Animal("Quail", 17, 1250, 475));
        animals[17] = (new Animal("Rabbit", 18, 1300, 500));
        animals[18] = (new Animal("Snake", 19, 1350, 525));
        animals[19] = (new Animal("Tiger", 20, 1400, 550));
        animals[20] = (new Animal("Unicornfish", 21, 1450, 575));
        animals[21] = (new Animal("Vole", 22, 1500, 600));
        animals[22] = (new Animal("Wolf", 23, 1550, 625));
        animals[23] = (new Animal("Xenops", 24, 1600, 650));
        animals[24] = (new Animal("Yak", 25, 1650, 675));
        animals[25] = (new Animal("Null", 26, 0, 0));



        while (true) {
            int shift = 0;
            for (int i = 0; i < players.size(); i++) {//loops through each player
                turn(players.get(i - shift));
                if (players.get(i - shift).getMoney() < 0) {
                    players.remove(i - shift);
                    shift++;
                    checkWinner();
                }
            }
        }
    }

    private Player addPlayer(int playerNumber) {
        String name;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the next player: ");
        name = scanner.nextLine();
        return new Player(name, playerNumber);
    }

    private int turn(Player player) {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("It's player " + player.getPlayerNum() + "'s turn!");
        //roll the dice and store value
        int[] diceValues = dice.rollDice();
        int die1 = diceValues[0];
        int die2 = diceValues[1];
        System.out.println("You rolled " + die1 +" and " + die2 + "!");
        boolean checkDouble = dice.checkDouble();
        if (checkDouble) {
            card.getRandomCard(player);//makes the player draw a card if a double is rolled
            if (player.getMoney() < 0) {//if bankrupt
                //end turn
                return 0;

            }
        }
        board.movePlayer(player.getPlayerNum(), die1 + die2, player, animals, players);
        return 1;
    }

    private void checkWinner() {
        if (players.size() == 1) {
            System.out.println(players.get(0).getName() + " has won the game!");
            System.exit(0);
        }
    }
}