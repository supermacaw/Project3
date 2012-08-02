package src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ArrayList;
import java.awt.Point;
import java.util.*;

//check hashcodes later and equals methods

public class Solver {
	private Tray tray;
	private ArrayList<Block> goalBlocks; // need to add some stuff for this in solver
	HashSet <Tray> seenConfigs;

	public Solver (int tRow, int tColumn) {
		tray = new Tray(tRow, tColumn);
		goalBlocks = new ArrayList<Block>();
		seenConfigs = new HashSet<Tray>();
	}

	public Tray getTray() {
		return tray;
	}

	private ArrayList<Block> emptyCoordsAdjBlocksHelper(int row, int col){
		ArrayList<Block> result = new ArrayList<Block>();
		helperHelper(result, row-1, col, 0);//up, down, left, right
		helperHelper(result, row + 1, col, 1);//GOTTA CHECK IF THEY"RE OUTTA BOUNDSSSS, maybe check this funky coordinate business
		helperHelper(result, row, col-1, 2);    	//coord system weird, top down
		helperHelper(result, row, col+1, 3);
		return result;
	}

	private void helperHelper(ArrayList<Block>result, int i, int j, int dir){ // don't think i need to check for same block, b/c not possible?
		if(i>tray.lengthOfTray-1 || i < 0 || j > tray.widthOfTray-1 || j < 0){
			return;
		}
		else if(tray.config[i][j]!=null){
			tray.config[i][j].directions[dir] = true;
			result.add(tray.config[i][j]);
		}
	}

	public ArrayList<Block> emptyCoordsAdjBlocks(Tray myTray){
		ArrayList<Block> result = new ArrayList<Block>();
		for(int row = 0; row < myTray.lengthOfTray; row++){
			for(int col = 0; col < myTray.widthOfTray; col++){
				if(myTray.config[row][col]==null){
					System.out.println("empty at "+ row + " " + col);
					result.addAll(this.emptyCoordsAdjBlocksHelper(row, col));
				}
			}
		}
		return result;
	}

	public void solve(){ //should this go in tray?
		this.solveHelper(tray, new ArrayList<String>());   
	}
	private void solveHelper(Tray myTray, ArrayList<String> moves){ //redundancy in moving in one direction, multiple empty spaces
		if(myTray.isAtGoal(goalBlocks)){
			for(int i = 0; i < moves.size(); i++){
				System.out.println(moves.get(i));
			}
			System.exit(1);
			return;
		}
		seenConfigs.add(myTray);
		ArrayList<Block> adjToEmpty = this.emptyCoordsAdjBlocks(myTray);
		for(Block value: adjToEmpty){
			System.out.println("block " + value.upLCrow + " "+ value.upLCcol + " " + value.length + " " + value.width);
			if(value.directions[0]){ 
				Tray one = new Tray(myTray);
				if(one.move(value, value.upLCrow + 1, value.upLCcol)){
					if(!seenConfigs.contains(one)){
						moves.add(value.length + " " + value.width + " " + value.upLCrow + " " + value.upLCcol);
						solveHelper(one, new ArrayList<String>(moves));
					}
				}
			}
			if(value.directions[1]){ 
				Tray two = new Tray(myTray);
				if(two.move(value, value.upLCrow - 1, value.upLCcol)){
					if(!seenConfigs.contains(two)){
						moves.add(value.length + " " + value.width + " " + value.upLCrow + " " + value.upLCcol);
						solveHelper(two, new ArrayList<String>(moves));
					}
				}
			}
			if(value.directions[2]){ 
				Tray three = new Tray(myTray);
				if(three.move(value, value.upLCrow, value.upLCcol + 1)){
					if(!seenConfigs.contains(three)){
						moves.add(value.length + " " + value.width + " " + value.upLCrow + " " + value.upLCcol);
						solveHelper(three, new ArrayList<String>(moves));
					}
				}
			}
			if(value.directions[3]){ 
				Tray four = new Tray(myTray);
				if(four.move(value, value.upLCrow, value.upLCcol - 1)){
					if(!seenConfigs.contains(four)){
						moves.add(value.length + " " + value.width + " " + value.upLCrow + " " + value.upLCcol);
						solveHelper(four, new ArrayList<String>(moves));
					}
				}
			}
		}
	}

	public void addToGoalBlocks(Block blockToAdd, int row, int col){//do we need this?
		blockToAdd.upLCrow = row;
		blockToAdd.upLCcol = col;
		this.goalBlocks.add(blockToAdd);
	}

	public static void main(String[] args) {
		Solver s = null;
		check(args);
		FileItr configRdr = new FileItr(args[args.length - 2]);
		FileItr goalRdr = new FileItr(args[args.length - 1]);
		while (configRdr.hasNext()) {
			int[] param = parseInt(configRdr.next().split(" "));
			if (configRdr.lineNumber() == 1) {
				s = new Solver(param[0], param[1]);
			} else {
				Block newBlock = new Block(param[0], param[1]);
				s.getTray().place(newBlock, param[2], param[3]);
			}
		}
		while (goalRdr.hasNext()) {
			int[] param = parseInt(goalRdr.next().split(" "));
			s.addToGoalBlocks(new Block(param[0], param[1]), param[2], param[3]);
		}
		s.solve();
	}

	private static void check(String[] args) {
		if (args.length < 2) {
			throw new IllegalArgumentException("Not enough arguments");
		}
		for (int i = 0 ; i < args.length - 2 ; i ++) {
			if (!args[i].startsWith("-")) {
				throw new IllegalArgumentException("Option in wrong format");
			}
		}
	}

	private static int[] parseInt(String[] args) {
		int[] toRtn = new int[args.length];
		for (int i = 0; i < args.length ; i++) {
			toRtn[i] = Integer.parseInt(args[i]);
		}
		return toRtn;
	}
}
