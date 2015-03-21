/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluehat;

import java.io.IOException;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;


/**
 * @author Andrew
 */
public class BluehatStart extends MIDlet implements CommandListener {

    private BluehatCanvas bluehatCanvas;
    private Display display;
    Form form;
    Command cmdStartGame;
    Command cmdEndGame;
    ImageItem splash;
    
    public BluehatStart(){
        form=new Form("Blue Hat");
        display = Display.getDisplay(this);
        bluehatCanvas = new BluehatCanvas(display,form, this);
        
        cmdStartGame = new Command("Start",Command.OK,1);
        cmdEndGame = new Command("Exit",Command.EXIT,1);
        
        //form.append(new Spacer(50,100));
        //Display Bluehat splash screen
        
        try{
            Image image = Image.createImage("/BlueHatTitle.png");
            splash = new ImageItem(null,image,ImageItem.LAYOUT_CENTER,"Blue Hat");
            
            
        }catch(IOException ioe){
            System.out.println(ioe.toString());
        }
        
        form.append(splash);
        form.addCommand(cmdStartGame);
        form.addCommand(cmdEndGame);
        form.setCommandListener(this);
        
    }
    public void startApp() {
       
        display.setCurrent(form);
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
    
    public void commandAction(Command cmd, Displayable dsp) {
        if(cmd == cmdStartGame){
         form.deleteAll();
         bluehatCanvas = new BluehatCanvas(display,form, this);
         display.setCurrent(bluehatCanvas);
                  
        }
        else if(cmd == cmdEndGame){
            try{
                this.destroyApp(true);
                notifyDestroyed();
            }catch(Exception e){
                System.out.println(e.toString());
            }
     }
    }
}
