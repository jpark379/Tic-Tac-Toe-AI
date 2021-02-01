import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// GAME: Class that holds all the logical components for tic tac toe
public class Game {
	
	// Create a HashMap that stores every single move's position and its turn
	HashMap<Integer, String> board = new HashMap<Integer,String>();
	
	// Create an ArrayList that holds the moves
	ArrayList<Integer> moves = new ArrayList<Integer>();
	
	// Create an instance of the HLThreading class
	HLThreading thread = new HLThreading();
	
	// Variables to keep tack of the score
	int xWins = 0;
	int oWins = 0;
	
	// GETMOVES: calls the thread() function in HLThreading which uses
	// threads to conjure moves. Those moves get stored into an ArrayList
	// and that ArrayList gets returned
	public ArrayList<Integer> getMoves(String xLevel, String oLevel) {
		// Call thread() and update moves ArrayList
		thread.thread(moves, xLevel, oLevel);
		
		// Calculate the score
		calcWins();
		
		// Return moves
		return moves;
	}
	
	// GETXTOTALWINS: returns X's scores
	public Integer getXTotalWins() {
		return xWins;
	}
	
	// GETXTOTALWINS: returns O's scores
	public Integer getOTotalWins() {
		return oWins;
	}
	
	// CALCWINS: Figure out who wins by calling checkWin() and
	// updates the score accordingly. This also returns
	// the player who won or "TIE" if there was a draw
	public String calcWins() {
		String winner = checkWin();
		
		// If X won...
		if (winner.equals("X")) {
			xWins++;
			winner = "X";
			return winner;
		}
		
		// If O won...
		else if (winner.equals("O")) {
			oWins++;
			winner = "O";
			return winner;
		}
		
		// None of the conditions were met --> TIE
		return "TIE";
	}
	
	// FILLGAMEBOARD: fills the HashMap with the moves and respective turns
	public void fillGameBoard() {
		// Iterate through moves ArrayList
		for ( int i = 0; i < moves.size(); i++) {
			
			// If i is even, it is an X turn
			if ( i % 2 == 0) {
				board.put(moves.get(i), "X");
			}
			
			// If i is odd, it is an O turn
			else {
				board.put(moves.get(i), "O");
			}
		}
	}
	
	// SETMOVES: Used for testing. This will take in a custom
	// created ArrayList and update moves to be a copy.
	// This is so that you can fill the board manually
	public void setMoves(ArrayList<Integer> movesList) {
		// Iterate through the moves array and update it
		for ( int i = 0; i < movesList.size(); i++) {
			moves.add(movesList.get(i));
		}
	}
	
	// CLEARGAME: clears everything in the Game class:
	// the board, the moves ArrayList, and the scores
	public void clearGame() {
		board.clear();
		moves.clear();
		xWins = 0;
		oWins = 0;
	}
	
	// CLEARBOARD: clears only the board and moves
	public void clearBoard() {
		board.clear();
		moves.clear();
	}
	
	// CHECKWIN: fills the board and checks for any wins
	// If there is one, it will return the name of the winner
	public String checkWin() {
		
		// Fill the board
		fillGameBoard();
		 
		// Check if there is a win at pos 1, 2, 3
		if ( board.get(1) != null && board.get(2) != null && board.get(3) != null) {
			if (board.get(1).equals(board.get(2)) && board.get(1).equals(board.get(3))) {
				return board.get(1);
			}
		}
		
		// Check if there is a win at pos 4, 5, 6
		if ( board.get(4) != null && board.get(5) != null && board.get(6) != null) {
			if (board.get(4).equals(board.get(5)) && board.get(4).equals(board.get(6))) {
				return board.get(4);
			}
		}
		
		// Check if there is a win at pos 7, 8, 9
		if ( board.get(7) != null && board.get(8) != null && board.get(9) != null) {
			if (board.get(7).equals(board.get(8)) && board.get(7).equals(board.get(9))) {
				return board.get(7);
			}
		}
		
		// Check if there is a win at pos 1, 4, 7
		if ( board.get(1) != null && board.get(4) != null && board.get(7) != null) {
			if (board.get(1).equals(board.get(4)) && board.get(1).equals(board.get(7))) {
				return board.get(1);
			}
		}
		
		// Check if there is a win at pos 2, 5, 8
		if ( board.get(2) != null && board.get(5) != null && board.get(8) != null) {
			if (board.get(2).equals(board.get(5)) && board.get(2).equals(board.get(8))) {
				return board.get(2);
			}
		}
		
		// Check if there is a win at pos 3, 6, 9
		if ( board.get(3) != null && board.get(6) != null && board.get(9) != null) {
			if (board.get(3).equals(board.get(6)) && board.get(3).equals(board.get(9))) {
				return board.get(3);
			}
		}
		
		// Check if there is a win at pos 1, 5, 9
		if ( board.get(1) != null && board.get(5) != null && board.get(9) != null) {
			if (board.get(1).equals(board.get(5)) && board.get(1).equals(board.get(9))) {
				return board.get(1);
			}
		}
		
		// Check if there is a win at pos 3, 5, 7
		if ( board.get(3) != null && board.get(5) != null && board.get(7) != null) {
			if (board.get(3).equals(board.get(5)) && board.get(3).equals(board.get(7))) {
				return board.get(3);
			}
		}

		// There was no win, there must be a TIE or it is too soon to call
		return "TIE";
	}
	
