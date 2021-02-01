/* JavaFXTemplate.java */
//
// <JUDY PARK>
// U. Of Illinois, Chicago
// CS 342: Fall 2020
// NetID: jpark379
// Email: jpark379@uic.edu
// Description: A program that uses JavaFX and uses UI to display a game of tic tac toe
//				using AI to have two computers play against each other.

/* IMPORTS */
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.util.concurrent.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

// JAVAFXTEMPLATE: The class that displays the GUI
public class JavaFXTemplate extends Application {
	
	// Create a game object
	Game game = new Game();
	
	// Index on the board when updating it
	int i = 0;
	
	// The number of games played per round
	int numGamePlayed = 0;
	
	// Strings that will hold the levels of each player
	String xLevelStr, oLevelStr;

	// An ArrayList that will hold the moves in order after being conjured from the HL threads
	private ArrayList<Integer> moves = new ArrayList<Integer>();
	
	// Hashmap that will contain all the scenes
	private HashMap<String, Scene> mapOfScenes;
	
	// MenuBar that will hold menu
	private MenuBar menuBar;
	
	// Menu that will contain instructions, user guide, and exit
	private Menu menu;
	
	// MenuItems for menuBar
	private MenuItem instructions, userGuide, exit;
	
	// Button for return from menuBar items
	private Button backInst, backUG;
	
	// ChoiceBoxes for selecting numGame and various levels
	private ChoiceBox<Integer> numGame;
	private ChoiceBox<String> xLevel, oLevel;
	
	// Text objects that will prompt the user to the number of games they
	// wanted to watch and the number of games they watched so far
	private Text numGamesPlayedText;
	private Text numGamesText;
	
	// Text objects will prompt the user to the levels they want each
	// player to be
	private Text xLevelText;
	private Text oLevelText;
	
	// Text object that will display if one of the fields was not inputted
	private Text playScreenError;
	
	// Texts to keep track of the score
	private Text xScore, oScore;
		
	// Texts to keep track of the winner
	private Text winner;
	
	// Texts for the tictactoe board display
	private Text c0r0, c1r0, c2r0,
				 c0r1, c1r1, c2r1,
				 c0r2, c1r2, c2r2;
	
	// GridPane that holds the game board
	private GridPane board;
	
	// Button to start the game
	private Button start;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// initialize the hash map
		mapOfScenes = new HashMap<String, Scene>();
		
		// Initialize the texts
		c0r0 = new Text("-");
		c1r0 = new Text("-");
		c2r0 = new Text("-");
		c0r1 = new Text("-");
		c1r1 = new Text("-");
		c2r1 = new Text("-");
		c0r2 = new Text("-");
		c1r2 = new Text("-");
		c2r2 = new Text("-");
		
		// create the welcome screen and put it into the map
		mapOfScenes.put("welcome", createWelcome(primaryStage));
		mapOfScenes.put("gameplay", createGamePlay(primaryStage));
		mapOfScenes.put("instructions", instructionScene(primaryStage));
		mapOfScenes.put("userGuide", userGuideScene(primaryStage));
		mapOfScenes.put("results", createResultScene(primaryStage));
		
		// ----------------- EVENT HANDLERS AND RESPECTIVE TIMELINES ------------------
		
			// Change to the gameplay screen after event
			EventHandler<ActionEvent> eventHandler = (e -> {
				primaryStage.setScene(mapOfScenes.get("gameplay"));
			});
			
			// Delay for 3 seconds and call the eventhandler
			Timeline timeline = new Timeline (
					new KeyFrame(Duration.seconds(3), eventHandler),
					new KeyFrame(Duration.millis(5000))
			);
			
			// Change to the gameplay screen after event
			EventHandler<ActionEvent> resultScene = (e -> {
				// Increment the number of games played
				numGamePlayed++;
				
				// Set the start button as visible again
				start.setVisible(true);
				
				// Get the "results" scene
				primaryStage.setScene(mapOfScenes.get("results"));
				
				// Reset the board
				resetBoard();
				
				// Play the timeline Timeline
				timeline.play();
				
			});
			
			// Delay for 3 seconds and call the eventhandler
			Timeline timelineResults = new Timeline (
					new KeyFrame(Duration.seconds(3), resultScene),
					new KeyFrame(Duration.millis(1000))
			);
			
