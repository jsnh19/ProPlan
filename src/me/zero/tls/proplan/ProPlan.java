package me.zero.tls.proplan;

import me.zero.tls.proplan.gui.view.ProPlanUI;
import me.zero.tls.proplan.lang.Lang;
import me.zero.tls.proplan.settings.Settings;

public class ProPlan {

	public static boolean isDebug = true;
        
	/**
	 * Init Methode
	 * @param args (Commandline Parameters)
	 */
	public static void main(String[] args) {
		new Lang();
		System.out.println("loading settings....");
		new Settings();
		new ProPlanUI();		
	}
	
	/**
	 * Returns a String representing the Author of this Program
	 * @return String representing the author
	 */
	public static String getAuthor() {
		return "Julius Schönhut (2020)";
	}
	/**
	 * Returns a String representing the Version of this Program
	 * @return String representing the version
	 */
	public static String getVersion() {
		return "15052020";
	}
}
