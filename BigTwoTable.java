
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
/**
 * The BigTwoTable class implements the CardGameTable interface. It is used 
 * to build a GUI for the Big Two card game and handle all user actions.
 * 
 * @author Reno Andrianto
 *
 */
public class BigTwoTable implements CardGameTable{
	/**
	 * A constructor for creating a BigTwoTable.
	 * 
	 * @param game reference to a card game associated with this table.
	 */
	public BigTwoTable(CardGame game) {
		this.game = game;
		avatars = new Image[4];
		avatars[0] = new ImageIcon("avatars/yugi.png").getImage();
		avatars[1] = new ImageIcon("avatars/joey.png").getImage();
		avatars[2] = new ImageIcon("avatars/kaiba.png").getImage();
		avatars[3] = new ImageIcon("avatars/marik.png").getImage();
		emptyAvatar = new ImageIcon("avatars/empty.png").getImage();
		cardImages = new Image[4][13];
		for(int i = 0; i < 4; i ++) {
			for (int j = 0; j < 13; j++) {
				Image source = new ImageIcon("cards/" + i + "-" + j + ".png").getImage();
				Image resized = resizer(source, 80, 110);
				cardImages[i][j] = new ImageIcon(resized).getImage();
			}
		}
		Image cardBackSource = new ImageIcon("cards/yugioh.jpg").getImage();
		cardBackImage = new ImageIcon(resizer(cardBackSource, 80, 110)).getImage();
		//activePlayer = game.getCurrentIdx();
		
		//frame setting
		frame = new JFrame("Big Two");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(130, 100);
		frame.setSize(new Dimension(1600, 900));
		GridLayout layout = new GridLayout(1,2);
		frame.setLayout(layout);
		
		//msgArea setting
		msgArea = new JTextArea();
		msgArea.setLineWrap(true);
		msgArea.setEditable(false);
		msgArea.setFont(msgArea.getFont().deriveFont(20f));
		
		
		//chatMsgArea setting
		chatMsgArea = new JTextArea();
		chatMsgArea.setLineWrap(true);
		chatMsgArea.setEditable(false);
		chatMsgArea.setFont(msgArea.getFont().deriveFont(20f));
		
		//play and pass button setting
		playButton = new JButton("Play"); passButton = new JButton("Pass");
		playButton.addActionListener(new PlayButtonListener());
		passButton.addActionListener(new PassButtonListener());
		
		
	}
	//a card game associated with this table.
	private CardGame game;
	//a boolean array indicating which cards are being selected.
	private boolean[] selected;
	//an integer specifying the index of the active player.
	private int activePlayer;
	//he main window of the application.
	private JFrame frame;
	//a panel for showing the cards of each player and the cards played on the table.
	private JPanel bigTwoPanel;
	//a “Play” button for the active player to play the selected cards.
	private JButton playButton;
	//a “Pass” button for the active player to pass his/her turn to the next player.
	private JButton passButton;
	//a text area for showing the current game status as well as end of game messages.
	private JTextArea msgArea;
	//a panel for housing panels on the right pane of the frame
	private JPanel rightPanel;
	//a text area for showing the chat box of the game.
	private JTextArea chatMsgArea;
	//a text field for entering chat messages
	private JTextField chatInput;
	//a 2D array storing the images for the faces of the cards.
	private Image[][] cardImages;
	//an image for the backs of the cards.
	private Image cardBackImage;
	//array storing the images for the avatars.
	private Image[] avatars;
	//an image for empty player slot
	private Image emptyAvatar;
	
