/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluehat;

import static java.lang.Math.abs;
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

    private int change_direction=0;

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
    
    private Vector findPath(int currentPosition_X, int currentPosition_Y, int targetPosition_X, int targetPosition_Y, int tileHeight, int tileWidth, int tileUnblocked, TiledLayer tlMap)
    {
        //Create two vectors to hold the closed path and the open path
        Vector vecOpenPath = new Vector();
        Vector vecClosedPath = new Vector();
        int intCell_x;
        int intCell_y;
        
        int intTargetCell_x;
        int intTargetCell_y;
        
        intCell_x = (int) Math.floor(currentPosition_X/tileWidth);
        intCell_y = (int) Math.floor(currentPosition_Y/tileHeight);
        
        intTargetCell_x = (int) Math.floor(targetPosition_X/tileWidth);
        intTargetCell_y = (int) Math.floor(targetPosition_Y/tileHeight);
        
//Create a the tile the sprite is occupying
 
        PathTile pt = new PathTile(intCell_x,intCell_y,tileHeight,tileWidth);
        pt.setIntGValue(0);
        pt.setIntHValue((abs(intTargetCell_x-intCell_x)+ (abs(intTargetCell_y-intCell_y))));
        vecClosedPath.addElement(pt);
        
        //Create pathtiles around the agent and if unblocked then add them to openpath vector.
         for (int i = 0; i < 8; i = i + 2) {
            int intCoordinatesAroundAgent[] = {0, -1, 1, 0, 0, 1, -1, 0}; //x and y coordinates

            intCell_x = pt.getPosition_x() + intCoordinatesAroundAgent[i];
            intCell_y = pt.getPosition_y() + intCoordinatesAroundAgent[i + 1];
            if(intCell_x > 0 && intCell_y >0){
                int intMapValue = tlMap.getCell(intCell_x, intCell_y);
                if (intMapValue ==0){
                    PathTile ptOpen = new PathTile(intCell_x,intCell_y,tileHeight,tileWidth);
                    ptOpen.setIntGValue(1);
                    ptOpen.setIntHValue(abs(intTargetCell_x - intCell_x)+abs(intTargetCell_y-intCell_y));
                }
            }
        }
        
        //
        
        return null;
    }
}
