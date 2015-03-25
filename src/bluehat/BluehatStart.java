/*
Assignment: TME1
Assignment Name: Blue Hat
Assignement Date: March 25th, 2015

@ author: Andrew Miller
Student ID: 2433560
Course: COMP 486

The primary purpose of the midlet class is to start running the 
game canvas thread which starts the game. The class also implements 
the CommandListener interface to handle the command buttons (Start and Exit)

When the Start button is clicked by the user the BluehatCanvas class will start
running.
 */

package bluehat;

import java.io.IOException;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;


public class BluehatStart extends MIDlet implements CommandListener {

    private BluehatCanvas bluehatCanvas;
    private Display display;
    Form form;
    Command cmdStartGame;
    Command cmdEndGame;
    ImageItem splash;
    
    public BluehatStart(){
        //Create a new form and display objects and provide them to the new
        //Bluehat canvas object. 
        
        form=new Form("Blue Hat");
        display = Display.getDisplay(this);
                
        cmdStartGame = new Command("Start",Command.OK,1);
        cmdEndGame = new Command("Exit",Command.EXIT,1);

        
        //Display the game title screen
        try{
            Image image = Image.createImage("/BlueHatTitle.png");
            splash = new ImageItem(null,image,ImageItem.LAYOUT_CENTER,"Blue Hat");
            
            
        }catch(IOException ioe){
            System.out.println(ioe.toString());
        }
        
        //append the form objects to display them to the user.
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
        //The start game will start the BlueCanvas object thread to start running.
        //The Midlet object is passed to the Bluehat Canvas so that the canvas
        //can exit the game to the operating system of the phone.
        //
        //The end game command destorys the Midlet and exits the user to the phones
        //operating system.
        
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
