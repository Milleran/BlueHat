/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluehat;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;

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
    //private BluehatTask clock;
    private boolean endLevel = true;
    private Command cmdStartHack;
    private Command cmdEndHack;
    private Form form;
    private Display display;
    
    private Sprite playerSprite;
    private Sprite serverSprite;
    private Image playerImage;
    private Image playerSpritePage;
    private Image serverSpritePage;
    private Image ndiSpritePage;
    private Sprite ndiSprite;
    private TiledLayer blueHatBackground;
    
    private int player_x_pos = 0;
    private int player_y_pos = 16;
    private int player_x_pos_last = 0;
    private int player_y_pos_last = 16;

    private boolean player_has_objective = false;
    private boolean run_game = true;
    
    static int TILE_HEIGHT_WIDTH = 16;
    static int WALL_IMPACT = 1;
    static int WALL_TILE = 378;
    static int FLOOR_TILE = 9;
    
    

    public BluehatCanvas(String strTitle) {
        super(true);
        setTitle(strTitle);
    }

    public BluehatCanvas(Display start, Form stform) {
        super(true);
        form = stform;
        display = start;
        //Create the contract screen/game objection screen.
        this.showContractScreen();

        //Create the player sprite that will be used in the game.
        try {
            playerSpritePage = Image.createImage("/Player0.png");
            playerImage = Image.createImage(playerSpritePage, 96, 48, 16, 16, Sprite.TRANS_NONE);
            playerSprite = new Sprite(playerImage, 16, 16);
            
            serverSpritePage = Image.createImage("/Server.png");
            serverSprite = new Sprite(serverSpritePage,16,16);
            int[] serverFrameSeq = {0,1,2};
            serverSprite.setFrameSequence(serverFrameSeq);
            
            //Create the network intrution detection agents.
            ndiSpritePage = Image.createImage("AgentSmith.png");
            ndiSprite = new Sprite(ndiSpritePage,16,16);
            int[] ndiFrameSeq = {0,1,2};
            ndiSprite.setFrameSequence(ndiFrameSeq);
        
        
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void run() {
        graphics = getGraphics();
        //create the game background and the network maze.
        gamemazeScreen();

        //Place the player at the starting position.
        playerSprite.defineReferencePixel(player_x_pos, player_y_pos);
        
        //Place the agent at the starting position.
        ndiSprite.defineReferencePixel(176, 208);
        ndiSprite.setPosition(176, 208);
        
        //varible to slow down the frame rate 
        int animationFrameRate = 0;
        
        //Run through the endless loop taking in the users input from the phone.
        while (run_game) {
            int keyState = this.getKeyStates();
            if ((keyState & UP_PRESSED) != 0) {

                player_y_pos_last = player_y_pos;
                player_y_pos--;

                System.out.println("X,Y: " + player_x_pos + "," + player_y_pos);
                //spriteA.setTransform(Sprite.TRANS_NONE);
            } else if ((keyState & RIGHT_PRESSED) != 0) {

                player_x_pos_last = player_x_pos;
                player_x_pos++;

                System.out.println("X,Y: " + player_x_pos + "," + player_y_pos);
                //spriteA.setTransform(Sprite.TRANS_ROT90);
            } else if ((keyState & LEFT_PRESSED) != 0) {

                player_x_pos_last = player_x_pos;
                player_x_pos--;

                System.out.println("X,Y: " + player_x_pos + "," + player_y_pos);
                //spriteA.setTransform(Sprite.TRANS_ROT270 );
            } else if ((keyState & DOWN_PRESSED) != 0) {

                player_y_pos_last = player_y_pos;
                player_y_pos++;

                System.out.println("X,Y: " + player_x_pos + "," + player_y_pos);
                //spriteA.setTransform(spriteA.TRANS_MIRROR_ROT180);
            }

            this.clearScreen(graphics);

            //repaint the background
            blueHatBackground.paint(graphics);
            //set the player at the new location on the screen
 
            playerSprite.setPosition(player_x_pos, player_y_pos);

            playerSprite.paint(graphics);
            
            //paint the server with a reduced framerate.
            serverSprite.setPosition(192, 208);
            animationFrameRate ++;
            if(animationFrameRate/10 == 1){
                serverSprite.nextFrame();
            }
            serverSprite.paint(graphics);
            
            int[] intAgentMove = randomAgentMovement(ndiSprite.getX(), ndiSprite.getY());
            ndiSprite.setPosition(intAgentMove[0], intAgentMove[1]);
            if(animationFrameRate/10 == 1){
                ndiSprite.nextFrame();
            }
            ndiSprite.paint(graphics);
            
            if(animationFrameRate/10 == 1){
                animationFrameRate = 0;
            }
            
            //Check for wall collisions, if collision occurs then place them
            //back at the last known good x,y
            if (detectWallTileCollision()) {
                player_x_pos = player_x_pos_last;
                player_y_pos = player_y_pos_last;
                playerSprite.paint(graphics);
            }
            
            //Check for the player sprite colliding with the server
            if(serverSprite.collidesWith(playerSprite, true)){
                serverSprite.setVisible(false);
                player_has_objective = true;
            }
            
            //The player exiting the maze, if the player has the object then succuess.
            //If not then the player is prevented from leaving.
            if (detectPlayerExitMaze()){
                if(player_has_objective == true){
                    showSuccessScreen();
                }else{
                    Font gameFont = Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_LARGE);
                    
                    graphics.setColor(0);
                    graphics.setFont(gameFont);
                    graphics.drawString("Empty Handed!", 0,288,0);
                    
                    player_x_pos = player_x_pos_last;
                    player_y_pos = player_y_pos_last;
                    playerSprite.paint(graphics);
                }
            }

            //flush the graphics for the next iteration of the loop.
            flushGraphics();

            //Put the thread asleep for 20 miliseconds.
            try {
                Thread.currentThread().sleep(10);
            } catch (InterruptedException x) {

            }

        }

    }

    private void showContractScreen() {
        //Setup the screen with the correct font type.
        graphics = getGraphics();
        Font fontSplash = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
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
    
    private void showSuccessScreen(){
        
        //Erase GameMaze Screen
        clearScreen(graphics);
        //Setup the screen with the correct font type.
        
        graphics = getGraphics();
        Font fontSplash = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        graphics.setFont(fontSplash);

        graphics.drawString("Success!!!!", 0, 0, 0);

        String strSuccess = "You have retrieved the document with the "
                + "specications from our servers. "
                + "Our IT security group will have to go through the data and "
                + "develop a strong defense against hackers.";
        
        

        BluehatUtil.drawMultilineString(graphics, fontSplash, strSuccess, 5, getHeight() / 8, 0, 225);
        
        //Add a exit game command
        cmdEndHack = new Command("Exit", Command.OK, 1);
        this.addCommand(cmdEndHack);
        this.setCommandListener(this);
    }

    private void gamemazeScreen() {

        //Create the Sprite for the player avatar.
        try {
            //Create the background with a tiledlayer
            Image background = Image.createImage("/Wall.png");

            int cols = getWidth() / TILE_HEIGHT_WIDTH;
            int rows = getHeight() / TILE_HEIGHT_WIDTH;

            blueHatBackground = new TiledLayer(cols, rows, background, TILE_HEIGHT_WIDTH, TILE_HEIGHT_WIDTH);

            int tileCount = background.getWidth() / TILE_HEIGHT_WIDTH;

            drawSelectedTiles(blueHatBackground, false, tileCount);

            //blueHatBackground.paint(graphics);
        } catch (Exception ioe) {
            System.out.println("Unable to get the background Image: " + ioe.toString());
        }

    }

    private void clearScreen(Graphics g) {
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

    public void commandAction(Command cmd, Displayable display) {
        if (cmd == cmdStartHack) {
            System.out.println("start");
            this.removeCommand(cmd);
            this.repaint();
            clearScreen(graphics);

            runner = new Thread(this);
            runner.start();

        }
        if (cmd == cmdEndHack){
            System.out.println("End");
            run_game = false;
        }

    }

    private boolean detectWallTileCollision() {

        if (playerSprite.collidesWith(blueHatBackground, true)) {
            System.out.print("Hit a wall");
            //must not allow the user to go into the wall of the network maze.
            return true;
        }
        return false;

    }
    
    private boolean detectPlayerExitMaze(){
        if(playerSprite.getX()<=0){
            return true;
        }
      return false;
    }

    private void drawSelectedTiles(TiledLayer tLayer, boolean seeAll, int maxScrTiles) {
        int[] mazeWalls = mazeWalls();
        int mazeCellNumber = 0;
        for (int rowcnt = 0; rowcnt < tLayer.getRows(); rowcnt++) {
            for (int colcnt = 0; colcnt < tLayer.getColumns(); colcnt++) {
                blueHatBackground.setCell(colcnt, rowcnt, mazeWalls[mazeCellNumber]);
                mazeCellNumber++; //move to the next cell
            }
        }
    }

    private int[] mazeWalls() {
        int[] maze = {
            WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE,
            FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE,
            WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE,
            WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE,
            WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE, FLOOR_TILE, WALL_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE,
            WALL_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, WALL_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE,
            WALL_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE,
            WALL_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE,
            WALL_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, WALL_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE,
            WALL_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, WALL_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE,
            WALL_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE,
            WALL_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE,
            WALL_TILE, WALL_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE,
            WALL_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, WALL_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE,
            WALL_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE,
            WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, WALL_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, WALL_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE,
            WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, FLOOR_TILE, WALL_TILE,
            WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE,
            WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE, WALL_TILE

        };
        return maze;
    }
     
    private int[] randomAgentMovement(int current_x,int current_y){
        
        //Discover what is around the agent sprite on the tile layaer
        java.util.Vector viableDirection = new java.util.Vector();
        for(int i = 0;i<4;i++){
            int intCoordinatesAroundAgent[] = {0,-1,1,0,0,1,-1,0}; //x and y coordinates
            
            if (blueHatBackground.getCell(current_x, current_y)==FLOOR_TILE)
            {
                AgentMovement agentMovement = new AgentMovement(current_x, current_y);
                viableDirection.addElement(agentMovement);
            }  
        }
        //Where is the player Sprite?
        //determine the direction UP,DOWN,LEFT,RIGHT
        Random rdmDirection = new Random(3);
        int intRandomDirection = rdmDirection.nextInt();
        if ((intRandomDirection) == 0) {

                current_y--;
                
            } else if ((intRandomDirection) == 3) {

                current_x++;
             
            } else if ((intRandomDirection) == 2) {

                current_x--;

            } else if ((intRandomDirection) == 1) {

                current_y++;
            }
        int intAgentPosition[]={current_x,current_y};
        return intAgentPosition;
    }

}
