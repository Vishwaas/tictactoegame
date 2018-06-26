package com.game.tictactoe;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TicTacToeGame implements Game {
	private Scanner scanner;
	private static final TicTacToeGame ticTacToeGameInstance = null;
	Player player1;
	Player player2;
	Player currentPlayer;
	String[][] ticTacToeBoard = new String[3][3]; 
	
	public static TicTacToeGame getInstance() {
		return ticTacToeGameInstance == null ? new TicTacToeGame() : ticTacToeGameInstance;
	}
	
	private TicTacToeGame() {
		scanner = new Scanner(new InputStreamReader(System.in));
	}

	@Override
	public boolean start() {
		player1 = new Player("X", askAndFetch("Name of player 1:"));
		player2 = new Player("O", askAndFetch("Name of player 2:"));
		currentPlayer = player1;
		System.out.println("Enter \"exit\" at any stage to quit the game. "
				+ "\n Enter the values as hyphenated row and column numbers. Eg: 1st box is 1-1");
		process();
		
		return false;
	}

	@Override
	public boolean stop() {
		
		player1 = null;
		player2 = null;
		ticTacToeBoard= null;
		
		System.exit(0);
		return false;
	}

	@Override
	public void process() {
		
		while(true) {
			displayBoard();
			String playerInput = askAndFetch(currentPlayer.getName() + ":");
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
		// TODO Auto-generated method stub
		return null;
	}
	
	private String askAndFetch(String question) {
		System.out.print(question);
		return scanner.nextLine();
	}
	
	private void updateCurrentPlayer() {
		currentPlayer = currentPlayer == player1 ? player2 : player1;
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
			System.out.println("Invalid input" + e.getMessage());
			return false;
		}
	}
	
	private Player determineWinner(int currentRow, int currentCol) {
		Player winner = null;
		winner = isRowMatching(currentRow);
		if (winner != null) {
			return winner;
		}
		
		winner = isColMatching(currentCol);
		if (winner != null) {
			return winner;
		}
		
		if (currentRow == currentCol) {
			winner = isLeftDiagonalMatching(currentRow, currentCol);
			if (winner != null) {
				return winner;
			}
		}
		
		if (currentCol == 2 - currentRow) {
			winner = isRightDiagonalMatching(currentRow, currentCol);
			if (winner != null) {
				return winner;
			}
		}
		return null;
	}
	
	Player isRowMatching(int row) {
		Player winner = null;
		
		Map<String, Integer> match = new HashMap<String, Integer>();
		for(String playerId: ticTacToeBoard[row]) {
			if (playerId == null) {
				return null;
			}
			updateBoardEntryCount(match, playerId);
			
		}
		winner = checkMatchForWinner(match);
		return winner;
	}
	
	Player isColMatching(int col) {
		Player winner = null;
		
		Map<String, Integer> match = new HashMap<String, Integer>();
		for(int i = 0; i < 3; i ++) {
			String playerId = ticTacToeBoard[i][col];
			if (playerId == null) {
				return null;
			}
			updateBoardEntryCount(match, playerId);
		}
		winner = checkMatchForWinner(match);
		return winner;
	}
	
	Player isLeftDiagonalMatching(int row, int col) {
		Player winner = null;
		
		Map<String, Integer> match = new HashMap<String, Integer>();
		for(int i = 0; i < 3; i ++) {
			String playerId = ticTacToeBoard[i][i];
			if (playerId == null) {
				return null;
			}
			updateBoardEntryCount(match, playerId);
		}
		winner = checkMatchForWinner(match);
		return winner;
	}
	
	Player isRightDiagonalMatching(int row, int col) {
		Player winner = null;
		
		Map<String, Integer> match = new HashMap<String, Integer>();
		for(int i = 0; i < 3; i ++) {
			String playerId = ticTacToeBoard[i][2 - i];
			if (playerId == null) {
				return null;
			}
			updateBoardEntryCount(match, playerId);
			
		}
		winner = checkMatchForWinner(match);
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
	
	private Player checkMatchForWinner(Map<String, Integer> match) {
		Player winner = null;
		Integer player1Count = match.get(player1.getId()); 
		if (player1Count != null && player1Count == 3) {
			winner = player1;
		}
		Integer player2Count = match.get(player2.getId()); 
		if (player2Count != null && player2Count == 3) {
			winner = player2;
		}
		return winner;
	}
	
	private void displayBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				String value = ticTacToeBoard[i][j];
				System.out.print((value == null ? " " : value) + "|");
			}
			System.out.println("\n------\n");
		}
	}
}
