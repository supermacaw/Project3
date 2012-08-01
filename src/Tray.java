package src;

import java.util.*;

public class Tray {
	
	protected int lengthOfTray;
	protected int widthOfTray;
    protected Block [][] config;
    protected ArrayList<Block> blocksOnTray = new ArrayList<Block>(); //arraylist of blocks for ease of access when generating hash code
	
	public Tray(int rows, int cols) {
		this.lengthOfTray = rows;
		this.widthOfTray = cols;
        this.config = new Block[rows][cols];
	}
	
	public void put (Block blockToAdd, int row, int col) {
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
	}
	
	public int hashCode(){
		
	}


/* This proves that boolean[][] is default to false
    public static void main(String[] args) {
        Tray test = new Tray(5, 5);
        for (boolean[] b : test.isUsed) {
            for (boolean bb : b) {
                System.out.println(bb);
            }
        }
    }*/
}
