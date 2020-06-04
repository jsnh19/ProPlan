/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zero.tls.proplan.lang;

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

/**
 *
 * @author Julius.Schoenhut
 */
public class Lang {
    
    private HashMap<String,String> messages = new HashMap<>();
    private static final String foldername = "ProPlan";
    private static String homepath = System.getProperty("user.home") + System.getProperty("file.separator") + foldername;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithModifiers(Modifier.STATIC).create();
    private static File dir = new File(homepath);
	private static File langFile = new File(homepath + System.getProperty("file.separator") + "language.json");
    private static Lang instance;
    
    public Lang(){
    	Lang.instance = this;
    	if(existLangFile()) {
			loadDefault();
    		try {
				load();
			} catch (FileNotFoundException e) {
				loadDefault();
				MessageUtil.showErrorMessage(getLanguageValue(Language.TITEL_ERROR), e.getMessage());
				e.printStackTrace();
			}
    	}else {
    		loadDefault();
    		try {
    			save();
			} catch (IOException e) {
				loadDefault();
				MessageUtil.showErrorMessage(getLanguageValue(Language.TITEL_ERROR), e.getMessage());
				e.printStackTrace();
			}
    	}        
    }
    @SuppressWarnings("unchecked")
	public void load() throws FileNotFoundException {
    	BufferedReader fileReader = new BufferedReader(new FileReader(langFile));		
    	Gson gson = new Gson(); ;
    	HashMap<String,String> map = new HashMap<String,String>();
    	map = (HashMap<String,String>) gson.fromJson(fileReader, map.getClass());    
    	boolean override = map.size() < Lang.instance.messages.size();    	
    	
    	for(String key : map.keySet()) {
    		Lang.instance.messages.put(key, map.get(key));
    	}
    	if(override) {
    		try {
    			Lang.instance.save();
    			MessageUtil.showInformationMessage(Lang.getLanguageValue(Language.INFORMATION_TITLE), Lang.getLanguageValue(Language.LANGUAGE_FILE_CHANGED));
    			System.out.println(UIManager.get("OptionPane.minimumSize"));
    		} catch (IOException e) {
    			e.printStackTrace();
    		}  
    	}    	  	
    }
    public boolean existLangFile() {
    	return langFile.exists();
    }
	public void save() throws IOException {		
		//Exists Dir
		if(!dir.exists()) {
			dir.mkdirs();
			langFile.createNewFile();
		//Exists Lang File
		}else if(!langFile.exists()) {
			langFile.createNewFile();
		}
		//Speichern der Werte
		//CUIConfiguration config = CUIConfiguration.GSON.fromJson(fileReader, CUIConfiguration.class);
		
		String json = GSON.toJson(instance.messages);
		BufferedWriter writer = new BufferedWriter(new FileWriter(langFile));
		writer.write(json);
		writer.flush();
		writer.close();
	}
    /**
     * Loads the default language, German
     */
    private void loadDefault(){
        messages.put(Language.NEW_PLAN_NAME.name(), "Name des Plans");
        messages.put(Language.NEW_PLAN_DESCRIPTION.name(), "Beschreibung zum Plan");
        messages.put(Language.SAVE.name(), "Speichern");
        messages.put(Language.LOAD.name(), "laden");
        messages.put(Language.CANCEL.name(), "abbrechen");
        messages.put(Language.TITEL_NEW_PLAN.name(), "Neuen Plan erstellen");
        messages.put(Language.TITEL_NEW_PROCESS.name(), "Neuen Prozess erstellen");
        messages.put(Language.TITEL_EDIT_PROCESS.name(), "Plan editieren");
        messages.put(Language.TITEL_ERROR.name(), "Fehler");
        messages.put(Language.TITEL_SUCCESS.name(), "Erfolg");
        messages.put(Language.ERROR_PLAN_NAME_LENGTH.name(), "Bitte geben Sie einen validen Plan Namen ein, doppelte sind nicht erlaubt!");
        messages.put(Language.ERROR_PROCESS_NAME_LENGTH.name(), "Bitte geben Sie einen validen Prozess Namen ein!");
        messages.put(Language.ERROR_PROCESS_DURATION.name(), "Die Dauer des Prozesses ist wohl keine Zahl!");
        messages.put(Language.ERROR_PROCESS_ID.name(), "Die Prozess ID ist keine Zahl oder existiert bereits!");
        
        messages.put(Language.ALLREADY_A_CHILD.name(), "%newprocess% ist bereits ein Kind von %process%");
        messages.put(Language.ALLREADY_A_PARENT.name(), "%newprocess% ist bereits ein Elternteil von %process%");
        messages.put(Language.ALLREADY_A_PARENT.name(), "%newprocess% ist bereits ein Elternteil von %process%");
        messages.put(Language.GENERAL_SETTING.name(), "Allgemein");
        messages.put(Language.PREDECESSOR.name(), "Vorgänger");

        messages.put(Language.PROCESSNAME.name(), "Prozessname");
        messages.put(Language.PROCESSDURATION.name(), "Dauer");
        messages.put(Language.PROCESSID.name(), "Prozess ID");
        

        messages.put(Language.TOOLTIP_LOAD.name(), "Laden");
        messages.put(Language.TOOLTIP_SAVE.name(), "Speichern");
        messages.put(Language.TOOLTIP_EDIT.name(), "Editieren");
        messages.put(Language.TOOLTIP_DELETE.name(), "Löschen");

        messages.put(Language.HEADERADDPROCESS.name(), "Vorgängerprozess zum Hinzufügen auswählen");
        messages.put(Language.PLANEXISTS.name(), "Der Plan %plan% existiert bereits");
        messages.put(Language.PLANLOADED.name(), "Der Plan %plan% wurde geladen!");
        messages.put(Language.PLANNOTFOUND.name(), "Es wurde kein Plan ausgewählt!");

        messages.put(Language.FILTER_PLAN_FILES.name(), "ProPlan File (.plson)");
        messages.put(Language.FILTER_PLAN_FILES_SECOND.name(), "ProPlan File (.plan)");
        messages.put(Language.UNKNOWN_COMMAND.name(), "Unbekannte Wahl %command%");
        messages.put(Language.ADD.name(), "Hinzufügen");
        messages.put(Language.REMOVE.name(), "Löschen");

        messages.put(Language.SUCCESSFULLY_SAVED.name(), "Datei wurde erfolgreich gespeichert");

        messages.put(Language.INFORMATION_SELECTED_PLAN_TITLE.name(), "Informationen zum Plan");
        messages.put(Language.INFORMATION_SYSTEM_TITLE.name(), "Informationen zu ProPlan");
        messages.put(Language.INFORMATION_AUTHOR.name(), "Autor");
        messages.put(Language.INFORMATION_VERSION.name(), "Version");
        messages.put(Language.INFORMATION_USAGE.name(), "Benutzung");
        messages.put(Language.INFORMATION_USAGE_TEXT.name(), getHelpText());
        messages.put(Language.INFORMATION_TITLE.name(), "ProPlan Hilfe");
        
        messages.put(Language.LOAD_PARENT_NOT_FOUND.name(), "Es konnte kein Node mit dem Namen %name%, childID = %id% gefunden werden. (Lade Parents)");
        messages.put(Language.LOAD_CHILD_NOT_FOUND.name(), "Es konnte kein Node mit dem Namen %name%, childID = %id% gefunden werden. (Lade Childs)");
        messages.put(Language.PROCESS_NOT_FOUND.name(), "Es konnte kein Node mit dem Namen %name% gefunden werden.");
        
        messages.put(Language.UNSAFED_CHANGES_TITLE.name(), "Beenden ?");
        messages.put(Language.UNSAFED_CHANGES.name(), "Sie haben den Plan '%plan%' noch nicht gepspeichert, beenden?");
        messages.put(Language.UNSAFED_CHANGES_REMOVE.name(), "Sie haben den Plan '%plan%' noch nicht gepspeichert, Plan schließen?");
        messages.put(Language.LANGUAGE_FILE_CHANGED.name(), "Ihnen fehlt anscheinend eine Sprachvariabele, das versuche ich zu fixen.");
        
        messages.put(Language.NO_END_DEFINED.name(), "Es wurde kein Endprocess definiert, dies ist jedoch für die Berechnung wichtig!");
        

        
    }
    /**
     * Returns the translation of the give Enum
     * @param lang {@link Language}
     * @return the right translation or an error String
     */
    public static String getLanguageValue(Language lang){        
        if(Lang.instance.messages.containsKey(lang.name())){
            return Lang.instance.messages.get(lang.name());
        }
        return "Error, coudln't load value for " + lang.toString();
    }
    /**
     * Creates the help text
     * @return
     */
    private String getHelpText() {
    	StringBuilder builder = new StringBuilder();
    	
    	builder.append("\t\t\t#### HilfeText von ProPlan ####\n");
    	builder.append("\n1) Erstellen eines neuen Planes\n\n");
    	builder.append("\tZum erstellen eines neuen Planes klicken Sie bitte auf das Dreieck nebem dem Plus und wählen Sie \"neuen Plan erstellen\"\n");
    	builder.append("\tGeben Sie hier einen Namen und eine Beschreibung an und bestätigen mit Ok.\n");
    	builder.append("\n2) Erstellen eines neuen Prozesses\n\n");
    	builder.append("\tZum erstellen eines neuen Planes klicken Sie bitte auf das Dreieck nebem dem Plus und wählen Sie \"neuen Prozess erstellen\"\n");
    	builder.append("\tIn dem Reiter\"Allgemein\" können Sie eine ProzessID, die Prozessdauer sowie den Namen des Prozesses festlegen.\n");
    	builder.append("\tIn dem Reiter\"Vorgänger\" können Sie einen Vorgängerprozess definieren, wählen Sie in dem Dropdown den gewünschten Prozess aus.\n");
    	builder.append("\tund fügen diesen mit \"Hinzufügen\" hinzu, mittels \"Löschen\" können Sie einen Vörgänger löschen.\n");
    	builder.append("\n3) Speichern eines Planes\n\n");
    	builder.append("\tZum speichern eines Planes klicken Sie bitte auf das Symbol mit dem Pfeil nach unten.\n");
    	builder.append("\tWählen Sie einen validen Speicherort aus und drücken Sie \"Speichern\" um zu speichern.\n");
    	builder.append("\tAchtung gespeicherte Pläne können nur mit der gleichen Version des ProPlan geöffnet werden!\n");
    	builder.append("\n4) Laden eines Planes\n\n");
    	builder.append("\tZum laden eines Planes klicken Sie bitte auf das Symbol mit dem Pfeil nach oben.\n");
    	builder.append("\tWählen Sie einen validen Speicherort aus und drücken Sie \"Öffnen\" um zu speichern.\n");
    	builder.append("\n5) Löschen eines Prozesses\n\n");
    	builder.append("\tZum löschen eines Prozesses klicken Sie bitte auf das Symbol des Mülleimers.\n");
    	builder.append("\tNavigieren Sie mit der Maus zu dem zu löschendem Prozess und klicken auf dieses.\n");
    	builder.append("\n6) Editieren eines Prozesses\n\n");
    	builder.append("\tZum editieren eines Prozesses klicken Sie bitte auf das Symbol des Stiftes.\n");
    	builder.append("\tNavigieren Sie mit der Maus zu dem zu editieren Prozess und klicken auf dieses.\n");
    	builder.append("\tBearbeiten Sie den Prozess wie bei Punkt 2.\n");
    	builder.append("\tAlternativ können Sie auch einen Doppelklick auf den zu editierende Prozess ausführen.\n");
    	return builder.toString();
    }
    public enum Language {    
        NEW_PLAN_NAME,
        NEW_PLAN_DESCRIPTION,
        SAVE,
        LOAD,
        CANCEL,
        TITEL_NEW_PLAN,
        TITEL_NEW_PROCESS,    
        TITEL_EDIT_PROCESS,
        TITEL_ERROR,
        TITEL_SUCCESS,
        ERROR_PLAN_NAME_LENGTH,
        ALLREADY_A_PARENT,
        ALLREADY_A_CHILD,
        TOOLTIP_LOAD,
        TOOLTIP_SAVE,
        TOOLTIP_DELETE,
        TOOLTIP_EDIT,
        PREDECESSOR,
        GENERAL_SETTING,
        PROCESSNAME,
        PROCESSDURATION,
        PROCESSID,
        HEADERADDPROCESS,

        PLANEXISTS,
        PLANLOADED,
        FILTER_PLAN_FILES,
        UNKNOWN_COMMAND,
        ADD,
        REMOVE,
        SUCCESSFULLY_SAVED,
        
        INFORMATION_SELECTED_PLAN_TITLE,
        INFORMATION_SYSTEM_TITLE,
        INFORMATION_AUTHOR,
        INFORMATION_VERSION,
        INFORMATION_USAGE,
        INFORMATION_USAGE_TEXT,
        INFORMATION_TITLE,
    	
    	LOAD_CHILD_NOT_FOUND,
    	PROCESS_NOT_FOUND,
    	LOAD_PARENT_NOT_FOUND,
    	UNSAFED_CHANGES_TITLE,
    	UNSAFED_CHANGES,
    	UNSAFED_CHANGES_REMOVE,
    	ERROR_PROCESS_DURATION,
    	ERROR_PROCESS_ID,
    	ERROR_PROCESS_NAME_LENGTH,
    	
    	LANGUAGE_FILE_CHANGED,
    	PLANNOTFOUND,
    	NO_END_DEFINED,
    	FILTER_PLAN_FILES_SECOND;
    }
}