			// Update the board
			EventHandler<ActionEvent> update = (e -> {
				// Call the update board function
				updateBoard(i);
				
				// Incrememnt i
				i++;
				
				// If the number of i match the moves size array, display the scores
				// and resume to the timelineResults Timeline
				if (i == moves.size()) {
					xScore.setText(Integer.toString(game.getXTotalWins()));
					oScore.setText(Integer.toString(game.getOTotalWins()));
					timelineResults.play();
				}
			});
			
			// Delay for 1 seconds and call the eventhandler
			Timeline timelineUpdateBoard = new Timeline (
					new KeyFrame(Duration.seconds(1), update),
					new KeyFrame(Duration.millis(1000))
			);
			
			// Cycle through timelineUpdateBoard 9 times		
			timelineUpdateBoard.setCycleCount(9);
			
		// ----------------- INSTRUCTIONS EVENT HANDLER ------------------
			
			// Instructions event handler
			instructions.setOnAction(e -> {
				primaryStage.setScene(mapOfScenes.get("instructions"));
			});
		
			// Back button for instructions scene
			backInst.setOnAction(e -> {
				primaryStage.setScene(mapOfScenes.get("gameplay"));
			});
		
		// ----------------- USER GUIDE EVENT HANDLER ------------------
			
			// User guide event handler
			userGuide.setOnAction(e -> {
				primaryStage.setScene(mapOfScenes.get("userGuide"));
			});
		
			// Back button for the user guide scene
			backUG.setOnAction(e -> {
				primaryStage.setScene(mapOfScenes.get("gameplay"));
			});
			
		// ----------------- START EVENT HANDLER ------------------
			start.setOnAction(e -> {
				
				// String that stores the error message
				String error = "";
				
				// Check that choice boxes have been selected
				// If not, display the error message on GUI
				if (xLevel.getValue() == null) {
					error += "Please select a level for X\n";
					playScreenError.setText(error);
					playScreenError.setVisible(true);
				}
				if (oLevel.getValue() == null) {
					error += "Please select a level for O\n";
					playScreenError.setText(error);
					playScreenError.setVisible(true);
				}
				if (numGame.getValue() == null) {
					error += "Please select the number of games\n";
					playScreenError.setText(error);
					playScreenError.setVisible(true);
				}
				
				// If all fields were selected, continue to get the moves and update the board
				if ( xLevel.getValue() != null && oLevel.getValue() != null && numGame.getValue() != null) {
					
					// If button was pressed once, hide it
					start.setVisible(false);
					
					// If there was an error message, hide it
					playScreenError.setVisible(false);
					
					// Get the values of each level
					xLevelStr = xLevel.getValue();
					oLevelStr = oLevel.getValue();
					
					// Call the getMoves function and store result into moves ArrayList
					moves = game.getMoves(xLevelStr, oLevelStr);
					
					// Call to play timelineUpdateBoard Timeline
					timelineUpdateBoard.play();
					
					// Grab the results scene
					mapOfScenes.put("results", createResultScene(primaryStage));
				}
				
				// Reset the error message
				error = "";
			});
			
		// ----------------- EXIT EVENT HANDLER ------------------
			
			// Exit event handler
			exit.setOnAction(e -> {
				System.exit(0);
			});
		
		// Play the timeline
		timeline.play();
		
