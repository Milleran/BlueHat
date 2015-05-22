package bluehat;

import javax.microedition.rms.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @author MillerAn
 * @version 1.0
 * @created 17-May-2015 2:11:32 PM
 */
public class RMS_NPC {

    private static final String REC_STORE = "NPCRS";
    private RecordStore rs;

    int start;
    int end;
    int end_skill_level;

    public RMS_NPC() {
        try {
            rs = RecordStore.openRecordStore(REC_STORE, true);
        } catch (RecordStoreException rse) {
            System.out.println(rse.toString());
        }
    }

    public void finalize()
            throws Throwable {

    }

    public void closeRecordStore() {
        try {
            rs.closeRecordStore();
        } catch (RecordStoreException rse) {
            System.out.println(rse.toString());
        }
    }
    

    /**
     *
     * @param name
     */
    public NPC readNPCData(String npc_name) {
        //The record for an NPC will be name, security defense number, hacking skills
        /*
         This method will find the player character and convert the CSV record into 
         a PlayerAvatar object
         */

        NPC npc = new NPC();
        String strNPCRec = "";
        int recID = 0;

        try {
            TextFilter filter = new TextFilter(npc_name);
            RecordEnumeration npcEnum = rs.enumerateRecords(filter, null, true);

            while (npcEnum.hasNextElement()) {
                recID = npcEnum.nextRecordId();
                strNPCRec = new String(rs.getRecord(recID));
            }

        } catch (RecordStoreNotOpenException rsnoe) {
            System.out.println(rsnoe.toString());
        } catch (InvalidRecordIDException ire) {
            System.out.println(ire.toString());
        } catch (RecordStoreException res) {
            System.out.println(res.toString());
        }

        //set the record ID
        npc.setRecordID(recID);

        // get the Name
        start = 0;
        end = strNPCRec.indexOf(",");

        npc.setName(strNPCRec.substring(start, end));

        //get the security level
        start = end + 1;
        end = strNPCRec.indexOf(",", start);

        npc.setSecurity_defense_level(Integer.parseInt(strNPCRec.substring(start, end)));
        //get the image name of the defense
        start = end + 1;
        end = strNPCRec.indexOf(",", start);

        npc.setImage_name(strNPCRec.substring(start, end));
                
        //get the hack skill
        start = end + 1;
        end = strNPCRec.indexOf(",", start);

        HackSkill hs = new HackSkill(strNPCRec.substring(start, end), Integer.parseInt(strNPCRec.substring(end + 1)));
        npc.setHack_attack(hs);

        return npc;
    }

    public int writeNPCData(NPC objNPC) {
        //This will write the record:
        //name, security defense number, hack skill, hack skill level

        int intRecordNum = objNPC.getRecordID();
        try {
            byte[] rec_npc = objNPC.toString().getBytes();
            if (objNPC.getRecordID() > 0) {
                rs.setRecord(objNPC.getRecordID(), rec_npc, 0, rec_npc.length);
            } else {
                intRecordNum = rs.addRecord(rec_npc, 0, rec_npc.length);
            }
        } catch (Exception rse) {

            System.out.println("Error:" + rse.toString());
        }
        return intRecordNum;
    }
}//end RMS_NPC
