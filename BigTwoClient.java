import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The BigTwoClient class implements the CardGame interface and NetworkGame interface. It 
 * is used to model a Big Two card game that supports 4 players playing over the Internet. 
 * Below is a detailed description for the BigTwoClient class.
 * 
 * @author Reno Andrianto
 *
 */
public class BigTwoClient implements CardGame, NetworkGame {
	/**
	 * A constructor for creating a Big Two client. You should (i) create 4 
	 * players and add them to the list of players; (ii) create a Big Two table which builds the 
	 * GUI for the game and handles user actions; and (iii) make a connection to the game 
	 * server by calling the makeConnection() method from the NetworkGame interface.
	 * 
	 */
	public BigTwoClient() {
		//(i)
		playerList = new ArrayList<CardGamePlayer>();
		for(int i = 0; i < 4; i++) {
			playerList.add(new CardGamePlayer());
			playerList.get(i).setName(null);
		}
		handsOnTable = new ArrayList<Hand>();
		
		started = false;
		
		//(ii) create a BigTwoTable
		table = new BigTwoTable(this);
		table.repaint();
		
		//(iii) make connection to the game server
		setPlayerName(JOptionPane.showInputDialog("Player name: "));
		setServerIP("127.0.0.1");
		setServerPort(2396);
		makeConnection();
		
	}
	
	//an integer specifying the number of players.
	private int numOfPlayers;
	//a deck of cards.
	private Deck deck;
	//a list of players.
	private ArrayList<CardGamePlayer> playerList;
	//a list of hands played on the table.
	private ArrayList<Hand> handsOnTable;
	//an integer specifying the playerID (i.e., index) of the local player.
	private int playerID;
	//a string specifying the name of the local player.
	private String playerName;
	//a string specifying the IP address of the game server.
	private String serverIP;
	//an integer specifying the TCP port of the game server.
	private int serverPort;
	//a socket connection to the game server.
	private Socket sock;
	//an ObjectOutputStream for sending messages to the server.
	private ObjectOutputStream oos;
	//an integer specifying the index of the player for the current turn.
	private int currentIdx; 
	//a Big Two table which builds the GUI for the game and handle all user actions.
	private BigTwoTable table;
	//boolean value that determines whether the game has started or not
	private boolean started;
	//boolean value that determines whether the client is connected or not
	private boolean connected;
	
	/**
	 * A method for getting boolean variable that determines whether the game has started or not.
	 * 
	 * @return started
	 */
	public boolean getStarted() {
		return started;
	}
	/**
	 * A method for getting status of connection from client to server.
	 * 
	 * @return connected
	 */
	public boolean getConnected() {
		return connected;
	}
	
	//CardGame interface methods:
	
