import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * This class is used to read in a state of a tic tac toe board. It creates a MinMax object and passes the state to it. What returns is a list 
 * of possible moves for the player X that have been given min/max values by the method findMoves. The moves that can result in a win or a 
 * tie for X are printed out with the method printBestMoves()
 * 
 * @author Mark Hallenbeck
 *
 * Copyright© 2014, Mark Hallenbeck, All Rights Reservered.
 *
 */
public class AI_MinMax {
	
	private String[] init_board;
	
	private ArrayList<Node> movesList;
	
	private ArrayList<Integer> expertMoves = new ArrayList<Integer>();
	private ArrayList<Integer> advancedMoves = new ArrayList<Integer>();
	private ArrayList<Integer> noviceMoves = new ArrayList<Integer>();
	private ArrayList<Integer> defaultMoves = new ArrayList<Integer>();
	
	AI_MinMax(String[] board)
	{
		init_board = board;
		
		if(init_board.length != 9)
		{
			System.out.println("You have entered an invalid state for tic tac toe, exiting......");
			System.exit(-1);
		}
		
		MinMax sendIn_InitState = new MinMax(init_board);
		
		movesList = sendIn_InitState.findMoves();
		
		printBestMoves();
	}
	
	
	
	/**
	 * goes through a node list and prints out the moves with the best result for player X
	 * checks the min/max function of each state and only recomends a path that leads to a win or tie
	 */
	private void printBestMoves()
	{
		System.out.print("\n\nThe moves list is: < ");
		
		for(int x = 0; x < movesList.size(); x++)
		{
			Node temp = movesList.get(x);

			System.out.print(temp.getMovedTo() + " ");
		}
		
		System.out.print(">");
	}
	
	// GETEXPERTMOVE: Retrieves only the winning/normal moves
	public Integer getExpertMove() {
		int index = 0;
		Node temp;
		
		// Loop through the movesList ArrayList
		for ( int x = 0; x < movesList.size(); x++) {
			temp = movesList.get(x);
			
			// Store all the moves onto defaultMoves ArrayList
			defaultMoves.add(temp.getMovedTo());
			
			// If the move is a winning move, store it into the expertMoves ArrayList
			if (temp.getMinMax() == 10) {
				expertMoves.add(temp.getMovedTo());
			}
			
			// If the move does not win but does not lose, store it into the advancedMoves ArrayList
			else if (temp.getMinMax() == 0) {
				advancedMoves.add(temp.getMovedTo());
			}
		}
		
		// If the expertMoves ArrayList is not empty
		if (expertMoves.size() > 0) {
			// Pick a random index from that ArrayList and return the value
			Random r = new Random();
			index = r.nextInt(expertMoves.size());
			return expertMoves.get(index);
		}
		
		// If the advancedMoves ArrayList is not empty
		else if (advancedMoves.size() > 0) {
			// Pick a random index from that ArrayList and return the value
			Random r = new Random();
			index = r.nextInt(advancedMoves.size());
			return advancedMoves.get(index);
		}
		
		// If the expertMoves ArrayList is empty and so is the advancedMoves ArrayList
		// Return any move from the defaultMoves ArrayList
		Random r = new Random();
		index = r.nextInt(defaultMoves.size());
		return defaultMoves.get(index);
		
	}

	// GETADVANCEDMOVES: Retrieves the normal/winning moves
	public Integer getAdvancedMoves() {
		
		int index = 0;
		Node temp;
		
		// Loop through the movesList ArrayList
		for ( int x = 0; x < movesList.size(); x++) {
			temp = movesList.get(x);
			
			// Store all the moves onto defaultMoves ArrayList
			defaultMoves.add(temp.getMovedTo());
			
			// If the move is a winning or a normal move, store it into the advancedMoves ArrayList
			if (temp.getMinMax() == 10 || temp.getMinMax() == 0) {
				advancedMoves.add(temp.getMovedTo());
			}
		}

		// If the advancedMoves ArrayList is not empty
		if (advancedMoves.size() > 0) {
			// Pick a random index from that ArrayList and return the value
			Random r = new Random();
			index = r.nextInt(advancedMoves.size());
			return advancedMoves.get(index);
		}

		// If the advancedMoves ArrayList is empty
		// Return any move from the defaultMoves ArrayList
		Random r = new Random();
		index = r.nextInt(defaultMoves.size());
		return defaultMoves.get(index);
	}

	// GETNOVICEMOVES: Retrieves the normal/losing moves
	public Integer getNoviceMoves() {
	
		int index = 0;
		Node temp;
		
		// Loop through the movesList ArrayList
		for ( int x = 0; x < movesList.size(); x++) {
			temp = movesList.get(x);
			
			// Store all the moves onto defaultMoves ArrayList
			defaultMoves.add(temp.getMovedTo());
			
			// If the move is a losing move, store it into the noviceMoves ArrayList
			if (temp.getMinMax() == -10) {
				noviceMoves.add(temp.getMovedTo());
			}
			
			// If the move does not win but does not lose, store it into the advancedMoves ArrayList
			else if (temp.getMinMax() == 0) {
				advancedMoves.add(temp.getMovedTo());
			}
		}
		
		// If the noviceMoves ArrayList is not empty
		if (noviceMoves.size() > 0) {
			// Pick a random index from that ArrayList and return the value
			Random r = new Random();
			index = r.nextInt(noviceMoves.size());
			return noviceMoves.get(index);
		}
		
		// If the advancedMoves ArrayList is not empty
		else if (advancedMoves.size() > 0) {
			// Pick a random index from that ArrayList and return the value
			Random r = new Random();
			index = r.nextInt(advancedMoves.size());
			return advancedMoves.get(0);
		}
		
		// If the advancedMoves ArrayList is empty and so is the noviceMoves ArrayList
		// Return any move from the defaultMoves ArrayList
		Random r = new Random();
		index = r.nextInt(defaultMoves.size());
		return defaultMoves.get(0);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] str = {"X", "X", "O", "O", "O", "b", "X", "b", "b"};
		AI_MinMax startThis = new AI_MinMax(str);
	}

}
