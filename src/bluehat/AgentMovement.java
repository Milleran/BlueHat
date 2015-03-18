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
