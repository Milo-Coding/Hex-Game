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

import java.util.ArrayList;

/**
 * Class to model the play of the game
 *
 */
public class GameModel {

	// used to keep things readable in makePlay function
    public static final boolean WHITE = true;
    public static final boolean BLACK = false;
    // tracks the length/width of the game board
    public int gameEdge;
    
    // create the graph that that game is played on
	public Graph<String, DefaultEdge> gameBoard = new SimpleGraph<>(DefaultEdge.class);
	// create a dictionary that stores the tiles on the board and the clr linked to them
	public Map<String, Integer> hexDict = new HashMap<>();
	
    /**
     * Construct a game with given sizexsize and an empty game board
     * @param sz the square size of the board
     */
    public GameModel(int sz) {
    	gameEdge = sz;
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
    	        if (row > 0 && col > 0) {
    	        	String diagPos = (row - 1) + "," + (col - 1);
    	        	gameBoard.addEdge(position, diagPos);
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
    	// if the hex selected doesn't have a color already then you can play
    	String checkHex = row + "," + col;
        if (hexDict.get(checkHex) == 0) {
        	return true;
        }
        // otherwise it is an invalid play
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
    	// create a string with the selected hex's name
    	String setHex = row + "," + col;
    	// decide which value to set the hex to based on its color
    	int newClr = 1;
    	if (clr == WHITE) {newClr = -1;}
        hexDict.put(setHex, newClr);
        
        // TODO: check if a white island spans the entire height or a black island spans the width
        return false;
    }

    /**
     * Return the score for white
     * @return white score
     */
    public int whiteScore() {
    	// list of all checked white hexes
    	ArrayList<String> scoreList = new ArrayList<String>();
    	// track the score
    	int score = 0;
    	// check if the current hex is part of a counted island
    	boolean inIsland;
    	
    	// run for each hex
    	for (int row = 0; row < gameEdge; row++) {
    		for (int col = 0; col < gameEdge; col++) {
    			inIsland = false;
    			// only continue if the hex is white
    			if (hexDict.get(row + "," + col) == -1) {
    				// check all the connected hexes
    				for (DefaultEdge otherHex : gameBoard.edgesOf(row + "," + col)) {
    					// if one of the connected hexes has already been counted, this is part of the same island
    					if (scoreList.contains(otherHex.toString().substring(1, 4)) || scoreList.contains(otherHex.toString().substring(7, 10))) {
    						inIsland = true; // it is part of an island, don't count it
    					}
    					// check everything is running correctly
    					//System.out.println(scoreList);
    					//System.out.println(otherHex);
    					//System.out.println(otherHex.toString().substring(1, 4));
    					//System.out.println(otherHex.toString().substring(7, 10));
    				}
    				// if the hex isn't part of an existing island, count it
    				if (inIsland == false) {
    					score++;
	    				// check everything is being counted correctly
	    				//System.out.println(scoreList);
	    				
    				}
    				// after the white hex has been check, add it to the list of counted hexes
    				scoreList.add(row + "," + col);
    				//System.out.println("White: " + scoreList); // checking
    				
    				// check the island didn't get counted twice
    				score += checkDoubleCount(row, col, -1);
    			}
        	}
    	}
        return score;
    }

    /**
     * return the score for black
     * @return black score
     */
    public int blackScore() {
    	/*
    	 * copy pasted the code from whiteScore and change all the white references to black
    	 */
    	// list of all checked black hexes
    	ArrayList<String> scoreList = new ArrayList<String>();
    	// track the score
    	int score = 0;
    	// check if the current hex is part of a counted island
    	boolean inIsland;
    	
    	// run for each hex
    	for (int row = 0; row < gameEdge; row++) {
    		for (int col = 0; col < gameEdge; col++) {
    			inIsland = false;
    			// only continue if the hex is black
    			if (hexDict.get(row + "," + col) == 1) {
    				// check all the connected hexes
    				for (DefaultEdge otherHex : gameBoard.edgesOf(row + "," + col)) {
    					// if one of the connected hexes has already been counted, this is part of the same island
    					if (scoreList.contains(otherHex.toString().substring(1, 4)) || scoreList.contains(otherHex.toString().substring(7, 10))) {
    						inIsland = true; // it is part of an island, don't count it
    					}
    					// check everything is running correctly
    					//System.out.println(scoreList);
    					//System.out.println(otherHex);
    					//System.out.println(otherHex.toString().substring(1, 4));
    					//System.out.println(otherHex.toString().substring(7, 10));
    				}
    				// if the hex isn't part of an existing island, count it
    				if (inIsland == false) {
    					score++;
	    				// check everything is being counted correctly
	    				//System.out.println(scoreList);
    				}
    				// after the black hex has been check, add it to the list of counted hexes
    				scoreList.add(row + "," + col);
    				//System.out.println("Black: " + scoreList); // checking
    				
    				// check the island didn't get counted twice
    				score += checkDoubleCount(row, col, 1);
    			}
        	}
    	}
        return score;
    }
    
    /**
	 * based on the way the score is counted there there is an arrangement
	 * of three tiles that can result in an island being counted twice:
	 * #1 anyHex					Hexes #1 and #3 are counted before
	 * #2 upper right of Hex #1		the checker realizes they are connected
	 * #3 Hex above Hex #2			by Hex #2
	 * 
	 * This function subtracts a point from the score (returns -1) if that
	 * is the case
	 */
    public int checkDoubleCount(int row, int col, int clr) {
    	if (row == 0 || col == 0) {
    		return 0;
    	}
    	/*
    	 *  if the islands to the left and top of the hex are the same
    	 *  color as this hex but the top-left is not, then we are in
    	 *  the problem case
    	 */
    	if (hexDict.get((row - 1) + "," + col) == clr && hexDict.get(row + "," + (col - 1)) == clr &&  hexDict.get((row - 1) + "," + (col - 1)) != clr) {
    		// return -1 so one point is taken away from the score
    		System.out.println("subtracting " + clr + " at " + row + "," + col);
    		return -1;
    	}
    	return 0;
    }
}
