package simonSays;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

public class View extends JFrame {
	
	// Fields
	private Label red, blue, green, yellow;
	private Panel northpanel, southpanel;
	private JButton start;
	private Label roundLabel, pointsLabel, livesLabel;
	private TextField round, points, lives;
	Dialog dialog, scoreDialog, signUpDialog;
	String msg, msg2;
	public enum Status {
		CORRECT, WRONG
	}
	public final long NORMAL = 400L;
	public final long EASY = 400L;
	public final long HARD = 400L;
	
	private MenuBar menuBar;
	private Menu menu, selectDifficulty;
	private MenuItem newGame, scoreBoard, help, hard, normal, easy;
	private String playerName;
	private long time;
	private Label activity;
	
	private Controller c;
	private Model m;
	
	// Constructor
	public View () {
		/*by Peter Štibraný-------------------------------------------*/
        InputMap im = (InputMap) UIManager.getDefaults().get("Button.focusInputMap");
        Object pressedAction = im.get(KeyStroke.getKeyStroke("pressed SPACE"));
        Object releasedAction = im.get(KeyStroke.getKeyStroke("released SPACE"));
        im.put(KeyStroke.getKeyStroke("pressed ENTER"), pressedAction);
        im.put(KeyStroke.getKeyStroke("released ENTER"), releasedAction);
		/*----------------this allows ENTER to fire ActionEvents as well, 
		 * by default its only the SPACE bar.*/
		m = new Model();
		c = new Controller(m, this);
		addWindowListener(c);
		// Set default time
		time = 400L;
		// Indicator if user has to wait or be active
		activity = new Label("Wait");
		Panel p = new Panel();
		p.add(activity, BorderLayout.CENTER);
		add(p, BorderLayout.CENTER);
		// Create the menu
		menuBar = new MenuBar();
		menu = new Menu("Options");
		newGame = new MenuItem("New Game");
		scoreBoard = new MenuItem("Score Board");
		selectDifficulty = new Menu("Select Difficulty");
		help = new MenuItem("Help");
		newGame.addActionListener(c);
		scoreBoard.addActionListener(c);
		help.addActionListener(c);
		hard = new MenuItem("Hard");
		normal = new MenuItem("Normal");
		easy = new MenuItem("Easy");
		hard.addActionListener(c);
		normal.addActionListener(c);
		easy.addActionListener(c);
		selectDifficulty.add(hard);
		selectDifficulty.add(normal);
		selectDifficulty.add(easy);
		menu.add(newGame);
		menu.add(scoreBoard);
		menu.add(selectDifficulty);
		menu.add(help);
		menuBar.add(menu);
		setMenuBar(menuBar);
		// Create labels for the colors
		red = new Label();
		blue = new Label();
		green = new Label();
		yellow = new Label();
		red.setBackground(Color.red);
		blue.setBackground(Color.blue);
		green.setBackground(Color.green);
		yellow.setBackground(Color.yellow);
		red.setName("red");
		blue.setName("blue");
		green.setName("green");
		yellow.setName("yellow");
		red.addMouseListener(c);
		blue.addMouseListener(c);
		green.addMouseListener(c);
		yellow.addMouseListener(c);
		// Use setPreferredSize when the component has a LayoutManager
		// Use setSize when the Component doesn't use a Layout manager
		red.setPreferredSize(new Dimension(50,100));
		// Create panel as container
		northpanel = new Panel();
		northpanel.setSize(300,200);
		// Give the panel a gridlayout with two cols and 2 rows
		GridLayout myLayout = new GridLayout(2,2);
		northpanel.setLayout(myLayout);
		
		// Add color-labels to the container panel
		northpanel.add(red);
		northpanel.add(blue);
		northpanel.add(green);
		northpanel.add(yellow);
		
		// Create a start-button and add the controller as action listener and a container-panel
		southpanel = new Panel();
		southpanel.setLayout(new FlowLayout());
		start = new JButton("Start");
		start.addActionListener(c);
		southpanel.add(start);
		start.grabFocus();
		
		// Add labels for round, points, lives remaining
		roundLabel = new Label("Rounds: ");
		round = new TextField();
		pointsLabel = new Label("Points: ");
		points = new TextField();
		livesLabel = new Label("Lives: ");
		lives = new TextField();
		round.setEditable(false);
		points.setEditable(false);
		lives.setEditable(false);
		round.setText(m.getRound() + "");
		points.setText(m.getPoints() + "");
		lives.setText("3");
		southpanel.add(roundLabel);
		southpanel.add(round);
		southpanel.add(pointsLabel);
		southpanel.add(points);
		southpanel.add(livesLabel);
		southpanel.add(lives);
		
		// Add both containers to the frame
		add(northpanel, BorderLayout.NORTH);
		add(southpanel, BorderLayout.SOUTH);
		// Set size and visibility for main panel
		setSize(600,400);
		setLocationRelativeTo(null);
		setVisible(true);
		showHelpDialog();
		showSignUpDialog();
	}
	
