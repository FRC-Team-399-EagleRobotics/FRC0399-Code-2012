/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.Utilities;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.microedition.io.Connector;

/**
 * Datalogger class. prints data to file
 * @author Justin S. and Jeremy G.
 */
public class DataLogger {

    private PrintStream out = null;
    private static DataLogger instance = null;
    private boolean enabled = false;

    private DataLogger(String filename) {
        try {
            OutputStream os = Connector.openOutputStream(filename);
            out = new PrintStream(os);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Returns the current instance of the datalogger
     * @param filename the location of the file to save to
     * @return The current datalogger instance
     */
    public static DataLogger getInstance(String filename) {
        if (instance == null) {
            instance = new DataLogger(filename);
        }
        return instance;
    }

    /**
     * Prints a string to file
     * @param s 
     */
    public void print(String s) {
        if (enabled) {
            out.print(s);
            out.flush();
        }
    }

    /**
     * Prints a string to file, appends with a newline char
     * @param s 
     */
    public void println(String s) {
        if (enabled) {
            out.println(s);
            out.flush();
        }
    }
}
