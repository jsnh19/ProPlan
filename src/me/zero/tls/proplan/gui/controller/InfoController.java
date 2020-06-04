package me.zero.tls.proplan.gui.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import me.zero.tls.proplan.ProPlan;
import me.zero.tls.proplan.components.Plan;
import me.zero.tls.proplan.gui.view.ProPlanUI;
import me.zero.tls.proplan.lang.Lang;
import me.zero.tls.proplan.lang.Lang.Language;

public class InfoController implements ActionListener {

	// Used to store the previous Size of JoptionPane
	private Dimension preDimension;

	@Override
	public void actionPerformed(ActionEvent e) {
		preDimension = (Dimension) UIManager.get("OptionPane.minimumSize");
		UIManager.put("OptionPane.minimumSize", new Dimension(500, 500));

		JOptionPane.showConfirmDialog(null, createNewInfoUI(), Lang.getLanguageValue(Language.INFORMATION_TITLE),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		UIManager.put("OptionPane.minimumSize", preDimension);

	}
	/**
	 * Creates a new TabbedPane with info tabs
	 * @return
	 */
	private JPanel createNewInfoUI() {
		JTabbedPane tab = new JTabbedPane();
		tab.setSize(500, 500);
		tab.addTab(Lang.getLanguageValue(Language.INFORMATION_SELECTED_PLAN_TITLE), createPlanInfoPanel());
		tab.addTab(Lang.getLanguageValue(Language.INFORMATION_SYSTEM_TITLE), createProPlanInfoPanel());

		JPanel mainPanel = new JPanel(new BorderLayout());

		mainPanel.setSize(500, 500);
		mainPanel.add(tab, BorderLayout.CENTER);
		return mainPanel;
	}
	/**
	 * Creates a JPanel with information about the Plan
	 * @return
	 */
	private JPanel createPlanInfoPanel() {
		JPanel mainpanel = new JPanel(new BorderLayout());
		Plan plan = ProPlanUI.tabs.tabToPanel
				.get(ProPlanUI.tabs.tabbedPane.getTitleAt(ProPlanUI.tabs.tabbedPane.getSelectedIndex()));
		// Namensanzeige
		JPanel namepanel = new JPanel(new BorderLayout());
		JLabel label_name_plan = new JLabel(Lang.getLanguageValue(Language.NEW_PLAN_NAME));
		JLabel name_plan = new JLabel(plan.getName());
		namepanel.add(label_name_plan, BorderLayout.WEST);
		namepanel.add(name_plan, BorderLayout.EAST);
		namepanel.add(new JSeparator(), BorderLayout.SOUTH);
		mainpanel.add(namepanel, BorderLayout.NORTH);

		// Beschreibungsanzeige
		JPanel descriptionpanel = new JPanel(new BorderLayout());
		JLabel label_description_plan = new JLabel(Lang.getLanguageValue(Language.NEW_PLAN_DESCRIPTION));
		JTextField description = new JTextField();
		description.setText(plan.getDescription());
		description.setEditable(false);

		descriptionpanel.add(label_description_plan, BorderLayout.NORTH);
		descriptionpanel.add(description, BorderLayout.CENTER);
		mainpanel.add(descriptionpanel, BorderLayout.CENTER);

		return mainpanel;
	}
	/**
	 * Create an informationpanel containing diffrent informations
	 * @return
	 */
	private JPanel createProPlanInfoPanel() {
		JPanel mainpanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// Authoren anzeige
		JPanel namepanel = new JPanel(new BorderLayout());
		JLabel label_name_plan = new JLabel(Lang.getLanguageValue(Language.INFORMATION_AUTHOR));
		JLabel author_proplan = new JLabel(ProPlan.getAuthor());
		namepanel.add(label_name_plan, BorderLayout.WEST);
		namepanel.add(author_proplan, BorderLayout.EAST);
		namepanel.add(new JSeparator(), BorderLayout.SOUTH);
		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		mainpanel.add(namepanel, c);

		// Versions anzeige
		JPanel versionpanel = new JPanel(new BorderLayout());
		JLabel label_version_plan = new JLabel(Lang.getLanguageValue(Language.INFORMATION_VERSION));
		JLabel version_proplan = new JLabel(ProPlan.getVersion());
		versionpanel.add(label_version_plan, BorderLayout.WEST);
		versionpanel.add(version_proplan, BorderLayout.EAST);
		versionpanel.add(new JSeparator(), BorderLayout.SOUTH);
		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		mainpanel.add(versionpanel, c);

		// Benutzungsanzeige anzeige

		JPanel descriptionpanel = new JPanel(new BorderLayout());
		JLabel label_description_plan = new JLabel(Lang.getLanguageValue(Language.INFORMATION_USAGE));
		JTextArea description = new JTextArea();
		description.setSize(description.getSize().width, 100);
		JScrollPane spane = new JScrollPane(description);
		spane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		description.setText(Lang.getLanguageValue(Language.INFORMATION_USAGE_TEXT));
		description.setEditable(false);

		descriptionpanel.add(label_description_plan, BorderLayout.NORTH);
		descriptionpanel.add(spane, BorderLayout.CENTER);
		namepanel.setMaximumSize(new Dimension(namepanel.getSize().width, 10));
		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		mainpanel.add(descriptionpanel, c);

		return mainpanel;
	}

}
