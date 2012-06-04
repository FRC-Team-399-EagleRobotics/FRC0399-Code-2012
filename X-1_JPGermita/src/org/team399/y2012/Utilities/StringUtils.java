/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2012.Utilities;

import java.util.Vector;

/**
 * Class of string utilities
 * @author Jeremy
 */
public class StringUtils {

    /**
     * Splits a string
     * @param input Input string to split
     * @param regex character to split at
     * @return an array of strings resulting from the split at regex
     */
    public static String[] split(String input, String regex) {
        Vector nodes = new Vector();
        String separator = regex;
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

    /**
     * Returns a string as a double
     * @param in input string to parse as a double
     * @return the parsed double
     */
    public static double toDouble(String in) {
        return Double.parseDouble(in);
    }
}
