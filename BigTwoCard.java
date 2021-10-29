/**
 * The BigTwoCard class is a subclass of the Card class, and is used to model a 
 * card used in a Big Two card game. It overrides the compareTo() method 
 * it inherits from the Card class to reflect the ordering of cards used in a 
 * Big Two card game.
 * 
 * @author Reno Andrianto
 *
 */
public class BigTwoCard extends Card{
	/**
	 * A constructor for building a card with the specified suit and rank. 
	 * 
	 * @param suit an integer between 0 and 3
	 * @param rank an integer between 0 and 12
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit, rank);
	}
	/**
	 * Compares this card with the specified card for order.
	 * 
	 * @param card
	 *            the card to be compared
	 * @return a negative integer, zero, or a positive integer as this card is
	 *         less than, equal to, or greater than the specified card
	 */
	public int compareTo(Card card) {
		if (this.rank >= 2 && card.rank >= 2) {
			return super.compareTo(card);
		}
		else if (this.rank < 2 && card.rank < 2) {
			return super.compareTo(card);
		}
		else if (this.rank < 2 && card.rank >= 2) {
			return 1;
		}
		else {
			return -1;
		}
	}
}
