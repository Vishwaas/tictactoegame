package com.game.tictactoe;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TicTacToeGame implements Game {
	private Scanner inputReader;
	private static final TicTacToeGame ticTacToeGameInstance = null;
	Player playerX;
	Player playerO;
	Player currentPlayer;
	String[][] ticTacToeBoard = new String[3][3]; 
	
	public static TicTacToeGame getInstance() {
		return ticTacToeGameInstance == null ? new TicTacToeGame() : ticTacToeGameInstance;
	}
	
	private TicTacToeGame() {
		inputReader = new Scanner(new InputStreamReader(System.in));
	}

	@Override
	public boolean start() {
		playerX = new Player("X", promptPlayer("Name of player 1:"));
		playerO = new Player("O", promptPlayer("Name of player 2:"));
		currentPlayer = playerX;
		
		System.out.println("Enter \"exit\" at any stage to quit the game. "
				+ "\n Enter the values as hyphenated row and column numbers. Eg: 1st box is 1-1");
		process();
		
		return false;
	}

	@Override
	public boolean stop() {
		
		playerX = null;
		playerO = null;
		ticTacToeBoard= null;
		
		System.exit(0);
		return false;
	}

	@Override
	public void process() {
		
		while(true) {
			displayBoard();
			String playerInput = promptPlayer(currentPlayer.getName() + ":");
			if (playerInput.equalsIgnoreCase("exit")) {
				System.out.println("Exiting game. Have a nice day and hope to see you soon.");
				break;
			}
			if(!validateInput(playerInput)) {
				System.out.println("Invalid input kindly enter again");
				continue;
			}
			
			String[] boardPositions = playerInput.split("-");
			int inputRow = Integer.valueOf(boardPositions[0]) - 1;
			int inputCol = Integer.valueOf(boardPositions[1]) - 1;
					
			ticTacToeBoard[inputRow][inputCol] = currentPlayer.getId();
			Player winner = determineWinner(inputRow, inputCol);
			
			if (winner != null) {
				displayBoard();
				System.out.println("\n" + winner.getName() + " is the WINNER!!!! Congrats");
				break;
			}
			
			updateCurrentPlayer();
		}
		stop();
	}

	@Override
	public String notifyStatus() {
		
		return null;
	}
	
	private String promptPlayer(String question) {
		System.out.print(question);
		return inputReader.nextLine();
	}
	
	private void updateCurrentPlayer() {
		currentPlayer = currentPlayer == playerX ? playerO : playerX;
	}
	
	private boolean validateInput(String input) {
		try {
			String[] inputParts = input.split("-"); 
			if (inputParts.length != 2) {
				throw new Exception("Input should be of the format RowNumber-ColNumber");
			}
			if (!inputParts[0].matches("\\d") || !inputParts[1].matches("\\d") ) {
				throw new Exception("Row and Col numbers have to be numberic");
			}
			int rowNumber = Integer.valueOf(inputParts[0]);
			int colNumber = Integer.valueOf(inputParts[1]);
			
			if (!((rowNumber > 0 && rowNumber <= 3) && (colNumber > 0 && colNumber <= 3))) {
				throw new Exception("Row and Col numbers exceed index");
			}
			
			if (ticTacToeBoard[rowNumber - 1][colNumber - 1] != null) {
				throw new Exception("Row and Col already filled");
			}
			return true;
		} catch(Exception e) {
			System.out.println("Invalid input. " + e.getMessage());
			return false;
		}
	}
	
	private Player determineWinner(int currentRow, int currentCol) {
		Player winner = null;
		winner = isRowComplete(currentRow);
		if (winner != null) {
			return winner;
		}
		
		winner = isColComplete(currentCol);
		if (winner != null) {
			return winner;
		}
		
		if (currentRow == currentCol) {
			winner = isLeftDiagonalComplete(currentRow, currentCol);
			if (winner != null) {
				return winner;
			}
		}
		
		if (currentCol == 2 - currentRow) {
			winner = isRightDiagonalComplete(currentRow, currentCol);
			if (winner != null) {
				return winner;
			}
		}
		return null;
	}
	
	Player isRowComplete(int row) {
		Player winner = null;
		
		Map<String, Integer> playerEntryCount = new HashMap<String, Integer>();
		for(String playerId: ticTacToeBoard[row]) {
			if (playerId == null) {
				return null;
			}
			updateBoardEntryCount(playerEntryCount, playerId);
			
		}
		winner = checkPlayerEntryForWinner(playerEntryCount);
		return winner;
	}
	
	Player isColComplete(int col) {
		Player winner = null;
		
		Map<String, Integer> playerEntryCount = new HashMap<String, Integer>();
		for(int i = 0; i < 3; i ++) {
			String playerId = ticTacToeBoard[i][col];
			if (playerId == null) {
				return null;
			}
			updateBoardEntryCount(playerEntryCount, playerId);
		}
		winner = checkPlayerEntryForWinner(playerEntryCount);
		return winner;
	}
	
	Player isLeftDiagonalComplete(int row, int col) {
		Player winner = null;
		
		Map<String, Integer> playerEntryCount = new HashMap<String, Integer>();
		for(int i = 0; i < 3; i ++) {
			String playerId = ticTacToeBoard[i][i];
			if (playerId == null) {
				return null;
			}
			updateBoardEntryCount(playerEntryCount, playerId);
		}
		winner = checkPlayerEntryForWinner(playerEntryCount);
		return winner;
	}
	
	Player isRightDiagonalComplete(int row, int col) {
		Player winner = null;
		
		Map<String, Integer> playerEntryCount = new HashMap<String, Integer>();
		for(int i = 0; i < 3; i ++) {
			String playerId = ticTacToeBoard[i][2 - i];
			if (playerId == null) {
				return null;
			}
			updateBoardEntryCount(playerEntryCount, playerId);
			
		}
		winner = checkPlayerEntryForWinner(playerEntryCount);
		return winner;
	}
	
	private void updateBoardEntryCount(Map<String, Integer> match, String playerId) {
		Integer count = match.get(playerId);
		if(count == null) {
			count = new Integer(1);
		} else {
			count++;
		}
		match.put(playerId, count);
	}
	
	private Player checkPlayerEntryForWinner(Map<String, Integer> playerEntryCount) {
		Player winner = null;
		Integer playerXCount = playerEntryCount.get(playerX.getId()); 
		if (playerXCount != null && playerXCount == 3) {
			winner = playerX;
		}
		Integer playerOCount = playerEntryCount.get(playerO.getId()); 
		if (playerOCount != null && playerOCount == 3) {
			winner = playerO;
		}
		return winner;
	}
	
	private void displayBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				String value = ticTacToeBoard[i][j];
				System.out.print((value == null ? " " : value) + "|");
			}
			System.out.println("\n -----\n");
		}
	}
}
