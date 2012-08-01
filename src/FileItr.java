package src;

import java.util.Iterator;
import java.util.Scanner;
import java.io.File;

class FileItr implements Iterator{
    Scanner itr;
    boolean flag;

    public FileItr (String fileName) {
        File toRead = new File(fileName);
        try {
            itr = new Scanner(toRead);
        } catch (Exception e) {
            e.printStackTrace();
        }
        flag = true;
        itr.useDelimiter("\\s*\n\\s*");
    }

    public boolean hasNext() {
        return flag;
    }

    public String next() {
        String toRtn = itr.next();
        if (!itr.hasNext()) {
            itr.close();
            flag = false;
        }
        return toRtn;
    }
    
    public void remove() {
        throw new UnsupportedOperationException("remove not supported");
    }
}
