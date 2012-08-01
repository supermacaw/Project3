package src;

import java.util.*;

public class Tray {
	
	protected int lengthOfTray;
	protected int widthOfTray;
    protected Block [][] config;
    protected Block [][] goalConfig;
    //protected ArrayList<Block> blocksOnTray = new ArrayList<Block>();
    protected HashSet<Block> blocksOnTray = new HashSet<Block>();
	protected ArrayList<Block> goalBlocks;
	
	public Tray(int rows, int cols) {
		this.lengthOfTray = rows;
		this.widthOfTray = cols;
        this.config = new Block[rows][cols];
	}
	
	public void place (Block blockToAdd, int row, int col) {
		blockToAdd.upLCrow = row;
		blockToAdd.upLCcol = col;
		blocksOnTray.add(blockToAdd);
		for (int i = row; i < row+blockToAdd.length; i++){
			for(int j = col; i < col+blockToAdd.width; j++){
				if(this.config[i][j] != null){
					throw new IllegalArgumentException("Conflict in tray initialization, already occupied position at (r,c) = (" + row + "," + col + ")");
				}
				this.config[i][j] = blockToAdd;
			}
		}
	}
	
	public void move (Block blockToMove, int row, int col) {
		blocksOnTray.remove(blockToMove);
		for (int m = blockToMove.upLCrow; m < blockToMove.upLCrow+blockToMove.length; m++){
			for(int n = blockToMove.upLCcol; n < blockToMove.upLCcol+blockToMove.width; n++){
				this.config[m][n] = null;
			}
		}
		blockToMove.upLCrow = row;
		blockToMove.upLCcol = col;
		for (int i = row; i < row+blockToMove.length; i++){
			for(int j = col; i < col+blockToMove.width; j++){
				if(this.config[i][j] != null){
					throw new IllegalArgumentException("Conflict in tray reconfiguration, already occupied position at (r,c) = (" + row + "," + col + ")");
				}
				this.config[i][j] = blockToMove;
			}
		}
		blocksOnTray.add(blockToMove);
	}
	public void putGoalConfig(Block blockToAdd){
		goalBlocks.add(blockToAdd);
	}
	
	//check last moved block?... i feel like there's a better way of doing this
	public boolean isAtGoal(){
		for(int i = 0; i<goalBlocks.size(); i++){
			if(!blocksOnTray.contains(goalBlocks.get(i))){//may have to check this contains method.. not sure if == or equals
				return false;
			}
		}
		return true;
	}
	
	private boolean isOK() {
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

