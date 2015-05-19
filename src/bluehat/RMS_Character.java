package bluehat;

import javax.microedition.rms.*;
import java.util.Enumeration;
import java.util.Vector;


/**
 * @author MillerAn
 * @version 1.0
 * @created 17-May-2015 11:23:01 AM
 */
public class RMS_Character {

    private static final String REC_STORE = "PlayerCharacterRS";
    private RecordStore rs;

    int start;
    int end;
    int end_skill_level;

    public RMS_Character(){
        try{
            rs = RecordStore.openRecordStore(REC_STORE, true);
        }catch(RecordStoreException rse){
            System.out.println(rse.toString());
        }

    }

    public void finalize() throws Throwable {

    }

    public void closeRecordStore() throws Exception {
        //Close the record store
        rs.closeRecordStore();

    }

    public PlayerAvatar readPlayerCharacterData(int recID) throws Exception {
        /*
         This method will find the player character and convert the CSV record into 
         a PlayerAvatar object
         */
        PlayerAvatar pc = new PlayerAvatar();

        String strPC = new String(rs.getRecord(recID));
        System.out.println(strPC);
        Vector vecHackingSkills = new Vector();

        //set the record ID
        pc.setRecordID(recID);

        // get the Name
        start = 0;
        end = strPC.indexOf(",");

        pc.setName(strPC.substring(start, end));
        System.out.println("Name:" +pc.getName());

        //get the background
        start = end+1;
        end = strPC.indexOf(",", start);
        
        pc.setBackground(strPC.substring(start, end));
        
        System.out.println("Background:" +pc.getBackground());

            //get the hacking skills of the player character
        // they should have a least one skill
        
        while (strPC.indexOf(",",end_skill_level + 1) != strPC.lastIndexOf(',')){
            if (end_skill_level != 0) {
                start = end_skill_level + 1;
            } else {
                start = end + 1;
            }
            end = strPC.indexOf(",", start);
            end_skill_level = strPC.indexOf(",", end+1);
                                  
            HackSkill hackskill = new HackSkill(strPC.substring(start, end), Integer.parseInt(strPC.substring(end + 1, end_skill_level)));
            vecHackingSkills.addElement(hackskill);
            
            if(strPC.indexOf(",",end_skill_level + 1) == strPC.lastIndexOf(',')){
                start = end_skill_level+1;
                end = strPC.indexOf(",", start);

                hackskill = new HackSkill(strPC.substring(start, end), Integer.parseInt(strPC.substring(end+1)));
                vecHackingSkills.addElement(hackskill);
            }
            
        }

        pc.setVectorHackingSkill(vecHackingSkills);
        
        return pc;
    }

    public int writePlayerCharacterData(PlayerAvatar objPC) {
        int intRecordNum = objPC.getRecordID();
        try {
            byte[] rec_char = objPC.toString().getBytes();
            if(objPC.getRecordID() > 0){
                rs.setRecord(objPC.getRecordID(), rec_char, 0, rec_char.length);
            }else{
                intRecordNum = rs.addRecord(rec_char, 0, rec_char.length);
            }
        } catch (Exception rse) {
            
            System.out.println("Error:"+rse.toString());
        }
        return intRecordNum;
    }
}//end RMS_Character
