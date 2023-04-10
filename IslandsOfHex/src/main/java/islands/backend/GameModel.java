/*
 * Project taken from http://nifty.stanford.edu/2023/godbout-islands-of-hex/
 * Credit for all the code outside of this file goes to Andrew Godbout
 * The following code is created or adapted by Milo Fritzen
 * Start Date: 3/23/23
 */

package islands.backend;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to model the play of the game
 *
 */
public class GameModel {

	// used to keep things readable in makePlay function
    public static final boolean WHITE = true;
    public static final boolean BLACK = false;
    
    // create the graph that that game is played on
	public Graph<String, DefaultEdge> gameBoard = new SimpleGraph<>(DefaultEdge.class);
	// create a dictionary that stores the tiles on the board and the clr linked to them
	public Map<String, Integer> hexDict = new HashMap<>();
	
    /**
     * Construct a game with given sizexsize and an empty game board
     * @param sz the square size of the board
     */
    public GameModel(int sz) {
    	// populate the graph with a node at each point
    	for (int row = 0; row < sz; row++) {
    	    for (int col = 0; col < sz; col++) {
    	        String position = row + "," + col;
    	        // add the new point to the graph
    	        gameBoard.addVertex(position);
    	        // add edges to adjacent hexes if possible
    	        if (row > 0) {
    	        	String upPos = (row - 1) + "," + col;
    	        	gameBoard.addEdge(position, upPos);
    	        }
    	        if (col > 0) {
    	        	String leftPos = row + "," + (col - 1);
    	        	gameBoard.addEdge(position, leftPos);
    	        }
    	        
    	        // link the number 0 (symbolizing a grey tile) to it in the map
    	        hexDict.put(position, 0);
    	    }
    	}
    	
    	// check graph is running properly
    	// System.out.println(gameBoard);
        
    }

    /**
     * Can a play be made at position row, col
     * @param row the row in question
     * @param col the col in question
     * @return true if row, col is empty, false o.w.
     * @throws IllegalArgumentException for invalid row and col
     */
    public boolean canPlay(int row, int col) {
    	String checkHex = row + "," + col;
        if (hexDict.get(checkHex) == 0) {
        	return true;
        }
        return false;
    }

    /**
     * play a piece and report if the game is over (true) false, otherwise
     * @param row the row where a piece is played
     * @param col the col where a piece is played
     * @param clr -1 for WHITE and 1 for BLACK
     * @return true if the game is over and false otherwise
     * @throws IllegalArgumentException for invalid row and col
     */
    public boolean makePlay(int row, int col, boolean clr) {
    	String setHex = row + "," + col;
    	int newClr = 1;
    	if (clr == WHITE) {newClr = -1;}
        hexDict.put(setHex, newClr);
        
        // TODO: check if an island spans the entire height
        return false;
    }

    /**
     * Return the score for white
     * @return white score
     */
    public int whiteScore() {
        return 0;
    }

    /**
     * return the score for black
     * @return black score
     */
    public int blackScore() {
        return 0;
    }
}
