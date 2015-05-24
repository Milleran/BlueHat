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
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.RecordStore;

public class BluehatStart extends MIDlet implements CommandListener {

    private BluehatCanvas bluehatCanvas;
    private Display display;
    Form form;
    Form frmPC;
    
    Command cmdStartGame;
    Command cmdEndGame;
    
    RMS_Character rmsCharacter = new RMS_Character();
    
    //Choose Character Form
    Form formCharacterList;
    Command cmdChooseCharacterForm;
    private ChoiceGroup characterChoiceGroup;
    Command cmdChooseCharacter;
    Command cmdExitChooseCharacter;
    Command cmdCreateCharacter;
    int characterChoiceGroupIndex;
    
    //Create Character Form
    Form formCreateCharacter;
    Command cmdSaveCharacter;
    Command cmdExitCreateCharacter;
    private TextField txtName;
    private TextField txtBackground;
    private TextField txtSkillPoints;
    private ChoiceGroup cgHackingSkills;
    
    
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
            //Delete the existing NPC and MazeMap RecordStores
            RecordStore.deleteRecordStore("NPCRS");
            RecordStore.deleteRecordStore("MazeMapRS");
         
            RMS_Character obj = new RMS_Character();
             
         
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
    
    private void createNPCDatabase(){
        
 
        RMS_NPC rms_npc = new RMS_NPC(); //opens record store
        
       
//        NPC npc = rms_npc.readNPCData("Router");
//        System.out.println("NPC Data "+ npc.toString());
         
        //Create the NDI Agents for the game
         HackSkill hs = new HackSkill("Log Tracing", 0);
         NPC npc = new NPC(0,"NDI Level 1",3,"NDI_Level_1.png",hs);
         System.out.println("NPC Number: "+rms_npc.writeNPCData(npc));
         
         
         npc = new NPC(0,"NDI Level 2",4,"NDI_Level_2.png",hs);
         System.out.println("NPC Number: "+rms_npc.writeNPCData(npc));
         
         
         npc = new NPC(0,"NDI Level 3",5,"NDI_Level_3.png",hs);
         System.out.println("NPC Number: "+rms_npc.writeNPCData(npc));
         
         npc = new NPC(0,"NDI Level 4",6,"NDI_Level_4.png",hs);
         System.out.println("NPC Number: "+rms_npc.writeNPCData(npc));
        
         
         //Create the Firewalls
         HackSkill hsFireWall = new HackSkill("Firmware Hacking",0);
         npc = new NPC(0,"Firewall Level 1",2,"FireWall_Level_1.png",hsFireWall);
         System.out.println("NPC Number: "+rms_npc.writeNPCData(npc));
         
         npc = new NPC(0,"Firewall Level 2",3,"FireWall_Level_2.png",hsFireWall);
         System.out.println("NPC Number: "+rms_npc.writeNPCData(npc));
         npc = new NPC(0,"Firewall Level 3",4,"FireWall_Level_3.png",hsFireWall);
         System.out.println("NPC Number: "+rms_npc.writeNPCData(npc));
         npc = new NPC(0,"Firewall Level 4",5,"FireWall_Level_4.png",hsFireWall);
         System.out.println("NPC Number: "+rms_npc.writeNPCData(npc));
         npc = new NPC(0,"Firewall Level 5",6,"FireWall_Level_5.png",hsFireWall);
         System.out.println("NPC Number: "+rms_npc.writeNPCData(npc));

         //Create the Router
         HackSkill hsRouter = new HackSkill("Packet Inspection",0);
         npc = new NPC(0,"Router Level 1",3,"wireless-router-icon.png",hsRouter);
         System.out.println("NPC Number: "+rms_npc.writeNPCData(npc));
         
         //Create the Document Encryption 
         HackSkill hsEncryption = new HackSkill("Encryption",0);
         npc = new NPC(0,"Encryption Level 1",4,".png",hsEncryption);

         
         
        
    }
    
