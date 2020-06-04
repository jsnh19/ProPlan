/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zero.tls.proplan.gui.components;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import me.zero.tls.proplan.gui.controller.DeleteController;
import me.zero.tls.proplan.gui.controller.EditController;
import me.zero.tls.proplan.gui.controller.InfoController;
import me.zero.tls.proplan.gui.controller.LadePlanController;
import me.zero.tls.proplan.gui.controller.NeuerPlanController;
import me.zero.tls.proplan.gui.controller.SpeicherPlanController;
import me.zero.tls.proplan.gui.util.GraphicsUtil;
import me.zero.tls.proplan.lang.Lang;
import me.zero.tls.proplan.lang.Lang.Language;

import org.openide.awt.DropDownButtonFactory;

/**
 *
 * @author Julius.Schoenhut
 */
public class ProPlanToolBar extends JPanel {

	// Used by Serializable
	private static final long serialVersionUID = 1149371853881787524L;
	private NeuerPlanController neuerplancontroller = new NeuerPlanController();

	public ProPlanToolBar() {
		super(new FlowLayout(FlowLayout.LEFT));
		addButtons();
	}
	/**
	 * Adds the Buttons to the Toolbar
	 */
	protected void addButtons() {
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.add(createPopUpNew());
		JButton button;
		button = new JButton();
		button.setIcon(GraphicsUtil.createImageIcon("/ressources/images/file_load2.png"));
		button.setToolTipText(Lang.getLanguageValue(Language.TOOLTIP_LOAD));
		button.addActionListener(new LadePlanController());
		toolbar.add(button);
		button = new JButton();
		button.setToolTipText(Lang.getLanguageValue(Language.TOOLTIP_SAVE));
		button.setIcon(GraphicsUtil.createImageIcon("/ressources/images/file_save2.png"));
		button.addActionListener(new SpeicherPlanController());
		toolbar.add(button);

		button = new JButton();
		button.setToolTipText(Lang.getLanguageValue(Language.TOOLTIP_DELETE));
		button.setIcon(GraphicsUtil.createImageIcon("/ressources/images/trash2.png"));
		button.addActionListener(new DeleteController());
		toolbar.add(button);

		button = new JButton();
		button.setToolTipText(Lang.getLanguageValue(Language.TOOLTIP_EDIT));
		button.setIcon(GraphicsUtil.createImageIcon("/ressources/images/edit2.png"));
		button.addActionListener(new EditController());
		toolbar.add(button);

		button = new JButton();
		button.setToolTipText(Lang.getLanguageValue(Language.TOOLTIP_EDIT));
		button.setIcon(GraphicsUtil.createImageIcon("/ressources/images/questionmark2.png"));
		button.addActionListener(new InfoController());
		toolbar.add(button);

		this.add(toolbar);
	}
	/**
	 * Creates a JComponent to be used as a dropdown button
	 * @return {@link JComponent}
	 */
	private JComponent createPopUpNew() {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem menuItemNeuerPlan = new JMenuItem(Lang.getLanguageValue(Language.TITEL_NEW_PLAN));
		menuItemNeuerPlan.addActionListener(neuerplancontroller);
		popupMenu.add(menuItemNeuerPlan);
		JMenuItem menuItemCreateNeuerProzess = new JMenuItem(Lang.getLanguageValue(Language.TITEL_NEW_PROCESS));
		menuItemCreateNeuerProzess.addActionListener(neuerplancontroller);
		popupMenu.add(menuItemCreateNeuerProzess);
		JButton dropDownButton = DropDownButtonFactory
				.createDropDownButton(GraphicsUtil.createImageIcon("/ressources/images/plus2.png"), popupMenu);
		dropDownButton.addActionListener(neuerplancontroller);
		return dropDownButton;
	}
}
