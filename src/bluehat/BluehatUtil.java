/*
 Assignment: TME3
 Assignment Name: Blue Hat
 Assignement Date: June 1st, 2015

 @ author: Andrew Miller
 Student ID: 2433560
 Course: COMP 486

 This is a utility class for the BlueHat game.
 The following code was found on developer.nokia.com.

 */
package bluehat;

import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Andrew Miller - 2433560
 */
public class BluehatUtil {

    static public int drawMultilineString(Graphics g, Font font, String str, int x, int y, int anchor, int width) {
        /*
         Name: drawMulilineString
         Description: outputs a formatted string for the phones screen 
         Inputs: graphics, font, string, int, int, int
         Output: int
         Called by Whom: showContractScreen, showSuccessScreen, showFailureScreen
         Calls: wrap
         */

        g.setFont(font);

        Vector lines = wrap(str, font, width);
        for (int i = 0; i < lines.size(); i++) {
            int liney = y + (i * font.getHeight());
            g.drawString((String) lines.elementAt(i), x, liney, anchor);
        }
        return y + (lines.size() * font.getHeight());
    }

    static Vector wrap(String text, Font font, int width) {
        Vector result = new Vector();
        if (text == null) {
            return result;
        }

        boolean hasMore = true;

        // The current index of the cursor
        int current = 0;

        // The next line break index
        int lineBreak = -1;

        // The space after line break
        int nextSpace = -1;

        while (hasMore) {
            //Find the line break
            while (true) {
                lineBreak = nextSpace;
                if (lineBreak == text.length() - 1) {
                    // We have reached the last line
                    hasMore = false;
                    break;
                } else {
                    nextSpace = text.indexOf(' ', lineBreak + 1);
                    if (nextSpace == -1) {
                        nextSpace = text.length() - 1;
                    }
                    int linewidth = font.substringWidth(text, current, nextSpace - current);
                    // If too long, break out of the find loop
                    if (linewidth > width) {
                        break;
                    }
                }
            }
            String line = text.substring(current, lineBreak + 1);
            result.addElement(line);
            current = lineBreak + 1;
        }
        return result;
    }
}
