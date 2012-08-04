package src;

public class Block{
	int length;
	int width;
	int upLCrow;
	int upLCcol;
    boolean movable;
    boolean[] directions;
    boolean priority;
	
    public Block (int length, int width) {
		if (length < 0 || width < 0) {
			throw new IllegalArgumentException("Length and width must be greater than 0");
		}
		this.length = length;
        this.width = width;
        this.priority = false;
        directions = new boolean[4];
        for(boolean i: directions){ // just to make sure it's false
        	i = false;
        }
    }
    
    public Block(Block otherBlock){
    	this.length = otherBlock.length;
    	this.width = otherBlock.width;
    	this.upLCrow = otherBlock.upLCrow;
    	this.upLCcol = otherBlock.upLCcol;
        this.priority = otherBlock.isPriority();
    	this.directions = new boolean[4];
    	//others?
    }

    public void setPriority() {
        priority = true;
    }
    
    public int hashCode() {
    	int dim = length + width * 10;
    	int coord = upLCrow + upLCcol*10;
    	return dim ^ coord;
    }
      
    public boolean equals(Object other){
    	Block otherB = null;
    	try{
    		otherB = (Block) other;
    	}catch (Exception e){
    		return false;
    	}
    	return (length == otherB.length 
    			&& width == otherB.width
    			&& upLCrow == otherB.upLCrow
    			&& upLCcol == otherB.upLCcol);
    }

    public boolean isPriority() {
        return priority;
    }

    public void setMovable() {
        movable = true;
    }

    public void setUnmovable() {
        movable = false;
    }
    
    public String toString(){
    	return "Block l: " + length 
    			+ " w: " + width 
    			+ " coords: " + upLCrow + ", " + upLCcol + " "
    			+ directions[0] + directions[1] + directions[2] + directions[3];
    }
}
