/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluehat;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;


/**
 * @author Andrew
 */
public class BluehatStart extends MIDlet {

    private BluehatCanvas bluehatCanvas;
    private Display display;
    
    public BluehatStart(){
        bluehatCanvas = new BluehatCanvas("Blue Hat");
    }
    public void startApp() {
        display = Display.getDisplay(this);
        bluehatCanvas.start();
        display.setCurrent(bluehatCanvas);
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
