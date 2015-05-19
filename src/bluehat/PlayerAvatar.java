package bluehat;

import java.util.Vector;

/**
 * @author MillerAn
 * @version 1.0
 * @created 17-May-2015 11:36:55 AM
 */
public class PlayerAvatar {

    private String background;
    private Vector vectorHackingSkill = new Vector();
    private String name;
    private int recordID;

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public PlayerAvatar() {

    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Vector getVectorHackingSkill() {
        return vectorHackingSkill;
    }

    public void setVectorHackingSkill(Vector vectorHackingSkill) {
        this.vectorHackingSkill = vectorHackingSkill;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void finalize() throws Throwable {

    }

    public String toString() {
        String strReturn = this.getName() + "," + this.getBackground();

        for (int i = 0; i < this.getVectorHackingSkill().size(); i++) {
            strReturn += "," + ((HackSkill) this.getVectorHackingSkill().elementAt(i)).getSkillName();
            strReturn += "," + ((HackSkill) this.getVectorHackingSkill().elementAt(i)).getSkillLevel();
        }

        return strReturn;
    }
}//end PlayerAvatar
