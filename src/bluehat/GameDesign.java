/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluehat;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.io.IOException;

/**
 * @author Andrew
 */
public class GameDesign {

//<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private Image Wall;
    private Image networkWall;
    private TiledLayer Maze1;
//</editor-fold>//GEN-END:|fields|0|

//<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
//</editor-fold>//GEN-END:|methods|0|

    public Image getWall() throws java.io.IOException {//GEN-BEGIN:|1-getter|0|1-preInit
        if (Wall == null) {//GEN-END:|1-getter|0|1-preInit
 // write pre-init user code here
Wall = Image.createImage("/Wall.png");//GEN-BEGIN:|1-getter|1|1-postInit
        }//GEN-END:|1-getter|1|1-postInit
 // write post-init user code here
return this.Wall;//GEN-BEGIN:|1-getter|2|
    }//GEN-END:|1-getter|2|



    public Image getNetworkWall() throws java.io.IOException {//GEN-BEGIN:|3-getter|0|3-preInit
        if (networkWall == null) {//GEN-END:|3-getter|0|3-preInit
 // write pre-init user code here
networkWall = Image.createImage("/networkWall.png");//GEN-BEGIN:|3-getter|1|3-postInit
        }//GEN-END:|3-getter|1|3-postInit
 // write post-init user code here
return this.networkWall;//GEN-BEGIN:|3-getter|2|
    }//GEN-END:|3-getter|2|



    public TiledLayer getMaze1() throws java.io.IOException {//GEN-BEGIN:|5-getter|0|5-preInit
        if (Maze1 == null) {//GEN-END:|5-getter|0|5-preInit
 // write pre-init user code here
Maze1 = new TiledLayer(15, 18, getNetworkWall(), 16, 16);//GEN-BEGIN:|5-getter|1|5-midInit
            int[][] tiles = {
                {23, 10, 10, 10, 10, 10, 10, 10, 10, 18, 10, 10, 10, 10, 28},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 2},
                {2, 0, 10, 10, 28, 0, 2, 2, 0, 2, 0, 10, 29, 0, 2},
                {2, 0, 0, 0, 2, 0, 2, 2, 0, 2, 0, 0, 2, 0, 2},
                {2, 0, 23, 0, 2, 0, 2, 2, 0, 33, 10, 10, 38, 0, 2},
                {2, 0, 33, 0, 2, 0, 2, 2, 0, 0, 0, 0, 0, 0, 2},
                {2, 0, 0, 0, 2, 0, 0, 0, 0, 23, 10, 10, 0, 10, 2},
                {2, 0, 10, 10, 38, 0, 2, 2, 0, 4, 0, 0, 0, 0, 2},
                {2, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 2, 0, 0, 2},
                {2, 0, 0, 10, 10, 10, 18, 10, 10, 39, 0, 2, 0, 10, 2},
                {2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 2, 0, 0, 2},
                {2, 10, 10, 10, 30, 0, 2, 0, 10, 10, 10, 39, 0, 0, 2},
                {2, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2},
                {2, 0, 2, 0, 2, 0, 0, 0, 10, 10, 10, 10, 30, 0, 2},
                {2, 0, 33, 10, 38, 0, 2, 0, 0, 0, 0, 0, 2, 0, 2},
                {2, 0, 0, 0, 0, 0, 2, 0, 10, 10, 28, 0, 42, 0, 2},
                {2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2},
                {33, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 38}
            };//GEN-END:|5-getter|1|5-midInit
 // write mid-init user code here
for (int row = 0; row < 18; row++) {//GEN-BEGIN:|5-getter|2|5-postInit
                for (int col = 0; col < 15; col++) {
                    Maze1.setCell(col, row, tiles[row][col]);
                }
            }
        }//GEN-END:|5-getter|2|5-postInit
 // write post-init user code here
	return Maze1;//GEN-BEGIN:|5-getter|3|
    }//GEN-END:|5-getter|3|

}
