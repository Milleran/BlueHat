/*
 Assignment: TME3
 Assignment Name: Blue Hat
 Assignement Date: June 1st, 2015

 @ author: Andrew Miller
 Student ID: 2433560
 Course: COMP 486

 * This is an object holds the agents movement in the x and y attributes.
 *
 * @version 1.0


 */
package bluehat;

/**
 *
 * @author MillerAn
 */
public class AgentMovement {
    private int x_pos;
    private int y_pos;
    
    
    AgentMovement(int x, int y){
        x_pos = x;
        y_pos = y;
    }

    /**
     * @return the x_pos
     */
    public int getX_pos() {
        return x_pos;
    }

    /**
     * @param x_pos the x_pos to set
     */
    public void setX_pos(int x_pos) {
        this.x_pos = x_pos;
    }

    /**
     * @return the y_pos
     */
    public int getY_pos() {
        return y_pos;
    }

    /**
     * @param y_pos the y_pos to set
     */
    public void setY_pos(int y_pos) {
        this.y_pos = y_pos;
    }
    
}
