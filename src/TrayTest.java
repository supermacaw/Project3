package src;

import static org.junit.Assert.*;

import org.junit.Test;

public class TrayTest {

	@Test
	public void testEasyIsOK() {
		Tray testTray = new Tray(3,3);
		Block block1 = new Block(2,2);
		Block block2 = new Block(2,1);
		Block block3 = new Block(1,2);
		Block block4 = new Block(1,1);
		testTray.place(block1,0,0);
		testTray.place(block2,0,2);
		testTray.place(block3,2,0);
		testTray.place(block4,2,2);
		assertTrue(testTray.isOK());
	}
	
	public void testInput() {
		Tray testTray = new Tray(3,3);
		Block block1 = new Block(2,2);
		Block block2 = new Block(2,1);
	}

}
