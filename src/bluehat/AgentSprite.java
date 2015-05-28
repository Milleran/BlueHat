/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluehat;

import static java.lang.Math.abs;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;

/**
 *
 * @author Andrew
 */
public class AgentSprite extends Sprite {

    private AgentMovement direction = new AgentMovement(0, 0);

    private int ndi_level = 1;

    private int change_direction = 0;

    private int ndi_x_pos_last;

    private int ndi_y_pos_last;

    public AgentSprite(Image image) {
        super(image);
    }

    /**
     *
     * @param img
     * @param x
     * @param y
     */
    public AgentSprite(Image img, int x, int y) {
        super(img, x, y);

    }

    public AgentMovement getDirection() {
        return direction;
    }

    public void setDirection(AgentMovement direction) {
        this.direction = direction;
    }

    public int getNdi_x_pos_last() {
        return ndi_x_pos_last;
    }

    public int getNdi_y_pos_last() {
        return ndi_y_pos_last;
    }

    public void setPosition(int x, int y) {
        ndi_x_pos_last = this.getX();
        ndi_y_pos_last = this.getY();
        super.setPosition(x, y); //To change body of generated methods, choose Tools | Templates.

    }

    public int getNdi_level() {
        return ndi_level;
    }

    public void setNdi_level(int ndi_level) {
        this.ndi_level = ndi_level;
    }

    public int getChange_direction() {
        return change_direction;
    }

    public void setChange_direction(int change_direction) {
        this.change_direction = change_direction;
    }

    private Vector findPath(int currentPosition_X, int currentPosition_Y, int targetPosition_X, int targetPosition_Y, int tileUnblocked, TiledLayer tlMap) {
        //Create two vectors to hold the closed path and the open path
        //The closed vector will be the vector used to move the Sprite in the 
        //maze.
        
        Vector vecOpenPath = new Vector();
        Vector vecClosedPath = new Vector();
        int intCell_x;
        int intCell_y;

        int intG = 0;

        int intTargetCell_x;
        int intTargetCell_y;
//convert from pixels to cell location on the tiledlayer
        intCell_x = (int) Math.floor(currentPosition_X / BluehatCanvas.TILE_HEIGHT_WIDTH); 
        intCell_y = (int) Math.floor(currentPosition_Y / BluehatCanvas.TILE_HEIGHT_WIDTH);

        intTargetCell_x = (int) Math.floor(targetPosition_X / BluehatCanvas.TILE_HEIGHT_WIDTH);
        intTargetCell_y = (int) Math.floor(targetPosition_Y / BluehatCanvas.TILE_HEIGHT_WIDTH);

//Create the pathtile object the sprite is currently occupying and add it to the closed array
        PathTile pt = new PathTile(intCell_x, intCell_y);
        PathTile ptEndPoint = new PathTile(intTargetCell_x, intTargetCell_y);
        pt.setIntGValue(intG);
        pt.setIntHValue((abs(intTargetCell_x - intCell_x) + (abs(intTargetCell_y - intCell_y))));
        vecClosedPath.addElement(pt);
        

//get the last pathtile from the ClosedPath vector
        PathTile ptCurrentTile = (PathTile) vecClosedPath.lastElement();
        
        while (!ptCurrentTile.equals(ptEndPoint)) {
            
            ptCurrentTile = (PathTile) vecClosedPath.lastElement();
            intG++;
            //Create pathtiles around the agent and if unblocked then add them to openpath vector.
            for (int i = 0; i < 8; i = i + 2) {
            //0,-1 = cell below
                //1,0 = cell to the right
                //0,1 = cell above
                //-1,0 = cell to the left

                int intCoordinatesAroundAgent[] = {0, -1, 1, 0, 0, 1, -1, 0}; //x and y coordinates

                intCell_x = ptCurrentTile.getPosition_x() + intCoordinatesAroundAgent[i];
                intCell_y = ptCurrentTile.getPosition_y() + intCoordinatesAroundAgent[i + 1];

                if (intCell_x > 0 && intCell_y > 0) {
                    int intMapValue = tlMap.getCell(intCell_x, intCell_y);
                    if (intMapValue == tileUnblocked) { //the tile is open, not a wall
                        PathTile ptOpen = new PathTile(intCell_x, intCell_y);
                        ptOpen.setIntGValue(intG);
                        ptOpen.setIntHValue(abs(intTargetCell_x - intCell_x) + abs(intTargetCell_y - intCell_y));
                        if (!checkClosedPathTiles(vecClosedPath, ptOpen)) { //check if it exists in the closed vector
                            vecOpenPath.addElement(ptOpen);
                        };
                    }
                }
            }

        //iterate through the OPEN Vector and determine the Pathtile with the lowest F value which will become 
        // the new addition to the CLOSED Vector and newest step in the path.
            Enumeration enumOpenPathTiles = vecOpenPath.elements();
            PathTile objNewClosePT = null;

            while (enumOpenPathTiles.hasMoreElements()) {

                PathTile objOpenPT = (PathTile) enumOpenPathTiles.nextElement();
                if (objNewClosePT == null) {
                    objNewClosePT = objOpenPT;
                } else if (objOpenPT.getIntFValue() < objNewClosePT.getIntFValue()) {
                    objNewClosePT = objOpenPT;

                }

            }
            //place the objClosePT in the Closed Vector
            vecClosedPath.addElement(objNewClosePT);
            vecOpenPath.removeAllElements();//remove the elements to prepare for the next pathtile.

        }//do it again with the new closed pathtile but add one to G

        //
        return null;
    }

    private boolean checkClosedPathTiles(Vector vecClosedPath, PathTile ptOpen) {
        //Checks if the pathtile is in the closed vector. 
        
        Enumeration enumClosedPathTiles = vecClosedPath.elements();
        while (enumClosedPathTiles.hasMoreElements()) {
            PathTile ptClosed = (PathTile) enumClosedPathTiles.nextElement();
            if (ptClosed.getPosition_x() == ptOpen.getPosition_x() && ptClosed.getPosition_y() == ptOpen.getPosition_y()) {
                return true;
            }
        }
        return false;
    }
}
