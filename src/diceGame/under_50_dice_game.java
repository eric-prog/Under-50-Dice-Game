package diceGame;

import java.util.*;


class Player {
	public String name;
	public Integer score;
	public boolean isHuman;
	
	public Player(String name, Integer score, boolean isHuman) {
		this.name = name;
		this.score = score;
		this.isHuman = isHuman;
	}
}


public class under_50_dice_game {
	static Random random = new Random();
	static Scanner input = new Scanner(System.in);
		
	public static void main(String[] args) {
         printInstructions();         
         playGame(); 
	}

	
	public static int generateRollFlip() {
		int score = 0;
		int diceA = generateRandomRoll();
		int diceB = generateRandomRoll();
		System.out.println("Dice roll 1: " + diceA);
		System.out.println("Dice roll 2: " + diceB);
		boolean isHeads = generateRandomFlip();
		boolean evenDiceA = diceA % 2 == 0;
		boolean evenDiceB = diceB % 2 == 0;
		
		if (isHeads) {
			if (evenDiceA && evenDiceB) {
				score = diceA + diceB;
			} else if (!evenDiceA && !evenDiceB) {
				if(diceA > diceB) {
					score = diceA * 2;
				} else {
					score = diceB * 2;
				}
			} else {
				score = 2 * (diceA + diceB);
			}
		} else {
			if (evenDiceA && evenDiceB) {
				score = diceA * diceB;
			} else if (!evenDiceA && !evenDiceB) {
				if(diceA < diceB) {
					score = 2 * diceA;
				} else {
					score = 2 * diceB;
				}
			} else {
				score = 2 * (diceA * diceB);
			}
		}
		
		System.out.println("Points received this roll: " + score);
		return score;
	}
	

	public static void printInstructions() {
		System.out.println("|<---- Under 50 Dice Game ---->|\n\n" + "Objective of the game: To get a score as close to the score of 50\n"
				+ "without going over in each round.\n"
				+ "|--> Each player will roll two dice and flip one coin.\n"
				+ "|--> Depending on the result the player will recieve a updated score.\n"
		        + "|--> The current player will be able to go again or quit their turn.\n"
		        + "|--> If the player's score is over 50, then it goes on to the next player.\n"
		        + "|--> Once all players are finished their turns the winner of the round is output,\n"
		        + "     scores are all cleared and the next round begins.\n"
		        + "Enjoy.\n\n");
	}
	
	
	public static int generateRandomRoll() {
		return 1 + random.nextInt(6);
	}
	
	
	public static boolean generateRandomFlip() {
		int coinFace = random.nextInt(2);
		if(coinFace == 1) {
			System.out.println("Coin flip: Heads");
			return true;
		} else {
			System.out.println("Coin flip: Tails");
			return false;
		}
	}
	
	
	public static ArrayList<Player> getPlayersInput() {
		System.out.println("Enter number of human players: ");
		int numHuman = input.nextInt();
		System.out.println("Enter number of computer players: ");
		int numComputer = input.nextInt();
        input.nextLine();
        int score = 0;
        
        int totalPlayers = numHuman + numComputer;
        ArrayList<Player> playersInGame = new ArrayList<Player>();
        System.out.print("\n"); 
		
		for(int i = 0; i < numHuman; i++) {
			System.out.println("Enter player name: ");
			String name = input.nextLine();
			playersInGame.add(new Player(name, score, true));
		}
		
		for(int i = numHuman; i < totalPlayers; i++) {
			String compName = "computer" +  (i+1 - numHuman);
			playersInGame.add(new Player(compName, score, false));
		}
		
		return playersInGame;
	}
	
	
	public static ArrayList<Player> shiftArray (ArrayList<Player> allPlayers) {
		Player temp = allPlayers.get(0);
		int arraySize = allPlayers.size() - 1;
		
		for(int i=0; i<allPlayers.size()-1; i++){
   	 		allPlayers.set(i, allPlayers.get(i+1));
		}
		allPlayers.set(arraySize, temp);
		return allPlayers;
	}
	
	
	public static ArrayList<Player> shiftArray (ArrayList<Player> allPlayers, int shiftBy) {
		ArrayList<Player> shiftedArray = new ArrayList<Player>();
		int arraySize = allPlayers.size();
		
		for(int i=0; i < arraySize; i++){
			int oldIndex = (i + shiftBy) % arraySize;
			shiftedArray.add(allPlayers.get(oldIndex));
		}

		return shiftedArray;
	}
	
	
	public static ArrayList<Player> determineFirstPlayer(ArrayList<Player> allNames) {
		int highestRoll = 0;
		String firstPlayer = "";
		int playerIndex = 0;
		
		for (int i = 0; i < allNames.size(); i++) {
			int diceA = generateRandomRoll();
			int diceB = generateRandomRoll();
			int total = diceA + diceB;
			
			if (total > highestRoll) {
				firstPlayer = allNames.get(i).name;
				highestRoll = total;
				playerIndex = i;
			}
		}
		
		System.out.println(firstPlayer + " rolled the highest number: " + highestRoll + "\n" + firstPlayer + " is going first.\n");
		
		return shiftArray(allNames, playerIndex);
	}
	
	
	public static void playGame() {
		ArrayList<Player> allPlayers = getPlayersInput();
		int totalPlayers = allPlayers.size();
		int round = 1;
		
		ArrayList<Player> orderedPlayers = determineFirstPlayer(allPlayers);
		Hashtable<String, Integer> totalPlayerScores = new Hashtable<String, Integer>();
				
		while (true) {
			System.out.println("BEGINNING ROUND: " + round + " -------------------");
			
			Player roundWinner = playRound(orderedPlayers, totalPlayers);
			int newTotalScore = roundWinner.score;
			
			if (totalPlayerScores.containsKey(roundWinner.name)) {
				int previousTotalScore = totalPlayerScores.get(roundWinner.name);
				newTotalScore += previousTotalScore;
			} 
			
			totalPlayerScores.put(roundWinner.name, newTotalScore);
			System.out.println("\n" + roundWinner.name + " won the round with " + roundWinner.score + " and a total amount of points with " + newTotalScore + "\n");

			if (newTotalScore >= 1000) {
				System.out.println("\nWinner of the game: " + roundWinner.name + " with a total score of: " + roundWinner.score);
				return;
			} 
			
			round++;
			orderedPlayers = shiftArray(orderedPlayers);
		}
	}


