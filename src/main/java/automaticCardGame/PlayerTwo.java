package automaticCardGame;

import static cardgame.Card.getDeck;
import java.util.*;

import cardgame.Card;
import cardgame.Card.Rank;
import cardgame.Card.Suit;
import cardgame.PlayerStrategy;
import cardgame.PlayerTurn;

public class PlayerTwo implements PlayerStrategy{
	
	static List<Card> myCards = new ArrayList<>();
	int playerId;
	List<Integer> opponentIds;

	/**
     * Gives the player their assigned id, as well as a list of the opponents' assigned ids.
     *
     * @param playerId    The id for this player, assigned by the game engine
     * @param opponentIds A list of ids for this player's opponents
     */
	@Override
	public void init(int playerId, List<Integer> opponentIds) {
		this.playerId = playerId;
		this.opponentIds = opponentIds;
		
	}

	/**
     * Called once at the beginning of o game to deal the player their initial cards.
     *
     * @param cards The initial list of cards dealt to this player
     */
	@Override
	public void receiveInitialCards(List<Card> cards) {
		myCards.addAll(cards);
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
	@Override
	public boolean shouldDrawCard(Card topPileCard, Suit changedSuit) {
		if(changedSuit == null) {
			for(int i = 0; i < myCards.size(); i++) {
				if(myCards.get(i).getSuit() == topPileCard.getSuit() || myCards.get(i).getRank() == topPileCard.getRank() || myCards.get(i).getRank() == Rank.EIGHT) {
					return false;
				}
			}
		}
		else {
			for(int i = 0; i < myCards.size(); i++) {
				if(myCards.get(i).getSuit() == changedSuit) {
					return false;
				}
			}
		}
		return true;
	}

	/**
     * Called when this player has chosen to draw a card from the deck.
     *
     * @param drawnCard The card that this player has drawn
     */
	@Override
	public void receiveCard(Card drawnCard) {
		myCards.add(drawnCard);
		
	}

	/**
     * Called when this player is ready to play a card (will not be called if this player drew on
     * their turn).
     * <p>
     * This will end this player's turn.
     *
     * @return The card this player wishes to put on top of the pile
     */
	@Override
	public Card playCard() {
		Card.Suit changedSuit = AutomaticGameEngine.changeSuit;
		Card topPileCard = AutomaticGameEngine.discardPile;
		if(changedSuit == null) {
			for(int i = 0; i < myCards.size(); i++) {
				if(myCards.get(i).getSuit() == topPileCard.getSuit() || myCards.get(i).getRank() == topPileCard.getRank() || myCards.get(i).getRank() == Rank.EIGHT) {
					Card card = myCards.get(i);
					myCards.remove(i);
					return card;
				}
			}
		}
		else {
			for(int i = 0; i < myCards.size(); i++) {
				if(myCards.get(i).getSuit() == changedSuit) {
					Card card = myCards.get(i);
					myCards.remove(i);
					AutomaticGameEngine.changeSuit = null;
					return card;
				}
			}
		}
		return null;
	}

	/**
     * Called if this player decided to play a "8" card to ask the player what suit they would like
     * to declare.
     * <p>
     * @return suit of first card of this player as per this strategy
     */
	@Override
	public Suit declareSuit() {
		return myCards.get(0).getSuit();
	}

	@Override
	public void processOpponentActions(List<PlayerTurn> opponentActions) {
		
	}

	/**
     * Called for resetting any state between games.
     */
	@Override
	public void reset() {
		AutomaticGameEngine.drawPile = getDeck();
		List<Card> list1 = new ArrayList<>();
		List<Card> list2 = new ArrayList<>();
		for(int i = 0; i <  5; i++) {
			list1.add(AutomaticGameEngine.drawPile.get(0));
			AutomaticGameEngine.drawPile.remove(0);
			list2.add(AutomaticGameEngine.drawPile.get(0));
			AutomaticGameEngine.drawPile.remove(0);
		}
		PlayerOne.myCards = list1;
		PlayerTwo.myCards = list2;
		AutomaticGameEngine.discardPile = AutomaticGameEngine.drawPile.get(0);
		AutomaticGameEngine.drawPile.remove(0);
	}

	/**
     * Returns the score of each player
     */
	@Override
	public int getScore() {
		int sum = 0;
		if(myCards.isEmpty()) {
			return 0;
		}
		for(Card card : myCards) {
			sum += card.getPointValue();
		}
		return sum;
	}
}
