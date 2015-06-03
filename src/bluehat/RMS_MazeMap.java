/*
 Assignment: TME3
 Assignment Name: Blue Hat
 Assignement Date: June 1st, 2015

 @ author: Andrew Miller
 Student ID: 2433560
 Course: COMP 486

 * This is an object controls the record management system for the mazemaps.
 *
 * @version 1.0


 */
package bluehat;

import javax.microedition.rms.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 * A record for each screen maze.
 *
 * @author MillerAn
 * @version 1.0
 * @created 17-May-2015 2:11:37 PM
 */
public class RMS_MazeMap {

    private static final String REC_STORE = "MazeMapRS";
    private RecordStore rs;
    
    int start;
    int end = -1;

    public RMS_MazeMap() {
        try {
            rs = RecordStore.openRecordStore(REC_STORE, true);
        } catch (RecordStoreException rse) {
            System.out.println(rse.toString());
        }
    }

    /**
     *
     * @exception Throwable
     */
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

    public int[][] readMazeMapData(int mapNumber) {

        int[][] returningMap;
        returningMap = new int[18][15];
        int arrayCounter = 0;
        int start = 0;
        int end = 0;

        try {
            String strMap = new String(rs.getRecord(mapNumber));
            int intMap[] = new int[270];

            while (end > -1) {
                if (end > 0) {
                    start = end + 1;
                }
                end = strMap.indexOf(",", start);
//                System.out.println("Start: " + start);
//                System.out.println("End: " + end);
                

                if (end > -1) {
                    intMap[arrayCounter] = Integer.parseInt(strMap.substring(start, end).trim());
//                    System.out.println("Value: " + strMap.substring(start, end));
                    arrayCounter++;
                }else{
                    //grab the last number from the string.
                    intMap[arrayCounter] = Integer.parseInt(strMap.substring(start).trim());
//                    System.out.println("Value: " + strMap.substring(start));
                }
            }

            //Transfer the 1d array into a 2d array
//            System.out.println("Starting the trasnfer to 2D");
            arrayCounter = 0; //reset for the transfer

            for (int row = 0; row <= 17; row++) {
                for (int col = 0; col <= 14; col++) {
                    returningMap[row][col] = intMap[arrayCounter];
                    arrayCounter++;
//                    System.out.println("Transfer to Cell: " + row + "," + col +","+returningMap[row][col]);
                }
            }

        } catch (RecordStoreException rse) {
            System.out.println(rse.toString());
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

        return returningMap;
    }

    public int writeMazeMapData(String strMap) {
        //This will write the record:
        //of all the tile ids in a comma separated file.
        int intRecordNum = 0;
        try {
            byte[] rec_mazemap = strMap.getBytes();
            intRecordNum = rs.addRecord(rec_mazemap, 0, rec_mazemap.length);
            System.out.println("Record Added");
        } catch (RecordStoreException rse) {
            System.out.println("Error:" + rse.toString());
        }

        return intRecordNum;
    }
}//end RMS_MazeMap
