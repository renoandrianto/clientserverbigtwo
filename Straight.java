/**
 * The Straight class is a subclass of the Hand class, and is used to model a straight
 * hand in a Big Two Card game. This class overrides beats, isValid, and getType 
 * method of Hand class.
 * 
 * @author Reno Andrianto
 *
 */
public class Straight extends Hand{
	/**
	 * A constructor for building a Straight hand with the specified player 
	 * and list of cards.
	 * 
	 * @param player the player which has this hand
	 * @param cards list of cards in the hand
	 */
	public Straight(CardGamePlayer player, CardList cards) {
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
		if(hand.getType() == "Straight") {
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
		if(this.size() == 5) {
			if(this.getTopCard().getRank() > 1 && this.getTopCard().getRank() <= 12) {
				if(this.getCard(4).getRank() == this.getCard(3).getRank() + 1
					&& this.getCard(3).getRank() == this.getCard(2).getRank() + 1 
					&& this.getCard(2).getRank() == this.getCard(1).getRank() + 1
					&& this.getCard(1).getRank() == this.getCard(0).getRank() + 1) {
					return true;
				}
				else {
					return false;
				}
			}
			else if (this.getTopCard().getRank() == 0 || this.getTopCard().getRank() == 1) {
				boolean equalAce = true;
				boolean equalTwo = true;
				int [] rankAce = {9, 10 ,11 ,12 ,0};
				int [] rankTwo = {10, 11, 12, 0, 1};
				int [] check = new int[5];
				for(int i = 0; i < 5; i ++) {
					check[i] = this.getCard(i).getRank();
				}
				for(int i = 0; i < 5; i ++) {
					if(check[i] != rankAce[i]) {
						equalAce = false;
					}
					if(check[i] != rankTwo[i]) {
						equalTwo = false;
					}
				}
				if(equalAce == true || equalTwo == true) {
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
		return "Straight";
	}
}
