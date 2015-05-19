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

	public RMS_NPC(){

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

	/**
	 * 
	 * @param name
	 */
	public NPC readNPCData(String name){
		return null;
	}
}//end RMS_NPC