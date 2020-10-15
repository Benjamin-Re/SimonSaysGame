package simonSays;

import java.io.Serializable;

public class Player implements Serializable{
	private String name;
	private int highScore, lives;
	
	public Player (String name, int highScore) {
		this.name = name;
		this.highScore = highScore;
		lives = 3;
	}
	
	public String getName() {
		return name;
	}
	
	public void decreaseLives() {
		lives--;
	}
	
	public int getLives() {
		return lives;
	}
	
	public void setHighScore(int points) {
		this.highScore = points;
	}
	
	public int getHighScore() {
		return highScore;
	}
}
