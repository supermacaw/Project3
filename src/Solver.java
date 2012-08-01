package src;

import java.util.Iterator;

public class Solver {
    Tray tray;

    public Solver (int tWidth, int tLength) {
        tray = new Tray(tWidth, tLength);
    }

    public static void main(String[] args) {
        check(args);
        Iterator configRdr = new FileItr(args[args.length - 2]);
        Iterator goalRdr = new FileItr(args[args.length - 1]);
        while (configRdr.hasNext()) {
            System.out.println(configRdr.next());
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
}
