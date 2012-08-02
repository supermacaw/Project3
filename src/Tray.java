package src;

import java.util.*;

public class Tray {
	
	 int lengthOfTray;
	 int widthOfTray;
     Block [][] config;
    // ArrayList<Block> blocksOnTray = new ArrayList<Block>();
     HashSet<Block> blocksOnTray = new HashSet<Block>();
	 
	
	public Tray(int rows, int cols) {
		this.lengthOfTray = rows;
		this.widthOfTray = cols;
        this.config = new Block[rows][cols];
	}
	
	public Tray(Tray otherTray){
		
	}
	
	public void place (Block blockToAdd, int row, int col) {
		blockToAdd.upLCrow = row;
		blockToAdd.upLCcol = col;
		blocksOnTray.add(blockToAdd);
		for (int i = row; i < row+blockToAdd.length; i++){
			for(int j = col; j < col+blockToAdd.width; j++){
				if(this.config[i][j] != null){
					throw new IllegalArgumentException("Conflict in tray initialization, already occupied position at (r,c) = (" + row + "," + col + ")");
				}
				this.config[i][j] = blockToAdd;
			}
		}
	}
	
	public boolean move (Block blockToMove, int row, int col) {
		blocksOnTray.remove(blockToMove);
		for (int m = blockToMove.upLCrow; m < blockToMove.upLCrow+blockToMove.length; m++){
			for(int n = blockToMove.upLCcol; n < blockToMove.upLCcol+blockToMove.width; n++){
				this.config[m][n] = null;
			}
		}
		for (int i = row; i < row+blockToMove.length; i++){
			for(int j = col; j < col+blockToMove.width; j++){
				if(this.config[i][j] != null){
					return false;
				}
			}
		}
		for (int i = row; i < row+blockToMove.length; i++) {
			for(int j = col; j < col+blockToMove.width; j++) {
				this.config[i][j] = blockToMove;
			}
		}
		blockToMove.upLCrow = row;
		blockToMove.upLCcol = col;
		blocksOnTray.add(blockToMove);
		return true;
	}
	
	/*public boolean moveOneStep (Block blockToMove, int currRow, int currCol, int dir) {
		switch (dir){
		case 1: //up
			for(int j = currCol; j < currCol + blockToMove.length; j++){
				if(this.config[blockToMove.upLCrow - 1][j] != null || !this.config[blockToMove.upLCrow - 1][j].equals(blockToMove)){
					return false;
				}
			}
			
		case 2: //down
			for(int j = currCol; j < currCol + blockToMove.length; j++){
				if(this.config[blockToMove.upLCrow + blockToMove.length][j] != null || !this.config[blockToMove.upLCrow + blockToMove.length][j].equals(blockToMove)){
					return false;
				}
			}
		case 3: //left
			for(int j = currRow; j < currRow + blockToMove.width; j++){
				if(this.config[blockToMove.upLCcol - 1][j] != null || !this.config[blockToMove.upLCcol - 1][j].equals(blockToMove)){
					return false;
				}
			}
		case 4: //right
			for(int j = currRow; j < currRow + blockToMove.width; j++){
				if(this.config[blockToMove.upLCrow + blockToMove.width][j] != null || !this.config[blockToMove.upLCrow + blockToMove.width][j].equals(blockToMove)){
					return false;
				}
			}
		}
	}*/
	
	//check last moved block?... i feel like there's a better way of doing this
	public boolean isAtGoal(ArrayList<Block> goalBlocks){
		for(int i = 0; i<goalBlocks.size(); i++){
			if(!blocksOnTray.contains(goalBlocks.get(i))){//may have to check this contains method.. not sure if == or equals
				return false;
			}
		}
		return true;
	}
	
	boolean isOK() {
		HashMap<Block, Integer> counts = new HashMap<Block, Integer>();
		for (int m = 0; m < this.lengthOfTray; m++) {
			for (int n = 0; n < this.widthOfTray; n++) {
				Block curBlock = this.config[m][n];
				if(curBlock!=null){
					if(counts.containsKey(curBlock)){
						counts.put(curBlock, counts.get(curBlock)+1);
					}else{
						counts.put(curBlock, 1);
					}
				}
			}
		}
		Iterator<Block> i = blocksOnTray.iterator();
		while(i.hasNext()){
			Block b = i.next();
			int numSpaces = b.length * b.width;
			if(numSpaces != counts.get(b).intValue()) {
				return false;
			}
		}
		/*for(Block b : blocksOnTray){
			int numSpaces = b.length * b.width;
			if(numSpaces != counts.get(b).intValue()){
				return false;
			}
		}*/
		return true;
	}
	
	public int hashCode(){
		int hashCode = 1;
		Iterator<Block> i = blocksOnTray.iterator();
		while (i.hasNext()) {
		      Block x = i.next();
		      hashCode = 31*hashCode + (x==null ? 0 : x.hashCode());
		}
		return hashCode;
	}
	
	public boolean equals(Tray otherTray){
		//compare length/width of tray? not needed apparently
		//do we need an equals(obj)? used to override object's and is used in hashsets
		for(int i = 0; i < this.lengthOfTray; i++){
			for(int j = 0; j < this.widthOfTray; j++){
				if(!otherTray.config[i][j].equals(this.config[i][j])){ //.equals or ==?
					return false;
				}
			}
		}
		return true;
	}
	

}

