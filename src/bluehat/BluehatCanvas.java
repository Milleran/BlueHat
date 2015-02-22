/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bluehat;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.GameCanvas;

/**
 *
 * @author Andrew
 */



public class BluehatCanvas extends GameCanvas implements Runnable{
    private Graphics graphics;
    private Image imageSplash;
    private Image imageSplashBackground;
    
    public BluehatCanvas(String strTitle){
        super(true);
        setTitle(strTitle);
    }
    
    public void start(){
        this.showSplash();
    }

    public void run() {
       
    }
    
    private void showSplash(){
        graphics = getGraphics();
        imageSplash = createImage("/bluehat75x75.png");
        
        graphics.drawImage(imageSplash, 90, 25, 0);
        
        Font fontSplash = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE);
   
        graphics.setFont(fontSplash);
        graphics.drawString("A Hacker Adventure",60,125,0);
    }
        
    Image createImage(String strFileName){
        Image tempImage = null;
        try{
            tempImage = Image.createImage(strFileName);
        }catch(Exception ioe){
            System.out.println(ioe.toString());
        }
        return tempImage;
    }
}

