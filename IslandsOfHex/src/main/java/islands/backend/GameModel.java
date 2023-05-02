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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public int move = 0;
    
    // create the graph that that game is played on
	public Graph<String, DefaultEdge> gameBoard = new SimpleGraph<>(DefaultEdge.class);
	// create a dictionary that stores the tiles on the board and the clr linked to them
	public Map<String, Integer> hexDict = new HashMap<>();
	
	// create global lists for later
	ArrayList<String> counted;
	ArrayList<String> toCount;
	
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
        
        // throw error
        if(row >= gameEdge || row < 0 || col >= gameEdge || col < 0) {
        	throw new IllegalArgumentException();
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
        this.move += 1;
        // throw error
        if(row >= gameEdge || row < 0 || col >= gameEdge || col < 0) {
        	throw new IllegalArgumentException();
        }
        
        
        // check to see if every piece on the board is full
        if (this.move == this.gameEdge * this.gameEdge) {
        	return true;
        }
        // TODO: check if a white island spans the entire height or a black island spans the width
        // excluded from my project due to time constraints
        return (whiteSpan() || blackSpan());
    }
    
    /**
     * helper method that checks if white has met the end game condition
     */
    public boolean whiteSpan() {
        return false;
    }
    
    /**
     * helper method that checks if black has met the end game condition
     */
    public boolean blackSpan() {
        return false;
    }

    /**
     * Return the score for white
     * @return white score
     */
    public int whiteScore() {
    	// list of all checked hexes
    	counted = new ArrayList<String>();
    	
    	// list of hexes that still need to be checked
    	toCount = new ArrayList<String>();
    	
    	// track the score
    	int score = 0;
    	
    	// run for each hex
    	for (int row = 0; row < gameEdge; row++) {
    		for (int col = 0; col < gameEdge; col++) {
    			// define our current hex
    			String currentHex = row + "," + col;
    			// if we haven't check this hex yet and it is the correct color
    			if (counted.contains(currentHex) == false && hexDict.get(currentHex) == -1) {
    				// we haven't checked this hex of the correct color yet so add score
    				score++;
    				// add this hex and any hexes of the same color it is touching to the counted list
    				toCount.add(currentHex);
    				helpCount(currentHex, -1);
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
    	// same as whiteScore with -1 values replaced by 1
    	// list of all checked hexes
    	counted = new ArrayList<String>();
    	
    	// list of hexes that still need to be checked
    	toCount = new ArrayList<String>();
    	
    	// track the score
    	int score = 0;
    	
    	// run for each hex
    	for (int row = 0; row < gameEdge; row++) {
    		for (int col = 0; col < gameEdge; col++) {
    			// define our current hex
    			String currentHex = row + "," + col;
    			// if we haven't check this hex yet and it is the correct color
    			if (counted.contains(currentHex) == false && hexDict.get(currentHex) == 1) {
    				// we haven't checked this hex of the correct color yet so add score
    				score++;
    				// add this hex and any hexes of the same color it is touching to the counted list
    				toCount.add(currentHex);
    				helpCount(currentHex, 1);
    			}
    		}
    	}
        return score;
    }
    
    /**
     * helper function for the score that check if a hex is in an island
     */
    public void helpCount(String currentHex, int color) {
		toCount.remove(0); // this is our new currentHex

    	// check to make sure we didn't accidentally check a hex twice
    	
    	// we have counted the current hex
    	counted.add(currentHex);
    	
    	// check the connected hexes
    	for (DefaultEdge connectedHexes : gameBoard.edgesOf(currentHex)) {
    		// if one of the connected hexes has the same color we need to count it too with breadth first sort
    		
    		// I need to define a pattern so the code can extract the position of the tiles in a given vertex
    		// Define the pattern
    		String pattern = "\\((\\d+,\\d+)\\s*:\\s*(\\d+,\\d+)\\)";

    		// Create a Pattern object
    		Pattern hexPattern = Pattern.compile(pattern);
    		
    		// Create a Matcher object
    		Matcher m = hexPattern.matcher(connectedHexes.toString());
    		
    		// get the first and second hex in the edge
    		m.find();
    		String hexOne = m.group(1); // #,#
    	    String hexTwo = m.group(2); // #,#
    	    
    	    // check if either one is part of the island
    	    if (hexDict.get(hexOne) == color && hexOne.equals(currentHex) == false) {
				if (counted.contains(hexOne) == false) {
					toCount.add(hexOne);
				}
			} else if (hexDict.get(hexTwo) == color && hexTwo.equals(currentHex) == false) {
				if (counted.contains(hexTwo) == false) {
					toCount.add(hexTwo);
				}
			}
    	}
    	// if there are more hexes in this island run the same code for the next hex
    	if (toCount.isEmpty() == false) {
    		helpCount(toCount.get(0), color);
    	}
    	
    }
}
