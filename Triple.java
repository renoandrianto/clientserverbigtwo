/**
 * The Triple class is a subclass of the Hand class, and is used to model a triple
 * hand in a Big Two Card game. This class overrides beats, isValid, and getType 
 * method of Hand class.
 * 
 * @author Reno Andrianto
 *
 */
public class Triple extends Hand{
	/**
	 * A constructor for building a Triple hand with the specified player 
	 * and list of cards.
	 * 
	 * @param player the player which has this hand
	 * @param cards list of cards in the hand
	 */
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/**
	 * A method for checking if this hand beats a specified hand.
	 * 
	 * @param hand another hand that will be compared to this hand
	 * @return true if this hand beats the other hand, false otherwise
	 * 
	 */
	public boolean beats(Hand hand) {
		if(hand.getType() == "Triple") {
			if(this.getTopCard().compareTo(hand.getTopCard()) == 1) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	/**
	 * A method for checking if this is a valid hand.
	 * 
	 * @return true if valid, false if invalid
	 */
	public boolean isValid() {
		if(this.size() == 3 && this.getCard(0).getRank() == this.getCard(1).getRank()
				&& this.getCard(1).getRank() == this.getCard(2).getRank()) {
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * A method for returning a string specifying the type of this hand.
	 * 
	 * @return type of this hand
	 */
	public String getType() {
		return "Triple";
	}
}
