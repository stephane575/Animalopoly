import java.util.Scanner;
import java.util.ArrayList;

public class Board {
    private int[] board;
    private int[] playerLocations;

    public Board(int numOfPlayers) {
        board = new int[26];
        playerLocations = new int[numOfPlayers];//initialises attributes

        for (int i = 0; i < numOfPlayers; i++) {
            board[i] = 0;//sets each player to start at space 0 (the start)
        }

        for (int i = 0 ; i<=25 ; i++){
            board[i] = i;//sets each space to have it's corresponding number
        }
    }

    public void displayBoard(){

        for (int i = 0; i < 8; i++) {
            System.out.print("(" + board[i] + ") "); //prints each space on the top row
        }
        System.out.println();
        for (int i = 0; i < 5; i++) {
            System.out.print("(" + board[25-i] + ")");//prints the left hand side on rows starting 25 to 21
            for (int j=0; j<6; j++){
                System.out.print("    "); // creates spaces in the middle so that the total grid displays neatly
            }
            System.out.println("(" + board[8+i] + ")");//prints the right hand side of the loops (spots 8 to 12)
        }
        for (int i = 0 ; i< 8 ; i++){
            System.out.print("(" + board[20-i] + ")");//prints the bottom row
        }

        System.out.println();
        for (int i = 0 ; i< playerLocations.length ; i++){
            System.out.println("Player " + i + " is at space " + playerLocations[i]);//Tells players where they are
        }
    }

    public int getPlayerLocation(int player){
        return playerLocations[player];
    }

    public void movePlayer(int playerNum, int spaces, Player player, Animal[] animals, ArrayList<Player> players){
        playerLocations[playerNum] = (spaces + playerLocations[playerNum]) % 26;  //moves the player in playerLocations
        System.out.println("Player " + playerNum + " moved " + spaces  + " spaces to space " + playerLocations[playerNum]); //tells the player what happened
        displayBoard();//shows the new status of the board
        if (playerLocations[playerNum] == 0){
            System.out.println("player "+playerNum+" gained 1000 for landing on start and now has: " + player.addMoney(1000));  //adds 1000 to player's money if they land on start and tells the user their balance
        }else if (playerLocations[playerNum] < spaces){
            System.out.println("player "+playerNum+" gained 500 for passing start and now has: " + player.addMoney(500));  //adds 500 to player's money if they pass start and tells the user their balance
        }else if (playerLocations[playerNum] == 13){
            player.setMissTurn(true); //tells the player to miss a turn if they land on 13
            System.out.println("player "+playerNum+" will miss next turn since they landed on 13");
        }
        displayPlayerOptions(playerNum,player,animals,players); //will display the options for the user to take

    }

    private void displayPlayerOptions(int playerNum,Player player, Animal[] animals, ArrayList<Player> players){
        Animal animal = animals[12];
        if (!(playerLocations[playerNum] == 0 || playerLocations[playerNum] == 13)){
            //display options based on animal at the player's location
            for (int i=0 ; i<animals.length ; i++) {
                if (playerLocations[playerNum] == animals[i].animalPosition()) {//finds the animal at the same location
                    animal = animals[i];
                }
            }
            if (animal.getOwner()==-1){   //check if the animal has an owner
                if (player.getMoney() < animal.getPrice()) {
                    System.out.println("You don not have enough money for this animal");  //tells user they don't have money to buy the animal
                }else {
                    System.out.println("Would you like to buy the " + animal.getName() + " for £" + animal.getPrice());//offers player to buy animal at location
                    System.out.println("You currently have £"+player.getMoney());

                    int option;
                    do {

                    System.out.println("Type 1 for buy");
                    System.out.println("Type 2 for skip");
                    Scanner scanner = new Scanner(System.in);
                    option = scanner.nextInt();          //takes input

                    }  while (!( option==1 || option ==2));  //loops until valid input
                    if (option == 1){
                        animal.setOwner(playerNum);
                        player.subtractMoney(animal.getPrice());
                        System.out.println("Player " + playerNum + " bought the " + animal.getName() + " and has £" + player.getMoney() + " left.");
                    }

                }
            }else{
                int payment = animal.getVisitCost();
                player.subtractMoney(payment);//takes money out of the player who landed on it
                for (int i = 0; i<players.size();i++){//goes through players
                    if (players.get(i).getPlayerNum() == animal.getOwner()){//checks if the player owns the animal
                        players.get(i).addMoney(payment); //gives money to player that owns animal
                    }
                }

                System.out.println("Player " + playerNum + " payed £" + payment + " to player " + animal.getOwner() + " since they own the " + animal.getName());//tells user what happened
            }
        }
        //display options for upgrade of owned animals if any

       ArrayList<Animal> ownedAnimals= new ArrayList<Animal>();
       for (int i=0 ; i<animals.length ; i++) {
           if (animals[i].getOwner() == playerNum){
              ownedAnimals.add(animals[i]); //adds all animals owned by the player to a list
           }
       }
       if (ownedAnimals.isEmpty()){
           System.out.println("You don't have any animals to upgrade.");

       }else {
           int exit;
           do {
               exit = 1;

               //this it to make sure the user does not see a single option to exit the upgrade screen if they can't buy any animals
               for (int i = 0; i < ownedAnimals.size(); i++) {
                   if ( ownedAnimals.get(i).getLevel() < 4 && player.getMoney() > ownedAnimals.get(i).getPrice()) {
                       exit = 0;
                   }
               }
               if (exit == 1) {
                   System.out.println("You can't upgrade any animals.");
               }else{
                   System.out.println("you currently have £" + player.getMoney());
                   System.out.println("You can upgrade the following animals:");
                   for (int i = 0; i < ownedAnimals.size(); i++) {
                       if (ownedAnimals.get(i).getLevel() < 4 && player.getMoney() > ownedAnimals.get(i).getPrice()) {
                           System.out.println("(" + (i + 1) + ") " + ownedAnimals.get(i).getName() + " to level " + ownedAnimals.get(i).getLevel() + " for £" + ownedAnimals.get(i).getPrice()); // displays animals player can upgrade
                       }
                   }
                   System.out.println("Enter the first letter of the animal you want to upgrade or enter E to exit");
                   Scanner scanner = new Scanner(System.in);
                   String input = scanner.nextLine();
                   input = input.toLowerCase(); //takes the users input
                   if (input.equals("e")) {
                       exit = 1;  //exits the loop if the player wants to
                   } else {
                       for (int i = 0; i < ownedAnimals.size(); i++) {
                           if (ownedAnimals.get(i).getName().toLowerCase().charAt(0) == input.charAt(0)) {
                               ownedAnimals.get(i).upgrade();   //upgrades the animal the user wants to
                               player.subtractMoney(ownedAnimals.get(i).getPrice());   //takes money from user
                               System.out.println("Player " + playerNum + " has upgraded the " + ownedAnimals.get(i).getName() + " and has £" + player.getMoney() + " left.");   //tells user what happened
                           }
                       }
                   }

               }
           } while(exit == 0);
       }
    }
}