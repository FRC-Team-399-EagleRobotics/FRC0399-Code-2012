/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.Utilities;

import java.util.Vector;

/**
 *
 * @author Jeremy
 */
public class StringUtils {

    public static String[] split(String input, String regex) {
        Vector nodes = new Vector();
        String separator = regex;
        System.out.println("split start...................");
        // Parse nodes into vector
        int index = input.indexOf(separator);
        while (index >= 0) {
            nodes.addElement(input.substring(0, index));
            input = input.substring(index + separator.length());
            index = input.indexOf(separator);
        }
        // Get the last node
        nodes.addElement(input);

        // Create splitted string array
        String[] result = new String[nodes.size()];
        if (nodes.size() > 0) {
            for (int loop = 0; loop < nodes.size(); loop++) {
                result[loop] = (String) nodes.elementAt(loop);
                System.out.println(result[loop]);
            }

        }

        return result;
    }
    
    public static double toDouble(String in) {
        return Double.parseDouble(in);
    }
}
