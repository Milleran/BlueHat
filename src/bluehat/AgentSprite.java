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
    
}
