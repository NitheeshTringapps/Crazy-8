package cardgame;

import static cardgame.Card.getDeck;

import java.util.*;
import cardgame.Card.Rank;
import cardgame.Card.Suit;

/**
 * @author Nitheesh
 * Created a gameEngine to play crazy 8 game
 * for n number of players individually and manually
 */

/*
 * Note: Check Crazy8Game class of automaticCardGame package to see the implementation
 * of 2 players playing crazy 8 game automatically and finds the result
 */

public class GameEngine implements PlayerStrategy{
	
	private static List<Card> drawPile = getDeck();
	private static Card discardPile;
	static Map<PlayerTurn, List<Card>> allPlayersAndTheirCards = new LinkedHashMap<>();
	static int maxScore = 0;
	static boolean cheated = true;
	static boolean out = false;
	static boolean max = false;
	static List<Integer> playersScore;
	static Card.Suit declaredSuit = null;

	/**
        * Gives the player their assigned id, as well as a list of the opponents' assigned ids.
        * <p>
        * This method will be called by the game engine once at the very beginning (before any games
        * are started), to allow the player to set up any initial state.
        *
        * @param playerId    The id for this player, assigned by the game engine
        * @param opponentIds A list of ids for this player's opponents
        */
	@Override //initiates players and allocates their initial cards
	public void init(int playerId, List<Integer> opponentIds) {
		//First player
		PlayerTurn player = new PlayerTurn();
		player.playerId = playerId;
		List<Card> list = new ArrayList<>();
		for(int i = 0; i <  5; i++) {
			list.add(drawPile.get(0));
			drawPile.remove(0);
		}
		allPlayersAndTheirCards.put(player, list);
		
		//Other players
		for(int i = 0; i < opponentIds.size(); i++) {
			PlayerTurn tempPlayer = new PlayerTurn();
			tempPlayer.playerId = opponentIds.get(i);
			List<Card> tempList = new ArrayList<>();
			for(int j = 0; j <  5; j++) {
				tempList.add(drawPile.get(0));
				drawPile.remove(0);
			}
			allPlayersAndTheirCards.put(tempPlayer, tempList);
		}
		
		//Assigning score as 0 for each players
		int totalPlayers = opponentIds.size()+1;
		playersScore = new ArrayList<Integer>();
		for(int i = 0; i < totalPlayers; i++) {
			playersScore.add(0);
		}
		
		//Initiating discard pile card
		discardPile = drawPile.get(0);
		drawPile.remove(0);
	}


	@Override //Prints current player's cards
	public void receiveInitialCards(List<Card> cards) {
		System.out.println("Your current cards are :");
		for(Card card:cards) {
			System.out.println(card.getSuit() + " " + card.getRank());
		}
		System.out.println("----------------------------------");
	}

	/**
     * Called to ask whether the player wants to draw this turn. Gives this player the top card of
     * the discard pile at the beginning of their turn, as well as an optional suit for the pile in
     * case a "8" was played, and the suit was changed.
     * <p>
     * By having this return true, the game engine will then call receiveCard() for this player.
     * Otherwise, playCard() will be called.
     *
     * @param topPileCard The card currently at the top of the pile
     * @param changedSuit The suit that the pile was changed to as the result of an "8" being
     *                    played. Will be null if no "8" was played.
     * @return whether or not the player wants to draw
     */
	@Override //Ask the player whether to draw a card or not
	public boolean shouldDrawCard(Card topPileCard, Suit changedSuit) {
		if(changedSuit != null) {
			System.out.println("You Should choose  cards only of Suit : " + changedSuit);
		}
		else {
			System.out.println("Top Pile card is : " + topPileCard.getSuit() + " " + topPileCard.getRank());
		}
		Scanner sc = new Scanner(System.in);
		System.out.println("Should you need to draw the Turn?");
		System.out.println("Enter 1 for YES or 0 for NO");
		int choice = sc.nextInt();
		if(choice == 1) {
			return true;
		}
		else {
			return false;
		}
	}


