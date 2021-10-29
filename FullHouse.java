/**
 * The FullHouse class is a subclass of the Hand class, and is used to model a full
 * house hand in a Big Two Card game. This class overrides getTopCard, beats, 
 * isValid, and getType method of Hand class.
 * 
 * @author Reno Andrianto
 *
 */
public class FullHouse extends Hand{
	/**
	 * A constructor for building a Full House hand with the specified player 
	 * and list of cards.
	 * 
	 * @param player the player which has this hand
	 * @param cards list of cards in the hand
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/**
	 * A method for retrieving the top card of this hand.
	 * 
	 * @return top card of this hand
	 */
	public Card getTopCard() {
		int maxSuit = -1;
		if(this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank()
				&& this.getCard(3).getRank() == this.getCard(4).getRank()) {
			for(int i = 0; i < 3; i++) {
				if(this.getCard(i).getSuit() > maxSuit) {
					maxSuit = this.getCard(i).getSuit();
				}
			}
			return new BigTwoCard(maxSuit, this.getCard(0).getRank());
		}
		else {
			maxSuit = -1;
			for(int i = 2; i < 5; i++) {
				if(this.getCard(i).getSuit() > maxSuit) {
					maxSuit = this.getCard(i).getSuit();
				}
			}
			return new BigTwoCard(maxSuit, this.getCard(2).getRank());
		}
	}
	/**
	 * A method for checking if this hand beats a specified hand.
	 * 
	 * @param hand another hand that will be compared to this hand
	 * @return true if this hand beats the other hand, false otherwise
	 * 
	 */
	public boolean beats(Hand hand) {
		if(hand.getType() == "Full House") {
			if(this.getTopCard().compareTo(hand.getTopCard()) == 1) {
				return true;
			}
			else {
				return false;
			}
		}
		else if (hand.getType() == "Straight" || hand.getType() == "Flush") {
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
		if(this.size() == 5) {
			if(this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank()
					&& this.getCard(3).getRank() == this.getCard(4).getRank()
					||
					this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(2).getRank() == this.getCard(3).getRank()
					&& this.getCard(3).getRank() == this.getCard(4).getRank()
					) {
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
	 * A method for returning a string specifying the type of this hand.
	 * 
	 * @return type of this hand
	 */
	public String getType() {
		return "Full House";
	}
}
