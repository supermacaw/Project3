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
}
