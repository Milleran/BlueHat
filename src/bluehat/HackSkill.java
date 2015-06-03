/*
 Assignment: TME3
 Assignment Name: Blue Hat
 Assignement Date: June 1st, 2015

 @ author: Andrew Miller
 Student ID: 2433560
 Course: COMP 486

 * This is an object that holds the information about a hacking skill.
 *
 * @author MillerAn
 * @version 1.0


 */
package bluehat;

public class HackSkill {

    private int skillLevel;
    private String skillName;

    public HackSkill() {

    }

    public HackSkill(String strSkillName, int intSkillLevel) {
        skillLevel = intSkillLevel;

        skillName = strSkillName;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public String getSkillName() {
        return skillName;
    }

    public String toString() {

        return skillName + " - " + skillLevel;
    }

    public void finalize() throws Throwable {

    }
}//end HackSkill
