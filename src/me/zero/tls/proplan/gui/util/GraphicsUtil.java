/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zero.tls.proplan.gui.util;

import javax.swing.ImageIcon;
import me.zero.tls.proplan.gui.components.Tabs;

/**
 *
 * @author Julius.Schoenhut
 */
public class GraphicsUtil {
    
    /**
     * Returns an ImageIcon, or null if the path was invalid.
     * @param path The Link to the Image
     * @return instance of ImageIcon
     */
    public static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Tabs.class.getResource(path);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            return icon;
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
}
