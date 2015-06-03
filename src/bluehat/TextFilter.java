/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

 */
package bluehat;

import javax.microedition.rms.RecordFilter;

/**
 *
 * @author Andrew
 */
public class TextFilter implements RecordFilter {
    private String textToFind=null;
    
    public TextFilter(String text){
        textToFind = text.toLowerCase();
        
    }
    
    public boolean matches(byte[] value){
        String str = new String(value).toLowerCase();
        
        if(textToFind != null && str.indexOf(textToFind)!=-1){
            return true;
        }else{
            return false;
        }
    }
    
}
