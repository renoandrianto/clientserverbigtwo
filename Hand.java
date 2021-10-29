/**
 * The Hand class is a subclass of the CardList class, and is used to model a hand of cards. 
 * It has a private instance variable for storing the player who plays this hand. 
 * It also has methods for getting the player of this hand, checking if it is a valid hand, 
 * getting the type of this hand, getting the top card of this hand, and checking if it beats 
 * a specified hand.
 * 
 * @author Reno Andrianto
 *
 */
public abstract class Hand extends CardList{
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * 
	 * @param player the player which has this hand
	 * @param cards list of cards in the hand
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for(int i = 0; i < cards.size(); i++) {
			this.addCard(cards.getCard(i));
		}
		sort();
	}
	//the player who plays this hand.
	private CardGamePlayer player;
	/**
	 * A method for retrieving the player of this hand.
	 * 
	 * @return player of this hand
	 */
	public CardGamePlayer getPlayer() {
		return this.player;
	}
	/**
	 * A method for retrieving the top card of this hand.
	 * 
	 * @return top card of this hand
	 */
	public Card getTopCard() {
		return getCard(size() - 1);
	}
	/**
	 * A method for checking if this hand beats a specified hand.
	 * Will be overridden by subclasses of Hand.
	 * 
	 * @param hand another hand that will be compared to this hand
	 * @return true if this hand beats another hand, false otherwise
	 */
	public  boolean beats(Hand hand) {
		return false;
	}
	/**
	 * A method for checking if this is a valid hand.
	 * 
	 * @return true if valid, false if invalid
	 */
	public abstract boolean isValid();
	/**
	 * A method for returning a string specifying the type of this hand.
	 * 
	 * @return type of this hand
	 */
	public abstract String getType();
}
