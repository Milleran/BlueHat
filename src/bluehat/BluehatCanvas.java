/**
* Assignment: TME1
* Assignment Name: Blue Hat
* Assignment Date: March 25th, 2015

* @ author: Andrew Miller
* Student ID: 2433560
* Course: COMP 486

* The primary purpose of the Bluehat Canvas class is to execute the game.
* The class extends Game Canvas to allow the drawing of sprite's and to 
* provide basic game functionality.
* The class also implements the runnable interface to continuesly run game loops
* The class also implements the CommandListner to provide a user interface to exit
* or play again.
*/
package bluehat;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;
import javax.microedition.midlet.MIDlet;


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
    private Command cmdPlayAgain;
    private Form form;
    private Display display;
    private MIDlet startgameMIDlet;
    Random rdmNumber = new Random();

    Player backgroundMusicPlayer;
    Player soundEffectPlayer;

    private Sprite playerSprite;
    private Sprite serverSprite;
    private Vector vecServerSprites;
    private int numberOfServers = 1;
    private Image playerImage;
    private Image playerSpritePage;
    private Image serverSpritePage;
    private Image ndiSpritePage;
    private Sprite ndiSprite;
    private TiledLayer blueHatBackground;
    private TiledLayer NetworkWall_NotAnimated;
    
    private Font gameFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
    private String strStatus;

    private int player_x_pos = 16;
    private int player_y_pos = 16;
    private int player_x_pos_last = 16;
    private int player_y_pos_last = 16;
    private int agent_change_direction;
    private int[] intAgentMove = {0, 0};
    private int animationFrameRate;

    private boolean player_has_objective = false;
    private boolean run_game = true;
    private boolean level_complete = false;

    static int TILE_HEIGHT_WIDTH = 16;
    static int WALL_IMPACT = 1;
    static int WALL_TILE = 378;
    static int FLOOR_TILE = 0;

    public BluehatCanvas(String strTitle) {
        super(true);
        setTitle(strTitle);
    }

    public BluehatCanvas(Display start, Form stform, MIDlet startMIDlet) {
        super(true);
        form = stform;
        display = start;
        startgameMIDlet = startMIDlet;
        strStatus = "Obtain the secret document!";
        //Create the contract screen/game objection screen.
        showContractScreen();


        //create the game sprites
        createGameSprites();
        
        //Place all the game pieces on the board.
        initializeGame();


    }
    private void initializeGame(){
        
        //Reset the player objective and server back to visible
        player_has_objective = false;
        level_complete = false;
        serverSprite.setVisible(true);
        player_x_pos = 16;
        player_y_pos = 16;
        
        //create the game background and the network maze.
            gamemazeScreen();
        //Place the player at the starting position.
        playerSprite.defineReferencePixel(player_x_pos, player_y_pos);

        //Place the agent at the starting position.
        ndiSprite.defineReferencePixel(80, 208);
        ndiSprite.setPosition(80, 208);

        //varible to slow down the frame rate 
        animationFrameRate = 0;

        //Start the background music for the game.
        playBackgroundMusic("toner_2.mp3", "audio/mpeg");
        

    }

    public void run() {
 
        //Run through the endless loop taking in the users input from the phone.
        while (run_game) {
            int keyState = this.getKeyStates();
            if ((keyState & UP_PRESSED) != 0) {

                player_y_pos_last = player_y_pos;
                player_y_pos--;

            } else if ((keyState & RIGHT_PRESSED) != 0) {

                player_x_pos_last = player_x_pos;
                player_x_pos++;

            } else if ((keyState & LEFT_PRESSED) != 0) {

                player_x_pos_last = player_x_pos;
                player_x_pos--;

            } else if ((keyState & DOWN_PRESSED) != 0) {

                player_y_pos_last = player_y_pos;
                player_y_pos++;

            }

            this.clearScreen(graphics);
            
            //Display the game status.
            graphics.setColor(0);
            graphics.setFont(gameFont);
            graphics.drawString(strStatus, 0, 288, 0);
            //repaint the background
            blueHatBackground.paint(graphics);

            //increase the frame count by 1
            animationFrameRate++;

            //set the player at the new location on the screen
            movePlayer();

            //paint the server and reduce the FrameRate
            paintServer(animationFrameRate);

            //Moves the agent and reduces the framerate of the animation
            moveAgent(animationFrameRate);

            //control the framerate of the animations of the sprites, this
            // reduces it by a factor of 100.
            if (animationFrameRate / 100 == 1) {
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
            //for (int i = 0; i < numberOfServers; i++) {
            //    serverSprite = (Sprite) vecServerSprites.elementAt(i);
                if (serverSprite.collidesWith(playerSprite, true)) {
                    serverSprite.setVisible(false);
                    player_has_objective = true;
                    playBackgroundMusic("Chip Bit Danger.mp3", "audio/mpeg");
                    strStatus = "You have it! Get to the exit.";
                }
            //}

            if (detectAgentCollision()) {
                showFailureScreen();
                //run_game = false;
            }

            //check if the player has retrived the document and can exit the maze.
            determineSuccess();

            //flush the graphics for the next iteration of the loop.
            if(!level_complete){
                flushGraphics();
            }
            //Put the thread asleep for 10 miliseconds.
            try {
                Thread.sleep(10);
            } catch (InterruptedException x) {

            }

        }

    }

    private void createGameSprites() {

        try {
            //Create the player sprite that will be used in the game.
            playerSpritePage = Image.createImage("/Player0.png");
            playerImage = Image.createImage(playerSpritePage, 96, 48, 16, 16, Sprite.TRANS_NONE);
            playerSprite = new Sprite(playerImage, 16, 16);

            //Create the server sprite
            serverSpritePage = Image.createImage("/Server.png");
            //       for (int i = 0; i < numberOfServers; i++) {
            serverSprite = new Sprite(serverSpritePage, 16, 16);
            int[] serverFrameSeq = {0, 1, 2};
            serverSprite.setFrameSequence(serverFrameSeq);
            //vecServerSprites.addElement(serverSprite);
            //       }

            //Create the network intrution detection agents.
            ndiSpritePage = Image.createImage("AgentSmith.png");
            ndiSprite = new Sprite(ndiSpritePage, 16, 16);
            int[] ndiFrameSeq = {0, 1, 2};
            ndiSprite.setFrameSequence(ndiFrameSeq);

        } catch (IOException ioe){
            System.out.println(ioe.toString());
        }
    }

    private void determineSuccess() {
        //The player exiting the maze, if the player has the object then succuess.
        //If not then the player is prevented from leaving.
        if (detectPlayerExitMaze()) {
            if (player_has_objective == true) {
                showSuccessScreen();
                playBackgroundMusic("Grey Sector v0_85.mp3", "audio/mpeg");
                //run_game = true;
                level_complete = true;
                               
                //reset the player objective.
                player_has_objective = false;
            } else {
                Font gameFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);

                graphics.setColor(0);
                graphics.setFont(gameFont);
                graphics.drawString("You must retrieve the secret document!", 0, 288, 0);

                player_x_pos = player_x_pos_last;
                player_y_pos = player_y_pos_last;
                playerSprite.paint(graphics);
            }
        }
    }

    private void moveAgent(int animationFrameRate) {
        //only get the array after 16 loops of the run to match the tile size.
        //move 16 pixels and then determine the next tile to move toward.
        agent_change_direction++;
        if (agent_change_direction >= TILE_HEIGHT_WIDTH) {
            intAgentMove = randomAgentMovement(ndiSprite.getX(), ndiSprite.getY(), new AgentMovement(intAgentMove[0], intAgentMove[1]));
            agent_change_direction = 0;
        }

        ndiSprite.setPosition(ndiSprite.getX() + intAgentMove[0], ndiSprite.getY() + intAgentMove[1]);

        if (animationFrameRate / 10 == 1) {
            ndiSprite.nextFrame();
        }
        ndiSprite.paint(graphics);
    }

    private void paintServer(int animationFrameRate) {
        //paint the server with a reduced framerate.

        if (animationFrameRate / 10 == 1) {
            serverSprite.nextFrame();
        }
        serverSprite.paint(graphics);

    }

    private void movePlayer() {
        playerSprite.setPosition(player_x_pos, player_y_pos);

        playerSprite.paint(graphics);
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

    private void showSuccessScreen() {

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
        cmdPlayAgain = new Command("Play Again", Command.OK, 1);

        this.addCommand(cmdEndHack);
        this.setCommandListener(this);

        this.addCommand(cmdPlayAgain);
        this.setCommandListener(this);
    }

    private void showFailureScreen() {

        //Erase GameMaze Screen
        clearScreen(graphics);
        //Setup the screen with the correct font type.

        graphics = getGraphics();
        Font fontSplash = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        graphics.setFont(fontSplash);

        graphics.drawString("Failure!!!!", 0, 0, 0);

        String strSuccess = "You have failed in retrieving the document.";

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
            Image background = Image.createImage("/networkWall.png");

            int cols = getWidth() / TILE_HEIGHT_WIDTH;
            int rows = getHeight() / TILE_HEIGHT_WIDTH;

            blueHatBackground = getNetworkWall_NotAnimated(rows, cols, background);

            //Draw the Server Sprite in a random floor only area of the Maze
            //for(int i=0;i<numberOfServers;i++){
            boolean flag = true;

            while (flag) {
                int random_x = rdmNumber.nextInt(16);
                int random_y = rdmNumber.nextInt(13);
                
                //Place the server sprite on a Floor tile and 6 or more rows below the player.
                if (blueHatBackground.getCell(random_y, random_x) == FLOOR_TILE && random_y>=6) {
                    System.out.println("Random_y: "+random_y + "Random_x: "+ random_x);
                    serverSprite.setPosition(random_x * TILE_HEIGHT_WIDTH, random_y * TILE_HEIGHT_WIDTH);
                    flag = false;
                }

            }
            //}

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

            this.removeCommand(cmd);
            this.repaint();
            clearScreen(graphics);

            runner = new Thread(this);
            runner.start();

        }
        if (cmd == cmdEndHack) {

            run_game = false;

            startgameMIDlet.notifyDestroyed();

        }
        if (cmd == cmdPlayAgain) {
            
            clearScreen(graphics);
            
            this.repaint();
            
            initializeGame();
          
            if(runner.isAlive()){
                System.out.println("its alive");
            }
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

    private boolean detectAgentCollision() {
        if (playerSprite.collidesWith(ndiSprite, true)) {
            playSoundEffect("Explosion.wav");
            playBackgroundMusic("ThisGameIsOver.wav", "audio/X-wav");
            return true;
        }
        return false;
    }

    private boolean detectPlayerExitMaze() {
        //Since the network maze has walls all arond the screen the only exit
        // has a position where x =0

        return playerSprite.getX() <= 0;
    }

    public TiledLayer getNetworkWall_NotAnimated(int rows, int cols, Image background) throws java.io.IOException {
        if (NetworkWall_NotAnimated == null) {

            NetworkWall_NotAnimated = new TiledLayer(cols, rows, background, TILE_HEIGHT_WIDTH, TILE_HEIGHT_WIDTH);

            int[][] tiles = generateMaps();

        // Set all the cells in the tiled layer
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    NetworkWall_NotAnimated.setCell(col, row, tiles[row][col]);
                }
            }
        }
        // write post-init user code here
        return NetworkWall_NotAnimated;
    }

    private int[] randomAgentMovement(int current_x, int current_y, AgentMovement currentDirection) {
        int intAgentPosition[] = {0, 0};

        if (blueHatBackground.isVisible()) {
            //Discover what is around the agent sprite on the tile layaer
            java.util.Vector viableDirection = new java.util.Vector();

            for (int i = 0; i < 8; i = i + 2) {
                int intCoordinatesAroundAgent[] = {0, -1, 1, 0, 0, 1, -1, 0}; //x and y coordinates
                int tile_x = current_x / TILE_HEIGHT_WIDTH + intCoordinatesAroundAgent[i];
                int tile_y = current_y / TILE_HEIGHT_WIDTH + intCoordinatesAroundAgent[i + 1];

                if (blueHatBackground.getCell(tile_x, tile_y) == FLOOR_TILE && tile_x != 0) {
                    AgentMovement agentMovement = new AgentMovement(intCoordinatesAroundAgent[i], intCoordinatesAroundAgent[i + 1]);
                    viableDirection.addElement(agentMovement);
                }
            }
            //Add the current direction if its a FLOOR_TILE
            int tile_x = current_x / TILE_HEIGHT_WIDTH + currentDirection.getX_pos();
            int tile_y = current_y / TILE_HEIGHT_WIDTH + currentDirection.getY_pos();

            if (blueHatBackground.getCell(tile_x, tile_y) == FLOOR_TILE && tile_x != 0) {

                viableDirection.addElement(currentDirection);
                viableDirection.addElement(currentDirection);
                viableDirection.addElement(currentDirection);
                viableDirection.addElement(currentDirection);
                viableDirection.addElement(currentDirection);
            }

            //Where is the player Sprite?
            //determine the viable direction UP,DOWN,LEFT,RIGHT
            int direction_size = viableDirection.size();
            int intRandomDirection = rdmNumber.nextInt(direction_size);
            AgentMovement direction = (AgentMovement) viableDirection.elementAt(intRandomDirection);

            intAgentPosition[0] = direction.getX_pos();
            intAgentPosition[1] = direction.getY_pos();

        }

        return intAgentPosition;
    }

    private int[][] generateMaps() {

        int[][] returningMap = new int[18][15];

        // Map arrays
        int[][] maps = {
            {23, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 28},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {23, 10, 10, 0, 10, 10, 18, 0, 18, 10, 10, 10, 0, 10, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2},
            {2, 0, 10, 10, 0, 10, 38, 0, 33, 10, 0, 10, 10, 10, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 23, 10, 10, 10, 10, 0, 10, 10, 10, 10, 10, 0, 2},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 2, 0, 23, 10, 10, 10, 10, 0, 23, 0, 28, 0, 2},
            {2, 0, 2, 0, 2, 0, 0, 0, 0, 0, 2, 0, 2, 0, 2},
            {2, 0, 2, 0, 0, 0, 23, 10, 10, 0, 2, 0, 2, 0, 2},
            {2, 0, 0, 0, 2, 0, 2, 0, 0, 0, 38, 0, 2, 0, 2},
            {2, 0, 2, 0, 2, 0, 2, 0, 23, 0, 0, 0, 2, 0, 2},
            {2, 0, 2, 0, 2, 0, 0, 0, 33, 10, 10, 10, 38, 0, 2},
            {2, 0, 2, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2},
            {2, 0, 0, 0, 2, 0, 33, 10, 10, 10, 10, 0, 10, 10, 2},
            {2, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
            {33, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 38},
            //add the second map
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
        };

        int numberOfMaps = maps.length / 18; //count the rows and divide by 18
        int startingMapRow = rdmNumber.nextInt(numberOfMaps) * 18;

        for (int i = startingMapRow; i < startingMapRow + 18; i++) {
            System.arraycopy(maps[i], 0, returningMap[i - startingMapRow], 0, 15);
        }

        return returningMap;
    }

    private void playBackgroundMusic(String strMusic, String strFileType) {
        /*
         This starts the background music for the game.
         Used music from http://www.opengameart.org/ a open source
         public domain game asset library.
         "This Game Is Over" - Joseph Pueyo http://www.josephpueyo.com/
         */

        //if there is background music playing then stop it and replace it with 
        //the new file.
        try {
            if (backgroundMusicPlayer != null) {
                if (backgroundMusicPlayer.getState() == Player.STARTED) {
                    backgroundMusicPlayer.stop();
                }
            }
            //bring in the file as a stream
            InputStream inputStream = this.getClass().getResourceAsStream("/music/" + strMusic);

            //Create a player for the music and fetch the music file.
            backgroundMusicPlayer = Manager.createPlayer(inputStream, strFileType);
            backgroundMusicPlayer.prefetch();

        //Add a listener to the canvas
            //backgroundMusicPlayer.addPlayerListener(this);
            //loop the music forever
            backgroundMusicPlayer.setLoopCount(-1);

        //set the volumn control
            VolumeControl backgroundVolumnControl = (VolumeControl) backgroundMusicPlayer.getControl("VolumeControl");
            backgroundVolumnControl.setLevel(10);

            //start the playing of the music.
            backgroundMusicPlayer.start();
        
        
        } catch (IOException ex) {
            System.out.println(ex.toString());
        } catch (MediaException ex) {
            System.out.println(ex.toString());
        }
    }

    private void playSoundEffect(String strSound) {
        /*
         This method plays sound effects for the game.
         Used effects from http://www.opengameart.org/ a open source
         public domain game asset library.
         */

        try {
            //bring in the file as a stream
            InputStream inputStream = this.getClass().getResourceAsStream("/sounds/" + strSound);

            //Create a player for the music and fetch the music file.
            soundEffectPlayer = Manager.createPlayer(inputStream, "audio/X-wav");
            soundEffectPlayer.prefetch();

        //set the volumn control
            VolumeControl backgroundVolumnControl = (VolumeControl) backgroundMusicPlayer.getControl("VolumeControl");
            backgroundVolumnControl.setLevel(10);

            //start the playing of the music.
            soundEffectPlayer.start();
      
       
        } catch (IOException ex) {
            System.out.println(ex.toString());
        } catch (MediaException ex) {
            System.out.println(ex.toString());
        }
    }

}
