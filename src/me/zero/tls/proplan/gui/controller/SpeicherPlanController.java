/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zero.tls.proplan.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPOutputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import me.zero.tls.proplan.components.Plan;
import me.zero.tls.proplan.gui.view.MessageUtil;
import me.zero.tls.proplan.gui.view.ProPlanUI;
import me.zero.tls.proplan.lang.Lang;
import me.zero.tls.proplan.lang.Lang.Language;
import me.zero.tls.proplan.settings.Settings;
import me.zero.tls.proplan.settings.Settings.SETTINGS_KEY;

/**
 *
 * @author Julius.Schoenhut
 */
public class SpeicherPlanController extends FileFilter implements ActionListener{

	@Override
    public void actionPerformed(ActionEvent e) {        
		
		if(ProPlanUI.tabs.tabbedPane.getSelectedIndex() < 0) {			
			MessageUtil.showErrorMessage(Lang.getLanguageValue(Language.TITEL_ERROR), Lang.getLanguageValue(Language.PLANNOTFOUND));			
			return;
		}
		
        Plan plan = ProPlanUI.tabs.tabToPanel.get(ProPlanUI.tabs.tabbedPane.getTitleAt(ProPlanUI.tabs.tabbedPane.getSelectedIndex()));
        
        final JFileChooser fc;
    	if(Settings.instance.settings.get(Settings.SETTINGS_KEY.LAST_STORE_PATH.name()) != null && Settings.instance.settings.get(Settings.SETTINGS_KEY.LAST_STORE_PATH.name()).toString().length() > 0) {
    		fc = new JFileChooser(new File(Settings.instance.settings.get(Settings.SETTINGS_KEY.LAST_STORE_PATH.name()).toString()));
    	}else{
    		fc = new JFileChooser();
    	}
    	fc.setDialogTitle(Lang.getLanguageValue(Language.SAVE));
    	fc.addChoosableFileFilter(this);
    	if(plan.saveFileName == null) {
    		fc.setSelectedFile(new File(plan.getName() + ".plson"));
    	}else {
    		fc.setSelectedFile(new File(plan.saveFileName));
    	}
    	
    	fc.addChoosableFileFilter(new FileFilter() {
			
   		 @Override
   		    public boolean accept(File f) {
   		        if (f.isDirectory()) {
   		            return true;
   		        }
   		        return f.getName().endsWith(".plan");
   		    }

   		    @Override
   		    public String getDescription() {
   		        return Lang.getLanguageValue(Language.FILTER_PLAN_FILES_SECOND);
   		    }
		});
        fc.setAcceptAllFileFilterUsed(false);
        int returnVal = fc.showSaveDialog(null);
        if(returnVal == 0){
        	String filter = fc.getFileFilter() == this ? "plson":"plan";
        	Settings.instance.change(SETTINGS_KEY.LAST_STORE_PATH, fc.getSelectedFile().getAbsolutePath());
            try {
            	FileOutputStream fos = null;
            	if(filter.equalsIgnoreCase("plan")) {
            		if(!fc.getSelectedFile().getAbsolutePath().endsWith(".plan")) {
            			fos = new FileOutputStream(new File(fc.getSelectedFile().getAbsolutePath() + ".plan"));
            			plan.saveFileName = fc.getSelectedFile().getAbsolutePath() + ".plan";
            		}else {
            			fos = new FileOutputStream(fc.getSelectedFile());
            			plan.saveFileName = fc.getSelectedFile().getName();
            		}            		
            		//fos = new FileOutputStream(fc.getSelectedFile());
            		ObjectOutputStream oos = new ObjectOutputStream (fos);
                    oos.writeObject(plan);
                    oos.flush();
                    oos.close();
            	}else if(filter.equalsIgnoreCase("plson")) {
            		//Json Ausgaube des Planes
            		GZIPOutputStream zipOut;
            		if(!fc.getSelectedFile().getAbsolutePath().endsWith(".plson")) {
            			zipOut = new GZIPOutputStream(new FileOutputStream(new File(fc.getSelectedFile().getAbsolutePath() + ".plson")));    
                    	plan.saveFileName = fc.getSelectedFile().getAbsolutePath() + ".plson";
            		}else {
            			zipOut = new GZIPOutputStream(new FileOutputStream(fc.getSelectedFile()));  
            			plan.saveFileName = fc.getSelectedFile().getName();
            		}            		        		
            		zipOut.write(plan.asJson().getBytes("UTF-8")); 
            		zipOut.flush();
            		zipOut.close();
            	}
            	plan.changed = false;
            	plan.setChangedMarker();
            	MessageUtil.showInformationMessage(Lang.getLanguageValue(Language.TITEL_SUCCESS), Lang.getLanguageValue(Language.SUCCESSFULLY_SAVED));
            }catch(IOException ex){
                MessageUtil.showErrorMessage(ex.getMessage(), Lang.getLanguageValue(Language.TITEL_ERROR));
                ex.printStackTrace();
            }
        }
    } 

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        return f.getName().endsWith(".plson")  ;
    }

    @Override
    public String getDescription() {
        return Lang.getLanguageValue(Language.FILTER_PLAN_FILES);
    }
}
