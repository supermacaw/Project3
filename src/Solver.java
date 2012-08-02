package src;

import java.util.Iterator;
import java.util.ArrayList;
import java.awt.Point;
import java.util.*;

public class Solver {
    private Tray tray;
    private ArrayList<Block> goalBlocks;

    public Solver (int tRow, int tColumn) {
        tray = new Tray(tRow, tColumn);
        goalBlocks = new ArrayList<Block>();
    }

    public Tray getTray() {
        return tray;
    }
    
    private ArrayList<Block> emptyCoordsAdjBlocksHelper(int x, int y){
    	ArrayList<Block> result = new ArrayList<Block>();
    	helperHelper(result, x, y+1, 1);
    	helperHelper(result, x, y-1, 2);//GOTTA CHECK IF THEY"RE OUTTA BOUNDSSSS, maybe check this funky coordinate business
    	helperHelper(result, x-1, y, 3);    	
    	helperHelper(result, x+1, y, 4);
    	return result;
    }
    
    private void helperHelper(ArrayList<Block>result, int i, int j, int dir){ // don't think i need to check for same block, b/c not possible?
    	if(i>tray.widthOfTray || i < 0 || j > tray.lengthOfTray || j < 0){
    		return;
    	}
    	else if(tray.config[i][j]!=null){
    		tray.config[i][j].direction = dir;
    		result.add(tray.config[i][j]);
    	}
    }
    
    public HashMap<Point, ArrayList<Block>> emptyCoordsAdjBlocks(){
    	HashMap<Point, ArrayList<Block>> emptySpacesAdjBlocks = new HashMap<Point, ArrayList<Block>>();
    	for(int i = 0; i < tray.widthOfTray; i++){
    		for(int j = 0; j < tray.lengthOfTray; j++){
    			if(tray.config[i][j]==null){
    				emptySpacesAdjBlocks.put(new Point(i, j), this.emptyCoordsAdjBlocksHelper(i,j));
    			}
    		}
    	}
    	return emptySpacesAdjBlocks;
    }
    
    public void solve(){ //should this go in tray?
    	HashMap<Point, ArrayList<Block>> adjToEmpty = this.emptyCoordsAdjBlocks();
    	for (Map.Entry<Point, ArrayList<Block>> entry : adjToEmpty.entrySet()) {
    		Point key = entry.getKey();
    		ArrayList<Block> adjBlocks = entry.getValue();
    		for(Block value: adjBlocks){
    			switch(value.direction){
    			case 1: 
    				(new Tray(tray)).move(value, value.upLCrow - 1, value.upLCcol);
    			case 2:
    				(new Tray(tray)).move(value, value.upLCrow + 1, value.upLCcol);
    			case 3:
    				(new Tray(tray)).move(value, value.upLCrow, value.upLCcol + 1);
    			case 4:
    				(new Tray(tray)).move(value, value.upLCrow, value.upLCcol - 1);
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
            int[] param = parseInt(goalRdr.next().split(" ")); // should update this for multiple goal blocks
            s.addToGoalBlocks(new Block(param[0], param[1]), param[2], param[3]);
        }
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