	/**
	 * A method for setting the index of the active player (i.e., the current player).
	 */
	@Override
	public void setActivePlayer(int activePlayer) {
		// TODO Auto-generated method stub
		if (activePlayer < 0 || activePlayer >= game.getPlayerList().size()) {
			this.activePlayer = -1;
		} else {
			this.activePlayer = activePlayer;
		}
	}
	/**
	 * A method for getting an array of indices of the cards selected.
	 */
	@Override
	public int[] getSelected() {
		// TODO Auto-generated method stub
		ArrayList<Integer> intSelected = new ArrayList<Integer>();
		for(int i = 0; i < selected.length; i++) {
			if(selected[i] == true) {
				intSelected.add(i);
			}
		}
		int size = intSelected.size();
		int[] intArraySelected = new int[size];
		for(int i = 0; i <size; i++) {
			intArraySelected[i] = intSelected.get(i); 
		}
		return intArraySelected;
	}
	/**
	 * A method for resetting the list of selected cards.
	 */
	@Override
	public void resetSelected() {
		// TODO Auto-generated method stub
		selected = new boolean[game.getPlayerList().get(activePlayer).getNumOfCards()];
	}
	/**
	 * A method for repainting the GUI.
	 */
	@Override
	public void repaint() {
		// TODO Auto-generated method stub
		
		frame.setContentPane(new JPanel(new GridLayout(1,2)));
		
		//menu bar setting
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Game");
//		JMenuItem quit, restart;
		JMenuItem quit, connect;
		quit = new JMenuItem("Quit");
		connect = new JMenuItem("Connect");
		connect.addActionListener(new ConnectMenuItemListener());
		quit.addActionListener(new QuitMenuItemListener());
		menu.add(connect); menu.add(quit); 
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
		
		
		//bigTwoPanel setting
		bigTwoPanel = new TableBackground("arena/arena.jpg");
		//bigTwoPanel.setBackground(new Color(165, 0, 0));
		bigTwoPanel.setLayout(new BorderLayout());
		JPanel bottom = new JPanel();
		bottom.setLayout(new GridLayout(1,2));
		bottom.add(playButton); bottom.add(passButton);
		if(game.getCurrentIdx() == activePlayer) {
			enable();
		}
		else {
			disable();
		}
		bigTwoPanel.add(bottom, BorderLayout.SOUTH);
		
		
		//players panel to house each player # panel
		JPanel playerPanel;
		playerPanel = new JPanel();
		playerPanel.setBackground(new Color(0, 0, 0, 0));
		playerPanel.setLayout(new GridLayout(5, 1));
		
		
		//game started or stopped/ended
		BigTwoClient client = (BigTwoClient) game;
		if(game.endOfGame() != true  && client.getStarted() == true) {
			resetSelected();
			if(game.getCurrentIdx() == activePlayer) {
				printMsg("Your turn:");
			}
			else {
				printMsg(game.getPlayerList().get(game.getCurrentIdx()).getName() + "'s turn:");
			}
		}
		else {
			resetSelected();
			disable();
		}
		
		//player 0 panel
		JPanel player0Panel; JPanel player1Panel; JPanel player2Panel; JPanel player3Panel;
		if(game.getPlayerList().get(0).getName() != null && game.getPlayerList().get(0).getName() != "") {
			player0Panel = new BigTwoPanel(avatars[0]);
		}
		else {
			player0Panel = new BigTwoPanel(emptyAvatar);
		}
		player0Panel.setBackground(new Color(213, 134, 145, 50));
		player0Panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		player0Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel player0Label = new JLabel();
		if(game.getPlayerList().get(0).getName() != null) {
			if(client.getPlayerID() == 0) {
				player0Label.setText("You");
			}
			else {
				player0Label.setText(game.getPlayerList().get(0).getName());
			}
		}
		player0Label.setForeground(Color.WHITE);
		player0Panel.add(player0Label);
		JLayeredPane layeredPane0 = new JLayeredPane();
		layeredPane0.setPreferredSize(new Dimension(1000, 1000));
		printerCardPanel(0, layeredPane0, player0Panel);
		
		
		//player 1 panel
		if(game.getPlayerList().get(1).getName() != null && game.getPlayerList().get(1).getName() != "") {
			player1Panel = new BigTwoPanel(avatars[1]);
		}
		else {
			player1Panel = new BigTwoPanel(emptyAvatar);
		}
		player1Panel.setBackground(new Color(0, 134, 145, 50));
		player1Panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		player1Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel player1Label = new JLabel();
		if(game.getPlayerList().get(1).getName() != null) {
			if(client.getPlayerID() == 1) {
				player1Label.setText("You");
			}
			else {
				player1Label.setText(game.getPlayerList().get(1).getName());
			}
		}
		player1Label.setForeground(Color.WHITE);
		player1Panel.add(player1Label);
		JLayeredPane layeredPane1 = new JLayeredPane();
		layeredPane1.setPreferredSize(new Dimension(1000, 1000));
		printerCardPanel(1, layeredPane1, player1Panel);
		
		
		//player 2 panel
		if(game.getPlayerList().get(2).getName() != null && game.getPlayerList().get(2).getName() != "") {
			player2Panel = new BigTwoPanel(avatars[2]);
		}
		else {
			player2Panel = new BigTwoPanel(emptyAvatar);
		}
		player2Panel.setBackground(new Color(213, 0, 145, 50));
		player2Panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		player2Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel player2Label = new JLabel();
		if(game.getPlayerList().get(2).getName() != null) {
			if(client.getPlayerID() == 2) {
				player2Label.setText("You");
			}
			else {
				player2Label.setText(game.getPlayerList().get(2).getName());
			}
		}
		player2Label.setForeground(Color.WHITE);
		player2Panel.add(player2Label);
		JLayeredPane layeredPane2 = new JLayeredPane();
		layeredPane2.setPreferredSize(new Dimension(1000, 1000));
		printerCardPanel(2, layeredPane2, player2Panel);
		
		
		//player 3 panel
		if(game.getPlayerList().get(3).getName() != null && game.getPlayerList().get(3).getName() != "") {
			player3Panel = new BigTwoPanel(avatars[3]);
		}
		else {
			player3Panel = new BigTwoPanel(emptyAvatar);
		}
		player3Panel.setBackground(new Color(213, 134, 0, 50));
		player3Panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		player3Panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel player3Label = new JLabel();
		if(game.getPlayerList().get(3).getName() != null) {
//			BigTwoClient client = (BigTwoClient) game;
			if(client.getPlayerID() == 3) {
				player3Label.setText("You");
			}
			else {
				player3Label.setText(game.getPlayerList().get(3).getName());
			}
		}
		player3Label.setForeground(Color.WHITE);
		player3Panel.add(player3Label);
		JLayeredPane layeredPane3 = new JLayeredPane();
		layeredPane3.setPreferredSize(new Dimension(1000, 1000));
		printerCardPanel(3, layeredPane3, player3Panel);
		
		
		//table panel
		JPanel table = new JPanel();
		table.setBackground(new Color(213, 134, 0, 0));
		table.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		table.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel tableLabel = new JLabel();
		tableLabel.setForeground(Color.WHITE);
		table.add(tableLabel);
		JLayeredPane layeredPaneTable = new JLayeredPane();
		Hand lastHandOnTable;
		if(game.getHandsOnTable().isEmpty()) {
			lastHandOnTable = null;
			tableLabel.setText("Empty");
		}
		else {
			lastHandOnTable = game.getHandsOnTable().get(game.getHandsOnTable().size() - 1);
			tableLabel.setText("Played by " + lastHandOnTable.getPlayer().getName());
		}
		layeredPaneTable.setPreferredSize(new Dimension(1000, 1000));
		if(lastHandOnTable != null) {
			for(int i = 0; i < lastHandOnTable.size(); i ++) {
				CardPanel cardPanel;
				Card card = lastHandOnTable.getCard(i);
				int x = 20 + 27 * i;
				int y = 5;
				cardPanel = new CardPanel(cardImages[card.getSuit()][card.getRank()], x, y, activePlayer, card, i);	
				cardPanel.setBounds(x, y, 80, 110);
				layeredPaneTable.add(cardPanel, 0);
			}
			table.add(layeredPaneTable);
		}
		
		playerPanel.add(player0Panel);
		playerPanel.add(player1Panel);
		playerPanel.add(player2Panel);
		playerPanel.add(player3Panel);
		playerPanel.add(table);
		
		bigTwoPanel.add(playerPanel);
		
		frame.add(bigTwoPanel);
		JScrollPane msgScrollPane1 = new JScrollPane(msgArea);
		JScrollPane chatScrollPane2 = new JScrollPane(chatMsgArea);
		
		//right pane setting
		chatInput = new JTextField(60);
		chatInput.addActionListener(new ChatInputListener());
		JPanel otherPanel = new JPanel();
		JPanel chatInputPanel = new JPanel();
		chatInputPanel.setLayout(new FlowLayout());
		chatInputPanel.add(new JLabel("Message: "));
		chatInputPanel.add(chatInput);
		//chatInputPanel.add(chatInput);
		//otherPanel.add(chatInputPanel, BorderLayout.SOUTH);
		rightPanel = new JPanel();
		rightPanel.setLayout(new GridLayout(2,1));
		rightPanel.add(msgScrollPane1);
		rightPanel.add(chatScrollPane2);
		otherPanel.setLayout(new BorderLayout());
		otherPanel.add(rightPanel);
		otherPanel.add(chatInputPanel, BorderLayout.SOUTH);
		frame.add(otherPanel);
		msgArea.setCaretPosition(msgArea.getDocument().getLength());
		chatMsgArea.setCaretPosition(chatMsgArea.getDocument().getLength());

		frame.setVisible(true);
	}
	/**
	 * A method for printing the specified string to the message area of the GUI.
	 */
	@Override
	public void printMsg(String msg) {
		// TODO Auto-generated method stub
		msgArea.append(msg + "\n");
	}
	/**
	 * A method for clearing the message area of the GUI.
	 */
	@Override
	public void clearMsgArea() {
		// TODO Auto-generated method stub
		msgArea.setText(null);
	}
	public void printChatMsg(String msg) {
		chatMsgArea.append(msg + "\n");
	}
	public void clearChatMsgArea() {
		// TODO Auto-generated method stub
		chatMsgArea.setText(null);
	}
	/**
	 * A method for resetting the GUI. It (i) resets the list of selected cards 
	 * using resetSelected() method from the CardGameTable interface; 
	 * (ii) clears the message area using the clearMsgArea() method from the 
	 * CardGameTable interface; and (iii) enables user interactions using the 
	 * enable() method from the CardGameTable interface.
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		resetSelected();
		clearMsgArea();
		enable();
	}
	/**
	 * A method for enabling user interactions with the GUI. It enables the 
	 * “Play” button and “Pass” button (i.e., making them clickable).
	 */
	@Override
	public void enable() {
		// TODO Auto-generated method stub
		playButton.setEnabled(true);
		passButton.setEnabled(true);
	}
	/**
	 * A method for disabling user interactions with the GUI. It disables 
	 * the “Play” button and “Pass” button (i.e., making them not clickable).
	 */
	@Override
	public void disable() {
		// TODO Auto-generated method stub
		passButton.setEnabled(false);
		playButton.setEnabled(false);
		
	}
	//a method to resize an image to a specific dimension
	private Image resizer(Image source, int width, int height) {
		source = new ImageIcon(source).getImage();
		Image resized = source.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return resized;
	}
	//a method to print the cards of each player on each player panel
	private void printerCardPanel(int playerIdx, JLayeredPane layeredPane, JPanel eachPlayerPanel) {
		int numOfCardsInHand = game.getPlayerList().get(playerIdx).getNumOfCards();
		for(int i = 0; i < numOfCardsInHand; i ++) {
			CardPanel button;
			Card card = game.getPlayerList().get(playerIdx).getCardsInHand().getCard(i);
			int x = 170 + 27 * i;
			int y = 20;
			if(playerIdx == activePlayer && ((BigTwoClient) game).getStarted() == true) { //opens up the cards of active player and makes them clickable
				button = new CardPanel(cardImages[card.getSuit()][card.getRank()], x, y, playerIdx, card, i);
				if(playerIdx == game.getCurrentIdx()) {
					button.addMouseListener(button);
				}
			}
			else {
				if(game.endOfGame() == true) { //opens up all cards when the game ends
					button = new CardPanel(cardImages[card.getSuit()][card.getRank()], x, y, playerIdx, card, i);
				}
				else { //close all cards for all non active players
					button = new CardPanel(cardBackImage, x, y, playerIdx, card, i);
				}
			}
			button.setBounds(x, y, 80, 110);
			layeredPane.add(button, 0);
		}
		eachPlayerPanel.add(layeredPane);
	}
	/**
	 * An inner class that extends JPanel and implements MouseListener to design a card that will be 
	 * printed on the GUI. It contains the essential information about the card such as the index of 
	 * the player that holds the card, image of the card, coordinate, index of the card in the 
	 * player's hand, and the Card object itself.
	 * 
	 * @author Reno Andrianto
	 *
	 */
	class CardPanel extends JPanel implements MouseListener{
		boolean raised = false;
		private Image image;
		private int x;
		private int y;
		private int playerIdx;
		private Card card;
		private int index;
		public CardPanel(Image image, int x, int y, int playerIdx, Card card, int index) {
			this.card = card;
			this.index = index;
			this.playerIdx = playerIdx;
			this.x = x;
			this.y = y;
			this.image = image;
			Dimension size = new Dimension(80, 110);
			setPreferredSize(size);
			setMinimumSize(size);
	        setMaximumSize(size);
	        setSize(size);
		}
		public void paintComponent(Graphics g) {
	        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	        //frame.repaint();
	    }
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
			if(!raised) {
				setBounds(x, 0, 80, 110);
				selected[index] = true;
				raised = true;
			}
			else {
				setBounds(x, 20, 80, 110);
				selected[index] = false;
				raised = false;
			}
//			debug
//			for(int i = 0; i < selected.length; i ++) {
//				System.out.println(game.getPlayerList().get(playerIdx).getCardsInHand().getCard(i).toString() + selected[i]);
//			}
//			System.out.println();
			frame.repaint();
		}
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	/**
	 * An inner class that extends the JPanel class and implements the MouseListener 
	 * interface. It houses the components of each player panel. Overrides the paintComponent() 
	 * method inherited from the JPanel class to draw the card game table for each player. 
	 * Implements the mouseClicked() method from the MouseListener interface to handle mouse 
	 * click events.
	 * 
	 * @author Reno Andrianto
	 *
	 */
	class BigTwoPanel extends JPanel implements MouseListener {
		Image avatar;
		BigTwoPanel(Image avatar) {
			Image resized = resizer(avatar, 150, 150);
			this.avatar = new ImageIcon(resized).getImage();
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		public void paintComponent(Graphics g) {
			g.drawImage(avatar, 5, 20, avatar.getWidth(this), avatar.getHeight(this), this);

		}
	}
	/**
	 * An inner class that extends the JPanel class. Overrides the paintComponent() 
	 * method inherited from the JPanel class to draw the card game table background. 
	 * 
	 * @author Reno Andrianto
	 *
	 */
	class TableBackground extends JPanel{
		private Image image;

