package src;

import java.util.ArrayList;
import java.util.HashSet;

//check hashcodes later and equals methods

public class Solver {
	private Tray tray;
	private ArrayList<Block> goalBlocks; // need to add some stuff for this in solver
	HashSet <Tray> seen;

	public Solver (int tRow, int tColumn) {
		tray = new Tray(tRow, tColumn);
		goalBlocks = new ArrayList<Block>();
		seen = new HashSet<Tray>();
	}

	public Tray getTray() {
		return tray;
	}
	
	private int getDir(Block blockToCheck, int row, int col, Tray myTray){
		if(row>myTray.lengthOfTray-1 || row < 0 || col > myTray.widthOfTray-1 || col < 0){
			throw new IllegalArgumentException();
		}
		else if(blockToCheck.upLCrow < row){
			return 0;
		}else if(blockToCheck.upLCrow > row){
			return 1;
		}else if(blockToCheck.upLCcol < col){
			return 2;
		}else if(blockToCheck.upLCcol > col){
			return 3;
		}else if(blockToCheck.upLCrow == row && blockToCheck.upLCcol == col){
			return -1;
		}else{
			throw new IllegalArgumentException();
		}
	}
	
	public boolean getDirWorks(){
		Tray myTray = new Tray(5,5);
		Block myBlock = new Block(2,2);
		myTray.place(myBlock, 2, 2);
		if(getDir(myBlock, 0, 0, myTray)!=1){
			return false;
		}if(getDir(myBlock, 3, 0, myTray)!=0){
			return false;
		}if(getDir(myBlock, 2, 0, myTray)!=3){
			return false;
		}if(getDir(myBlock, 2, 4, myTray)!=2){
			return false;
		}if(getDir(myBlock, 2, 2, myTray)!=(-1)){
			return false;
		}try{
			getDir(myBlock, 19, 19, myTray);
			return false;
		}catch(Exception e){
		}
		return true;
	}

	private void addAdjBlocks(ArrayList<Block>result, int i, int j, int dir, Tray myTray){ // don't think i need to check for same block, b/c not possible?
		if(i>myTray.lengthOfTray-1 || i < 0 || j > myTray.widthOfTray-1 || j < 0){
			return;
		}
		else if(myTray.config[i][j]!=null){//contains is ok b/c coords same
			myTray.config[i][j].directions[dir] = true;
			if(!result.contains(myTray.config[i][j])){
				result.add(myTray.config[i][j]);
			}
		}
	}
	
	private void emptyCoordsAdjBlocksHelper(int row, int col, Tray myTray, ArrayList<Block> result){
		addAdjBlocks(result, row-1, col, 0, myTray);//up, down, left, right
		addAdjBlocks(result, row + 1, col, 1, myTray);//GOTTA CHECK IF THEY"RE OUTTA BOUNDSSSS, maybe check this funky coordinate business
		addAdjBlocks(result, row, col-1, 2, myTray);    	//coord system weird, top down
		addAdjBlocks(result, row, col+1, 3, myTray);
	}

	public ArrayList <Block> emptyCoordsAdjBlocks(Tray myTray){
		ArrayList<Block> result = new ArrayList<Block>();//repeated block adds
		for(int row = 0; row < myTray.lengthOfTray; row++){
			for(int col = 0; col < myTray.widthOfTray; col++){
				if(myTray.config[row][col]==null){
					this.emptyCoordsAdjBlocksHelper(row, col, myTray, result);
				}
			}
		}
		return result;
	}

	private void solveHelper(Tray myTray, ArrayList<String> moves){ //redundancy in moving in one direction, multiple empty spaces
		/*System.out.println();
		for(int i = 0; i < moves.size(); i++){
			System.out.print(moves.get(i) + "-->");
		}*/
		if(myTray.isAtGoal(goalBlocks)){
			for(int i = 0; i < moves.size(); i++){
				System.out.println(moves.get(i));
			}
			System.exit(0);
			return;
		}// need to have new blocks every time
		seen.add(myTray);
		ArrayList<Block> adjToEmpty = this.emptyCoordsAdjBlocks(myTray);
		System.out.println();
		for(Block bix: adjToEmpty){
			System.out.println(bix);
		}
		for(Block value: adjToEmpty){// this is new
			//System.out.println("block " + value.upLCrow + " "+ value.upLCcol + " " + value.length + " " + value.width + " " + value.priority);
			if(value.directions[0]){  
				Tray one = new Tray(myTray);
				Block copy1 = one.config[value.upLCrow][value.upLCcol];
				if(one.isValidMove(copy1, copy1.upLCrow + 1, copy1.upLCcol)){
					one.move(copy1, copy1.upLCrow+1, copy1.upLCcol);
					if(!seen.contains(one)){
						ArrayList <String> a = new ArrayList<String>(moves);
						a.add(copy1.length + " " + copy1.width + " " + copy1.upLCrow + " " + copy1.upLCcol);
						solveHelper(one, a);
					}
				}
			}
			if(value.directions[1]){ 
				Tray two = new Tray(myTray);
				Block copy2 = two.config[value.upLCrow][value.upLCcol];
				if(two.isValidMove(copy2, copy2.upLCrow - 1, copy2.upLCcol)){
					two.move(copy2, copy2.upLCrow-1, copy2.upLCcol);
					if(!seen.contains(two)){	
						ArrayList <String> b = new ArrayList<String>(moves);
						b.add(copy2.length + " " + copy2.width + " " + copy2.upLCrow + " " + copy2.upLCcol);
						solveHelper(two, b);
					}
				}
			}
			if(value.directions[2]){ 
				Tray three = new Tray(myTray);
				Block copy3 = three.config[value.upLCrow][value.upLCcol];
				if(three.isValidMove(copy3, copy3.upLCrow, copy3.upLCcol + 1)){
					three.move(copy3, copy3.upLCrow, copy3.upLCcol + 1);
					if(!seen.contains(three)){	
						ArrayList <String> c = new ArrayList<String>(moves);
						c.add(copy3.length + " " + copy3.width + " " + copy3.upLCrow + " " + copy3.upLCcol);
						solveHelper(three, c);
					}
				}
			}
			if(value.directions[3]){ 
				Tray four = new Tray(myTray);
				Block copy4 = four.config[value.upLCrow][value.upLCcol];
				if(four.isValidMove(copy4, copy4.upLCrow, copy4.upLCcol - 1)){
					four.move(copy4, copy4.upLCrow, copy4.upLCcol - 1);
					if(!seen.contains(four)){
						ArrayList <String> d = new ArrayList<String>(moves);
						d.add(copy4.length + " " + copy4.width + " " + copy4.upLCrow + " " + copy4.upLCcol);
						solveHelper(four, d);
					}
				}
			}
		}
	}
	
	public void solve(){ //should this go in tray?
		this.solveHelper(tray, new ArrayList<String>());   
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
				try{//just to make sure that random ass trays don't mess it up, maybe modify this
					Block newBlock = new Block(param[0], param[1]);
					s.getTray().place(newBlock, param[2], param[3]);
				}catch(Exception e){
					System.out.println("Bad tray / bad block");
				}
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
