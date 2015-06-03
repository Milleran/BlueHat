/*
 Assignment: TME3
 Assignment Name: Blue Hat
 Assignement Date: June 1st, 2015

 @ author: Andrew Miller
 Student ID: 2433560
 Course: COMP 486

 * This is an object that holds the information about a NPC like a firewall.
 *
 * @author MillerAn
 * @version 1.0

 */
package bluehat;


public class NPC {

    private HackSkill hack_attack;
    private String name;
    private int security_defense_level;


    private String image_name;
    
    private int recordID;
    public NPC(){
        
    }
    public NPC(int recID, String strName, int sec_def_level, String img_name, HackSkill hackSkill) {
        hack_attack = hackSkill;
        name = strName;
        image_name = img_name;
        security_defense_level = sec_def_level;
        recordID = recID;
    }

    public void finalize() throws Throwable {

    }

    public HackSkill getHack_attack() {
        return hack_attack;
    }

    public int getRecordID() {
        return recordID;
    }

    public String getName() {
        return name;
    }

    public int getSecurity_defense_level() {
        return security_defense_level;
    }

    public void setHack_attack(HackSkill hack_attack) {
        this.hack_attack = hack_attack;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSecurity_defense_level(int security_defense_level) {
        this.security_defense_level = security_defense_level;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String toString() {
        String strReturn = this.getName() + "," + this.getSecurity_defense_level() + ","+ this.getImage_name()+ "," + this.getHack_attack().getSkillName() + "," + this.getHack_attack().getSkillLevel();

        return strReturn;
    }
}//end NPC
