package me.zero.tls.proplan.gui.view;

import java.awt.Dimension;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class MessageUtil {

	private static Dimension preDimension;
	
	
	/**
	 * Displays a JOptionPane Error Message
	 * @param title the title of the JOptionPane
	 * @param message title the body of the JOptionPane
	 */
	public static void showErrorMessage(String title,String message) {
		preDimension = (Dimension)UIManager.get("OptionPane.minimumSize");
        UIManager.put("OptionPane.minimumSize",new Dimension(262,90));         
        JOptionPane.showMessageDialog(
        		null, 
        		message,
        		title,
        		JOptionPane.ERROR_MESSAGE
        );       
        UIManager.put("OptionPane.minimumSize",preDimension); 
	}
	/**
	 * Displays a JOptionPane Information Message
	 * @param title title the title of the JOptionPane
	 * @param message title the body of the JOptionPane
	 */
	public static void showInformationMessage(String title,String message) {
		preDimension = (Dimension)UIManager.get("OptionPane.minimumSize");
        UIManager.put("OptionPane.minimumSize",new Dimension(262,90));         
        JOptionPane.showMessageDialog(
        		null, 
        		message,
        		title,
        		JOptionPane.INFORMATION_MESSAGE
        );       
        UIManager.put("OptionPane.minimumSize",preDimension); 
	}
	
}
