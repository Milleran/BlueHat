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

    public BluehatCanvas(String strTitle) {
        super(true);
        setTitle(strTitle);
    }

    public BluehatCanvas(Display start, Form stform) {
        super(true);
        form = stform;
        display = start;
        this.showContractScreen();

    }

    public void start() {
        this.showContractScreen();
        runner = new Thread(this);
        runner.run();
    }

    public void run() {

        clock = new BluehatTask();
        gameClock = new Timer();
        gameClock.schedule(clock, 0, 1000);
        while (true) {
            int keyState = getKeyStates();
            System.out.println("KeyState: " + keyState);
        }

    }

    private void showContractScreen() {
        //Setup the screen with the correct font type.
        graphics = getGraphics();
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
        //graphics.setColor(0x000000);
        //graphics.setClip(0, 0, getWidth(), getHeight());
        //graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(100, 149, 237);
        graphics.fillRect(0, 0, getWidth(), getHeight());
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
            //this.repaint();
        }

    }

    public void paint(Graphics g) {
        // get the dimensions of the screen:
        int width = getWidth();
        int height = getHeight();
        // clear the screen (paint it white):
        g.setColor(0xffffff);
    // The first two args give the coordinates of the top 
        // left corner of the rectangle.  (0,0) corresponds 
        // to the top left corner of the screen.
        g.fillRect(0, 0, width, height);
    }
}