	public void refresh() throws InterruptedException {
		// Every round refresh() displays the sequence to the user
		/* Disable the northpanel with the colors so the
		 * user can't just copy the sequence immediately but has to memorize
		 * */
		activity.setText("Wait");
		northpanel.setEnabled(false);
		ArrayList<Integer> os = m.getOutSequence();
		for(int i = 0; i < os.size(); i++) {
			int y = os.get(i);
			// For every n in the sequence determine which panel should be manipulated
			colorChange(y);
		}
		// Enable the northpanel once the sequence has been shown completely
		northpanel.setEnabled(true);
		activity.setText("Go!");

	}
	
	public void resetSpeed() {
		time = NORMAL;
	}
	
	public void speedUp() {
		if(time > 100L) {
			time -= 50L;
		}
	}
	
	public void setDifficulty(long time) {
		this.time = time;
	}
	
	public void updateScore() {
		round.setText(m.getRound() + "");
		points.setText(m.getPoints() + "");
		lives.setText(m.getCurrentPlayer().getLives()+"");
	}
	
	public void setMessage(Status val) {
		// This method sets the dialog message according to the outcome
		Status status = val;
		this.msg = status.name();
		if(val == Status.CORRECT) {
			msg2 = "Correct! You score a point. Continue to the next round.";
		} else {
			msg2 = "Wrong! Try again. Press start to see the sequence again.";
		}
	}
	
	public void showDialog(Status val) {
		// Correct and Wrong dialog
		setMessage(val);
		dialog = new Dialog(this, msg, true);
		Label message = new Label();
		message.setText(msg2);
		JButton okButton = new JButton("OK");
		Panel southpanel = new Panel();
		southpanel.setLayout(new FlowLayout());
		southpanel.add(okButton);
		okButton.addActionListener(c);
		dialog.add(message, BorderLayout.NORTH);
		dialog.add(southpanel);
		dialog.pack();
		okButton.grabFocus();
		dialog.addWindowListener(c);
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}
	
