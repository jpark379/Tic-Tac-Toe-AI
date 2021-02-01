import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// MYTEST: 5 JUnit Test cases
class MyTest {
	// ----------------- GAME OBJECT FOR TESTING ------------------
	Game game = new Game();
	
	// Before each test case, clear the game
	@BeforeEach
	void init() {
		game.clearGame();
	}
	
	// Checks when the board is sent in as:
		//		X X O
		//		X O b
		//		O b b
		// O wins.
	@Test
	void testOWin() {
		
		// Make an ArrayList moves
		ArrayList<Integer> moves = new ArrayList<Integer>();
		
		// Add each move onto the list
		moves.add(1);
		moves.add(3);
		moves.add(2);
		moves.add(7);
		moves.add(4);
		moves.add(5);

		// Set the moves in the game object
		game.setMoves(moves);
		
		// Calculate the winner
		game.calcWins();
		
		// Check if O won and scores were updated accordingly
		assertEquals(game.checkWin(), "O");
		assertEquals(game.getXTotalWins(), 0);
		assertEquals(game.getOTotalWins(), 1); 
	}
	
	
	// Checks when the board is sent in as:
		//		X X X
		//		b O b
		//		b O b
		// X wins.
	@Test
	void testXWin() {
		
		// Make an ArrayList moves
		ArrayList<Integer> moves = new ArrayList<Integer>();
		
		// Add each move onto the list
		moves.add(1);
		moves.add(5);
		moves.add(2);
		moves.add(8);
		moves.add(3);

		// Set the moves in the game object
		game.setMoves(moves);
		
		// Calculate the winner
		game.calcWins();
		
		// Check if X won and scores were updated accordingly
		assertEquals(game.checkWin(), "X");
		assertEquals(game.getXTotalWins(), 1);
		assertEquals(game.getOTotalWins(), 0); 
		
		
	}
	
	// Checks when the board is sent in as:
	//		X X O
	//		O X X
	//		X O O
	// TIE occurs
	@Test
	void testTie() {
		
		// Make an ArrayList moves
		ArrayList<Integer> moves = new ArrayList<Integer>();
		
		// Add each move onto the list
		moves.add(1);
		moves.add(3);
		moves.add(2);
		moves.add(8);
		moves.add(6);
		moves.add(4);
		moves.add(7);
		moves.add(9);
		moves.add(5);

		// Set the moves in the game object
		game.setMoves(moves);
		
		// Calculate the winner
		game.calcWins();
		
		// Check there was a TIE and scores were updated accordingly
		assertEquals(game.checkWin(), "TIE");
		assertEquals(game.getXTotalWins(), 0);
		assertEquals(game.getOTotalWins(), 0); 
		
		
	}
	
	// Checks if for 4 games played consecutively
	// it results in a tie
	//		X O b
	//		X b O
	//		X b b
	//	X wins - 1
	
	//		O X b
	//		X O b
	//		b X O
	//	O wins - 1
	
	//		O X b
	//		O X b
	//		b X b
	//	X wins - 2
	
	//		X X O
	//		b b O
	//		b X O
	//	O wins - 2
	
	//		X O X
	//		X O O
	//		O X X
	//	Tie
	@Test
	void testCount() {
		
		// Make an ArrayList moves
		ArrayList<Integer> moves = new ArrayList<Integer>();
		
		// Add each move onto the list
		moves.add(1);
		moves.add(2);
		moves.add(4);
		moves.add(6);
		moves.add(7);
		
		// Set the moves in the game object
		game.setMoves(moves);
		
		// Calculate the winner
		game.calcWins();
		
		// Check if X won and scores were updated accordingly
		assertEquals(game.checkWin(), "X");
		assertEquals(game.getXTotalWins(), 1);
		assertEquals(game.getOTotalWins(), 0);
		
		// Clear the board and the local moves ArrayList
		game.clearBoard();
		moves.clear();
		
		// Add each move onto the list
		moves.add(2);
		moves.add(1);
		moves.add(4);
		moves.add(5);
		moves.add(8);
		moves.add(9);
		
		// Set the moves in the game object
		game.setMoves(moves);
		
		// Calculate the winner
		game.calcWins();
		
		// Check if O won and scores were updated accordingly
		assertEquals(game.checkWin(), "O");
		assertEquals(game.getXTotalWins(), 1);
		assertEquals(game.getOTotalWins(), 1);
		
		// Clear the board and the local moves ArrayList
		game.clearBoard();
		moves.clear();
		
		// Add each move onto the list
		moves.add(2);
		moves.add(1);
		moves.add(5);
		moves.add(4);
		moves.add(8);
		
		// Set the moves in the game object
		game.setMoves(moves);
		
		// Calculate the winner
		game.calcWins();
		
		// Check if X won and scores were updated accordingly
		assertEquals(game.checkWin(), "X");
		assertEquals(game.getXTotalWins(), 2);
		assertEquals(game.getOTotalWins(), 1);
		
		// Clear the board and the local moves ArrayList
		game.clearBoard();
		moves.clear();
		
		// Add each move onto the list
		moves.add(2);
		moves.add(3);
		moves.add(1);
		moves.add(6);
		moves.add(8);
		moves.add(9);
		
		// Set the moves in the game object
		game.setMoves(moves);
		
		// Calculate the winner
		game.calcWins();
		
		// Check if O won and scores were updated accordingly
		assertEquals(game.checkWin(), "O");
		assertEquals(game.getXTotalWins(), 2);
		assertEquals(game.getOTotalWins(), 2);
		
		// Clear the board and the local moves ArrayList
		game.clearBoard();
		moves.clear();
		
		// Add each move onto the list
		moves.add(1);
		moves.add(2);
		moves.add(3);
		moves.add(5);
		moves.add(4);
		moves.add(6);
		moves.add(8);
		moves.add(7);
		moves.add(9);
		
		// Set the moves in the game object
		game.setMoves(moves);
		
		// Calculate the winner
		game.calcWins();
		
		// Check if there was a TIE and scores were updated accordingly
		assertEquals(game.checkWin(), "TIE");
		assertEquals(game.getXTotalWins(), 2);
		assertEquals(game.getOTotalWins(), 2);
		
		
	}
	
	// Checks the remaining cases if there is a win
	//		O O b
	//		X X X
	//		b b b
	//	X wins - 1
	
	//		X b b
	//		X X b
	//		O O O
	//	O wins - 1
	@Test
	void testCount2() {
		
		// Make an ArrayList moves
		ArrayList<Integer> moves = new ArrayList<Integer>();
		
		// Add each move onto the list
		moves.add(4);
		moves.add(1);
		moves.add(5);
		moves.add(2);
		moves.add(6);
		
		// Set the moves in the game object
		game.setMoves(moves);
		
		// Calculate the winner
		game.calcWins();
		
		// Check if there was a TIE and scores were updated accordingly
		assertEquals(game.checkWin(), "X");
		assertEquals(game.getXTotalWins(), 1);
		assertEquals(game.getOTotalWins(), 0);
		
		// Clear the board and the local moves ArrayList
		game.clearBoard();
		moves.clear();
		
		// Add each move onto the list
		moves.add(1);
		moves.add(7);
		moves.add(4);
		moves.add(8);
		moves.add(5);
		moves.add(9);
		
		// Set the moves in the game object
		game.setMoves(moves);
		
		// Calculate the winner
		game.calcWins();
		
		// Check if there was a TIE and scores were updated accordingly
		assertEquals(game.checkWin(), "O");
		assertEquals(game.getXTotalWins(), 1);
		assertEquals(game.getOTotalWins(), 1);
	}
}
