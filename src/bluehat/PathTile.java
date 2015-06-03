/**
 * Assignment: TME3 Assignment Name: Blue Hat Assignment Date: June 1st, 2015
 *
 * @ author: Andrew Miller Student ID: 2433560 Course: COMP 486
 *
 * The primary purpose of the PathTile class is to create a path for the NDI Agents
 * using the A* algorithm. Holds the x and y maze tiles and calculates the F, G, H
 * values required to execute the A* algorithm.
 * 
 */
package bluehat;

/**
 *
 * @author MillerAn
 */
public class PathTile {

    private int position_x;
    private int position_y;
    private int intFValue=0;
    private int intGValue=0;
    private int intHValue=0;

    public PathTile() {

    }

    public PathTile(int position_x, int position_y) {
        this.position_x = position_x;
        this.position_y = position_y;

    }

    public int getIntFValue() {
        intFValue = intGValue+intHValue;
        
        return intFValue;
    }

    public void setIntFValue(int intFValue) {
        this.intFValue = intFValue;
    }

    public int getIntGValue() {
        return intGValue;
    }

    public void setIntGValue(int intGValue) {
        this.intGValue = intGValue;
    }

    public int getIntHValue() {
        return intHValue;
    }

    public void setIntHValue(int intHValue) {
        this.intHValue = intHValue;
    }

    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + this.position_x;
        hash = 23 * hash + this.position_y;
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PathTile other = (PathTile) obj;
        if (this.position_x != other.position_x || this.position_y != other.position_y) {
            return false;
        }

        return true;
    }

    public int getPosition_x() {
        return position_x;
    }

    public void setPosition_x(int position_x) {
        this.position_x = position_x;
    }

    public int getPosition_y() {
        return position_y;
    }

    public void setPosition_y(int position_y) {
        this.position_y = position_y;
    }
    public String toString() {
        return "PathTile{" + "position_x=" + position_x + ", position_y=" + position_y + ", intFValue=" + intFValue + ", intGValue=" + intGValue + ", intHValue=" + intHValue + '}';
    }

}
