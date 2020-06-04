package me.zero.tls.proplan.settings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import javax.swing.UIManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.zero.tls.proplan.gui.view.MessageUtil;
import me.zero.tls.proplan.lang.Lang;
import me.zero.tls.proplan.lang.Lang.Language;

public class Settings {

	public static Settings instance;
	
	public HashMap<String, Object> settings = new HashMap<String, Object>();
    private static final String foldername = "ProPlan";
    private static String homepath = System.getProperty("user.home") + System.getProperty("file.separator") + foldername;
    private static File dir = new File(homepath);
	private static File configFile = new File(homepath + System.getProperty("file.separator") + "settings.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithModifiers(Modifier.STATIC).create();
	
	public Settings() {
		Settings.instance = this;
		try {
			load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the default Values and overrites them with the file from the settings file
	 * @throws IOException 
	 */
	
	@SuppressWarnings("unchecked")
	public void load() throws IOException {
		
		//Lade default Einstellungen
		loadDefault();
		
		if(!configFile.exists()) {
			configFile.createNewFile();
			save();
		}else {
			//Überschreibe Einstellungen
			BufferedReader fileReader = new BufferedReader(new FileReader(configFile));		
	    	Gson gson = new Gson(); ;
	    	HashMap<String,Object> map = new HashMap<String,Object>();
	    	map = (HashMap<String,Object>) gson.fromJson(fileReader, map.getClass());    
	    	boolean override = map.size() < Settings.instance.settings.size();    	
	    	
	    	for(String key : map.keySet()) {
	    		Settings.instance.settings.put(key, map.get(key));
	    	}
	    	if(override) {
	    		try {
	    			Settings.instance.save();
	    			MessageUtil.showInformationMessage(Lang.getLanguageValue(Language.INFORMATION_TITLE), Lang.getLanguageValue(Language.LANGUAGE_FILE_CHANGED));
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}  
	    	}   
		}
	}
	/**
	 * Loads the default values
	 */
	private void loadDefault() {
		
		settings.put(Settings.SETTINGS_KEY.LOADEXAMPLE.name(), true);
		settings.put(Settings.SETTINGS_KEY.MAX_CHILDS.name(), 10);
		settings.put(Settings.SETTINGS_KEY.MAX_PARENTS.name(), 10);
		settings.put(Settings.SETTINGS_KEY.LAST_STORE_PATH.name(), "");
		
	}
	/**
	 * Writes the Settings Hashmap to the disc
	 * @throws IOException
	 */
	public void save() throws IOException {
		//Exists Dir
		if(!dir.exists()) {
			dir.mkdirs();
			configFile.createNewFile();
			//Exists Lang File
		}else if(!configFile.exists()) {
			configFile.createNewFile();
		}
		//Speichern der Werte
		//CUIConfiguration config = CUIConfiguration.GSON.fromJson(fileReader, CUIConfiguration.class);
				
		String json = GSON.toJson(instance.settings);
		BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
		writer.write(json);
		writer.flush();
		writer.close();
	}
	/**
	 * Chages a Setting by the give key
	 * @param key instance if SETTINGS_KEY
	 * @param obj the Object to store
	 * @return true if the Object returned by HashMap.put is the same as the given object
	 */
	public boolean change(SETTINGS_KEY key,Object obj) {
		try {
			Settings.instance.settings.put(key.name(), obj);
			save();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public enum SETTINGS_KEY{
		LOADEXAMPLE,
		MAX_CHILDS,
		MAX_PARENTS,
		LAST_STORE_PATH;
	}
}
