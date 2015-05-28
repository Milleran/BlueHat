/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluehat;

/**
 *
 * @author MillerAn
 */
public class PathTile {

    private int position_x;

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
    private int position_y;
    private int intFValue;
    private int intGValue;
    private int intHValue;
    private int intTileHeight;
    private int intTileWidth;

    public PathTile() {

    }

    public PathTile(int position_x, int position_y, int intTileHeight, int intTileWidth) {
        this.position_x = position_x;
        this.position_y = position_y;
        this.intTileHeight = intTileHeight;
        this.intTileWidth = intTileWidth;
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

}
