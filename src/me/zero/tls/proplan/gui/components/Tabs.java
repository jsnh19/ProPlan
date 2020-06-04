/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zero.tls.proplan.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import me.zero.tls.proplan.components.Plan;
import me.zero.tls.proplan.components.Process;
import me.zero.tls.proplan.gui.util.GraphicsUtil;
import me.zero.tls.proplan.gui.view.ProPlanUI;
import me.zero.tls.proplan.lang.Lang;
import me.zero.tls.proplan.lang.Lang.Language;
import me.zero.tls.proplan.settings.Settings;

/**
 *
 * @author Julius.Schoenhut
 */
public final class Tabs extends JPanel {

	//Used by serializable
	private static final long serialVersionUID = -4357073224068281510L;
	//Tab class which holds every Tab
	public JTabbedPane tabbedPane;
	//Hashmap of all plans by name
	public final HashMap<String, Plan> tabToPanel = new HashMap<>();

	public Tabs() {
		super(new GridLayout(1, 1));

		tabbedPane = new JTabbedPane();
		
		if((boolean) Settings.instance.settings.get(Settings.SETTINGS_KEY.LOADEXAMPLE.name())) {
			addNewTab("Beispiel #1", getFreeKeyEvent(), "Example Tab");

			Process start = new Process(0, "Start", 100, 200, 0,false);
			Process A1 = new Process(1, "A1", 300, 200, 1,false);
			Process B1 = new Process(1, "B1", 500, 100, 2,false);
			Process B2 = new Process(3, "B2", 500, 300, 3,false);
			Process end = new Process(0, "END", 700, 200, 4,true);

			start.addSuccessor(A1);
			A1.addPredecessor(start);
			A1.addSuccessor(B1);
			A1.addSuccessor(B2);
			B1.addPredecessor(A1);
			B1.addSuccessor(end);
			B2.addPredecessor(A1);
			B2.addSuccessor(end);
			end.addPredecessor(B1);
			end.addPredecessor(B2);

			// System.exit(0);
			tabToPanel.get("Beispiel #1").addNode(start);
			tabToPanel.get("Beispiel #1").addNode(A1);
			tabToPanel.get("Beispiel #1").addNode(B1);
			tabToPanel.get("Beispiel #1").addNode(B2);
			tabToPanel.get("Beispiel #1").addNode(end);

			// Add the tabbed pane to this panel.
		}
		add(tabbedPane);
		// The following line enables to use scrolling tabs.
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	/**
	 * Returns a free keyevent, if tabcount < 10
	 * @return {@link KeyEvent}
	 */
	private int getFreeKeyEvent() {
		switch (tabbedPane.getTabCount()) {
		case 0:
			return KeyEvent.VK_0;
		case 1:
			return KeyEvent.VK_1;
		case 2:
			return KeyEvent.VK_2;
		case 3:
			return KeyEvent.VK_3;
		case 4:
			return KeyEvent.VK_4;
		case 5:
			return KeyEvent.VK_5;
		case 6:
			return KeyEvent.VK_6;
		case 7:
			return KeyEvent.VK_7;
		case 8:
			return KeyEvent.VK_8;
		case 9:
			return KeyEvent.VK_9;
		}
		return -1;
	}
	/**
	 * Adds a new Plan-Tab to the tab list
	 * @param name {@link String}
	 * @param keyEvent {@link KeyEvent}
	 * @param beschreibung {@link String}
	 */
	public void addNewTab(String name, int keyEvent, String beschreibung) {
		ImageIcon icon = GraphicsUtil.createImageIcon("/ressources/images/map2.png");
		Plan panel1 = makePanel(name, beschreibung);
		panel1.setSize(500, 500);
		// panel1.setBackground(Color.orange);
		tabToPanel.put(name, panel1);
		tabbedPane.addTab(name, icon, panel1);
		tabbedPane.setMnemonicAt(0, keyEvent);
		
		tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1, createCloseAbleTabTitle(name,panel1));
	}
	/**
	 * Adds a new Plan-Tab to the tab list
	 * @param name {@link String}
	 * @param beschreibung {@link String}
	 */
	public void addNewTabWithoutKey(String name, String beschreibung) {
		ImageIcon icon = GraphicsUtil.createImageIcon("/ressources/images/map2.png");
		Plan panel1 = makePanel(name, beschreibung);
		tabToPanel.put(name, panel1);
		tabbedPane.addTab(name, icon, panel1);

		tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1, createCloseAbleTabTitle(name,panel1));
	}
	/**
	 * Adds an existing Plan-Tab to the plan List
	 * @param plan Der Plan für den neuen Tab
	 */
	public void addTab(Plan plan) {
		ImageIcon icon = GraphicsUtil.createImageIcon("/ressources/images/map2.png");
		tabToPanel.put(plan.getName(), plan);
		tabbedPane.addTab(plan.getName(), icon, plan);
		

		tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1, createCloseAbleTabTitle(plan.getName(),plan));
	}
	/**
	 * Adds a new Plan-Tab to the tab list, generates automaticly a keyevent if possible
	 * @param name {@link String}
	 * @param beschreibung {@link String}
	 */
	public void addNewTab(String name, String beschreibung) {
		int eventKey = getFreeKeyEvent();
		if (eventKey != -1) {
			addNewTab(name, eventKey, beschreibung);
		} else {
			addNewTabWithoutKey(name, beschreibung);
		}
	}
	/**
	 * Builds the Panel
	 * @param name {@link String}
	 * @param beschreibung {@link String}
	 */
	protected Plan makePanel(String name, String beschreibung) {
		// JPanel panel = new JPanel(false);
		Plan panel = new Plan(name, beschreibung);
		panel.setLayout(null);
		panel.setBackground(Color.red);
		panel.setSize(this.getSize().width, this.getSize().height);
		return panel;
	}
	
	public JPanel createCloseAbleTabTitle(String tabTitle,Plan p) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setFocusable(false);
		//panel.setLayout(null);		
		//panel.setLayout(new BorderLayout());
		JLabel title = new JLabel(tabTitle);
		title.setFocusable(false);
		panel.add(title,BorderLayout.CENTER);
		title.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		
		p.setTabTitle(title);
		
		JButton close = new JButton("X");
		close.setBounds(5, 5, 5, 5);
		close.addActionListener((action) -> {
			if(ProPlanUI.tabs.tabToPanel.containsKey(tabTitle)){	
				//Plan p = ProPlanUI.tabs.tabToPanel.get(tabTitle);
				if(p.changed) {
					int confirm = JOptionPane.showOptionDialog(null, Lang.getLanguageValue(Language.UNSAFED_CHANGES_REMOVE).replace("%plan%", tabTitle), 
                            Lang.getLanguageValue(Language.UNSAFED_CHANGES_TITLE), JOptionPane.YES_NO_OPTION, 
                            JOptionPane.QUESTION_MESSAGE, null, null, null);
                    if (confirm == 0) {
                    	ProPlanUI.tabs.tabToPanel.remove(tabTitle);
       					ProPlanUI.tabs.tabbedPane.remove(p);
                    }
				}else {
					ProPlanUI.tabs.tabToPanel.remove(tabTitle);
					ProPlanUI.tabs.tabbedPane.remove(p);
				}				
			}
		});
		close.setBorderPainted(false);
		close.setContentAreaFilled(false);
		panel.add(close,BorderLayout.EAST);
		
		return panel;
	}
}
