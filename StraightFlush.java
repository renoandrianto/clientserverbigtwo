/**
 * The StraightFlush class is a subclass of the Hand class, and is used to model a 
 * straight flush hand in a Big Two Card game. This class overrides beats, 
 * isValid, and getType method of Hand class.
 * 
 * @author Reno Andrianto
 *
 */
public class StraightFlush extends Hand{
	/**
	 * A constructor for building a Straight Flush hand with the specified player 
	 * and list of cards.
	 * 
	 * @param player the player which has this hand
	 * @param cards list of cards in the hand
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
		Straight straight = new Straight(player, cards);
		Flush flush = new Flush (player, cards);
		isStraight = straight.isValid();
		isFlush = flush.isValid();
	}
	//boolean variable that determines whether the Hand is a straight or not
	private boolean isStraight;
	//boolean variable that determines whether the Hand is a flush or not
	private boolean isFlush;
	/**
	 * A method for checking if this hand beats a specified hand.
	 * 
	 * @param hand another hand that will be compared to this hand
	 * @return true if this hand beats the other hand, false otherwise
	 * 
	 */
	public boolean beats(Hand hand) {
		if(hand.getType() == "Straight Flush") {
			if(this.getTopCard().compareTo(hand.getTopCard()) == 1) {
				return true;
			}
			else {
				return false;
			}
		}
		else if (hand.getType() == "Straight" || hand.getType() == "Flush" || hand.getType() == "Full House"
				|| hand.getType() == "Quad") {
			return true;
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
		if (isStraight && isFlush) {
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
		return "Straight Flush";
	}
}