	// HLTHREADING: class that utilizes threads to determine the next move
	// a player should take. It uses the AI_MinMax algorithm provided.
	public class HLThreading {
		// String that stores the winner
		String winner;
		
		// THREAD: Thread uses the Executors class to create a thread pool of 9.
		// It then creates a future object that can call the get() function to obtain the next move
		public void thread(ArrayList<Integer> moves, String xLevel, String oLevel) {
			
			// Start off with an empty game board
			String[] gameBoard = {"b", "b", "b", "b", "b", "b", "b", "b", "b"};
			
			// Create an ExecutorService object with 9 threads
			ExecutorService ex = Executors.newFixedThreadPool(9);
			
			// Intialize the turn as X's turn
			String turn = "X";
			
			// Loop through for 9 interations since there are 9 spots on the tic tac toe board
			for(int iter = 0; iter < 9; iter++) {
				
				// Create a future object using the submit function and calling the MyCall constructor
				Future<Integer> future = ex.submit(new MyCall(gameBoard, turn, xLevel, oLevel, iter));
				
				// Try obtaining the index by calling get() function
				// Must be in a try catch block since get() is a blocking function
				try {
					
					// Update index
					Integer index = future.get();
		
					// If the turn is O change it to X -- this is necessary because
					// we invert the board since AI_MinMax takes in X's best move,
					// not O's
					if (turn.equals("O")) {
						turn = "X";
					}
					
					// Update the moves ArrayList
					moves.add(index);
					
					// Check for a winner
					winner = checkWin();
					
					// If X or O wins, there is no point in continuing. Break out of the loop
					if (winner == "X" || winner == "O") {
						break;
					}
					
					// Update the gameboard
					gameBoard[index - 1] = turn;
					
				}catch(Exception e){System.out.println(e.getMessage());}
					
				// Change to the next turn
				// If it is X's turn, make it O's turn
				if(turn.equals("X")) {
					turn = "O";
				}
				
				// If it is O's turn, make it X's turn
				else {
					turn = "X";
				}
				
				// Invert the board
				invertBoard(gameBoard);
			}
			ex.shutdown();
			
		}
	
		// INVERTBOARD: inverts the board of the game
		public void invertBoard(String[] board) {
			// Loop through the board
			for ( int i = 1; i <= board.length; i++) {
				// For every spot there is an X make it a O
				if (board[i-1].equals("X")) {
					board[i-1] = "O";
				}
				// For every spot there is an O make it a X
				else if (board[i-1].equals("O")) {
					board[i-1] = "X";
				}
			}
		}
	
	}
	
	// MYCALL: The class that utilizes the AI_MinMax class to find the expert, advanced, and
	// novice moves
	class MyCall implements Callable<Integer>{
	
		// Get a board of size 9
		String[] board = new String[9];
		
		// String that stores the move
		String move;
		
		// Create an AI_MinMax object
		AI_MinMax ai;
		
		// Create a counter to keep track of the turn
		int counter;
		
		// Strings to store the levels
		String xLevel;
		String oLevel;
		
		// MyCall constructor that initializes the board, ai, move, xLevel, oLevel and counter
		MyCall(String[] game, String move, String xLevel, String oLevel, int count){
			board = game;
			ai = new AI_MinMax(board);
			this.move = move;
			this.xLevel = xLevel;
			this.oLevel = oLevel;
			counter = count;
		}
		
		// CALL: utilizes the ai functions to get the indexes of the moves
		@Override
		public Integer call() throws Exception {
			
			// Set bool as true
			boolean bool = true;
			
			// Set val as 0
			Integer val = 0;	
			
			// Loop while there is a blank spot on the board
			while(bool) {
				
				// If it is X's turn
				if (counter % 2 == 0) {
					
					// If X's level is novice, call the getNoviceMoves() function
					if (xLevel.equals("novice")) {
						val = ai.getNoviceMoves();
					}
					
					// If X's level is advanced, call the getAdvancedMoves() function
					else if (xLevel.equals("advanced")) {
						val = ai.getAdvancedMoves();
					}
					
					// If X's level is expert, call the getExpertMoves() function
					else {
						val = ai.getExpertMove();
					}
				}
				
				// If it is O's turn
				else {
					
					// If O's level is novice, call the getNoviceMoves() function
					if (oLevel.equals("novice")) {
						val = ai.getNoviceMoves();
					}
					
					// If O's level is advanced, call the getAdvancedMoves() function
					else if (oLevel.equals("advanced")) {
						val = ai.getAdvancedMoves();
					}
					
					// If O's level is expert, call the getExpertMoves() function
					else {
						val = ai.getExpertMove();
					}
				}
				
				// If the board val is X or O, keep the bool as true
				if(board[val-1] == "X" || board[val-1] == "O") {
					bool = true;
						
				}
				else {bool = false;}	
			} 
			
			Thread.sleep(100);
			// Return the value/index
			return val;
		}
		
	}

}
