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
import java.util.Vector;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class BluehatStart extends MIDlet implements CommandListener {

    private BluehatCanvas bluehatCanvas;
    private Display display;
    Form form;
    Command cmdStartGame;
    Command cmdEndGame;
    ImageItem splash;
    
    private PlayerAvatar pc;

    public BluehatStart() {
        //Create a new form and display objects and provide them to the new
        //Bluehat canvas object. 

        form = new Form("Blue Hat");
        display = Display.getDisplay(this);

        cmdStartGame = new Command("Start", Command.OK, 1);
        cmdEndGame = new Command("Exit", Command.EXIT, 1);

        //Display the game title screen
        try {
            Image image = Image.createImage("/BlueHatTitle.png");
            splash = new ImageItem(null, image, ImageItem.LAYOUT_CENTER, "Blue Hat");

        } catch (IOException ioe) {
            System.out.println(ioe.toString());
        }

        //append the form objects to display them to the user.
        form.append(splash);
        form.addCommand(cmdStartGame);
        form.addCommand(cmdEndGame);
        form.setCommandListener(this);
        
         try{
         RMS_Character obj = new RMS_Character();
         
         
         
//         NPC npc = rms_npc.readNPCData("Router");
//         System.out.println("NPC Data "+ npc.toString());
//        RMS_NPC rms_npc = new RMS_NPC();  
//         HackSkill hs = new HackSkill("Log Tracing", 0);
//         NPC npc = new NPC(0,"NDI Level 1",3,"NDI.png",hs);
//         System.out.println("NPC Number: "+rms_npc.writeNPCData(npc));
         
         
//         RMS_MazeMap objMazeMap = new RMS_MazeMap();
//         String strMap = "23, 10, 10, 10, 10, 10, 10, 10, 10, 18, 10, 10, 10, 10, 28,"+
//            "0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 2,"+
//            "2, 0, 10, 10, 28, 0, 2, 2, 0, 2, 0, 10, 29, 0, 2,"+
//            "2, 0, 0, 0, 2, 0, 2, 2, 0, 2, 0, 0, 2, 0, 2,"+
//            "2, 0, 23, 0, 2, 0, 2, 2, 0, 33, 10, 10, 38, 0, 2,"+
//            "2, 0, 33, 0, 2, 0, 2, 2, 0, 0, 0, 0, 0, 0, 2,"+
//            "2, 0, 0, 0, 2, 0, 0, 0, 0, 23, 10, 10, 0, 10, 2,"+
//            "2, 0, 10, 10, 38, 0, 2, 2, 0, 4, 0, 0, 0, 0, 2,"+
//            "2, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 2, 0, 0, 2,"+
//            "2, 0, 0, 10, 10, 10, 18, 10, 10, 39, 0, 2, 0, 10, 2,"+
//            "2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 2, 0, 0, 2,"+
//            "2, 10, 10, 10, 30, 0, 2, 0, 10, 10, 10, 39, 0, 0, 2,"+
//            "2, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2,"+
//            "2, 0, 2, 0, 2, 0, 0, 0, 10, 10, 10, 10, 30, 0, 2,"+
//            "2, 0, 33, 10, 38, 0, 2, 0, 0, 0, 0, 0, 2, 0, 2,"+
//            "2, 0, 0, 0, 0, 0, 2, 0, 10, 10, 28, 0, 42, 0, 2,"+
//            "2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2,"+
//            "33, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 38";
//         
//         System.out.println("Maze Number: "+objMazeMap.writeMazeMapData(strMap));
        
//         int map[][] = objMazeMap.readMazeMapData(mapnumber);
        
        
         
//         pc.setName("Andrew Miller");
//         pc.setBackground("this is the background.");
//        
//         HackSkill hs1 = new HackSkill("HackSKill1",2);
//         HackSkill hs2 = new HackSkill("HackSKill2",4);
//         HackSkill hs3 = new HackSkill("HackSKill3",5);
//         HackSkill hs4 = new HackSkill("HackSKill4",1);
//        
//         Vector vecHackSkills = new Vector();
//         vecHackSkills.addElement(hs1);
//         vecHackSkills.addElement(hs2);
//         vecHackSkills.addElement(hs3);
//         vecHackSkills.addElement(hs4);
//        
////         pc.setVectorHackingSkill(vecHackSkills);
//        HackSkill hs1 = new HackSkill("Hardware Cracking",1);
//        HackSkill hs2 = new HackSkill("Social Engineering",3);
//        
//      
//            
//         pc = obj.readPlayerCharacterData(1);
//         pc.getVectorHackingSkill().addElement(hs1);
//         pc.getVectorHackingSkill().addElement(hs2);
//         
//         obj.writePlayerCharacterData(pc);
         
         pc = obj.readPlayerCharacterData(1);
         
         System.out.println("Player Character: " + pc.toString());
        
         } catch (Exception e){
         System.out.println(e.toString());
         }
         

    }

    public void startApp() {

        display.setCurrent(form);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command cmd, Displayable dsp) {
        /*
         Name: commandAction
         Description:  The start game will start the BlueCanvas object thread to start running.
         The Midlet object is passed to the Bluehat Canvas so that the canvas
         can exit the game to the operating system of the phone.
        
         The end game command destorys the Midlet and exits the user to the phones
         operating system.
         Inputs: command, displayable
         Output: void
         Called by Whom: void
         Calls: void
         */

        if (cmd == cmdStartGame) {
            reset();

        } else if (cmd == cmdEndGame) {
            try {
                this.destroyApp(true);
                notifyDestroyed();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    public void reset() {
        form.deleteAll();
        bluehatCanvas = new BluehatCanvas(display, form, this);
        display.setCurrent(bluehatCanvas);
    }
}
