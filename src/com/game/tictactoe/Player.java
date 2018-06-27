package com.game.tictactoe;

import java.util.Arrays;

public class Player {
	private String id;
	private String name;
	private int[] rowCount = new int[3];
	private int[] colCount = new int[3];
	private int[] diagonalCount = new int[2];
	
	public Player(String id, String name) {
		Arrays.fill(rowCount, 0);
		Arrays.fill(colCount, 0);
		Arrays.fill(rowCount, 0);
		
		this.id = id;
		this.name = name;
	}
	
	String getId() {
		return id;
	}
	
	String getName() {
		return name;
	}
	
	public void incrementRowCount(int index) {
		rowCount[index]++;
	}
	
	public void incrementColCount(int index) {
		colCount[index]++;
	}
	
	public void incrementDiagonalCount(int index) {
		diagonalCount[index]++;
	}
}
