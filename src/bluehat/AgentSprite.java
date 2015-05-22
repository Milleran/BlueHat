/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluehat;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Andrew
 */
public class AgentSprite extends Sprite{
    
    private AgentMovement direction = new AgentMovement(0, 0);

    
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
}
