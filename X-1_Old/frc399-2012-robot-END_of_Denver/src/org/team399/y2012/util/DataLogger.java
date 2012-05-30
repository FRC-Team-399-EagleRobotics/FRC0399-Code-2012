/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2012.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.microedition.io.Connector;

/**
 *
 * @author kspoelstra
 */
public class DataLogger {

    private  PrintStream out = null;

    private static DataLogger instance = null;
    
    private boolean enabled = false;

    private DataLogger(String filename)
    {
        try{
            OutputStream os = Connector.openOutputStream(filename);
            out = new PrintStream(os);
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public static DataLogger getInstance(String filename)
    {
        if(instance == null){
            instance = new DataLogger(filename);
        }
        return instance;
    }


    public void print(String s)
    {
        if(enabled) {
            out.print(s);
            out.flush();
        }
    }

    public void println(String s)
    {
        if(enabled) {
            out.println(s);
            out.flush();
        }
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
