/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.robot.Controls.Autonomous;

import org.team399.y2012.Utilities.FileReader;
import org.team399.y2012.Utilities.StringUtils;

/**
 * A class to read from the autonomous text file on the onboard flash
 * @author Jeremy
 */
public class AutonFile {

    private String m_filename;
    private String[][] parsedFile;
    FileReader fr;

    /**
     * Constructor
     * @param filename location of the autonomous text file
     */
    public AutonFile(String filename) {
        this.m_filename = filename;
        fr = new FileReader(m_filename);
        read();
    }

    /**
     * Reads the file into memory
     */
    private void read() {
        

        String NL = System.getProperty("line.separator");
        System.out.println("Read from file: ");
        String[] lines = fr.getContents();
        for(int i = 0; i < lines.length; i++) {
            System.out.println(lines[i]);
        }
        String[][] elements = new String[lines.length][];

        System.out.println("Lines Parsed: " + lines.length);
        for (int i = 0; i < lines.length; i++) {
            System.out.println(i + ": " + lines[i]);
            elements[i] = StringUtils.split(lines[i], ", ");
            System.out.println("Elements Parsed: " + elements[i].length);
        }
        parsedFile = elements;
        System.gc();
    }

    /**
     * Returns the file from memory as an array of strings
     * @return 
     */
    public String[][] getParsedFile() {
        return parsedFile;
    }

    /**
     * Reloads the file into memory
     */
    public void reload() {
        read();
    }

    /**
     * Reloads a new file into memory
     * @param filename the new filename to load
     */
    public void reload(String filename) {
        this.m_filename = filename;
        read();
    }
}
