/**
 * Assignment: TME1 Assignment Name: Blue Hat Assignment Date: March 25th, 2015
 *
 * @ author: Andrew Miller Student ID: 2433560 Course: COMP 486
 *
 * The primary purpose of the Bluehat Canvas class is to execute the game. The
 * class extends Game Canvas to allow the drawing of sprite's and to provide
 * basic game functionality. The class also implements the runnable interface to
 * run the game loop The class also implements the CommandListner to provide a
 * user interface to exit or play again.
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

    private Thread runner;
    private Command cmdStartHack;
    private Command cmdEndHack;
    private Command cmdPlayAgain;
    private Command cmdHack;
    private Command cmdResume;
    private Command cmdReHack;
    private Form form;
    private Display display;
    private Display displaystart;
    private MIDlet startgameMIDlet;
    Random rdmNumber = new Random();

    Player backgroundMusicPlayer;
    Player soundEffectPlayer;

    private Sprite playerSprite;
    private Sprite serverSprite;
    private Image firewallSpritePage;
    private Sprite firewallSprite;
    private Image playerImage;
    private Image playerSpritePage;
    private Image serverSpritePage;
    private Image ndiSpritePage;
    private AgentSprite[] ndiSprites;
    private TiledLayer blueHatBackground;
    private TiledLayer NetworkWall_NotAnimated;

    private Font gameFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD,
            Font.SIZE_LARGE);
    private String strStatus;

    private int player_x_pos = 16;
    private int player_y_pos = 16;
    private int player_x_pos_last = 16;
    private int player_y_pos_last = 16;
    private int agent_change_direction;
    private int animationFrameRate;

    private boolean player_has_objective = false;
    private boolean run_game = true;
    private boolean level_complete = false;
    private boolean game_paused = false;
    private boolean firewall_hacked = false;

    static int TILE_HEIGHT_WIDTH = 16;
    static int WALL_IMPACT = 1;
    static int WALL_TILE = 378;
    static int FLOOR_TILE = 0;

    static final int HCENTER = 1;
    static final int LEFT = 4;
    static final int TOP = 16;
    static final int VCENTER = 2;
    static final int BOTTOM = 32;
    static final int RIGHT = 8;
    static final int BASELINE = 64;

    //Create the player avatar
    PlayerAvatar pc;
    NPC objNPC;

    //Set the level of play, there are 6 levels to the game.
    int intMapLevel = 1;

    //Threat Level for the game
    static final int GAME_OVER_THREAT_LEVEL = 3;
    int current_threat_level = 0;

    public BluehatCanvas(String strTitle) {
        super(true);
        setTitle(strTitle);
    }

    public BluehatCanvas(Display start, Form stform, MIDlet startMIDlet, PlayerAvatar objPlayerAvatar) {

        super(true);
        form = stform;
        display = start;
        displaystart = start;
        startgameMIDlet = startMIDlet;
        pc = objPlayerAvatar;
        strStatus = "Current System Threat Level: ";

        //Create the contract screen/game objection screen.
        showContractScreen();

        //create the game sprites
        createGameSprites();

        //Place all the game pieces on the board.
        initializeGame();

    }

    private void initializeGame() {
        player_has_objective = false;
        run_game = true;
        level_complete = false;
        serverSprite.setVisible(true);
        player_x_pos = 16;
        player_y_pos = 16;
        player_x_pos_last = 16;
        player_y_pos_last = 16;

        strStatus = "Current System Threat Level: ";
        gamemazeScreen();
        playerSprite.defineReferencePixel(player_x_pos, player_y_pos);

        createNDIAgents();

        for (int i = 0; i < ndiSprites.length; i++) {
            ndiSprites[i].setPosition(80, 224);
        }

        if (intMapLevel < 6) {
            firewallSprite.setPosition(80, 208);
            firewallSprite.setVisible(true);
        }
        animationFrameRate = 0;
        playBackgroundMusic("toner_2.mp3", "audio/mpeg");

    }

    public void run() {
        /*
         Name: run
         Description: a runnable thread method, main game loop
         Inputs: void
         Output: void
         Called by Whom: nothing
         Calls: movePlayer, paintServer, moveAgent, detectWallTileCollision, 
         playBackgroundMusic, detectAgentCollision, showFailureScreen, 
         determineSuccuess, flushGraphics, 
         */

        //Run through the endless loop taking in the users input from the phone.
        while (run_game) {
            //System.out.println("The game thread is still running!!!$$%%##");
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
            graphics.drawString(strStatus + String.valueOf(current_threat_level), 0, 288, 0);
            //repaint the background
            blueHatBackground.paint(graphics);

            //increase the frame count by 1
            animationFrameRate++;

            //set the player at the new location on the screen
            movePlayer();
            if (intMapLevel == 6) {
                //paint the server at a reduced FrameRate
                paintServer(animationFrameRate);
            }

            //paint the firewall at the reduced framerate
            paintFireWall(animationFrameRate);

            //Moves the agent and reduces the framerate of the animation
            moveAgent(animationFrameRate);

            //control the framerate of the animations of the sprites, this
            // reduces it by a factor of 100.
            if (animationFrameRate / 100 == 1) {
                animationFrameRate = 0;
            }

            //Check for wall collisions, if collision occurs then place them
            //back at the last known good x,y
            if (detectWallTileCollision(playerSprite)) {
                player_x_pos = player_x_pos_last;
                player_y_pos = player_y_pos_last;
                playerSprite.paint(graphics);
            }

            //Check for the player sprite colliding with the server
            //If true then change the music and change the status message at the
            //bottom.
            if (serverSprite.collidesWith(playerSprite, true)) {
                serverSprite.setVisible(false);
                playBackgroundMusic("Chip Bit Danger.mp3", "audio/mpeg");
                showHackScreen("Encryption Level 1");
                while (game_paused) {

                    try {
                        runner.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                }
                player_has_objective = true;

                //strStatus = "You have it! Get to the exit.";
            }

            //If the player character touches the agent then the game is over
            if (detectAgentCollision()) {
                //run_game = !detectAgentCollision();
                //showFailureScreen();
                switch (intMapLevel) {
                    case 1:
                        showHackScreen("NDI Level 1");
                        break;
                    case 2:
                        showHackScreen("NDI Level 2");
                        break;
                    case 3:
                        showHackScreen("NDI Level 2");
                        break;
                    case 4:
                        showHackScreen("NDI Level 3");
                        break;
                    case 5:
                        showHackScreen("NDI Level 3");
                        break;
                    case 6:
                        showHackScreen("NDI Level 4");
                        break;
                }

                while (game_paused) {

                    try {
                        runner.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                }

                flushGraphics();

                resumeGame();

                //repostion the NDI Sprite
                playSoundEffect("Pickup_Coin.wav");

            }

            if (detectFirewallCollision()) {

                switch (intMapLevel) {
                    case 1:
                        showHackScreen("Firewall Level 1");
                        break;
                    case 2:
                        showHackScreen("Firewall Level 2");
                        break;
                    case 3:
                        showHackScreen("Firewall Level 3");
                        break;
                    case 4:
                        showHackScreen("Firewall Level 4");
                        break;
                    case 5:
                        showHackScreen("Firewall Level 5");
                        break;
                    case 6:
                        showHackScreen("Firewall Level 5");
                        break;
                }

                while (game_paused) {

                    try {
                        runner.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                }
                //have to remove the player sprite from the game.
                firewallSprite.setVisible(false);

                //If the firewall has been hacked then the player can move on to 
                //the next level.
                if (firewall_hacked) {
                    intMapLevel++;

                    initializeGame();
                }
                resumeGame();

                flushGraphics();

            }

            //check if the player has retrived the document and can exit the maze.
            determineSuccess();

            //determine if the system threat level has reached the threshold.
            if (current_threat_level >= GAME_OVER_THREAT_LEVEL) {
                showFailureScreen();
            }

            //flush the graphics for the next iteration of the loop.
            if (!level_complete) {
                flushGraphics();
            }

            //Put the thread asleep for 10 miliseconds.
            try {
                Thread.sleep(10);
            } catch (InterruptedException x) {
                System.out.println(x.toString());
            }

        }

    }

    private void createGameSprites() {
        /*
         Name:createGameSprites
         Description: creates the game sprites that will be used in the game.
         Inputs: void
         Output: void
         Called by Whom: BluehatCanvas
         Calls: nothing
         */

        try {
            //Create the player sprite that will be used in the game.
            playerSpritePage = Image.createImage("/Player0.png");
            playerImage = Image.createImage(playerSpritePage, 96, 48, 16, 16, Sprite.TRANS_NONE);
            playerSprite = new Sprite(playerImage, 16, 16);

            //Create the server sprite
            serverSpritePage = Image.createImage("/Server.png");
            serverSprite = new Sprite(serverSpritePage, 16, 16);
            int[] serverFrameSeq = {0, 1, 2};
            serverSprite.setFrameSequence(serverFrameSeq);

            createNDIAgents();

            //Create the Firewall sprite that will move the character to different maps (levels)
            firewallSpritePage = Image.createImage("Firewall_icon.png");
            firewallSprite = new Sprite(firewallSpritePage, 16, 16);
            int[] firewallFrameSeq = {0, 1, 2};
            firewallSprite.setFrameSequence(firewallFrameSeq);

        } catch (IOException ioe) {
            System.out.println(ioe.toString());
        }
    }

    private void createNDIAgents() {
        try {
            //Create the network intrution detection agents.
            ndiSpritePage = Image.createImage("AgentSmith.png");
//            ndiSprite = new AgentSprite(ndiSpritePage, 16, 16);
            int[] ndiFrameSeq = {0, 1, 2};
//            ndiSprite.setFrameSequence(ndiFrameSeq);
            //Create an array of NDISprites
            switch (intMapLevel) {
                case 1:
                    ndiSprites = new AgentSprite[2];
                    ndiSprites[0] = new AgentSprite(ndiSpritePage, 16, 16);
                    ndiSprites[1] = new AgentSprite(ndiSpritePage, 16, 16);
                    ndiSprites[0].setFrameSequence(ndiFrameSeq);
                    ndiSprites[1].setFrameSequence(ndiFrameSeq);

                    break;
                case 2:
                    ndiSprites = new AgentSprite[2];
                    ndiSprites[0] = new AgentSprite(ndiSpritePage, 16, 16);
                    ndiSprites[1] = new AgentSprite(ndiSpritePage, 16, 16);
                    ndiSprites[0].setFrameSequence(ndiFrameSeq);
                    ndiSprites[1].setFrameSequence(ndiFrameSeq);

                    break;
                case 3:
                    ndiSprites = new AgentSprite[3];
                    ndiSprites[0] = new AgentSprite(ndiSpritePage, 16, 16);
                    ndiSprites[1] = new AgentSprite(ndiSpritePage, 16, 16);
                    ndiSprites[2] = new AgentSprite(ndiSpritePage, 16, 16);
                    ndiSprites[0].setFrameSequence(ndiFrameSeq);
                    ndiSprites[1].setFrameSequence(ndiFrameSeq);
                    ndiSprites[2].setFrameSequence(ndiFrameSeq);
                    break;
                case 4:
                    ndiSprites = new AgentSprite[3];
                    ndiSprites[0] = new AgentSprite(ndiSpritePage, 16, 16);
                    ndiSprites[1] = new AgentSprite(ndiSpritePage, 16, 16);
                    ndiSprites[2] = new AgentSprite(ndiSpritePage, 16, 16);
                    ndiSprites[0].setFrameSequence(ndiFrameSeq);
                    ndiSprites[1].setFrameSequence(ndiFrameSeq);
                    ndiSprites[2].setFrameSequence(ndiFrameSeq);
                    break;
                case 5:
                    ndiSprites = new AgentSprite[4];
                    ndiSprites[0] = new AgentSprite(ndiSpritePage, 16, 16);
                    ndiSprites[1] = new AgentSprite(ndiSpritePage, 16, 16);
                    ndiSprites[2] = new AgentSprite(ndiSpritePage, 16, 16);
                    ndiSprites[3] = new AgentSprite(ndiSpritePage, 16, 16);
                    ndiSprites[0].setFrameSequence(ndiFrameSeq);
                    ndiSprites[1].setFrameSequence(ndiFrameSeq);
                    ndiSprites[2].setFrameSequence(ndiFrameSeq);
                    ndiSprites[3].setFrameSequence(ndiFrameSeq);
                    break;
                case 6:
                    ndiSprites = new AgentSprite[4];
                    ndiSprites[0] = new AgentSprite(ndiSpritePage, 16, 16);
                    ndiSprites[1] = new AgentSprite(ndiSpritePage, 16, 16);
                    ndiSprites[2] = new AgentSprite(ndiSpritePage, 16, 16);
                    ndiSprites[3] = new AgentSprite(ndiSpritePage, 16, 16);
                    ndiSprites[0].setFrameSequence(ndiFrameSeq);
                    ndiSprites[1].setFrameSequence(ndiFrameSeq);
                    ndiSprites[2].setFrameSequence(ndiFrameSeq);
                    ndiSprites[3].setFrameSequence(ndiFrameSeq);
                    break;
            }
        } catch (IOException ioe) {

        }
    }

    private void determineSuccess() {
        /*
         Name: determineSuccess
         Description: determines if the player has the item and can exit the 
         maze.
         Inputs: void
         Output: void
         Called by Whom: run
         Calls: detectPlayerExitMaze, showSuccessScreen, playBackgroundMusic
         */

        //The player exiting the maze, if the player has the object then succuess.
        //If not then the player is prevented from leaving.
        if (player_has_objective == true) {
            showSuccessScreen();
            playBackgroundMusic("Grey Sector v0_85.mp3", "audio/mpeg");
            //run_game = false;
            level_complete = true;

            //reset the player objective.
            player_has_objective = false;
        }
//            } else {
//                Font gameFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
//
//                graphics.setColor(0);
//                graphics.setFont(gameFont);
//                graphics.drawString("You must retrieve the secret document!", 0, 288, 0);
//
//                player_x_pos = player_x_pos_last;
//                player_y_pos = player_y_pos_last;
//                playerSprite.paint(graphics);
//            }

    }

    private void moveAgent(int animationFrameRate) {
        /*
         Name: moveAgent
         Description:
         Only get the array after 16 loops of the run to match the tile size.
         move 16 pixels and then determine the next tile to move toward.
         Inputs: integer
         Output: void
         Called by Whom: run()
         Calls: nothing
         */
        for (int i = 0; i < ndiSprites.length; i++) {
            ndiSprites[i].setChange_direction(ndiSprites[i].getChange_direction() + 1);
            if (ndiSprites[i].getChange_direction() >= TILE_HEIGHT_WIDTH) { //check if the sprite has moved a tile before changing the direction.
                //pass the agents current position and the current direction.

                System.out.println("Sprite:" + i + " X:" + ndiSprites[i].getX());
                System.out.println("Sprite:" + i + " Y:" + ndiSprites[i].getY());
                ndiSprites[i].setDirection(randomAgentMovement(ndiSprites[i].getX(), ndiSprites[i].getY(), ndiSprites[i].getDirection()));
                ndiSprites[i].setPosition(ndiSprites[i].getX() + ndiSprites[i].getDirection().getX_pos(), ndiSprites[i].getY() + ndiSprites[i].getDirection().getY_pos());
                if (detectWallTileCollision(ndiSprites[i])) {
//                    System.out.println("X:"+ndiSprites[i].getX());
//                    System.out.println("Y:"+ndiSprites[i].getY());
//                    System.out.println("Last X:"+ndiSprites[i].getNdi_x_pos_last());
//                    System.out.println("Last Y:"+ndiSprites[i].getNdi_y_pos_last());
                    ndiSprites[i].setPosition(ndiSprites[i].getNdi_x_pos_last() + ndiSprites[i].getDirection().getX_pos(), ndiSprites[i].getNdi_y_pos_last() + ndiSprites[i].getDirection().getY_pos());
                }
                ndiSprites[i].setChange_direction(0);
            }
            
        }
        //Collection set movement
        for (int j = 0; j < ndiSprites.length; j++) {
            ndiSprites[j].setPosition(ndiSprites[j].getX() + ndiSprites[j].getDirection().getX_pos(), ndiSprites[j].getY() + ndiSprites[j].getDirection().getY_pos());
        }

        if (animationFrameRate / 10 == 1) {
//            ndiSprite.nextFrame();
            for (int i = 0; i < ndiSprites.length; i++) {
                ndiSprites[i].nextFrame();
            }
        }
//        ndiSprite.paint(graphics);

        for (int i = 0; i < ndiSprites.length; i++) {
            ndiSprites[i].paint(graphics);
        }
    }

    private void paintServer(int animationFrameRate) {
        /*
         Name: paintServer
         Description: paint the server with a reduced framerate.
         Inputs: integer
         Output: void
         Called by Whom: run
         Calls: nothing
         */

        if (animationFrameRate / 10 == 1) {
            serverSprite.nextFrame();
        }
        serverSprite.paint(graphics);

    }

    private void paintFireWall(int animationFrameRate) {
        /*
         Name: paintFireWall
         Description: paint the firewall with a reduced framerate.
         Inputs: integer
         Output: void
         Called by Whom: run
         Calls: nothing
         */

        if (animationFrameRate / 10 == 1) {
            firewallSprite.nextFrame();
        }
        if (intMapLevel < 6) {
            firewallSprite.paint(graphics);
        }

    }

    private void movePlayer() {
        /*
         Name: movePlayer
         Description: move the player character by one pixel on the screen.
         The player_x_pos and player_y_pos are controlled by the run loop.
         Inputs: void
         Output: void
         Called by Whom: run
         Calls: nothing
         */

        playerSprite.setPosition(player_x_pos, player_y_pos);
        playerSprite.paint(graphics);
    }

    private void showHackScreen(String strNPC) {
        //Pause the game thread.
        game_paused = true;

        //Get the NPC object to hack.
        RMS_NPC objRMSNPC = new RMS_NPC();
        objNPC = objRMSNPC.readNPCData(strNPC);
        //need to save all the screen data and the objects positions
        resumeGame();

        clearScreen(graphics);
        graphics = getGraphics();

        Font fontSplash = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD,
                Font.SIZE_LARGE);
        graphics.setFont(fontSplash);

        graphics.drawString("Cracking - " + objNPC.getName(), getWidth() / 2, 0, TOP | HCENTER);

        graphics.drawImage(this.createImage(objNPC.getImage_name()), getWidth() / 2, 20, TOP | HCENTER);

        graphics.drawLine(0, 140, getWidth(), 140);
        graphics.drawString("Security Defense: " + String.valueOf(objNPC.getSecurity_defense_level()), getWidth() / 2, 150, TOP | HCENTER);
        graphics.drawLine(0, 165, getWidth(), 165);

        graphics.drawString("Hacker Skill", 10, 190, BOTTOM | LEFT);

        //create a string that represents the hackers skills with the objects hacker vulnerability
        try {

            Enumeration playSkills = pc.getVectorHackingSkill().elements();
            int skill_pos = 205;
            boolean show_no_skill = true;
            while (playSkills.hasMoreElements()) {
                HackSkill hs = (HackSkill) playSkills.nextElement();
                if (hs.getSkillName().equals(objNPC.getHack_attack().getSkillName())) {
                    graphics.drawString(hs.getSkillName() + ":" + String.valueOf(hs.getSkillLevel()), 25, skill_pos, BOTTOM | LEFT);
                    skill_pos += 15;
                    show_no_skill = false;
                }
            }
            if (show_no_skill) {
                graphics.drawString("No Skill", 25, skill_pos, BOTTOM | LEFT);
            }

            //create a command button to hack the defense.
            cmdHack = new Command("Hack", Command.OK, 1);
            this.addCommand(cmdHack);
            this.setCommandListener(this);

        } catch (Exception e) {

        }

        graphics.drawString("Hack Attack Results: ", 10, 250, BOTTOM | LEFT);
        graphics.drawString(strStatus + String.valueOf(current_threat_level), 0, 288, 0);

    }

    private void showContractScreen() {
        /*
         Name: showContractScreen
         Description: displays the contract screen to the player to start the 
         game.
         Inputs: void
         Output: void
         Called by Whom: BluehatCanvas
         Calls: drawMultilineString
         */

        //Setup the screen with the correct font type.
        graphics = getGraphics();
        Font fontSplash = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD,
                Font.SIZE_LARGE);
        graphics.setFont(fontSplash);

        graphics.drawString("Contract - " + pc.getName(), 0, 0, 0);

        String strContract = "You must retrieve the specification document "
                + "of the next new computer chip. "
                + "Your hacking will allow us to review and update our IT security."
                + " "
                + "Once you have decrypted the document you will have receive your payment.";

        BluehatUtil.drawMultilineString(graphics, fontSplash, strContract, 5,
                getHeight() / 8, 0, 225);

        //create a command button for the contract screen.
        cmdStartHack = new Command("Start", Command.OK, 1);
        this.addCommand(cmdStartHack);
        this.setCommandListener(this);

    }

    private void showSuccessScreen() {
        /*
         Name: showSuccessScreen
         Description: displays the success screen to the player if the player
         exits the maze.
         Inputs: void
         Output: void
         Called by Whom: determinSuccess
         Calls: clearScreen, drawMultilineString
         */

        //Erase GameMaze Screen
        clearScreen(graphics);

        //Setup the screen with the correct font type.
        graphics = getGraphics();
        Font fontSplash = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        graphics.setFont(fontSplash);

        graphics.drawString("Success!!!!", 0, 0, 0);

        String strSuccess = "Your hack was able to retrieve the specifications document! "
                + "Our IT security group will have to go through the data and "
                + "develop a stronger defense against hackers.";

        BluehatUtil.drawMultilineString(graphics, fontSplash, strSuccess, 5,
                getHeight() / 8, 0, 225);

        //Add a exit game command
        if (cmdEndHack == null) {
            cmdEndHack = new Command("Exit", Command.OK, 1);
            this.addCommand(cmdEndHack);
            this.setCommandListener(this);
        }

        if (cmdPlayAgain == null) {
            cmdPlayAgain = new Command("Play Again", Command.OK, 1);
            this.addCommand(cmdPlayAgain);
            this.setCommandListener(this);
        }
    }

    private void showFailureScreen() {
        /*
         Name: showFailureScreen
         Description: displays the failure screen to the player if they are caught
         by the agent.
         Inputs: void
         Output: void
         Called by Whom: run
         Calls: clearScreen,drawMultilineString 
         */

        //Erase GameMaze Screen
        clearScreen(graphics);

        //Setup the screen with the correct font type.
        graphics = getGraphics();
        Font fontSplash = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD,
                Font.SIZE_LARGE);
        graphics.setFont(fontSplash);

        graphics.drawString("Failure!!!!", 0, 0, 0);

        String strSuccess = "Your hack has failed to retrieve the document.";

        BluehatUtil.drawMultilineString(graphics, fontSplash, strSuccess, 5,
                getHeight() / 8, 0, 225);

        //Add a exit game command
        if (cmdEndHack == null) {
            cmdEndHack = new Command("Exit", Command.OK, 1);
            this.addCommand(cmdEndHack);
            this.setCommandListener(this);
        }
    }

    private void gamemazeScreen() {
        /*
         Name: gamemazeScreen
         Description: Creates the bluehat maze and places the server in a 
         random area of the maze.
         Inputs: void
         Output: void
         Called by Whom: initializeGame
         Calls: getNetworkWall_NotAnimated
         */

        //Create the Sprite for the player avatar.
        try {
            drawMap();
            if (intMapLevel == 6) {
                //Draw the Server Sprite in a random floor only area of the Maze
                boolean flag = true;

                while (flag) {
                    int random_x = rdmNumber.nextInt(16);
                    int random_y = rdmNumber.nextInt(13);

                    //Place the server sprite on a Floor tile and 6 or more rows below the player.
                    if (blueHatBackground.getCell(random_y, random_x) == FLOOR_TILE && random_y >= 6) {
                        serverSprite.setPosition(random_x * TILE_HEIGHT_WIDTH, random_y * TILE_HEIGHT_WIDTH);
                        flag = false;
                    }

                }
            }

        } catch (Exception ioe) {
            System.out.println("Unable to get the background Image: " + ioe.toString());
        }

    }

    private void clearScreen(Graphics g) {
        /*
         Name: clearScreen
         Description:Clear the screen with white.
         Inputs: graphics
         Output: void
         Called by Whom: run, showSuccessScreen, showFailureScreen, commandAction
         Calls: nothing
         */

        g.setColor(0xFFFFFF);
        g.setClip(0, 0, getWidth(), getHeight());
        g.fillRect(0, 0, getWidth(), getHeight());

    }

    Image createImage(String strFileName) {
        /*
         Name: createImage
         Description: Steps to create an image from a file on the phones local memory.
         Inputs: string
         Output: image
         Called by Whom:
         Calls: Nothing
         */

        Image tempImage = null;
        try {
            tempImage = Image.createImage(strFileName);
        } catch (Exception ioe) {
            System.out.println(ioe.toString());
        }
        return tempImage;
    }

    public void commandAction(Command cmd, Displayable display) {
        /*
         Name: commmandAction
         Description: The method captures the command events on the phone.
         Inputs: command, displayable
         Output: void
         Called by Whom: nothing 
         Calls: clearScreen, playSoundEffect, initializeGame
         */

        if (cmd == cmdStartHack) {

            this.removeCommand(cmd);
            this.repaint();
            clearScreen(graphics);
            playSoundEffect("Powerup.wav");

            //starts the game loop.
            runner = new Thread(this);
            runner.start();

        }
        if (cmd == cmdEndHack) {

            run_game = false;
            startgameMIDlet.notifyDestroyed();

        }
        if (cmd == cmdPlayAgain) {

            clearScreen(graphics);

            displaystart = Display.getDisplay(startgameMIDlet);
            displaystart.setCurrent(form);

            //need a reset function on all the varibles.
            playSoundEffect("Powerup.wav");

        }
        if (cmd == cmdResume) {

            game_paused = false;

            display.removeCommand(cmdResume);
            //Redraw the maze from it previous state.
            this.repaint();

        }
        if (cmd == cmdReHack) {
            //remove any existing string text in the results area
            graphics.setColor(0xFFFFFF);
            graphics.fillRect(0, 250, getWidth(), 270);

            conductHackAttack(display);

            display.removeCommand(cmdReHack);
            this.repaint();
        }
        if (cmd == cmdHack) {

            conductHackAttack(display);

            this.repaint();

        }

    }

    private boolean detectWallTileCollision(Sprite objSprite) {
        /*
         Name: detectWallTileCollision
         Description: returns if the player collides with the wall.
         Inputs: void
         Output: boolean
         Called by Whom: run
         Calls: nothing
         */

        if (objSprite.collidesWith(blueHatBackground, true)) {
            //must not allow the user to go into the wall of the network maze.
            return true;
        }
        return false;

    }

    private boolean detectFirewallCollision() {
        /*
         Name: detectFirewallCollision
         Description: returns if the player collides with the firewall.
         Inputs: void
         Output: boolean
         Called by Whom: run
         Calls: playSoundEffect, playBackgroundMusic
         */
        //if the player hits the agent then change the music and return true.
        if (playerSprite.collidesWith(firewallSprite, true)) {
            return true;
        }
        return false;
    }

    private boolean detectAgentCollision() {
        /*
         Name: detectAgentCollision
         Description: returns if the player collides with the agent.
         Inputs: void
         Output: boolean
         Called by Whom: run
         Calls: playSoundEffect, playBackgroundMusic
         */

        //if the player hits the agent then change the music and return true.
        for (int i = 0; i < ndiSprites.length; i++) {
            if (playerSprite.collidesWith(ndiSprites[i], true)) {
                playSoundEffect("Explosion.wav");
                playBackgroundMusic("ThisGameIsOver.wav", "audio/X-wav");
                ndiSprites[i].setPosition(33, 16);
                return true;
            }
        }

        return false;
    }

    private boolean detectPlayerExitMaze() {
        /*
         Name: detectPlayerExitMaze
         Description: Since the network maze has walls all arond the screen the only exit
         has a position where x =0
         Inputs: void
         Output: boolean
         Called by Whom: determineSuccess
         Calls: nothing
         */

        return playerSprite.getX() <= 0;
    }

    public TiledLayer getNetworkWall_NotAnimated(int rows, int cols, Image background) throws java.io.IOException {
        /*
         Name: getNetworkWall_NotAnimated
         Description:  The method creates the tilelayer for the maze.
         it will loop through a 2 Dimenstional array to create the tiledlayer.
         Inputs: int, int, Image
         Output: TiledLayer
         Called by Whom: gamemazeScreen
         Calls: generateMaps
         */

        //Reset the tilelayer 
        NetworkWall_NotAnimated = null;

        NetworkWall_NotAnimated = new TiledLayer(cols, rows, background,
                TILE_HEIGHT_WIDTH, TILE_HEIGHT_WIDTH);

        //int[][] tiles = generateMaps();
        int[][] tiles = new RMS_MazeMap().readMazeMapData(intMapLevel);
        // Set all the cells in the tiled layer
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                NetworkWall_NotAnimated.setCell(col, row, tiles[row][col]);
            }
        }

        // write post-init user code here
        return NetworkWall_NotAnimated;
    }

    private AgentMovement randomAgentMovement(int current_x, int current_y, AgentMovement currentDirection) {
        /*
         Name: randomAgentMovement
         Description: The random agent movement method will randomly determine the direction of the 
         agent every 16 pixels traveled. To stop the agent from moving back and 
         forth in the same area I added multiple of the same direction into the 
         vector. Then the application generates a random number to choose from
         the vector and that determines the next direction that the agent moves.
         Inputs: int, int, AgentMovement
         Output: int array
         Called by Whom: moveAgent
         Calls: AgentMovement
         */

        int intAgentPosition[] = {0, 0};

        if (blueHatBackground.isVisible()) {
            //Discover what is around the agent sprite on the tile layaer
            java.util.Vector viableDirection = new java.util.Vector();

            for (int i = 0; i < 8; i = i + 2) {
                int intCoordinatesAroundAgent[] = {0, -1, 1, 0, 0, 1, -1, 0}; //x and y coordinates
                int tile_x = current_x / TILE_HEIGHT_WIDTH + intCoordinatesAroundAgent[i];
                int tile_y = current_y / TILE_HEIGHT_WIDTH + intCoordinatesAroundAgent[i + 1];
                if (tile_x < 0) {
                    tile_x = 1;
                };
                if (tile_y < 0) {
                    tile_y = 1;
                };
                
                System.out.println("tile_x:" + tile_x);
                System.out.println("tile_y:" + tile_y);

                if (blueHatBackground.getCell(tile_x, tile_y) == FLOOR_TILE && tile_x != 0) {
                    AgentMovement agentMovement = new AgentMovement(intCoordinatesAroundAgent[i],
                            intCoordinatesAroundAgent[i + 1]);
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

            //determine the viable direction UP,DOWN,LEFT,RIGHT with the random
            //selection of a vector element.
            int direction_size = viableDirection.size();
            int intRandomDirection = rdmNumber.nextInt(direction_size);
            AgentMovement direction = (AgentMovement) viableDirection.elementAt(intRandomDirection);

            intAgentPosition[0] = direction.getX_pos();
            intAgentPosition[1] = direction.getY_pos();

        }

        return new AgentMovement(intAgentPosition[0], intAgentPosition[1]);
    }

    private int[][] generateMaps() {
        /*
         Name: generateMaps
         Description: Store all the mazes in a 2 dimenstional array calles maps.
         The array will hold multiple maps and then method will randomally
         select a map. Each map is 18 rows.
         Inputs: void
         Output: int array
         Called by Whom: getNetworkWall_NotAnimated
         Calls: nothing
         */

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
         Name: playBackgroundMusic
         Description: This starts the background music for the game.
         Used music from http://www.opengameart.org/ a open source
         public domain game asset library.
         "This Game Is Over" - Joseph Pueyo http://www.josephpueyo.com/
         Inputs: string, string
         Output: void
         Called by Whom: initializeGame, run, determineSuccess, 
         detectAgentCollision
         Calls: nothing
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
         Name:playSoundEffect
         Description:  This method plays sound effects for the game.
         Used effects from http://www.opengameart.org/ a open source
         public domain game asset library.
         Inputs: string
         Output: void
         Called by Whom: commandAction, detectAgentCollision
         Calls: nothing
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

    private boolean conductHackAttack(Displayable display) {
        graphics = getGraphics();

        boolean attackResult = false;

        if (pc != null && objNPC != null) {
            try {

                //generate a random roll between 1 and 6
                Random r = new Random();
                //int intHackAttackRoll = r.nextInt(5)+1;
                int intHackAttackRoll = 6;

                int intHackerSkill = 0;
                int intHackerAttackValue = 0;

                //Add Skill level to the random roll.
                Enumeration playSkills = pc.getVectorHackingSkill().elements();
                while (playSkills.hasMoreElements()) {
                    HackSkill hs = (HackSkill) playSkills.nextElement();
                    if (hs.getSkillName().equals(objNPC.getHack_attack().getSkillName())) {
                        intHackerSkill = hs.getSkillLevel();

                    }
                }
                intHackerAttackValue = intHackAttackRoll + intHackerSkill;

                //Determine failure
                if (intHackerAttackValue >= objNPC.getSecurity_defense_level()) {
                    //Sucessful hack and back to the network maze.
                    graphics.drawString(String.valueOf(intHackerSkill) + " Skill + " + String.valueOf(intHackAttackRoll) + " Roll = " + String.valueOf(intHackerAttackValue), getWidth() / 2, 250, TOP | HCENTER);
                    graphics.drawString("CRACKED!!!!", getWidth() / 2, 270, TOP | HCENTER);

                    display.removeCommand(cmdHack);

                    cmdResume = new Command("Resume", Command.OK, 1);
                    display.addCommand(cmdResume);
                    this.setCommandListener(this);
                    attackResult = true;

                    //Check if the hack was against a firewall to get to the next level.
                    if (objNPC.getName().startsWith("Firewall")) {
                        firewall_hacked = true;
                    }

                } else {
                    //Threat Level increase by 1 and attempt again.
                    graphics.drawString(String.valueOf(intHackerSkill) + " Skill + " + String.valueOf(intHackAttackRoll) + " roll = " + String.valueOf(intHackerAttackValue), getWidth() / 2, 250, TOP | HCENTER);
                    graphics.drawString("FAILED, The Threat Level has increased!!!!", getWidth() / 2, 270, TOP | HCENTER);
                    current_threat_level += 1;

                    display.removeCommand(cmdHack);
                    display.removeCommand(cmdReHack);

                    cmdReHack = new Command("Rehack?", Command.OK, 1);
                    display.addCommand(cmdReHack);
                    attackResult = false;

                }

            } catch (Exception e) {

            }
        }

        return attackResult;
    }

    private void resumeGame() {

        //Redraw the map
        try {
            drawMap();

        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    private void drawMap() throws IOException {
        //Create the background with a tiledlayer
        Image background = Image.createImage("/networkWall.png");

        int cols = getWidth() / TILE_HEIGHT_WIDTH;
        int rows = getHeight() / TILE_HEIGHT_WIDTH;

        blueHatBackground = getNetworkWall_NotAnimated(rows, cols, background);

        blueHatBackground.setVisible(true);
    }

}
