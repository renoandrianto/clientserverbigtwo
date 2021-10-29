/**
 * The BigTwoDeck class is a subclass of the Deck class, and is 
 * used to model a deck of cards used in a Big Two card game. 
 * It overrides the initialize() method it inherits from the 
 * Deck class to create a deck of Big Two cards.
 * 
 * @author Reno Andrianto
 *
 */
public class BigTwoDeck extends Deck{
	/**
	 * A method for initializing a deck of Big Two cards. It should remove 
	 * all cards from the deck, create 52 Big Two cards and add them to 
	 * the deck.
	 */
	public void initialize() {
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard card = new BigTwoCard(i, j);
				addCard(card);
			}
		}
	}
}
