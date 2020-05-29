/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zero.tls.proplan.gui.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import me.zero.tls.proplan.components.Plan;
import me.zero.tls.proplan.gui.view.MessageUtil;
import me.zero.tls.proplan.gui.view.ProPlanUI;
import me.zero.tls.proplan.lang.Lang;
import me.zero.tls.proplan.lang.Lang.Language;
import me.zero.tls.proplan.components.Process;

/**
 *
 * @author Julius.Schoenhut
 */
public class NeuerPlanController implements ActionListener{

    //Fields for createNewPlan
	//Name Textfield
    private JTextField name_jtf;
    //Description Textfield
    private JTextArea beschreibung;    
    //Stored Dimension  of joptionpane
    private Dimension preDimension;
       
    //Fields for createNewProcess
    //Gui List with all Parents
    private JList<String> parents;
    //Duration textfield 
    private JFormattedTextField dauer_jtf;
    //Id textfield
    private JFormattedTextField id_jtf;
    //Name textfield
    private JTextField processname_jtf;
    //Auswahl ist Ende
    JCheckBox checkbox = new JCheckBox();
        
    //Store last actioncommand to execute it automaticly if needed
    private String lastUsedActionCommand = null;
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String issuedCommand = e.getActionCommand();
        if(e.getActionCommand() == null && lastUsedActionCommand != null){
            issuedCommand = lastUsedActionCommand;
        }else if(e.getActionCommand() == null){
            return;
        }
        if(issuedCommand.equalsIgnoreCase(Lang.getLanguageValue(Language.TITEL_NEW_PROCESS))){
        	
        	if(ProPlanUI.tabs.tabbedPane.getSelectedIndex() < 0) {
        		MessageUtil.showErrorMessage(Lang.getLanguageValue(Language.TITEL_ERROR), Lang.getLanguageValue(Language.PLANNOTFOUND));
        		return;
        	}
        	
           preDimension = (Dimension)UIManager.get("OptionPane.minimumSize");
           UIManager.put("OptionPane.minimumSize",new Dimension(500,500)); 
           int returnvalue = JOptionPane.showConfirmDialog(null,
                        createNewProcessUI(),
                        Lang.getLanguageValue(Language.TITEL_NEW_PROCESS),
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);
           //createNewPlanUI().setVisible(true);

           if(returnvalue == 0){
        	   
        	   
        	   if(parents != null && processname_jtf != null && !processname_jtf.getText().isEmpty() && !existsName(processname_jtf.getText())) {
        		   if(id_jtf != null && !id_jtf.getText().isEmpty() && isNumeric(id_jtf.getText()) && !existsID(Integer.parseInt(id_jtf.getText())) ) {
        			   if(dauer_jtf != null && !dauer_jtf.getText().isEmpty() && isNumeric(dauer_jtf.getText())) {
        	               Plan plan = ProPlanUI.tabs.tabToPanel.get(ProPlanUI.tabs.tabbedPane.getTitleAt(ProPlanUI.tabs.tabbedPane.getSelectedIndex()));
                           Process proc = new Process(Double.valueOf(dauer_jtf.getText()), processname_jtf.getText(), 0, 0, Integer.valueOf(id_jtf.getText()), checkbox.isSelected());
                           for(int i = 0; i < parents.getModel().getSize();i++){                               
                        	   int id = Integer.parseInt(parents.getModel().getElementAt(i).toString().split(";")[1].split("\\(")[1].split("\\)")[0] );
                        	   
                        	   Process newdNode = plan.getNodeByID(id);
                        	   proc.addPredecessor(newdNode);
                        	   newdNode.addSuccessor(proc);
                           }    
                           plan.addNode(proc);
                           UIManager.put("OptionPane.minimumSize",preDimension); 
                       }else {
                    	   //Dauer ist leer oder keine Zahl
                		   UIManager.put("OptionPane.minimumSize",preDimension); 
                           MessageUtil.showErrorMessage(Lang.getLanguageValue(Language.TITEL_ERROR), Lang.getLanguageValue(Language.ERROR_PROCESS_DURATION));
                	   } 
            	   }else {
            		   UIManager.put("OptionPane.minimumSize",preDimension); 
                       MessageUtil.showErrorMessage(Lang.getLanguageValue(Language.TITEL_ERROR), Lang.getLanguageValue(Language.ERROR_PROCESS_ID));
            	   } 
        	   }else {
        		   UIManager.put("OptionPane.minimumSize",preDimension); 
                   MessageUtil.showErrorMessage(Lang.getLanguageValue(Language.TITEL_ERROR), Lang.getLanguageValue(Language.ERROR_PLAN_NAME_LENGTH));
        	   }                 
           }
        }else if(issuedCommand.equalsIgnoreCase(Lang.getLanguageValue(Language.TITEL_NEW_PLAN))){
            preDimension = (Dimension)UIManager.get("OptionPane.minimumSize");
            UIManager.put("OptionPane.minimumSize",new Dimension(500,500)); 
            int returnvalue = JOptionPane.showConfirmDialog(null,
                        createNewPlanUI(),
                        Lang.getLanguageValue(Language.TITEL_NEW_PLAN),
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);
           //createNewPlanUI().setVisible(true);
           if(returnvalue == 0){               
               if(name_jtf != null && !name_jtf.getText().isEmpty()){
                   ProPlanUI.tabs.addNewTab(name_jtf.getText(),beschreibung.getText());
                   UIManager.put("OptionPane.minimumSize",preDimension); 
               }else{
                   UIManager.put("OptionPane.minimumSize",preDimension); 
                   MessageUtil.showErrorMessage(Lang.getLanguageValue(Language.TITEL_ERROR), Lang.getLanguageValue(Language.ERROR_PLAN_NAME_LENGTH));
               }               
           }
        }else{
        	String message = Lang.getLanguageValue(Language.UNKNOWN_COMMAND).replace("%command%", issuedCommand);
            MessageUtil.showErrorMessage(Lang.getLanguageValue(Language.TITEL_ERROR), message);
        }

