package simonSays;

import java.util.ArrayList;
import java.util.Iterator;

public class Model {
	private ArrayList<Integer> outSequence, inSequence;
	private ArrayList<Player> players;
	private int size, round, points;
	
	public Model() {
		outSequence = new ArrayList<Integer>();
		inSequence = new ArrayList<Integer>();
		players = new ArrayList<Player>();
		size = 4;
		round = 0;
		points = 0;
		fillOutSequence();
	}
	
	public void addPlayer(Player p) {
		if (players.size() > 10) {
			players.remove(0);
		} else {
			players.add(p);
		}
		
	}
	
	public Player getCurrentPlayer() {
		Player p = (Player)players.get(players.size()-1);
		return p;
	}
	
	public ArrayList<Player> getAllPlayers() {
		return players;
	}
	
	
	public void updatePlayer() {
		getCurrentPlayer().setHighScore(points);
	}
	
	public Iterator<Player> getPlayerIterator() {
		Iterator<Player> iterator = players.iterator();
		return iterator;
	}
	
	public void fillOutSequence() {
		// first erase outSequence so a new one can be recorded
		outSequence.clear();
		for(int i = 0; i < size; i++) {
			outSequence.add((int)(Math.random()*4));
		}
		// Increase the size with every round
		size++;
		round++;
	}
	
	public int getRound() {
		return round;
	}
	
	public int getPoints() {
		return points;
	}
	
	public void increasePoints () {
		points++;
	}
	
	public void printOutSequence() {
		for(int i = 0; i < outSequence.size(); i++) {
			System.out.print(outSequence.get(i));
		} 
		System.out.println();
	}
	
	public ArrayList<Integer> getOutSequence(){
		return outSequence;
	}
	
	public int getOutSize() {
		return outSequence.size();
	}
	
	public int getInSize() {
		return inSequence.size();
	}
	
	public void addToInSequence(int i) {
		inSequence.add(i);
	}
	
	
	public boolean compare() {
		if(inSequence.equals(outSequence)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void resetInSequence() {
		inSequence.clear();
	}
	
	public void resetGame() {
		resetInSequence();
		outSequence.clear();
		size = 4;
		points = 0;
		round = 0;
		fillOutSequence();
	}
}
