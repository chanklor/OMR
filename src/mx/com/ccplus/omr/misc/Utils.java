/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ccplus.omr.misc;

import javafx.scene.paint.Color;

/**
 *
 * @author Acer
 */
public class Utils {
    
    public static String getRGBoFromColor(Color color){
        return String.format( "#%02X%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 ),
            (int)( color.getOpacity() * 255)
        );
    }
    
}
