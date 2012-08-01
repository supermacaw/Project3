package src;

import java.util.Iterator;

public class Solver {
    private Tray tray;
    private Block goalBlock;
    private int goalRow;
    private int goalCol;


    public Solver (int tRow, int tColumn) {
        tray = new Tray(tRow, tColumn);
    }

    public Tray getTray() {
        return tray;
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
                Block newBlock = new Block(param[0], param[1]);
                s.getTray().place(newBlock, param[2], param[3]);
            }
        }
        if (goalRdr.hasNext()) {
            int[] param = parseInt(goalRdr.next().split(" "));
            s.goalBlock = new Block(param[0], param[1]);
            s.goalRow = param[2];
            s.goalCol = param[3];
        }
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