    private void createMazeMaps(){
        
        RMS_MazeMap rms_mazemap = new RMS_MazeMap();
        
         String strMap1 = "23, 10, 10, 10, 10, 10, 10, 10, 10, 18, 10, 10, 10, 10, 28,"+
            "0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 2,"+
            "2, 0, 10, 10, 28, 0, 2, 2, 0, 2, 0, 10, 29, 0, 2,"+
            "2, 0, 0, 0, 2, 0, 2, 2, 0, 2, 0, 0, 2, 0, 2,"+
            "2, 0, 23, 0, 2, 0, 2, 2, 0, 33, 10, 10, 38, 0, 2,"+
            "2, 0, 33, 0, 2, 0, 2, 2, 0, 0, 0, 0, 0, 0, 2,"+
            "2, 0, 0, 0, 2, 0, 0, 0, 0, 23, 10, 10, 0, 10, 2,"+
            "2, 0, 10, 10, 38, 0, 2, 2, 0, 4, 0, 0, 0, 0, 2,"+
            "2, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 2, 0, 0, 2,"+
            "2, 0, 0, 10, 10, 10, 18, 10, 10, 39, 0, 2, 0, 10, 2,"+
            "2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 2, 0, 0, 2,"+
            "2, 10, 10, 10, 30, 0, 2, 0, 10, 10, 10, 39, 0, 0, 2,"+
            "2, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2,"+
            "2, 0, 2, 0, 2, 0, 0, 0, 10, 10, 10, 10, 30, 0, 2,"+
            "2, 0, 33, 10, 38, 0, 2, 0, 0, 0, 0, 0, 2, 0, 2,"+
            "2, 0, 0, 0, 0, 0, 2, 0, 10, 10, 28, 0, 42, 0, 2,"+
            "2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2,"+
            "33, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 38";
         
         System.out.println("Maze Number: "+rms_mazemap.writeMazeMapData(strMap1));
        
         String strMap2 = "23, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 28,"
                + "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,"
                + "23, 10, 10, 0, 10, 10, 18, 0, 18, 10, 10, 10, 0, 10, 2,"
                + "2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2,"
                + "2, 0, 10, 10, 0, 10, 38, 0, 33, 10, 0, 10, 10, 10, 2,"
                + "2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,"
                + "2, 0, 23, 10, 10, 10, 10, 0, 10, 10, 10, 10, 10, 0, 2,"
                + "2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,"
                + "2, 0, 2, 0, 23, 10, 10, 10, 10, 0, 23, 0, 28, 0, 2,"
                + "2, 0, 2, 0, 2, 0, 0, 0, 0, 0, 2, 0, 2, 0, 2,"
                + "2, 0, 2, 0, 0, 0, 23, 10, 10, 0, 2, 0, 2, 0, 2,"
                + "2, 0, 0, 0, 2, 0, 2, 0, 0, 0, 38, 0, 2, 0, 2,"
                + "2, 0, 2, 0, 2, 0, 2, 0, 23, 0, 0, 0, 2, 0, 2,"
                + "2, 0, 2, 0, 2, 0, 0, 0, 33, 10, 10, 10, 38, 0, 2,"
                + "2, 0, 2, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2,"
                + "2, 0, 0, 0, 2, 0, 33, 10, 10, 10, 10, 0, 10, 10, 2,"
                + "2, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,"
                + "33, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 38";
          System.out.println("Maze Number: "+rms_mazemap.writeMazeMapData(strMap2));

        String strMap3 = "23, 10, 10, 10, 10, 10, 18, 10, 10, 10, 10, 10, 10, 10, 28,"+
                "0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2,"+
                "23, 10, 10, 10, 28, 0, 0, 0, 10, 10, 10, 10, 10, 0, 2,"+
                "2, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 10, 2,"+
                "2, 0, 2, 0, 2, 0, 2, 0, 10, 10, 10, 10, 10, 0, 2,"+
                "2, 0, 2, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2,"+
                "2, 0, 2, 0, 2, 0, 2, 0, 10, 10, 10, 10, 10, 0, 2,"+
                "2, 0, 2, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 10, 2,"+
                "2, 0, 0, 0, 0, 0, 0, 0, 10, 10, 10, 10, 10, 0, 2,"+
                "2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,"+
                "2, 0, 23, 10, 18, 0, 0, 23, 18, 29, 0, 18, 10, 0, 2,"+
                "2, 0, 2, 0, 2, 0, 0, 34, 2, 38, 0, 2, 0, 0, 2,"+
                "2, 0, 2, 0, 2, 0, 0, 0, 2, 0, 0, 2, 0, 0, 2,"+
                "2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 0, 2, 0, 0, 2,"+
                "2, 0, 2, 0, 0, 0, 0, 0, 2, 0, 23, 38, 0, 0, 2,"+
                "2, 0, 34, 0, 10, 10, 10, 10, 38, 0, 34, 10, 10, 0, 2,"+
                "2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,"+
                "33, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 38";
        System.out.println("Maze Number: " + rms_mazemap.writeMazeMapData(strMap3));
        
        String strMap4 = "23, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 28,"+
                "0, 0, 0, 2, 0, 0, 2, 0, 0, 2, 0, 0, 2, 0, 2,"+
                "23, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,"+
                "2, 0, 23, 18, 28, 0, 23, 18, 28, 0, 23, 18, 28, 0, 2,"+
                "2, 0, 2, 2, 2, 0, 2, 2, 2, 0, 2, 2, 2, 0, 2,"+
                "2, 0, 2, 2, 2, 0, 2, 2, 2, 0, 2, 2, 2, 0, 2,"+
                "2, 0, 34, 10, 39, 0, 34, 10, 39, 0, 34, 10, 39, 0, 2,"+
                "2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,"+
                "2, 0, 0, 10, 0, 0, 0, 10, 0, 0, 0, 10, 0, 0, 2,"+
                "2, 0, 10, 10, 10, 0, 10, 10, 10, 0, 10, 10, 10, 0, 2,"+
                "2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,"+
                "2, 0, 23, 10, 10, 28, 0, 2, 0, 23, 10, 10, 28, 0, 2,"+
                "2, 0, 2, 2, 2, 2, 0, 2, 0, 2, 2, 2, 2, 0, 2,"+
                "2, 0, 2, 2, 2, 2, 0, 0, 0, 2, 2, 2, 2, 0, 2,"+
                "2, 0, 2, 2, 2, 2, 0, 2, 0, 2, 2, 2, 2, 0, 2,"+
                "2, 0, 34, 10, 10, 38, 0, 2, 0, 34, 10, 10, 40, 0, 2,"+
                "2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,"+
                "34, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 39";
        System.out.println("Maze Number: " + rms_mazemap.writeMazeMapData(strMap4));
        
        String strMap5 = "23, 10, 10, 10, 10, 10, 10, 10, 10, 10, 18, 10, 10, 10, 28,"+
                "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 34, 10, 10, 0, 2,"+
                "23, 0, 10, 10, 10, 10, 10, 10, 0, 0, 0, 0, 0, 0, 2,"+
                "2, 0, 0, 0, 0, 0, 0, 0, 0, 10, 10, 10, 10, 10, 2,"+
                "2, 0, 10, 18, 10, 10, 10, 10, 0, 0, 0, 0, 0, 0, 2,"+
                "2, 0, 0, 2, 0, 0, 0, 0, 0, 10, 10, 10, 28, 0, 2,"+
                "2, 0, 0, 2, 0, 0, 23, 28, 0, 0, 0, 0, 2, 0, 2,"+
                "2, 0, 0, 2, 0, 0, 2, 2, 0, 0, 0, 0, 2, 0, 2,"+
                "2, 0, 0, 0, 0, 0, 2, 2, 0, 10, 10, 10, 38, 0, 2,"+
                "2, 0, 23, 10, 10, 0, 34, 38, 0, 0, 0, 0, 0, 0, 2,"+
                "2, 0, 2, 0, 0, 0, 0, 0, 0, 23, 10, 29, 0, 0, 2,"+
                "2, 0, 34, 18, 10, 10, 10, 10, 0, 2, 0, 2, 0, 0, 2,"+
                "2, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 2,"+
                "2, 0, 0, 0, 0, 23, 10, 28, 0, 2, 0, 34, 10, 0, 2,"+
                "2, 10, 28, 0, 0, 2, 0, 2, 0, 2, 0, 0, 0, 0, 2,"+
                "2, 23, 38, 0, 0, 2, 0, 38, 0, 34, 0, 23, 10, 10, 2,"+
                "2, 34, 10, 10, 10, 39, 0, 0, 0, 0, 0, 0, 0, 0, 2,"+
                "33, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 38";
        System.out.println("Maze Number: " + rms_mazemap.writeMazeMapData(strMap5));
        
        String strMap6 = "23, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 29,"+
                "0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2,"+
                "23, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2,"+
                "2, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 2, 0, 2,"+
                "2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2,"+
                "2, 0, 2, 0, 0, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2,"+
                "2, 0, 2, 0, 2, 10, 2, 0, 0, 0, 0, 0, 2, 0, 2,"+
                "2, 0, 0, 0, 2, 0, 2, 0, 2, 0, 2, 0, 0, 0, 2,"+
                "2, 0, 2, 0, 2, 0, 0, 0, 2, 10, 2, 0, 2, 0, 2,"+
                "2, 0, 2, 0, 0, 0, 2, 0, 2, 0, 2, 0, 2, 10, 2,"+
                "2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2,"+
                "2, 0, 2, 10, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2,"+
                "2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2,"+
                "2, 0, 0, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 10, 2,"+
                "2, 0, 2, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 2,"+
                "2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2,"+
                "2, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,"+
                "34, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 38";
        System.out.println("Maze Number: " + rms_mazemap.writeMazeMapData(strMap6));
    }

