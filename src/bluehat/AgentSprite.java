/**
 * Assignment: TME3 Assignment Name: Blue Hat Assignment Date: June 1st, 2015
 *
 * @ author: Andrew Miller Student ID: 2433560 Course: COMP 486
 *
 * The primary purpose of the AgentSprite class is to create a extended sprite
 * object that would find and retain its own path and movement direction
 * 
 */
package bluehat;

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

    private Vector vecAgentPath = null;

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

    public int getChangePath() {
        return change_direction;
    }

    public void setChangePath(int change_direction) {
        this.change_direction = change_direction;
    }

    public Vector getVecAgentPath() {
        return vecAgentPath;
    }

    public void setVecAgentPath(Vector vecAgentPath) {
        this.vecAgentPath = vecAgentPath;
    }

    public Vector findPath(int currentPosition_X, int currentPosition_Y, int targetPosition_X, int targetPosition_Y, int tileUnblocked, TiledLayer tlMap) {

        /*
         Name:findPath
         Description: This method detmines the pathfinding algorthium A*. It takes in
         two positions and determines the correct path.
         Inputs: integer, integer, integer, integer, integer, TiledLayer
         Output: Vector
         Called by Whom: moveAgent
         Calls: PathTile, sortPathTiles
         */
        //Create two vectors to hold the closed path and the open path
        //The closed vector will be the vector used to move the Sprite in the 
        //maze.
        Vector vecOpenPath = new Vector();
        Vector vecClosedPath = new Vector();
        int intCell_x = 0;
        int intCell_y = 0;

        int intG = 0;

        int intTargetCell_x = 0;
        int intTargetCell_y = 0;
        try {
//convert from pixels to cell location on the tiledlayer
            intCell_x = (int) Math.floor(currentPosition_X / BluehatCanvas.TILE_HEIGHT_WIDTH);
            intCell_y = (int) Math.floor(currentPosition_Y / BluehatCanvas.TILE_HEIGHT_WIDTH);

            intTargetCell_x = (int) Math.floor(targetPosition_X / BluehatCanvas.TILE_HEIGHT_WIDTH);
            intTargetCell_y = (int) Math.floor(targetPosition_Y / BluehatCanvas.TILE_HEIGHT_WIDTH);

//Create the pathtile object the sprite is currently occupying and add it to the closed array
            PathTile ptStartPoint = new PathTile(intCell_x, intCell_y);
            PathTile ptEndPoint = new PathTile(intTargetCell_x, intTargetCell_y);

            ptStartPoint.setIntGValue(intG);
            ptStartPoint.setIntHValue((Math.abs(intTargetCell_x - intCell_x) + (Math.abs(intTargetCell_y - intCell_y))));

            vecClosedPath.addElement(ptStartPoint);

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
                            ptOpen.setIntHValue(Math.abs(intTargetCell_x - intCell_x) + Math.abs(intTargetCell_y - intCell_y));

                            if (!checkClosedPathTiles(vecClosedPath, ptOpen)) { //check if it exists in the closed vector
                                vecOpenPath.addElement(ptOpen);
                            }
                        }
                    }
                }
//            Enumeration enumOpenPath = vecOpenPath.elements();
//            while(enumOpenPath.hasMoreElements()){
//                System.out.println(((PathTile)enumOpenPath.nextElement()).toString());
//            }

                // Sort the OPEN Vector and determine the Pathtile with the lowest F value  
                // The lowest will be the new addition to the CLOSED Vector and newest 
                // step in the path for the agent.
                if (!vecOpenPath.isEmpty()) {
                    vecOpenPath = sortPathTiles(vecOpenPath);

                    //place the objClosePT in the Closed Vector
                    vecClosedPath.addElement(vecOpenPath.firstElement());
                } else {
                    return vecClosedPath;
                }
                vecOpenPath.removeAllElements();//remove the elements to prepare for the next pathtile.

            }//do it again with the new closed pathtile but add one to G
        } catch (Exception ex) {
            System.out.println(ex.toString());
            System.out.println("intCell_x: " + intCell_x);
            System.out.println("intCell_y: " + intCell_y);
            System.out.println("intTargetCell_x: " + intTargetCell_x);
            System.out.println("intTargetCell_y: " + intTargetCell_y);
            System.out.println("intG: " + intG);
            ex.printStackTrace();

        }
        return vecClosedPath;
    }

    private boolean checkClosedPathTiles(Vector vecClosedPath, PathTile ptOpen) {
        /*
         Name:checkClosedPathTiles
         Description: This method checks if the pathtile is in the closed vector.  
         hack attack on anything in the game.
         Inputs: Vector, PathTile
         Output: boolean
         Called by Whom: findPath
         Calls: PathTile
         */

        Enumeration enumClosedPathTiles = vecClosedPath.elements();
        while (enumClosedPathTiles.hasMoreElements()) {
            PathTile ptClosed = (PathTile) enumClosedPathTiles.nextElement();
            if (ptClosed.getPosition_x() == ptOpen.getPosition_x() && ptClosed.getPosition_y() == ptOpen.getPosition_y()) {
                return true;
            }
        }
        return false;
    }

    private Vector sortPathTiles(Vector sort) {
        /*
         Name:sortPathTiles
         Description: Since Java 1.3 doesn't have any sorting abilities had to create one
         for pathtiles specificly. used the following link as a guide.
         http://stackoverflow.com/questions/6569414/how-to-sort-a-vector-of-string-in-java-me
         Inputs: Vector
         Output: Vector
         Called by Whom: findPath
         Calls: nothing
         */

        Vector v = new Vector();
        for (int count = 0; count < sort.size(); count++) {
            String s = String.valueOf(((PathTile) sort.elementAt(count)).getIntFValue());
            int i = 0;
            for (i = 0; i < v.size(); i++) {
                PathTile pt = (PathTile) v.elementAt(i);
                int c = s.compareTo(String.valueOf(pt.getIntFValue()));
                if (c < 0) {
                    v.insertElementAt(sort.elementAt(count), i);
                    break;
                } else if (c == 0) {
                    break;
                }
            }
            if (i >= v.size()) {
                v.addElement(sort.elementAt(count));
            }
        }
        return v;
    }
}