	@Override //removes the card from deck when a player receives a card
	public void receiveCard(Card drawnCard) {
		drawPile.remove(0);
	}

	@Override //Gets a card as input from console for a player
	public Card playCard() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the Suit of card you want to put in discard pile:");
		String suit = sc.nextLine();
		System.out.println("Enter the Rank of card you want to put in discard pile:");
		String rank = sc.nextLine();
		Card card = new Card(Suit.valueOf(suit), Rank.valueOf(rank));
		return card;
	}

	/**
     * Called if this player decided to play a "8" card to ask the player what suit they would like
     * to declare.
     * <p>
     * This player should then return the Card.Suit enum that it wishes to set for the discard
     * pile.
     */
	@Override //Gets a Suit input from user when the player need to declare a suit
	public Suit declareSuit() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the Suit of card you want to declare for the next player to choose:");
		String suit = sc.nextLine();
		return Suit.valueOf(suit);
	}


	@Override
	public void processOpponentActions(List<PlayerTurn> opponentActions) {
		// TODO Auto-generated method stub
		
	}

	@Override //Rest the Game by resetting deck, player's corresponding card & discard pile
	public void reset() {
		drawPile = getDeck();
		Collections.shuffle(drawPile);
		for(Map.Entry<PlayerTurn, List<Card>> m: allPlayersAndTheirCards.entrySet()) {
			List<Card> tempList = new ArrayList<>();
			for(int j = 0; j <  5; j++) {
				tempList.add(drawPile.get(0));
				drawPile.remove(0);
			}
			allPlayersAndTheirCards.put(m.getKey(), tempList);
		}
		discardPile = drawPile.get(0);
		drawPile.remove(0);
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	//Checks whether a player has cards to play by comparing top card from the discard pile
	public boolean isPresentToDraw(List<Card> currentPlayerCards) {
		for(int i = 0; i < currentPlayerCards.size(); i++) {
			if(currentPlayerCards.get(i).getSuit() == discardPile.getSuit() || currentPlayerCards.get(i).getRank() == discardPile.getRank() || currentPlayerCards.get(i).getRank() == Rank.EIGHT) {
				return true;
			}
		}
		return false;
	}
	
	//Returns the current player's score
	public int getCurrentPlayerScore(List<Card> currentPlayerCards) {
		if(currentPlayerCards.isEmpty()) {
			return 0;
		}
		int sum = 0;
		for(Card card: currentPlayerCards) {
			sum += card.getPointValue();
		}
		return sum;
	}
	
	//Checks whether User input card is present with the corresponding player
	public boolean playCardPresentWithPlayer(Card playCard, List<Card>  currentPlayerCards) {
		for(int i = 0; i < currentPlayerCards.size(); i++) {
			if(currentPlayerCards.get(i).getSuit() == playCard.getSuit() && currentPlayerCards.get(i).getRank() == playCard.getRank()) {
				return true;
			}
		}
		return false;
	}
	
public static void main(String[] args) {
		
		//Shuffling deck
		Collections.shuffle(drawPile);
		
		//creating a instance of GameEngine and creating instances for players with allocating their initial cards
		GameEngine gameEngine = new GameEngine();
//		gameEngine.init(1, new ArrayList<Integer>(Arrays.asList(2))); //UnComment for 2 players
		gameEngine.init(1, new ArrayList<Integer>(Arrays.asList(2,3,4))); //UnComment for 4 players
//		gameEngine.init(1, new ArrayList<Integer>(Arrays.asList(2,3,4,5))); //Uncomment for 5 players
		
		//running loop until anyone's score reaches 200 and above
		while(maxScore < 200) {
			int playerIndex = 0;
			for(Map.Entry<PlayerTurn, List<Card>> m: allPlayersAndTheirCards.entrySet()) { //Looping through all players's turn
				System.out.println("----------------------------------");
				System.out.println("Player:" + m.getKey().playerId);
				gameEngine.receiveInitialCards(m.getValue());
				
				if(gameEngine.shouldDrawCard(discardPile, declaredSuit)) { //asking whether to draw or skipping their move to opponent
					List<Card> currentPlayerCards = m.getValue();
					
					if(gameEngine.isPresentToDraw(currentPlayerCards)) { //checks whether card is available to draw: If no card present receive a new card from deck in else part
						Card playCard = gameEngine.playCard();
						
						//TO CHECK A CARD WITH  USER ENTERED SUIT AND RANK IS PRESENT IN PLAYER
						if(gameEngine.playCardPresentWithPlayer(playCard, currentPlayerCards)) {
							
							//Looping towards all the cards of current player and finding the required index of card
							for(int i = 0; i < currentPlayerCards.size(); i++) {
								if(currentPlayerCards.get(i).getSuit() == playCard.getSuit() && (playCard.getSuit()==discardPile.getSuit() || playCard.getRank()==discardPile.getRank() || playCard.getRank()==Rank.EIGHT)) {
									if(declaredSuit != null) {
										declaredSuit = null;
									}
									if(playCard.getRank() == Rank.EIGHT) { //Checking EIGHT for declaring suit
										declaredSuit = gameEngine.declareSuit();
										Card card = new Card(declaredSuit, discardPile.getRank());
										discardPile = card;
									}else {
										discardPile = playCard;
										currentPlayerCards.remove(i);
									}
									cheated = false;
									break;
								}
							}
						}
						else {//If user entered card is not present, this else part runs
							System.out.println("Entered Card is not present with player!");
							cheated = true;
							out = true;
							break;
						}
						if(cheated == true) {//To break out of Map Entry loop
							out = true;
							System.out.println("Chosen Card cannot be played!");
							break;
						}
					}
					else {//If no card is present in player's deck, this else part will run and get a new card from Deck
						System.out.println("No cards available to play from your deck. So recieving card from the deck..........");
						Card recieveCard = drawPile.get(0);
						gameEngine.receiveCard(recieveCard);
						currentPlayerCards.add(recieveCard);
						System.out.println("Recieved Card is: " + recieveCard.getSuit() + " " + recieveCard.getRank());
					}
				}
				int currentPlayerScore = gameEngine.getCurrentPlayerScore(m.getValue());//gets current Player's score
				if(currentPlayerScore+ playersScore.get(playerIndex) > maxScore) {//setting highest score
					maxScore = currentPlayerScore+ playersScore.get(playerIndex);
				}
				if(maxScore >= 200) {//To break out of Map Entry loop if max score of a player is greater than 200
					max = true;
					break;
				}
				
				//If any of the player's card (deck) is empty, storing every player's scores of that round in playersScore list and resetting the deck and player's card
				if(currentPlayerScore == 0) {
					int index = 0;
					for(Map.Entry<PlayerTurn, List<Card>> x: allPlayersAndTheirCards.entrySet()) {
						playersScore.add(index, (playersScore.get(index) + gameEngine.getCurrentPlayerScore(x.getValue())));
						index++;
					}
					gameEngine.reset();//resetting game
				}
				playerIndex++;
				cheated = true;
			}
			if(out == true || max == true) {//To break out of while loop
				break;
			}
		}
		if(out == true) {//Runs when player makes a wrong move
			System.out.println("Cheated! Game Over!");
		}
		else if(max == true){//Prints the winner
			System.out.println("Game reached above 200");
			int minimumScore = Collections.min(playersScore);
			int minIndex = playersScore.indexOf(minimumScore);
			int index = 0;
			for(Map.Entry<PlayerTurn, List<Card>> m: allPlayersAndTheirCards.entrySet()) {
				if(index == minIndex) {
					System.out.println("The winner is Player : " + m.getKey().playerId);
				}
				index++;
			}
		}
	}

}
