package org.free;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class FileIO {
    // Read file line by line and store it in MTree
    public static boolean read(String path, StringMTree tree) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = null;
            while ((line = br.readLine()) != null) {
                tree.add(line);
            }
            br.close();
        } catch (FileNotFoundException excep) {
            return false;
        } catch (IOException excep) {
            // Not handling this
        }
        return true;
    }
}
