package src;

public class Block {
	int length;
	int width;
	int upLCrow;
	int upLCcol;
    boolean movable;
    int direction;
	
    public Block (int length, int width) {
		if (length < 0 || width < 0) {
			throw new IllegalArgumentException("Length and width must be greater than 0");
		}
		this.length = length;
        this.width = width;
    }
    
    public Block(Block otherBlock){
    	this.length = otherBlock.length;
    	this.width = otherBlock.width;
    	this.upLCrow = otherBlock.upLCrow;
    	this.upLCcol = otherBlock.upLCcol;
    	//others?
    }
    
    public int hashCode() {
    	int dim = length + width * 10;
    	int coord = upLCrow + upLCcol*10;
    	return dim ^ coord;
    }
    
    public boolean equals(Object other){
    	try{
    		other = (Block) other;
    	}catch (Exception e){
    		return false;
    	}
    	return (length == other.length && width == other.width && upLCrow == other.upLCrow && upLCcol == other.upLCcol);
    }

    public void setMovable() {
        movable = true;
    }

    public void setUnmovable() {
        movable = false;
    }
}
