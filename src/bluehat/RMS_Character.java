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

    public RMS_Character() {
        try {
            rs = RecordStore.openRecordStore(REC_STORE, true);
        } catch (RecordStoreException rse) {
            System.out.println(rse.toString());
        }

    }

    public void finalize() throws Throwable {

    }

    public void closeRecordStore() {
        //Close the record store
        try {
            rs.closeRecordStore();
        } catch (RecordStoreException rse) {
            System.out.println(rse.toString());
        }

    }

    public Vector readAllPlayerCharacterData() {
        Vector vecAllPlayers = new Vector();
        try {
            RecordEnumeration enumPlayers = rs.enumerateRecords(null, null, false);
            while (enumPlayers.hasNextElement()) {
                PlayerAvatar pc = readPlayerCharacterData(enumPlayers.nextRecordId());
                vecAllPlayers.addElement(pc);
            }
            
        } catch (RecordStoreNotOpenException ex) {
            System.out.println(ex.toString());
        } catch (InvalidRecordIDException ex) {
            System.out.println(ex.toString());
        }

        return vecAllPlayers;
    }
    
    public PlayerAvatar readPlayerCharacterData(String playerName){
        PlayerAvatar retrivedPlayer = new PlayerAvatar();
        try{
            TextFilter filter = new TextFilter(playerName);
            RecordEnumeration enumPlayer = rs.enumerateRecords(filter, null, false);
            while(enumPlayer.hasNextElement()){
                retrivedPlayer = readPlayerCharacterData(enumPlayer.nextRecordId());
            }

        }catch(Exception ex){
            ex.toString();
        }
        
        return retrivedPlayer;
    }

    public PlayerAvatar readPlayerCharacterData(int recID)  {
        /*
         This method will find the player character and convert the CSV record into 
         a PlayerAvatar object
         */

        PlayerAvatar pc = new PlayerAvatar();
        try {
            String strPC = new String(rs.getRecord(recID));
            System.out.println("Raw Data:"+strPC);
            Vector vecHackingSkills = new Vector();

            //set the record ID
            pc.setRecordID(recID);

            // get the Name
            start = 0;
            end = strPC.indexOf(",");
            end_skill_level =0;

            pc.setName(strPC.substring(start, end));

            //get the background
            start = end + 1;
            end = strPC.indexOf(",", start);

            pc.setBackground(strPC.substring(start, end));

        //get the hacking skills of the player character
            // they should have a least one skill
            while (strPC.indexOf(",", end_skill_level + 1) != strPC.lastIndexOf(',')) {
                
                if (end_skill_level != 0) {
                    start = end_skill_level + 1;
                } else {
                    start = end + 1;
                }
                end = strPC.indexOf(",", start);
                end_skill_level = strPC.indexOf(",", end + 1);

                HackSkill hackskill = new HackSkill(strPC.substring(start, end), Integer.parseInt(strPC.substring(end + 1, end_skill_level)));
                vecHackingSkills.addElement(hackskill);
                
                if (strPC.indexOf(",", end_skill_level + 1) == strPC.lastIndexOf(',')) {
                    start = end_skill_level + 1;
                    end = strPC.indexOf(",", start);

                    hackskill = new HackSkill(strPC.substring(start, end), Integer.parseInt(strPC.substring(end + 1)));
                    vecHackingSkills.addElement(hackskill);
                }

            }
            
            pc.setVectorHackingSkill(vecHackingSkills);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return pc;
    }

    public int writePlayerCharacterData(PlayerAvatar objPC) {
        int intRecordNum = objPC.getRecordID();
        try {
            byte[] rec_char = objPC.toString().getBytes();
            if (objPC.getRecordID() > 0) {
                rs.setRecord(objPC.getRecordID(), rec_char, 0, rec_char.length);
            } else {
                intRecordNum = rs.addRecord(rec_char, 0, rec_char.length);
            }
        } catch (Exception rse) {

            System.out.println("Error:" + rse.toString());
        }
        return intRecordNum;
    }
}//end RMS_Character