        UIManager.put("OptionPane.minimumSize",preDimension); 
        lastUsedActionCommand = e.getActionCommand();
    }
    /**
     * Generates the New Process Tab System
     * @return a JPanel including all Tabs to create a new Process
     */
    public JPanel createNewProcessUI(){        
        JTabbedPane tab = new JTabbedPane();
        tab.setSize(500,500);
        tab.addTab(Lang.getLanguageValue(Language.GENERAL_SETTING), createGeneralSettings());
        tab.addTab(Lang.getLanguageValue(Language.PREDECESSOR), createTabParents());
        //tab.addTab("Nachfolger", createTabChilds());        
        JPanel mainPanel = new JPanel(new BorderLayout());   
        mainPanel.setSize(500,500);
        mainPanel.add(tab,BorderLayout.CENTER);        
        return mainPanel;
    }
    /**
     * Create the general Panel
     * @return a JPanel including the Content of the General Tab
     */
    private JPanel createGeneralSettings(){
        JPanel general = new JPanel(new GridLayout(0,2));
        
        JLabel name_jl = new JLabel(Lang.getLanguageValue(Language.PROCESSNAME));        
        JLabel dauer_jl = new JLabel(Lang.getLanguageValue(Language.PROCESSDURATION));
        JLabel id_jl = new JLabel(Lang.getLanguageValue(Language.PROCESSID));
        
        dauer_jtf = new JFormattedTextField(NumberFormat.getNumberInstance());
        id_jtf = new JFormattedTextField(NumberFormat.getNumberInstance());
        processname_jtf = new JTextField();        
        Plan plan = ProPlanUI.tabs.tabToPanel.get(ProPlanUI.tabs.tabbedPane.getTitleAt(ProPlanUI.tabs.tabbedPane.getSelectedIndex()));
        id_jtf.setText(plan.nodes.size()+"");
        general.add(id_jl);
        general.add(id_jtf);
        general.add(name_jl);
        general.add(processname_jtf);
        general.add(dauer_jl);
        general.add(dauer_jtf);
        checkbox.setText("als Ende definieren ?");        
        general.add(checkbox);
        
        return general;
    }
    /**
     * Create the Parents Panel
     * @return a JPanel including all Tabs to add Parents
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private JPanel createTabParents(){
        JPanel addChilds = new JPanel(new BorderLayout());  
        JPanel addProcess = new JPanel(new BorderLayout());  
        JLabel header = new JLabel(Lang.getLanguageValue(Language.HEADERADDPROCESS));
        JComboBox processes = new JComboBox();
        Plan plan = ProPlanUI.tabs.tabToPanel.get(ProPlanUI.tabs.tabbedPane.getTitleAt(ProPlanUI.tabs.tabbedPane.getSelectedIndex()));
        for(Process pro : plan.nodes){
            processes.addItem(pro.getName() + "; (" + pro.getID() + ")");
        }
        addProcess.add(header,BorderLayout.NORTH);
        addProcess.add(processes,BorderLayout.CENTER);
        JButton add = new JButton(Lang.getLanguageValue(Language.ADD));
        DefaultListModel module = new DefaultListModel();
        add.addActionListener((e)->{
            module.addElement(processes.getSelectedItem());
        });
        addProcess.add(add,BorderLayout.SOUTH);
                
        addChilds.add(addProcess,BorderLayout.NORTH);
        
        JPanel showSelectedProcesses = new JPanel(new BorderLayout());
        parents = new JList(module);
        
        showSelectedProcesses.add(parents,BorderLayout.CENTER);
        JButton delete = new JButton(Lang.getLanguageValue(Language.REMOVE));
        delete.addActionListener((e)-> {
            if(parents.getSelectedIndex() >= 0){
               module.remove(parents.getSelectedIndex()); 
            }
        });
        showSelectedProcesses.add(delete,BorderLayout.SOUTH);
        addChilds.add(showSelectedProcesses,BorderLayout.CENTER);
        addChilds.setBackground(Color.red);
        return addChilds;
    }    
    /**
     * Creates a JoptionPane which asks for a plan name and description
     * @return a JPanel which asks for a plan name and description
     */
    public JPanel createNewPlanUI(){        
        JPanel mainPanel = new JPanel();        
        mainPanel.setLayout(new BorderLayout());        
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BorderLayout());
        JLabel name_l = new JLabel(Lang.getLanguageValue(Language.NEW_PLAN_NAME));
        name_jtf = new JTextField();      
        namePanel.add(name_l,BorderLayout.NORTH);
        namePanel.add(name_jtf,BorderLayout.CENTER);        
        mainPanel.add(namePanel,BorderLayout.NORTH);
        mainPanel.add(new JSeparator(JSeparator.CENTER));
        
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BorderLayout());
        JLabel description_l = new JLabel(Lang.getLanguageValue(Language.NEW_PLAN_DESCRIPTION));
        beschreibung = new JTextArea();
        descriptionPanel.add(description_l,BorderLayout.NORTH);
        descriptionPanel.add(beschreibung,BorderLayout.CENTER);  
        mainPanel.add(descriptionPanel,BorderLayout.CENTER);
        return mainPanel;
    }
    
    private boolean isNumeric(String txt) {
    	try {
    		Integer.parseInt(txt);
    		return true;
    	}catch(NumberFormatException e) {
    		return false;
    	}
    }
    public boolean existsID(int id) { 
    	Plan plan = ProPlanUI.tabs.tabToPanel.get(ProPlanUI.tabs.tabbedPane.getTitleAt(ProPlanUI.tabs.tabbedPane.getSelectedIndex()));
    
    	for(Process process : plan.nodes) {
    		if(process.getID() == id) {
    			return true;
    		}
    	}
    	return false;
    }
    public boolean existsName(String name) { 
    	Plan plan = ProPlanUI.tabs.tabToPanel.get(ProPlanUI.tabs.tabbedPane.getTitleAt(ProPlanUI.tabs.tabbedPane.getSelectedIndex()));
    
    	for(Process process : plan.nodes) {
    		if(process.getName() == name) {
    			return true;
    		}
    	}
    	return false;
    }
}