    public void startApp() {

        display.setCurrent(form);
        createMazeMaps();
        createNPCDatabase();
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
            //reset();
            frmPC = createCharacterChoiceForm();
            display.setCurrent(frmPC);

        } else if (cmd == cmdEndGame) {
            try {
                this.destroyApp(true);
                notifyDestroyed();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        
        if(cmd==cmdChooseCharacter){
            reset();
            
        }
        
        if(cmd==cmdCreateCharacter){
            formCreateCharacter = createCharacterForm();
            display.setCurrent(formCreateCharacter);
        }
        
        if(cmd==cmdSaveCharacter){
//                private TextField txtName;
//    private TextField txtBackground;
//    private TextField txtSkillPoints;
//    private ChoiceGroup cgHackingSkills;
            pc.setName(txtName.getString());
            pc.setBackground(txtBackground.getString());
            
            //iteratate the skills list
            Vector vecHS = new Vector();
            for(int i =0;i<cgHackingSkills.size();i++){
                String strHackLabel = cgHackingSkills.getString(i);
            }
            
                    
            rmsCharacter.writePlayerCharacterData(pc);
            
            
        }
    }

    public void reset() {
        form.deleteAll();
        bluehatCanvas = new BluehatCanvas(display, form, this, pc);
        display.setCurrent(bluehatCanvas);
    }
    
    public Form createCharacterChoiceForm(){
        
       formCharacterList = new Form("Characters");
        
        characterChoiceGroup = new ChoiceGroup("Choose a Character:",Choice.EXCLUSIVE);
        
        
        Vector vecPlayerAvatar = rmsCharacter.readAllPlayerCharacterData();
              
        //Add the values to the characterChoiceGroup.
        Enumeration enumPC = vecPlayerAvatar.elements();
        while(enumPC.hasMoreElements()){
            PlayerAvatar pc = (PlayerAvatar)enumPC.nextElement();
            characterChoiceGroup.append(pc.getName(), null);
        }
        
        characterChoiceGroup.setSelectedIndex(0, true);
        
        cmdExitChooseCharacter = new Command("Exit", Command.EXIT,2);
        cmdChooseCharacter = new Command("Select", Command.OK,1);
        cmdCreateCharacter = new Command("Create", Command.SCREEN,3);
        
        characterChoiceGroupIndex = formCharacterList.append(characterChoiceGroup);
        
        formCharacterList.addCommand(cmdChooseCharacter);
        formCharacterList.addCommand(cmdExitChooseCharacter);
        formCharacterList.addCommand(cmdCreateCharacter);
        formCharacterList.setCommandListener(this);
        
        return formCharacterList;
    }
    
    public Form createCharacterForm(){
        
        formCreateCharacter = new Form("Create Character");
        
        txtName = new TextField("Name:", "John Doe", 50, TextField.ANY);
        txtBackground = new TextField("Background:", null, 256, TextField.ANY);
        txtSkillPoints = new TextField("Skill Points to Spend:","3",2,TextField.UNEDITABLE);
        cgHackingSkills = new ChoiceGroup("Hacking Skills",ChoiceGroup.EXCLUSIVE);
        
        
        cgHackingSkills.append("Hardware Cracking - Level 0", null);
        cgHackingSkills.append("Packet Sniffing - Level 0", null);
        cgHackingSkills.append("Decryption - Level 0", null);
        cgHackingSkills.append("Luck - Level 0", null);
        
        formCreateCharacter.setItemStateListener(new ItemStateListener() {

            public void itemStateChanged(Item item) {
                if (item == cgHackingSkills) {
                    //need to read the skill and add one to the level and remove one from the txtSkillPoints
                    if (Integer.parseInt(txtSkillPoints.getString()) > 0) {
                        int skillpoints = Integer.parseInt(txtSkillPoints.getString()) - 1;
                        txtSkillPoints.setString(String.valueOf(skillpoints));
                        
                        ChoiceGroup cg = (ChoiceGroup)item;
                                                
                        String strNewLevel = cg.getString(cg.getSelectedIndex()).substring(cg.getString(cg.getSelectedIndex()).trim().length() - 1);
                        System.out.println("Skill Level"+strNewLevel);
                        int intNewLabel = Integer.parseInt(strNewLevel) + 1;
                        String strNewLabel;
                        strNewLabel = cg.getString(cg.getSelectedIndex()).trim().substring(0, cg.getString(cg.getSelectedIndex()).length() - 1) + intNewLabel;
                        cg.set(cg.getSelectedIndex(), strNewLabel, null);
                    }
                }
            }

        });
        
             
        
        formCreateCharacter.append(txtName);
        formCreateCharacter.append(txtBackground);
        formCreateCharacter.append(txtSkillPoints);
        formCreateCharacter.append(cgHackingSkills);
        
        
        cmdSaveCharacter = new Command("Save", Command.OK,1);
        cmdExitCreateCharacter = new Command("Exit", Command.BACK,2);
        
        formCreateCharacter.addCommand(cmdSaveCharacter);
        formCreateCharacter.addCommand(cmdExitCreateCharacter);
        
        formCreateCharacter.setCommandListener(this);
        
        return formCreateCharacter;
    }
}
