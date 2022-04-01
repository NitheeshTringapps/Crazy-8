package automaticCardGame;

import static cardgame.Card.getDeck;
import java.util.*;
import cardgame.Card;
import cardgame.Card.Rank;

/**
 * @author Nitheesh
 * This class is called by Crazy8Game class
 * This AutomaticGameEngine class automatically plays the game and finds the winner
 */

public class AutomaticGameEngine {
	
	static List<Card> drawPile = getDeck();
	static Card discardPile;
	static Card.Suit changeSuit;
	
	/**
	 * Starts the game and finds the winner
	 * @param playerOne instance of PlayerOne.java
	 * @param playerTwo instance of PlayerTwo.java
	 * @return The winner and their score
	 */
	public String start(PlayerOne playerOne, PlayerTwo playerTwo) {
		Collections.shuffle(drawPile);
		int playerOneScore = 0;
		int playerTwoScore = 0;
		List<Card> list1 = new ArrayList<>();
		List<Card> list2 = new ArrayList<>();
		for(int i = 0; i <  5; i++) {
			list1.add(drawPile.get(0));
			drawPile.remove(0);
			list2.add(drawPile.get(0));
			drawPile.remove(0);
		}
		//initiating cards to players
		playerOne.receiveInitialCards(list1);
		playerTwo.receiveInitialCards(list2);
		
		discardPile = drawPile.get(0); //initiating discard pile
		drawPile.remove(0);
		
		while(playerOneScore < 200 && playerTwoScore < 200) { //checking whether any player score reaches 200 or more to end the tournament
			while(!playerOne.myCards.isEmpty() && !playerTwo.myCards.isEmpty() && !drawPile.isEmpty()) { //conditions to play until a game ends
				//playerOne Turn
				for(int i = 0; i < 3 && !drawPile.isEmpty(); i++) {
					if(playerOne.shouldDrawCard(discardPile, changeSuit)) {
						playerOne.receiveCard(drawPile.get(0));
						drawPile.remove(0);
					}
					else {
						Card removedCard = playerOne.playCard();
						if(removedCard.getRank() == Rank.EIGHT) {
							changeSuit = playerOne.declareSuit();
						}
						break;
					}
				}
				if(!playerOne.myCards.isEmpty() && !playerTwo.myCards.isEmpty() && !drawPile.isEmpty()) {
					//playerTwo turn
					for(int i = 0; i < 3 && !drawPile.isEmpty(); i++) {
						if(playerTwo.shouldDrawCard(discardPile, changeSuit)) {
							playerTwo.receiveCard(drawPile.get(0));
							drawPile.remove(0);
						}
						else {
							Card removedCard = playerTwo.playCard();
							if(removedCard.getRank() == Rank.EIGHT) {
								changeSuit = playerTwo.declareSuit();
							}
							break;
						}
					}
				}
			}
			//calculating players scores of each game and updating them
			playerOneScore += playerOne.getScore();
			playerTwoScore += playerTwo.getScore();
			playerOne.reset(); //resetting game if a game ends
		}
		
		//returning winner and their scores
		if(playerOneScore > playerTwoScore) {
			return ("Player Two Won and his Score is : " + playerOneScore);
		}
		else {
			return ("Player One Won and his Score is : " + playerTwoScore);
		}
	}
}