	    public TableBackground(String image) {
	        this(new ImageIcon(image).getImage());
	    }

	    public TableBackground(Image image) {
	        this.image = image;
	        Dimension size = new Dimension(image.getWidth(null), image.getHeight(null));
	        setPreferredSize(size);

	        setMinimumSize(size);
	        setMaximumSize(size);

	        setSize(size);
	        setLayout(null);
	    }
	    
	    public void paintComponent(Graphics g) {
	        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	    }
	}
	
	/**
	 * An inner class that implements the ActionListener interface. Implements the actionPerformed() 
	 * method from the ActionListener interface to handle button-click events for the “Play” button. 
	 * When the “Play” button is clicked, it will call the makeMove() method of your CardGame 
	 * object to make a move.
	 * 
	 * @author Reno Andrianto
	 *
	 */
	class PlayButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			game.makeMove(activePlayer, getSelected());
		}
	}
	/**
	 * An inner class that implements the ActionListener interface. Implements the actionPerformed() 
	 * method from the ActionListener interface to handle button-click events for the “Pass” button. 
	 * When the “Pass” button is clicked, it will call the makeMove() method of your CardGame 
	 * object to make a move.
	 * 
	 * @author Reno Andrianto
	 *
	 */
	class PassButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			game.makeMove(activePlayer, null);
		}
	}
	/**
	 * An inner class that implements the ActionListener interface. Implements the actionPerformed() 
	 * method from the ActionListener interface to handle menu-item-click events for the “Restart” 
	 * menu item. When the “Restart” menu item is selected, it will (i) create a new BigTwoDeck 
	 * object and call its shuffle() method; and (ii) call the start() method of your CardGame object 
	 * with the BigTwoDeck object as an argument.
	 * 
	 * @author Reno Andrianto
	 *
	 */
	class RestartMenuItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			BigTwoDeck newDeck = new BigTwoDeck();
			newDeck.initialize();
			newDeck.shuffle();
			game.start(newDeck);
			reset();
			repaint();
		}
		
	}
	/**
	 * An inner class that implements the ActionListener interface. Implements the actionPerformed() 
	 * method from the ActionListener interface to handle menu-item-click events for the “Connect” 
	 * menu item. When the “Connect” menu item is selected, it will call makeConnection() method from 
	 * the client if the client is not currently connected to the server.
	 * 
	 * @author Reno Andrianto
	 *
	 */
	class ConnectMenuItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			BigTwoClient client = (BigTwoClient) game;
			if(client.getConnected() == true) {
				printMsg("You are already connected to the server!");
				frame.repaint();
			}
			else {
				client.makeConnection();
			}	
		}
	}
	/**
	 * An inner class that implements the ActionListener interface. Implements the actionPerformed() 
	 * method from the ActionListener interface to handle the game chat text input field to send out messages. 
	 * It will send out the typed String on the text input field by calling sendMessage() method from the client. 
	 * 
	 * @author Reno Andrianto
	 *
	 */
	class ChatInputListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			BigTwoClient client = (BigTwoClient) game;
			CardGameMessage message = new CardGameMessage(CardGameMessage.MSG, -1, chatInput.getText());
			chatInput.setText("");
			chatInput.requestFocus();
			client.sendMessage(message);
		}
	}
	/**
	 * An inner class that implements the ActionListener interface. Implements the actionPerformed() 
	 * method from the ActionListener interface to handle menu-item-click events for the “Quit” menu item. 
	 * When the “Quit” menu item is selected, it will terminate the application.
	 * 
	 * @author Reno Andrianto
	 *
	 */
	class QuitMenuItemListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			System.exit(0);
		}
		
	}
}
