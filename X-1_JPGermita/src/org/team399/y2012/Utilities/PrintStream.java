/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.Utilities;

/**
 * Standard print stream utility class. Contains various functions to modify the print stream
 * for easy debugging or readability
 * @author Jeremy
 */
public class PrintStream {

    String prefix;
    String suffix = " ";
    
    

    /**
     * Constructor
     * @param prefix The string of text to append to the beginning of the stream
     */
    public PrintStream(String prefix) {
        this.prefix = prefix;
    }
    
    

    /**
     * Constructor
     * @param prefix The string of text to append to the beginning of the stream
     */
    public PrintStream(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /**
     * Prints data with suffix and prefix
     * @param data 
     */
    public void println(String data) {
        System.out.println(prefix + data + suffix);
    }
    
    public void print(String data)  {
        System.out.print(data);
    }
}