	public static Player playRound(ArrayList<Player> newRoundPlayers, Integer totalPlayers) {
		int totalRoundScore = 0;
	    int previousPlayerScore = 0;
	    ArrayList<Player> currRoundPlayers = new ArrayList<Player>(newRoundPlayers);
	    
		Hashtable<String, Integer> currPlayerScore = new Hashtable<String, Integer>();
		Player roundWinner = null;
	    
		while (currRoundPlayers.size() != 1) {
			Iterator<Player> i = currRoundPlayers.iterator();
			
			while (i.hasNext()) {
				Player player = i.next();
				if (currRoundPlayers.size() == 1) {
					break;
				}
				System.out.println("\n" + player.name + "'s turn: ======================");
				
				int previousScore = (currPlayerScore.contains(player.name)) ? currPlayerScore.get(player.name) : 0;
				
				int finalScore = playersTurn(previousScore, previousPlayerScore, player);
				
				if (finalScore == 50) {
					totalRoundScore = 50 * totalPlayers;
					roundWinner = player;
					return new Player(roundWinner.name, totalRoundScore, player.isHuman);
				} else if (finalScore > 50) {
					i.remove();
					totalRoundScore += finalScore;
				} else {
					currPlayerScore.put(player.name, finalScore);
					previousPlayerScore = finalScore;
				}
			} 
		}
		
		if(roundWinner == null) {
			roundWinner = currRoundPlayers.get(0);
		}
		return new Player(roundWinner.name, totalRoundScore, roundWinner.isHuman);
	}
	
	
	public static int playersTurn(int playersPrevScore, int goal, Player player) {
		int current_score = generateRollFlip();
		int totalScore = playersPrevScore + current_score;
		System.out.println("The score of " + player.name + " is " + totalScore);
		
		if(totalScore < 50) {
			if (totalScore <= goal) {
				System.out.println("You must get a score above the previous players score: " + goal +
						"\nRolling again!\n");
				return playersTurn(totalScore, goal, player);
			}
			else {
				if (player.isHuman && playerOptions()) {
					return playersTurn(totalScore, goal, player);
				}
				return totalScore;
			}
		} else {
		    return totalScore;
		}	
	}


	public static boolean playerOptions() {
        System.out.println("|-> To go again, input \"y\"\n"
        		         + "|-> To end turn, input \"n\"");
        String playerOption = input.nextLine();
        System.out.print("\n");
        
		if(playerOption.equals("y")) {
			System.out.println("Player is rolling again.");
			return true;
		} else {
			System.out.println("Player decided to stop rolling");
			return false;
		}
	}	
}