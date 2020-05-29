/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zero.tls.proplan.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import me.zero.tls.proplan.components.Plan;
import me.zero.tls.proplan.components.Process;
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
public class LadePlanController extends FileFilter implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {    
    	final JFileChooser fc;
    	if(Settings.instance.settings.get(Settings.SETTINGS_KEY.LAST_STORE_PATH.name()) != null && Settings.instance.settings.get(Settings.SETTINGS_KEY.LAST_STORE_PATH.name()).toString().length() > 0) {
    		fc = new JFileChooser(new File(Settings.instance.settings.get(Settings.SETTINGS_KEY.LAST_STORE_PATH.name()).toString()));
    	}else{
    		fc = new JFileChooser();
    	}
        
        fc.addChoosableFileFilter(this);
        fc.setAcceptAllFileFilterUsed(false);
        int returnVal = fc.showOpenDialog(null);
        if(returnVal == 0){
            Settings.instance.change(SETTINGS_KEY.LAST_STORE_PATH, fc.getSelectedFile().getAbsolutePath());
            try{            	
            	if(fc.getSelectedFile().getName().endsWith(".plson")) {
            		
            		//Unzip First
            		final GZIPInputStream gis = new GZIPInputStream(new FileInputStream(fc.getSelectedFile()));
                    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, "UTF-8"));

                    String line = bufferedReader.readLine();
                    
                    StringBuilder json = new StringBuilder();
                    while (line != null) {
                      json.append(line);
                      line = bufferedReader.readLine();
                    }
            		bufferedReader.close();
            		gis.close();            		
            		Gson gson = new GsonBuilder().setPrettyPrinting().create();		            		
            		HashMap<String, Object> data = gson.fromJson(json.toString(), new TypeToken<HashMap<String, Object>>(){}.getType());   
            		Plan plan = new Plan(data.get("name").toString(), data.get("description").toString());
            		 if(ProPlanUI.tabs.tabToPanel.containsKey(plan.getName())){
            			String message = Lang.getLanguageValue(Language.PLANEXISTS).replace("%plan%", plan.getName());
            			MessageUtil.showErrorMessage(Lang.getLanguageValue(Language.TITEL_ERROR), message);
            			return;
            		 }
            		@SuppressWarnings("unchecked")
					ArrayList<Object> nodes = (ArrayList<Object>) data.get("nodes");            		
            		HashMap<Integer, Process> allNodes = new HashMap<Integer, Process>();            		
            		//Processe erstellen
            		for(Object node : nodes) {
            			@SuppressWarnings("unchecked")
						LinkedTreeMap<String, Object> listedNode = (LinkedTreeMap<String, Object>) node;
            			Process process = new Process(
            					Double.valueOf(listedNode.get("duration").toString()), 
            					listedNode.get("description").toString(),
            					Integer.valueOf(listedNode.get("startX").toString()), 
            					Integer.valueOf(listedNode.get("startY").toString()), 
            					Integer.valueOf(listedNode.get("id").toString()),
            					Boolean.valueOf(listedNode.get("isEnd").toString())
            				);
            			allNodes.put(process.getID(), process);
            			plan.addNode(process);
            			System.out.println("adding node with id " + listedNode.get("id").toString());
            		}
            		//Eltern und Kind Processe erstellen
            		for(Object node : nodes) {
            			@SuppressWarnings("unchecked")
						LinkedTreeMap<String, Object> listedNode = (LinkedTreeMap<String, Object>) node;            			
            			Process workingProcess = allNodes.get(Integer.parseInt(listedNode.get("id").toString()));            			
            			if(workingProcess == null) {
            				MessageUtil.showErrorMessage(Lang.getLanguageValue(Language.TITEL_ERROR), Lang.getLanguageValue(Language.PROCESS_NOT_FOUND).replace("%name%", "\"" + listedNode.get("description").toString() + "\""));
            			}            			
            			List<?> childs = (List<?>) listedNode.get("childs");            			
            			for(Object s : childs) {
            				//Suche childProcess   
            				System.out.println(s + " for node " + listedNode.get("description"));
            				Process child = getProcessFromListByID(Integer.parseInt(s.toString().replace(".0","")), plan.nodes); 
            				if(child == null) {
                				MessageUtil.showErrorMessage(Lang.getLanguageValue(Language.TITEL_ERROR), Lang.getLanguageValue(Language.LOAD_CHILD_NOT_FOUND).replace("%id%", s.toString().replace(".0","")).replace("%name%", "\"" + listedNode.get("description").toString() + "\""));
                    			break;
            				}            			
            				System.out.println("adding child " + child.getID() + " to " + workingProcess);
            				workingProcess.addSuccessor(child);       				
            			}            			
            			List<?> parents = (List<?>) listedNode.get("parents");
            			for(Object s : parents) {
            				//Suche childProcess
            				Process parent = getProcessFromListByID(Integer.parseInt(s.toString().replace(".0","")), plan.nodes);      				
            				if(parent == null) {
                				MessageUtil.showErrorMessage(Lang.getLanguageValue(Language.TITEL_ERROR), Lang.getLanguageValue(Language.LOAD_PARENT_NOT_FOUND).replace("%id%", s.toString().replace(".0","")).replace("%name%", "\"" + listedNode.get("description").toString() + "\""));
                				break;
            				}
            				workingProcess.addPredecessor(parent);
            			}
            		}
            		plan.calcData();
            		plan.calcData();
            		ProPlanUI.tabs.addTab(plan);       
            		String message = Lang.getLanguageValue(Language.PLANLOADED).replace("%plan%", plan.getName());
                    MessageUtil.showInformationMessage(Lang.getLanguageValue(Language.TITEL_SUCCESS), message);                    
            	}else {
            		FileInputStream fis = new FileInputStream (fc.getSelectedFile());
                    ObjectInputStream ois = new ObjectInputStream (fis);
                    Plan plan = (Plan) ois.readObject();
                    if(!ProPlanUI.tabs.tabToPanel.containsKey(plan.getName())){
                        ProPlanUI.tabs.addTab(plan);
                        plan.calcData();
                        String message = Lang.getLanguageValue(Language.PLANLOADED).replace("%plan%", plan.getName());
                        MessageUtil.showInformationMessage(Lang.getLanguageValue(Language.TITEL_SUCCESS), message);
                    }else{
                    	String message = Lang.getLanguageValue(Language.PLANEXISTS).replace("%plan%", plan.getName());
                        MessageUtil.showErrorMessage(Lang.getLanguageValue(Language.TITEL_ERROR), message);
        				
                    }
            	}            	                
            }catch(IOException ex){
                ex.printStackTrace();
                MessageUtil.showErrorMessage(Lang.getLanguageValue(Language.TITEL_ERROR), ex.getMessage());
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                MessageUtil.showErrorMessage(Lang.getLanguageValue(Language.TITEL_ERROR), ex.getMessage());
            }
        }
    }
    
    public Process getProcessFromListByID(int id,List<Process> allNodes) {  	
    	for(Process tPro : allNodes){
    		if(tPro.getID() == id) {
    			return tPro;
    		}
    	}    	
    	return null;
    }

   /* public static String decompress(final byte[] compressed) throws IOException {
        final StringBuilder outStr = new StringBuilder();
        if ((compressed == null) || (compressed.length == 0)) {
          return "";
        }
        if (isCompressed(compressed)) {
          final GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
          final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, "UTF-8"));

          String line;
          while ((line = bufferedReader.readLine()) != null) {
            outStr.append(line);
          }
        } else {
          outStr.append(compressed);
        }
        return outStr.toString();
      }*/
    
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        return f.getName().endsWith(".plan") || f.getName().endsWith(".plson");
    }

    @Override
    public String getDescription() {
        return Lang.getLanguageValue(Language.FILTER_PLAN_FILES);
    }
}
