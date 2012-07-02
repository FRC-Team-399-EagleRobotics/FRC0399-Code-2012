/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.Utilities;

import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Class used to provide functionality for reading files
 * @author Jeremy
 */
public class FileReader {

    private String m_filename;
    private String[] m_lines;
    private static final char NEW_LINE = '\n';

    /**
     * Constructor
     * @param filename 
     */
    public FileReader(String filename) {
        m_filename = filename;
        read();
    }

    /**
     * CAll this method to read the contents of the file from flash into memory
     */
    private void read() {

        Reader in = new InputStreamReader(getClass().getResourceAsStream(m_filename));
        StringBuffer temp = new StringBuffer(1024);
        char[] buffer = new char[1024];
        int read;
        try {
            while ((read = in.read(buffer, 0, buffer.length)) != -1) {
                temp.append(buffer, 0, read);
            }
        } catch (Exception e) {
            System.out.println("WARNING: Error reading configuration file! ");
        }


        m_lines = StringUtils.split(temp.toString(), System.getProperty("line.separator"));

    }

    /**
     * Reload the file and any changes into memory
     */
    public void reload() {
        read();
    }

    /**
     * REturns an array of strings representing the lines of the file read from memory
     * @return 
     */
    public String[] getContents() {
        return m_lines;
    }
}
