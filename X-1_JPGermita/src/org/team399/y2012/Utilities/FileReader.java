/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.Utilities;

import com.sun.squawk.microedition.io.FileConnection;
import java.io.DataInputStream;
import javax.microedition.io.Connector;

/**
 * Class used to provide functionality for reading files
 * @author Jeremy
 */
public class FileReader {

    private String m_filename;
    private String[] m_lines = {"foo", "bar"};
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

        DataInputStream theFile = null;
        FileConnection fc = null;

        try {
            fc = (FileConnection) Connector.open(m_filename, Connector.READ);
            theFile = fc.openDataInputStream();
        } catch (Exception ex) {
            System.out.println("failed to open data input stream");
        }
        if(theFile == null) {
            System.out.println("File DataInputStream null!");
        }

        String file = null;
        if (fc != null) {
            System.out.println("File Reading now!");
            try {
                file = theFile.readUTF();

            } catch (Exception ex) {
                System.out.println("null pointer on file read " + m_filename);
            }
            try {
                fc.close();
            } catch (Exception ex) {
                System.out.println("failed to close file " + m_filename);
            }
//        Reader in = new InputStreamReader(getClass().getResourceAsStream(m_filename));
//        StringBuffer temp = new StringBuffer(2048);
//        char[] buffer = new char[2048];
//        int read;
//        try {
//            while ((read = in.read(buffer, 0, buffer.length)) != -1) {
//                temp.append(buffer, 0, read);
//            }
//        } catch (Exception e) {
//            System.out.println("WARNING: Error reading configuration file! ");
//        }

            m_lines = StringUtils.split(file, System.getProperty("line.separator"));
        }
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