	public void showScoreDialog() {
		// Different colors for even and odd rows
		final Color EVEN_ROW_COLOR = new Color(240,240,255);
		final Color ODD_ROW_COLOR = new Color(255,255,240);
		boolean isEvenRow = true;
		// Dialog Container to display the scores
		scoreDialog = new Dialog(this, "SCORES", true);
		// Panel with GridLayout (rows, cols)
		GridLayout scoreLayout = new GridLayout(0,2);
		scoreLayout.setHgap(40);
		Panel scorePanel = new Panel(scoreLayout);
		// Iterate over all players that played so far
		Iterator<Player> iterator = m.getPlayerIterator();
		while(iterator.hasNext()) {
			Player p = iterator.next();
			Label nameLabel = new Label();
			Label scoreLabel = new Label();
			Color rowColor = isEvenRow? EVEN_ROW_COLOR : ODD_ROW_COLOR;
			nameLabel.setBackground(rowColor);
			scoreLabel.setBackground(rowColor);
			nameLabel.setText(p.getName());
			scoreLabel.setText(p.getHighScore()+"");
			scorePanel.add(nameLabel);
			scorePanel.add(scoreLabel);
			isEvenRow = !isEvenRow;
		}
		// Add a button to close the score Dialog
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				scoreDialog.dispose();
			}
		});
		Panel southPanel = new Panel(new FlowLayout());
		southPanel.add(closeButton);
		scoreDialog.add(scorePanel, BorderLayout.NORTH);
		scoreDialog.add(southPanel, BorderLayout.SOUTH);
		scoreDialog.setLocationRelativeTo(this);
		scoreDialog.setPreferredSize(new Dimension(200, 400));
		closeButton.grabFocus();
		scoreDialog.addWindowListener(c);
		scoreDialog.pack();
		scoreDialog.setVisible(true);
	}
	
	public void showSignUpDialog() {
		// Dialog prompts for username
		signUpDialog = new Dialog(this, "SIGN UP", true);
		Panel dataPanel = new Panel(new GridLayout(0,1));
		TextField nameField = new TextField("username", 10);
		Label warning = new Label("warning");
		warning.setText("");
		dataPanel.add(new Label("Player name: "));
		dataPanel.add(nameField);
		dataPanel.add(warning);

		signUpDialog.add(dataPanel, BorderLayout.NORTH);
		JButton confirmButton = new JButton("Confirm");
		
		// Make a lokal ActionListener, its used for button and Enter keypress
		class ConfirmListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (nameField.getText().length() < 10) {
					Player p = new Player(nameField.getText(), 0);
					m.addPlayer(p);				
					signUpDialog.dispose();
				} else {
					warning.setText("Name is too long");
				}
			}
		}
		ConfirmListener cl = new ConfirmListener();
		confirmButton.addActionListener(cl);
		nameField.addActionListener(cl);
	
		Panel buttonPanel = new Panel(new FlowLayout());
		buttonPanel.add(confirmButton);
		signUpDialog.add(buttonPanel, BorderLayout.SOUTH);
		signUpDialog.setPreferredSize(new Dimension(200,200));
		signUpDialog.pack();
		
		// So the button is clickable by keyboard as well grab focus (Only JButton not Button)
		confirmButton.grabFocus();
		signUpDialog.setLocationRelativeTo(this);
		signUpDialog.setVisible(true);
	}
	
	public void disposeCorrectDialog() {
		dialog.dispose();
	}

	public int showGameOverDialog() {
		int input = JOptionPane.showConfirmDialog(this, "Game Over. Continue?");
		return input;
	}
	
	public void showHelpDialog() {
		dialog = new Dialog(this, "HELP", true);
		Panel northpanel = new Panel(new FlowLayout());
		TextArea message = new TextArea();
		String helpText = 
				"This game is commonly known as Simon says.\r\n" 
				+ "To play it enter your username and click start.\r\n"
				+ "You will see a sequence that you have to repeat.\r\n"
				+ "If you repeat it correctly you score a point and proceed to the next round,\r\n"
				+ "if you fail you lose a life. \r\n"
				+ "Every round the sequence is +1 longer.\r\n"
				+ "If you want to see the sequence again just click start again.\r\n";
		message.setText(helpText);
		message.setEditable(false);
		northpanel.add(message);
		JButton okButton = new JButton("OK");
		Panel southpanel = new Panel();
		southpanel.setLayout(new FlowLayout());
		southpanel.add(okButton);
		okButton.addActionListener(c);
		this.getRootPane().setDefaultButton(okButton);
		dialog.addWindowListener(c);
		dialog.add(northpanel, BorderLayout.NORTH);
		dialog.add(southpanel, BorderLayout.SOUTH);
		dialog.pack();
		dialog.setLocationRelativeTo(this);
		// giving the JButton focus, this needs to be between pack but before setVisible
		// okButton.requestFocusInWindow();
		okButton.grabFocus();
		dialog.setVisible(true);
		//UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
	}
	
	public void colorChange(int y) {
		try {
			switch (y) {
			case 0:
				// change
				red.setBackground(new Color(255, 150, 150));
				Thread.sleep(time);
				// reset
				red.setBackground(Color.red);
				Thread.sleep(time);
				break;
			case 1:
				blue.setBackground(new Color(150, 150, 255));
				Thread.sleep(time);
				blue.setBackground(Color.blue);
				Thread.sleep(time);
				break;
			case 2:
				green.setBackground(new Color(150, 255, 150));
				Thread.sleep(time);
				green.setBackground(Color.green);
				Thread.sleep(time);
				break;
			case 3:
				yellow.setBackground(new Color(255, 250, 150));
				Thread.sleep(time);
				yellow.setBackground(Color.yellow);
				Thread.sleep(time);
				break;
			default:
				System.out.println();
			}
		} catch (InterruptedException ie) {ie.getMessage();}
	}

	
	
}
