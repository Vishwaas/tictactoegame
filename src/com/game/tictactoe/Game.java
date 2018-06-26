package com.game.tictactoe;

public interface Game {
	boolean start();
	
	boolean stop();
	
	void process();
	
	String notifyStatus();
}
