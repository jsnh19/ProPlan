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
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import me.zero.tls.proplan.components.Plan;
import me.zero.tls.proplan.components.Process;
import me.zero.tls.proplan.gui.view.MessageUtil;
import me.zero.tls.proplan.gui.view.ProPlanUI;
import me.zero.tls.proplan.lang.Lang;
import me.zero.tls.proplan.lang.Lang.Language;

/**
 *
 * @author Julius.Schoenhut
 */
public class EditController implements ActionListener{

	//Plan which should be edited
    private Plan plan;
    //Process which will be edited
    private Process node;
    
    //Dimension of the JoptionPane Window
    //backup to restore default size
    private Dimension preDimension;
    
    //a Guilist of parents 
    private JList<String> parents;
    //A Textfield containing the duration of the edited Process
    private JFormattedTextField dauer_jtf;
  //A Textfield containing the ID of the edited Process
    private JFormattedTextField id_jtf;
  //A Textfield containing the name of the edited Process
    private JTextField processname_jtf;
    //Auswahl ist Ende
    JCheckBox checkbox = new JCheckBox();
    
    public EditController(Plan plan,Process node){
        this.plan = plan;
        this.node = node;
    }
    public EditController(){}
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e == null){
            preDimension = (Dimension)UIManager.get("OptionPane.minimumSize");
            UIManager.put("OptionPane.minimumSize",new Dimension(500,500)); 
            int returnvalue = JOptionPane.showConfirmDialog(null,
                        createNewProcessUI(),
                        Lang.getLanguageValue(Language.TITEL_EDIT_PROCESS),
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);
           //createNewPlanUI().setVisible(true);
           if(returnvalue == 0){
                if(parents != null && processname_jtf != null && !processname_jtf.getText().isEmpty()&&
                       id_jtf != null && !id_jtf.getText().isEmpty() &&
                               dauer_jtf != null && !dauer_jtf.getText().isEmpty()){
                   
                   node.setDauer(Double.valueOf(dauer_jtf.getText()));
                   node.setName(processname_jtf.getText());
                   node.setID(Integer.valueOf(id_jtf.getText()));
                   
                   //Update die Eltern & Kinder
                  /* node.clearPredecessor();
                   node.clearSuccessor();
                   for(int i = 0; i < parents.getModel().getSize();i++){
                	   String id = parents.getModel().getElementAt(i).toString().split(";")[1].split("\\(")[1].split("\\)")[0];
                	   System.out.println("adding " + id + " as parent of " + node.getName());
                	   Process editedNode = plan.getNodeByID(Integer.parseInt(id));
                	   //Füge parent für diesen Knoten hinzu
                	   node.addPredecessor(editedNode);
                	   //Füge child für diesen Knoten hinzu
                	   editedNode.addSuccessor(node);
                   }
                   plan.calcData();
                   plan.repaint();    */           
               }   
                System.out.println("setting isEnd = " + checkbox.isSelected() + " for " + node.getName());
                System.out.println(node.isEnd);
                Process previousEnd = getEndNode();
                
                //Falls die gleichen Knoten und ende wegnehmen
                
                if(previousEnd == node) {
                	if(previousEnd.isEnd && !checkbox.isSelected()) {
                		MessageUtil.showInformationMessage(Lang.getLanguageValue(Language.INFORMATION_TITLE), Lang.getLanguageValue(Language.NO_END_DEFINED));   
                	}
                }
                node.isEnd = checkbox.isSelected();
                
                plan.calcData();
                plan.repaint();
                
                UIManager.put("OptionPane.minimumSize",preDimension); 
                //JOptionPane.showMessageDialog(null, Lang.getLanguageValue(Language.ERROR_PLAN_NAME_LENGTH),Lang.getLanguageValue(Language.TITEL_ERROR),JOptionPane.PLAIN_MESSAGE);
           }
           UIManager.put("OptionPane.minimumSize",preDimension); 
           ProPlanUI.mode = -1;
        }else{
            ProPlanUI.mode = 0;
        }
    }
    
    public Process getEndNode() {
    	for(Process pro : plan.nodes) {
    		if(pro.isEnd) {
    			return pro;
    		}
    	}
    	return null;
    }
    /**
     * Creates the Tab Header of the edit Panel
     * @return {@link JPanel}
     */
    public JPanel createNewProcessUI(){
        
        JTabbedPane tab = new JTabbedPane();
        tab.setSize(500,500);
        tab.addTab(Lang.getLanguageValue(Language.GENERAL_SETTING), createGeneralSettings());
        tab.addTab(Lang.getLanguageValue(Language.PREDECESSOR), createTabParents());
        
        JPanel mainPanel = new JPanel(new BorderLayout());        
                
        mainPanel.setSize(500,500);
        mainPanel.add(tab,BorderLayout.CENTER);        
        return mainPanel;
    }
    /**
     * Creates the general setting panel for the tab system
     * @return {@link JPanel}
     */
    private JPanel createGeneralSettings(){
        JPanel general = new JPanel(new GridLayout(0,2));
        
        JLabel name_jl = new JLabel(Lang.getLanguageValue(Language.PROCESSNAME));        
        JLabel dauer_jl = new JLabel(Lang.getLanguageValue(Language.PROCESSDURATION));
        JLabel id_jl = new JLabel(Lang.getLanguageValue(Language.PROCESSID));
        
        dauer_jtf = new JFormattedTextField(NumberFormat.getNumberInstance());
        dauer_jtf.setText(node.getDauer()+"");
        id_jtf = new JFormattedTextField(NumberFormat.getNumberInstance());
        processname_jtf = new JTextField();
        processname_jtf.setText(node.getName());
        id_jtf.setText(node.getID()+"");
        general.add(id_jl);
        general.add(id_jtf);
        general.add(name_jl);
        general.add(processname_jtf);
        general.add(dauer_jl);
        general.add(dauer_jtf);
        checkbox.setText("als Ende definieren ?");        
        checkbox.setSelected(node.isEnd);
        general.add(checkbox);
        
        return general;
    }
    /**
     * Create the parents panel for the tab system
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private JPanel createTabParents(){
        JPanel addChilds = new JPanel(new BorderLayout());  
        JPanel addProcess = new JPanel(new BorderLayout());  
        JLabel header = new JLabel(Lang.getLanguageValue(Language.HEADERADDPROCESS));
        JComboBox<String> processes = new JComboBox<String>();
        for(Process pro : plan.nodes){
            processes.addItem(pro.getName() + "; (" + pro.getID() + ")");
        }
        addProcess.add(header,BorderLayout.NORTH);
        addProcess.add(processes,BorderLayout.CENTER);
        JButton add = new JButton(Lang.getLanguageValue(Language.ADD));
        DefaultListModel<String> module = new DefaultListModel<String>();
        for(Process p : node.getPredecessor()){
            module.addElement(p.getName() + "; (" + p.getID() + ")");
        }
        add.addActionListener((e)->{
            module.addElement(processes.getSelectedItem()+"");
            // Node updaten neuen Kind Prozess hinzufügen, sowie Parent des Kindes
            String id = processes.getSelectedItem().toString().split(";")[1].split("\\(")[1].split("\\)")[0];
            Process toaddProcess = plan.getNodeByID(Integer.parseInt(id));
            System.out.println("add " + toaddProcess + " (" + id + ")");
            node.addPredecessor(toaddProcess);
            toaddProcess.addSuccessor(node);
        });
        addProcess.add(add,BorderLayout.SOUTH);
                
        addChilds.add(addProcess,BorderLayout.NORTH);
        
        JPanel showSelectedProcesses = new JPanel(new BorderLayout());
        parents = new JList(module);
        
        showSelectedProcesses.add(parents,BorderLayout.CENTER);
        JButton delete = new JButton(Lang.getLanguageValue(Language.REMOVE));
        delete.addActionListener((e)-> {
            if(parents.getSelectedIndex() >= 0){          
               String id = parents.getModel().getElementAt(parents.getSelectedIndex()).toString().split(";")[1].split("\\(")[1].split("\\)")[0];
               Process removedProcess = plan.getNodeByID(Integer.parseInt(id));
               System.out.println("removing " + removedProcess + " (" + id + ")");
               node.removePredecessor(removedProcess);
               removedProcess.removeSuccessor(node);
               
               module.remove(parents.getSelectedIndex());      
            }
        });
        showSelectedProcesses.add(delete,BorderLayout.SOUTH);
        addChilds.add(showSelectedProcesses,BorderLayout.CENTER);
        addChilds.setBackground(Color.red);
        return addChilds;
    }   
}

//Node updaten Kind entfernen und parent des Kindes
//Update die Eltern & Kinder
/* node.clearPredecessor();
 node.clearSuccessor();
 for(int i = 0; i < parents.getModel().getSize();i++){
	   String id = parents.getModel().getElementAt(i).toString().split(";")[1].split("\\(")[1].split("\\)")[0];
	   System.out.println("adding " + id + " as parent of " + node.getName());
	   Process editedNode = plan.getNodeByID(Integer.parseInt(id));
	   //Füge parent für diesen Knoten hinzu
	   node.addPredecessor(editedNode);
	   //Füge child für diesen Knoten hinzu
	   editedNode.addSuccessor(node);
 }
 plan.calcData();
 plan.repaint();    */