package src;

public class Block {
	protected int length;
	protected int width;
	protected int upLCrow;
	protected int upLCcol;
	
    public Block (int length, int width) {
		if (length < 0 || width < 0) {
			throw new IllegalArgumentException("Length and width must be greater than 0");
		}
		this.length = length;
        this.width = width;
    }
    
    public int hashCode() {
    	int dim = length + width * 10;
    	int coord = upLCrow + upLCcol*10;
    	return dim ^ coord;
    }
    
    public boolean equals (Block other) {
    	return (length == other.length && width == other.width && upLCrow == other.upLCrow && upLCcol == other.upLCcol);
    }
}
