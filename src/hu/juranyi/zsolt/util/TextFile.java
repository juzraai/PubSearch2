/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.juranyi.zsolt.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Zsolt
 */
public class TextFile {

    private final String fileName;
    private final List<String> lines = new ArrayList<String>();

    public TextFile(String fileName) {
        this.fileName = fileName;
        BufferedReader r = null;
        try {
            r = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = r.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException ex) {
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    public List<String> getLines() {
        return lines;
    }

    public boolean save() {
        boolean result = true;
        BufferedWriter w = null;
        try {
            w = new BufferedWriter(new FileWriter(fileName));
            for (String line : lines) {
                w.write(line);
                w.newLine();
            }
        } catch (IOException ex) {
            result = false;
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException ex) {
                }
            }
        }
        return result;
    }
}