		// Set the scene to the welcome screen and show
		primaryStage.setScene(mapOfScenes.get("welcome"));
		primaryStage.show();
	}
 
	// CREATEWELCOME: creates the welcome screen
	public Scene createWelcome(Stage primaryStage) {
		
		// ----------------- TEXT ~ WELCOME MESSAGE ------------------
		// Create a welcome text
		Text welcome = new Text("WELCOME TO AI TIC TAC TOE");
		
		// Set the font
		welcome.setFont(Font.font("Helvetica",FontWeight.BOLD,20));
		welcome.setFill(Color.BURLYWOOD);
		
		
		// ----------------- VBOX ------------------
		// VBox for layout
		VBox startBox = new VBox(welcome);
		startBox.setAlignment(Pos.CENTER);
		
		// ----------------- BORDERPANE ------------------
		// Create a borderpane to organize the window
		BorderPane pane = new BorderPane();
		pane.setCenter(startBox);
		
		pane.setStyle("-fx-background-color: azure;");
		return new Scene(pane, 700, 700);
	}
	
	// CREATEGAMEPLAY: creates the game play screen
	public Scene createGamePlay(Stage primaryStage) {
		
		//  ----------------- TEXT ~ # OF GAMES PLAYED ------------------
			numGamesPlayedText = new Text("GAME NO. " + Integer.toString(numGamePlayed + 1));
			
			// Set the font
			numGamesPlayedText.setFont(Font.font("Helvetica",FontWeight.BOLD,14));
			numGamesPlayedText.setFill(Color.BURLYWOOD);
			
			
		// ----------------- MENUBAR ~ ERROR MESSAGE ------------------
			playScreenError = new Text();
			playScreenError.setVisible(false);
			
			// Set the font
			playScreenError.setFont(Font.font("Helvetica",FontWeight.BOLD,14));
			playScreenError.setFill(Color.BURLYWOOD);
		
		// ----------------- MENUBAR ~ INSTRUCTIONS, USERGUIDE, EXIT ------------------
			// Define the menubar along with its items
			menuBar = new MenuBar();
			menu = new Menu("MENU");
			instructions = new MenuItem("instructions");
			userGuide = new MenuItem("user guide");
			exit = new MenuItem("exit");
		
		// ----------------- CHOICEBOXES & TEXT ~ CHOOSE NUMBER OF GAMES ------------------
			// Create text to prompt user and define choice boxes for user to pick the number of games
			numGamesText = new Text("PICK NO. OF GAMES TO PLAY: ");
			
			// Set the font
			numGamesText.setFont(Font.font("Helvetica",FontWeight.BOLD,14));
			numGamesText.setFill(Color.BURLYWOOD);
			
			// Set up the choiceboxes
			Integer numGamesArr[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
			numGame = new ChoiceBox<Integer>(FXCollections.observableArrayList(numGamesArr));
			
			// Style the choiceboxes
			numGame.setStyle("-fx-background-color: FLORALWHITE;");
		
		// ----------------- GRIDPANE ~ GAME BOARD ------------------
			// Create the game board
			board = createBoard();
			board.setAlignment(Pos.CENTER);
		
		// ----------------- BUTTON ~ START GAME ------------------
			// Create the start game button
			start = new Button("START THE GAME");
			start.setStyle("-fx-background-color: FLORALWHITE;");
		
		
		// ----------------- CHOICEBOXES & TEXT ~ CHOOSE LEVEL ------------------
			// Create text to prompt user and define choice boxes for user to pick the level
			xLevelText = new Text("LEVEL FOR PLAYER X: ");
			oLevelText = new Text("THE LEVEL FOR PLAYER O: ");
			
			// Set the font
			xLevelText.setFont(Font.font("Helvetica",FontWeight.BOLD,14));
			xLevelText.setFill(Color.BURLYWOOD);
			
			oLevelText.setFont(Font.font("Helvetica",FontWeight.BOLD,14));
			oLevelText.setFill(Color.BURLYWOOD);
			
			// Set up the choiceboxes
			String levels[] = {"novice", "advanced", "expert"};
			xLevel = new ChoiceBox<String>(FXCollections.observableArrayList(levels));
			oLevel = new ChoiceBox<String>(FXCollections.observableArrayList(levels));
			
			// Style the choiceboxes
			xLevel.setStyle("-fx-background-color: FLORALWHITE;");
			oLevel.setStyle("-fx-background-color: FLORALWHITE;");
			
			// Place the menu items inside of menu and into the menu bar
			menu.getItems().addAll(instructions, userGuide, exit);
			menuBar.getMenus().add(menu);
			menuBar.setStyle("-fx-background-color: FLORALWHITE;");
			
		// ----------------- TEXT ~ SCORES ------------------
			// Initialize the scores respectively for each side
			Text xScorePrompt = new Text("SCORE FOR X: ");
			Text oScorePrompt = new Text("SCORE FOR O: ");
			
			String xScoreStr = "";
			String oScoreStr = "";
			
			xScore = new Text(xScoreStr);
			oScore = new Text(oScoreStr);
			
			// Set the font
			xScorePrompt.setFont(Font.font("Helvetica",FontWeight.BOLD,14));
			xScorePrompt.setFill(Color.BURLYWOOD);
			
			xScore.setFont(Font.font("Helvetica",FontWeight.BOLD,14));
			xScore.setFill(Color.BURLYWOOD);
			
			oScorePrompt.setFont(Font.font("Helvetica",FontWeight.BOLD,14));
			oScorePrompt.setFill(Color.BURLYWOOD);
			
			oScore.setFont(Font.font("Helvetica",FontWeight.BOLD,14));
			oScore.setFill(Color.BURLYWOOD);
		
		// ----------------- HBOX ------------------
			// HBox that holds prompt and choicebox for number of games
			HBox numGamesBox = new HBox(numGamesText, numGame);
			numGamesBox.setAlignment(Pos.CENTER);
			numGamesBox.setSpacing(10.0);
			
			// HBox that holds the prompt and choicebox for picking player x's level
			HBox xLevelHBox = new HBox(xLevelText, xLevel);
			xLevelHBox.setAlignment(Pos.CENTER_RIGHT);
			xLevelHBox.setSpacing(10.0);
			
			// HBox that holds the prompt and choicebox for picking player o's level
			HBox oLevelHBox = new HBox(oLevelText, oLevel);
			oLevelHBox.setAlignment(Pos.CENTER_LEFT);
			oLevelHBox.setSpacing(10.0);
			
			// HBox that holds the picking of levels
			HBox levelHBox = new HBox(xLevelHBox, oLevelHBox);
			levelHBox.setAlignment(Pos.CENTER);
			levelHBox.setSpacing(40.0);
		
			// HBox that holds the start button
			HBox startHBox = new HBox(start);
			startHBox.setAlignment(Pos.CENTER);
			
			// HBox that holds the x score
			HBox xScoreHBox = new HBox(xScorePrompt, xScore);
			xScoreHBox.setAlignment(Pos.CENTER_RIGHT);
			xScoreHBox.setSpacing(10.0);
			
			// HBox that holds the o score
			HBox oScoreHBox = new HBox(oScorePrompt, oScore);
			oScoreHBox.setAlignment(Pos.CENTER_LEFT);
			oScoreHBox.setSpacing(10.0);
			
			// HBox that holds the two scores
			HBox scores = new HBox(xScoreHBox, oScoreHBox);
			scores.setAlignment(Pos.CENTER);
			scores.setSpacing(40.0);
			
			
		// ----------------- VBOX ------------------
			
			// VBox that holds the number of games played and error
			VBox errorNumGames = new VBox(numGamesPlayedText, playScreenError);
			errorNumGames.setAlignment(Pos.CENTER);
			
			VBox gameVBox = new VBox(menuBar, numGamesBox, levelHBox, board, startHBox, scores, playScreenError, errorNumGames);
			gameVBox.setSpacing(40.0);
		
		// ----------------- BORDERPANE ------------------
			// Create a borderpane to organize the window
			BorderPane pane = new BorderPane();
			pane.setTop(gameVBox);
			pane.setStyle("-fx-background-color: mistyrose;");
			return new Scene(pane, 700, 700);
	}
	
	// INSTRUCTIONSCENE: creates the game play screen
	public Scene instructionScene(Stage primaryStage) {
		
		// ----------------- TEXT ------------------
		// Create a new text storing the instructions
		String instructions = "This is an AI Tic Tac Toe game.\n"
							+ "One player will play as X and the other will play as O.\n"
							+ "The goal of the game is for one of the player to get their pieces\n"
							+ "in an adjacent diagonal, column or row of 3.\n"
							+ "If neither of them gets a row, diagonal, or column of 3, it is a tie.";
		
		// Store the string into the text
		Text instText = new Text(instructions);
		
		// Create a header for this scene
		Text instHeader = new Text("THE RULES");
		
		// Set the font
		instText.setFont(Font.font("Helvetica",FontWeight.BOLD,14));
		instText.setFill(Color.MINTCREAM);
		instHeader.setFont(Font.font("Helvetica",FontWeight.BOLD,30));
		instHeader.setFill(Color.SEAGREEN);
		
		// ----------------- BUTTON ------------------
		// Define the back button
		backInst = new Button("BACK");
		backInst.setStyle("-fx-background-color: FLORALWHITE;");
		
		// ----------------- VBOX ------------------
		// Layout for the instructions scene
		VBox instVBox = new VBox(instHeader, instText, backInst);
		instVBox.setSpacing(50.0);
		instVBox.setAlignment(Pos.CENTER);
		
		// ----------------- BORDERPANE ------------------
		// Create a borderpane to organize the window
		BorderPane pane = new BorderPane();
		
		pane.setCenter(instVBox);
		pane.setStyle("-fx-background-color: thistle;");
		return new Scene(pane, 700, 700);
	}
	
	// USERGUIDESCENE: creates the game play screen
	public Scene userGuideScene(Stage primaryStage) {
		
		// ----------------- TEXT ------------------
		// Create a new text with the instructions to the user
		String userGuideStr = "In the game screen, pick a level for each player.\n"
							+ "The different levels include novice, advanced, and expert.\n"
							+ "You may also choose to watch up to 10 games in a row.\n"
							+ "Simply make those selections in the previous screen.\n"
							+ "Start the game by simply clicking the start button.\n"
							+ "After the game ends, a winder will be declared.\n"
							+ "Scores will also be kept on the game screen.";
		
		// Store userGuide string to the text
		Text userGuideText = new Text(userGuideStr);
		
		// Create a header
		Text ugHeader = new Text("HOW TO PLAY");
		
		// Set the font
		userGuideText.setFont(Font.font("Helvetica",FontWeight.BOLD,14));
		userGuideText.setFill(Color.ALICEBLUE);
		ugHeader.setFont(Font.font("Helvetica",FontWeight.BOLD,30));
		ugHeader.setFill(Color.CORNFLOWERBLUE);
		
		// ----------------- BUTTON ------------------
		// Back button for user guide
		backUG = new Button("BACK");
		backUG.setStyle("-fx-background-color: FLORALWHITE;");
		
		// ----------------- VBOX ------------------
		// Create layout for the user guide scene
		VBox instVBox = new VBox(ugHeader, userGuideText, backUG);
		instVBox.setSpacing(50.0);
		instVBox.setAlignment(Pos.CENTER);
		
		// ----------------- BORDERPANE ------------------
		// Create a borderpane to organize the window
		BorderPane pane = new BorderPane();
		pane.setCenter(instVBox);
		pane.setStyle("-fx-background-color: LIGHTSALMON;");
		return new Scene(pane, 700, 700);
	}
	
	// CREATERESULTSSCENE: creates the page taht displays who won
	public Scene createResultScene(Stage primaryStage) {
		
		// ----------------- TEXT ------------------
		// Create a text that creates the title
		Text winnerPrompt = new Text();
		winner = new Text(game.checkWin());
		
		// If there was a Tie, prompt with "THERE WAS A"
		if (game.checkWin().equals("TIE")) {
			winnerPrompt.setText("THERE WAS A");
		}
		// Otherwise, prompt with a "THE WINNER IS"
		else {
			winnerPrompt.setText("THE WINNER IS");
		}
		
		// Set Font
		winnerPrompt.setFont(Font.font("Helvetica",FontWeight.BOLD,30));
		winnerPrompt.setFill(Color.FLORALWHITE);
		
		winner.setFont(Font.font("Helvetica",FontWeight.BOLD,30));
		winner.setFill(Color.PAPAYAWHIP);
		
		// ----------------- VBOX ------------------
		VBox winnerVBox = new VBox(winnerPrompt, winner);
		winnerVBox.setAlignment(Pos.CENTER);
		winnerVBox.setSpacing(15);
		
		// ----------------- BBORDER PANE ------------------
		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(winnerVBox);
		borderPane.setStyle("-fx-background-color: BURLYWOOD;");
		return new Scene(borderPane, 700, 700);
	}
	
	// RESETBOARD: resets the whole GUI after a round ends
	public void resetBoard() {
		// Reset i
		i = 0;
		
		// Clear the GUI board to its inital state
		c0r0.setText("-");
		c1r0.setText("-");
		c2r0.setText("-");
		c0r1.setText("-");
		c1r1.setText("-");
		c2r1.setText("-");
		c0r2.setText("-");
		c1r2.setText("-");
		c2r2.setText("-");
		
		// If the number of rounds did not end...
		if (numGame.getValue() > 1 && numGamePlayed != numGame.getValue()) {
			// Set the prompts/choiceboxes for user input as hidden
			numGamesText.setVisible(false);
			xLevelText.setVisible(false);
			oLevelText.setVisible(false);
			numGame.setVisible(false);
			xLevel.setVisible(false);
			oLevel.setVisible(false);
			
			// Clear the board for the game object
			game.clearBoard();
			
			// Update the number of games played
			numGamesPlayedText.setText("GAME NO. " + Integer.toString(numGamePlayed + 1));
		}
		
		// If the number of rounds ended
		else {
			// Reset the number of games played to 0 and make it reflect on the text
			numGamePlayed = 0;
			numGamesPlayedText.setText("GAME NO. " + Integer.toString(numGamePlayed + 1));
			
			// Show the prompts/choiceboxes for user input again
			numGamesText.setVisible(true);
			xLevelText.setVisible(true);
			oLevelText.setVisible(true);
			numGame.setVisible(true);
			xLevel.setVisible(true);
			oLevel.setVisible(true);
			
			// Make the scores empty
			xScore.setText("");
			oScore.setText("");
			
			// Clear the board and game for the game object
			game.clearBoard();
			game.clearGame();
			
			// Clear the previous selections in the choiceboxes
			xLevel.getSelectionModel().clearSelection();
			oLevel.getSelectionModel().clearSelection();
			numGame.getSelectionModel().clearSelection();
		}
		
	}
	
	// UPDATEBBOARD: Updates each text that represents the tic tac toe board
	public void updateBoard(int i) {
		
		// If i is less than the size of moves
		if ( i < moves.size()) {
			
			// X was chosen
			if (i % 2 == 0) {
				
				// Go through and find the index at the i-th pos
				// of moves. Update the GUI board accordingly
				if ( moves.get(i) == 1) {
					c0r0.setText("X");
				}
				if ( moves.get(i) == 2) {
					c1r0.setText("X");
				}
				if ( moves.get(i) == 3) {
					c2r0.setText("X");
				}
				if ( moves.get(i) == 4) {
					c0r1.setText("X");
				}
				if ( moves.get(i) == 5) {
					c1r1.setText("X");
				}
				if ( moves.get(i) == 6) {
					c2r1.setText("X");
				}
				if ( moves.get(i) == 7) {
					c0r2.setText("X");
				}
				if ( moves.get(i) == 8) {
					c1r2.setText("X");
				}
				if ( moves.get(i) == 9) {
					c2r2.setText("X");
				}
			}
			
			// O was chosen
			else {
				// Go through and find the index at the i-th pos
				// of moves. Update the GUI board accordingly
				if ( moves.get(i) == 1) {
					c0r0.setText("O");
				}
				if ( moves.get(i) == 2) {
					c1r0.setText("O");
				}
				if ( moves.get(i) == 3) {
					c2r0.setText("O");
				}
				if ( moves.get(i) == 4) {
					c0r1.setText("O");
				}
				if ( moves.get(i) == 5) {
					c1r1.setText("O");
				}
				if ( moves.get(i) == 6) {
					c2r1.setText("O");
				}
				if ( moves.get(i) == 7) {
					c0r2.setText("O");
				}
				if ( moves.get(i) == 8) {
					c1r2.setText("O");
				}
				if ( moves.get(i) == 9) {
					c2r2.setText("O");
				}
			}
		}
		
	}
	
	// CREATEBOARD: creates the GUI game board
	public GridPane createBoard() {
		
		// Create the board GridPane
		GridPane grid = new GridPane();
		
		// Set the gaps between each Text
		grid.setHgap(50);
		grid.setVgap(20);
		
		// Style the texts
		c0r0.setFont(Font.font("Helvetica",FontWeight.BOLD,50));
		c0r0.setFill(Color.BURLYWOOD);
		
		c1r0.setFont(Font.font("Helvetica",FontWeight.BOLD,50));
		c1r0.setFill(Color.BURLYWOOD);
		
		c2r0.setFont(Font.font("Helvetica",FontWeight.BOLD,50));
		c2r0.setFill(Color.BURLYWOOD);
		
		c0r1.setFont(Font.font("Helvetica",FontWeight.BOLD,50));
		c0r1.setFill(Color.BURLYWOOD);
		
		c1r1.setFont(Font.font("Helvetica",FontWeight.BOLD,50));
		c1r1.setFill(Color.BURLYWOOD);
		
		c2r1.setFont(Font.font("Helvetica",FontWeight.BOLD,50));
		c2r1.setFill(Color.BURLYWOOD);
		
		c0r2.setFont(Font.font("Helvetica",FontWeight.BOLD,50));
		c0r2.setFill(Color.BURLYWOOD);
		
		c1r2.setFont(Font.font("Helvetica",FontWeight.BOLD,50));
		c1r2.setFill(Color.BURLYWOOD);
		
		c2r2.setFont(Font.font("Helvetica",FontWeight.BOLD,50));
		c2r2.setFill(Color.BURLYWOOD);
		
		// Add each text onto the GridPane
		grid.add(c0r0, 0, 0);
		grid.add(c1r0, 1, 0);
		grid.add(c2r0, 2, 0);
		grid.add(c0r1, 0, 1);
		grid.add(c1r1, 1, 1);
		grid.add(c2r1, 2, 1);
		grid.add(c0r2, 0, 2);
		grid.add(c1r2, 1, 2);
		grid.add(c2r2, 2, 2);
		
		return grid;
	}
	
}