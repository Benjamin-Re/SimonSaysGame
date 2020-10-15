package simonSays;

import java.awt.Dialog;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import simonSays.View.Status;

public class Controller implements WindowListener, ActionListener, MouseListener{
	
	private Model m;
	private View v;
	private String filePath = System.getProperty("user.home")+"\\Simon";
	private File scoresD;
	private File scoresF;
	
	public Controller(Model m, View v) {
		this.m = m;
		this.v = v;
		// Make folder file
		File scoresD = new File(filePath);
		scoresD.mkdir();
		// Make txt file within
		File scoresF = new File(filePath+"\\s.txt");
		if(scoresF.exists()) {
			m.getAllPlayers().addAll(load());
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// Action Listener for Dialog Button and Menus
		if (arg0.getActionCommand().equals("OK")) {
			v.disposeCorrectDialog();
		} else if (arg0.getActionCommand().equals("New Game")) {
			m.updatePlayer();
			m.resetGame();
			v.resetSpeed();
			v.updateScore();
			v.showSignUpDialog();
		} else if (arg0.getActionCommand().equals("Start")){
			try {
				v.updateScore();
				// Reset the inSequence so user doesn't accidentally have slots filled already
				m.resetInSequence(); 
				v.refresh();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else if (arg0.getActionCommand().equals("Score Board")) {
			v.showScoreDialog();
		} else if (arg0.getActionCommand().equals("Help")) {
			v.showHelpDialog();
		} else if (arg0.getActionCommand().equals("Hard")) {
			v.setDifficulty(v.HARD);
		} else if (arg0.getActionCommand().equals("Normal")) {
			v.setDifficulty(v.NORMAL);
		} else if (arg0.getActionCommand().equals("Easy")) {
			v.setDifficulty(v.EASY);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// if the inSequence is still smaller than the outSequence accept input
		if(m.getInSize() < m.getOutSize()) {
			Label l = (Label)arg0.getSource();
			switch(l.getName()) {
			case "red":
				m.addToInSequence(0);
				v.colorChange(0);
				break;
			case "blue":
				m.addToInSequence(1);
				v.colorChange(1);
				break;
			case "green":
				m.addToInSequence(2);
				v.colorChange(2);
				break;
			case "yellow":
				m.addToInSequence(3);
				v.colorChange(3);
				break;
			default:
				System.out.println("Click on a color");
			}
		// Check if inSequence is full and check for equality
		} if (m.getInSize() == m.getOutSize()) {
			if (m.compare()) {
				v.showDialog(Status.CORRECT);
				m.increasePoints();
				v.updateScore();
				// Increase speed each round
				v.speedUp();
				// Start next round
				m.fillOutSequence();
			} else {
				if(m.getCurrentPlayer().getLives() == 0) {
					int input = v.showGameOverDialog();
					if(input == 0) {
						// save player highscore before new game
						m.updatePlayer();
						// start new game
						m.resetGame();
						v.showSignUpDialog();
					} else {
						// save player highscore before exit
						m.updatePlayer();
						v.updateScore();
						System.exit(0);
					}
					return;
				}
				m.getCurrentPlayer().decreaseLives();
				v.showDialog(Status.WRONG);
			}
			m.resetInSequence();
		} 
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// Distinguish which window's X was pressed
		if(arg0.getSource() instanceof Dialog) {
			Dialog d = (Dialog) arg0.getSource();
			d.dispose();
		} else {
				File scoresD = new File(filePath);
				scoresD.mkdir();
				// Make txt file within
				File scoresF = new File(filePath+"\\s.txt");
				if (scoresF.exists()) {
					ArrayList<Player> oldPlayers = load();
					m.getAllPlayers().addAll(oldPlayers);
					write();
				} else {
					write();
				}
			System.exit(0);
		}
	}
	
	public void write() {
		// Write to txt file
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(filePath+"\\s.txt");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(m.getAllPlayers());
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<Player> load () {
		ObjectInputStream ois;
		try {
			FileInputStream fis = new FileInputStream(filePath+"\\s.txt");
			ois = new ObjectInputStream(fis);
			return (ArrayList<Player>)ois.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// in case loading didn't work
		return null;

		
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
