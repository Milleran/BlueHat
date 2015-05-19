package bluehat;

import javax.microedition.rms.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 * A record for each screen maze.
 * @author MillerAn
 * @version 1.0
 * @created 17-May-2015 2:11:37 PM
 */
public class RMS_MazeMap {

	private static final String REC_STORE = "MazeMapRS";
	private RecordStore rs;

	public RMS_MazeMap(){

	}

	/**
	 * 
	 * @exception Throwable
	 */
	public void finalize()
	  throws Throwable{

	}

	public void closeRecordStore(){

	}

	public void openRecordStore(){

	}

	public String readMazeMapData(){
		return "";
	}
}//end RMS_MazeMap