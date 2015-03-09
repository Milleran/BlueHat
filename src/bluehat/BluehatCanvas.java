/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluehat;

import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author Andrew
 */
public class BluehatCanvas extends GameCanvas implements Runnable, CommandListener {

    private Graphics graphics;
    private Image imageSplash;
    private Image imageSplashBackground;
    private Thread runner;
    private Timer gameClock;
    private BluehatTask clock;
    private boolean endLevel = true;
    private Command cmdStartHack;
    private Form form;
    private Display display;
    private Sprite playerSprite;
    private Image playerImage;

    public BluehatCanvas(String strTitle) {
        super(true);
        setTitle(strTitle);
    }

    public BluehatCanvas(Display start, Form stform) {
        super(true);
        form = stform;
        display = start;
        //this.showContractScreen();
        runner = new Thread(this);
        runner.start();
    }

    public void run() {
        graphics = getGraphics();
        this.showContractScreen();
        
        //clock = new BluehatTask();
        //gameClock = new Timer();
        //gameClock.schedule(clock, 0, 1000);
        while (true) {
            int keyState = getKeyStates();
            System.out.println("KeyState: " + keyState);
            if(keyState == FIRE_PRESSED){
                this.repaint();
                clearScreen(graphics);
                
                gamemazeScreen();
            }
        }

    }

    private void showContractScreen() {
        //Setup the screen with the correct font type.
        
        Font fontSplash = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE);
        graphics.setFont(fontSplash);

        graphics.drawString("Contract", 0, 0, 0);

        String strContract = "You must retrieve the document with the "
                + "specications of the next new computer chip from our servers. "
                + "This will allow us to review and update our IT security.";

        BluehatUtil.drawMultilineString(graphics, fontSplash, strContract, 5, getHeight() / 8, 0, 225);

        cmdStartHack = new Command("Start", Command.OK, 1);
        this.addCommand(cmdStartHack);
        this.setCommandListener(this);

    }

    private void gamemazeScreen() {
        
        //Create the Sprite for the player avatar.
        try{
            playerImage = Image.createImage("/Player0.png");
            playerSprite = new Sprite(playerImage,16,16);
            playerSprite.defineReferencePixel(0, 0);
            playerSprite.setPosition(getWidth()/2, getHeight()/2);
            playerSprite.paint(graphics);
            
        }catch(Exception ioe){
            System.out.println("Unable to get the Player Image: " + ioe.toString());
        }
        
     }
    private void clearScreen(Graphics g){
        g.setColor(0xFFFFFF);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        
    }

    Image createImage(String strFileName) {
        Image tempImage = null;
        try {
            tempImage = Image.createImage(strFileName);
        } catch (Exception ioe) {
            System.out.println(ioe.toString());
        }
        return tempImage;
    }

    public class BluehatTask extends TimerTask {

        public void run() {
            //move the network detection objects.
        }
    }

    public void commandAction(Command cmd, Displayable display) {
        if (cmd == cmdStartHack) {
            System.out.println("start");
            //Clear the screen
            clearScreen(graphics);
            //Create the network maze to start the game.
            gamemazeScreen();
        }

    }

    //public void paint(Graphics g) {
        // get the dimensions of the screen:
        // clear the screen (paint it white):
        //this.clearScreen(g);
        //this.showContractScreen();
        //this.gamemazeScreen();
    //}
}