	/**
	 * A method for getting the number of players.
	 */
	@Override
	public int getNumOfPlayers() {
		// TODO Auto-generated method stub
		return numOfPlayers;
	}
	/**
	 * A method for getting the deck of cards being used.
	 */
	@Override
	public Deck getDeck() {
		// TODO Auto-generated method stub
		return deck;
	}
	/**
	 * A method for getting the list of players.
	 */
	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		// TODO Auto-generated method stub
		return playerList;
	}
	/**
	 * A method for getting the list of hands played on the table.
	 */
	@Override
	public ArrayList<Hand> getHandsOnTable() {
		// TODO Auto-generated method stub
		return handsOnTable;
	}
	/**
	 * A method for getting the index of the player for the current turn.
	 */
	@Override
	public int getCurrentIdx() {
		// TODO Auto-generated method stub
		return currentIdx;
	}
	/**
	 * A method for starting/restarting the game with a given 
	 * shuffled deck of cards. It will (i) remove all the cards from the players as well as 
	 * from the table; (ii) distribute the cards to the players; (iii) identify the player who holds 
	 * the 3 of Diamonds; (iv) set the currentIdx of the BigTwoClient instance to the 
	 * playerID (i.e., index) of the player who holds the 3 of Diamonds; and (v) set the 
	 * activePlayer of the BigTwoTable instance to the playerID (i.e., index) of the local 
	 * player (i.e., only shows the cards of the local player and the local player can only select 
	 * cards from his/her own hand).
	 * 
	 */
	@Override
	public void start(Deck deck) {
		// TODO Auto-generated method stub
		currentIdx = 0;
		this.deck = deck;
		//(i) remove all the cards from the players as well as from the table
		for(int i = 0; i < playerList.size(); i++) {
			playerList.get(i).removeAllCards();
		}
		handsOnTable.clear();
		//(ii) distribute the cards to the players
		for(int i = 0; i < 13; i++) {
			for(int j = 0; j < 4; j++) {
				playerList.get(j).addCard(this.deck.getCard(currentIdx));
				currentIdx++;
			}
		}
		//sort cards in hand of each player
		currentIdx = 0;
		for(int i = 0; i < 4; i ++) {
			playerList.get(i).sortCardsInHand();
		}
		/* (iii) identify the player who holds
		 * the 3 of Diamonds; (iv) set the currentIdx of the BigTwoClient instance to the
		 * playerID (i.e., index) of the player who holds the 3 of Diamonds
		 */
		final BigTwoCard threeDiamond = new BigTwoCard(0,2);
		for(int i = 0; i < 4; i ++) {
			if(playerList.get(i).getCardsInHand().contains(threeDiamond) == true) {
				currentIdx = i;
				break;
			}
		}
		/* (v) set the activePlayer of the BigTwoTable instance to the playerID (i.e., index) 
		 * of the local player
		 */
		table.setActivePlayer(playerID);
		
		table.printMsg("All players are ready. Game on!");
		started = true;
	}
	/**
	 * A method for making a move by a 
	 * player with the specified playerID using the cards specified by the list of indices. This 
	 * method should be called from the BigTwoTable when the local player presses either the 
	 * “Play” or “Pass” button. It will create a CardGameMessage object of the type 
	 * MOVE, with the playerID and data in this message being -1 and cardIdx, respectively, 
	 * and send it to the game server using the sendMessage() method from the NetworkGame 
	 * interface.
	 */
	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		// TODO Auto-generated method stub
		CardGameMessage message = new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx);
		sendMessage(message);
	}
	/**
	 * A method for checking a move 
	 * made by a player. This method should be called from the parseMessage() method from 
	 * the NetworkGame interface when a message of the type MOVE is received from the game 
	 * server. The playerID and data in this message give the playerID of the player who 
	 * makes the move and a reference to a regular array of integers specifying the indices of 
	 * the selected cards, respectively. These are used as the arguments in calling the 
	 * checkMove() method.
	 */
	@Override
	public synchronized void checkMove(int playerID, int[] cardIdx) {
		// TODO Auto-generated method stub
		CardGamePlayer player = playerList.get(playerID);
		boolean gameEnd = false;
		if(cardIdx != null) {
			if (cardIdx.length == 0) {
				//System.out.println("Please select a hand to play");
				table.printMsg("Please select a hand to play");
			}
			else {
				CardList cardPlay = player.play(cardIdx);
				Hand hand = composeHand(player, cardPlay);
				if(hand != null) {
					if(handsOnTable.isEmpty()) {
						if(hand.contains(new BigTwoCard(0,2))) {
							handsOnTable.add(hand);
							player.removeCards(hand);
							currentIdx++;
							currentIdx %= 4;
							//table.setActivePlayer(currentIdx);
							//System.out.println("{" + hand.getType() + "} " + hand.toString());
							table.printMsg("{" + hand.getType() + "} " + hand.toString());
							table.repaint();
						}
						else {
							//System.out.println("{" + hand.getType() + "} " + hand.toString() + " <== Not a legal move!!!");
							table.printMsg("{" + hand.getType() + "} " + hand.toString() + " <== Not a legal move!!!");
						}
					}
					else if (player.equals(handsOnTable.get(handsOnTable.size() - 1).getPlayer())) {
						handsOnTable.add(hand);
						player.removeCards(hand);
						if(endOfGame()) {
							gameEnd = true;
						}
						else {
							currentIdx++;
							currentIdx %= 4;
							//table.setActivePlayer(currentIdx);
							//System.out.println("{" + hand.getType() + "} " + hand.toString());
							table.printMsg("{" + hand.getType() + "} " + hand.toString());
							table.repaint();
						}
						
					}
					else if (hand.beats(handsOnTable.get(handsOnTable.size() - 1)) ) {
						handsOnTable.add(hand);
						player.removeCards(hand);
						if(endOfGame()) {
							gameEnd = true;
						}
						else {
							currentIdx++;
							currentIdx %= 4;
							//table.setActivePlayer(currentIdx);
							//System.out.println("{" + hand.getType() + "} " + hand.toString());
							table.printMsg("{" + hand.getType() + "} " + hand.toString());
							table.repaint();
						}
						
					}
					else {
						//System.out.println("{" + hand.getType() + "} " + hand.toString() + " <== Not a legal move!!!");
						table.printMsg("{" + hand.getType() + "} " + hand.toString() + " <== Not a legal move!!!");
					}
					if(gameEnd) {
						//table.setActivePlayer(-1);
						table.disable();
						//System.out.println("{" + hand.getType() + "} " + hand.toString());
						table.printMsg("{" + hand.getType() + "} " + hand.toString());
						table.printMsg("");
						//System.out.println("Game ends");
						table.printMsg("Game ends");
						String dialogMessage = "";
						for(CardGamePlayer playerEnd: getPlayerList()) {
							if(playerEnd.getNumOfCards() != 0) {
								//System.out.println(playerEnd.getName() + " has " + playerEnd.getNumOfCards() + " cards in hand.");
								table.printMsg(playerEnd.getName() + " has " + playerEnd.getNumOfCards() + " cards in hand.");
								dialogMessage += (playerEnd.getName() + " has " + playerEnd.getNumOfCards() + " cards in hand.\n");
							}
							else {
								//System.out.println(playerEnd.getName() + " wins the game.");
								table.printMsg(playerEnd.getName() + " wins the game.");
								dialogMessage += (playerEnd.getName() + " wins the game.\n");
							}
							table.repaint();
						}
						JOptionPane.showMessageDialog(new JFrame(), dialogMessage);
						CardGameMessage readyMessage = new CardGameMessage(CardGameMessage.READY, -1, null);
						sendMessage(readyMessage);
					}
				}
				else {
					//System.out.println("{" + cardPlay.toString() + "}" + " <== Not a legal move!!!");
					table.printMsg("{" + cardPlay.toString() + "}" + " <== Not a legal move!!!");
				}
			}
		}
		else {
			if(handsOnTable.size() != 0 && !player.equals(handsOnTable.get(handsOnTable.size() - 1).getPlayer())) {
				currentIdx++;
				currentIdx %= 4;
				//System.out.println("[pass]");
				table.printMsg("[pass]");
				//table.setActivePlayer(currentIdx);
				table.repaint();
			}
			else {
				//System.out.println("[pass] <== Not a legal move!!!");
				table.printMsg("[pass] <== Not a legal move!!!");
			}
		}		
	}
	/**
	 * A method for checking if the game ends.
	 */
	@Override
	public boolean endOfGame() {
		// TODO Auto-generated method stub
		for(int i = 0; i < 4; i ++) {
			if(playerList.get(i).getCardsInHand().isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	//NetworkGame interface methods:
	
	/**
	 * A method for getting the playerID (i.e., index) of the local player.
	 */
	@Override
	public int getPlayerID() {
		// TODO Auto-generated method stub
		return playerID;
	}
	/**
	 * A method for setting the playerID (i.e., index) of the local player. 
	 * This method will be called from the parseMessage() method when a message 
	 * of the type PLAYER_LIST is received from the game server.
	 */
	@Override
	public void setPlayerID(int playerID) {
		// TODO Auto-generated method stub
		this.playerID = playerID;
	}
	/**
	 * A method for getting the name of the local player.
	 */
	@Override
	public String getPlayerName() {
		// TODO Auto-generated method stub
		return playerName;
	}
	/**
	 * A method for setting the name of the local player.
	 */
	@Override
	public void setPlayerName(String playerName) {
		// TODO Auto-generated method stub
		this.playerName = playerName;
	}
	/**
	 * A method for getting the IP address of the game server.
	 */
	@Override
	public String getServerIP() {
		// TODO Auto-generated method stub
		return serverIP;
	}
	/**
	 * A method for setting the IP address of the game server.
	 */
	@Override
	public void setServerIP(String serverIP) {
		// TODO Auto-generated method stub
		this.serverIP = serverIP;
	}
	/**
	 * A method for getting the TCP port of the game server.
	 */
	@Override
	public int getServerPort() {
		// TODO Auto-generated method stub
		return serverPort;
	}
	/**
	 * A method for setting the TCP port of the game server.
	 */
	@Override
	public void setServerPort(int serverPort) {
		// TODO Auto-generated method stub
		this.serverPort = serverPort;
	}
	/**
	 * A method for making a socket connection with the game server. 
	 * Upon successful connection, It will (i) create an ObjectOutputStream 
	 * for sending messages to the game server; (ii) create a thread for receiving \
	 * messages from the game server.
	 */
	@Override
	public void makeConnection() {
		// TODO Auto-generated method stub
		try {
			sock = new Socket(getServerIP(), getServerPort());
			oos = new ObjectOutputStream(sock.getOutputStream());
			
			//connected = true;
			
			//thread
			Thread readerThread = new Thread(new ServerHandler());
			readerThread.start();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * A method for parsing the messages received from the game server. 
	 * This method should be called from the thread responsible for receiving 
	 * messages from the game server. Based on the message type, different actions 
	 * will be carried out.
	 */
	@Override
	public void parseMessage(GameMessage message) {
		// TODO Auto-generated method stub
		if(message.getType() == CardGameMessage.PLAYER_LIST) {
			connected = true;
			setPlayerID(message.getPlayerID());
			String[] data = (String[]) message.getData();
			for(int i = 0; i < data.length; i ++) {
				playerList.get(i).setName(data[i]);
			}
			CardGameMessage joinRequest = new CardGameMessage(CardGameMessage.JOIN, -1, getPlayerName());
			sendMessage(joinRequest);
		}
		else if(message.getType() == CardGameMessage.JOIN) {
			String data = (String) message.getData();
			//System.out.println(data);
			playerList.get(message.getPlayerID()).setName(data);
			if(playerID == message.getPlayerID()) {
				CardGameMessage readyMessage = new CardGameMessage(CardGameMessage.READY, -1, null);
				sendMessage(readyMessage);
			}
			table.repaint();
		}
		else if(message.getType() == CardGameMessage.FULL) {
			connected = false;
			table.printMsg("Sorry, the server is currently full!");
		}
		else if(message.getType() == CardGameMessage.QUIT) {
			table.printMsg(playerList.get(message.getPlayerID()).getName() + " left the game");
			playerList.get(message.getPlayerID()).setName("");
			CardGameMessage readyMessage = new CardGameMessage(CardGameMessage.READY, -1, null);
			sendMessage(readyMessage);
			started = false;
			table.repaint();
		}
		else if(message.getType() == CardGameMessage.READY) {
			table.printMsg(playerList.get(message.getPlayerID()).getName() + " is ready.");
		}
		else if(message.getType() == CardGameMessage.START) {
			BigTwoDeck deck = (BigTwoDeck) message.getData();
			start(deck);
			table.repaint();
		}
		else if(message.getType() == CardGameMessage.MOVE) {
			int[] cardIdx = (int[]) message.getData();
			checkMove(message.getPlayerID(), cardIdx);
		}
		else if(message.getType() == CardGameMessage.MSG) {
			String formattedChat = (String) message.getData();
			table.printChatMsg(formattedChat);
		}
	}
	/**
	 * A method for sending the specified message to the game server. 
	 * This method should be called whenever the client wants to communicate 
	 * with the game server or other clients.
	 */
	@Override
	public synchronized void sendMessage(GameMessage message) {
		// TODO Auto-generated method stub
		try {
			oos.writeObject(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * An inner class that implements the Runnable interface. 
	 * Upon receiving a message, the parseMessage() method from the NetworkGame 
	 * interface will be called to parse the messages accordingly.
	 * 
	 * @author Reno Andrianto
	 *
	 */
	class ServerHandler implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			CardGameMessage message;
			ObjectInputStream oistream;
			try {
				oistream = new ObjectInputStream(sock.getInputStream());
				while((message = (CardGameMessage) oistream.readObject()) != null) {
					parseMessage(message);
				}
			} catch(SocketException ex) {
				connected = false;
				started = false;
				table.printMsg("Your connection to the server is lost. Please click connect"
						+ " from the menu button.");
				table.disable();
				ex.printStackTrace();
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				connected = false;
				ex.printStackTrace();
			}
		}
	}
	/**
	 * A method for returning a valid hand from the specified list of cards of the player. 
	 * Returns null if no valid hand can be composed from the specified list of cards.
	 * 
	 * @param player a player in the card game
	 * @param cards list of cards that may form a Hand
	 * @return possible hand from the list of cards
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		Hand hand;
		if(cards.size() == 1) {
			hand = new Single(player, cards);
			if(hand.isValid()) {
				return hand;
			}
		}
		else if(cards.size() == 2) {
			hand = new Pair(player, cards);
			if(hand.isValid()) {
				return hand;
			}
		}
		else if(cards.size() == 3) {
			hand = new Triple(player, cards);
			if(hand.isValid()) {
				return hand;
			}
		}
		else if(cards.size() == 5) {
			hand = new StraightFlush(player, cards);
			if(hand.isValid()) {
				return hand;
			}
			hand = new Quad(player, cards);
			if(hand.isValid()) {
				return hand;
			}
			hand = new FullHouse(player, cards);
			if(hand.isValid()) {
				return hand;
			}
			hand = new Flush(player, cards);
			if(hand.isValid()) {
				return hand;
			}
			hand = new Straight(player, cards);
			if(hand.isValid()) {
				return hand;
			}
		}
		hand = null;
		return hand;
	}
	/**
	 * A method for creating an instance of BigTwoClient.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		BigTwoClient client = new BigTwoClient();
	}
}
