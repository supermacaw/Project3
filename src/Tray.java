package src;

import java.util.*;
import java.awt.Point;

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
		this.lengthOfTray = otherTray.lengthOfTray;
		this.widthOfTray = otherTray.widthOfTray;
        this.config = new Block[otherTray.lengthOfTray][otherTray.widthOfTray];
        Iterator<Block> i = otherTray.blocksOnTray.iterator();
		while (i.hasNext()) {
			Block temp = new Block(i.next());
			this.place(temp, temp.upLCrow, temp.upLCcol);
		}
	}
	
	public void place (Block blockToAdd, int row, int col) {
		blockToAdd.upLCrow = row;
		blockToAdd.upLCcol = col;
		blocksOnTray.add(blockToAdd);
		for (int i = row; i < row + blockToAdd.length; i++){
			for(int j = col; j < col + blockToAdd.width; j++){
				if(this.config[i][j] != null){
					throw new IllegalArgumentException("Conflict in tray initialization, already occupied position at (r,c) = (" + row + "," + col + ")");
				}
				this.config[i][j] = blockToAdd;
			}
		}
	}

    public void remove (Block toRemove) {
        int row = toRemove.upLCrow;
        int col = toRemove.upLCcol;
        for (int i = row ; i < row + toRemove.length ; i++) {
            for (int j = col ; j < col + toRemove.width ; j++) {
                config[i][j] = null;
            }
        }
    }
	
	/**
	 * Moves a given block from its given position on the board to a new position if possible.
	 * 
	 * @param blockToMove
	 * 			the block that will be moved
	 * @param row
	 * 			the end row the upper left corner of the block would be moved to
	 * @param col
	 * 			the end col the upper left corner of the block would be moved to
	 */
	public void move (Block blockToMove, int row, int col) { // can make this more efficient
		this.remove(blockToMove);
		blocksOnTray.remove(blockToMove);
		this.place(blockToMove, row, col);
		blockToMove.upLCrow = row;
		blockToMove.upLCcol = col;
		blocksOnTray.add(blockToMove); //check hashCode?
	}

    /**
     * Convenience method to check if a block is movable.
     */
    private boolean checkMovable (Block toCheck) {
        int row = toCheck.upLCrow;
        int col = toCheck.upLCcol;
        int length = toCheck.length;
        int width = toCheck.width;
        boolean up, down, left, right;
        up = down = left = right = false;
        if (inBounds(row - 1, col)) {
            boolean upT = true;
            for (int c = col ; c < col + width ; c++) {
                upT = upT && (config[row - 1][c] == null);
            }
            up = upT;
        }
        if (inBounds(row + length, col)) {
            boolean downT = true;
            for (int c = col ; c < col + width ; c++) {
                downT = downT && (config[row + length][c] == null);
            }
            down = downT;
        }
        if (inBounds(row, col - 1)) {
            boolean leftT = true;
            for (int r = row ; r < row + length ; r++) {
                leftT = leftT && (config[r][col - 1] == null);
            }
            left = leftT;
        }
        if (inBounds(row, col + width)) {
            boolean rightT = true;
            for (int r = row ; r < row + length ; r++) {
                rightT = rightT && (config[r][col + width] == null);
            }
            right = rightT;
        }
        return up || down || left || right;
    }
    
    /**
	  * Essentially a helper method for move, which is first called on by the solver for validity of a move on a block before the block is moved.
	  * 
	  * @param blockToMove
	  * 		the block that is being attempted to move
	  * @param endRow
	  * 		the end row the upper left corner of the block would be moved to
	  * @param endCol
	  * 		the end col the upper left corner of the block would be moved to
	  * @return
	  */
	public boolean isValidMove (Block blockToMove, int endRow, int endCol) {
		if ((endRow < 0 || endCol < 0) || (endRow + blockToMove.length > lengthOfTray || endCol + blockToMove.width > widthOfTray)) {
			return false;
		}
		if (blockToMove.upLCrow != endRow && blockToMove.upLCcol != endCol) { //cannot move diagonally
			return false;
		}
		int dir = 0;
		if (blockToMove.upLCrow - endRow > 0) {
			dir = 1; //up
		}
		if (blockToMove.upLCrow - endRow < 0) {
			dir = 2; //down
		}
		if (blockToMove.upLCcol - endCol > 0) {
			dir = 3; //left
		}
		if (blockToMove.upLCcol - endCol < 0) {
			dir = 4; //right
		}
		switch (dir){
		case 1: //up: make sure width of blockToMove is never blocked along the path
			for(int i = blockToMove.upLCrow-1; i >= endRow; i--){
				for (int j = blockToMove.upLCcol; j < blockToMove.upLCcol + blockToMove.width; j++){
					if(this.config[i][j] != null){
						return false;
					}
				}
			}
			
		case 2: //down: make sure width of blockToMove is never blocked along the path
			for(int i = blockToMove.upLCrow+blockToMove.length; i <= endRow + blockToMove.length-1 ; i++){
				for (int j = blockToMove.upLCcol; j < blockToMove.upLCcol + blockToMove.width; j++){
					if(this.config[i][j] != null){
						return false;
					}
				}
			}
			
		case 3: //left: make sure width of blockToMove is never blocked along the path
			for (int i = blockToMove.upLCrow; i < blockToMove.upLCrow + blockToMove.length; i++){
				for(int j = blockToMove.upLCcol-1; j >= endCol; j--){
					if(this.config[i][j] != null){
						return false;
					}
				}
			}
			
		case 4: //right: make sure width of blockToMove is never blocked along the path
			for (int i = blockToMove.upLCrow; i < blockToMove.upLCrow + blockToMove.length; i++){
				for(int j = blockToMove.upLCcol+blockToMove.width; j <= endCol + blockToMove.width-1; j++){
					if(this.config[i][j] != null){
						return false;
					}
				}
			}
		}
		return true;
	}

    /**
     * Convenience method to check if a coor is
     * in tray's bounds.
     */
    private boolean inBounds (int row, int col) {
        return row >= 0 && col >= 0 
                && row < lengthOfTray
                && col < widthOfTray;
    }

    /* This is just a temporary hack for main() */
    public HashSet<Block> getBlocks() {
        return blocksOnTray;
    }
	
	
	
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
	
	public boolean equals(Object other){ //can consolidate this
		Tray otherTray;
		try{
			otherTray = (Tray) other;
		}
		catch (Exception e){
			return false;
		}
		for(int i = 0; i < this.lengthOfTray; i++){
			for(int j = 0; j < this.widthOfTray; j++){
				if(otherTray.config[i][j]!=null && this.config[i][j]!=null){
					return otherTray.config[i][j].equals(this.config[i][j]);
				}else if(otherTray.config[i][j] == null ^ this.config[i][j]==null){
					return false;
				}
			}
		}
		return true;
	}
	
	public static void main(String[] args){
		/*Tray one = new Tray(4,3);
		Tray two = new Tray(4,3);
		Block mine = new Block(2,1);
		one.place(mine, 0, 2);
		two.place(new Block(2,1), 0, 2);
		//System.out.println(one.equals(two));
		one.move(mine, 0, 0);
		System.out.println(mine.upLCrow + " " + mine.upLCcol);*/
		/*ArrayList <Point> one = new ArrayList<Point>();
		Point ho = new Point(3,1);
		one.add(ho);
		ArrayList <Point>two = new ArrayList<Point>(one);
		one.get(0).x = 10000;
		for(int i = 0; i< one.size(); i++){
			System.out.println(one.get(i));
		}
		for(int j = 0; j < two.size(); j++){
			System.out.println(two.get(j));
		}*/
		
	}
	
	
}

